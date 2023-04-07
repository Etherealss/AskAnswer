package cn.hwb.askanswer.common.base.pojo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hwb
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewAnswerEvent extends DomainEvent {
    private final Long questionId;

    public NewAnswerEvent(Long answerId, Long userId) {
        super(userId);
        this.questionId = answerId;
    }
}