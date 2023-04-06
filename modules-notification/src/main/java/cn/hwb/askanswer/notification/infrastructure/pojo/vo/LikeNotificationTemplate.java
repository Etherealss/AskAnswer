package cn.hwb.askanswer.notification.infrastructure.pojo.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class LikeNotificationTemplate {
    /**
     * 点赞的用户ID
     */
    Long userId;

    /**
     * 点赞的用户名
     */
    String username;

    /**
     * 点赞的问题ID
     */
    Long answerId;

    /**
     * 点赞的问题标题
     */
    String answerTitle;
}
