package cn.hwb.askanswer.question.infrastructure.pojo.request;

import cn.hwb.askanswer.common.base.validation.ListStringValidation;
import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XssEscape
public class CreateQuestionRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    @XssEscape
    String title;

    @Size(min = 1, max = 1000)
    @NotBlank
    @XssEscape
    String content;

    @NotEmpty
    @ListStringValidation(regexp = "^[a-zA-Z\\p{IsHan}]+$", min = 1, max = 10, message = "列表元素格式不符合要求：regexp = ^[a-zA-Z\\p{IsHan}]+$, min = 1, max = 10")
    List<String> tags;
}
