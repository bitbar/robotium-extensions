package com.bitbar.recorder.extensions;

import java.lang.reflect.Field;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class ProxyWebChromeClient extends WebChromeClient {
   
    private WebChromeClient originalWebChromeClient;
    private final HtmlUtils htmlUtils;
    
    public ProxyWebChromeClient(WebView webView, HtmlUtils htmlUtils) {
        this.htmlUtils = htmlUtils;
        try {
            Object finalWebView = webView;
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                Field mProvider = WebView.class.getDeclaredField("mProvider");
                mProvider.setAccessible(true);            
                finalWebView = mProvider.get(webView); //in API >= 16 webView contain provider: WebViewClassic
            }
            
            Field mCallbackProxyField = finalWebView.getClass().getDeclaredField("mCallbackProxy");
            mCallbackProxyField.setAccessible(true);
            Object mCallbackProxy = mCallbackProxyField.get(finalWebView);
            
            Field mWebChromeClientField = mCallbackProxy.getClass().getDeclaredField("mWebChromeClient");
            mWebChromeClientField.setAccessible(true);
            originalWebChromeClient = (WebChromeClient)mWebChromeClientField.get(mCallbackProxy);
            
            Log.i(ExtSolo.TAG, (String.format(Messages.ORIGINAL_WEB_CHROME_CLIENT, originalWebChromeClient)));
        } catch (Exception e) {
            Log.w(Messages.CANNOT_GET_ORIGINAL_WEB_CHROME_CLIENT, e);
        }
    }
    
    @Override
    public Bitmap getDefaultVideoPoster() {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.getDefaultVideoPoster();
        } else {
            return super.getDefaultVideoPoster();
        }                    
    }
    
    @Override
    public View getVideoLoadingProgressView() {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.getVideoLoadingProgressView();
        } else {
            return super.getVideoLoadingProgressView();
        }
    }
    
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.getVisitedHistory(callback);
        } else {
            super.getVisitedHistory(callback);
        }
    }
    
    @Override
    public void onCloseWindow(WebView window) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onCloseWindow(window);
        } else {
            super.onCloseWindow(window);
        }
    }
    
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
        } else {
            super.onConsoleMessage(message, lineNumber, sourceID);
        }
    }
    
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {        
        if (consoleMessage.message().startsWith(HtmlUtils.JS_RESULT_PREFIX)) {
            synchronized(htmlUtils) {            
                htmlUtils.setJSResult(consoleMessage.message().replaceFirst(HtmlUtils.JS_RESULT_PREFIX, ""));
                htmlUtils.notify();
            }
        }
        
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onConsoleMessage(consoleMessage);
        } else {
            return super.onConsoleMessage(consoleMessage);
        }
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        } else {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    }
    
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
            long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        } else {
            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        }
    }
    
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onGeolocationPermissionsHidePrompt();
        } else {
            super.onGeolocationPermissionsHidePrompt();
        }
    }
    
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
        } else {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }
    
    @Override
    public void onHideCustomView() {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onHideCustomView();
        } else {
            super.onHideCustomView();
        }
    }
    
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onJsAlert(view, url, message, result);
        } else {
            return super.onJsAlert(view, url, message, result);
        }
    }
    
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        if (originalWebChromeClient.onJsBeforeUnload(view, url, message, result)) {
            return originalWebChromeClient.onJsBeforeUnload(view, url, message, result);
        } else {
            return super.onJsBeforeUnload(view, url, message, result);
        }
    }
    
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onJsConfirm(view, url, message, result);
        } else {
            return super.onJsConfirm(view, url, message, result);
        }
    }
    
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);            
        } else {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }
    
    @Override
    public boolean onJsTimeout() {
        if (originalWebChromeClient != null) {
            return originalWebChromeClient.onJsTimeout();
        } else {
            return super.onJsTimeout();
        }
    }
    
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (originalWebChromeClient != null) {            
            originalWebChromeClient.onProgressChanged(view, newProgress);
        } else {
            super.onProgressChanged(view, newProgress);
        }
    }
    
    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        } else {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }
    }
    
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onReceivedIcon(view, icon);
        } else {
            super.onReceivedIcon(view, icon);
        }
    }
    
    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onReceivedTitle(view, title);
        } else {
            super.onReceivedTitle(view, title);
        }
    }
    
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
        } else {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }
    }
    
    @Override
    public void onRequestFocus(WebView view) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onRequestFocus(view);
        } else {
            super.onRequestFocus(view);
        }
    }
    
    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (originalWebChromeClient != null) {
            originalWebChromeClient.onShowCustomView(view, callback);
        } else {
            super.onShowCustomView(view, callback);
        }
    }
}