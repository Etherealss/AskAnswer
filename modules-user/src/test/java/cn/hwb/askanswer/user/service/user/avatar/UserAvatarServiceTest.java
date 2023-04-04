package cn.hwb.askanswer.user.service.user.avatar;

import cn.hwb.askanswer.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j(topic = "test")
@DisplayName("UserAvatarServiceTest测试")
@SpringBootTest(classes = TestApplication.class)
class UserAvatarServiceTest {

    @Autowired
    private UserAvatarService userAvatarService;

    @Test
    void testGetDefaultAvatar() {
        String avatar = userAvatarService.getDefaultAvatar();
        log.info("avatar: {}", avatar);
        assertNotNull(avatar);
    }
}