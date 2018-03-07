package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment;

public class TemplateChatActivity extends BasePresenterActivity implements HasComponent {

    private static final String TAG = "TEMPLATE_CHAT_FRAGMENT";


    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, TemplateChatActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getString(R.string.drawer_title_setting));
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

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
                .add(R.id.container, TemplateChatFragment.createInstance(getIntent().getExtras()),
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

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TEMPLATE_CHAT_SETTING;
    }
}
