package com.bitbar.recorder.extensions;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.bitbar.recorder.extensions.OtherUtils.Type;
import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Extension of robotium-solo library. This class extends robotium solo library
 * (Solo class) and makes testing easier. It requires robotium solo included
 * too.
 * <p/>
 * <h6>Example:</h6>
 * <p/>
 * <pre class="brush: java">
 * {@code
 * import android.test.ActivityInstrumentationTestCase2;
 * import com.bitbar.testdroid.testapp.TestdroidTestAppActivity;
 * import com.bitbar.recorder.extensions.ExtSolo;
 * <p/>
 * public class TestdroidTestAppActivityTest4 extends
 * ActivityInstrumentationTestCase2<TestdroidTestAppActivity> {
 * <p/>
 * private ExtSolo solo;
 * <p/>
 * public TestdroidTestAppActivityTest() {
 * super("com.bitbar.testdroid.testapp", TestdroidTestAppActivity.class);
 * }
 * <p/>
 * public void setUp() throws Exception {
 * super.setUp();
 * solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass().getCanonicalName(), getName());
 * }
 * <p/>
 * public void tearDown() throws Exception {
 * solo.finishOpenedActivities();
 * solo.tearDown();
 * super.tearDown();
 * }
 * <p/>
 * public void testMethod() throws Exception {
 * // Your staff
 * }
 * }
 * }
 * </pre>
 */

public class ExtSolo extends Solo {

    public enum WIFI_STATE {
        CONNECTED,
        DISCONNECTED
    }

    private Instrumentation inst;
    private HtmlUtils htmlUtils;
    private ScreenshotUtils screenshotUtils;
    private Clicker clicker;
    private Waiter waiter;

    private OtherUtils otherUtils;
    protected static final String TAG = "ExtSolo";
    protected static final int ORIENTATION_LANDSCAPE = 1;

    public final static int A = KeyEvent.KEYCODE_A;
    public final static int B = KeyEvent.KEYCODE_B;
    public final static int C = KeyEvent.KEYCODE_C;
    public final static int D = KeyEvent.KEYCODE_D;
    public final static int E = KeyEvent.KEYCODE_E;
    public final static int F = KeyEvent.KEYCODE_F;
    public final static int G = KeyEvent.KEYCODE_G;
    public final static int H = KeyEvent.KEYCODE_H;
    public final static int I = KeyEvent.KEYCODE_I;
    public final static int J = KeyEvent.KEYCODE_J;
    public final static int K = KeyEvent.KEYCODE_K;
    public final static int L = KeyEvent.KEYCODE_L;
    public final static int M = KeyEvent.KEYCODE_M;
    public final static int N = KeyEvent.KEYCODE_N;
    public final static int O = KeyEvent.KEYCODE_O;
    public final static int P = KeyEvent.KEYCODE_P;
    public final static int Q = KeyEvent.KEYCODE_Q;
    public final static int R = KeyEvent.KEYCODE_R;
    public final static int S = KeyEvent.KEYCODE_S;
    public final static int T = KeyEvent.KEYCODE_T;
    public final static int U = KeyEvent.KEYCODE_U;
    public final static int V = KeyEvent.KEYCODE_V;
    public final static int W = KeyEvent.KEYCODE_W;
    public final static int X = KeyEvent.KEYCODE_X;
    public final static int Y = KeyEvent.KEYCODE_Y;
    public final static int Z = KeyEvent.KEYCODE_Z;

    /**
     * Constructor that takes in the instrumentation, activity, class name,
     * method name ExtSolo instance should be initialized in setUp method
     *
     * @param inst       Instrumentation instance
     * @param activity   Activity instance
     * @param className  name of the class (test class)
     * @param methodName name of the method <h6>Example:</h6>
     *                   <p/>
     *                   <pre class="brush: java">
     *                   {@code
     *                   public void setUp() throws Exception {
     *                   super.setUp();
     *                   solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass().getCanonicalName(), getName());
     *                   }
     *                   }
     *                   </pre>
     * @since 2.7
     */
    public ExtSolo(Instrumentation inst, Activity activity, String className, String methodName) {
        super(inst, activity);
        this.inst = inst;
        htmlUtils = new HtmlUtils(this, className, methodName);
        screenshotUtils = new ScreenshotUtils(this);
        otherUtils = new OtherUtils(this, className, methodName);
        clicker = new Clicker(this);
        waiter = new Waiter(this);
    }

    /**
     * @deprecated As of release 2.7, replaced by
     * {@link #ExtSolo(Instrumentation inst, Activity activity, String className, String methodName)}
     */
    public ExtSolo(Instrumentation inst, Solo solo) {
        super(inst, solo.getCurrentActivity());
        this.inst = inst;
        htmlUtils = new HtmlUtils(this, "unknown", "unknown");
        screenshotUtils = new ScreenshotUtils(this);
        otherUtils = new OtherUtils(this, "unknown", "unknown");
        clicker = new Clicker(this);
        waiter = new Waiter(this);
    }

