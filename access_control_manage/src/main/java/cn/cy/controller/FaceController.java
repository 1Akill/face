package cn.cy.controller;

import cn.cy.entity.UserInfo;
import cn.cy.service.UserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import cn.cy.dto.*;
import cn.cy.entity.ProcessInfo;
import cn.cy.entity.UserCompareInfo;
import cn.cy.rpc.Response;
import cn.cy.service.FaceEngineService;
import cn.cy.util.Base64Util;
import cn.cy.util.UserRamCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/user")
public class FaceController {

    @Autowired
    private FaceEngineService faceEngineService;
    @Autowired
    private UserInfoService userInfoService;


    //初始化注册人脸，注册到本地内存
    @PostConstruct
    public void initFace() throws Exception {
        List<UserInfo> list = userInfoService.list();
        for (UserInfo user : list) {
            faceEngineService.register(user);
        }


    }


    /*
    人脸添加
     */
    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    public Response faceAdd(FaceAddReqDTO faceAddReqDTO) {
        String image = faceAddReqDTO.getImage();

        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo);
                if (feature != null) {
                    UserRamCache.UserInfo userInfo = new UserCompareInfo();
                    userInfo.setFaceId(faceAddReqDTO.getName());
                    userInfo.setName(faceAddReqDTO.getName());
                    userInfo.setFaceFeature(feature);
                    //这边注册到内存缓存中，也可以根据业务，注册到数据库中
                    UserRamCache.addUser(userInfo);
                }

            }

        }
        return Response.newSuccessResponse("");
    }


    @RequestMapping(value = "/getFaceList", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<GetFaceListResDTO>> getFaceList() {
        List<UserRamCache.UserInfo> userList = UserRamCache.getUserList();
        List<GetFaceListResDTO> resDTOS = new LinkedList<>();
        for (UserRamCache.UserInfo userInfo : userList) {
            GetFaceListResDTO face = new GetFaceListResDTO();
            face.setId(userInfo.getFaceId());
            face.setName(userInfo.getName());
            face.setUrl("/images/" + face.getId() + ".jpg");
            resDTOS.add(face);
        }
        return Response.newSuccessResponse(resDTOS);
    }


    /*
    人脸识别
     */
    @RequestMapping(value = "/faceRecognition", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<FaceRecognitionResDTO>> faceRecognition(FaceRecognitionReqDTO faceRecognitionReqDTO) {
        String image = faceRecognitionReqDTO.getImage();

        List<FaceRecognitionResDTO> faceRecognitionResDTOList = Lists.newLinkedList();
        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo);
                if (feature != null) {
                    List<UserCompareInfo> userCompareInfos = faceEngineService.faceRecognition(feature, UserRamCache.getUserList(), 0.8f);
                    if (CollectionUtil.isNotEmpty(userCompareInfos)) {
                        faceRecognitionResDTO.setName(userCompareInfos.get(0).getName());
                        faceRecognitionResDTO.setSimilar(userCompareInfos.get(0).getSimilar());
                    }
                }
                faceRecognitionResDTOList.add(faceRecognitionResDTO);
            }

        }


        return Response.newSuccessResponse(faceRecognitionResDTOList);
    }

    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<FaceDetectResDTO>> detectFaces(@RequestBody FaceDetectReqDTO faceDetectReqDTO) {
        String image = faceDetectReqDTO.getImage();
        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceDetectResDTO> faceDetectResDTOS = Lists.newLinkedList();
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            List<ProcessInfo> process = faceEngineService.process(rgbData, faceInfoList);

            for (int i = 0; i < faceInfoList.size(); i++) {
                FaceDetectResDTO faceDetectResDTO = new FaceDetectResDTO();
                FaceInfo faceInfo = faceInfoList.get(i);
                faceDetectResDTO.setRect(faceInfo.getRect());
                faceDetectResDTO.setOrient(faceInfo.getOrient());
                faceDetectResDTO.setFaceId(faceInfo.getFaceId());
                if (CollectionUtil.isNotEmpty(process)) {
                    ProcessInfo processInfo = process.get(i);
                    faceDetectResDTO.setAge(processInfo.getAge());
                    faceDetectResDTO.setGender(processInfo.getGender());
                    faceDetectResDTO.setLiveness(processInfo.getLiveness());

                }
                faceDetectResDTOS.add(faceDetectResDTO);

            }
        }

        return Response.newSuccessResponse(faceDetectResDTOS);
    }

    @RequestMapping(value = "/compareFaces", method = RequestMethod.POST)
    @ResponseBody
    public Response<Float> compareFaces(@RequestBody CompareFacesReqDTO compareFacesReqDTO) {

        String image1 = compareFacesReqDTO.getImage1();
        String image2 = compareFacesReqDTO.getImage2();

        byte[] bytes1 = Base64Util.base64ToBytes(image1);
        byte[] bytes2 = Base64Util.base64ToBytes(image2);
        ImageInfo rgbData1 = ImageFactory.getRGBData(bytes1);
        ImageInfo rgbData2 = ImageFactory.getRGBData(bytes2);

        Float similar = faceEngineService.compareFace(rgbData1, rgbData2);

        return Response.newSuccessResponse(similar);
    }


}
