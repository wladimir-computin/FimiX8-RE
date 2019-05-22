package com.fimi.x8sdk.entity;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorCodeBean implements Serializable {
    private List<ActionBean> Configs = new ArrayList();
    private VersionBean Version;

    public class ActionBean {
        private List<ConditionValuesBean> ConditionValues = new ArrayList();
        private List<ConstraintBitBean> ConstraintBits = new ArrayList();
        private List<CtrlModeBean> CtrlMode = new ArrayList();
        private int Display;
        private List<FlightPhase> FlightPhase = new ArrayList();
        private String GroupID;
        private int Inhibition;
        private boolean IsInFlight;
        private String Label;
        private int OffsetBit;
        private int Severity;
        private int Speak;
        private int Text;
        private int Value;
        private boolean Vibrate;
        private boolean isShow;
        private boolean speaking;
        private boolean vibrating;

        public boolean isShow() {
            return this.isShow;
        }

        public void setShow(boolean show) {
            this.isShow = show;
        }

        public void setSpeaking(boolean isSpeak) {
            this.speaking = isSpeak;
        }

        public boolean isVibrating() {
            return this.vibrating;
        }

        public void setVibrating(boolean vibrating) {
            this.vibrating = vibrating;
        }

        public String getLabel() {
            return this.Label;
        }

        public void setLabel(String label) {
            this.Label = label;
        }

        public int getValue() {
            return this.Value;
        }

        public void setValue(int value) {
            this.Value = value;
        }

        public List<ConditionValuesBean> getConditionValues() {
            return this.ConditionValues;
        }

        public void setConditionValues(List<ConditionValuesBean> conditionValues) {
            this.ConditionValues = conditionValues;
        }

        public List<ConstraintBitBean> getConstraintBits() {
            return this.ConstraintBits;
        }

        public void setConstraintBits(List<ConstraintBitBean> constraintBits) {
            this.ConstraintBits = constraintBits;
        }

        public List<FlightPhase> getFlightPhase() {
            return this.FlightPhase;
        }

        public void setFlightPhase(List<FlightPhase> flightPhase) {
            this.FlightPhase = flightPhase;
        }

        public List<CtrlModeBean> getCtrlMode() {
            return this.CtrlMode;
        }

        public void setCtrlMode(List<CtrlModeBean> ctrlMode) {
            this.CtrlMode = ctrlMode;
        }

        public String getGroupID() {
            return this.GroupID;
        }

        public void setGroupID(String groupID) {
            this.GroupID = groupID;
        }

        public int getOffsetBit() {
            return this.OffsetBit;
        }

        public void setOffsetBit(int offsetBit) {
            this.OffsetBit = offsetBit;
        }

        public int getSeverity() {
            return this.Severity;
        }

        public void setSeverity(int severity) {
            this.Severity = severity;
        }

        public int getText() {
            return this.Text;
        }

        public void setText(int text) {
            this.Text = text;
        }

        public boolean isVibrate() {
            return this.Vibrate;
        }

        public void setVibrate(boolean vibrate) {
            this.Vibrate = vibrate;
        }

        public boolean isInFlight() {
            return this.IsInFlight;
        }

        public void setIsInFlight(boolean inFlight) {
            this.IsInFlight = inFlight;
        }

        public int getSpeak() {
            return this.Speak;
        }

        public void setSpeak(int speak) {
            this.Speak = speak;
        }

        public int getInhibition() {
            return this.Inhibition;
        }

        public void setInhibition(int inhibition) {
            this.Inhibition = inhibition;
        }

        public int getDisplay() {
            return this.Display;
        }

        public void setDisplay(int display) {
            this.Display = display;
        }

        public boolean equals(Object o) {
            ActionBean b = (ActionBean) o;
            boolean isGroup = this.GroupID.equals(b.getGroupID());
            boolean isOffSetBit = getOffsetBit() == b.getOffsetBit();
            boolean isSeverity = getSeverity() == b.getSeverity();
            boolean isText = getText() == b.getText();
            boolean isSpeak = getSpeak() == b.getSpeak();
            boolean isVibrate = isVibrate() == b.isVibrate();
            boolean isInSky = isInFlight() == b.isInFlight();
            boolean isValue = getValue() == b.getValue();
            boolean isCheckBits = getConstraintBits().size() == b.getConstraintBits().size();
            if (isCheckBits) {
                isCheckBits = getConstraintBits().containsAll(b.getConstraintBits());
            }
            boolean isCheckFligthPhas = getFlightPhase().size() == b.getFlightPhase().size();
            if (isCheckFligthPhas) {
                isCheckFligthPhas = getFlightPhase().containsAll(b.getFlightPhase());
            }
            boolean isCheckCtrlMode = getCtrlMode().size() == b.getCtrlMode().size();
            if (isCheckCtrlMode) {
                isCheckCtrlMode = getCtrlMode().containsAll(b.getCtrlMode());
            }
            boolean isCheckConditionValuesBean = getConditionValues().size() == b.getConditionValues().size();
            if (isCheckConditionValuesBean) {
                isCheckConditionValuesBean = getConditionValues().containsAll(b.getConditionValues());
            }
            if (isGroup && isOffSetBit && isSeverity && isText && isSpeak && isVibrate && isInSky && isVibrate && isValue && isCheckBits && isCheckFligthPhas && isCheckCtrlMode && isCheckConditionValuesBean) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "ActionBean{speaking=" + this.speaking + ", vibrating=" + this.vibrating + ", GroupID='" + this.GroupID + CoreConstants.SINGLE_QUOTE_CHAR + ", Label='" + this.Label + CoreConstants.SINGLE_QUOTE_CHAR + ", OffsetBit=" + this.OffsetBit + ", Severity=" + this.Severity + ", Text=" + this.Text + ", Speak=" + this.Speak + ", Vibrate=" + this.Vibrate + ", IsInFlight=" + this.IsInFlight + ", Value=" + this.Value + CoreConstants.CURLY_RIGHT;
        }
    }

    public static class ConditionValuesBean {
        private String GroupID;
        private boolean IsEqual;
        private int Value;

        public String getGroupID() {
            return this.GroupID;
        }

        public void setGroupID(String groupID) {
            this.GroupID = groupID;
        }

        public boolean isEqual() {
            return this.IsEqual;
        }

        public void setIsEqual(boolean equal) {
            this.IsEqual = equal;
        }

        public int getValue() {
            return this.Value;
        }

        public void setValue(int value) {
            this.Value = value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConditionValuesBean that = (ConditionValuesBean) o;
            if (this.Value == that.getValue() && this.GroupID.equals(that.getGroupID()) && this.IsEqual == that.isEqual()) {
                return true;
            }
            return false;
        }
    }

    public static class ConstraintBitBean {
        private int BitOffset;
        private String GroupID;
        private boolean Value;

        public String getGroupID() {
            return this.GroupID;
        }

        public void setGroupID(String groupID) {
            this.GroupID = groupID;
        }

        public int getBitOffset() {
            return this.BitOffset;
        }

        public void setBitOffset(int bitOffset) {
            this.BitOffset = bitOffset;
        }

        public boolean isValue() {
            return this.Value;
        }

        public void setValue(boolean value) {
            this.Value = value;
        }

        public String toString() {
            return "ConstraintBit{GroupID='" + this.GroupID + CoreConstants.SINGLE_QUOTE_CHAR + ", BitOffset=" + this.BitOffset + ", Value=" + this.Value + CoreConstants.CURLY_RIGHT;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConstraintBitBean that = (ConstraintBitBean) o;
            if (this.BitOffset == that.getBitOffset() && this.GroupID.equals(that.getGroupID()) && this.Value == that.isValue()) {
                return true;
            }
            return false;
        }
    }

    public static class CtrlModeBean {
        private boolean IsEqual;
        private int Value;

        public boolean isEqual() {
            return this.IsEqual;
        }

        public void setIsEqual(boolean equal) {
            this.IsEqual = equal;
        }

        public int getValue() {
            return this.Value;
        }

        public void setValue(int value) {
            this.Value = value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CtrlModeBean that = (CtrlModeBean) o;
            if (this.Value == that.getValue() && this.IsEqual == that.isEqual()) {
                return true;
            }
            return false;
        }
    }

    public static class FlightPhase {
        private boolean IsEqual;
        private int Value;

        public boolean isEqual() {
            return this.IsEqual;
        }

        public void setIsEqual(boolean equal) {
            this.IsEqual = equal;
        }

        public int getValue() {
            return this.Value;
        }

        public void setValue(int value) {
            this.Value = value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FlightPhase that = (FlightPhase) o;
            if (this.Value == that.getValue() && this.IsEqual == that.isEqual()) {
                return true;
            }
            return false;
        }
    }

    public class VersionBean {
        private String Content;
        private String Format;

        public String getFormat() {
            return this.Format;
        }

        public void setFormat(String format) {
            this.Format = format;
        }

        public String getContent() {
            return this.Content;
        }

        public void setContent(String content) {
            this.Content = content;
        }

        public String toString() {
            return "VersionBean{Format='" + this.Format + CoreConstants.SINGLE_QUOTE_CHAR + ", Content='" + this.Content + CoreConstants.SINGLE_QUOTE_CHAR + CoreConstants.CURLY_RIGHT;
        }
    }

    public VersionBean getVersion() {
        return this.Version;
    }

    public void setVersion(VersionBean version) {
        this.Version = version;
    }

    public List<ActionBean> getConfigs() {
        return this.Configs;
    }

    public void setConfigs(List<ActionBean> configs) {
        this.Configs = configs;
    }

    public String toString() {
        return "ErrorCodeBean{Version=" + this.Version + ", Configs=" + this.Configs + CoreConstants.CURLY_RIGHT;
    }
}
