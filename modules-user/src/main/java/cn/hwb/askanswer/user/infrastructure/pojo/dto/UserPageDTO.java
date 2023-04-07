package cn.hwb.askanswer.user.infrastructure.pojo.dto;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * @author hwb
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserPageDTO<T> extends PageDTO<T> {
    Map<Long, UserBriefDTO> userMap;
}
