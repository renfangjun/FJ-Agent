package com.fangjun.junaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.fangjun.junaiagent.agent.model.AgentState;
import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author fangjun
 * @modify 2025-07-01 16:37:50
 */
@Data
public abstract class BaseAgent {
    private static final Logger log = LoggerFactory.getLogger(BaseAgent.class);
    //提示词
    private String systemPrompt;
    private String nextStepPrompt;

    private String name;

    //代理状态
    private AgentState state = AgentState.IDLE;

    //执行步骤控制
    private int currentStep = 0;
    private int maxSteps = 10;

    // LLM大模型
    private ChatClient chatClient;

    // Memory 记忆
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     *
     * @param userPrompt
     * @return
     */
    public String run(String userPrompt) {
        // 基础校验
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state:" + this.state);
        }

        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty user prompt:" + this.state);
        }

        //执行，更改状态
        this.state = AgentState.RUNNING;

        //记录上下文信息
        messageList.add(new UserMessage(userPrompt));

        //保存结果列表
        List<String> results = new ArrayList<>();


        try {
            //执行循环
            for (int i = 0; i < maxSteps && this.state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                this.currentStep = stepNumber;
                log.info("Execute Step:{}/{}", stepNumber, maxSteps);
                // 单步执行，得到结果
                String stepResult = step();
                String result = "Step" + stepNumber + ": " + stepResult;
                results.add(result);
            }

            if (currentStep >= maxSteps) {
                this.state = AgentState.FINISHED;
                log.info("Terminated: Reached max step (" + maxSteps + ")");
            }
            return StrUtil.join("\n", results);
        } catch (Exception e) {
            this.state = AgentState.ERROR;
            log.error("Error execute agent:", e.getMessage());
            return "Error execute agent:" + e.getMessage();
        } finally {
            // 清理资源
            cleanup();
        }
    }

    /**
     * 运行代理（流式输出）
     *
     * @param userPrompt 用户提示词
     * @return SseEmitter实例
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建SseEmitter，设置较长的超时时间
        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(() -> {
            try {
                if (this.state != AgentState.IDLE) {
                    emitter.send("错误：无法从状态运行代理: " + this.state);
                    emitter.complete();
                    return;
                }
                if (StringUtil.isBlank(userPrompt)) {
                    emitter.send("错误：不能使用空提示词运行代理");
                    emitter.complete();
                    return;
                }

                // 更改状态
                state = AgentState.RUNNING;
                // 记录消息上下文
                messageList.add(new UserMessage(userPrompt));

                try {
                    for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                        int stepNumber = i + 1;
                        currentStep = stepNumber;
                        log.info("Executing step " + stepNumber + "/" + maxSteps);

                        // 单步执行
                        String stepResult = step();
                        String result = "Step " + stepNumber + ": " + stepResult;

                        // 发送每一步的结果
                        emitter.send(result);
                    }
                    // 检查是否超出步骤限制
                    if (currentStep >= maxSteps) {
                        state = AgentState.FINISHED;
                        emitter.send("执行结束: 达到最大步骤 (" + maxSteps + ")");
                        emitter.send(getMessageList().getLast().getText());
                    }
                    // 正常完成
                    emitter.complete();
                } catch (Exception e) {
                    state = AgentState.ERROR;
                    log.error("执行智能体失败", e);
                    try {
                        emitter.send("执行错误: " + e.getMessage());
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                } finally {
                    // 清理资源
                    this.cleanup();
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        emitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });

        return emitter;
    }



    /**
     * 定义单个步骤
     */
    public abstract String step();


    /**
     * 清理资源
     */
    protected void cleanup() {
        //子类可覆盖此方法来清理资源
    }

}
