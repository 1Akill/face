package cn.cy.vo;

import cn.cy.entity.AccessRecord;
import cn.cy.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVO extends UserInfo {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date joinTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outTime;

    private int recordId;

}
