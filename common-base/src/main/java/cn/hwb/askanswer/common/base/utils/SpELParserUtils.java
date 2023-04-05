package cn.hwb.askanswer.common.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wang tengkun
 * @date 2022/2/26
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Slf4j
public class SpELParserUtils {

    private static final String EXPRESSION_PREFIX = "#{";

    private static final String EXPRESSION_SUFFIX = "}";

    /**
     * 表达式解析器
     */
    private static final ExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 参数名解析器，用于获取参数名
     */
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 解析spel表达式
     * @param method 方法
     * @param args 参数值
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(Method method, Object[] args, String spelExpression, Class<T> clz) {
        String[] params = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量，将方法的参数全部设置进去
        for (int i = 0; i < Objects.requireNonNull(params).length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return getResult(context, spelExpression, clz);
    }

    /**
     * 获取spel表达式后的结果
     * @param context 解析器上下文接口
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     * @return 执行spel表达式后的结果
     */
    private static <T> T getResult(EvaluationContext context, String spelExpression, Class<T> clz) {
        try {
            //解析表达式
            Expression expression = SPEL_EXPRESSION_PARSER.parseExpression(spelExpression);
            //获取表达式的值
            return expression.getValue(context, clz);
        } catch (ParseException e) {
            log.warn("SpEL解析失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

