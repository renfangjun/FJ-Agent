package com.fangjun.junaiagent.rag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器
 *
 * @author fangjun
 * @modify 2025-06-26 15:36:16
 */
@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(ChatModel dashscopeChatModel) {
        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(dashscopeChatModel))
                .build();
    }

    /**
     * 执行查询重写
     * @param userPrompt
     * @return
     */
    public String doRewrite(String userPrompt) {
        Query query = new Query(userPrompt);
        // 执行查询重写
        String text = queryTransformer.transform(query).text();
        return text;
    }
}
