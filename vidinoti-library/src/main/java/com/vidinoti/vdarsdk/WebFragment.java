package com.vidinoti.vdarsdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@SuppressWarnings("unused")
public class WebFragment extends Fragment {

    private static final String ARG_KEY_URL = "com.vidinoti.vdarsdk.URL";

    private WebView webView;
    private String url;

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY_URL, url);
        fragment.setArguments(bundle);
        return fragment;
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
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.loadUrl(url);
    }
}
