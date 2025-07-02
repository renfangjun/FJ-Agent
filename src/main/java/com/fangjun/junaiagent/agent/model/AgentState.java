package com.fangjun.junaiagent.agent.model;

/**
 * 代理执行状态的枚举类
 *
 * @author fangjun
 * @modify 2025-07-01 16:36:20
 */
public enum AgentState {
    /**
     * 空闲状态
     */
    IDLE,

    /**
     * 运行中状态
     */
    RUNNING,

    /**
     * 已完成状态
     */
    FINISHED,

    /**
     * 错误状态
     */
    ERROR
}
