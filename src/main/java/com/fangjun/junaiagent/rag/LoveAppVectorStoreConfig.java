package com.fangjun.junaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author fangjun
 * @modify 2025-05-26 00:18:59
 * @motto Talk is cheap, show me the code!
 * @description <h1> </h1>
 */
@Configuration
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyDocumentEnricher myDocumentEnricher;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> enrichedDocuments = myDocumentEnricher.enrichDocumentsByKeyword(loveAppDocumentLoader.loadMarkdown());
        simpleVectorStore.doAdd(enrichedDocuments);
        return simpleVectorStore;
    }
}
