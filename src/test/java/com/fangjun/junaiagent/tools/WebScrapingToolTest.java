package com.fangjun.junaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fangjun
 * @modify 2025-06-27 17:30:01
 */
@SpringBootTest
class WebScrapingToolTest {


        @Test
        public void testScrapeWebPage() {
            WebScrapingTool tool = new WebScrapingTool();
            String url = "https://www.codefather.cn";
            String result = tool.scrapeWebPage(url);
            assertNotNull(result);
        }


}