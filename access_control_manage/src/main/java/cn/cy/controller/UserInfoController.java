package cn.cy.controller;


import cn.cy.entity.UserInfo;
import cn.cy.service.FaceEngineService;
import cn.cy.service.UserInfoService;
import cn.cy.util.Condition;
import cn.cy.util.Query;
import cn.cy.util.UserRamCache;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
@RequestMapping("/user-info")
@AllArgsConstructor
public class UserInfoController {

    private UserInfoService userInfoService;

    private FaceEngineService faceEngineService;

    /**
     * 分页获取用户数据列表
     * @param userInfo
     * @param query
     * @return
     */
    @GetMapping("/page")
    public R page(UserInfo userInfo, Query query){
        IPage<UserInfo> page = Condition.getPage(query);
        page = userInfoService.page(page, Wrappers.<UserInfo>lambdaQuery().like(ObjectUtil.isNotEmpty(userInfo.getName()), UserInfo::getName, userInfo.getName()));
        return R.ok(page);
    }

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody UserInfo userInfo){
        UserInfo one = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getPhone, userInfo.getPhone()).or().eq(UserInfo::getStudentId, userInfo.getStudentId()));
        if (one != null){
            return R.failed("该用户已存在");
        }
        Boolean register = faceEngineService.register(userInfo);
        if (!register){
            return R.failed("请重新上传图片");
        }
        return R.ok(userInfoService.save(userInfo));
    }

    @PostMapping("/remove/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@PathVariable("ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        List<UserInfo> userInfos = userInfoService.listByIds(idList);
        userInfoService.removeByIds(idList);
        List<String> faceIds = userInfos.stream().map(UserInfo::getStudentId).collect(Collectors.toList());
        UserRamCache.removeUsers(faceIds);
        return R.ok(true);
    }

    public String getSavePath() {
        // 这里需要注意的是ApplicationHome是属于SpringBoot的类
        // 获取项目下resources/static/img路径
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());

        // 保存目录位置根据项目需求可随意更改
        return applicationHome.getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\img";
    }
    @RequestMapping("/upload")
    public R update(@RequestParam("file") MultipartFile file) {
        try {
            // 检查上传的文件是否为空
            if (file.isEmpty()) {
                return R.failed("文件不能为空");
            }

            // 生成唯一的文件名
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFilename = UUID.randomUUID().toString() + extension;

            // 获取资源目录的绝对路径
            String path =  getSavePath();
            File saveFile = new File(path, newFilename);

            file.transferTo(saveFile);

            // 返回上传文件的路径
            String uploadedFilePath = "/img/" + newFilename;
            return R.ok(uploadedFilePath);
        } catch (Exception e){
            return R.failed("上传文件错误，请重试");
        }
    }

    @PostMapping("/face-recognition")
    public R faceRecognition(@RequestBody JSONObject param){
        String image = param.getString("image");
        UserInfo userInfo = userInfoService.faceRecognition(image);
        if (userInfo == null){
            return R.failed("非系统用户，禁止访问");
        }
        return R.ok(userInfo);
    }

}
