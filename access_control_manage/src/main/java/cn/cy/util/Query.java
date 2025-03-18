package cn.cy.util;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author chenyi
 * @version 1.0
 * @description: TODO
 * @date 2022/12/7 18:22
 */
@Data
@Accessors(chain = true)
public class Query {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页的数量
     */
    private Integer size;


}
