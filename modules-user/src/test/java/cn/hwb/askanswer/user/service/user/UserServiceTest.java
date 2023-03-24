package cn.hwb.askanswer.user.service.user;

import cn.hwb.askanswer.TestApplication;
import cn.hwb.askanswer.user.infrastructure.pojo.request.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@Slf4j(topic = "test")
@DisplayName("UserServiceTest测试")
@SpringBootTest(classes = TestApplication.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("username1");
        req.setUsername("password1");
        req.setBirthday(new Date());
        Long userId = userService.createUser(req);
        log.info("userId: {}", userId);
    }

    @Test
    void testGetBriefById() {
    }

    @Test
    void testGetBatchBriefsByIds() {
    }

    @Test
    void testUsernameExists() {
    }
}