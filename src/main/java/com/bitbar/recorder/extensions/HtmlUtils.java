package com.bitbar.recorder.extensions;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import junit.framework.AssertionFailedError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;

class HtmlUtils {

    public static String JS_RESULT_PREFIX = "EXTOSOLO_JS_RESULT:";

    private static final int TIMEOUT_WEB_VIEW_FULLY_LOADED = 10000;
    private static final int TIMEOUT_FOR_WEBVIEW = 60000;
    private static final int INTERVAL_FOR_CHECK = 500;
    private static final int TIMEOUT_FOR_JS_RESPONSE = 2000;

    private ExtSolo extSolo;

    private WebView webView;
    private String jsResult;
    private String methodName;
    private String className;
    private Object browserFrame;
    private Activity currentActivity;

    HtmlUtils(ExtSolo extSolo, String className, String methodName) {
        this.extSolo = extSolo;
        this.className = className;
        this.methodName = methodName;
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        if (currentActivity == null) {
            currentActivity = extSolo.getCurrentActivity();
        }

        return currentActivity;
    }

    protected boolean elementExistsOnHtmlPage(
            String tag, String id, String name, String className, int index, String xPath,
            Map<String, String> customAttributes, String selector) {
        return elementExistsOnHtmlPage(getWebView(), tag, id, name, className, index, xPath, customAttributes, selector);
    }

    protected boolean elementExistsOnHtmlPage(
            WebView webview, String tag, String id, String name, String className, int index, String xPath,
            Map<String, String> customAttributes, String selector) {
        final String element = extSolo.getOtherUtils().isNullOrEmpty(
                selector) ? getHTMLElement(tag, id, name, className,
                index, xPath, customAttributes) : getHTMLElement(selector, index);
        return Boolean.valueOf(executeCommandWithResult(webview, element + " !== undefined"));
    }

    protected int numberOfHtmlElements(
            String tag, String id, String name, String className, String xPath, Map<String,
            String> customAttributes, String selector) {
        final String element = extSolo.getOtherUtils().isNullOrEmpty(selector) ? getHTMLElement(tag, id, name,
                className, -1, xPath, customAttributes) : getHTMLElement(selector, -1);
        return Integer.valueOf(executeCommandWithResult(getWebView(), element + ".length"));
    }

    protected boolean waitForHtmlElement(String tag, String id, String name,
                                         String className, int index, int time, String xPath,
                                         Map<String, String> customAttributes, String selector) {
        boolean result = false;
        WebView wv = getWebView();

        //waiting needed time for element
        long timeOut = System.currentTimeMillis() + time;
        while (System.currentTimeMillis() <= timeOut && !(result = elementExistsOnHtmlPage(wv, tag, id, name,
                className, index, xPath, customAttributes, selector))) {
            extSolo.soloSleep(INTERVAL_FOR_CHECK);
        }

        return result;
    }

    protected void enterTextIntoHtmlElement(
            String tag, String id, String name, String className, final String text, int index, String xPath,
            Map<String, String> customAttributes, String selector) {
        WebView wv = getWebView();
        assertTrue(Messages.ELEMENT_DOES_NOT_EXIST, elementExistsOnHtmlPage(wv, tag, id, name, className, index,
                xPath, customAttributes, selector));
        final String element = extSolo.getOtherUtils().isNullOrEmpty(selector) ?
                getHTMLElement(tag, id, name, className, index, xPath, customAttributes) :
                getHTMLElement(selector, index);

        executeCommand(
                wv,
                "var el = " + element + ";" +
                        "el.value = '" + text + "';" +
                        "var changeEvent = document.createEvent(\"HTMLEvents\");" +
                        "changeEvent.initEvent(\"change\",true);" +
                        "el.dispatchEvent(changeEvent);" +
                        "var keydownEvent = document.createEvent(\"UIEvents\");" +
                        "keydownEvent.initEvent(\"keydown\",true);" +
                        "el.dispatchEvent(keydownEvent);" +
                        "var keypressEvent = document.createEvent(\"UIEvents\");" +
                        "keypressEvent.initEvent(\"keypress\",true);" +
                        "el.dispatchEvent(keypressEvent);" +
                        "var keyupEvent = document.createEvent(\"UIEvents\");" +
                        "keyupEvent.initEvent(\"keyup\",true);" +
                        "el.dispatchEvent(keyupEvent);");
    }