    /**
     * Method used to assert current Activity
     *
     * @param message message, which is thrown when Activity was not found
     * @param name    name of Activity
     *                <p/>
     *                <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.assertCurrentActivity("Activity not found", "MainActivity");
     *                }
     *                </pre>
     */
    @Override
    public void assertCurrentActivity(String message, String name) {
        otherUtils.addAction(String.format(Messages.ASSERT_CURRENT_ACTIVITY, name), Type.assertion);
        super.assertCurrentActivity(message, name);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to assert memory
     * <p/>
     * <h6>Example:</h6> *
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.assertMemoryNotLow();
     * }
     * </pre>
     */
    @Override
    public void assertMemoryNotLow() {
        otherUtils.addAction(Messages.ASSERT_MEMORY_NOT_LOW, Type.assertion);
        super.assertMemoryNotLow();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to change locale of device
     *
     * @param locale locale e.g. 'new Locale("en")' <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.changeDeviceLanguage(new Locale("en"));
     *               <p/>
     *               solo.changeDeviceLanguage(new Locale("en", "US"));
     *               }
     *               </pre>
     */
    public void changeDeviceLanguage(Locale locale) throws Exception {
        if (otherUtils.isNullOrEmpty(locale.getDisplayCountry())) {
            otherUtils.addAction(
                    String.format(Messages.CHANGE_LANGUAGE_TO_ONE_PARAM, locale.getDisplayLanguage()), Type.config);
        } else {
            otherUtils.addAction(String.format(Messages.CHANGE_LANGUAGE_TO_TWO_PARAMS, locale.getDisplayLanguage(),
                            locale.getDisplayCountry()), Type.config);
        }
        otherUtils.changeDeviceLanguage(locale);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to clear edit text
     *
     * @param editText instance of EditText
     *                 <p/>
     *                 <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clearEditText((EditText)solo.findViewById("editText"));
     *                 }
     *                 </pre>
     */
    @Override
    public void clearEditText(android.widget.EditText editText) {
        otherUtils.addAction(Messages.CLEAR_EDIT_TEXT, Type.input);
        super.clearEditText(editText);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to clear edit text
     *
     * @param index index of EditText
     *              <p/>
     *              <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clearEditText(1);
     *              }
     *              </pre>
     */
    @Override
    public void clearEditText(int index) {
        otherUtils.addAction(String.format(Messages.CLEAR_EDIT_TEXT_WITH_INDEX, index), Type.input);
        super.clearEditText(index);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to clear edit text
     *
     * @param text text of EditText
     *             <p/>
     *             <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clearEditText("text");
     *             }
     *             </pre>
     */
    public void clearEditText(String text) {
        otherUtils.addAction(Messages.CLEAR_EDIT_TEXT, Type.input);
        super.clearEditText(getEditText(text));
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click in list
     *
     * @param line line of first ListView
     * @return ArrayList of the TextView objects that the list line is showing
     * <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.clickInList(3);
     * }
     * </pre>
     */
    @Override
    public ArrayList<android.widget.TextView> clickInList(int line) {
        otherUtils.addAction(String.format(Messages.CLICK_IN_LIST, line), Type.click);
        ArrayList<android.widget.TextView> result = super.clickInList(line);
        String text = "";
        for (TextView aResult : result) {
            text += aResult.getText().toString() + " ";
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1); // remove last space
            otherUtils.changeActionDescription(String.format(Messages.CLICK_IN_LIST_WITH_TEXT, text, line));
        }
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to click in list
     *
     * @param line  line of ListView
     * @param index index of ListView
     * @return ArrayList of the TextView objects that the list line is showing
     * <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.clickInList(3, 1);
     * }
     * </pre>
     */
    @Override
    public ArrayList<android.widget.TextView> clickInList(int line, int index) {
        otherUtils.addAction(String.format(Messages.CLICK_IN_LIST, line), Type.click);
        ArrayList<android.widget.TextView> result = super.clickInList(line, index);
        String text = "";
        for (TextView aResult : result) {
            text += aResult.getText().toString() + " ";
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1); // remove last space
            otherUtils.changeActionDescription(String.format(Messages.CLICK_IN_LIST_WITH_TEXT, text, line));
        }
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to click long on button
     *
     * @param button instance of Button to click <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.clickLongOnButton(solo.getButton(1));
     *               }
     *               </pre>
     * @since 4.4
     */
    public void clickLongOnButton(Button button) {
        clicker.click(Button.class, button, true);
    }

    /**
     * Method used to click long on button
     *
     * @param button instance of Button to click
     * @param time   the amount of time to long click <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.clickLongOnButton(solo.getButton(1), 1000);
     *               }
     *               </pre>
     * @since 4.4
     */
    public void clickLongOnButton(Button button, int time) {
        clicker.click(Button.class, button, true, time);
    }

    /**
     * Method used to click long on button
     *
     * @param index index of Button to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnButton(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnButton(int index) {
        clicker.click(Button.class, index, true);
    }

    /**
     * Method used to click long on button
     *
     * @param index index of Button to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnButton(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnButton(int index, int time) {
        clicker.click(Button.class, index, true, time);
    }

    /**
     * Method used to click long on button
     *
     * @param name name of Button to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnButton("name");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnButton(String name) {
        clicker.click(Button.class, name, true);
    }

    /**
     * Method used to click long on button
     *
     * @param name name of Button to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnButton(name, 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnButton(String name, int time) {
        clicker.click(Button.class, name, true, time);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param checkBox instance of CheckBox to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnCheckBox((CheckBox)solo.findViewById("com.example.id.checkbox1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(CheckBox checkBox) {
        clicker.click(CheckBox.class, checkBox, true);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param checkBox instance of CheckBox to click
     * @param time     the amount of time to long click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnCheckBox((CheckBox)solo.findViewById("com.example.id.checkbox1"), 1000);
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(CheckBox checkBox, int time) {
        clicker.click(CheckBox.class, checkBox, true, time);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param index index of CheckBox to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnCheckBox(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(int index) {
        clicker.click(CheckBox.class, index, true);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param index index of CheckBox to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnCheckBox(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(int index, int time) {
        clicker.click(CheckBox.class, index, true, time);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param name name or text of CheckBox to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnCheckBox("check box");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(String name) {
        clicker.click(CheckBox.class, name, true);
    }

    /**
     * Method used to click long on checkbox
     *
     * @param name name or text of CheckBox to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnCheckBox("check box", 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnCheckBox(String name, int time) {
        clicker.click(CheckBox.class, name, true, time);
    }

    /**
     * Method used to click long on editText
     *
     * @param editText instance of EditText to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnEditText((EditText)solo.findViewById("com.example.id.editText1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(EditText editText) {
        clicker.click(EditText.class, editText, true);
    }

    /**
     * Method used to click long on editText
     *
     * @param editText instance of EditText to click
     * @param time     the amount of time to long click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnEditText((EditText)solo.findViewById("com.example.id.editText1"), 1000);
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(EditText editText, int time) {
        clicker.click(EditText.class, editText, true, time);
    }

    /**
     * Method used to click long on editText
     *
     * @param index index of EditText to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnEditText(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(int index) {
        clicker.click(EditText.class, index, true);
    }

    /**
     * Method used to click long on editText
     *
     * @param index index of EditText to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnEditText(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(int index, int time) {
        clicker.click(EditText.class, index, true, time);
    }

    /**
     * Method used to click long on editText
     *
     * @param name name or text of EditText to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnEditText("editText");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(String name) {
        clicker.click(EditText.class, name, true);
    }

    /**
     * Method used to click long on editText
     *
     * @param name name or text of EditText to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnEditText("editText", 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnEditText(String name, int time) {
        clicker.click(EditText.class, name, true, time);
    }

    /**
     * Method used to click long on imageView
     *
     * @param imageView instance of ImageView to click <h6>Example:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.clickLongOnImage((ImageView)solo.findViewById("com.example.id.imageView1"));
     *                  }
     *                  </pre>
     * @since 4.4
     */
    public void clickLongOnImage(ImageView imageView) {
        clicker.click(ImageView.class, imageView, true);
    }

    /**
     * Method used to click long on imageView
     *
     * @param imageView instance of ImageView to click
     * @param time      the amount of time to long click <h6>Example:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.clickLongOnImage((ImageView)solo.findViewById("com.example.id.imageView1"), 1000);
     *                  }
     *                  </pre>
     * @since 4.4
     */
    public void clickLongOnImage(ImageView imageView, int time) {
        clicker.click(ImageView.class, imageView, true, time);
    }

    /**
     * Method used to click long on imageView
     *
     * @param index index of ImageView to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnImage(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnImage(int index) {
        clicker.click(ImageView.class, index, true);
    }

    /**
     * Method used to click long on imageView
     *
     * @param index index of ImageView to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnImage(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnImage(int index, int time) {
        clicker.click(ImageView.class, index, true, time);
    }

    /**
     * Method used to click long on imageButton
     *
     * @param imageButton instance of ImageButton to click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickLongOnImageButton((ImageButton)solo.findViewById("com.example.id.imageButton1"));
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickLongOnImageButton(ImageButton imageButton) {
        clicker.click(ImageButton.class, imageButton, true);
    }

    /**
     * Method used to click long on imageButton
     *
     * @param imageButton instance of ImageButton to click
     * @param time        the amount of time to long click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickLongOnImageButton((ImageButton)solo.findViewById("com.example.id.imageButton1"), 1000);
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickLongOnImageButton(ImageButton imageButton, int time) {
        clicker.click(ImageButton.class, imageButton, true, time);
    }

    /**
     * Method used to click long on imageButton
     *
     * @param index index of ImageButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnImageButton(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnImageButton(int index) {
        clicker.click(ImageButton.class, index, true);
    }

    /**
     * Method used to click long on imageButton
     *
     * @param index index of ImageButton to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnImageButton(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnImageButton(int index, int time) {
        clicker.click(ImageButton.class, index, true, time);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param index index of RadioButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnRadioButton(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(int index) {
        clicker.click(RadioButton.class, index, true);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param index index of RadioButton to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnRadioButton(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(int index, int time) {
        clicker.click(RadioButton.class, index, true, time);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param radioButton instance of RadioButton to click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickLongOnRadioButton((RadioButton)solo.findViewById("com.example.id.radioButton1"));
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(RadioButton radioButton) {
        clicker.click(RadioButton.class, radioButton, true);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param radioButton instance of RadioButton to click
     * @param time        the amount of time to long click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickLongOnRadioButton((RadioButton)solo.findViewById("com.example.id.radioButton1"), 1000);
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(RadioButton radioButton, int time) {
        clicker.click(RadioButton.class, radioButton, true, time);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param name name or text of RadioButton to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnRadioButton("radioButton");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(String name) {
        clicker.click(RadioButton.class, name, true);
    }

    /**
     * Method used to click long on radioButton
     *
     * @param name name or text of RadioButton to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnRadioButton("radioButton", 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnRadioButton(String name, int time) {
        clicker.click(RadioButton.class, name, true, time);
    }

    /**
     * Method used to click long on screen
     *
     * @param x x position
     * @param y y position <h6>Example:</h6>
     *          <p/>
     *          <pre class="brush: java">
     *          {@code
     *          solo.clickLongOnScreen(341, 301);
     *          }
     *          </pre>
     */
    @Override
    public void clickLongOnScreen(float x, float y) {
        if (otherUtils.getDefaultDisplay().getOrientation() == ORIENTATION_LANDSCAPE) {
            float oldX = x;
            x = y;
            y = otherUtils.getScreenWidth() - oldX;
        }
        otherUtils.addAction(String.format(Messages.CLICK_LONG_ON_SCREEN, (int) x, (int) y), Type.click);
        super.clickLongOnScreen(x, y);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click long on screen
     *
     * @param x    x position
     * @param y    y position
     * @param time the amount of time to long click<h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnScreen(341, 301);
     *             }
     *             </pre>
     */
    @Override
    public void clickLongOnScreen(float x, float y, int time) {
        if (otherUtils.getDefaultDisplay().getOrientation() == ExtSolo.ORIENTATION_LANDSCAPE) {
            float oldX = x;
            x = y;
            y = otherUtils.getScreenWidth() - oldX;
        }
        otherUtils.addAction(String.format(Messages.CLICK_LONG_ON_SCREEN, (int) x, (int) y), Type.click);
        super.clickLongOnScreen(x, y, time);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click long on spinner
     *
     * @param index index of Spinner to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnSpinner(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnSpinner(int index) {
        clicker.click(Spinner.class, index, true);
    }

    /**
     * Method used to click long on spinner
     *
     * @param index index of Spinner to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnSpinner(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnSpinner(int index, int time) {
        clicker.click(Spinner.class, index, true, time);
    }

    /**
     * Method used to click long on spinner
     *
     * @param spinner instance of Spinner to click <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.clickLongOnSpinner((Spinner)solo.findViewById("com.example.id.spinner1"));
     *                }
     *                </pre>
     * @since 4.4
     */
    public void clickLongOnSpinner(Spinner spinner) {
        clicker.click(Spinner.class, spinner, true);
    }

    /**
     * Method used to click long on spinner
     *
     * @param spinner instance of Spinner to click
     * @param time    the amount of time to long click <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.clickLongOnSpinner((Spinner)solo.findViewById("com.example.id.spinner1"), 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public void clickLongOnSpinner(Spinner spinner, int time) {
        clicker.click(Spinner.class, spinner, true, time);
    }

    /**
     * Method used to click long on textView
     *
     * @param index index of TextView to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnText(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnText(int index) {
        clicker.click(TextView.class, index, true);
    }

    /**
     * Method used to click long on textView
     *
     * @param index index of TextView to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnText(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnText(int index, int time) {
        clicker.click(TextView.class, index, true, time);
    }

    /**
     * Method used to click long on textView
     *
     * @param text name or text of TextView to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnText("textView");
     *             }
     *             </pre>
     * @since 4.4
     */
    @Override
    public void clickLongOnText(String text) {
        clicker.click(TextView.class, text, true);
    }

    /**
     * Method used to click long on textView
     *
     * @param text name or text of TextView to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnText("textView", 1000);
     *             }
     *             </pre>
     */
    @Override
    public void clickLongOnText(String text, int time) {
        clicker.click(TextView.class, text, true, time);
    }

    /**
     * Method used to click long on textView
     *
     * @param textView instance of TextView to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnText((TextView)solo.findViewById("com.example.id.textView1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnText(TextView textView) {
        clicker.click(TextView.class, textView, true);
    }

    /**
     * Method used to click long on textView
     *
     * @param textView instance of TextView to click
     * @param time     the amount of time to long click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickLongOnText((TextView)solo.findViewById("com.example.id.textView1"), 1000);
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickLongOnText(TextView textView, int time) {
        clicker.click(TextView.class, textView, true, time);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param index index of ToggleButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnToggleButton(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(int index) {
        clicker.click(ToggleButton.class, index, true);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param index index of ToggleButton to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnToggleButton(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(int index, int time) {
        clicker.click(ToggleButton.class, index, true, time);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param name name or text of ToggleButton to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnToggleButton("toggleButton");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(String name) {
        clicker.click(ToggleButton.class, name, true);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param name name or text of ToggleButton to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnToggleButton("toggleButton", 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(String name, int time) {
        clicker.click(ToggleButton.class, name, true, time);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param toggleButton instance of ToggleButton to click <h6>Example:</h6>
     *                     <p/>
     *                     <pre class="brush: java">
     *                     {@code
     *                     solo.clickLongOnToggleButton((ToggleButton)solo.findViewById("com.example.id.toggleButton1"));
     *                     }
     *                     </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(ToggleButton toggleButton) {
        clicker.click(ToggleButton.class, toggleButton, true);
    }

    /**
     * Method used to click long on toggleButton
     *
     * @param toggleButton instance of ToggleButton to click
     * @param time         the amount of time to long click <h6>Example:</h6>
     *                     <p/>
     *                     <pre class="brush: java">
     *                     {@code
     *                     solo.clickLongOnToggleButton((ToggleButton)solo.findViewById("com.example.id.toggleButton1"), 1000);
     *                     }
     *                     </pre>
     * @since 4.4
     */
    public void clickLongOnToggleButton(ToggleButton toggleButton, int time) {
        clicker.click(ToggleButton.class, toggleButton, true, time);
    }

    /**
     * Method used to click long on view
     *
     * @param index index of View to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnView(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnView(int index) {
        clicker.click(View.class, index, true);
    }

    /**
     * Method used to click long on view
     *
     * @param index index of View to click
     * @param time  the amount of time to long click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickLongOnView(1, 1000);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickLongOnView(int index, int time) {
        clicker.click(View.class, index, true, time);
    }

    /**
     * Method used to click long on view
     *
     * @param text text of View to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnView("view");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnView(String text) {
        clicker.click(View.class, text, true);
    }

    /**
     * Method used to click long on view
     *
     * @param text text or text of View to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnView("view", 1000);
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickLongOnView(String text, int time) {
        clicker.click(View.class, text, true, time);
    }

    /**
     * Method used to click long on view
     *
     * @param view instance of View to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnView(solo.findViewById("com.example.id.view1"));
     *             }
     *             </pre>
     */
    @Override
    public void clickLongOnView(View view) {
        clicker.click(View.class, view, true);
    }

    /**
     * Method used to click long on view
     *
     * @param view instance of View to click
     * @param time the amount of time to long click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickLongOnView(solo.findViewById("com.example.id.view1"), 1000);
     *             }
     *             </pre>
     */
    @Override
    public void clickLongOnView(View view, int time) {
        clicker.click(View.class, view, true, time);
    }

    /**
     * Method used to click on button
     *
     * @param button instance of Button to click <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.clickOnButton((Button)solo.findViewById("com.example.id.button1"));
     *               }
     *               </pre>
     * @since 4.4
     */
    public void clickOnButton(Button button) {
        clicker.click(Button.class, button, false);
    }

    /**
     * Method used to click on button
     *
     * @param index index of Button to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnButton(1);
     *              }
     *              </pre>
     */
    @Override
    public void clickOnButton(int index) {
        clicker.click(Button.class, index, false);
    }

    /**
     * Method used to click on button
     *
     * @param name name or text of Button to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnButton("button");
     *             }
     *             </pre>
     */
    @Override
    public void clickOnButton(String name) {
        clicker.click(Button.class, name, false);
    }

    /**
     * Method used to click on checkBox
     *
     * @param checkBox instance of CheckBox to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickOnCheckBox((CheckBox)solo.findViewById("com.example.id.checkBox1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickOnCheckBox(CheckBox checkBox) {
        clicker.click(CheckBox.class, checkBox, false);
    }

    /**
     * Method used to click on checkBox
     *
     * @param index index of CheckBox to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnCheckBox(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    @Override
    public void clickOnCheckBox(int index) {
        clicker.click(CheckBox.class, index, false);
    }

    /**
     * Method used to click on checkBox
     *
     * @param name name or text of CheckBox to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnCheckBox(" checkBox");
     *             }
     *             </pre>
     */
    public void clickOnCheckBox(String name) {
        clicker.click(CheckBox.class, name, false);
    }

    /**
     * Method used to click on editText
     *
     * @param editText instance of EditText to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickOnEditText((EditText)solo.findViewById("com.example.id.editText1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickOnEditText(EditText editText) {
        clicker.click(EditText.class, editText, false);
    }

    /**
     * Method used to click on editText
     *
     * @param index index of EditText to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnEditText(1);
     *              }
     *              </pre>
     */
    @Override
    public void clickOnEditText(int index) {
        clicker.click(EditText.class, index, false);
    }

    /**
     * Method used to click on editText
     *
     * @param text text or text of EditText to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnEditText(" editText");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickOnEditText(String text) {
        clicker.click(EditText.class, text, false);
    }

    /**
     * Method used to click on HTML element inside WebView
     *
     * @param xPath XPATH to find element <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnHtmlElement("(//a[@id = 'id'])[1]");
     *              }
     *              </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #clickOnWebElement(By by)}
     */
    public void clickOnHtmlElement(String xPath) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_HTML_ELEMENT_XPATH, xPath), Type.click);
        htmlUtils.clickOnHtmlElement(null, null, null, null, -1, xPath, null, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on HTML element inside WebView. If index is set to
     * -1, click will be involved on every found element
     *
     * @param tag       tag of element
     * @param id        id of element, null or empty string, if does not exist
     * @param name      name of element, null or empty string, if does not exist
     * @param className class of element, null or empty string, if does not exist
     * @param index     index of element, -1 if unknown <h6>Examples:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.clickOnHtmlElement("input", "id", "", "login", 0);
     *                  <p/>
     *                  solo.clickOnHtmlElement("a", "", "", "", 5);
     *                  }
     *                  </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #clickOnWebElement(By by)}
     */
    public void clickOnHtmlElement(String tag, String id, String name, String className, int index) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_HTML_ELEMENT_TAG, tag, id, name, className), Type.click);
        htmlUtils.clickOnHtmlElement(tag, id, name, className, index < 0 ? 0 : index, null, null, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on HTML element inside WebView. If index is set to
     * -1, click will be involved on every found element
     *
     * @param tag              tag of element
     * @param id               id of element, null or empty string, if does not exist
     * @param name             name of element, null or empty string, if does not exist
     * @param className        class of element, null or empty string, if does not exist
     * @param index            index of element, -1 if unknown
     * @param customAttributes Map of extra attributes, null allowed <h6>Example:</h6>
     *                         <p/>
     *                         <pre class="brush: java">
     *                         {@code
     *                         solo.clickOnHtmlElement("a", "id", "", "class", 0,
     *new HashMap<String, String>() {
     *                         {
     *                         put("custom_id", "my_val");
     *                         };
     *                         }
     *                         );
     *                         }
     *                         </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #clickOnWebElement(By by)}
     */
    public void clickOnHtmlElement(
            String tag, String id, String name, String className, int index, Map<String, String> customAttributes) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_HTML_ELEMENT_TAG, tag, id, name, className), Type.click);
        htmlUtils.clickOnHtmlElement(tag, id, name, className, index < 0 ? 0 : index, null, customAttributes, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on HTML element inside WebView
     *
     * @param jQuerySelector jQuery selector to find element <h6>Example:</h6>
     *                       <p/>
     *                       <pre class="brush: java">
     *                       {@code
     *                       solo.clickOnHtmlElementBySelector("$('p.class1')");
     *                       }
     *                       </pre>
     * @since 4.3
     * @deprecated it's better to use {@link #clickOnHtmlElementByCssSelector(String, int)}
     * as this method convert jQuery selector to css selector since version 4.5
     * what is not always possible
     */
    @Deprecated
    public void clickOnHtmlElementBySelector(String jQuerySelector) {
        String cssSelector = jQuerySelector.replace("$('", "").replace("')", ""); //primitive attempt to convert from jQuery selector to css selector

        clickOnHtmlElementByCssSelector(cssSelector, 0);
    }

    /**
     * Method used to click on HTML element inside WebView
     *
     * @param cssSelector css selector to find element - first matched will be used
     *                    <p/>
     *                    <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickOnHtmlElementByCssSelector("p#id.class1");
     *                    }
     *                    </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #clickOnWebElement(By by)}
     */
    public void clickOnHtmlElementByCssSelector(String cssSelector) {
        clickOnHtmlElementByCssSelector(cssSelector, 0);
    }

    /**
     * Method used to click on HTML element inside WebView
     *
     * @param cssSelector css selector to find element
     * @param index       index of matched elements
     *                    <p/>
     *                    <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickOnHtmlElementByCssSelector("p#id.class1", 2);
     *                    }
     *                    </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #clickOnWebElement(By by)}
     */
    public void clickOnHtmlElementByCssSelector(String cssSelector, int index) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_HTML_ELEMENT_BY_CSS_SELECTOR, cssSelector, index),
                Type.click);
        htmlUtils.clickOnHtmlElement(null, null, null, null, index < 0 ? 0 : index, null, null, cssSelector);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on imageView
     *
     * @param imageView instance of ImageView to click <h6>Example:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.clickOnImage((ImageView)solo.findViewById("com.example.id.imageView1"));
     *                  }
     *                  </pre>
     * @since 4.4
     */
    public void clickOnImage(ImageView imageView) {
        clicker.click(ImageView.class, imageView, false);
    }

    /**
     * Method used to click on imageView
     *
     * @param index index of ImageView to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnImage(1);
     *              }
     *              </pre>
     */
    @Override
    public void clickOnImage(int index) {
        clicker.click(ImageView.class, index, false);
    }

    /**
     * Method used to click on imageButton
     *
     * @param imageButton instance of ImageButton to click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickOnImageButton((ImageButton)solo.findViewById("com.example.id.imageButton1"));
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickOnImageButton(ImageButton imageButton) {
        clicker.click(ImageButton.class, imageButton, false);
    }

    /**
     * Method used to click on imageButton
     *
     * @param index index of ImageButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnImageButton(1);
     *              }
     *              </pre>
     */
    @Override
    public void clickOnImageButton(int index) {
        clicker.click(ImageButton.class, index, false);
    }

    /**
     * Method used to click on menu item
     *
     * @param text text of menu item to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnMenuItem("Settings");
     *             }
     *             </pre>
     */
    @Override
    public void clickOnMenuItem(String text) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_MENU_ITEM, text), Type.click);
        super.clickOnMenuItem(text);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on radioButton
     *
     * @param index index of RadioButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnRadioButton(1);
     *              }
     *              </pre>
     */
    @Override
    public void clickOnRadioButton(int index) {
        clicker.click(RadioButton.class, index, false);
    }

    /**
     * Method used to click on radioButton
     *
     * @param radioButton instance of RadioButton to click <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.clickOnRadioButton((RadioButton)solo.findViewById("com.example.id.radioButton1"));
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void clickOnRadioButton(RadioButton radioButton) {
        clicker.click(RadioButton.class, radioButton, false);
    }

    /**
     * Method used to click on radioButton
     *
     * @param text text of RadioButton to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnRadioButton("radioButton");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickOnRadioButton(String text) {
        clicker.click(RadioButton.class, text, false);
    }

    /**
     * Method used to click on screen
     *
     * @param x x position
     * @param y y position <h6>Example:</h6>
     *          <p/>
     *          <pre class="brush: java">
     *          {@code
     *          solo.clickOnScreen(341, 301);
     *          }
     *          </pre>
     */
    @Override
    public void clickOnScreen(float x, float y) {
        //landscape mode - translate position according to this. We recording as if rotation doesn't
        //influence on x,y clicks        
        if (otherUtils.getDefaultDisplay().getOrientation() == ORIENTATION_LANDSCAPE) {
            float oldX = x;
            x = y;
            y = otherUtils.getScreenWidth() - oldX;
        }

        otherUtils.addAction(String.format(Messages.CLICK_ON_SCREEN, (int) x, (int) y), Type.click);

        super.clickOnScreen(x, y);

        otherUtils.addDurationToAction();
    }

    /**
     * Method used to click on textView
     *
     * @param text text of TextView to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnText("textView");
     *             }
     *             </pre>
     */
    @Override
    public void clickOnText(String text) {
        clicker.click(TextView.class, text, false);
    }

    /**
     * Method used to click on textView
     *
     * @param index index of TextView to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnText(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickOnText(int index) {
        clicker.click(TextView.class, index, false);
    }

    /**
     * Method used to click on textView
     *
     * @param textView instance of TextView to click <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.clickOnText((TextView)solo.findViewById("com.example.id.textView1"));
     *                 }
     *                 </pre>
     * @since 4.4
     */
    public void clickOnText(TextView textView) {
        clicker.click(TextView.class, textView, false);
    }

    /**
     * Method used to click on toggleButton
     *
     * @param index index of ToggleButton to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnToggleButton(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickOnToggleButton(int index) {
        clicker.click(ToggleButton.class, index, false);
    }

    /**
     * Method used to click on toggleButton
     *
     * @param name name or text of ToggleButton to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnToggleButton("toggleButton");
     *             }
     *             </pre>
     */
    @Override
    public void clickOnToggleButton(String name) {
        clicker.click(ToggleButton.class, name, false);
    }

    /**
     * Method used to click on toggleButton
     *
     * @param toggleButton instance of ToggleButton to click <h6>Example:</h6>
     *                     <p/>
     *                     <pre class="brush: java">
     *                     {@code
     *                     solo.clickOnToggleButton((ToggleButton)solo.findViewById("com.example.id.toggleButton1"));
     *                     }
     *                     </pre>
     * @since 4.4
     */
    public void clickOnToggleButton(ToggleButton toggleButton) {
        clicker.click(ToggleButton.class, toggleButton, false);
    }

    /**
     * Method used to click on view
     *
     * @param index index of View to click <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnView(1);
     *              }
     *              </pre>
     * @since 4.4
     */
    public void clickOnView(int index) {
        clicker.click(View.class, index, false);
    }

    /**
     * Method used to click on view
     *
     * @param text text of View to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnView("view");
     *             }
     *             </pre>
     * @since 4.4
     */
    public void clickOnView(String text) {
        clicker.click(View.class, text, false);
    }

    /**
     * Method used to click on view
     *
     * @param view instance of View to click <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.clickOnView((View)solo.findViewById("com.example.id.view1"));
     *             }
     *             </pre>
     */
    @Override
    public void clickOnView(View view) {
        clicker.click(View.class, view, false);
    }

    /**
     * Simulate touching a given location and dragging it to a new location
     *
     * @param fromX     initial x position
     * @param toX       final x position
     * @param fromY     initial y position
     * @param toY       final y position
     * @param stepCount count of steps
     *                  <p/>
     *                  <p/>
     *                  <h6>Examples:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.drag(solo.toScreenX(0.744f), solo.toScreenX(0.031f),
     *                  solo.toScreenY(0.302f), solo.toScreenY(0.671f), 4);
     *                  <p/>
     *                  solo.drag(500, 400, 100, 250, 5);
     *                  }
     *                  </pre>
     */
    @Override
    public void drag(float fromX, float toX, float fromY, float toY,                     int stepCount) {
        //landscape mode - translate position according to this. We recording as if rotation doesn't
        //influence on x,y clicks
        if (otherUtils.getDefaultDisplay().getOrientation() == ORIENTATION_LANDSCAPE) {
            float oldFromX = fromX;
            fromX = fromY;
            fromY = otherUtils.getScreenWidth() - oldFromX;

            float oldToX = toX;
            toX = toY;
            toY = otherUtils.getScreenWidth() - oldToX;
        }
        otherUtils.addAction(String.format(Messages.DRAG, (int) fromX, (int) fromY, (int) toX, (int) toY), Type.drag);
        super.drag(fromX, toX, fromY, toY, stepCount);
        otherUtils.addDurationToAction();
    }

    /**
     * Simulate touching a given location and dragging it to a new location in
     * the specified time.
     *
     * @param fromX     initial x position
     * @param toX       final x position
     * @param fromY     initial y position
     * @param toY       final y position
     * @param stepCount count of steps
     * @param elapsed   specified time in milliseconds
     *                  <p/>
     *                  <h6>Examples:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.drag(solo.toScreenX(0.744f), solo.toScreenX(0.031f),
     *                  solo.toScreenY(0.302f), solo.toScreenY(0.671f), 4, 100
     *                  );
     *                  <p/>
     *                  solo.drag(500, 400, 100, 250, 5, 100);
     *                  }
     *                  </pre>
     * @since 1.0
     */
    public void drag(float fromX, float toX, float fromY, float toY, int stepCount, int elapsed) {
        otherUtils.addAction(String.format(Messages.DRAG, (int) fromX, (int) fromY, (int) toX, (int) toY), Type.drag);
        otherUtils.drag(fromX, toX, fromY, toY, stepCount, elapsed);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param xPath XPATH to find element
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(index: 0, name: j_username).",
     * solo.elementExistsOnHtmlPage("(//input[@id = 'id'])[0]")
     * );
     * }
     * </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #waitForWebElement(By by)}
     */
    public boolean elementExistsOnHtmlPage(String xPath) {
        otherUtils.addAction(String.format(Messages.ELEMENT_EXISTS_ON_HTML_PAGE_XPATH, xPath), Type.assertion);
        boolean result = htmlUtils.elementExistsOnHtmlPage(null, null, null, null, -1, xPath, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param tag       tag of element
     * @param id        id of element, null or empty string, if does not exist
     * @param name      name of element, null or empty string, if does not exist
     * @param className class of element, null or empty string, if does not exist
     * @param index     index of element, -1 if unknown
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(index: 0, name: j_username).",
     * solo.elementExistsOnHtmlPage("input", "", "j_username", "", 0)
     * );
     * }
     * </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #waitForWebElement(By by)}
     */
    public boolean elementExistsOnHtmlPage(String tag, String id, String name, String className, int index) {
        otherUtils.addAction(String.format(Messages.ELEMENT_EXISTS_ON_HTML_PAGE_TAG, tag, id, name, className),
                Type.assertion);
        boolean result = htmlUtils.elementExistsOnHtmlPage(tag, id, name, className, index, null, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param tag              tag of element
     * @param id               id of element, null or empty string, if does not exist
     * @param name             name of element, null or empty string, if does not exist
     * @param className        class of element, null or empty string, if does not exist
     * @param index            index of element, -1 if unknown
     * @param customAttributes Map of extra attributes, null allowed
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(index: 0, name: j_username).",
     * solo.elementExistsOnHtmlPage("input", "", "j_username", "", 0,
     * new HashMap<String, String>() {
     * put("custom_id", "my_val");
     * }
     * )
     * );
     * }
     * </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #waitForWebElement(By by)}
     */
    public boolean elementExistsOnHtmlPage(
            String tag, String id, String name, String className, int index, Map<String, String> customAttributes) {
        otherUtils.addAction(String.format(Messages.ELEMENT_EXISTS_ON_HTML_PAGE_TAG, tag, id, name, className),
                Type.assertion);
        boolean result = htmlUtils.elementExistsOnHtmlPage(tag, id, name, className, index, null, customAttributes,
                null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param jQuerySelector jQuery selector to find element
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(p, class: class1) failed.",
     * solo.elementExistsOnHtmlPageBySelector("$('p.class1')")
     * );
     * }
     * </pre>
     * @since 4.3
     * @deprecated it's better to use {@link #elementExistsOnHtmlPageByCssSelector(String)}
     * as this method convert jQuery selector to css selector since version 4.5
     * what is not always possible
     */
    @Deprecated
    public boolean elementExistsOnHtmlPageBySelector(String jQuerySelector) {
        //primitive attempt to convert from jQuery selector to css selector
        String cssSelector = jQuerySelector.replace("$('", "").replace("')", "");

        return elementExistsOnHtmlPageByCssSelector(cssSelector, 0);
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param cssSelector css selector to find any matched element
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(p, class: class1) failed.",
     * solo.elementExistsOnHtmlPageByCssSelector("p.class1")
     * );
     * }
     * </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #waitForWebElement(By by)}
     */
    public boolean elementExistsOnHtmlPageByCssSelector(String cssSelector) {
        return elementExistsOnHtmlPageByCssSelector(cssSelector, 0);
    }

    /**
     * Method used to check, if HTML element exists on HTML page inside WebView
     *
     * @param cssSelector css selector to find element
     * @param index       index of matched elements
     * @return true if element exists, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Element does not exist - input(p, class: class1) failed.",
     * solo.elementExistsOnHtmlPageByCssSelector("p.class1", 2)
     * );
     * }
     * </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #waitForWebElement(By by)}
     */
    public boolean elementExistsOnHtmlPageByCssSelector(String cssSelector, int index) {
        otherUtils.addAction(String.format(Messages.ELEMENT_EXISTS_ON_HTML_PAGE_BY_CSS_SELECTOR, cssSelector, index),
                Type.assertion);
        boolean result = htmlUtils.elementExistsOnHtmlPage(null, null, null, null, index, null, null, cssSelector);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to enter text into editText
     *
     * @param editText instance of EditText
     * @param text     text to enter <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.enterText((EditText)solo.findViewById("com.example.id.textView1"), "textToEnter");
     *                 }
     *                 </pre>
     */
    @Override
    public void enterText(android.widget.EditText editText, String text) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT, text), Type.input);

        if (editText instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) editText).setThreshold(Integer.MAX_VALUE);
        }

        super.clearEditText(editText);
        super.enterText(editText, text);

        otherUtils.addDurationToAction();
    }

    /**
     * Method used to enter text into editText
     *
     * @param index index of EditText
     * @param text  text to enter <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.enterText(0, "textToEnter");
     *              }
     *              </pre>
     */
    @Override
    public void enterText(int index, String text) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT, text), Type.input);
        View view = super.getView(EditText.class, index);

        if (view instanceof AutoCompleteTextView) {
            ((AutoCompleteTextView) view).setThreshold(Integer.MAX_VALUE);
        }

        super.clearEditText(index);
        super.enterText(index, text);

        otherUtils.addDurationToAction();
    }

    /**
     * Method used to enter text into HTML element inside WebView
     *
     * @param xPath XPATH to find element
     * @param text  text to enter <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.enterTextIntoHtmlElement("(//input[@id = 'id'])[0]", "textToEnter");
     *              }
     *              </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #enterTextInWebElement(By by, String text)}
     */
    public void enterTextIntoHtmlElement(String xPath, String text) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT_XPATH, text, xPath), Type.input);
        htmlUtils.enterTextIntoHtmlElement(null, null, null, null, text, -1, xPath, null, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to enter text into HTML element inside WebView. If index is
     * set to -1, enter text will be involved on every found element
     *
     * @param tag       tag of element
     * @param id        id of element, null or empty string, if does not exist
     * @param name      name of element, null or empty string, if does not exist
     * @param className class of element, null or empty string, if does not exist
     * @param text      text to enter
     * @param index     index of element, -1 if unknown <h6>Example:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.enterTextIntoHtmlElement("input", "", "j_username", "", "login", 0);
     *                  }
     *                  </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #enterTextInWebElement(By by, String text)}
     */
    public void enterTextIntoHtmlElement(String tag, String id, String name, String className, String text, int index) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT_TAG, text, tag, id, name, className), Type.input);
        htmlUtils.enterTextIntoHtmlElement(tag, id, name, className, text, index, null, null, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to enter text into HTML element inside WebView. If index is
     * set to -1, enter text will be involved on every found element
     *
     * @param tag              tag of element
     * @param id               id of element, null or empty string, if does not exist
     * @param name             name of element, null or empty string, if does not exist
     * @param className        class of element, null or empty string, if does not exist
     * @param text             text to enter
     * @param index            index of element, -1 if unknown
     * @param customAttributes Map of extra attributes, null allowed <h6>Example:</h6>
     *                         <p/>
     *                         <pre class="brush: java">
     *                         {@code
     *                         solo.enterTextIntoHtmlElement("input", "", "j_username", "", "login", 0,
     *new HashMap<String, String>() {
     *                         {
     *                         put("custom_id", "my_val");
     *                         };
     *                         }
     *                         );
     *                         }
     *                         </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #enterTextInWebElement(By by, String text)}
     */
    public void enterTextIntoHtmlElement(
            String tag, String id, String name, String className, String text, int index,
            Map<String, String> customAttributes) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT_TAG, text, tag, id, name, className), Type.input);
        htmlUtils.enterTextIntoHtmlElement(tag, id, name, className, text, index, null, customAttributes, null);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to enter text into HTML element inside WebView
     *
     * @param jQuerySelector jQuery selector to find element
     * @param text           text to enter <h6>Example:</h6>
     *                       <p/>
     *                       <pre class="brush: java">
     *                       {@code
     *                       solo.enterTextIntoHtmlElementBySelector("$('p.class1')", "textToEnter");
     *                       }
     *                       </pre>
     * @since 4.3
     * @deprecated it's better to use {@link #enterTextIntoHtmlElementByCssSelector(String, int, String)}
     * as this method convert jQuery selector to css selector since version 4.5
     * what is not always possible
     */
    @Deprecated
    public void enterTextIntoHtmlElementBySelector(String jQuerySelector, String text) {
        //primitive attempt to convert from jQuery selector to css selector
        String cssSelector = jQuerySelector.replace("$('", "").replace("')", "");

        enterTextIntoHtmlElementByCssSelector(cssSelector, 0, text);
    }

    /**
     * Method used to enter text into HTML element inside WebView
     *
     * @param cssSelector css selector to find element - first matched will be used
     * @param text        text to enter
     *                    <p/>
     *                    <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.enterTextIntoHtmlElementByCssSelector("p#id.class1", "textToEnter");
     *                    }
     *                    </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #enterTextInWebElement(By by, String text)}
     */
    public void enterTextIntoHtmlElementByCssSelector(String cssSelector, String text) {
        enterTextIntoHtmlElementByCssSelector(cssSelector, 0, text);
    }

    /**
     * Method used to enter text into HTML element inside WebView
     *
     * @param cssSelector css selector to find element
     * @param index       index of matched elements
     * @param text        text to enter
     *                    <p/>
     *                    <h6>Example:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.enterTextIntoHtmlElementByCssSelector("p#id.class1", 2, "textToEnter");
     *                    }
     *                    </pre>
     * @since 4.5
     * @deprecated it's better to use {@link #enterTextInWebElement(By by, String text)}
     */
    public void enterTextIntoHtmlElementByCssSelector(String cssSelector, int index, String text) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT_BY_CSS_SELECTOR, text, cssSelector, index), Type.input);
        htmlUtils.enterTextIntoHtmlElement(null, null, null, null, text, index, null, null, cssSelector);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to save information about failed action
     *
     * @since 2.7 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * try {
     * } catch (Exception e) {
     * solo.fail("fail", e);
     * throw e;
     * }
     * }
     * </pre>
     */
    public void fail(String name, Object e) {
        otherUtils.fail(name, e);
    }

    /**
     * Method used to find View specified by its id.
     *
     * @param id id of View
     * @return View <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.findViewById(com.bitbar.testdroid.R.id.webview);
     * }
     * </pre>
     * @since 1.0
     */
    public View findViewById(int id) {
        View result;
        String viewId;
        try {
            viewId = getInstrumentation().getTargetContext().getResources().getResourceEntryName(id);
        } catch (NotFoundException e) {
            viewId = String.valueOf(id);
        }
        otherUtils.addAction(String.format(Messages.FIND_VIEW_BY_ID, viewId), Type.util);
        result = otherUtils.findViewById(id);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to find View specified by its id.
     *
     * @param id id of View
     * @return View <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.findViewById("sample_id");
     * }
     * </pre>
     * @since 1.0
     */
    public View findViewById(String id) {
        otherUtils.addAction(String.format(Messages.FIND_VIEW_BY_ID, id), Type.util);
        View result = otherUtils.findViewById(id);
        otherUtils.addDurationToAction();
        return result;
    }

    Clicker getClicker() {
        return clicker;
    }

    /**
     * Get all ListView widgets visible on screen
     *
     * @return all ListView visible on screen
     */
    public ArrayList<ListView> getCurrentListViews() {
        return getCurrentViews(ListView.class);
    }

    @Override
    public EditText getEditText(String text) {
        otherUtils.addAction(String.format(Messages.GET_EDIT_TEXT, text), Type.util);
        EditText result = super.getEditText(text);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to get HTML code from HTML page inside WebView
     *
     * @return String with HTML code <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {
     * &#064;code
     * String myCode = solo.getHtmlCode();
     * }
     * </pre>
     * @since 2.5
     */
    public String getHtmlCode() {
        otherUtils.addAction(Messages.GET_HTML_CODE, Type.util);
        String result = htmlUtils.getHtmlCode();
        otherUtils.addDurationToAction();
        return result;
    }

    HtmlUtils getHtmlUtils() {
        return htmlUtils;
    }

    Instrumentation getInstrumentation() {
        return inst;
    }

    OtherUtils getOtherUtils() {
        return otherUtils;
    }

    ScreenshotUtils getScreenshootUtils() {
        return screenshotUtils;
    }

    /**
     * Method used to get TextView with specified text
     *
     * @param text text of TextView
     * @return TextView with specified text <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.getText("TEXT");
     * }
     * </pre>
     */
    @Override
    public TextView getText(String text) {
        otherUtils.addAction(String.format(Messages.GET_TEXT, text), Type.util);
        TextView result = super.getText(text);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to get View with specified index
     *
     * @param index index of View
     * @return View with specified index <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.getViewByIndex(5);
     * }
     * </pre>
     */
    public View getViewByIndex(int index) {
        otherUtils.addAction(String.format(Messages.GET_VIEW, index), Type.util);
        View result = super.getView(View.class, index);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to get View with specified text and class
     *
     * @param classToFilterBy class used to filter views with given text
     * @param text            text inside View
     * @return View with specified text and class <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.getViewByIndex(Button.class, "Click me!");
     * }
     * </pre>
     */
    public <T extends View> View getViewByText(Class<T> classToFilterBy, String text) {
        View result = null;

        if (classToFilterBy == null) {
            classToFilterBy = (Class<T>) TextView.class;
        }

        otherUtils.addAction(String.format(Messages.GET_VIEW_BY_TEXT, text), Type.util);

        for (View view : getCurrentViews(classToFilterBy)) {
            if (view instanceof TextView && ((TextView) view).getText().equals(text)) {
                result = view;
                break;
            }
        }

        otherUtils.addDurationToAction();
        return result;
    }

    Waiter getWaiter() {
        return waiter;
    }

    /**
     * Method used to go back
     */
    @Override
    public void goBack() {
        otherUtils.addAction(Messages.GO_BACK, Type.navigation);
        super.goBack();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to hide keyboard
     *
     * @since 1.0 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.hideKeyboard();
     * }
     * </pre>
     */
    public void hideKeyboard() {
        otherUtils.addAction(Messages.HIDE_KEYBOARD, Type.config);
        otherUtils.hideKeyboard();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to go back on HTML page inside WebView
     *
     * @since 2.5 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.htmlGoBack();
     * }
     * </pre>
     */
    public void htmlGoBack() {
        otherUtils.addAction(Messages.HTML_GO_BACK, Type.navigation);
        htmlUtils.htmlGoBack();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to go forward on HTML page inside WebView
     *
     * @since 2.5 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.htmlGoForward();
     * }
     * </pre>
     */
    public void htmlGoForward() {
        otherUtils.addAction(Messages.HTML_GO_FORWARD, Type.navigation);
        htmlUtils.htmlGoForward();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to zoom in HTML page inside WebView
     *
     * @since 2.5 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.htmlZoomIn();
     * }
     * </pre>
     */
    public void htmlZoomIn() {
        otherUtils.addAction(Messages.HTML_ZOOM_IN, Type.drag);
        htmlUtils.htmlZoomIn();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to zoom out HTML page inside WebView
     *
     * @since 2.5 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.htmlZoomOut();
     * }
     * </pre>
     */
    public void htmlZoomOut() {
        otherUtils.addAction(Messages.HTML_ZOOM_OUT, Type.drag);
        htmlUtils.htmlZoomOut();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to inject JavaScript code into HTML page inside WebView
     *
     * @param code String with JavaScript code (tag 'script' should be missed)
     *             <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.injectJavaScriptCode("alert('test');");
     *             }
     *             </pre>
     * @since 2.5
     */
    public void injectJavaScriptCode(String code) {
        otherUtils.addAction(Messages.INJECT_JAVASCRIPT_CODE, Type.util);
        htmlUtils.injectJavaScriptCode(code);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to inject JavaScript file into HTML page inside WebView
     *
     * @param fileName name of file to inject (file must be placed in Assets of test
     *                 project) <h6>Example:</h6>
     *                 <p/>
     *                 <pre class="brush: java">
     *                 {@code
     *                 solo.injectJavaScriptFile("jsFile.js");
     *                 }
     *                 </pre>
     * @since 2.5
     */
    public void injectJavaScriptFile(String fileName) throws IOException {
        otherUtils.addAction(Messages.INJECT_JAVASCRIPT_FILE, Type.util);
        htmlUtils.injectJavaScriptFile(fileName);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to check if specified text is present on HTML page inside
     * WebView
     *
     * @param text specified text
     * @return true if text is present, false otherwise <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Text isn't present - text",
     *solo.isTextPresentOnHtmlPage("searchedText")
     * );
     * }
     * </pre>
     * @since 2.5
     * @deprecated it's better to use {@link #waitForText(String text)}
     */
    public boolean isTextPresentOnHtmlPage(String text) {
        otherUtils.addAction(String.format(Messages.IS_TEXT_PRESENT_ON_HTML_PAGE, text), Type.assertion);
        boolean result = htmlUtils.isTextPresentOnHtmlPage(text);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to log some info in LogCat
     *
     * @since 2.7 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.logInfoInLogcat("Tag", "some info");
     * }
     * </pre>
     */
    public void logInfoInLogcat(String tag, String message) {
        otherUtils.addAction(String.format(Messages.LOG_INFO_IN_LOGCAT, tag, message), Type.util);
        Log.i(tag, message);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to log very log info in LogCat
     *
     * @since 4.4 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.logInfoInLogcat("Tag", solo.getHtmlCode());
     * }
     * </pre>
     */
    public void logLongInfoInLogcat(String tag, String message) {
        if (message.length() > 4000) {
            Log.d(tag, message);
            logLongInfoInLogcat(tag, message.substring(4000));
        } else {
            Log.d(tag, message);
        }
    }

    /**
     * Method used to move gallery to left.
     *
     * @param gallery       instance of Gallery
     * @param numberOfTimes number of moves <h6>Example:</h6>
     *                      <p/>
     *                      <pre class="brush: java">
     *                      {@code
     *                      solo.moveGalleryLeft(solo.findViewById(com.testdroid.R.id.gallery), 2);
     *                      }
     *                      </pre>
     * @since 3.9
     */
    public void moveGalleryLeft(Gallery gallery, int numberOfTimes) {
        otherUtils.addAction(String.format(Messages.MOVE_GALLERY, otherUtils.getDescriptionFromView(gallery)),
                Type.scroll);
        otherUtils.moveGalleryLeft(gallery, numberOfTimes);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to move gallery to right.
     *
     * @param gallery       instance of Gallery
     * @param numberOfTimes number of moves <h6>Example:</h6>
     *                      <p/>
     *                      <pre class="brush: java">
     *                      {@code
     *                      solo.moveGalleryRight(solo.findViewById(com.testdroid.R.id.gallery), 2);
     *                      }
     *                      </pre>
     * @since 3.9
     */
    public void moveGalleryRight(Gallery gallery, int numberOfTimes) {
        otherUtils.addAction(String.format(Messages.MOVE_GALLERY, otherUtils.getDescriptionFromView(gallery)),
                Type.scroll);
        otherUtils.moveGalleryRight(gallery, numberOfTimes);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to simulate touching a given location (first point) and
     * dragging it to a new location (last point) going through all points.
     * Values have to be normalized.
     *
     * @param points array of arrays of floats <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.multiDrag({{ 0.1685f, 0.2542f }, { 0.2741f, 0.3573f },
     *               { 0.4000f, 0.2750f }, { 0.4889f, 0.2156f }});
     *               }
     *               </pre>
     * @since 2.8
     */
    public void multiDrag(float[][] points) {
        otherUtils.addAction(Messages.MULTIPATH_DRAG, Type.drag);
        otherUtils.multiDrag(points);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to check, how many HTML elements exist on HTML page inside
     * WebView
     *
     * @param xPath XPATH to find element
     * @return number of elements <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue(solo.numberOfHtmlElements("(//input[@id = 'id'])") > 3);
     * }
     * </pre>
     * @since 4.1
     */
    public int numberOfHtmlElements(String xPath) {
        otherUtils.addAction(String.format(Messages.NUMBER_OF_HTML_ELEMENTS_XPATH, xPath), Type.util);
        int result = htmlUtils.numberOfHtmlElements(null, null, null, null, xPath, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, how many HTML elements exist on HTML page inside
     * WebView
     *
     * @param tag       tag of element
     * @param id        id of element, null or empty string, if does not exist
     * @param name      name of element, null or empty string, if does not exist
     * @param className class of element, null or empty string, if does not exist
     * @return number of elements <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue(solo.numberOfHtmlElements("input", "", "j_username", "") > 3);
     * }
     * </pre>
     * @since 4.1
     */
    public int numberOfHtmlElements(String tag, String id, String name, String className) {
        otherUtils.addAction(String.format(Messages.NUMBER_OF_HTML_ELEMENTS_TAG, tag, id, name, className), Type.util);
        int result = htmlUtils.numberOfHtmlElements(tag, id, name, className, null, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, how many HTML elements exist on HTML page inside
     * WebView
     *
     * @param tag              tag of element
     * @param id               id of element, null or empty string, if does not exist
     * @param name             name of element, null or empty string, if does not exist
     * @param className        class of element, null or empty string, if does not exist
     * @param customAttributes Map of extra attributes, null allowed
     * @return number of elements <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue(solo.numberOfHtmlElements("input", "", "j_username", "",
     *new HashMap<String, String>() {
     * put("custom_id", "my_val");
     * }
     * ) > 3);
     * }
     * </pre>
     * @since 4.1
     */
    public int numberOfHtmlElements(
            String tag, String id, String name, String className, Map<String, String> customAttributes) {
        otherUtils.addAction(String.format(Messages.NUMBER_OF_HTML_ELEMENTS_TAG, tag, id, name, className), Type.util);
        int result = htmlUtils.numberOfHtmlElements(tag, id, name, className, null, customAttributes, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to check, how many HTML elements exist on HTML page inside
     * WebView
     *
     * @param jQuerySelector jQuery selector to find element
     * @return number of elements <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue(solo.numberOfHtmlElementsBySelector("$('p.class1')") > 3);
     * );
     * }
     * </pre>
     * @since 4.3
     * @deprecated it's better to use {@link #enterTextIntoHtmlElementByCssSelector(String, int, String)}
     * as this method convert jQuery selector to css selector since version 4.5
     * what is not always possible
     */
    @Deprecated
    public int numberOfHtmlElementsBySelector(String jQuerySelector) {
        //primitive attempt to convert from jQuery selector to css selector
        String cssSelector = jQuerySelector.replace("$('", "").replace("')", "");

        return numberOfHtmlElementsByCssSelector(cssSelector);
    }

    /**
     * Method used to check, how many HTML elements exist on HTML page inside
     * WebView
     *
     * @param cssSelector css selector to find element
     * @return number of elements <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue(solo.numberOfHtmlElementsByCssSelector("#p#id.class1") > 3);
     * );
     * }
     * </pre>
     * @since 4.5
     */
    public int numberOfHtmlElementsByCssSelector(String cssSelector) {
        otherUtils.addAction(String.format(Messages.NUMBER_OF_HTML_ELEMENTS_BY_CSS_SELECTOR, cssSelector), Type.util);
        int result = htmlUtils.numberOfHtmlElements(null, null, null, null, null, null, cssSelector);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to press specified item of specified spinner
     *
     * @param spinnerIndex index of spinner
     * @param itemIndex    index of item to press <h6>Example:</h6>
     *                     <p/>
     *                     <pre class="brush: java">
     *                     {@code
     *                     solo.pressSpinnerItem(0, 4);
     *                     }
     *                     </pre>
     */
    @Override
    public void pressSpinnerItem(int spinnerIndex, int itemIndex) {
        otherUtils.addAction(String.format(Messages.PRESS_SPINNER_ITEM, spinnerIndex, itemIndex), Type.click);
        super.pressSpinnerItem(spinnerIndex, itemIndex);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to scroll list to the specified line.
     *
     * @param line specified line <h6>Examples:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.scrollListToLine(0);
     *             <p/>
     *             solo.scrollListToLine(40);
     *             }
     *             </pre>
     * @since 1.0
     */
    public void scrollListToLine(int line) {
        otherUtils.addAction(String.format(Messages.SCROLL_LIST_TO_LINE, line), Type.scroll);
        otherUtils.waitForAnyListView();
        otherUtils.scrollListToLine(getCurrentListViews().get(0), line);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to scroll list to the specified line.
     *
     * @param index index of AbsListView
     * @param line  specified line <h6>Examples:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.scrollListToLine(0, 0);
     *              <p/>
     *              solo.scrollListToLine(0, 40);
     *              }
     *              </pre>
     */
    @Override
    public void scrollListToLine(int index, int line) {
        otherUtils.addAction(String.format(Messages.SCROLL_LIST_WITH_INDEX_TO_LINE, index, line), Type.scroll);
        super.scrollListToLine(index, line);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to scroll to bottom
     */
    @Override
    public void scrollToBottom() {
        otherUtils.addAction(Messages.SCROLL_TO_BOTTOM, Type.scroll);
        super.scrollToBottom();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to scroll to top
     */
    @Override
    public void scrollToTop() {
        otherUtils.addAction(Messages.SCROLL_TO_TOP, Type.scroll);
        super.scrollToTop();
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to check if text exists on the screen
     *
     * @param text text to search <h6>Examples:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             assertTrue(solo.searchText("text"));
     *             <p/>
     *             }
     *             </pre>
     */
    @Override
    public boolean searchText(String text) {
        otherUtils.addAction(String.format(Messages.SEARCH_TEXT, text), Type.util);
        boolean result = super.searchText(text);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to send key
     *
     * @param key key to send
     *            <p/>
     *            <h6>Examples:</h6>
     *            <p/>
     *            <pre class="brush: java">
     *            {@code
     *            solo.sendKey(ExtSolo.DELETE);     *
     *            }
     *            </pre>
     */
    @Override
    public void sendKey(int key) {
        switch (key) {
            case DELETE:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "DELETE"), Type.input);
                break;
            case LEFT:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "LEFT"), Type.input);
                break;
            case RIGHT:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "RIGHT"), Type.input);
                break;
            case UP:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "UP"), Type.input);
                break;
            case DOWN:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "DOWN"), Type.input);
                break;
            case ENTER:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "ENTER"), Type.input);
                break;
            case MENU:
                otherUtils.addAction(String.format(Messages.SEND_KEY_STRING, "MENU"), Type.input);
                break;
            default:
                KeyEvent kEvent = new KeyEvent(0, key);
                char digit = kEvent.getDisplayLabel();
                otherUtils.addAction(String.format(Messages.SEND_KEY_CHAR, digit), Type.input);
                break;
        }
        super.sendKey(key);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to set orientation of Activity
     *
     * @param orientation orientation to set
     *                    <p/>
     *                    <h6>Examples:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.setActivityOrientation(ExtSolo.LANDSCAPE);     *
     *                    }
     *                    </pre>
     */
    @Override
    public void setActivityOrientation(int orientation) {
        otherUtils.addAction(String.format(Messages.SET_ORIENTATION, orientation == 0 ? "landscape" : "portrait"), Type.config);
        super.setActivityOrientation(orientation);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to set date on DatePicker
     *
     * @param datePicker  instance of DatePicker
     * @param year        year to set
     * @param monthOfYear month to set
     * @param dayOfMonth  day to set
     *                    <p/>
     *                    <h6>Examples:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.setDatePicker((DatePicker)solo.findViewById("com.example.id.datePicker1"), 1999, 5, 23);
     *                    }
     *                    </pre>
     */
    @Override
    public void setDatePicker(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        otherUtils.addAction(String.format(Messages.SET_DATE_PICKER, dayOfMonth, monthOfYear, year), Type.input);
        super.setDatePicker(datePicker, year, monthOfYear, dayOfMonth);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to set date on DatePicker
     *
     * @param index       index of DatePicker
     * @param year        year to set
     * @param monthOfYear month to set
     * @param dayOfMonth  day to set
     *                    <p/>
     *                    <h6>Examples:</h6>
     *                    <p/>
     *                    <pre class="brush: java">
     *                    {@code
     *                    solo.setDatePicker(0, 1999, 5, 23);
     *                    }
     *                    </pre>
     * @since 4.4
     */
    public void setDatePicker(int index, int year, int monthOfYear, int dayOfMonth) {
        otherUtils.addAction(String.format(Messages.SET_DATE_PICKER, dayOfMonth, monthOfYear, year), Type.input);
        super.setDatePicker(getView(DatePicker.class, index), year, monthOfYear, dayOfMonth);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to simulate the desired localization.
     *
     * @param latitude  latitude in degrees. Positive number for North and negative
     *                  for South
     * @param longitude longitude in degrees. Positive number for East and negative
     *                  for West
     * @param altitude  altitude in meters <h6>Example:</h6>
     *                  <p/>
     *                  <pre class="brush: java">
     *                  {@code
     *                  solo.setGPSMockLocation(37.42198, -122.085083, 40);
     *                  }
     *                  </pre>
     * @since 2.9
     */
    public void setGPSMockLocation(double latitude, double longitude, double altitude) {
        otherUtils.addAction(String.format(Messages.SET_GPS_MOCK_LOCATION, latitude, longitude, altitude), Type.config);
        otherUtils.setGPSMockLocation(latitude, longitude, altitude);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to set time on TimePicker
     *
     * @param timePicker instance of TimePicker
     * @param hour       hour to set
     * @param minute     minute to set
     *                   <p/>
     *                   <h6>Examples:</h6>
     *                   <p/>
     *                   <pre class="brush: java">
     *                   {@code
     *                   solo.setTimePicker((TimePicker)solo.findViewById("com.example.id.timePicker1"), 4, 23);
     *                   }
     *                   </pre>
     */
    @Override
    public void setTimePicker(android.widget.TimePicker timePicker, int hour, int minute) {
        otherUtils.addAction(String.format(Messages.SET_TIME_PICKER, hour, minute), Type.input);
        super.setTimePicker(timePicker, hour, minute);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to set time on TimePicker
     *
     * @param index  index of TimePicker
     * @param hour   hour to set
     * @param minute minute to set
     *               <p/>
     *               <h6>Examples:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.setTimePicker(0, 4, 23);
     *               }
     *               </pre>
     * @since 4.4
     */
    public void setTimePicker(int index, int hour, int minute) {
        otherUtils.addAction(String.format(Messages.SET_TIME_PICKER, hour, minute), Type.input);
        super.setTimePicker(getView(TimePicker.class, index), hour, minute);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used sleep for specified time
     *
     * @param time time to sleep
     *             <p/>
     *             <h6>Examples:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.sleep(5000); // 5 sec
     *             }
     *             </pre>
     */
    @Override
    public void sleep(int time) {
        otherUtils.addAction(String.format(Messages.SLEEP, time), Type.util);
        super.sleep(time);
        otherUtils.addDurationToAction();
    }

    protected void soloClickOnScreen(float x, float y) {
        super.clickOnScreen(x, y);
    }

    protected void soloClick(View view, boolean longClick, Integer time) {
        if (longClick) {
            if (time != null) {
                super.clickLongOnView(view, time);
            } else {
                super.clickLongOnView(view);
            }
        } else {
            super.clickOnView(view);
        }
    }

    protected void soloLongClick(View view, int time) {
        super.clickLongOnView(view, time);
    }

    protected void soloSleep(int time) {
        super.sleep(time);
    }

    protected boolean soloWaitForText(String text, Integer timeout) {
        if (timeout != null) {
            return super.waitForText(text, 1, timeout);
        } else {
            return super.waitForText(text);
        }
    }

    /**
     * Method used to take screenshot with the specified name. File is stored on
     * SD card of device, if there is permission to do it.
     *
     * @param name name of file <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.takeScreenshot("screenshot_name");
     *             }
     *             </pre>
     * @since 1.0
     */
    public void takeScreenshot(String name) {
        screenshotUtils.takeScreenshot(name);
        otherUtils.addScreenshotToMetadata(String.format("%s.png", name), false);
    }

    // method used for failed screenshots - needed in metadata file
    protected void takeScreenshot(String name, boolean failed) {
        screenshotUtils.takeScreenshot(name);
        otherUtils.addScreenshotToMetadata(String.format("%s.png", name), failed);
    }

    /**
     * Method used to tear down this instance, should be used in tearDown method
     * of test
     *
     * @since 2.5 <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * public void tearDown() throws Exception {
     * solo.finishOpenedActivities();
     * solo.tearDown();
     * super.tearDown();
     * }
     * }
     * </pre>
     */
    public void tearDown() throws Exception {
        otherUtils.restoreLocaleIfWasChanged();
        otherUtils.resetGPSMocking();
        otherUtils.saveMetadataFile();
        screenshotUtils.tearDown();
        otherUtils.tearDown();
    }

    /**
     * Method used to get real x position from normalized value.
     *
     * @param normalizedX normalized x
     * @return real x position <h6>Examples:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.toScreenX(0.744f)
     * <p/>
     * solo.toScreenX(0.104f)
     * }
     * </pre>
     * @since 1.0
     */
    public float toScreenX(float normalizedX) {
        return normalizedX * otherUtils.getScreenWidth();
    }

    /**
     * Method used to get real y position from normalized value.
     *
     * @param normalizedY normalized y
     * @return real y position <h6>Examples:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * solo.toScreenY(0.744f)
     * <p/>
     * solo.toScreenY(0.104f)
     * }
     * </pre>
     * @since 1.0
     */
    public float toScreenY(float normalizedY) {
        return normalizedY * otherUtils.getScreenHeight();
    }

    /**
     * Method used to turn on/off wireless connection.
     *
     * @param enabled enable or disable wireless connection <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.turnWifi(false);
     *                }
     *                </pre>
     * @since 3.2
     */
    public void turnWifi(boolean enabled) {
        otherUtils.addAction(String.format(Messages.TURN_WIRELESS_CONNECTION, enabled ? "on" : "off"), Type.util);
        otherUtils.turnWifi(enabled);
        otherUtils.addDurationToAction();
    }

    /**
     * Method used to wait for Activity
     *
     * @param name name of Activity <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             asertTrue(solo.waitForActivity("MyActivity"));
     *             }
     *             </pre>
     */
    @Override
    public boolean waitForActivity(String name) {
        String[] parts = name.split("\\.");
        otherUtils.addAction(String.format(Messages.CHANGE_ACTIVITY_TO, parts[parts.length - 1]), Type.navigation);
        boolean result = super.waitForActivity(name);
        if (!result) {
            Log.w(TAG, String.format(Messages.WAIT_FOR_ACTIVITY_FAILED, name));
        }
        otherUtils.addDurationToAction();

        htmlUtils.setCurrentActivity(getCurrentActivity());

        return result;
    }

    /**
     * Method used to wait for Activity for specified time
     *
     * @param name    name of Activity
     * @param timeout time to wait<h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                asertTrue(solo.waitForActivity("MyActivity"), 5000);
     *                }
     *                </pre>
     */
    @Override
    public boolean waitForActivity(String name, int timeout) {
        String[] parts = name.split("\\.");
        otherUtils.addAction(String.format(Messages.CHANGE_ACTIVITY_TO, parts[parts.length - 1]), Type.navigation);
        boolean result = super.waitForActivity(name, timeout);
        if (!result) {
            Log.w(TAG, String.format(Messages.WAIT_FOR_ACTIVITY_FAILED, name));
        }
        otherUtils.addDurationToAction();

        htmlUtils.setCurrentActivity(getCurrentActivity());

        return result;
    }

    /**
     * Method used to wait for button
     *
     * @param id      id of Button to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForButtonById("com.example.id.button1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForButtonById(String id, int timeout) {
        return waiter.waitById(Button.class, id, timeout);
    }

    /**
     * Method used to wait for button
     *
     * @param index   index of Button to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForButton(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForButton(int index, int timeout) {
        return waiter.wait(Button.class, index, timeout);
    }

    /**
     * Method used to wait for button
     *
     * @param text    text of Button to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForButton("button", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForButton(String text, int timeout) {
        return waiter.wait(Button.class, text, timeout);
    }

    /**
     * Method used to wait for checkBox
     *
     * @param id      id of CheckBox to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForCheckBoxById("com.example.id.checkBox1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForCheckBoxById(String id, int timeout) {
        return waiter.waitById(CheckBox.class, id, timeout);
    }

    /**
     * Method used to wait for checkBox
     *
     * @param index   index of CheckBox to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForCheckBox(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForCheckBox(int index, int timeout) {
        return waiter.wait(CheckBox.class, index, timeout);
    }

    /**
     * Method used to wait for checkBox
     *
     * @param text    text of CheckBox to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForCheckBox("checkBox", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForCheckBox(String text, int timeout) {
        return waiter.wait(CheckBox.class, text, timeout);
    }

    /**
     * Method used to wait for datePicker
     *
     * @param id      id of DatePicker to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForDatePickerById("com.example.id.datePicker1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForDatePickerById(String id, int timeout) {
        return waiter.waitById(DatePicker.class, id, timeout);
    }

    /**
     * Method used to wait for datePicker
     *
     * @param index   index of DatePicker to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForDatePicker(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForDatePicker(int index, int timeout) {
        return waiter.wait(DatePicker.class, index, timeout);
    }

    /**
     * Method used to wait for editText
     *
     * @param id      id of EditText to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForEditTextById("com.example.id.editText1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForEditTextById(String id, int timeout) {
        return waiter.waitById(EditText.class, id, timeout);
    }

    /**
     * Method used to wait for editText
     *
     * @param index   index of EditText to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForEditText(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForEditText(int index, int timeout) {
        return waiter.wait(EditText.class, index, timeout);
    }

    /**
     * Method used to wait for editText
     *
     * @param text    text of EditText to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForEditText("editText", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForEditText(String text, int timeout) {
        return waiter.wait(EditText.class, text, timeout);
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param xPath XPATH to find element
     * @param time  time for waiting
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(index: 0, name: j_username) failed.",
     * solo.waitForHtmlElement("(//input[@id = 'id'])[0]" 20000)
     * );
     * }
     * </pre>
     * @since 2.5
     */
    public boolean waitForHtmlElement(String xPath, int time) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_HTML_ELEMENT_XPATH, xPath), Type.wait);
        boolean result = htmlUtils.waitForHtmlElement(null, null, null, null, -1, time, xPath, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param tag       tag of element
     * @param id        id of element, null or empty string, if does not exist
     * @param name      name of element, null or empty string, if does not exist
     * @param className class of element, null or empty string, if does not exist
     * @param index     index of element, -1 if unknown
     * @param time      time for waiting
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(index: 0, name: j_username) failed.",
     * solo.waitForHtmlElement("input", "", "j_username", "", 0, 20000, null)
     * );
     * }
     * </pre>
     * @since 2.5
     */
    public boolean waitForHtmlElement(String tag, String id, String name, String className, int index, int time) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_HTML_ELEMENT_TAG, tag, id, name, className), Type.wait);
        boolean result = htmlUtils.waitForHtmlElement(tag, id, name, className, index, time, null, null, null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param tag              tag of element
     * @param id               id of element, null or empty string, if does not exist
     * @param name             name of element, null or empty string, if does not exist
     * @param className        class of element, null or empty string, if does not exist
     * @param index            index of element, -1 if unknown
     * @param time             time for waiting
     * @param customAttributes Map of extra attributes, null allowed
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(index: 0, name: j_username) failed.",
     * solo.waitForHtmlElement("input", "", "j_username", "",
     * 0, 20000, new HashMap<String, String>() {
     * put("custom_id", "my_val");
     * }
     * )
     * );
     * }
     * </pre>
     * @since 2.5
     */
    public boolean waitForHtmlElement(
            String tag, String id, String name, String className, int index, int time,
            Map<String, String> customAttributes) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_HTML_ELEMENT_TAG, tag, id, name, className), Type.wait);
        boolean result = htmlUtils.waitForHtmlElement(tag, id, name, className, index, time, null, customAttributes,
                null);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param jQuerySelector jQuery selector to find element
     * @param time           time for waiting
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(p, class: class1) failed.",
     * solo.waitForHtmlElementBySelector("$('p.class1')", 20000)
     * );
     * }
     * </pre>
     * @since 4.3
     * @deprecated it's better to use {@link #waitForHtmlElementByCssSelector(String, int, int)}
     * as this method convert jQuery selector to css selector since version 4.5
     * what is not always possible
     */
    @Deprecated
    public boolean waitForHtmlElementBySelector(String jQuerySelector, int time) {
        //primitive attempt to convert from jQuery selector to css selector
        String cssSelector = jQuerySelector.replace("$('", "").replace("')", "");

        return waitForHtmlElementByCssSelector(cssSelector, time);
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param cssSelector css selector to find element
     * @param time        time for waiting
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(p, class: class1) failed.",
     * solo.waitForHtmlElementByCssSelector("p#id.class1", 20000)
     * );
     * }
     * </pre>
     * @since 4.5
     */
    public boolean waitForHtmlElementByCssSelector(String cssSelector, int time) {
        return waitForHtmlElementByCssSelector(cssSelector, 0, time);
    }

    /**
     * Method used to wait during loading HTML element inside WebView
     *
     * @param cssSelector css selector to find element
     * @param index       index of matched elements
     * @param time        time for waiting
     * @return false if element does not appeared after specified time, true if
     * element exist or just appeared <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * assertTrue("Wait for html element - input(p, class: class1) failed.",
     * solo.waitForHtmlElementByCssSelector("p#id.class1", 2, 20000)
     * );
     * }
     * </pre>
     * @since 4.5
     */
    public boolean waitForHtmlElementByCssSelector(String cssSelector, int index, int time) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_HTML_ELEMENT_BY_CSS_SELECTOR, cssSelector), Type.wait);
        boolean result = htmlUtils.waitForHtmlElement(null, null, null, null, index, time, null, null, cssSelector);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait for imageView
     *
     * @param id      id of ImageView to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForImageById("com.example.id.imageView1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForImageById(String id, int timeout) {
        return waiter.waitById(ImageView.class, id, timeout);
    }

    /**
     * Method used to wait for imageView
     *
     * @param index   index of ImageView to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForImage(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForImage(int index, int timeout) {
        return waiter.wait(ImageView.class, index, timeout);
    }

    /**
     * Method used to wait for imageButton
     *
     * @param id      id of ImageButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForImageButtonById("com.example.id.imageButton1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForImageButtonById(String id, int timeout) {
        return waiter.waitById(ImageButton.class, id, timeout);
    }

    /**
     * Method used to wait for imageButton
     *
     * @param index   index of ImageButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForImageButton(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForImageButton(int index, int timeout) {
        return waiter.wait(ImageButton.class, index, timeout);
    }

    /**
     * Method used to wait for list
     *
     * @param index   index of list to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                assertTrue(solo.waitForList(1, 1000));
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForList(int index, int timeout) {
        return waiter.wait(AdapterView.class, index, timeout);
    }

    /**
     * Method used to wait for log message
     *
     * @param logMessage message to wait
     * @param timeout    time to wait <h6>Example:</h6>
     *                   <p/>
     *                   <pre class="brush: java">
     *                   {@code
     *                   solo.waitForLogMessage("log message", 10000);
     *                   }
     *                   </pre>
     */
    @Override
    public boolean waitForLogMessage(String logMessage, int timeout) {
        otherUtils.addAction(Messages.WAIT_FOR_LOG_MESSAGE, Type.wait);
        boolean result = super.waitForLogMessage(logMessage, timeout);
        otherUtils.addDurationToAction();

        return result;
    }

    /**
     * Method used to wait for radioButton
     *
     * @param index   index of RadioButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForRadioButton(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForRadioButton(int index, int timeout) {
        return waiter.wait(RadioButton.class, index, timeout);
    }

    /**
     * Method used to wait for radioButton
     *
     * @param id      id of RadioButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForRadioButtonById("com.example.id.radioButton1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForRadioButtonById(String id, int timeout) {
        return waiter.waitById(RadioButton.class, id, timeout);
    }

    /**
     * Method used to wait for radioButton
     *
     * @param text    text of RadioButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForRadioButton("image", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForRadioButton(String text, int timeout) {
        return waiter.wait(RadioButton.class, text, timeout);
    }

    /**
     * Method used to wait for spinner
     *
     * @param index   index of Spinner to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForSpinner(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForSpinner(int index, int timeout) {
        return waiter.wait(Spinner.class, index, timeout);
    }

    /**
     * Method used to wait for spinner
     *
     * @param id      id of Spinner to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForSpinnerById("com.example.id.spinner1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForSpinnerById(String id, int timeout) {
        return waiter.waitById(Spinner.class, id, timeout);
    }

    /**
     * Method used to wait for textView
     *
     * @param index   index of TextView to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForText(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForText(int index, int timeout) {
        return waiter.wait(TextView.class, index, timeout);
    }

    /**
     * Method used to wait for textView
     *
     * @param text text of TextView to wait <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.waitForText("text");
     *             }
     *             </pre>
     */
    @Override
    public boolean waitForText(String text) {
        return waiter.wait(TextView.class, text, null);
    }

    /**
     * Method used to wait for textView
     *
     * @param text    text of TextView to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForText("text", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForText(String text, int timeout) {
        return waiter.wait(TextView.class, text, timeout);
    }

    @Override
    public boolean waitForText(String text, int minimumNumberOfMatches, long timeout) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_TEXT, text), Type.wait);
        boolean result = super.waitForText(text, minimumNumberOfMatches, timeout);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait for textView
     *
     * @param id      id of TextView to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForTextById("com.example.id.textView1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForTextById(String id, int timeout) {
        return waiter.waitById(TextView.class, id, timeout);
    }

    /**
     * Method used to wait for timePicker
     *
     * @param index   index of TimePicker to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForTimePicker(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForTimePicker(int index, int timeout) {
        return waiter.wait(TimePicker.class, index, timeout);
    }

    /**
     * Method used to wait for timePicker
     *
     * @param id      id of TimePicker to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForTimePickerById("com.example.id.timePicker1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForTimePickerById(String id, int timeout) {
        return waiter.waitById(TimePicker.class, id, timeout);
    }

    /**
     * Method used to wait for toggleButton
     *
     * @param index   index of ToggleButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForToggleButton(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForToggleButton(int index, int timeout) {
        return waiter.wait(ToggleButton.class, index, timeout);
    }

    /**
     * Method used to wait for toggleButton
     *
     * @param text    text of ToggleButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForToggleButton("text", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForToggleButton(String text, int timeout) {
        return waiter.wait(ToggleButton.class, text, timeout);
    }

    /**
     * Method used to wait for toggleButton
     *
     * @param id      id of ToggleButton to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForToggleButtonById("com.example.id.toggleButton1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForToggleButtonById(String id, int timeout) {
        return waiter.waitById(ToggleButton.class, id, timeout);
    }

    /**
     * Method used to wait for view
     *
     * @param viewClass              class of View
     * @param minimumNumberOfMatches minimum number of matches
     * @param timeout                time to wait
     * @param scroll                 flag to scroll screen to find views<h6>Example:</h6>
     *                               <p/>
     *                               <pre class="brush: java">
     *                               {@code
     *                               solo.waitForView(ToggleButton.class, 2, 10000, false);
     *                               }
     *                               </pre>
     */
    @Override
    public <T extends View> boolean waitForView(
            Class<T> viewClass, int minimumNumberOfMatches, int timeout, boolean scroll) {
        String parts[] = viewClass.getName().split("\\.");
        otherUtils.addAction(String.format(Messages.WAIT_FOR_VIEW, parts[parts.length - 1]), Type.wait);
        boolean result = super.waitForView(viewClass, minimumNumberOfMatches, timeout, scroll);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait for view
     *
     * @param index   index of View to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForView(1, 1000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForView(int index, int timeout) {
        return waiter.wait(View.class, index, timeout);
    }

    /**
     * Method used to wait for view
     *
     * @param text    text of View to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForView("view", 4000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForView(String text, int timeout) {
        return waiter.wait(View.class, text, timeout);
    }

    /**
     * Method used to wait for view
     *
     * @param id      id of View to wait
     * @param timeout time to wait <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForViewById("com.example.id.view1", 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    public boolean waitForViewById(String id, int timeout) {
        return waiter.waitById(View.class, id, timeout);
    }

    /**
     * Method used to wait for view
     *
     * @param view    instance of View to wait
     * @param timeout time to wait
     * @param scroll  flag to scroll screen to find views <h6>Example:</h6>
     *                <p/>
     *                <pre class="brush: java">
     *                {@code
     *                solo.waitForView((View)solo.findViewById("com.example.id.view1"), 10000);
     *                }
     *                </pre>
     * @since 4.4
     */
    @Override
    public boolean waitForView(View view, int timeout, boolean scroll) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_VIEW, otherUtils.getDescriptionFromView(view)), Type.wait);
        boolean result = super.waitForView(view, timeout, scroll);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Method used to wait for wireless connection connected/disconnected
     *
     * @param state expected state - WIFI_STATE.CONNECTED/WIFI_STATE.DISCONNECTED
     *              <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              assertTrue(solo.waitForWifi(WIFI_STATE.DISCONNECTED, 1000));
     *              }
     *              </pre>
     * @since 4.3
     */
    public boolean waitForWifi(WIFI_STATE state, int time) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_WIRELESS_CONNECTION,
                state.equals(WIFI_STATE.CONNECTED) ? "connected" : "disconnected"), Type.util);
        boolean result = otherUtils.waitForWifi(state, time);
        otherUtils.addDurationToAction();
        return result;
    }

    protected <T extends View> void clickOnTextBySolo(Class<T> clazz, String text) {
        try {
            Field clickerField = Class.forName("com.jayway.android.robotium.solo.Solo").getDeclaredField("clicker");
            clickerField.setAccessible(true);
            Object clicker = clickerField.get(this);
            Method clickOn = clicker.getClass().getDeclaredMethod("clickOn", Class.class, String.class);
            clickOn.setAccessible(true);
            clickOn.invoke(clicker, clazz, text);
        } catch (Exception ignored) {
        }
    }


    /**
     * Method used to send string into application
     *
     * @param text text to send
     *             <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.sendStringSync("text");
     *             }
     *             </pre>
     * @since 4.8
     */
    public void sendStringSync(String text) {
        otherUtils.addAction(String.format(Messages.SEND_STRING, text), Type.input);
        otherUtils.sendStringSync(text);
        otherUtils.addDurationToAction();
    }

    // robotium webview support overridden just for meta-data

    /**
     * Clicks a WebElement matching the specified By object.
     *
     * @param by the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     *           <p/>
     *           <h6>Example:</h6>
     *           <p/>
     *           <pre class="brush: java">
     *           {@code
     *           solo.clickOnWebElement(By.name("name"));
     *           }
     *           </pre>
     */
    public void clickOnWebElement(By by) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_WEB_ELEMENT_S, by.getValue()), Type.click);
        super.clickOnWebElement(by);
        otherUtils.addDurationToAction();
    }

    /**
     * Clicks the specified WebElement.
     *
     * @param webElement the WebElement to click
     *                   <p/>
     *                   <h6>Example:</h6>
     *                   <p/>
     *                   <pre class="brush: java">
     *                   {@code
     *                   solo.clickOnWebElement(webElement);
     *                   }
     *                   </pre>
     */
    public void clickOnWebElement(WebElement webElement) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_WEB_ELEMENT_5S,
                webElement.getTagName(), webElement.getId(),
                webElement.getName(), webElement.getClassName(),
                webElement.getText()), Type.click);
        super.clickOnWebElement(webElement);
        otherUtils.addDurationToAction();
    }

    /**
     * Clicks a WebElement matching the specified By object.
     *
     * @param by    the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @param match if multiple objects match, this determines which one to click
     *              <p/>
     *              <h6>Example:</h6>
     *              <p/>
     *              <pre class="brush: java">
     *              {@code
     *              solo.clickOnWebElement(By.id("my_id"), 0);
     *              }
     *              </pre>
     */
    public void clickOnWebElement(By by, int match) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_WEB_ELEMENT_S_I, by.getValue(), match), Type.click);
        super.clickOnWebElement(by, match);
        otherUtils.addDurationToAction();
    }

    /**
     * Clicks a WebElement matching the specified By object.
     *
     * @param by     the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @param match  if multiple objects match, this determines which one to click
     * @param scroll {@code true} if scrolling should be performed
     *               <p/>
     *               <h6>Example:</h6>
     *               <p/>
     *               <pre class="brush: java">
     *               {@code
     *               solo.clickOnWebElement(By.id("my_id"), 0, true);
     *               }
     *               </pre>
     */
    public void clickOnWebElement(By by, int match, boolean scroll) {
        otherUtils.addAction(String.format(Messages.CLICK_ON_WEB_ELEMENT_S_I, by.getValue(), match), Type.click);
        super.clickOnWebElement(by, match, scroll);
        otherUtils.addDurationToAction();
    }

    /**
     * Waits for a WebElement matching the specified By object. Default timeout is 20 seconds.
     *
     * @param by the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @return {@code true} if the {@link WebElement} is displayed and {@code false} if it is not displayed before the timeout
     * <p/>
     * <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * boolean result = solo.waitForWebElement(By.id("my_id"));
     * }
     * </pre>
     */
    public boolean waitForWebElement(By by) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_WEB_ELEMENT_S, by.getValue()), Type.wait);
        boolean result = super.waitForWebElement(by);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Waits for a WebElement matching the specified By object.
     *
     * @param by      the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @param timeout the the amount of time in milliseconds to wait
     * @param scroll  {@code true} if scrolling should be performed
     * @return {@code true} if the {@link WebElement} is displayed and {@code false} if it is not displayed before the timeout
     * <p/>
     * <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * boolean result = solo.waitForWebElement(By.id("my_id"), 20000, false);
     * }
     * </pre>
     */
    public boolean waitForWebElement(By by, int timeout, boolean scroll) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_WEB_ELEMENT_S, by.getValue()), Type.wait);
        boolean result = super.waitForWebElement(by, timeout, scroll);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Waits for a WebElement matching the specified By object.
     *
     * @param by                     the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @param minimumNumberOfMatches the minimum number of matches that are expected to be found. {@code 0} means any number of matches
     * @param timeout                the the amount of time in milliseconds to wait
     * @param scroll                 {@code true} if scrolling should be performed
     * @return {@code true} if the {@link WebElement} is displayed and {@code false} if it is not displayed before the timeout
     * <p/>
     * <h6>Example:</h6>
     * <p/>
     * <pre class="brush: java">
     * {@code
     * boolean result = solo.waitForWebElement(By.id("my_id"), 3, 20000, true);
     * }
     * </pre>
     */
    public boolean waitForWebElement(By by, int minimumNumberOfMatches, int timeout, boolean scroll) {
        otherUtils.addAction(String.format(Messages.WAIT_FOR_WEB_ELEMENT_S, by.getValue()), Type.wait);
        boolean result = super.waitForWebElement(by, minimumNumberOfMatches, timeout, scroll);
        otherUtils.addDurationToAction();
        return result;
    }

    /**
     * Enters text in a WebElement matching the specified By object.
     *
     * @param by   the By object. Examples are: {@code By.id("id")} and {@code By.name("name")}
     * @param text the text to enter in the {@link WebElement} field
     *             <p/>
     *             <h6>Example:</h6>
     *             <p/>
     *             <pre class="brush: java">
     *             {@code
     *             solo.enterTextInWebElement(By.id("my_id"), "Testdroid");
     *             }
     *             </pre>
     */
    public void enterTextInWebElement(By by, String text) {
        otherUtils.addAction(String.format(Messages.ENTER_TEXT_IN_WEB_ELEMENT, text, by.getValue()), Type.input);
        super.enterTextInWebElement(by, text);
        otherUtils.addDurationToAction();
    }
}
