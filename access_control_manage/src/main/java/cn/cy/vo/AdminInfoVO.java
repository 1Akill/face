package cn.cy.vo;

import cn.cy.entity.AdminInfo;
import lombok.Data;

@Data
public class AdminInfoVO extends AdminInfo {
    private String token;
}
