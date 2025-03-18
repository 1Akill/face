package cn.cy.service.impl;

import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserInfo;
import cn.cy.mapper.AccessRecordMapper;
import cn.cy.service.AccessRecordService;
import cn.cy.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordMapper, AccessRecord> implements AccessRecordService {

    @Override
    public List<UserInfoVO> recordList(UserInfo userInfo, IPage<UserInfoVO> page) {
        List<UserInfoVO> accessRecordVOS = baseMapper.recordList(userInfo, page);
        return accessRecordVOS;
    }
}
