package com.fimi.app.x8s.tensortfloow;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Trace;
import com.fimi.app.x8s.tensortfloow.Classifier.Recognition;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.tensorflow.lite.Interpreter;

public class TFLiteObjectDetectionAPIModel implements Classifier {
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;
    private static final int NUM_DETECTIONS = 2;
    private static final int NUM_THREADS = 6;
    private ByteBuffer imgData;
    private int inputSize;
    private int[] intValues;
    private boolean isModelQuantized;
    private Vector<String> labels = new Vector();
    private float[] numDetections;
    private float[][] outputClasses;
    private float[][][] outputLocations;
    private float[][] outputScores;
    private Interpreter tfLite;

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        return new FileInputStream(fileDescriptor.getFileDescriptor()).getChannel().map(MapMode.READ_ONLY, fileDescriptor.getStartOffset(), fileDescriptor.getDeclaredLength());
    }

    public static Classifier create(AssetManager assetManager, String modelFilename, String labelFilename, int inputSize, boolean isQuantized) throws IOException {
        TFLiteObjectDetectionAPIModel d = new TFLiteObjectDetectionAPIModel();
        BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(labelFilename.split("file:///android_asset/")[1])));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            d.labels.add(line);
        }
        br.close();
        d.inputSize = inputSize;
        try {
            int numBytesPerChannel;
            d.tfLite = new Interpreter(loadModelFile(assetManager, modelFilename));
            d.isModelQuantized = isQuantized;
            if (isQuantized) {
                numBytesPerChannel = 1;
            } else {
                numBytesPerChannel = 4;
            }
            d.imgData = ByteBuffer.allocateDirect((((d.inputSize * 1) * d.inputSize) * 3) * numBytesPerChannel);
            d.imgData.order(ByteOrder.nativeOrder());
            d.intValues = new int[(d.inputSize * d.inputSize)];
            d.tfLite.setNumThreads(6);
            d.outputLocations = (float[][][]) Array.newInstance(Float.TYPE, new int[]{1, 2, 4});
            d.outputClasses = (float[][]) Array.newInstance(Float.TYPE, new int[]{1, 2});
            d.outputScores = (float[][]) Array.newInstance(Float.TYPE, new int[]{1, 2});
            d.numDetections = new float[1];
            return d;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TFLiteObjectDetectionAPIModel() {
    }

    public List<Recognition> recognizeImage(Bitmap bitmap) {
        int i;
        Trace.beginSection("recognizeImage");
        Trace.beginSection("preprocessBitmap");
        bitmap.getPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.imgData.rewind();
        for (i = 0; i < this.inputSize; i++) {
            for (int j = 0; j < this.inputSize; j++) {
                int pixelValue = this.intValues[(this.inputSize * i) + j];
                if (this.isModelQuantized) {
                    this.imgData.put((byte) ((pixelValue >> 16) & 255));
                    this.imgData.put((byte) ((pixelValue >> 8) & 255));
                    this.imgData.put((byte) (pixelValue & 255));
                } else {
                    this.imgData.putFloat((((float) ((pixelValue >> 16) & 255)) - 128.0f) / 128.0f);
                    this.imgData.putFloat((((float) ((pixelValue >> 8) & 255)) - 128.0f) / 128.0f);
                    this.imgData.putFloat((((float) (pixelValue & 255)) - 128.0f) / 128.0f);
                }
            }
        }
        Trace.endSection();
        Trace.beginSection("feed");
        this.outputLocations = (float[][][]) Array.newInstance(Float.TYPE, new int[]{1, 2, 4});
        this.outputClasses = (float[][]) Array.newInstance(Float.TYPE, new int[]{1, 2});
        this.outputScores = (float[][]) Array.newInstance(Float.TYPE, new int[]{1, 2});
        this.numDetections = new float[1];
        Object[] inputArray = new Object[]{this.imgData};
        Map<Integer, Object> outputMap = new HashMap();
        outputMap.put(Integer.valueOf(0), this.outputLocations);
        outputMap.put(Integer.valueOf(1), this.outputClasses);
        outputMap.put(Integer.valueOf(2), this.outputScores);
        outputMap.put(Integer.valueOf(3), this.numDetections);
        Trace.endSection();
        Trace.beginSection("run");
        this.tfLite.runForMultipleInputsOutputs(inputArray, outputMap);
        Trace.endSection();
        ArrayList<Recognition> arrayList = new ArrayList(2);
        for (i = 0; i < 2; i++) {
            RectF detection = new RectF(this.outputLocations[0][i][1] * ((float) this.inputSize), this.outputLocations[0][i][0] * ((float) this.inputSize), this.outputLocations[0][i][3] * ((float) this.inputSize), this.outputLocations[0][i][2] * ((float) this.inputSize));
            int nClass = (int) this.outputClasses[0][i];
            if (nClass >= 0 && nClass <= 90) {
                arrayList.add(new Recognition("" + i, (String) this.labels.get(nClass + 1), Float.valueOf(this.outputScores[0][i]), detection));
            }
        }
        Trace.endSection();
        return arrayList;
    }

    public void enableStatLogging(boolean logStats) {
    }

    public String getStatString() {
        return "";
    }

    public void close() {
    }
}
