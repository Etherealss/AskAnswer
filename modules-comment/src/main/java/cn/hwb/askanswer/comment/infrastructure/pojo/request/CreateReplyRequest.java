package cn.hwb.askanswer.comment.infrastructure.pojo.request;

import cn.hwb.common.security.xss.XssEscape;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@XssEscape
public class CreateReplyRequest {

    @NotBlank
    @Size(min = 1, max = 1000)
    @XssEscape
    String content;

    @NotNull
    Long containerId;
}
