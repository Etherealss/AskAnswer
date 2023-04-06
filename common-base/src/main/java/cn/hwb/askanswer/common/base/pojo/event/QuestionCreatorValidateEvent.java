package cn.hwb.askanswer.common.base.pojo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 验证当前用户是否为问题作者的事件
 * @author wtk
 * @date 2023-03-25
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QuestionCreatorValidateEvent extends DomainEvent {
    private final Long questionId;
    public QuestionCreatorValidateEvent(Long questionId, Long userId) {
        super(questionId, userId);
        this.questionId = questionId;
    }
}
