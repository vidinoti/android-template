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

public class WebFragment extends Fragment {

    private static final String ARG_KEY_URL = "com.vidinoti.vdarsdk.URL";

    private WebView webView;
    private String url;
    private WebFragmentDelegate delegate;

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
        if (args != null) {
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
        setupWebViewClient();
    }

    private void setupWebViewClient() {
        WebViewClient webViewClient = null;
        if (delegate != null) {
            webViewClient = delegate.getWebViewClient();
        }
        if (webViewClient == null) {
            webViewClient = new VidinotiWebViewClient(new VidinotiWebViewClientDelegate() {
                @Override
                public Activity getActivity() {
                    return WebFragment.this.getActivity();
                }
            });
        }
        webView.setWebViewClient(webViewClient);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.loadUrl(url);
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
