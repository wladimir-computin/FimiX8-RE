package com.fimi.app.x8s.config;

import com.fimi.kernel.store.shared.SPStoreManager;

public class X8AiConfig {
    private static X8AiConfig instance = new X8AiConfig();
    private int aiFlyGravitationHeight;
    private int aiFlyGravitationLevel;
    private int aiFlyGravitationRotate;
    private boolean isAiAerialPhotographCourse;
    private boolean isAiAutoPhotoCustomCourse;
    private boolean isAiAutoPhotoVerticalCourse;
    private boolean isAiFixedwingCourse;
    private boolean isAiFlyGravitation;
    private boolean isAiFollowLockupCourse;
    private boolean isAiFollowNormalCourse;
    private boolean isAiFollowParallelCourse;
    private boolean isAiHeadingLock;
    private boolean isAiLineCourse;
    private boolean isAiLineCourseFpv;
    private boolean isAiLineCourseHistory;
    private boolean isAiP2PCourse;
    private boolean isAiSar;
    private boolean isAiScrew;
    private boolean isAiSurroundCourse;
    private boolean isAiTripodCourse;

    public void setAiFlyGravitationRotate(int clockwise) {
        this.aiFlyGravitationRotate = clockwise;
        SPStoreManager.getInstance().saveInt("aiFlyGravitationRotate", this.aiFlyGravitationRotate);
    }

    public int getAiFlyGravitationRotate() {
        return this.aiFlyGravitationRotate;
    }

    public void setAiFlyGravitationHeight(int height) {
        this.aiFlyGravitationHeight = height;
        SPStoreManager.getInstance().saveInt("aiFlyGravitationHeight", this.aiFlyGravitationHeight);
    }

    public int getAiFlyGravitationHeight() {
        return this.aiFlyGravitationHeight;
    }

    public void setAiFlyGravitationLevel(int level) {
        this.aiFlyGravitationLevel = level;
        SPStoreManager.getInstance().saveInt("aiFlyGravitationLevel", this.aiFlyGravitationLevel);
    }

    public int getAiFlyGravitationLevel() {
        return this.aiFlyGravitationLevel;
    }

    public boolean isAiFlyGravitation() {
        return this.isAiFlyGravitation;
    }

    public void setAiFlyGravitation(boolean aiFlyGravitation) {
        this.isAiFlyGravitation = aiFlyGravitation;
        SPStoreManager.getInstance().saveBoolean("isAiFlyGravitation", this.isAiFlyGravitation);
    }

    public boolean isAiSar() {
        return this.isAiSar;
    }

    public void setAiSar(boolean aiSar) {
        this.isAiSar = aiSar;
        SPStoreManager.getInstance().saveBoolean("isAiSar", this.isAiSar);
    }

    public boolean isAiScrew() {
        return this.isAiScrew;
    }

    public void setAiScrew(boolean aiScrew) {
        this.isAiScrew = aiScrew;
        SPStoreManager.getInstance().saveBoolean("isAiScrew", this.isAiScrew);
    }

    public boolean isAiTripodCourse() {
        return this.isAiTripodCourse;
    }

    public void setAiTripodCourse(boolean aiTripodCourse) {
        this.isAiTripodCourse = aiTripodCourse;
        SPStoreManager.getInstance().saveBoolean("isAiTripodCourse", this.isAiTripodCourse);
    }

    public boolean isAiAerialPhotographCourse() {
        return this.isAiAerialPhotographCourse;
    }

    public void setAiAerialPhotographCourse(boolean aiAerialPhotographCourse) {
        this.isAiAerialPhotographCourse = aiAerialPhotographCourse;
        SPStoreManager.getInstance().saveBoolean("isAiAerialPhotographCourse", this.isAiAerialPhotographCourse);
    }

    public boolean isAiFixedwingCourse() {
        return this.isAiFixedwingCourse;
    }

    public void setAiFixedwingCourse(boolean aiFixedwingCourse) {
        this.isAiFixedwingCourse = aiFixedwingCourse;
        SPStoreManager.getInstance().saveBoolean("isAiFixedwingCourse", this.isAiFixedwingCourse);
    }

    public boolean isAiHeadingLock() {
        return this.isAiHeadingLock;
    }

    public void setAiHeadingLock(boolean aiHeadingLock) {
        this.isAiHeadingLock = aiHeadingLock;
        SPStoreManager.getInstance().saveBoolean("isAiHeadingLock", this.isAiHeadingLock);
    }

    public static X8AiConfig getInstance() {
        return instance;
    }

