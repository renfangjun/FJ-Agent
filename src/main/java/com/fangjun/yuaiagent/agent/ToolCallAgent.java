package com.fangjun.yuaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.fangjun.yuaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fangjun
 * @modify 2025-07-01 16:38:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用的工具
    private final ToolCallback[] avaibleTools;

    //保存工具调用信息的响应结果（要调用哪些工具）
    private ChatResponse toolCallResponse;

    // 工具调用管理者
    private final ToolCallingManager toolCallingManager;

    // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] avaibleTools) {
        super();
        this.avaibleTools = avaibleTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true)
                .build();
    }

    @Override
    public boolean think() {

        // 1. 校验提示词，拼接提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        // 2.调用大模型 ，获取工具调用结果
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(avaibleTools)
                    .call()
                    .chatResponse();

            // 记录响应，用于等下 Act
            this.toolCallResponse = chatResponse;
            // 3.解析工具调用结果 ，获取要调用的工具
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
            // 输出信息
            String result = assistantMessage.getText();
            log.info(getName() + "的思考：" + result);
            log.info(getName() + "选择了" + toolCalls.size() + "个工具去使用");
            String toolsInfo = toolCalls.stream()
                    .map(toolCall -> String.format("工具：%s,参数：%s", toolCall.name(), toolCall.arguments())).collect(Collectors.joining("\n"));
            log.info(toolsInfo);
            // 如果不需要调用工具，则返回 false
            if (toolCalls.isEmpty()) {
                // 只有不需要调用工具时，才需要手动记录助手消息，因为调用工具时会自动记录
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考出错：", e.getMessage());
            getMessageList().add(new AssistantMessage("处理出错：" + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (!toolCallResponse.hasToolCalls()) {
            return "没有工具需要调用";
        }
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallResponse);
        // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用返回的结果
        setMessageList(toolExecutionResult.conversationHistory());

        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        boolean terminateToolCalled = toolResponseMessage.getResponses().stream().allMatch(response -> response.name().equals("doTerminate"));
        if (terminateToolCalled) {
            // 任务结束 ，更改状态
            setState(AgentState.FINISHED);
        }
        String results = toolResponseMessage.getResponses().stream().map(response -> "工具" + response.name() + "返回结果：" + response.responseData()).collect(Collectors.joining("\n"));
        log.info(results);

        return results;
    }
}
