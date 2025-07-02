package com.fangjun.junaiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fangjun
 * @modify 2025-06-26 15:22:31
 */
@SpringBootTest
class MutiQueryExpanderDemoTest {

    @Resource
    private MutiQueryExpanderDemo mutiQueryExpanderDemo;

    @Test
    void expand() {
        List<Query> queries = mutiQueryExpanderDemo.expand("谁是程序员鱼皮啊？");
        assertNotNull(queries);
    }
}