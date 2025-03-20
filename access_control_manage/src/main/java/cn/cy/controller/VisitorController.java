package cn.cy.controller;


import cn.cy.entity.UserInfo;
import cn.cy.entity.Visitor;
import cn.cy.service.VisitorService;
import cn.cy.util.Condition;
import cn.cy.util.Query;
import cn.cy.util.UserRamCache;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
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
@RequestMapping("/visitor")
@AllArgsConstructor
public class VisitorController {

    private VisitorService visitorService;

    @PostMapping("/add")
    public R add(@RequestBody Visitor visitor){
        return R.ok(visitorService.save(visitor));
    }

    @PostMapping("/remove/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@PathVariable("ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        return R.ok(visitorService.removeByIds(idList));
    }

    @GetMapping("/page")
    public R page(Visitor visitor, Query query){
        IPage<Visitor> page = Condition.getPage(query);
        page = visitorService.page(page, Wrappers.<Visitor>lambdaQuery().like(ObjectUtil.isNotEmpty(visitor.getVisitorName()), Visitor::getVisitorName, visitor.getVisitorName()));
        return R.ok(page);
    }

}
