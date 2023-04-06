package cn.hwb.askanswer.notification.infrastructure.pojo.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 通知模板，形如：
 * - 用户 [username] 赞了你的: [targetDesc]
 * - 用户 张三 赞了你的：如何学好数学
 * userId、targetId用于超链接，文字模板由前端设置，后端仅存储关键信息
 *
 * @author wang tengkun
 * @date 2023/4/6
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class NotificationTemplate {
    /**
     * 引发通知的用户ID，例如对于点赞通知，userId=点赞用户ID
     */
    Long userId;

    /**
     * 引发通知的的用户名，例如对于点赞通知，username=点赞用户的用户名
     */
    String username;

    /**
     * 目标ID，例如对于回答点赞通知，targetId=回答ID
     */
    Long targetId;

    /**
     * 目标描述，例如对于回答点赞通知，targetDesc=回答标题
     */
    String targetDesc;
}
