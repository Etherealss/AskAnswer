package cn.hwb.askanswer.question.infrastructure.pojo.request;

import cn.hwb.askanswer.common.base.validation.ListStringValidation;
import cn.hwb.askanswer.question.infrastructure.enums.AgeBracketEnum;
import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XssEscape
public class UpdateQuestionRequest {
    @Size(min = 1, max = 100)
    @XssEscape
    String title;

    @Size(min = 1, max = 1000)
    @XssEscape
    String content;

    @ListStringValidation(regexp = "^[a-zA-Z\\p{IsHan}]+$", min = 1, max = 10)
    List<String> tags;

    AgeBracketEnum ageBracket;
}