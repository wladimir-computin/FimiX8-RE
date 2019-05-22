package com.fimi.kernel.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.util.Log;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Sets;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineConfig.Builder;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequest.CacheChoice;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoImageLoader {
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";
    private static final int MAX_DISK_CACHE_SIZE = 41943040;
    private static final int MAX_HEAP_SIZE = (((int) Runtime.getRuntime().maxMemory()) / 8);
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE;
    private static final int MAX_POOL_SIZE = 41943040;
    private static final String TAG = "FrescoImageLoader";
    private static ImagePipelineConfig sImagePipelineConfig;

    public static void display(SimpleDraweeView view, String uri) {
        display(view, uri, null);
    }

    public static void display(SimpleDraweeView view, String lowUri, String uri, ControllerListener listener) {
        if (uri != null) {
            ImageRequestBuilder imageRequest;
            int w = view.getLayoutParams().width;
            int h = view.getLayoutParams().height;
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            if (!lowUri.startsWith("file:///")) {
                lowUri = "file://" + lowUri;
            }
            if (lowUri != null && lowUri.length() > 0) {
                controller.setLowResImageRequest(ImageRequest.fromUri(lowUri));
            }
            controller.setOldController(view.getController());
            controller.setAutoPlayAnimations(true);
            if (uri.endsWith(".MP4")) {
                imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(lowUri));
            } else {
                imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
            }
            if (w > 0 && h > 0) {
                imageRequest.setResizeOptions(new ResizeOptions(w, h));
            }
            controller.setImageRequest(imageRequest.build());
            controller.setControllerListener(listener);
            view.setController(controller.build());
        }
    }

    public static void display(SimpleDraweeView view, String uri, ControllerListener listener) {
        if (uri == null) {
            uri = "";
        }
        view.setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).setLocalThumbnailPreviewsEnabled(true).build())).setOldController(view.getController())).setControllerListener(listener)).build());
    }

    public static void display(SimpleDraweeView view, String uri, int width, int height) {
        display(view, uri, width, height, null);
    }

    public static void display(SimpleDraweeView view, String uri, int width, int height, ControllerListener listener) {
        if (uri != null) {
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setOldController(view.getController());
            controller.setAutoPlayAnimations(true);
            ImageRequestBuilder imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).setCacheChoice(CacheChoice.SMALL).setLocalThumbnailPreviewsEnabled(true);
            imageRequest.setProgressiveRenderingEnabled(true);
            Log.d("Good", uri);
            if (width > 0 && height > 0) {
                imageRequest.setResizeOptions(new ResizeOptions(width, height));
            }
            controller.setImageRequest(imageRequest.build());
            controller.setControllerListener(listener);
            view.setController(controller.build());
        }
    }

    public static void initFresco(Context context) {
        Fresco.initialize(context, getImagePipelineConfig(context));
    }

    public static void shutdown() {
        Fresco.shutDown();
    }

    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configureCaches(configBuilder, context);
            configureLoggingListeners(configBuilder);
            sImagePipelineConfig = configBuilder.build();
        }
        return sImagePipelineConfig;
    }

    private static void configureCaches(Builder configBuilder, Context context) {
        MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(MAX_MEMORY_CACHE_SIZE, Integer.MAX_VALUE, MAX_MEMORY_CACHE_SIZE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        configBuilder.setDownsampleEnabled(true).setBitmapsConfig(Config.RGB_565).setBitmapMemoryCacheParamsSupplier(new MyBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService("activity"))).setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(context.getApplicationContext().getCacheDir()).setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR).setMaxCacheSize(41943040).build());
    }

    private static void configureLoggingListeners(Builder configBuilder) {
        configBuilder.setRequestListeners(Sets.newHashSet(new RequestLoggingListener()));
    }

    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
    }
}
