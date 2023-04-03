package cn.hwb.askanswer.common.base.web;

import cn.hwb.askanswer.common.base.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 方法返回 Msg 实例时，照常返回
 * 方法返回为空时，返回 Msg.ok()
 * 方法返回对象时，包装在 Msg 中返回
 * https://javadaily.cn/post/2022012758/2fed6f3dba49/
 * @see WebMvcConfiguration#configureMessageConverters(java.util.List) 解决返回String bug
 * @author wtk
 * @date 2022/2/21
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 方法上 或者所在的类上 有 ResponseAdvice 注解，返回true
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getDeclaringClass().isAnnotationPresent(ResponseAdvice.class)
            || returnType.getMethodAnnotation(ResponseAdvice.class) != null;
    }

    /**
     *
     * @param o 返回的对象，当返回值是void时返回null
     * @param returnType 返回的所有信息，可以获取返回的对象returnValue、对象类型genericParameterType等等
     * @param selectedContentType 返回值类型，比如 application/json
     * @param selectedConverterType 返回值转换器 比如 MappingJackson2HttpMessageConverter
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (o == null) {
            return Result.ok();
        }
        if (o instanceof Result) {
            return o;
        }
        return Result.ok(o);
    }
}
