package cn.cy.service.impl;

import cn.cy.entity.ProcessInfo;
import cn.cy.entity.UserCompareInfo;
import cn.cy.entity.UserInfo;
import cn.cy.enums.ErrorCodeEnum;
import cn.cy.factory.FaceEngineFactory;
import cn.cy.rpc.BusinessException;
import cn.cy.service.FaceEngineService;
import cn.cy.util.UserRamCache;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.arcsoft.face.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Service
@Slf4j
public class FaceEngineServiceImpl implements FaceEngineService {

    public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

    @Value("${config.arcface-sdk.app-id}")
    public String appId;

    @Value("${config.arcface-sdk.sdk-key}")
    public String sdkKey;

    @Value("${config.arcface-sdk.detect-pool-size}")
    public Integer detectPooSize;

    @Value("${config.arcface-sdk.compare-pool-size}")
    public Integer comparePooSize;

    private ExecutorService compareExecutorService;

    //通用人脸识别引擎池
    private GenericObjectPool<FaceEngine> faceEngineGeneralPool;

    //人脸比对引擎池
    private GenericObjectPool<FaceEngine> faceEngineComparePool;

    @PostConstruct
    public void init() {


        GenericObjectPoolConfig detectPoolConfig = new GenericObjectPoolConfig();
        detectPoolConfig.setMaxIdle(detectPooSize);
        detectPoolConfig.setMaxTotal(detectPooSize);
        detectPoolConfig.setMinIdle(detectPooSize);
        detectPoolConfig.setLifo(false);
        EngineConfiguration detectCfg = new EngineConfiguration();
        FunctionConfiguration detectFunctionCfg = new FunctionConfiguration();
        detectFunctionCfg.setSupportFaceDetect(true);//开启人脸检测功能
        detectFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        detectFunctionCfg.setSupportAge(true);//开启年龄检测功能
        detectFunctionCfg.setSupportGender(true);//开启性别检测功能
        detectFunctionCfg.setSupportLiveness(true);//开启活体检测功能
        detectCfg.setFunctionConfiguration(detectFunctionCfg);
        detectCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);//图片检测模式，如果是连续帧的视频流图片，那么改成VIDEO模式
        detectCfg.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);//人脸旋转角度
        faceEngineGeneralPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, null,detectCfg), detectPoolConfig);//底层库算法对象池


        //初始化特征比较线程池
        GenericObjectPoolConfig comparePoolConfig = new GenericObjectPoolConfig();
        comparePoolConfig.setMaxIdle(comparePooSize);
        comparePoolConfig.setMaxTotal(comparePooSize);
        comparePoolConfig.setMinIdle(comparePooSize);
        comparePoolConfig.setLifo(false);
        EngineConfiguration compareCfg = new EngineConfiguration();
        FunctionConfiguration compareFunctionCfg = new FunctionConfiguration();
        compareFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        compareCfg.setFunctionConfiguration(compareFunctionCfg);
        compareCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);//图片检测模式，如果是连续帧的视频流图片，那么改成VIDEO模式
        compareCfg.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);//人脸旋转角度
        faceEngineComparePool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, null,compareCfg), comparePoolConfig);//底层库算法对象池
        compareExecutorService = Executors.newFixedThreadPool(comparePooSize);
    }


    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        // 非活体
