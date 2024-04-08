package com.vidinoti.vdarsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment implements VidinotiLoadingManager.LoadingInterface {

    private static final String ARG_KEY_URL = "com.vidinoti.vdarsdk.URL";

    private final VidinotiLoadingManager loadingManager;

    private WebView webView;
    private View loadingView;
    public String url;
    private WebFragmentDelegate delegate;
    private boolean loadUrlExecuted = false;

    public interface WebFragmentDelegate {
        @Nullable
        WebViewClient getWebViewClient();
    }

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    public WebFragment() {
        this.loadingManager = new VidinotiLoadingManager(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WebFragmentDelegate) {
            delegate = (WebFragmentDelegate) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_KEY_URL)) {
            url = args.getString(ARG_KEY_URL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vidinoti_web_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.webView);
        loadingView = view.findViewById(R.id.progressView);
        setupWebViewClient();
    }

    private void setupWebViewClient() {
        WebViewClient webViewClient = null;
        if (delegate != null) {
            webViewClient = delegate.getWebViewClient();
        }
        if (webViewClient == null) {
            webViewClient = getWebViewClient();
        }
        if (webViewClient == null) {
            webViewClient = new VidinotiWebViewClient(WebFragment.this::getActivity);
        }
        webView.setWebViewClient(webViewClient);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!loadUrlExecuted) {
            loadUrlExecuted = true;
            reload();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadUrlExecuted = false;
    }

    public void beforeLoadUrl() {

    }

    public void reload() {
        beforeLoadUrl();
        webView.loadUrl(url);
    }

    public WebView getWebView() {
        return webView;
    }

    protected WebViewClient getWebViewClient() {
        return null;
    }

    public void startLoading() {
        loadingManager.startLoading();
    }

    public void stopLoading() {
        loadingManager.stopLoading();
    }

    /**
     * Sets the loading percentage
     * @param percent the percentage (between 0 and 1.0)
     */
    public void setLoadingProgress(double percent) {
        loadingManager.setProgress(percent);
    }

    public void runOnUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(runnable);
    }

    @Override
    public void setLoadingViewVisible(boolean visible) {
        if (visible) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    private interface VidinotiWebViewClientDelegate {
        Activity getActivity();
    }

    private static class VidinotiWebViewClient extends WebViewClient {

        private final VidinotiWebViewClientDelegate delegate;

        VidinotiWebViewClient(VidinotiWebViewClientDelegate delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.toLowerCase().startsWith("external")) {
                String externalUrl = url.substring(8);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl));
                Activity activity = delegate.getActivity();
                if (activity != null) {
                    activity.startActivity(intent);
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
