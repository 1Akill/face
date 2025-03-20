package cn.cy.service;

import cn.cy.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo faceRecognition(String image);
}
