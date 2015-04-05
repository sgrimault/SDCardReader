package sc.sn.sdcardreader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.internal.widget.TintCheckBox;
import android.support.v7.internal.widget.TintCheckedTextView;
import android.support.v7.internal.widget.TintEditText;
import android.support.v7.internal.widget.TintRadioButton;
import android.support.v7.internal.widget.TintSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.Date;

import sc.sn.sdcardreader.BuildConfig;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.util.DeviceUtils;

/**
 * Global preferences.
 *
 * @author S. Grimault
 */
public class SettingsActivity
        extends PreferenceActivity {

    private static final String KEY_PREFERENCE_ABOUT_APP_VERSION = "app_version";

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // backward compatibility for Android 2.3.x
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceScreen().findPreference(KEY_PREFERENCE_ABOUT_APP_VERSION)
                             .setSummary(
                                     getString(
                                             R.string.app_version,
                                             BuildConfig.VERSION_NAME,
                                             BuildConfig.VERSION_CODE,
                                             DateFormat.getDateTimeInstance()
                                                       .format(
                                                               new Date(Long.valueOf(BuildConfig.BUILD_DATE))
                                                       )
                                     )
                             );
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
        final View result = super.onCreateView(
                name,
                context,
                attrs
        );

        if (result != null) {
            return result;
        }

        if (!DeviceUtils.isPostLollipop()) {
            // if we're running pre-Lollipop, we need to 'inject' our tint aware Views in place of the standard framework versions
            switch (name) {
                case "EditText":
                    return new TintEditText(
                            this,
                            attrs
                    );
                case "Spinner":
                    return new TintSpinner(
                            this,
                            attrs
                    );
                case "CheckBox":
                    return new TintCheckBox(
                            this,
                            attrs
                    );
                case "RadioButton":
                    return new TintRadioButton(
                            this,
                            attrs
                    );
                case "CheckedTextView":
                    return new TintCheckedTextView(
                            this,
                            attrs
                    );
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
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(
                    R.layout.toolbar_settings,
                    root,
                    false
            );

            // insert at top
            root.addView(
                    toolbar,
                    0
            );
        }
        else {
            final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            final ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            toolbar = (Toolbar) LayoutInflater.from(this).inflate(
                    R.layout.toolbar_settings,
                    root,
                    false
            );

            int height;
            TypedValue tv = new TypedValue();

            if (getTheme().resolveAttribute(
                    R.attr.actionBarSize,
                    tv,
                    true
            )) {
                height = TypedValue.complexToDimensionPixelSize(
                        tv.data,
                        getResources().getDisplayMetrics()
                );
            }
            else {
                height = toolbar.getHeight();
            }

            content.setPadding(
                    0,
                    height,
                    0,
                    0
            );

            root.addView(content);
            root.addView(toolbar);
        }

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}
