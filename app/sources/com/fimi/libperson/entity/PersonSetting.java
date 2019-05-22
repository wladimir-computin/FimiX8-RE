package com.fimi.libperson.entity;

import com.fimi.libperson.adapter.PersettingFourAdapt.State;
import com.fimi.libperson.adapter.PersettingSecondAdapt;
import com.fimi.libperson.adapter.PersettingThirdAdapt;
import com.fimi.libperson.adapter.PersonSettingAdapt;
import java.util.Observable;

public class PersonSetting extends Observable {
    private String content;
    private boolean displayTv = false;
    private int id;
    private boolean isCheckedButton;
    private Boolean isOPen = Boolean.valueOf(false);
    private State mFourAdapt;
    private PersettingSecondAdapt.State mSecondAdapt;
    private PersonSettingAdapt.State mSettingAdaptState;
    private PersettingThirdAdapt.State mThirdAdapt;
    private int newHandModel;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
        setChanged();
        notifyObservers();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonSettingAdapt.State getSettingAdaptState() {
        return this.mSettingAdaptState;
    }

    public void setSettingAdaptState(PersonSettingAdapt.State mSettingAdaptState) {
        this.mSettingAdaptState = mSettingAdaptState;
    }

    public PersettingSecondAdapt.State getSecondAdapt() {
        return this.mSecondAdapt;
    }

    public void setSecondAdapt(PersettingSecondAdapt.State secondAdapt) {
        this.mSecondAdapt = secondAdapt;
    }

    public PersettingThirdAdapt.State getThirdAdapt() {
        return this.mThirdAdapt;
    }

    public void setThirdAdapt(PersettingThirdAdapt.State thirdAdapt) {
        this.mThirdAdapt = thirdAdapt;
    }

    public State getFourAdapt() {
        return this.mFourAdapt;
    }

    public void setFourAdapt(State fourAdapt) {
        this.mFourAdapt = fourAdapt;
    }

    public Boolean getIsOPen() {
        return this.isOPen;
    }

    public void setIsOPen(Boolean isOPen) {
        if (this.isOPen != null) {
            if ((!isOPen.booleanValue()) == this.isOPen.booleanValue()) {
                setChanged();
                notifyObservers();
                this.isOPen = isOPen;
            }
        }
    }

    public boolean isDisplayTv() {
        return this.displayTv;
    }

    public void setDisplayTv(boolean displayTv) {
        this.displayTv = displayTv;
    }

    public boolean isCheckedButton() {
        return this.isCheckedButton;
    }

    public void setCheckedButton(boolean checkedButton) {
        this.isCheckedButton = checkedButton;
    }

    public int getNewHandModel() {
        return this.newHandModel;
    }

    public void setNewHandModel(int newHandModel) {
        this.newHandModel = newHandModel;
    }
}
