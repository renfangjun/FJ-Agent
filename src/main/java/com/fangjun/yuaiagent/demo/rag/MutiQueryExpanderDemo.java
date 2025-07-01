package com.fangjun.yuaiagent.demo.rag;

import com.fangjun.yuaiagent.app.LoveApp;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询扩展器demo
 * @author fangjun
 * @modify 2025-06-26 15:14:44
 */
@Component
public class MutiQueryExpanderDemo {
    private final ChatClient.Builder chatClientBuilder;

    public MutiQueryExpanderDemo(ChatModel dashscopeChatModel) {
        this.chatClientBuilder = ChatClient.builder(dashscopeChatModel);
    }

    public List<Query> expand(String query) {
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = multiQueryExpander.expand(new Query(query));
        return queries;
    }
}
