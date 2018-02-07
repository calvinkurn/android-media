package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.fragment.EditTemplateChatFragment;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE;

public class EditTemplateChatActivity extends BasePresenterActivity implements HasComponent {

    private static final String TAG = "EDIT_TEMPLATE_CHAT_FRAGMENT";

    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, EditTemplateChatActivity.class);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        String message = extras.getString(PARAM_MESSAGE);
        setToolbarTitle(message == null);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, EditTemplateChatFragment.createInstance(getIntent().getExtras()),
                        TAG)
                .commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }


    private void setToolbarTitle(boolean isAdd) {
        if(isAdd) {
            toolbar.setTitle(getString(R.string.add_template_chat_title));
        }else {
            toolbar.setTitle(getString(R.string.edit_template_chat_title));
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TEMPLATE_CHAT_SET;
    }
}
