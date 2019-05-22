package com.fimi.x8sdk.dataparser.cmd;

public class CmdAiLinePointsAction {
    public int cmd0;
    public int cmd1;
    public int count;
    public int para0;
    public int para1;
    public int pos;
    public int time;

    public enum Cmd {
        NULL,
        HOVER,
        PHOTO,
        VIDEO,
        SLOW_VIDEO,
        PANORAMA
    }
}
