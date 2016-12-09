package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.localytics.android.Customer;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.AppEventTracking;

import java.util.HashMap;
import java.util.Map;

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
    public void sendLocalyticsEvent(Bundle bundle, int type) {
        if (type == DownloadService.LOGIN_ACCOUNTS_INFO ||
                type == DownloadService.REGISTER_PASS_PHONE)
            return;
        switch (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0)){
            case DownloadService.REGISTER_GOOGLE:
                sendLocalyticsRegisterEvent(bundle, AppEventTracking.GTMCacheValue.GMAIL);
                break;
            case DownloadService.REGISTER_FACEBOOK:
                sendLocalyticsRegisterEvent(bundle, AppEventTracking.GTMCacheValue.FACEBOOK);
                break;
            case DownloadService.REGISTER_WEBVIEW:
                sendLocalyticsRegisterEvent(bundle, AppEventTracking.GTMCacheValue.WEBVIEW);
                break;
            case DownloadService.LOGIN_GOOGLE:
                sendLocalyticsLoginEvent(bundle, AppEventTracking.GTMCacheValue.GMAIL);
                break;
            case DownloadService.LOGIN_FACEBOOK:
                sendLocalyticsLoginEvent(bundle, AppEventTracking.GTMCacheValue.FACEBOOK);
                break;
            case DownloadService.LOGIN_WEBVIEW:
                sendLocalyticsLoginEvent(bundle, AppEventTracking.GTMCacheValue.WEBVIEW);
                break;
        }
    }

    @Override
    public void sendNotifLocalyticsCallback(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey("ll")){
                Jordan.init(context).getLocalyticsContainer()
                        .sendNotificationCallback(intent);
            }
        }
    }

    private void sendLocalyticsLoginEvent(Bundle bundle, String label){

        Map<String, String> attributesLogin = new HashMap<String, String>();
        Jordan.init(context).getLocalyticsContainer().sendEventLogin(
                new Customer.Builder()
                        .setCustomerId(
                                bundle.getString(AppEventTracking.USER_ID_KEY,
                                        AppEventTracking.NOT_AVAILABLE)
                        )
                        .setFirstName(bundle.getString(AppEventTracking.FULLNAME_KEY,
                                AppEventTracking.NOT_AVAILABLE))
                        .setEmailAddress(
                                bundle.getString(AppEventTracking.EMAIL_KEY,
                                        AppEventTracking.NOT_AVAILABLE)
                        )
                        .build()
                , label,
                attributesLogin
        );
    }

    private void sendLocalyticsRegisterEvent(Bundle bundle, String label){

        Map<String, String> attributesLogin = new HashMap<String, String>();
        Jordan.init(context).getLocalyticsContainer().sendEventRegister(
                new Customer.Builder()
                        .setCustomerId(
                                bundle.getString(AppEventTracking.USER_ID_KEY,
                                        AppEventTracking.NOT_AVAILABLE)
                        )
                        .setFirstName(bundle.getString(AppEventTracking.FULLNAME_KEY,
                                AppEventTracking.NOT_AVAILABLE))
                        .setEmailAddress(
                                bundle.getString(AppEventTracking.EMAIL_KEY,
                                        AppEventTracking.NOT_AVAILABLE)
                        )
                        .build()
                , label,
                attributesLogin
        );
    }
}
