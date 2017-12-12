package com.tokopedia.inbox.inboxmessageold.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.inboxmessageold.fragment.SendMessageFragment;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessageActivityOld extends BasePresenterActivity {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PEOPLE_SEND_MESSAGE;
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
        return com.tokopedia.core.R.layout.activity_send_message;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentByTag(SendMessageFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (fragment == null) {
            fragment = SendMessageFragment.createInstance(getIntent().getExtras());
        }

        fragmentTransaction.replace(com.tokopedia.core.R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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

    public static Intent getAskSellerIntent(Context context, String toShopId,
                                            String shopName, String source) {
        Intent intent = new Intent(context, SendMessageActivityOld.class);
        Bundle bundle = new Bundle();
        bundle.putString(SendMessageFragment.PARAM_SHOP_ID, toShopId);
        bundle.putString(SendMessageFragment.PARAM_OWNER_FULLNAME, shopName);
        bundle.putString(SendMessageFragment.PARAM_SOURCE, source);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                            String customSubject, String customMessage, String source) {
        Intent intent = getAskSellerIntent(context, toShopId, shopName, customSubject, source);
        Bundle bundle = intent.getExtras();
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_MESSAGE, customMessage);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                            String customSubject, String source) {
        Intent intent = getAskSellerIntent(context, toShopId, shopName, source);
        Bundle bundle = intent.getExtras();
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_SUBJECT, customSubject);
        intent.putExtras(bundle);

        return intent;
    }

    public static Intent getAskUserIntent(Context context, String userId,
                                          String userName, String source) {
        Intent intent = new Intent(context, SendMessageActivityOld.class);
        Bundle bundle = new Bundle();
        bundle.putString(SendMessageFragment.PARAM_USER_ID, userId);
        bundle.putString(SendMessageFragment.PARAM_OWNER_FULLNAME, userName);
        bundle.putString(SendMessageFragment.PARAM_SOURCE, source);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAskBuyerIntent(Context context, String toUserId, String
            customerName, String customSubject, String customMessage, String source) {
        Intent intent = getAskUserIntent(context, toUserId, customerName, source);
        Bundle bundle = intent.getExtras();
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_SUBJECT, customSubject);
        bundle.putString(SendMessageFragment.PARAM_CUSTOM_MESSAGE, customMessage);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
