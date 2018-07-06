package com.tokopedia.session.inactivephonenumber;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;

/**
 * @author by nisie on 7/6/18.
 */
public class ChangeInactivePhoneNumberWebView extends SimpleWebViewWithFilePickerActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, ChangeInactivePhoneNumberWebView.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getIntentWithTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, ChangeInactivePhoneNumberWebView.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }
}
