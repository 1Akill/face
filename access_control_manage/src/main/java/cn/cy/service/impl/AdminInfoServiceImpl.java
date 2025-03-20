package cn.cy.service.impl;

import cn.cy.entity.AdminInfo;
import cn.cy.mapper.AdminInfoMapper;
import cn.cy.service.AdminInfoService;
import cn.cy.vo.AdminInfoVO;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {

    @Override
    public AdminInfoVO login(AdminInfo adminInfo) {
        AdminInfo one = this.getOne(Wrappers.<AdminInfo>lambdaQuery().eq(AdminInfo::getUserName, adminInfo.getUserName()).eq(AdminInfo::getPassword, adminInfo.getPassword()));
        if (one != null){
            return BeanUtil.copyProperties(one, AdminInfoVO.class);
        }
        return null;
    }
}
