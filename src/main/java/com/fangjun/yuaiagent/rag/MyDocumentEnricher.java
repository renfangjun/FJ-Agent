package com.fangjun.yuaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fangjun
 * @modify 2025-06-26 14:35:29
 */
@Component
public class MyDocumentEnricher {
    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 关键词元信息增强器
     *
     * @param documents
     * @return
     */
    List<Document> enrichDocumentsByKeyword(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        List<Document> enrichedDocuments = keywordMetadataEnricher.apply(documents);
        return enrichedDocuments;
    }

    List<Document> enrichDocumentsBySummary(List<Document> documents) {
        SummaryMetadataEnricher keywordMetadataEnricher = new SummaryMetadataEnricher(dashscopeChatModel, List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS, SummaryMetadataEnricher.SummaryType.CURRENT, SummaryMetadataEnricher.SummaryType.NEXT));
        List<Document> enrichedDocuments = keywordMetadataEnricher.apply(documents);
        return enrichedDocuments;
    }

}
