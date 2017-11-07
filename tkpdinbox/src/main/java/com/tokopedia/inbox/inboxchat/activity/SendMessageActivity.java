package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.fragment.SendChatFragment;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessageActivity extends TActivity {

    public static final String PARAM_OWNER_FULLNAME = "owner_fullname";
    public static final String PARAM_CUSTOM_SUBJECT = "custom_subject";
    public static final String PARAM_CUSTOM_MESSAGE = "custom_message";
    public static final String PARAM_SHOP_ID = "to_shop_id";
    public static final String PARAM_USER_ID = "to_user_id";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_ROLE = "role";
    public static final String ROLE_USER = "Pengguna";
    public static final String ROLE_SELLER = "Penjual";
    public static final String IS_HAS_ATTACH_BUTTON = "has_attachment";
    public static final String PARAM_AVATAR = "avatar";


    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PEOPLE_SEND_MESSAGE;
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.header_chat, null);
        toolbar.addView(mCustomView);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setContentInsetEndWithActions(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_send_message);
        initView();
    }

    private void initView() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (SendChatFragment.class.getSimpleName
                        ());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment == null) {
            fragment = SendChatFragment.createInstance(getIntent().getExtras());
        }

        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static Intent getAskSellerIntent(Context context, String toShopId,
                                            String shopName, String source, String avatar) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SHOP_ID, toShopId);
        bundle.putString(PARAM_OWNER_FULLNAME, shopName);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putString(PARAM_ROLE, ROLE_SELLER);
        bundle.putBoolean(IS_HAS_ATTACH_BUTTON, true);
        bundle.putString(PARAM_AVATAR, avatar);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                            String customSubject, String customMessage, String
                                                    source, String avatar) {
        Intent intent = getAskSellerIntent(context, toShopId, shopName, source,
                avatar);
        Bundle bundle = intent.getExtras();
        bundle.putString(PARAM_CUSTOM_SUBJECT, customSubject);
        bundle.putString(PARAM_CUSTOM_MESSAGE, customMessage);
        bundle.putBoolean(IS_HAS_ATTACH_BUTTON, false);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskUserIntent(Context context, String userId,
                                          String userName, String source,
                                          String avatar) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        bundle.putString(PARAM_OWNER_FULLNAME, userName);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putString(PARAM_ROLE, ROLE_USER);
        bundle.putBoolean(IS_HAS_ATTACH_BUTTON, true);
        bundle.putString(PARAM_AVATAR, avatar);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskBuyerIntent(Context context, String toUserId, String
            customerName, String customSubject, String customMessage, String source,
                                           String avatar) {
        Intent intent = getAskUserIntent(context, toUserId, customerName, source, avatar);
        Bundle bundle = intent.getExtras();
        bundle.putString(PARAM_CUSTOM_SUBJECT, customSubject);
        bundle.putString(PARAM_CUSTOM_MESSAGE, customMessage);
        bundle.putBoolean(IS_HAS_ATTACH_BUTTON, false);
        intent.putExtras(bundle);
        return intent;
    }


}
