package cn.hwb.askanswer.common.base.pojo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hwb
 */
@Getter
public abstract class DomainEvent extends ApplicationEvent {
    private final String id;
    private final Long userId;
    private final LocalDateTime occurredOn;

    public DomainEvent(Long userId) {
        this(userId, userId);
    }

    public DomainEvent(Object o, Long userId) {
        super(o);
        this.userId = userId;
        this.occurredOn = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
    }
}
