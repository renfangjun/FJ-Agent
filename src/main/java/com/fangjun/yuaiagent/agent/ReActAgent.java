package com.fangjun.yuaiagent.agent;

/**
 * @author fangjun
 * @modify 2025-07-01 16:38:09
 */
public abstract class ReActAgent extends BaseAgent {

    /**
     * 是否需要思考
     *
     * @return
     */
    public abstract boolean think();

    /**
     * 执行
     *
     * @return
     */
    public abstract String act();

    /**
     * 执行单个步骤 思考、执行
     *
     * @return
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                return "思考完成 - 无需思考";
            }
            return act();
        } catch (Exception e) {
            e.printStackTrace();
            return "步骤执行失败: " + e.getMessage();
        }
    }
}
