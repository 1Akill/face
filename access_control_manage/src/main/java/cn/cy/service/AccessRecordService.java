package cn.cy.service;

import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserInfo;
import cn.cy.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
public interface AccessRecordService extends IService<AccessRecord> {

    List<UserInfoVO> recordList(UserInfo userInfo, IPage<UserInfoVO> page);
}
