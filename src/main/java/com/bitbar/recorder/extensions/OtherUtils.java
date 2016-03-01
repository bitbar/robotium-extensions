package com.bitbar.recorder.extensions;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.bitbar.recorder.extensions.ExtSolo.WIFI_STATE;
import com.bitbar.testdroid.aidl.IMetadataService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class OtherUtils {

    private ExtSolo extSolo;

    private boolean mBound;
    private boolean mMetadataConnected;
    IMetadataService mMetadataService;

    private String methodName;
    private String className;
    private LocationManager locationManager = null;
    private Timer mockGPSTimer = null;

    private final Locale locale;

    private Integer screenWidth = null;
    private Integer screenHeight = null;
    private Display defaultDisplay = null;
    private static final int INTERVAL_FOR_CHECK = 500;

    protected enum Type {
        click, config, input, drag, assertion, wait, scroll, navigation, util
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            mMetadataService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mMetadataService = IMetadataService.Stub.asInterface(service);
            mBound = true;
        }
    };

    OtherUtils(ExtSolo extSolo, String className, String methodName) {
        mMetadataConnected = extSolo.getInstrumentation()
                .getTargetContext()
                .bindService(
                        new Intent(
                                "com.bitbar.testdroid.monitor.MetadataService"),
                        mConnection, Context.BIND_AUTO_CREATE);

        //set needed variables
        this.extSolo = extSolo;
        this.className = className;
        this.methodName = methodName;

        //get current language
        locale = extSolo.getInstrumentation().getTargetContext().getResources().getConfiguration().locale;
        Log.d(ExtSolo.TAG, String.format(Messages.CURRENT_LOCALE, locale.toString()));

        //initializaion of mocking location - move to constructor
        try {
            locationManager = (LocationManager) extSolo.getInstrumentation().getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, true, false, false, 1, 1);
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            mockGPSTimer = new Timer();
        } catch (SecurityException e) {
            locationManager = null;
        }

        setUp();
    }

    protected boolean isNullOrEmpty(String text) {
        return text == null || text.equals("");
    }

    protected void scrollListToLine(final ListView listView, final int line) {
        extSolo.getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                listView.setSelection(line);
            }
        });
    }

    protected Display getDefaultDisplay() {
        if (defaultDisplay == null) {
            defaultDisplay = extSolo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        }

        return defaultDisplay;
    }

    protected int getScreenWidth() {

        if (screenWidth == null) {
            screenWidth = (getDefaultDisplay().getOrientation() == ExtSolo.ORIENTATION_LANDSCAPE) ?
                    getDefaultDisplay().getHeight() : getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

    protected int getScreenHeight() {
        if (screenHeight == null) {
            screenHeight = (getDefaultDisplay().getOrientation() == ExtSolo.ORIENTATION_LANDSCAPE) ?
                    getDefaultDisplay().getWidth() : getDefaultDisplay().getHeight();
        }
        return screenHeight;
    }

    protected void waitForAnyView() {
        while (true) {
            if (extSolo.getViews().size() > 0) {
                return;
            }
        }
    }

    protected void waitForAnyListView() {
        while (true) {
            if (extSolo.getCurrentListViews().size() > 0) {
                return;
            }
        }
    }

    protected void hideKeyboard(final EditText editText) {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) extSolo
                        .getInstrumentation().getTargetContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }

    protected void hideKeyboard() {
        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                extSolo.getCurrentActivity()
                        .getWindow()
                        .setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

    protected View findViewById(String id) {
        return findViewById(extSolo.getCurrentActivity().getResources()
                .getIdentifier(id.replaceAll("\\.R\\.id\\.", ":id/"), null, null));
    }

    protected View findViewById(int id) {
        View view = extSolo.getView(id);
        if (view != null)
            return view;

        ArrayList<View> views = extSolo.getViews();
        for (View v : views) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    protected void drag(float fromX, float toX, float fromY, float toY, int stepCount, int elapsed) {
        long interval = elapsed / stepCount;

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        float y = fromY;
        float x = fromX;
        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, fromX, fromY, 0);
        try {
            extSolo.getInstrumentation().sendPointerSync(event);
        } catch (SecurityException ignored) {
        }
        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
            try {
                extSolo.getInstrumentation().sendPointerSync(event);
            } catch (SecurityException ignored) {
            }
            long startTime = SystemClock.uptimeMillis();
            long endTime = startTime + interval;
            while (SystemClock.uptimeMillis() <= endTime) ;
        }
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, toX, toY, 0);
        try {
            extSolo.getInstrumentation().sendPointerSync(event);
        } catch (SecurityException ignored) {
        }
    }

    protected void multiDrag(float[][] points) {
        //landscape mode - translate position according to this. We recording as if rotation doesn't
        //influence on x,y clicks
        if (getDefaultDisplay().getOrientation() == ExtSolo.ORIENTATION_LANDSCAPE) {
            for (int i = 0; i < points.length; i++) {
                float temp = points[i][0];
                points[i][0] = points[i][1];
                points[i][1] = getScreenWidth() - temp;
            }
        }

        if (points.length > 2) {
            long downTime = SystemClock.uptimeMillis() + 1000;
            long eventTime = SystemClock.uptimeMillis();

            MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                    MotionEvent.ACTION_DOWN, extSolo.toScreenX(points[0][0]), extSolo.toScreenY(points[0][1]), 0);
            try {
                extSolo.getInstrumentation().sendPointerSync(event);
            } catch (SecurityException ignored) {
            }
            for (int i = 1; i < points.length; i++) {
                eventTime = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(downTime, eventTime,
                        MotionEvent.ACTION_MOVE, extSolo.toScreenX(points[i][0]), extSolo.toScreenY(points[i][1]), 0);
                try {
                    extSolo.getInstrumentation().sendPointerSync(event);
                } catch (SecurityException ignored) {
                }
            }
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime,
                    MotionEvent.ACTION_UP,
                    extSolo.toScreenX(points[points.length - 1][0]),
                    extSolo.toScreenY(points[points.length - 1][1]), 0);
            try {
                extSolo.getInstrumentation().sendPointerSync(event);
            } catch (SecurityException ignored) {
            }
        }
    }

    protected void changeDeviceLanguage(Locale locale)
            throws ClassNotFoundException, SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            NoSuchFieldException {
        @SuppressWarnings("rawtypes")
        Class amnClass = Class.forName("android.app.ActivityManagerNative");
        Object amn = null;
        Configuration config = null;

        // amn = ActivityManagerNative.getDefault();
        Method methodGetDefault = amnClass.getMethod("getDefault");
        methodGetDefault.setAccessible(true);
        amn = methodGetDefault.invoke(amnClass);

        // config = amn.getConfiguration();
        Method methodGetConfiguration = amnClass.getMethod("getConfiguration");
        methodGetConfiguration.setAccessible(true);
        config = (Configuration) methodGetConfiguration.invoke(amn);

        // config.userSetLocale = true;
        @SuppressWarnings("rawtypes")
        Class configClass = config.getClass();
        Field f = configClass.getField("userSetLocale");
        f.setBoolean(config, true);

        // set the locale to the new value
        config.locale = locale;

        // amn.updateConfiguration(config);
        Method methodUpdateConfiguration = amnClass.getMethod(
                "updateConfiguration", Configuration.class);
        methodUpdateConfiguration.setAccessible(true);
        methodUpdateConfiguration.invoke(amn, config);
    }

    protected void restoreLocaleIfWasChanged() {
        if (!locale.equals(extSolo.getInstrumentation().getTargetContext()
                .getResources().getConfiguration().locale)) {
            try {
                changeDeviceLanguage(locale);
            } catch (Exception e) {
                // ignored
            }
        }
    }

    private List<String> getAllActivitiesFromApplication() throws NameNotFoundException {
        List<String> result = new ArrayList<String>();
        ActivityInfo[] list = extSolo.
                getInstrumentation()
                .getContext()
                .getPackageManager()
                .getPackageInfo(extSolo.getInstrumentation().getTargetContext().getPackageName(),
                        PackageManager.GET_ACTIVITIES).activities;
        for (ActivityInfo aList : list) {
            result.add(aList.name);
        }
        return result;
    }

    private void setUp() {
        try {
            if (mMetadataConnected) {
                int tries = 10;
                // give up to 10 seconds to bound service
                while (!mBound && tries-- > 0) {
                    extSolo.soloSleep(1000);
                }
                if (isBound()) {
                    mMetadataService.setAllActivitiesFromApplication(getAllActivitiesFromApplication());
                }
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_SET_ALL_ACTIVITIES);
        } catch (NameNotFoundException e) {
            Log.w(ExtSolo.TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    protected void fail(String name, Object e) {
        extSolo.takeScreenshot(name, true);
        try {
            if (isBound()) {
                mMetadataService.setErrorMessage(e.toString());
                mMetadataService.addDurationToAction();
            }
        } catch (RemoteException e1) {
            Log.d(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_MSG_OR_DURATION);
        }
    }

    protected void setGPSMockLocation(final double latitude, final double longitude, final double altitude) {
        if (locationManager != null) {
            //set timer which will regularly set desired location
            mockGPSTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    location.setAltitude(altitude);
                    location.setAccuracy(16F);
                    location.setTime(System.currentTimeMillis());
                    location.setBearing(0F);

                    locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
                }
            }, 0, 5000);
            //we need to send intent in case test is launched in cloud and monitor set own location
            //to avoid interfere those two gps timers
            Intent gpsMockMonitorIntent = new Intent("com.bitbar.testdroid.monitor.START_MOCK_GPS");
            gpsMockMonitorIntent.putExtra("latitude", Double.toString(latitude));
            gpsMockMonitorIntent.putExtra("longitude", Double.toString(longitude));
            gpsMockMonitorIntent.putExtra("elevation", Double.toString(altitude));

            extSolo.getInstrumentation().getContext().sendBroadcast(gpsMockMonitorIntent);
        } else {
            Log.d(ExtSolo.TAG, Messages.MOCK_LOCATION_NO_PERMISSION);
        }
    }

    protected void resetGPSMocking() {
        if (locationManager != null) {
            if (mockGPSTimer != null) {
                mockGPSTimer.cancel();
            }
            locationManager.clearTestProviderLocation(LocationManager.GPS_PROVIDER);
            locationManager.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);

            //we need to also reset our mock location in monitor to revive default settings
            Intent gpsMockMonitorIntent = new Intent("com.bitbar.testdroid.monitor.STOP_MOCK_GPS");
            extSolo.getInstrumentation().getContext().sendBroadcast(gpsMockMonitorIntent);
        }
    }

    protected String getDescriptionFromView(View view) {
        if (view == null) {
            return "null";
        }
        String parts[] = view.getClass().getName().split("\\.");
        String className = parts[parts.length - 1];

        String result = null;

        if (view instanceof CheckBox || view instanceof RadioButton || view instanceof ToggleButton) {
            result = String.format("\"%s\"", ((CompoundButton) view).getText().toString());
            if (isNullOrEmpty(result)) {
                result = getDescriptionOrId(view);
            }
            result += ((CompoundButton) view).isChecked() ? " - unchecked" : " - checked";
        } else if (view instanceof Button) {
            result = String.format("\"%s\"", ((Button) view).getText().toString());
        } else if (view instanceof TextView) {
            result = ((TextView) view).getText().toString();
        }

        if (isNullOrEmpty(result)) {
            result = getDescriptionOrId(view);
        }
        if (result == null) {
            return String.format("%s", className);
        } else {
            return String.format("%s %s", className, result);
        }
    }

    private String getDescriptionOrId(View view) {
        String result = view.getContentDescription() != null ? view
                .getContentDescription().toString() : null;
        if (isNullOrEmpty(result)) {
            if (view.getId() != View.NO_ID) {
                try {
                    result = String.format("with id: \"%s\"", view.getContext()
                            .getResources().getResourceEntryName(view.getId()));
                } catch (NotFoundException e) {
                    result = String.format(Locale.ENGLISH, "with id: \"%d\"",
                            view.getId());
                }
            }
        } else {
            result = String.format("\"%s\"", result);
        }
        return result;
    }

    protected void turnWifi(boolean enabled) {
        try {
            WifiManager wifiManager = (WifiManager) extSolo.getInstrumentation().getTargetContext()
                    .getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(enabled);

            // let know monitoring about programmatic turning on/off wifi
            Intent intent = new Intent();
            intent.setAction("com.bitbar.testdroid.monitor.TURN_WIFI");
            intent.putExtra("enabled", enabled);
            extSolo.getInstrumentation().getTargetContext().sendBroadcast(intent);
        } catch (Exception ignored) {
            // don't interrupt test execution, if there
            // is no permission for that action
        }
    }

    protected boolean waitForWifi(WIFI_STATE state, int time) {
        if (state != null) {
            WifiManager wifiManager = (WifiManager) extSolo.getInstrumentation().getTargetContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WIFI_STATE current;
            long timeOut = System.currentTimeMillis() + time;
            while (System.currentTimeMillis() <= timeOut) {
                current = wifiManager.isWifiEnabled() ? WIFI_STATE.CONNECTED : WIFI_STATE.DISCONNECTED;
                if (state.equals(current)) {
                    return true;
                }
                extSolo.soloSleep(INTERVAL_FOR_CHECK);
            }
        }
        return false;
    }

    public void addAction(String name, Type type) {
        Log.d(ExtSolo.TAG, name);
        String currentActivity = extSolo.getCurrentActivity().getClass().getCanonicalName();
        try {
            if (isBound()) {
                mMetadataService.addAction(name, type.toString(), currentActivity, className, methodName);
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_ADD_ACTION);
        }
    }

    public void addScreenshotToMetadata(String name, boolean failed) {
        Activity currentActivity = extSolo.getCurrentActivity();
        int orientation = -1;
        if (currentActivity != null) {
            orientation = ((WindowManager) currentActivity.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getOrientation();
        }
        try {
            if (isBound()) {
                mMetadataService.addScreenshotToMetadata(name, failed, orientation);
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_ADD_SCREENSHOT);
        }
    }

    public void addDurationToAction() {
        try {
            if (isBound()) {
                mMetadataService.addDurationToAction();
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_ADD_DURATION);
        }
    }

    public void saveMetadataFile() {
        try {
            if (isBound()) {
                mMetadataService.saveMetadataFile();
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_SAVE);
        } catch (NullPointerException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_SAVE);
        }
    }

    public void changeActionDescription(String name) {
        try {
            if (isBound()) {
                mMetadataService.changeActionDescription(name);
            }
        } catch (RemoteException e) {
            Log.w(ExtSolo.TAG, Messages.METADATA_SERVICE_ERROR_CHANGE);
        }
    }

    public void tearDown() {
        // unbind service
        if (isBound()) {
            extSolo.getInstrumentation().getTargetContext().unbindService(mConnection);
        }
    }

    protected void moveGalleryLeft(final Gallery gallery, final int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            extSolo.getInstrumentation().runOnMainSync(new Runnable() {
                public void run() {
                    gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, new KeyEvent(
                            0, 0));
                }

            });
            extSolo.soloSleep(150);
        }
    }

    protected void moveGalleryRight(final Gallery gallery, final int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            extSolo.getInstrumentation().runOnMainSync(new Runnable() {
                public void run() {
                    gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, new KeyEvent(0, 0));
                }

            });
            extSolo.soloSleep(150);
        }
    }

    private boolean isBound() {
        return (mMetadataService != null) && mBound;
    }

    protected void sendStringSync(String text) {
        extSolo.getInstrumentation().sendStringSync(text);
    }
}