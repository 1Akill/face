package cn.cy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 性别
     */
    private String sex;

    /**
     * 姓名
     */
    private String name;

    /**
     * 学院
     */
    private String college;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 专业
     */
    private String major;

    private String faceUrl;

}
