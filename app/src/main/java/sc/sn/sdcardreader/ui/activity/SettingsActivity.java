package sc.sn.sdcardreader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import sc.sn.android.commons.util.DeviceUtils;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.util.PreferenceUtils;

/**
 * Global preferences.
 *
 * @author S. Grimault
 */
public class SettingsActivity
        extends PreferenceActivity {

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // backward compatibility for Android 2.3.x
        addPreferencesFromResource(R.xml.preferences);

        PreferenceUtils.updatePreferences(getPreferenceScreen());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        configureToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(String name,
                             Context context,
                             AttributeSet attrs) {
        // allow super to try and create a view first
        final View result = super.onCreateView(name,
                                               context,
                                               attrs);

        if (result != null) {
            return result;
        }

        if (!DeviceUtils.isPostLollipop()) {
            // if we're running pre-Lollipop, we need to 'inject' our tint aware Views in place of the standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this,
                                                 attrs);
                case "Spinner":
                    return new AppCompatSpinner(this,
                                                attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this,
                                                 attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this,
                                                    attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this,
                                                        attrs);
            }
        }

        return null;
    }

    private void configureToolbar() {
        final Toolbar toolbar;

        if (DeviceUtils.isPostIceCreamSandwich()) {
            final LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent()
                                                                                    .getParent()
                                                                                    .getParent();
            toolbar = (Toolbar) LayoutInflater.from(this)
                                              .inflate(R.layout.toolbar_settings,
                                                       root,
                                                       false);

            // insert at top
            root.addView(toolbar,
                         0);
        }
        else {
            final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            final ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            toolbar = (Toolbar) LayoutInflater.from(this)
                                              .inflate(R.layout.toolbar_settings,
                                                       root,
                                                       false);

            int height;
            TypedValue tv = new TypedValue();

            if (getTheme().resolveAttribute(R.attr.actionBarSize,
                                            tv,
                                            true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data,
                                                                getResources().getDisplayMetrics());
            }
            else {
                height = toolbar.getHeight();
            }

            content.setPadding(0,
                               height,
                               0,
                               0);

            root.addView(content);
            root.addView(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     finish();
                                                 }
                                             });
    }
}
