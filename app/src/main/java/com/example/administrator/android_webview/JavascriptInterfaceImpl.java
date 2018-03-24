package com.example.administrator.android_webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/24.
 */
// 提供与JavaScript交互接口
public class JavascriptInterfaceImpl {
    private final String TAG = JavascriptInterfaceImpl.class.getSimpleName();
    private Context mContext;
    private WebView mWebView;
    private Handler mHandler;

    /** Instantiate the interface and set the context */
    JavascriptInterfaceImpl(Context c, WebView webView) {
        mContext = c;
        mWebView = webView;
        mHandler = new Handler(Looper.getMainLooper());//与主线程的Looper绑定
    }

    /** Show a toast from the web page */
    // 如果target 大于等于API 17，则需要加上如下注解
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }

    /**
     * 同步方法
     * @return
     */
    @JavascriptInterface
    public String syncExec() {
        return "hello android";
    }

    /**
     * 异步方法
     * @param msg
     * @param callbackId
     */
    @JavascriptInterface
    public void asyncExec(final String msg, final String callbackId) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(5 * 1000);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = "javascript:" + callbackId + "('" + msg
                                + " from android " + "')";
                        mWebView.loadUrl(url);
                    }
                });
            }
        }.start();
    }

    /**
     * 接收JavaScript的异步消息
     * @param msg
     */
    @JavascriptInterface
    public void callBack(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