    protected void clickOnHtmlElement(
            final String tag, String id, String name, String className, int index, String xPath, Map<String,
            String> customAttributes, String selector) {
        WebView wv = getWebView();
        assertTrue(Messages.ELEMENT_DOES_NOT_EXIST, elementExistsOnHtmlPage(wv, tag, id, name, className, index, xPath, customAttributes, selector));
        final String element = extSolo.getOtherUtils().isNullOrEmpty(
                selector) ? getHTMLElement(tag, id, name, className,
                index, xPath, customAttributes) : getHTMLElement(selector, index);

        Log.d(ExtSolo.TAG, "Clicking element " + element);
        executeCommand(
                wv,
                "var elem =" + element + "; " +

                        "var focus = document.createEvent(\"HTMLEvents\");" +
                        "focus.initEvent(\"focus\",true);" +
                        "elem.dispatchEvent(focus);" +

                        "var mousedown = document.createEvent(\"MouseEvents\");" +
                        "mousedown.initMouseEvent(\"mousedown\",true);" +
                        "elem.dispatchEvent(mousedown);" +

                        "var posX = elem.offsetLeft + 10;" +
                        "var posY = elem.offsetTop + 10;" +
                        "var identifier = Date.now();" +
                        "var touch = document.createTouch(window,elem,identifier,posX,posY,posX,posY);" +
                        "var touchList = document.createTouchList(touch);" +

                        "var touchstart = document.createEvent(\"TouchEvent\");" +
                        "touchstart.initTouchEvent(touchList, touchList, touchList, 'touchstart',  window, posX, posY, posX, posY, false, false, false, false);" +
                        "elem.dispatchEvent(touchstart);" +

                        "var mouseup = document.createEvent(\"MouseEvents\");" +
                        "mouseup.initMouseEvent(\"mouseup\",true);" +
                        "elem.dispatchEvent(mouseup);" +

                        "var touchend = document.createEvent(\"TouchEvent\");" +
                        "touchend.initTouchEvent(touchList, touchList, touchList, 'touchend',  window, posX, posY, posX, posY, false, false, false, false);" +
                        "elem.dispatchEvent(touchend);" +

                        "var click = document.createEvent(\"MouseEvents\");" +
                        "click.initMouseEvent(\"click\",true);" +
                        "elem.dispatchEvent(click);");
    }

