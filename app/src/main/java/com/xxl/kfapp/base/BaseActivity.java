package com.xxl.kfapp.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import talex.zsw.baselibrary.util.ACache;
import talex.zsw.baselibrary.util.StringUtils;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;
import talex.zsw.baselibrary.widget.RichText;

/**
 * 作者：xnn
 * 描述:
 */
@SuppressWarnings("deprecation")
public abstract class BaseActivity extends AppCompatActivity {
    // TODO: 2017/7/21  loading view show in http get/post
    private boolean is = false;

    protected abstract void initArgs(Intent intent);

    protected abstract void initView(Bundle bundle);

    protected abstract void initData();

    protected BaseApplication mApplication;
    private InputMethodManager mInputMethodManager;

    public ACache mACache;
    public Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        mApplication.addActivity(this);
        if (mACache == null) {
            mACache = ACache.get(this);
        }
        if (mGson == null) {
            mGson = new Gson();
        }
        mInputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            initArgs(getIntent());
            initView(savedInstanceState);
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @Override
    protected void onDestroy() {
        setContentView(talex.zsw.baselibrary.R.layout.activity_empty);
        mInputMethodManager = null;
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
            sweetAlertDialog = null;
        }
        mApplication.removeActivity(this);
        mACache = null;
        mGson = null;
        super.onDestroy();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    /**
     * 获取Application
     */
    protected BaseApplication getAppApplication() {
        if (null == mApplication) {
            mApplication = (BaseApplication) getApplication();
        }
        return mApplication;
    }


    /**
     * 隐藏键盘
     */
    public void hideInputMethod() {
        try {
            mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ignored) {

        }
    }

