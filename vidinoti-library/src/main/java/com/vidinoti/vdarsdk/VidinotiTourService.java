package com.vidinoti.vdarsdk;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VidinotiTourService {

    private static final String TOUR_API_ENDPOINT = "https://sdk.vidinoti.com/v1/tour";
    private static VidinotiTourService instance = null;

    private final RequestQueue queue;
    private final Map<String, String> headers;

    private VidinotiTourService(Context context) {
        queue = Volley.newRequestQueue(context);

        headers = new HashMap<>();
        headers.put("PixLiveSDK-AppId", context.getPackageName());
        headers.put("PixLiveSDK-Platform", "android");
        headers.put("PixLiveSDK-LicenseKey", VidinotiAR.getInstance().getOptions().getLicenseKey());
    }

    public static VidinotiTourService getInstance() {
        if (instance == null) {
            instance = new VidinotiTourService(VidinotiAR.getInstance().getContext());
        }
        return instance;
    }

    public void getTour(long id, ResponseCallback<VidinotiTour> successCallback, ResponseCallback<Exception> errorCallback) {
        String url = TOUR_API_ENDPOINT + "/" + id;
        Request<VidinotiTour> request = new GsonRequest<>(url, VidinotiTour.class, headers, successCallback::onResponse, errorCallback::onResponse);
        queue.add(request);
    }

}
