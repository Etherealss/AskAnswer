package cn.hwb.askanswer.common.base.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author wtk
 * @date 2022-02-05
 */
public class PageUtil {

    public static void copyPage(IPage<?> source, IPage<?> target) {
        target.setPages(source.getPages());
        target.setTotal(source.getTotal());
        target.setCurrent(source.getCurrent());
        target.setSize(source.getSize());
    }
}
