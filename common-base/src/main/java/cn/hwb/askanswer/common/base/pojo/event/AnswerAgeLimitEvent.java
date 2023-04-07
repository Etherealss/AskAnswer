package cn.hwb.askanswer.common.base.pojo.event;

import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hwb
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AnswerAgeLimitEvent extends DomainEvent {
    private final Long questionId;
    private final AgeBracketEnum ageBracket;

    public AnswerAgeLimitEvent(Long answerId, Long userId, AgeBracketEnum ageBracket) {
        super(userId);
        this.questionId = answerId;
        this.ageBracket = ageBracket;
    }
}
