package com.fangjun.junaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fangjun
 * @modify 2025-06-27 17:24:54
 */
@SpringBootTest
class WebSearchToolTest {
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testSearchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "程序员鱼皮编程导航 codefather.cn";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }

}