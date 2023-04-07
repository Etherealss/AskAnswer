package cn.hwb.askanswer.answer.infrastructure.pojo.request;

import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XssEscape
public class UpdateAnswerAcceptRequest {
    @NotNull
    Boolean isAccepted;
}
