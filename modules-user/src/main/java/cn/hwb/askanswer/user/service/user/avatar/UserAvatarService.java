package cn.hwb.askanswer.user.service.user.avatar;

import cn.hwb.askanswer.user.infrastructure.config.UserAvatarProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wtk
 * @date 2023-03-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAvatarService {

    private final UserAvatarProperties userAvatarProperties;

    public String getDefaultAvatar() {
        return userAvatarProperties.getDefaultAvatar();
    }

}
