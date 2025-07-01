package com.fangjun.yuaiagent.rag;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

/**
 * 创建自定义检索顾问的工厂
 *
 * @author fangjun
 * @modify 2025-06-26 15:59:35
 */
@Component
public class LoveAppRagCustomAdvisorFactory {
    /**
     * 创建自定义检索增强顾问
     *
     * @param vectorStore
     * @param status
     * @return
     */
    public static Advisor createloveAppRagCustomAdvisor(VectorStore vectorStore, String status) {
        // 过滤特定状态文档
        Filter.Expression expression = new FilterExpressionBuilder().eq("status", status).build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)      // 设置向量存储（里面有自己知识库中的内容）
                .filterExpression(expression) // 对知识库中的内容设置过滤条件
                .similarityThreshold(0.5)    // 设置相似度阈值
                .topK(3)                    // 设置返回文档数量
                .build();

        return RetrievalAugmentationAdvisor.builder()
                // 设置文档检索器
                .documentRetriever(documentRetriever)
                // 设置查询增强器(当用户差不到时可以给予用户友好的回答提示)
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();
    }
}
