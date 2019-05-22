package com.fimi.album.biz;

import android.graphics.drawable.Animatable;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

public class FrescoControllerListener implements ControllerListener<ImageInfo> {
    public void onSubmit(String id, Object callerContext) {
    }

    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
    }

    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
    }

    public void onIntermediateImageFailed(String id, Throwable throwable) {
    }

    public void onFailure(String id, Throwable throwable) {
    }

    public void onRelease(String id) {
    }
}
