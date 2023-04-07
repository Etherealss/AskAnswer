package cn.hwb.askanswer.common.base.web;


import cn.hwb.askanswer.common.base.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 将Controller中的将参数转为对应的BaseEnum
 * @author hwb
 */
public class StringEnumConverter implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    public static class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {
        private final Class<T> targetType;

        public StringToEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            return BaseEnum.fromName(targetType, source);
        }
    }
}
