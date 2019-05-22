package com.fimi.app.x8s.entity;

public class X8AiModeState {
    private AiModeState aiModeState = AiModeState.IDLE;

    public enum AiModeState {
        IDLE,
        READY,
        RUNNING
    }

    public AiModeState getAiModeState() {
        return this.aiModeState;
    }

    public void setAiModeState(AiModeState aiModeState) {
        this.aiModeState = aiModeState;
    }

    public boolean isAiModeStateReady() {
        return this.aiModeState == AiModeState.READY;
    }

    public boolean isAiModeStateIdle() {
        return this.aiModeState == AiModeState.IDLE;
    }
}
