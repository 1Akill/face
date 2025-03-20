package cn.cy.service;

import cn.cy.entity.AdminInfo;
import cn.cy.vo.AdminInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
public interface AdminInfoService extends IService<AdminInfo> {

    AdminInfoVO login(AdminInfo adminInfo);
}
