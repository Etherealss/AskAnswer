package cn.hwb.askanswer.user.infrastructure.pojo.dto;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
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
    /**
     * 用户信息
     */
    Map<Long, UserBriefDTO> userMap;

    public UserPageDTO(List<T> records, IPage<?> page, Map<Long, UserBriefDTO> userMap) {
        super(records, page);
        this.userMap = userMap;
    }
}
