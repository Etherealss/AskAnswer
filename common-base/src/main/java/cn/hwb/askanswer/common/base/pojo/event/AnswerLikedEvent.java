package cn.hwb.askanswer.common.base.pojo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 问题被点赞的事件
 * @author hwb
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AnswerLikedEvent extends DomainEvent {
    private final Long answerId;
    public AnswerLikedEvent(Long answerId, Long userId) {
        super(userId);
        this.answerId = answerId;
    }
}
