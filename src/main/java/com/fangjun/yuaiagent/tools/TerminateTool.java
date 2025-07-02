package com.fangjun.yuaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止工具 （让自制规划智能体终止）
 *
 * @author fangjun
 * @modify 2025-07-01 18:58:02
 */
public class TerminateTool {
    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate() {
        return "任务结束";
    }
}
