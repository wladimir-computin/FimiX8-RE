package com.fimi.libperson.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.fimi.libperson.entity.ImageSource;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executor;

public class BitmapLoadTaskInstance {
    private static final String TAG = "BitmapLoadTaskInstance";
    private static BitmapLoadTaskInstance sBitmapLoadTaskInstance;
    private Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
    private Bitmap mBitmap;
    private OnLoadListener mOnLoadListener;

    public interface OnLoadListener {
        void onComplete();
    }

    private static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        private Bitmap bitmap;
        private final WeakReference<Context> contextRef;
        private Exception exception;
        private final boolean preview;
        private final Uri source;

        public BitmapLoadTask(Context context, Uri source, boolean preview) {
            this.contextRef = new WeakReference(context);
            this.source = source;
            this.preview = preview;
        }

        /* Access modifiers changed, original: protected|varargs */
        public Integer doInBackground(Void... voids) {
            try {
                this.bitmap = BitmapLoadTaskInstance.sBitmapLoadTaskInstance.decode((Context) this.contextRef.get(), this.source);
                return Integer.valueOf(1);
            } catch (Exception var6) {
                Log.e(BitmapLoadTaskInstance.TAG, "Failed to load bitmap", var6);
                this.exception = var6;
            } catch (OutOfMemoryError var7) {
                Log.e(BitmapLoadTaskInstance.TAG, "Failed to load bitmap - OutOfMemoryError", var7);
                this.exception = new RuntimeException(var7);
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Integer orientation) {
            if (this.bitmap != null) {
                BitmapLoadTaskInstance.sBitmapLoadTaskInstance.onImageLoaded(this.bitmap);
            }
        }
    }

    public void setImage(ImageSource source, Context context) {
        execute(new BitmapLoadTask(context, source.getUri(), false));
    }

    private void execute(AsyncTask<Void, Void, ?> asyncTask) {
        asyncTask.executeOnExecutor(this.executor, new Void[0]);
    }

    public static BitmapLoadTaskInstance getInstance() {
        if (sBitmapLoadTaskInstance == null) {
            sBitmapLoadTaskInstance = new BitmapLoadTaskInstance();
        }
        return sBitmapLoadTaskInstance;
    }

    private synchronized void onImageLoaded(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onComplete();
        }
    }

    public Bitmap decode(Context context, Uri uri) throws Exception {
        Bitmap bitmap;
        String uriString = uri.toString();
        Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        if (uriString.startsWith("android.resource://")) {
            Resources res;
            String packageName = uri.getAuthority();
            if (context.getPackageName().equals(packageName)) {
                res = context.getResources();
            } else {
                res = context.getPackageManager().getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && ((String) segments.get(0)).equals("drawable")) {
                id = res.getIdentifier((String) segments.get(1), "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly((CharSequence) segments.get(0))) {
                try {
                    id = Integer.parseInt((String) segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        } else if (uriString.startsWith("file:///android_asset/")) {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(uriString.substring("file:///android_asset/".length())), (Rect) null, options);
        } else if (uriString.startsWith("file://")) {
            bitmap = BitmapFactory.decodeFile(uriString.substring("file://".length()), options);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream, (Rect) null, options);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e3) {
                    }
                }
            }
        }
        if (bitmap != null) {
            return bitmap;
        }
        throw new RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported");
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setRecyle() {
        if (!(this.mBitmap == null || this.mBitmap.isRecycled())) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener = null;
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }
}
