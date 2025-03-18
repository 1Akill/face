package cn.cy.controller;


import cn.cy.entity.AdminInfo;
import cn.cy.service.AdminInfoService;
import cn.cy.vo.AdminInfoVO;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
@RestController
@RequestMapping("/admin-info")
@AllArgsConstructor
public class AdminInfoController {

    private AdminInfoService adminInfoService;

    @PostMapping("/login")
    public R login(@RequestBody AdminInfo adminInfo){
        AdminInfoVO login = adminInfoService.login(adminInfo);
        if (login != null){
            login.setToken(UUID.randomUUID().toString());
            return R.ok(login);
        }
        return R.failed("失败");
    }

}
