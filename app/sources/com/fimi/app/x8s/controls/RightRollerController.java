package com.fimi.app.x8s.controls;

import android.content.Context;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.controls.aifly.X8AiTaskManager;
import com.fimi.app.x8s.interfaces.IRightRollerMoveListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.dataparser.AckRightRoller;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import java.util.Arrays;
import java.util.List;

public class RightRollerController {
    X8AiTaskManager aiTaskManager;
    CameraManager cameraManager;
    Context context;
    int evIndex;
    int isoIndex;
    X8MainBottomParameterController mX8MainBottomParameterController;
    IRightRollerMoveListener rightRollerMoveListener;
    private List<String> x8EVOptions = Arrays.asList(CameraJsonCollection.rulerValues);
    private List<String> x8IsoOptions;

    public RightRollerController(CameraManager cameraManager, X8AiTaskManager aiTaskManager, Context context, X8MainBottomParameterController mX8MainBottomParameterController, IRightRollerMoveListener rightRollerMoveListener) {
        this.cameraManager = cameraManager;
        this.aiTaskManager = aiTaskManager;
        this.context = context;
        this.x8IsoOptions = Arrays.asList(context.getResources().getStringArray(R.array.x8_iso_options));
        this.mX8MainBottomParameterController = mX8MainBottomParameterController;
        this.rightRollerMoveListener = rightRollerMoveListener;
    }

    public void changeRightRolloer(AckRightRoller rightRoller) {
        if (rightRoller != null) {
            if (this.aiTaskManager.isInSARMode()) {
                this.aiTaskManager.changeSarProceess(rightRoller.isMoveDown());
            } else if (isAutoMode()) {
                setX8EVOptions(rightRoller.isMoveDown());
            } else {
                setX8IOSOptions(rightRoller.isMoveDown());
            }
        }
    }

    private void setX8IOSOptions(boolean isDown) {
        this.isoIndex = getListIndex(this.x8IsoOptions, this.mX8MainBottomParameterController.getISOText().trim());
        if (this.isoIndex != -1) {
            if (isDown) {
                this.isoIndex--;
            } else {
                this.isoIndex++;
            }
            if (this.isoIndex <= 0) {
                this.isoIndex = 0;
            }
            if (this.isoIndex == this.x8IsoOptions.size()) {
                this.isoIndex = this.x8IsoOptions.size() - 1;
            }
            this.cameraManager.setCameraIsoParams((String) this.x8IsoOptions.get(this.isoIndex));
            this.rightRollerMoveListener.onISOSuccess((String) this.x8IsoOptions.get(this.isoIndex));
            X8CameraParamsValue.getInstance().getCurParamsJson().setAe_bias((String) this.x8IsoOptions.get(this.isoIndex));
        }
    }

    private void setX8EVOptions(boolean isDown) {
        this.evIndex = getListIndex(this.x8EVOptions, this.mX8MainBottomParameterController.getEvText().trim());
        if (this.evIndex != -1) {
            if (isDown) {
                this.evIndex--;
            } else {
                this.evIndex++;
            }
            if (this.evIndex <= 0) {
                this.evIndex = 0;
            }
            if (this.evIndex == this.x8EVOptions.size()) {
                this.evIndex = this.x8EVOptions.size() - 1;
            }
            this.cameraManager.setCameraEV(((String) this.x8EVOptions.get(this.evIndex)).equals("0.0") ? " 0.0" : (String) this.x8EVOptions.get(this.evIndex));
            this.rightRollerMoveListener.onEvSuccess((String) this.x8EVOptions.get(this.evIndex));
            this.mX8MainBottomParameterController.updateEvTextValue((String) this.x8EVOptions.get(this.evIndex));
            X8CameraParamsValue.getInstance().getCurParamsJson().setAe_bias((String) this.x8EVOptions.get(this.evIndex));
        }
    }

    public boolean isAutoMode() {
        String deControl = X8CameraParamsValue.getInstance().getCurParamsJson().getDe_control();
        return deControl == null || !deControl.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_MANUAL);
    }

    public int getListIndex(List<String> x8EVOptions, String text) {
        for (int i = 0; i < x8EVOptions.size(); i++) {
            if (text.equalsIgnoreCase((String) x8EVOptions.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
