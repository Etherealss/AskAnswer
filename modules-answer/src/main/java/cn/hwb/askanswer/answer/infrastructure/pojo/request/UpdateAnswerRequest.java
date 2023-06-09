package cn.hwb.askanswer.answer.infrastructure.pojo.request;

import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XssEscape
public class UpdateAnswerRequest {
    @Size(min = 1, max = 100)
    @XssEscape
    String title;

    @Size(min = 1, max = 1000)
    @XssEscape
    String content;

    Boolean isAnonymous;
}
