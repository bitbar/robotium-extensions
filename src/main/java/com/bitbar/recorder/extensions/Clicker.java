package com.bitbar.recorder.extensions;

import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.widget.EditText;
import com.bitbar.recorder.extensions.OtherUtils.Type;

class Clicker {

    private ExtSolo extSolo;

    Clicker(ExtSolo extSolo) {
        this.extSolo = extSolo;
    }

    <T extends View> void click(
            Class<T> clazz, View view, Integer index, String text, boolean longClick, Integer time) {
        final View toClick = (view != null) ? view : (index != null) ? extSolo.getView(clazz, index) : null;

        StringBuilder sb = new StringBuilder();
        sb.append("Click ").append(longClick ? "long on " : "on ");

        String[] parts = clazz.getName().split("\\.");

        sb.append(parts[parts.length - 1]);

        if (text != null) {
            sb.append(" with text: ").append(text);
        } else if (index != null) {
            sb.append(" with index: ").append(index);
        } else if (view != null) {
            if (view.getId() != View.NO_ID) {
                try {
                    sb.append(" with id: ").append(view.getContext().getResources().getResourceEntryName(view.getId()));
                } catch (NotFoundException e) {
                    sb.append(" with id: ").append(view.getId());
                }
            } else if (view.getContentDescription() != null) {
                sb.append(" with description: ").append(view.getContentDescription());
            }
        }
        extSolo.getOtherUtils().addAction(sb.toString(), Type.click);

        if (toClick != null && toClick instanceof EditText && view != null && view.hasFocusable() && !view.hasFocus()) {
            extSolo.getInstrumentation().runOnMainSync(new Runnable() {
                public void run() {
                    toClick.requestFocus();
                }
            });
        }
        if (text == null) {
            extSolo.soloClick(toClick, longClick, time);
        } else {
            extSolo.clickOnTextBySolo(clazz, text);
        }
        extSolo.getOtherUtils().addDurationToAction();
    }

    <T extends View> void click(Class<T> clazz, View view, boolean longClick) {
        click(clazz, view, null, null, longClick, null);
    }

    <T extends View> void click(Class<T> clazz, Integer index, boolean longClick) {
        click(clazz, null, index, null, longClick, null);
    }

    <T extends View> void click(Class<T> clazz, String text, boolean longClick) {
        click(clazz, null, null, text, longClick, null);
    }

    <T extends View> void click(Class<T> clazz, View view, boolean longClick, int time) {
        click(clazz, view, null, null, longClick, time);
    }

    <T extends View> void click(Class<T> clazz, Integer index, boolean longClick, int time) {
        click(clazz, null, index, null, longClick, time);
    }

    <T extends View> void click(Class<T> clazz, String text, boolean longClick, int time) {
        click(clazz, null, null, text, longClick, time);
    }
}