    private Object getBrowserFrame() {
        try {
            if (webView != null) {
                // getting WebViewCore from right WebViewProvider - WebView or
                // WebViewClassic
                @SuppressWarnings("rawtypes")
                Class clazz = webView.getClass();
                if (!clazz.getCanonicalName().equals(
                        "android.webkit.WebViewClassic")
                        && !clazz.getCanonicalName().equals("android.webkit.WebView")) {
                    try {
                        while (!clazz.equals(Class.forName("android.webkit.WebView"))) {
                            clazz = clazz.getSuperclass();
                            Log.d(ExtSolo.TAG, "WebView class: " + clazz.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        Log.w(ExtSolo.TAG, e);
                    }
                }

                //normally webView is suffice but in SDK 4.1 whole functionality
                //was moved to WebViewProvider interface which is implemented
                //by webViewClassic                
                Object webViewProvider = getWebViewOrWebViewClassic(clazz, webView);
                clazz = webViewProvider.getClass().getCanonicalName().equals("android.webkit.WebViewClassic") ?
                        webViewProvider.getClass() : clazz;

                Method getWebViewCore = clazz.getDeclaredMethod("getWebViewCore");
                getWebViewCore.setAccessible(true);
                Object webViewCore = getWebViewCore.invoke(webViewProvider);

                //getting BrowserFrame
                Method getBrowserFrame = webViewCore.getClass().getDeclaredMethod("getBrowserFrame");
                getBrowserFrame.setAccessible(true);
                Object browserFrame = getBrowserFrame.invoke(webViewCore);

                return browserFrame;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.w(ExtSolo.TAG, e);
        }

        return null;
    }

    private Object getWebViewOrWebViewClassic(@SuppressWarnings("rawtypes") Class webViewClass, Object webView) throws IllegalArgumentException, IllegalAccessException {
        try {
            Field mProvider = webViewClass.getDeclaredField("mProvider");
            mProvider.setAccessible(true);

            return mProvider.get(webView);
        } catch (NoSuchFieldException e) {
            //it means there is no WebViewClassic so we use 
            //SDK below 4.1 and WebView has whole needed functionality
            return webView;
        }
    }

    private String getTextFromBrowserFrame(Object browserFrame) {
        try {
            Method documentAsText = browserFrame.getClass().getDeclaredMethod("externalRepresentation");
            documentAsText.setAccessible(true);

            Object website = documentAsText.invoke(browserFrame);

            return String.valueOf(website);
        } catch (Exception e) {
            Log.w(ExtSolo.TAG, e);
        }

        return "";
    }

    protected boolean isTextPresentOnHtmlPage(String text) {
        boolean textPresented = false;

        //it's done to be sure that page is fully loaded and browserFrame is not null
        getWebView();

        // get only text in ""
        Pattern p = Pattern.compile("\".*\"");
        String result = "";
        Matcher m = p.matcher(getTextFromBrowserFrame(browserFrame));
        while (m.find()) {
            result += m.group(0);
        }

        // change coding
        String s[] = String.valueOf(result).split("\\\\x\\{");
        for (int i = 0; i < s.length; i++) {
            int count = s[i].indexOf("}");
            switch (count) {
                case 1:
                    s[i] = "\\u000" + s[i].replaceFirst("\\}", "");
                    break;
                case 2:
                    s[i] = "\\u00" + s[i].replaceFirst("\\}", "");
                    break;
                case 3:
                    s[i] = "\\u0" + s[i].replaceFirst("\\}", "");
                    break;
                case 4:
                    s[i] = "\\u" + s[i].replaceFirst("\\}", "");
                    break;
            }
        }
        result = "";
        for (String value : s) {
            result += value;
        }

        // convert unicode to utf-8
        Pattern p2 = Pattern.compile("\\\\u([0-9A-F]{4})");
        Matcher m2 = p2.matcher(result);
        while (m2.find()) {
            result = result.replaceAll("\\" + m2.group(0), Character.toString((char) Integer.parseInt(m2.group(1), 16)));
        }

        // find text
        Pattern p3 = Pattern.compile(text);
        Matcher m3 = p3.matcher(result);
        while (m3.find()) {
            textPresented = true;
        }
        return textPresented;
    }

    protected void htmlZoomIn() {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                getWebView().zoomIn();
            }
        });
    }