//        FaceEngine faceEngine = null;
//        try {
//            faceEngine = faceEngineGeneralPool.borrowObject();
//            if (faceEngine == null) {
//                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
//            }
//
//            //人脸检测得到人脸列表
//            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
//            //人脸检测
//            int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
//            if (errorCode == 0) {
//                return faceInfoList;
//            } else {
//                log.error("人脸检测失败，errorCode：" + errorCode);
//            }
//
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            if (faceEngine != null) {
//                //释放引擎对象
//                faceEngineGeneralPool.returnObject(faceEngine);
//            }
//        }
//
//        return null;

        //活体
        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }
            faceEngine.setLivenessParam(0.5f, 0.7f);
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            //人脸检测
            int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            if (errorCode == 0) {
                FunctionConfiguration configuration = new FunctionConfiguration();
                configuration.setSupportAge(true);
                configuration.setSupportGender(true);
                configuration.setSupportLiveness(true);
                List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
                errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, configuration);
                errorCode = faceEngine.getLiveness(livenessInfoList);
                System.out.println("活体：" + livenessInfoList.get(0).getLiveness());
                if (errorCode == 0 && livenessInfoList.get(0).getLiveness() == 1){
                    return faceInfoList;
                }else {
                    log.error("非活体");
                }

            } else {
                log.error("人脸检测失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    @Override
    public Float compareFace(ImageInfo imageInfo1, ImageInfo imageInfo2) {

        List<FaceInfo> faceInfoList1 = detectFaces(imageInfo1);
        List<FaceInfo> faceInfoList2 = detectFaces(imageInfo2);

        if (CollectionUtil.isEmpty(faceInfoList1)) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "照片1未检测到人脸");
        }
        if (CollectionUtil.isEmpty(faceInfoList2)) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "照片2未检测到人脸");
        }

        byte[] feature1 = extractFaceFeature(imageInfo1, faceInfoList1.get(0));
        byte[] feature2 = extractFaceFeature(imageInfo2, faceInfoList2.get(0));

        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }

            FaceFeature faceFeature1 = new FaceFeature();
            faceFeature1.setFeatureData(feature1);
            FaceFeature faceFeature2 = new FaceFeature();
            faceFeature2.setFeatureData(feature2);
            //提取人脸特征
            FaceSimilar faceSimilar = new FaceSimilar();
            int errorCode = faceEngine.compareFaceFeature(faceFeature1, faceFeature2, faceSimilar);
            if (errorCode == 0) {
                return faceSimilar.getScore();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    /**
     * 人脸特征
     *
     * @param imageInfo
     * @return
     */
    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo) {

        FaceEngine faceEngine = null;
        try {
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }

            FaceFeature faceFeature = new FaceFeature();
            //提取人脸特征
            int errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfo, faceFeature);
            if (errorCode == 0) {
                return faceFeature.getFeatureData();
            } else {
                log.error("特征提取失败，errorCode：" + errorCode);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    @Override
    public List<UserCompareInfo> faceRecognition(byte[] faceFeature, List<UserRamCache.UserInfo> userInfoList, float passRate) {
        List<UserCompareInfo> resultUserInfoList = Lists.newLinkedList();//识别到的人脸列表

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);

        List<List<UserRamCache.UserInfo>> faceUserInfoPartList = Lists.partition(userInfoList, 1000);//分成1000一组，多线程处理
        CompletionService<List<UserCompareInfo>> completionService = new ExecutorCompletionService(compareExecutorService);
        for (List<UserRamCache.UserInfo> part : faceUserInfoPartList) {
            completionService.submit(new CompareFaceTask(part, targetFaceFeature, passRate));
        }
        for (int i = 0; i < faceUserInfoPartList.size(); i++) {
            List<UserCompareInfo> faceUserInfoList = null;
            try {
                faceUserInfoList = completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
            }
            if (CollectionUtil.isNotEmpty(userInfoList)) {
                resultUserInfoList.addAll(faceUserInfoList);
            }
        }

        resultUserInfoList.sort((h1, h2) -> h2.getSimilar().compareTo(h1.getSimilar()));//从大到小排序

        return resultUserInfoList;
    }


    @Override
    public List<ProcessInfo> process(ImageInfo imageInfo, List<FaceInfo> faceInfoList) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineGeneralPool.borrowObject();
            if (faceEngine == null) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "获取引擎失败");
            }


            int errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).supportLiveness(true).build());
            if (errorCode == 0) {
                List<ProcessInfo> processInfoList = Lists.newLinkedList();

                //性别列表
                List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
                faceEngine.getGender(genderInfoList);

                //年龄列表
                List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
                faceEngine.getAge(ageInfoList);
                //活体结果列表
                List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
                faceEngine.getLiveness(livenessInfoList);


                for (int i = 0; i < genderInfoList.size(); i++) {
                    ProcessInfo processInfo = new ProcessInfo();
                    processInfo.setGender(genderInfoList.get(i).getGender());
                    processInfo.setAge(ageInfoList.get(i).getAge());
                    processInfo.setLiveness(livenessInfoList.get(i).getLiveness());
                    processInfoList.add(processInfo);
                }
                return processInfoList;

            }


        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineGeneralPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    @Override
    public Boolean register(UserInfo user) {
        try {
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static";
            String absolutePath = FileUtil.getAbsolutePath(path + user.getFaceUrl());
            InputStream inputStream = new FileInputStream(absolutePath);
            ImageInfo rgbData = ImageFactory.getRGBData(inputStream);
            List<FaceInfo> faceInfoList = this.detectFaces(rgbData);
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                byte[] feature = this.extractFaceFeature(rgbData, faceInfoList.get(0));
                UserRamCache.UserInfo userInfo = new UserCompareInfo();
                userInfo.setFaceId(user.getStudentId());
                userInfo.setName(user.getStudentId());
                userInfo.setFaceFeature(feature);
                //这边注册到内存缓存中，也可以根据业务，注册到数据库中
                UserRamCache.addUser(userInfo);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }


    private class CompareFaceTask implements Callable<List<UserCompareInfo>> {

        private List<UserRamCache.UserInfo> userInfoList;
        private FaceFeature targetFaceFeature;
        private float passRate;


        public CompareFaceTask(List<UserRamCache.UserInfo> userInfoList, FaceFeature targetFaceFeature, float passRate) {
            this.userInfoList = userInfoList;
            this.targetFaceFeature = targetFaceFeature;
            this.passRate = passRate;
        }

        @Override
        public List<UserCompareInfo> call() throws Exception {
            FaceEngine faceEngine = null;
            List<UserCompareInfo> resultUserInfoList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = faceEngineComparePool.borrowObject();
                for (UserRamCache.UserInfo userInfo : userInfoList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(userInfo.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    if (faceSimilar.getScore() > passRate) {//相似值大于配置预期，加入到识别到人脸的列表
                        UserCompareInfo info = new UserCompareInfo();
                        info.setName(userInfo.getName());
                        info.setFaceId(userInfo.getFaceId());
                        info.setSimilar(faceSimilar.getScore());
                        resultUserInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (faceEngine != null) {
                    faceEngineComparePool.returnObject(faceEngine);
                }
            }

            return resultUserInfoList;
        }

    }
}
