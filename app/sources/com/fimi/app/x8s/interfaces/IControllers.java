package com.fimi.app.x8s.interfaces;

import android.view.View;

public interface IControllers {
    void closeUi();

    void defaultVal();

    void initActions();

    void initViews(View view);

    boolean onClickBackKey();

    void openUi();
}
