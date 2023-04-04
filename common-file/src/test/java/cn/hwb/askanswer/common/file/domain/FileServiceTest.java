package cn.hwb.askanswer.common.file.domain;

import cn.hwb.askanswer.TestRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j(topic = "test")
@DisplayName("FileServiceTest测试")
@SpringBootTest(classes = TestRunner.class)
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    void testUploadFile() {
    }

    @Test
    void testGetFileUrl() {
        String fileUrl = fileService.getFileUrl("/avatars/default-boy.png");
        log.info("{}", fileUrl);
    }
}