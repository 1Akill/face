package cn.cy.service.impl;

import cn.cy.dto.FaceRecognitionResDTO;
import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserCompareInfo;
import cn.cy.entity.UserInfo;
import cn.cy.mapper.UserInfoMapper;
import cn.cy.service.AccessRecordService;
import cn.cy.service.FaceEngineService;
import cn.cy.service.UserInfoService;
import cn.cy.util.Base64Util;
import cn.cy.util.UserRamCache;
import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private FaceEngineService faceEngineService;
    @Autowired
    private AccessRecordService accessRecordService;
    @Override
    public UserInfo faceRecognition(String image) {
        byte[] bytes = Base64Util.base64ToBytes(image);
        ImageInfo rgbData = ImageFactory.getRGBData(bytes);
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(rgbData);
        FaceRecognitionResDTO faceRecognitionResDTO = new FaceRecognitionResDTO();
        if (CollectionUtil.isNotEmpty(faceInfoList)) {
            for (FaceInfo faceInfo : faceInfoList) {
                faceRecognitionResDTO.setRect(faceInfo.getRect());
                byte[] feature = faceEngineService.extractFaceFeature(rgbData, faceInfo);
                if (feature != null) {
                    List<UserCompareInfo> userCompareInfos = faceEngineService.faceRecognition(feature, UserRamCache.getUserList(), 0.8f);
                    if (CollectionUtil.isNotEmpty(userCompareInfos)) {
                        faceRecognitionResDTO.setName(userCompareInfos.get(0).getName());
                        faceRecognitionResDTO.setSimilar(userCompareInfos.get(0).getSimilar());
                    }
                }
            }

        }
        if (faceRecognitionResDTO.getName() == null){
            return null;
        }
        UserInfo userInfo = getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getStudentId, faceRecognitionResDTO.getName()));
        if (userInfo == null){
            return null;
        }
        AccessRecord accessRecord = accessRecordService.getOne(Wrappers.<AccessRecord>lambdaQuery().eq(AccessRecord::getUserId, userInfo.getId()).orderByDesc(AccessRecord::getId).last("limit 1"));
        if (accessRecord!=null && accessRecord.getOutTime() == null){
            accessRecord.setOutTime(new Date());
            accessRecordService.updateById(accessRecord);
        }else {
            accessRecord = new AccessRecord();
            accessRecord.setUserId(userInfo.getId());
            accessRecord.setJoinTime(new Date());
            accessRecordService.save(accessRecord);
        }
        return userInfo;
    }
}
