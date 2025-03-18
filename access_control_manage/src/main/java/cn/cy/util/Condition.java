package cn.cy.util;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author chenyi
 * @version 1.0
 * @description: TODO
 * @date 2022/12/7 18:21
 */
public class Condition {

    /**
     * 转化成mybatis plus中的Page
     *
     * @param query 查询条件
     * @return IPage
     */
    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(Convert.toInt(query.getCurrent(), 1), Convert.toInt(query.getSize(), 10));
        return page;
    }

}
