package cn.hwb.askanswer.common.base.pojo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 评论被点赞的事件
 * @author wang tengkun
 * @date 2023/4/6
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommentLikedEvent extends DomainEvent {
    private final Long commentId;
    public CommentLikedEvent(Long answerId, Long userId) {
        super(userId);
        this.commentId = answerId;
    }
}
