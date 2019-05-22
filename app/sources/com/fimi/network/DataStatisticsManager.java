package com.fimi.network;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.entity.DataStatistics;
import java.util.List;

public class DataStatisticsManager extends BaseManager {
    private static DataStatisticsManager sDataStatisticsManager;

    public DataStatisticsManager(Context context) {
    }

    public static DataStatisticsManager getIntance(Context context) {
        if (sDataStatisticsManager == null) {
            sDataStatisticsManager = new DataStatisticsManager(context.getApplicationContext());
        }
        return sDataStatisticsManager;
    }

    public void submitGh2UseRecord(List<DataStatistics> dataStatisticsList, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dataStatisticsList).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.HostURL + "v1/gh2record/" + "submitGh2UseRecord", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void submitGh2version(DataStatistics dataStatistics, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dataStatistics).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.HostURL + "v1/gh2v/" + "submitGh2version", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void submitX9flyTime(List<DataStatistics> dataStatisticsList, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dataStatisticsList).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.HostURL + "v1/x9fly/" + "submitX9flyTime", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void submitX9UseRecord(List<DataStatistics> dataStatisticsList, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dataStatisticsList).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.HostURL + "v1/x9record/" + "submitX9UseRecord", getRequestParams(requestParams)), disposeDataHandle);
    }

    public void submitX9Version(DataStatistics dataStatistics, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        dataStatistics.setProductModel("1");
        requestParams.put("jsonContent", JSON.toJSON(dataStatistics).toString());
        CommonOkHttpClient.post(CommonRequest.createPostRequest(HostConstants.HostURL + "/v1/x9v/" + "submitX9Version", getRequestParams(requestParams)), disposeDataHandle);
    }
}
