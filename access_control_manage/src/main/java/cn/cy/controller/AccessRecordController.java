package cn.cy.controller;


import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserInfo;
import cn.cy.service.AccessRecordService;
import cn.cy.util.Condition;
import cn.cy.util.Query;
import cn.cy.util.UserRamCache;
import cn.cy.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
@RestController
@RequestMapping("/access-record")
public class AccessRecordController {

    @Autowired
    private AccessRecordService accessRecordService;

    @GetMapping("/page")
    public R page(UserInfo userInfo, Query query){
        IPage<UserInfoVO> page = Condition.getPage(query);
        List<UserInfoVO> accessRecordVOS = accessRecordService.recordList(userInfo, page);
        page.setRecords(accessRecordVOS);
        return R.ok(page);
    }

    @PostMapping("/remove/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@PathVariable("ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        accessRecordService.removeByIds(idList);
        return R.ok(true);
    }
}
