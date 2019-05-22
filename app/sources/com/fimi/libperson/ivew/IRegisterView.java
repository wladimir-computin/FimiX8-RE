package com.fimi.libperson.ivew;

public interface IRegisterView {
    void getCodeResult(boolean z, String str);

    void loginSuccess();

    void registerEmailResult(boolean z, String str);

    void registerIphoneResult(boolean z, String str);

    void updateSeconds(boolean z, int i);
}
