package com.fangjun.junaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import jakarta.annotation.Resource;

/**
 * @author fangjun
 * @modify 2025-05-08 15:22:56
 * @motto Talk is cheap, show me the code!
 * @description <h1> </h1>
 */
public class LangChain4jInvoke {
    @Resource
    private QwenChatModel qwenChatModel;
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey("sk-d936bc9be6d0489ba5f13fa4fb879cf0")
                .modelName("qwen-plus")
                .build();
        String chat = qwenChatModel.chat("你好,我是俊俊");
        System.out.println(chat);
    }
}
