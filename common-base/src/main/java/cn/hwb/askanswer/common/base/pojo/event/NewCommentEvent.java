package cn.hwb.askanswer.common.base.pojo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 问题被评论的事件
 * @author wang tengkun
 * @date 2023/4/6
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewCommentEvent extends DomainEvent {
    private final Long answerId;
    public NewCommentEvent(Long answerId, Long userId) {
        super(userId);
        this.answerId = answerId;
    }
}
