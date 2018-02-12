package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.BranchSdkUtils;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public class SessionImpl implements Session {
    Context context;
    SessionView sessionView;

    int finishTo;
    int whichFragmentKey;

    public SessionImpl(Context context){
        this.context = context;
        sessionView = (SessionView)context;
    }

    @Override
    public void initDataInstance() {

    }

    @Override
    public int setWhichFragmentKeyInvalid(Intent intent) {
        throw new RuntimeException("setWhichFragmentKeyInvalid don't use this");
    }

    @Override
    public void finishTo() {
         switch (finishTo){
            case SessionView.MOVE_TO_CART_TYPE:
            case SessionView.HOME:
            case SessionView.SELLER_HOME:
                sessionView.moveTo(finishTo);
                break;
            default:
                Log.e(TAG, messageTAG + " move to unknown place !!!");
                break;
        }
    }

    @Override
    public void fetchExtras(Intent intent) {
        if(intent!=null){
            // set which activity should be moved after login process done
            int extra = intent.getExtras().getInt(SessionView.MOVE_TO_CART_KEY, SessionView.INVALID_MOVE_TYPE);
            if(extra !=SessionView.INVALID_MOVE_TYPE){
                finishTo = extra;
            }
            // set which fragment should be created
            int fragmentToShow = intent.getExtras().getInt(WHICH_FRAGMENT_KEY, WHICH_FRAGMENT_KEY_INVALID);
            if(fragmentToShow!=WHICH_FRAGMENT_KEY_INVALID){
                whichFragmentKey = fragmentToShow;
            }
        }
    }

    @Override
    public void saveDataBeforeRotate(Bundle outstate) {
        outstate.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        outstate.putInt(SessionView.MOVE_TO_CART_KEY, finishTo);
    }

    @Override
    public void fetchDataAfterRotate(Bundle instate) {
        if(instate!=null) {
            whichFragmentKey = instate.getInt(WHICH_FRAGMENT_KEY);
            finishTo = instate.getInt(SessionView.MOVE_TO_CART_KEY);
        }
        Log.d(TAG,messageTAG+"onCreate whichFragment : "+whichFragmentKey+" move to : "+finishTo);
    }

    @Override
    public boolean isAfterRotate() {
        return whichFragmentKey!=0;
    }

    @Override
    public int getWhichFragment() {
        return whichFragmentKey;
    }

    @Override
    public void setWhichFragment(int whichFragmentKey) {
        this.whichFragmentKey = whichFragmentKey;
    }@Override
    public void sendGTMEvent(Bundle bundle, int type) {
        if (type == DownloadService.LOGIN_ACCOUNTS_INFO ||
                type == DownloadService.REGISTER_PASS_PHONE)
            return;

        switch (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0)){
            case DownloadService.REGISTER_GOOGLE:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.REGISTER_SUCCESS,
                                AppEventTracking.Category.REGISTER,
                                AppEventTracking.Action.REGISTER_SUCCESS,
                                AppEventTracking.GTMCacheValue.EMAIL
                        );
                break;
            case DownloadService.REGISTER_FACEBOOK:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.REGISTER_SUCCESS,
                                AppEventTracking.Category.REGISTER,
                                AppEventTracking.Action.REGISTER_SUCCESS,
                                AppEventTracking.GTMCacheValue.FACEBOOK
                        );
                break;
            case DownloadService.REGISTER_WEBVIEW:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.REGISTER_SUCCESS,
                                AppEventTracking.Category.REGISTER,
                                AppEventTracking.Action.REGISTER_SUCCESS,
                                AppEventTracking.GTMCacheValue.WEBVIEW
                        );
                break;
            case DownloadService.LOGIN_GOOGLE:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.LOGIN,
                                AppEventTracking.Category.LOGIN,
                                AppEventTracking.Action.LOGIN,
                                AppEventTracking.GTMCacheValue.GMAIL
                        );
                break;
            case DownloadService.LOGIN_FACEBOOK:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.LOGIN,
                                AppEventTracking.Category.LOGIN,
                                AppEventTracking.Action.LOGIN,
                                AppEventTracking.GTMCacheValue.FACEBOOK
                        );
                break;
            case DownloadService.LOGIN_WEBVIEW:
                Nishikino.init(context).startAnalytics()
                        .sendButtonClick(
                                AppEventTracking.Event.LOGIN,
                                AppEventTracking.Category.LOGIN,
                                AppEventTracking.Action.LOGIN,
                                AppEventTracking.GTMCacheValue.WEBVIEW
                        );
                break;
        }
    }

    @Override
    public void sendAnalyticsEvent(Bundle bundle, int type) {
        CommonUtils.dumper("MoEngage called login events type "+bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0));
        if (type == DownloadService.LOGIN_ACCOUNTS_INFO ||
                type == DownloadService.REGISTER_PASS_PHONE)
            return;
        switch (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0)){
            case DownloadService.REGISTER_GOOGLE:
                sendMoEngageRegisterEvent(bundle, AppEventTracking.GTMCacheValue.GMAIL);
                sendBranchRegisterEvent(bundle);
                break;
            case DownloadService.REGISTER_FACEBOOK:
                sendMoEngageRegisterEvent(bundle, AppEventTracking.GTMCacheValue.FACEBOOK);
                sendBranchRegisterEvent(bundle);
                break;
            case DownloadService.REGISTER_WEBVIEW:
                sendMoEngageRegisterEvent(bundle, AppEventTracking.GTMCacheValue.WEBVIEW);
                sendBranchRegisterEvent(bundle);
                break;
            case DownloadService.LOGIN_GOOGLE:
                sendMoEngageLoginEvent(bundle, AppEventTracking.GTMCacheValue.GMAIL);
                sendBranchLoginEvent(bundle);
                break;
            case DownloadService.LOGIN_FACEBOOK:
                sendMoEngageLoginEvent(bundle, AppEventTracking.GTMCacheValue.FACEBOOK);
                sendBranchLoginEvent(bundle);
                break;
            case DownloadService.LOGIN_WEBVIEW:
                sendMoEngageLoginEvent(bundle, AppEventTracking.GTMCacheValue.WEBVIEW);
                sendBranchLoginEvent(bundle);
                break;
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
                sendMoEngageLoginEvent(bundle, AppEventTracking.GTMCacheValue.EMAIL);
                sendBranchLoginEvent(bundle);
                break;
        }
    }

    private void sendMoEngageLoginEvent(Bundle bundle, String label){
        TrackingUtils.setMoEUserAttributes(bundle,label);
    }

    private void sendMoEngageRegisterEvent(Bundle bundle, String label){

        TrackingUtils.setMoEngageUser(new CustomerWrapper.Builder()
                .setCustomerId(
                        bundle.getString(com.tokopedia.core.analytics.AppEventTracking.USER_ID_KEY,
                                com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                )
                .setFirstName(
                        bundle.getString(com.tokopedia.core.analytics.AppEventTracking.FULLNAME_KEY,
                                com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                )
                .setEmailAddress(
                        bundle.getString(com.tokopedia.core.analytics.AppEventTracking.EMAIL_KEY,
                                com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                )
                .build());
    }

    private void sendBranchRegisterEvent(Bundle bundle){
        AccountsParameter accountsParameter = bundle.getParcelable(AppEventTracking.ACCOUNTS_KEY);

        BranchSdkUtils.sendRegisterEvent( bundle.getString(com.tokopedia.core.analytics.AppEventTracking.EMAIL_KEY,
                com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL),
                accountsParameter.getInfoModel() != null ? accountsParameter.getInfoModel().getPhone() : "");
    }

    private void sendBranchLoginEvent(Bundle bundle){
        AccountsParameter accountsParameter = bundle.getParcelable(AppEventTracking.ACCOUNTS_KEY);

        BranchSdkUtils.sendLoginEvent( bundle.getString(com.tokopedia.core.analytics.AppEventTracking.EMAIL_KEY,
                com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL), accountsParameter.getInfoModel() != null ? accountsParameter.getInfoModel().getPhone() : "");
    }
}