    protected void htmlZoomOut() {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                getWebView().zoomOut();
            }
        });
    }

    protected void htmlGoForward() {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                getWebView().goForward();
            }
        });
    }

    protected void htmlGoBack() {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                getWebView().goBack();
            }
        });
    }

    protected synchronized void setJSResult(Object result) {
        this.jsResult = result.toString();
    }

    protected synchronized String getJSResult() {
        return this.jsResult;
    }

    protected String getHtmlCode() {
        return String.valueOf(executeCommandWithResult(getWebView(), "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'"));
    }

    protected void injectJavaScriptFile(String fileName) throws IOException {
        injectJavaScriptCode(getJavaScriptFromFile(fileName));
    }

    protected void injectJavaScriptCode(final String code) {
        getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                getWebView().loadUrl("javascript:" + code);
            }
        });
    }

    private WebView getWebView() {
        try {
            boolean found = false;
            WebView currentWebView = null;
            long timeOut = System.currentTimeMillis() + TIMEOUT_FOR_WEBVIEW;

            //searching first WebView on the screen
            while (System.currentTimeMillis() <= timeOut && !found) {
                ArrayList<View> views = extSolo.getViews();
                for (View v : views) {
                    if (v instanceof WebView) {
                        found = true;
                        currentWebView = (WebView) v;
                    }
                }
                extSolo.soloSleep(INTERVAL_FOR_CHECK);
            }

            //checking if it's new web view or already cached
            //if new then JavaScriptInterface need to be loaded
            //and webView need to be reloaded to load JSI
            //and then WebView is cached
            if (webView == null || webView != currentWebView) {
                webView = currentWebView;
                //we need to be sure that any WebView exists
                assertTrue("There is no WebView", webView != null);
                browserFrame = getBrowserFrame();

                getCurrentActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebChromeClient(new ProxyWebChromeClient(webView, HtmlUtils.this));
                    }
                });
            }
        } catch (AssertionFailedError e) {
            String packageName = extSolo.getInstrumentation().getContext().getPackageName();
            extSolo.fail(String.format("%s.%s.%s_src_fail", packageName, className, methodName), e);
        }

        return webView;
    }

    private void waitForWebViewFullyLoaded() {
        boolean result = false;
        //we need to check till webView is fully loaded(its content)
        //before do any loadUrl on it to avoid breaking our JS methods
        //during loading page        
        long timeOut = System.currentTimeMillis() + TIMEOUT_WEB_VIEW_FULLY_LOADED;
        while (System.currentTimeMillis() <= timeOut && !(result = isWebViewFullyLoaded())) {
            extSolo.soloSleep(INTERVAL_FOR_CHECK);
        }

        if (!result) {
            Log.w(ExtSolo.TAG, Messages.WEBVIEW_NOT_FULLY_LOADED);
        }
    }

    private boolean isWebViewFullyLoaded() {
        if (browserFrame != null) {
            try {
                Field mCommitted = browserFrame.getClass().getDeclaredField("mCommitted");
                mCommitted.setAccessible(true);

                Field mFirstLayoutDone = browserFrame.getClass().getDeclaredField("mFirstLayoutDone");
                mFirstLayoutDone.setAccessible(true);

                return mCommitted.getBoolean(browserFrame) && mFirstLayoutDone.getBoolean(browserFrame);
            } catch (NoSuchFieldException e) {
                Log.w(ExtSolo.TAG, e);
                return false;
            } catch (IllegalAccessException e) {
                Log.w(ExtSolo.TAG, e);
                return false;
            }
        } else {
            return false;
        }
    }

    private String executeCommandWithResult(final WebView webView, final String command) {
        executeCommand(webView, String.format("console.log('%s' + (%s));", JS_RESULT_PREFIX, command));

        setJSResult(false);
        synchronized (this) {
            try {
                this.wait(TIMEOUT_FOR_JS_RESPONSE);
            } catch (InterruptedException e) {
                Log.w(ExtSolo.TAG, e);
            }
        }

        return getJSResult();
    }

    private void executeCommand(final WebView webView, final String command) {
        //check if webView exist to run script on it
        assertTrue(Messages.NO_WEBVIEW, webView != null);
        waitForWebViewFullyLoaded();

        getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                webView.loadUrl("javascript:(function() { " + command + " })()");
            }
        });
    }

    private String getSelector(
            String tag, String id, String name, String className, Map<String, String> customAttributes) {
        StringBuilder sB = new StringBuilder();
        // START QUERY + TAG
        sB.append("document.querySelectorAll('").append(tag);
        //NAME
        if (!extSolo.getOtherUtils().isNullOrEmpty(name)) {
            sB.append("[name=").append(name).append("]");
        }
        //ATTRIBUTES
        if (customAttributes != null) {
            for (int i = 0; i < customAttributes.size(); i++) {
                String key = customAttributes.keySet().toArray()[i].toString();
                sB.append("[").append(key).append("=").append(customAttributes.get(key)).append("]");
            }
        }
        //ID
        if (!extSolo.getOtherUtils().isNullOrEmpty(id)) {
            sB.append("#").append(id);
        }
        // CLASS
        if (!extSolo.getOtherUtils().isNullOrEmpty(className)) {
            String[] classes = className.split(" ");
            for (String aClass : classes) {
                if (aClass.length() > 0) {
                    sB.append(".").append(aClass);
                }
            }
        }
        //END QUERY
        sB.append("')");
        return sB.toString();
    }

    private String getHTMLElement(
            String tag, String id, String name, String className, int index, String xPath, Map<String,
            String> customAttributes) {
        StringBuilder sB = new StringBuilder();
        if (extSolo.getOtherUtils().isNullOrEmpty(xPath)) {
            sB.append(getSelector(tag, id, name, className, customAttributes));
            //INDEX
            if (index >= 0) {
                sB.append("[" + index + "]");
            }
        } else {
            sB.append("document.evaluate(\"" + xPath + "\", document, null, 0, null).iterateNext()");
        }
        return sB.toString();
    }

    private String getHTMLElement(String selector, int index) {
        return String.format("document.querySelectorAll('%s')%s", selector, index < 0 ? "" : "[" + index + "]");
    }

    private String getJavaScriptFromFile(String jsFileName) throws IOException {
        InputStream is = extSolo.getInstrumentation().getContext().getAssets().open(jsFileName);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(buffer);
        os.close();
        is.close();

        return os.toString();
    }
}