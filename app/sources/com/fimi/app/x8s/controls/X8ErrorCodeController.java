package com.fimi.app.x8s.controls;

import android.view.View;
import com.alibaba.fastjson.JSON;
import com.fimi.TcpClient;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.X8ErrorCode;
import com.fimi.app.x8s.enums.X8ErrorCodeEnum;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.manager.X8ErrerCodeSpeakFlashManager;
import com.fimi.app.x8s.widget.X8ErrorCodeLayout;
import com.fimi.kernel.ttsspeak.SpeakTTs;
import com.fimi.kernel.ttsspeak.SpeakTTs.SpeakStatusListener;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.x8sdk.entity.ErrorCodeBean;
import com.fimi.x8sdk.entity.ErrorCodeBean.ActionBean;
import com.fimi.x8sdk.entity.ErrorCodeBean.ConditionValuesBean;
import com.fimi.x8sdk.entity.ErrorCodeBean.ConstraintBitBean;
import com.fimi.x8sdk.entity.ErrorCodeBean.CtrlModeBean;
import com.fimi.x8sdk.entity.ErrorCodeBean.FlightPhase;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;
import com.fimi.x8sdk.modulestate.StateManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class X8ErrorCodeController extends AbsX8Controllers {
    private ErrorCodeBean bean;
    public List<ActionBean> currentMap = new ArrayList();
    private Map<String, String> errCodeDesc = new HashMap();
    private X8ErrorCodeLayout errorCodeLayout;
    private boolean isGetData;
    private List<X8ErrorCodeInfo> list;
    private X8ErrerCodeSpeakFlashManager mX8ErrerCodeSpeakFlashManager;
    private List<ActionBean> mediumMap = new ArrayList();
    private List<ActionBean> seriousMap = new ArrayList();
    private SpeakStatusListener speakStatusListener = new SpeakStatusListener() {
        public void speakFinish(boolean finish) {
            if (finish) {
                X8ErrorCodeController.this.mX8ErrerCodeSpeakFlashManager.speakFinish();
            }
        }
    };

    public X8ErrorCodeController(View rootView) {
        super(rootView);
    }

    public void initViews(View rootView) {
        this.errorCodeLayout = (X8ErrorCodeLayout) rootView.findViewById(R.id.v_error_code);
        this.bean = getLocalError();
        this.mX8ErrerCodeSpeakFlashManager = new X8ErrerCodeSpeakFlashManager(rootView.getContext(), this);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00b2  */
    private void initErrCodeDes() {
        /*
        r13 = this;
        r10 = com.fimi.kernel.GlobalConfig.getInstance();
        r7 = r10.getLanguageModel();
        r3 = 0;
        r0 = 0;
        r6 = 0;
        r10 = "cn";
        r11 = r7.getInternalCoutry();	 Catch:{ IOException -> 0x00ed }
        r10 = r10.equalsIgnoreCase(r11);	 Catch:{ IOException -> 0x00ed }
        if (r10 == 0) goto L_0x00b6;
    L_0x0017:
        r4 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x00ed }
        r10 = r13.rootView;	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getContext();	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getResources();	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getAssets();	 Catch:{ IOException -> 0x00ed }
        r11 = "zh.txt";
        r10 = r10.open(r11);	 Catch:{ IOException -> 0x00ed }
        r4.<init>(r10);	 Catch:{ IOException -> 0x00ed }
        r3 = r4;
    L_0x0032:
        r8 = "(\\d+)\\s*=\\s*(.*)\\s*";
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x00ed }
        r1.<init>(r3);	 Catch:{ IOException -> 0x00ed }
    L_0x0039:
        r6 = r1.readLine();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        if (r6 == 0) goto L_0x00d2;
    L_0x003f:
        r10 = android.text.TextUtils.isEmpty(r6);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        if (r10 != 0) goto L_0x0039;
    L_0x0045:
        r10 = r6.matches(r8);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        if (r10 == 0) goto L_0x0039;
    L_0x004b:
        r10 = 0;
        r11 = "=";
        r11 = r6.indexOf(r11);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = r6.substring(r10, r11);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = r10.trim();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = java.lang.Integer.valueOf(r10);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r5 = r10.intValue();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = "=";
        r10 = r6.indexOf(r10);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = r10 + 1;
        r11 = r6.length();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = r6.substring(r10, r11);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11 = "\"";
        r12 = "";
        r10 = r10.replace(r11, r12);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11 = ";";
        r12 = "";
        r9 = r10.replace(r11, r12);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10 = r13.errCodeDesc;	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        if (r10 != 0) goto L_0x008d;
    L_0x0086:
        r10 = new java.util.HashMap;	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10.<init>();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r13.errCodeDesc = r10;	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
    L_0x008d:
        r10 = r13.errCodeDesc;	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11.<init>();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11 = r11.append(r5);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r12 = "";
        r11 = r11.append(r12);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r11 = r11.toString();	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        r10.put(r11, r9);	 Catch:{ IOException -> 0x00a6, all -> 0x00ea }
        goto L_0x0039;
    L_0x00a6:
        r2 = move-exception;
        r0 = r1;
    L_0x00a8:
        r2.printStackTrace();	 Catch:{ all -> 0x00de }
        if (r3 == 0) goto L_0x00b0;
    L_0x00ad:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r3);
    L_0x00b0:
        if (r0 == 0) goto L_0x00b5;
    L_0x00b2:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r0);
    L_0x00b5:
        return;
    L_0x00b6:
        r4 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x00ed }
        r10 = r13.rootView;	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getContext();	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getResources();	 Catch:{ IOException -> 0x00ed }
        r10 = r10.getAssets();	 Catch:{ IOException -> 0x00ed }
        r11 = "en.txt";
        r10 = r10.open(r11);	 Catch:{ IOException -> 0x00ed }
        r4.<init>(r10);	 Catch:{ IOException -> 0x00ed }
        r3 = r4;
        goto L_0x0032;
    L_0x00d2:
        if (r3 == 0) goto L_0x00d7;
    L_0x00d4:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r3);
    L_0x00d7:
        if (r1 == 0) goto L_0x00ef;
    L_0x00d9:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r1);
        r0 = r1;
        goto L_0x00b5;
    L_0x00de:
        r10 = move-exception;
    L_0x00df:
        if (r3 == 0) goto L_0x00e4;
    L_0x00e1:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r3);
    L_0x00e4:
        if (r0 == 0) goto L_0x00e9;
    L_0x00e6:
        com.fimi.kernel.animutils.IOUtils.closeQuietly(r0);
    L_0x00e9:
        throw r10;
    L_0x00ea:
        r10 = move-exception;
        r0 = r1;
        goto L_0x00df;
    L_0x00ed:
        r2 = move-exception;
        goto L_0x00a8;
    L_0x00ef:
        r0 = r1;
        goto L_0x00b5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.app.x8s.controls.X8ErrorCodeController.initErrCodeDes():void");
    }

    public void initActions() {
    }

    public void defaultVal() {
    }

    public void onInteruptForUdating() {
        onDismissErrorCode();
    }

    public void onDroneConnected(boolean b) {
        if (!b) {
            onDismissErrorCode();
        }
    }

    public void onDismissErrorCode() {
        this.isGetData = false;
        if (this.list != null && this.list.size() > 0) {
            this.errorCodeLayout.cleanAll();
            resetSpeakFlag1();
            resetSpeakFlag2();
            this.mX8ErrerCodeSpeakFlashManager.disconnect();
            this.list.clear();
        }
    }

    public void resetSpeakFlag1() {
        for (ActionBean actionBean : this.seriousMap) {
            actionBean.setSpeaking(false);
            actionBean.setVibrating(false);
        }
        this.seriousMap.clear();
    }

    public void resetSpeakFlag2() {
        for (ActionBean actionBean : this.mediumMap) {
            actionBean.setSpeaking(false);
            actionBean.setVibrating(false);
            TcpClient.getIntance().sendLog(actionBean.toString());
        }
        this.mediumMap.clear();
    }

    public void onErrorCode(List<X8ErrorCodeInfo> list) {
        if (!this.isGetData) {
            this.list = list;
            if (this.errCodeDesc == null || this.errCodeDesc.size() == 0) {
                initErrCodeDes();
            }
            this.mX8ErrerCodeSpeakFlashManager.start();
        }
    }

    public synchronized void onErrorCode3(List<X8ErrorCodeInfo> list) {
        this.isGetData = true;
        if (list.size() > 0) {
            this.currentMap.clear();
            for (int i = 0; i < list.size(); i++) {
                ActionBean action = getErrorDesBean((X8ErrorCodeInfo) list.get(i), StateManager.getInstance().getX8Drone().isInSky(), StateManager.getInstance().getX8Drone().getCtrlMode(), StateManager.getInstance().getX8Drone().getAutoFcHeart().getFlightPhase(), list);
                if (action != null) {
                    this.currentMap.add(action);
                    if (action.getSeverity() == 2) {
                        if (!this.seriousMap.contains(action)) {
                            this.seriousMap.add(action);
                        }
                    } else if (!this.mediumMap.contains(action)) {
                        this.mediumMap.add(action);
                    }
                }
            }
            removeSeriousDismissCode();
            removeMediumDismissCode();
        } else {
            resetSpeakFlag1();
            resetSpeakFlag2();
        }
        this.isGetData = false;
    }

    public void removeSeriousDismissCode() {
        Iterator<ActionBean> iterator = this.seriousMap.iterator();
        while (iterator.hasNext()) {
            ActionBean actionBean = (ActionBean) iterator.next();
            boolean isFind = false;
            for (int i = 0; i < this.currentMap.size(); i++) {
                if (actionBean.equals(this.currentMap.get(i))) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                this.mX8ErrerCodeSpeakFlashManager.removeSeriousMap(actionBean);
                iterator.remove();
            }
        }
    }

    public void removeMediumDismissCode() {
        Iterator<ActionBean> iterator = this.mediumMap.iterator();
        while (iterator.hasNext()) {
            ActionBean actionBean = (ActionBean) iterator.next();
            boolean isFind = false;
            for (int i = 0; i < this.currentMap.size(); i++) {
                if (actionBean.equals(this.currentMap.get(i))) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                this.mX8ErrerCodeSpeakFlashManager.removeMediumMap(actionBean);
                iterator.remove();
            }
        }
    }

    public String getErrorCodeString(int key) {
        return (String) this.errCodeDesc.get(key + "");
    }

    public String getString(int id) {
        return this.rootView.getContext().getString(id);
    }

    private ErrorCodeBean getLocalError() {
        String jsonStr = null;
        try {
            jsonStr = FileUtil.fileToString(this.rootView.getContext().getResources().getAssets().open("Alarms.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonStr != null) {
            ErrorCodeBean errorCodeBean = (ErrorCodeBean) JSON.parseObject(jsonStr, ErrorCodeBean.class);
            if (errorCodeBean != null) {
                return errorCodeBean;
            }
        }
        return null;
    }

    private String getErrorType(int type) {
        switch (type) {
            case 0:
                return "FCS-A";
            case 1:
                return "FCS-B";
            case 2:
                return "FCS-C";
            case 3:
                return "MTC";
            case 4:
                return "ATC";
            case 5:
                return "RCS";
            case 6:
                return "NFZS";
            default:
                return "";
        }
    }

    public int getMacthType(int type) {
        switch (type) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 1;
            case 5:
                return 0;
            case 6:
                return 1;
            default:
                return -1;
        }
    }

    public ActionBean getErrorDesBean(X8ErrorCodeInfo info, boolean isInSky, int ctrlMode, int flightPhasw, List<X8ErrorCodeInfo> list) {
        ActionBean b = null;
        List<ActionBean> result = new ArrayList();
        int matchType = getMacthType(info.getType());
        if (matchType == -1) {
            return null;
        }
        if (matchType == 0) {
            for (ActionBean action : this.bean.getConfigs()) {
                if (getErrorType(info.getType()).equals(action.getGroupID()) && action.getOffsetBit() == info.getIndex() && action.isInFlight() == isInSky) {
                    result.add(action);
                }
            }
        } else if (matchType == 1) {
            for (ActionBean action2 : this.bean.getConfigs()) {
                if (getErrorType(info.getType()).equals(action2.getGroupID()) && action2.getValue() == info.getValue() && action2.isInFlight() == isInSky) {
                    result.add(action2);
                }
            }
        }
        for (ActionBean action22 : result) {
            boolean isFind = false;
            if (action22.getCtrlMode().size() > 0) {
                if (((CtrlModeBean) action22.getCtrlMode().get(0)).isEqual()) {
                    if (((CtrlModeBean) action22.getCtrlMode().get(0)).getValue() == ctrlMode) {
                        b = action22;
                    }
                } else if (checkCtrlModeNotEqual(action22.getCtrlMode(), ctrlMode)) {
                    b = action22;
                }
            } else if (action22.getFlightPhase().size() <= 0) {
                if (action22.getConstraintBits().size() > 0) {
                    isFind = true;
                    if (checkConstraintBitBeans(action22.getConstraintBits(), list)) {
                        b = action22;
                        break;
                    }
                }
                if (action22.getConditionValues().size() > 0) {
                    isFind = true;
                    if (checkConditionValueBeans(action22.getConditionValues(), list)) {
                        b = action22;
                        break;
                    }
                }
                if (!isFind) {
                    b = action22;
                }
            } else if (((FlightPhase) action22.getFlightPhase().get(0)).isEqual()) {
                if (((FlightPhase) action22.getFlightPhase().get(0)).getValue() == flightPhasw) {
                    b = action22;
                }
            } else if (checkFlightPhaseNotEqual(action22.getFlightPhase(), flightPhasw)) {
                b = action22;
            }
        }
        return b;
    }

    private boolean checkConditionValueBeans(List<ConditionValuesBean> conditionValues, List<X8ErrorCodeInfo> list) {
        int size = conditionValues.size();
        int k = 0;
        for (ConditionValuesBean m : conditionValues) {
            boolean isEqual = m.isEqual();
            for (int i = 0; i < list.size(); i++) {
                X8ErrorCodeInfo c = (X8ErrorCodeInfo) list.get(i);
                if (!isEqual) {
                    if (getErrorType(c.getType()).equals(m.getGroupID())) {
                        break;
                    }
                    k++;
                } else if (getErrorType(c.getType()).equals(m.getGroupID())) {
                    if (m.getValue() == c.getIndex()) {
                        k++;
                    }
                }
            }
        }
        if (k == size) {
            return true;
        }
        return false;
    }

    public boolean checkCtrlModeNotEqual(List<CtrlModeBean> ctrlMode, int mode) {
        for (CtrlModeBean m : ctrlMode) {
            if (mode == m.getValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkFlightPhaseNotEqual(List<FlightPhase> flightPhases, int flightPhasw) {
        for (FlightPhase m : flightPhases) {
            if (flightPhasw == m.getValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkConstraintBitBeans(List<ConstraintBitBean> constraintBits, List<X8ErrorCodeInfo> list) {
        int k = 0;
        for (ConstraintBitBean m : constraintBits) {
            boolean isValue = m.isValue();
            boolean isFind = false;
            for (int i = 0; i < list.size(); i++) {
                X8ErrorCodeInfo c = (X8ErrorCodeInfo) list.get(i);
                if (getErrorType(c.getType()).equals(m.getGroupID()) && m.getBitOffset() == c.getIndex()) {
                    isFind = true;
                    break;
                }
            }
            if (isValue == isFind) {
                k++;
            }
        }
        if (k == constraintBits.size()) {
            return true;
        }
        return false;
    }

    public boolean onClickBackKey() {
        return false;
    }

    public boolean hasErrorCode() {
        return hasSeriousCode() || hasMediumCode();
    }

    public boolean hasSeriousCode() {
        onErrorCode3(this.list);
        return this.seriousMap.size() > 0;
    }

    public boolean hasSeriousCode2() {
        return this.seriousMap.size() > 0;
    }

    public boolean hasMediumCode() {
        onErrorCode3(this.list);
        return this.mediumMap.size() > 0;
    }

    public boolean hasMediumCode2() {
        return this.mediumMap.size() > 0;
    }

    public ActionBean getSeriousCode() {
        int i = 0;
        int size = this.seriousMap.size();
        ActionBean action = null;
        for (ActionBean entry : this.seriousMap) {
            if (!entry.isShow()) {
                action = entry;
                entry.setShow(true);
                break;
            }
            i++;
        }
        if (i != size) {
            return action;
        }
        for (ActionBean entry2 : this.seriousMap) {
            entry2.setShow(false);
        }
        return (ActionBean) this.seriousMap.get(0);
    }

    public ActionBean getMediumCode() {
        int i = 0;
        int size = this.mediumMap.size();
        ActionBean action = null;
        for (ActionBean entry : this.mediumMap) {
            if (!entry.isShow()) {
                action = entry;
                entry.setShow(true);
                break;
            }
            i++;
        }
        if (i != size) {
            return action;
        }
        for (ActionBean entry2 : this.mediumMap) {
            entry2.setShow(false);
        }
        return (ActionBean) this.mediumMap.get(0);
    }

    public void showTextByCode(X8ErrorCode code) {
        this.errorCodeLayout.addErrorCode(code);
        TcpClient.getIntance().sendLog("addErrorCode ");
    }

    public void runEnd(X8ErrorCodeEnum type) {
        TcpClient.getIntance().sendLog("runEnd " + type);
        if (X8ErrorCodeEnum.serious == type) {
            this.errorCodeLayout.cleanLevel1();
            resetSpeakFlag1();
        } else if (X8ErrorCodeEnum.medium == type) {
            this.errorCodeLayout.cleanLevel0();
            resetSpeakFlag2();
        }
    }

    public void initSpeak() {
        if (this.rootView.getContext() != null && this.speakStatusListener != null) {
            try {
                SpeakTTs.obtain(this.rootView.getContext()).addSpeaksListener(this.speakStatusListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDroneStateErrorByLable(String flag) {
        String lable;
        if (this.seriousMap.size() > 0) {
            for (ActionBean entry : this.seriousMap) {
                lable = entry.getLabel();
                if (lable != null && lable.equals(flag)) {
                    return true;
                }
            }
        }
        if (this.mediumMap.size() <= 0) {
            return false;
        }
        for (ActionBean entry2 : this.mediumMap) {
            lable = entry2.getLabel();
            if (lable != null && lable.equals(flag)) {
                return true;
            }
        }
        return false;
    }
}
