package com.fimi.kernel.utils;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoaderManager {
    private static final int CONNECTION_TIME_OUT = 5000;
    private static final int DISK_CACHE_SIZE = 52428800;
    private static final int MEMORY_CACHE_SIZE = 2097152;
    private static final int PRIORITY = 2;
    private static final int READ_TIME_OUT = 30000;
    private static final int THREAD_COUNT = 4;
    private static ImageLoaderManager mInstance = null;
    private static ImageLoader mLoader = null;

    public static ImageLoaderManager getInstance(Context context, int emptyUri) {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager(context, emptyUri);
                }
            }
        }
        return mInstance;
    }

    private ImageLoaderManager(Context context, int emptyUri) {
        ImageLoader.getInstance().init(new Builder(context).threadPoolSize(4).threadPriority(3).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).diskCacheSize(DISK_CACHE_SIZE).diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(getDefaultOptions(emptyUri)).imageDownloader(new BaseImageDownloader(context, 5000, 30000)).writeDebugLogs().build());
        mLoader = ImageLoader.getInstance();
    }

    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, listener);
        }
    }

    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }

    private DisplayImageOptions getDefaultOptions(int emptyUri) {
        return new DisplayImageOptions.Builder().showImageForEmptyUri(emptyUri).showImageOnFail(emptyUri).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Config.RGB_565).decodingOptions(new Options()).resetViewBeforeLoading(true).build();
    }
}
