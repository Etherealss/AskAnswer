package cn.hwb.askanswer.common.base.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author hwb
 */
public class PageUtil {

    public static void copyPage(IPage<?> source, IPage<?> target) {
        target.setPages(source.getPages());
        target.setTotal(source.getTotal());
        target.setCurrent(source.getCurrent());
        target.setSize(source.getSize());
    }
}
