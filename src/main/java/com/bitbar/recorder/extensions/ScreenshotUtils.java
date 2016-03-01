package com.bitbar.recorder.extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bitbar.testdroid.aidl.IScreenshotService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

class ScreenshotUtils {
    // timeout for waiting for screenshot taking and sent
    private static final int SCREENSHOT_TIMEOUT = 20000;

    private ExtSolo extSolo;
    private boolean mBound;
    IScreenshotService mScreenshotService;
    
    private ServiceConnection mConnection = new ServiceConnection() {        
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            mScreenshotService = null;            
        }
        
        public void onServiceConnected(ComponentName name, IBinder service) {
            mScreenshotService = IScreenshotService.Stub.asInterface(service);
            mBound = true;
        }
    };
    
    ScreenshotUtils(ExtSolo extSolo) {
        this.extSolo = extSolo;
        extSolo.getInstrumentation().getTargetContext().bindService(
                new Intent("com.bitbar.testdroid.monitor.ScreenshotService"), mConnection, Context.BIND_AUTO_CREATE);
    }
    
    public void tearDown() {
        //unbind service
        if (isBound()) {
            extSolo.getInstrumentation().getTargetContext().unbindService(mConnection);
        }
    }
    
    protected void takeScreenshot(final String name) {
        new Thread(new Runnable() {
            public void run() {
                boolean aslFailed = false;
                if (isBound()) {
                    Log.d(ExtSolo.TAG, Messages.CONNECTED_TO_SCREENSHOT_SERVICE);
                    try {
                        if (!mScreenshotService.takeScreenshot(name)) {
                            Log.d(ExtSolo.TAG, Messages.SCREENSHOT_SERVICE_FAILED);
                            aslFailed = true;
                        } else {
                            unlockThread();
                        }                        
                    } catch (RemoteException e) {
                        Log.d(ExtSolo.TAG, Messages.COULDNT_CALL_SCREENSHOT_SERVICE_METHOD);
                        aslFailed = true;
                    }
                } else {
                    aslFailed = true;
                }
                if (aslFailed) {
                    if (extSolo.waitForView(View.class, 1, SCREENSHOT_TIMEOUT)) {
                        final View view = extSolo.getViews().get(0).getRootView();
                        extSolo.getCurrentActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                takeScreenshotWithoutAsl(name, view);
                            }
                        });
                    } else {
                        Log.d(ExtSolo.TAG, Messages.SCRRENSHOT_NO_VIEWS);
                    }
                }
            }
        }).start();

        lockThread();        
    }
    
    private void unlockThread() {
        synchronized (ScreenshotUtils.this) {
            ScreenshotUtils.this.notify();
            Log.d(ExtSolo.TAG, Messages.SCREENSHOT_JOB_NOTIFIED);
        }
    }
    
    private void lockThread() {
        // wait until screenshot is fully taken and sent
        synchronized (this) {
            try {
                this.wait(SCREENSHOT_TIMEOUT);
            } catch (InterruptedException e) {
                Log.d(ExtSolo.TAG, Messages.SCREENSHOT_WAIT_INTERRUPTED);
            }
        }
        Log.d(ExtSolo.TAG, Messages.SCREENSHOT_JOB_WOKE_UP);
    }

    protected void takeScreenshotWithoutAsl(String name, View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // get drawing cache from given view
        Bitmap bmp = view.getDrawingCache();
        
        if (bmp != null) {
            // get path for sdcard
            String path = String.format("%s/%s/",
                    Environment.getExternalStorageDirectory(),
                    "test-screenshots");

            // create dir for screenshot if it doesn't exist
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            //save bitmap from drawing cache on sdcard
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(String.format(
                        "%s%s.png", path, name));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                Log.d(ExtSolo.TAG, Messages.SCREENSHOT_SAVED_WITH_OLD_METHOD);
            } catch (IOException e) {
                Log.d(ExtSolo.TAG,
                        String.format(Messages.SCREENSHOT_NO_PERMISSION, name));
            } finally {
                view.destroyDrawingCache();

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        Log.d(ExtSolo.TAG, Messages.UNABLE_TO_CLOSE_OPENED_STREAM);
                    }
                }
                
                unlockThread();
            }
        } else {
            Log.d(ExtSolo.TAG, Messages.SCREENSHOT_DRAWING_CACHE_NULL);
        }
    }
    
    private boolean isBound() {
        return (mScreenshotService != null) && mBound;
    }
}