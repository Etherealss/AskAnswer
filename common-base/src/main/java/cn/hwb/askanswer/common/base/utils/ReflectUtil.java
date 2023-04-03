package cn.hwb.askanswer.common.base.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author wang tengkun
 * @date 2022/2/23
 */
public class ReflectUtil {

    public static <T> T newInstance(Class<T> aClass) {
        return ReflectUtil.newInstance(aClass, null, null);
    }

    public static <T> T newInstance(Class<T> aClass, Class<?>[] parameterTypes, Object[] initargs) {
        try {
            return aClass.getDeclaredConstructor(parameterTypes).newInstance(initargs);
        } catch (Exception e) {
            throw new RuntimeException("反射构造对象异常。目标类型：" + aClass.getName());
        }
    }

    /**
     * 获取接口的泛型
     * @param interfaceType 接口类型
     * @param implementationClass 实现了接口的类型
     * @return
     */
    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> interfaceType,
                                                         Class<?> implementationClass) {
        Assert.notNull(interfaceType, "接口Class不能为null");
        Assert.isTrue(interfaceType.isInterface(), "给定的Class必须为接口类型");

        if (implementationClass == null) {
            return null;
        }

        // 获取泛型。getGenericInterfaces()见：https://my.oschina.net/617669559/blog/3012228
        // getInterfaces：主要是 获取由此对象表示的类或接口实现的接口
        // getGenericInterfaces： 主要是 获取由此对象表示的类或接口直接实现的接口的Type。
        // 区别在于getGenericInterfaces可以返回其参数化类型，例如： Collection<String>、 List<Coupon>中的String和Coupon
        ParameterizedType currentType =
                getParameterizedType(interfaceType, implementationClass.getGenericInterfaces());
        if (currentType != null) {
            return currentType;
        }

        // 从父类递归获取
        Class<?> superclass = implementationClass.getSuperclass();
        return getParameterizedType(interfaceType, superclass);
    }

    /**
     * 获取接口的泛型
     * @param superType 接口类型
     * @param genericTypes
     * @return
     */
    @Nullable
    private static ParameterizedType getParameterizedType(@NonNull Class<?> superType,
                                                         Type... genericTypes) {
        Assert.notNull(superType, "接口或其父接口的Class不能为null");

        ParameterizedType currentType = null;

        for (Type genericType : genericTypes) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                if (parameterizedType.getRawType().getTypeName().equals(superType.getTypeName())) {
                    currentType = parameterizedType;
                    break;
                }
            }
        }

        return currentType;
    }
}
