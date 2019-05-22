package com.fimi.app.x8s.interfaces;

import android.view.View;
import com.fimi.app.x8s.X8Application;

public abstract class AbsX8AiController extends AbsX8Controllers {
    public AbsX8AiController(View rootView) {
        super(rootView);
    }

    public void openUi() {
        super.openUi();
        X8Application.enableGesture = false;
    }

    public void closeUi() {
        super.closeUi();
        X8Application.enableGesture = true;
    }
}