    public void init() {
        this.isAiP2PCourse = SPStoreManager.getInstance().getBoolean("isAiP2PCourse", true);
        this.isAiLineCourse = SPStoreManager.getInstance().getBoolean("isAiLineCourse", true);
        this.isAiLineCourseFpv = SPStoreManager.getInstance().getBoolean("isAiLineCourseFpv", true);
        this.isAiLineCourseHistory = SPStoreManager.getInstance().getBoolean("isAiLineCourseHistory", true);
        this.isAiSurroundCourse = SPStoreManager.getInstance().getBoolean("isAiSurroundCourse", true);
        this.isAiAutoPhotoCustomCourse = SPStoreManager.getInstance().getBoolean("isAiAutoPhotoCustomCourse", true);
        this.isAiAutoPhotoVerticalCourse = SPStoreManager.getInstance().getBoolean("isAiAutoPhotoVerticalCourse", true);
        this.isAiFollowNormalCourse = SPStoreManager.getInstance().getBoolean("isAiFollowNormalCourse", true);
        this.isAiFollowParallelCourse = SPStoreManager.getInstance().getBoolean("isAiFollowParallelCourse", true);
        this.isAiFollowLockupCourse = SPStoreManager.getInstance().getBoolean("isAiFollowLockupCourse", true);
        this.isAiTripodCourse = SPStoreManager.getInstance().getBoolean("isAiTripodCourse", true);
        this.isAiAerialPhotographCourse = SPStoreManager.getInstance().getBoolean("isAiAerialPhotographCourse", true);
        this.isAiFixedwingCourse = SPStoreManager.getInstance().getBoolean("isAiFixedwingCourse", true);
        this.isAiHeadingLock = SPStoreManager.getInstance().getBoolean("isAiHeadingLock", true);
        this.isAiScrew = SPStoreManager.getInstance().getBoolean("isAiScrew", true);
        this.isAiSar = SPStoreManager.getInstance().getBoolean("isAiSar", true);
        this.isAiFlyGravitation = SPStoreManager.getInstance().getBoolean("isAiFlyGravitation", true);
        this.aiFlyGravitationLevel = SPStoreManager.getInstance().getInt("aiFlyGravitationLevel", 0);
        this.aiFlyGravitationHeight = SPStoreManager.getInstance().getInt("aiFlyGravitationHeight", 0);
        this.aiFlyGravitationRotate = SPStoreManager.getInstance().getInt("aiFlyGravitationRotate", 0);
    }

    public boolean isAiP2PCourse() {
        return this.isAiP2PCourse;
    }

    public void setAiP2PCourse(boolean aiP2PCourse) {
        this.isAiP2PCourse = aiP2PCourse;
        SPStoreManager.getInstance().saveBoolean("isAiP2PCourse", this.isAiP2PCourse);
    }

    public boolean isAiLineCourse() {
        return this.isAiLineCourse;
    }

    public void setAiLineCourse(boolean aiLineCourse) {
        this.isAiLineCourse = aiLineCourse;
        SPStoreManager.getInstance().saveBoolean("isAiLineCourse", aiLineCourse);
    }

    public boolean isAiLineCourseFpv() {
        return this.isAiLineCourseFpv;
    }

    public void setAiLineCourseFpv(boolean aiLineCourseFpv) {
        this.isAiLineCourseFpv = aiLineCourseFpv;
        SPStoreManager.getInstance().saveBoolean("isAiLineCourseFpv", this.isAiLineCourseFpv);
    }

    public boolean isAiLineCourseHistory() {
        return this.isAiLineCourseHistory;
    }

    public void setAiLineCourseHistory(boolean aiLineCourseHistory) {
        this.isAiLineCourseHistory = aiLineCourseHistory;
        SPStoreManager.getInstance().saveBoolean("isAiLineCourseHistory", this.isAiLineCourseHistory);
    }

    public boolean isAiSurroundCourse() {
        return this.isAiSurroundCourse;
    }

    public void setAiSurroundCourse(boolean aiSurroundCourse) {
        this.isAiSurroundCourse = aiSurroundCourse;
        SPStoreManager.getInstance().saveBoolean("isAiSurroundCourse", this.isAiSurroundCourse);
    }

    public boolean isAiAutoPhotoCustomCourse() {
        return this.isAiAutoPhotoCustomCourse;
    }

    public void setAiAutoPhotoCustomCourse(boolean aiAutoPhotoCustomCourse) {
        this.isAiAutoPhotoCustomCourse = aiAutoPhotoCustomCourse;
        SPStoreManager.getInstance().saveBoolean("isAiAutoPhotoCustomCourse", this.isAiAutoPhotoCustomCourse);
    }

    public boolean isAiAutoPhotoVerticalCourse() {
        return this.isAiAutoPhotoVerticalCourse;
    }

    public void setAiAutoPhotoVerticalCourse(boolean aiAutoPhotoVerticalCourse) {
        this.isAiAutoPhotoVerticalCourse = aiAutoPhotoVerticalCourse;
        SPStoreManager.getInstance().saveBoolean("isAiAutoPhotoVerticalCourse", this.isAiAutoPhotoVerticalCourse);
    }

    public boolean isAiFollowNormalCourse() {
        return this.isAiFollowNormalCourse;
    }

    public void setAiFollowNormalCourse(boolean aiFollowNormalCourse) {
        this.isAiFollowNormalCourse = aiFollowNormalCourse;
        SPStoreManager.getInstance().saveBoolean("isAiFollowNormalCourse", this.isAiFollowNormalCourse);
    }

    public boolean isAiFollowParallelCourse() {
        return this.isAiFollowParallelCourse;
    }

    public void setAiFollowParallelCourse(boolean aiFollowParallelCourse) {
        this.isAiFollowParallelCourse = aiFollowParallelCourse;
        SPStoreManager.getInstance().saveBoolean("isAiFollowParallelCourse", this.isAiFollowParallelCourse);
    }

    public boolean isAiFollowLockupCourse() {
        return this.isAiFollowLockupCourse;
    }

    public void setAiFollowLockupCourse(boolean aiFollowLockupCourse) {
        this.isAiFollowLockupCourse = aiFollowLockupCourse;
        SPStoreManager.getInstance().saveBoolean("isAiFollowLockupCourse", this.isAiFollowLockupCourse);
    }
}