    /**
     * @return 获取屏幕高度
     */
    public int getScrnHeight() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * @return 获取屏幕宽度
     */
    public int getScrnWeight() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 提示信息
     */
    public void ToastShow(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 公共的加载提示对话框
     */
    public SweetAlertDialog sweetAlertDialog;

    /**
     * 成功 错误 警告 异常 提示框(只用来更改提示信息和状态类型和设置是否可以返回取消对话框)
     */
    public void sweetDialog(String information, int type, boolean cancelable) {
        if (sweetAlertDialog == null || !sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        //0正常,1错误,2成功,3警告,5进度条
        sweetAlertDialog.setTitleText(information);
        sweetAlertDialog.changeAlertType(type);
        sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
        sweetAlertDialog.setConfirmText("确定");
        sweetAlertDialog.show();
    }
    public void sweetDialog(String information, int type, boolean cancelable, SweetAlertDialog.OnSweetClickListener confirmListener) {
        if (sweetAlertDialog == null || !sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        //0正常,1错误,2成功,3警告,5进度条
        sweetAlertDialog.setTitleText(information);
        sweetAlertDialog.changeAlertType(type);
        sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
        sweetAlertDialog.setConfirmText("确定");
        if (confirmListener != null) {
            sweetAlertDialog.setConfirmClickListener(confirmListener);
        }
        sweetAlertDialog.show();
    }

    public void sweetContextDialog(String information, String context, int type, boolean cancelable) {
        if (sweetAlertDialog == null || !sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        //0正常,1错误,2成功,3警告,5进度条
        sweetAlertDialog.setTitleText(information);
        sweetAlertDialog.setContentText(context);
        sweetAlertDialog.changeAlertType(type);
        sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
        sweetAlertDialog.setConfirmText("确定");
        sweetAlertDialog.show();
    }


    public void sweetDialogCustom(int type, String title, String content, String confirmText,
                                  String cancelText, SweetAlertDialog.OnSweetClickListener confirmListener,
                                  SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.changeAlertType(type);
        } else {
            sweetAlertDialog = new SweetAlertDialog(BaseActivity.this, type);
            sweetAlertDialog.setCancelable(false);
        }
        if (!StringUtils.isBlank(title)) {
            sweetAlertDialog.setTitleText(title);
        }
        if (!StringUtils.isBlank(content)) {
            sweetAlertDialog.setContentText(content);
        }
        if (!StringUtils.isBlank(confirmText)) {
            sweetAlertDialog.setConfirmText(confirmText);
        }
        if (!StringUtils.isBlank(cancelText)) {
            sweetAlertDialog.setCancelText(cancelText);
        }
        if (confirmListener != null) {
            sweetAlertDialog.setConfirmClickListener(confirmListener);
        }
        if (confirmListener != null) {
            sweetAlertDialog.setCancelClickListener(cancelListener);
        }
        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    /**
     * 关闭提示框
     */
    public void closeDialog() {
        if (sweetAlertDialog == null) {
            return;
        }
        sweetAlertDialog.dismiss();
    }

    public void setWebData(String content, WebView mWebView, RichText mRichText,
                           final ProgressBar mProgressBar) {
        // 设置WebView的属性，此时可以去执行JavaScript脚本`
        mWebView.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        mWebView.getSettings().setAllowFileAccess(true); // 允许访问文件
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        //		mWebView.getSettings().setDefaultFontSize( (int) (46 / scale) );
        //		mWebView.getSettings().setMinimumFontSize( (int) (38 / scale) );
        mWebView.getSettings().setSupportZoom(false);// 支持缩放
        mWebView.getSettings().setBuiltInZoomControls(false); // 设置显示缩放按钮
        //		mWebView.getSettings().setUseWideViewPort(true);
        //		mWebView.getSettings().setLoadWithOverviewMode(true);
        //		mWebView.setInitialScale(960 * 100 / getScrnHeight());

        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
            default:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
        }
        mWebView.getSettings().setDefaultZoom(zoomDensity);
        if (StringUtils.isBlank(content)) {
            mWebView.setVisibility(View.GONE);
            mRichText.setVisibility(View.VISIBLE);
            mRichText.setText("无信息");
        } else if (content.startsWith("http://") || content.startsWith("https://") ||
                content.startsWith("wap")) {
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(100);
            // 当webview里面能点击是 在当前页面上显示！
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            // 重写webview的值
            mWebView.setWebChromeClient(new WebChromeClient() {
                // 加载网页的进度条
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    mProgressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        if (mProgressBar.getVisibility() == View.GONE) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            // pb.setProgress(newProgress);
                        }
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });
            mWebView.loadUrl(content);
        } else {
            content = "<style>\n" +
                    "    img {\n" +
                    "        max-width: 100%;\n" +
                    "        width: 100%;\n" +
                    "        height: auto\n" +
                    "    }\n" +
                    "    \n" +
                    "    div {\n" +
                    "        width: 100%;\n" +
                    "        max-width: 100%;\n" +
                    "        height: auto\n" +
                    "    }\n" +
                    "    \n" +
                    "    p {\n" +
                    "        width: 100%;\n" +
                    "        max-width: 100%;\n" +
                    "        height: auto\n" +
                    "    }\n" +
                    "</style>\n" +
                    content + "<script type=\"text/javascript\">" +
                    "var imgs = document.getElementsByTagName('img');" +
                    "for(var i = 0; i<tables.length; i++){" +  // 逐个改变
                    "imgs[i].style.width = '100%';" +  // 宽度改为100%
                    "imgs[i].style.height = 'auto';" +
                    "}" +
                    "var ps = document.getElementsByTagName('p');" +
                    "for(var i = 0; i<tables.length; i++){" +  // 逐个改变
                    "ps[i].style.width = '100%';" +  // 宽度改为100%
                    "ps[i].style.height = 'auto';" +
                    "}" +
                    "</script>";
            String regEx = "</?[^>]+>";
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            boolean rs = mat.find();
            if (rs) {
                if (content.contains("https"))//如果含有包括https
                {
                    WebViewClient mWebviewclient = new WebViewClient() {
                        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                                       SslError error) {
                            handler.proceed();
                            //handler.cancel(); 默认的处理方式，WebView变成空白页
                            //handler.process();接受证书
                            //handleMessage(Message msg); 其他处理
                        }
                    };
                    mWebView.setWebViewClient(mWebviewclient);
                } else// 当webview里面能点击是 在当前页面上显示！
                {
                    mWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
                mWebView.loadData(content, "text/html; charset=UTF-8", null);
                // mWebView.loadData(fmtString(content), "text/html", "utf-8");
            } else {
                mWebView.setVisibility(View.GONE);
                mRichText.setVisibility(View.VISIBLE);
                mRichText.setRichText(content);
            }
        }
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView(mWebView);
        }
    }

    @TargetApi(11)
    private void fixWebView(WebView mWebView) {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
    }

    private static long mLastClickTime;
    public static final int MIN_CLICK_DELAY_TIME = 300;

    public static boolean isFastClick() {
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 两次点击的时间差
        long time = currentTime - mLastClickTime;
        if (0 < time && time < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }


}
