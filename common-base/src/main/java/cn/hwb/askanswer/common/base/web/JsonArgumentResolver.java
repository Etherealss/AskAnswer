package cn.hwb.askanswer.common.base.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author hwb
 */
@Component
@Slf4j(topic = "other")
public class JsonArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        JsonParam annotation = parameter.getParameterAnnotation(JsonParam.class);
        assert annotation != null;
        String paramName = annotation.value();
        if (StrUtil.isBlank(paramName)) {
            // 用方法形参名作参数名
            paramName = parameter.getParameterName();
        }

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;
        ServletInputStream inputStream = request.getInputStream();
        // 检查输入流是否已经被读取
        if (!inputStream.isFinished()) {
            // InputStram还没被读取，说明Request中还没有保存Json数据，此处读取Json
            String jsonString = getJsonString(request);
            JSONObject jsonObject = JSONUtil.parseObj(jsonString);
            // 保存到Request域中
            setAllJsonParam2Request(request, jsonObject);
        }

        // 从Request域中读取Json参数
        Object attribute = request.getAttribute(paramName);
        if (attribute == null) {
            // 如果从传来的Json中获取不到参数，就尝试获取默认值
            String defaultValue = annotation.defaultValue();
            if (defaultValue == null) {
                if (annotation.required()) {
                    // 如果没有默认值，但是该参数又不可缺失，则报错
                    throw new MissingRequestValueException("JSON参数缺失，参数名为：" + paramName);
                }
            } else {
                attribute = defaultValue;
            }
        } else if (attribute instanceof JSONObject) {
            // 如果是JSONObject，需要转化为方法参数的对应类型，否则会报错：argument type mismatch
            attribute = JSONUtil.toBean((JSONObject) attribute, parameter.getParameterType());
        }
        log.trace("param:{}\nattribute:{}", paramName, attribute);

        return attribute;
    }

    /**
     * 才request中获取json
     * @param req
     * @return
     * @throws IOException
     */
    private String getJsonString(HttpServletRequest req) throws IOException {
        StringBuilder builder = new StringBuilder();
        try {
            ServletInputStream inputStream = req.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(
                    inputStream, StandardCharsets.UTF_8));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                builder.append(inputStr);
            }
        } catch (IOException e) {
            throw new IOException("解析JSON数据时出错", e);
        }
        return builder.toString();
    }

    /**
     * 将JSON参数全部保存到Request域中
     * @param req
     * @param json
     */
    private void setAllJsonParam2Request(HttpServletRequest req, JSONObject json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            Object value = entry.getValue();
            log.trace("key:{}\nvalue:{}", entry.getKey(), value);
            req.setAttribute(entry.getKey(), value);
        }
    }
}
