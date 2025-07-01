package com.fangjun.yuaiagent.rag;

import com.github.xiaoymin.knife4j.core.util.Assert;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fangjun
 * @modify 2025-06-03 22:25:34
 * @motto Talk is cheap, show me the code!
 * @description <h1> </h1>
 */
@SpringBootTest
public class PgVectorVectorStoreConfigTest {
    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void contextLoads() {
        List<Document> documents = List.of(
                new Document("鱼皮的编程导航有什么用？学编程啊，做项目啊", Map.of("meta1", "meta1")),
                new Document("程序员鱼皮的原创项目教程 codefather.cn"),
                new Document("鱼皮这小伙子比较帅气", Map.of("meta2", "meta2"))
        );
        //  向量存储
        pgVectorVectorStore.add(documents);
        // 相似度搜索
        List<Document> results = pgVectorVectorStore.similaritySearch("怎么学编程");
        Assertions.assertNotNull(results);
    }
}