package com.fimi.x8sdk.cmdsenum;

public class X8Cali {

    public enum CaliCmd {
        CALI_CMD_IDLE,
        CALI_CMD_START,
        CALI_CMD_RESTART,
        CALI_CMD_NEXT_STEP,
        CALI_CMD_QUIT,
        CALI_CMD_ALL_DONE
    }

    public enum CaliMode {
        CALI_MODE_NULL,
        CALI_MODE_AUTO,
        CALI_MODE_MANUALLY
    }

    public enum CaliStep {
        CALI_STEP_IDLE,
        CALI_STEP_HORIZONTAL,
        CALI_STEP_VERTICAL,
        CALI_STEP_ORTH,
        CALI_STEP_X_POSITIVE_DIR,
        CALI_STEP_X_NAGITIVE_DIR,
        CALI_STEP_Y_POSITIVE_DIR,
        CALI_STEP_Y_NAGITIVE_DIR,
        CALI_STEP_Z_POSITIVE_DIR,
        CALI_STEP_Z_NAGITIVE_DIR,
        CALI_STEP_ENVIRONMENT_CHECK,
        CALI_STEP_DATA_PROCESS
    }

    public enum CaliStepStatus {
        CALI_STA_IDLE,
        CALI_STA_OBSERVERING,
        CALI_STA_SAMPLING,
        CALI_STA_COMPUTING,
        CALI_STA_SAVING,
        CALI_STA_WAIT_NEXT,
        CALI_STA_QUIT,
        CALI_STA_HANG_UP,
        CALI_STA_DONE,
        CALI_STA_ERR,
        CALI_STA_SAMPLED,
        CALI_STA_SAMPLE_DONE
    }

    public enum CaliType {
        CALI_TYPE_IDLE,
        CALI_MAG,
        CALI_ACC_SIX_POINT,
        CALI_IMU_ORTH
    }

    public enum SensorType {
        IDLE,
        IMUM,
        IMUS,
        MAGM,
        MAGS
    }
}
