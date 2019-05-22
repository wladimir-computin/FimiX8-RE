package com.fimi.kernel.fds;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.BaseManager;
import com.fimi.network.entity.NetModel;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FDSClient extends BaseManager {
    private Call call;
    private Zip2Fds mZip2Fds;
    private Object object = new Object();
    private Call postFdsUrlCall;
    private Call reqeuestFdsUrlCall;

    public void startUpload(IFdsFileModel model, IFdsUploadListener listener) throws IOException {
        this.mZip2Fds = new Zip2Fds();
        boolean b = this.mZip2Fds.log2Zip(model.getFile(), model.getNeedZipFileBySuffix());
        Log.i("istep", "mZip2Fds=" + b);
        this.mZip2Fds = null;
        if (b) {
            model.setZipFile(new File(model.getFile().getAbsolutePath() + "/" + (model.getFile().getName() + ".zip")));
            requesetFdsUrl(model.getZipFile().getName(), model);
            if (model.getFileFdsUrl() == null || model.getFileFdsUrl().equals("")) {
                model.setState(FdsUploadState.FAILED);
                return;
            }
            upload2Fds(model, listener);
            if (FdsUploadState.FAILED != model.getState() && FdsUploadState.STOP != model.getState()) {
                saveFdsUrl2Fimi(model.getZipFile().getName(), model);
                return;
            }
            return;
        }
        model.setState(FdsUploadState.FAILED);
    }

    public void requesetFdsUrl(String fileName, final IFdsFileModel model) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("bucketName", "x8-fclog");
        requestParams.put("objectName", fileName.replace(".zip", "-" + UUID.randomUUID() + ".zip"));
        this.reqeuestFdsUrlCall = CommonOkHttpClient.get(CommonRequest.createGetRequest(HostConstants.QUESET_FDS_URL + "getGeneratePresignedUri", getRequestParams(requestParams)), new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.isSuccess()) {
                    model.setFileFdsUrl(netModel.getData().toString());
                }
                FDSClient.this.releaseLocked();
            }

            public void onFailure(Object reasonObj) {
                FDSClient.this.releaseLocked();
            }
        }));
        lockedModel();
    }

    public void upload2Fds(IFdsFileModel model, IFdsUploadListener listener) throws IOException {
        String url = model.getFileFdsUrl();
        this.call = new OkHttpClient().newCall(new Builder().header(OAuthConstants.HEADER_AUTHORIZATION, "Client-ID " + UUID.randomUUID()).url(url).put(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", model.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), new File(model.getZipFile().getAbsolutePath()))).build()).build());
        model.setState(FdsUploadState.LOADING);
        listener.onProgress(model, 0, model.getFile().length());
        Response response = this.call.execute();
        if (response.isSuccessful()) {
            Log.i("istep", "ResponseBody " + response.toString() + " " + model.getFile().getName());
            return;
        }
        Log.i("istep", "Unexpected code " + response);
        model.setState(FdsUploadState.FAILED);
    }

    public void lockedModel() {
        synchronized (this.object) {
            try {
                this.object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseLocked() {
        synchronized (this.object) {
            try {
                this.object.notify();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("istep", "Exception   " + e.toString());
            }
        }
    }

    public void saveFdsUrl2Fimi(String fileName, final IFdsFileModel model) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("droneId", "x8");
        requestParams.put("logOwnerId", HostConstants.getUserDetail().getFimiId());
        requestParams.put("fileFdsUrl", model.getFileFdsUrl());
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        try {
            name = new SimpleDateFormat(HostConstants.FORMATDATE).format(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").parse(name));
        } catch (Exception e) {
        }
        requestParams.put("createFileTime", name);
        this.postFdsUrlCall = CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.SAVE_FDS_URL_2_FIMI_URL + "uploadFlyLog", getRequestParams(requestParams)), new DisposeDataHandle(new DisposeDataListener() {
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                Log.i("istep", "createFileTime " + netModel.toString());
                if (netModel.isSuccess()) {
                    model.setState(FdsUploadState.SUCCESS);
                    FDSClient.this.rename(model);
                } else {
                    model.setState(FdsUploadState.FAILED);
                }
                FDSClient.this.releaseLocked();
            }

            public void onFailure(Object reasonObj) {
                Log.i("istep", "onFailure " + reasonObj.toString());
                model.setState(FdsUploadState.FAILED);
                FDSClient.this.releaseLocked();
            }
        }));
        lockedModel();
    }

    public void stopUpload(IFdsFileModel model, IFdsUploadListener listener) {
        if (this.mZip2Fds != null) {
            this.mZip2Fds.stop();
            model.setState(FdsUploadState.STOP);
            this.mZip2Fds = null;
        }
        if (this.reqeuestFdsUrlCall != null) {
            this.reqeuestFdsUrlCall.cancel();
            model.setState(FdsUploadState.STOP);
            this.reqeuestFdsUrlCall = null;
        }
        if (this.call != null) {
            this.call.cancel();
            model.setState(FdsUploadState.STOP);
            listener.onStop(model);
            this.call = null;
        }
        if (this.postFdsUrlCall != null) {
            this.postFdsUrlCall.cancel();
            model.setState(FdsUploadState.STOP);
            this.postFdsUrlCall = null;
        }
    }

    public boolean rename(IFdsFileModel model) {
        File file = model.getFile();
        boolean b = file.renameTo(new File(file.getAbsolutePath() + model.getFileSuffix()));
        model.resetFile(file);
        return b;
    }
}
