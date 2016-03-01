package com.bitbar.recorder.extensions;

import android.os.SystemClock;
import android.view.View;
import com.bitbar.recorder.extensions.OtherUtils.Type;

class Waiter {
    private ExtSolo extSolo;

    Waiter(ExtSolo extSolo) {
        this.extSolo = extSolo;
    }

    public <T extends View> boolean wait(Class<T> clazz, Integer index, String text, Integer timeout) {
        boolean result;
        StringBuilder sb = new StringBuilder();
        sb.append("Wait for ");

        String[] parts = clazz.getName().split("\\.");

        sb.append(parts[parts.length - 1]);

        if (text != null) {
            sb.append(" with text: ").append(text);
        } else if (index != null) {
            sb.append(" with index: ").append(index);
        }
        extSolo.getOtherUtils().addAction(sb.toString(), Type.wait);
        if (text != null) {
            result = extSolo.soloWaitForText(text, timeout);
        } else {
            result = waitForView(clazz, index, timeout);
        }
        extSolo.getOtherUtils().addDurationToAction();
        return result;
    }

    public <T extends View> boolean wait(Class<T> clazz, Integer index, Integer timeout) {
        return wait(clazz, index, null, timeout);
    }

    public <T extends View> boolean wait(Class<T> clazz, String text, Integer timeout) {
        return wait(clazz, null, text, timeout);
    }

    public <T extends View> boolean waitById(Class<T> clazz, String id, int timeout) {

        StringBuilder sb = new StringBuilder();
        sb.append("Wait for ");

        String[] parts = clazz.getName().split("\\.");

        sb.append(parts[parts.length - 1]);
        sb.append(" with id: ").append(id);

        extSolo.getOtherUtils().addAction(sb.toString(), Type.wait);
        boolean result = false;
        long endTime = SystemClock.uptimeMillis() + timeout;
        do {
            View view = extSolo.getOtherUtils().findViewById(id);
            if (view != null) {
                result = extSolo.waitForView(view);
            }
        } while (SystemClock.uptimeMillis() <= endTime && !result);
        extSolo.getOtherUtils().addDurationToAction();
        return result;
    }

    private <T extends View> boolean waitForView(Class<T> clazz, Integer index, Integer timeout) {
        return extSolo.waitForView(clazz, index + 1, timeout);
    }
}