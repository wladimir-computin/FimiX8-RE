package com.fimi.app.x8s.interfaces;

public interface IX8AiFlyListener {
    void onAiAutoPhotoConfirmClick(int i);

    void onAiFollowConfirmClick(int i);

    void onAiGravitationConfimClick();

    void onAiLandingConfirmClick();

    void onAiLineConfirmClick(int i);

    void onAiLineConfirmClickByHistory(int i, long j, int i2);

    void onAiPoint2PointConfirmClick();

    void onAiSarConfimClick();

    void onAiScrewConfimCick();

    void onAiSurroundPointConfirmClick();

    void onAiTakeOffConfirmClick();

    void onCloseAiUi(boolean z, boolean z2);
}
