package cn.cy.mapper;

import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserInfo;
import cn.cy.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
public interface AccessRecordMapper extends BaseMapper<AccessRecord> {

    List<UserInfoVO> recordList(UserInfo userInfo, IPage<UserInfoVO> page);
}
