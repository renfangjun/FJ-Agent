package com.fangjun.junaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author fangjun
 * @modify 2025-07-01 19:16:11
 */
@SpringBootTest
class JunManusTest {
    @Resource
    private JunManus junManus;
    @Test
    void run() {
        String userPrompt = """  
                我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点，  
                并结合一些网络图片，制定一份详细的约会计划，  
                并以 PDF 格式输出""";
        String answer = junManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}