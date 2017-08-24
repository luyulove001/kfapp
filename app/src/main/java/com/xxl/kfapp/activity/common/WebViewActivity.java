package com.xxl.kfapp.activity.common;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.baidu.mobstat.StatService;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.webview)
    WebView webView;

    private String title, url;

    @Override
    protected void initArgs(Intent intent) {
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setTitle(title);
    }

    @Override
    protected void initData() {
        initWebView();
        StatService.bindJSInterface(this, webView);
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        webView.getSettings().setAllowFileAccess(true); // 允许访问文件
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.loadUrl(url);
    }
}
