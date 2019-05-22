package com.fimi.kernel.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoUtils {
    public static void displayPhoto(SimpleDraweeView mSimpleDraweeView, String path, int width, int height) {
        if (!TextUtils.isEmpty(path)) {
            PipelineDraweeControllerBuilder mPipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
            ImageRequestBuilder mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path));
            mImageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
            mPipelineDraweeControllerBuilder.setOldController(mSimpleDraweeView.getController());
            mPipelineDraweeControllerBuilder.setImageRequest(mImageRequestBuilder.build());
            mSimpleDraweeView.setController(mPipelineDraweeControllerBuilder.build());
        }
    }

    public static void displayPhoto(SimpleDraweeView mSimpleDraweeView, String path, int width, int height, ControllerListener mControllerListener) {
        if (!TextUtils.isEmpty(path)) {
            PipelineDraweeControllerBuilder mPipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
            ImageRequestBuilder mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path));
            mImageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
            mPipelineDraweeControllerBuilder.setOldController(mSimpleDraweeView.getController());
            mPipelineDraweeControllerBuilder.setImageRequest(mImageRequestBuilder.build());
            mPipelineDraweeControllerBuilder.setControllerListener(mControllerListener);
            mSimpleDraweeView.setController(mPipelineDraweeControllerBuilder.build());
        }
    }
}
