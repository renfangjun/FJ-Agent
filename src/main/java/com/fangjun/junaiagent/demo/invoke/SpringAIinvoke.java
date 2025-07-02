package com.fangjun.junaiagent.demo.invoke;// 建议dashscope SDK的版本 >= 2.12.0

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SpringAI 框架调用模型
 */
@Component
public class SpringAIinvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好,我是俊俊")).getResult().getOutput();
        System.out.println(output.getText());
    }
}
