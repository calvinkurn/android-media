package com.tokopedia.seller.instoped;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.R;
import com.tokopedia.seller.instoped.dialog.InstagramAuthenticationDialog;
import com.tokopedia.seller.instoped.fragment.InstagramMediaFragment;
import com.tokopedia.seller.instoped.model.InstagramUserModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 */
public class InstagramAuth {

    public static final String CLIENT_ID = "50dd2baace254c3dadcb9c876167baef";
    public static final String CLIENT_SECRET = "ac319756c2364c5ea70c53142f800d63";
    public static final String CALLBACK_URL = "https://www.tokopedia.com";

    public static final String KEY_ACCESS_TOKEN = "access_token";

    public static final String TAG = "INSTOPED";

    public interface OnRequestAccessTokenListener {
        void onSuccess(String code, FragmentManager fm);
        void onCancel();
    }

    public interface OnRequestMediaListener{
        void onRequestAccessToken(FragmentManager fm);
    }

    private InstagramAuthenticationDialog authenticationDialog;
//    private InstagramMediaFragment mediaDialog;
    private InstagramUserModel model;

    public InstagramAuth(){
        model = new InstagramUserModel();
        authenticationDialog = new InstagramAuthenticationDialog();
//        authenticationDialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_DialogWhenLarge);
//        mediaDialog = InstagramMediaFragment.createDialog(model);
//        mediaDialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
    }

    @Deprecated
    public void setGetMediaListener(InstagramMediaFragment.OnGetInstagramMediaListener listener){
//        mediaDialog.setOnGetInstagramMediaListener(listener);
    }

    public void getMedias(FragmentManager fm){
        if(model.accessToken == null)
            requestAccessToken(fm);
        else
            requestMedia(fm);
    }

    private void requestAccessToken(FragmentManager fm){
        authenticationDialog.setOnRequestAccessTokenListener(new OnRequestAccessTokenListener() {
            @Override
            public void onSuccess(String code, FragmentManager fm) {
                try {
                    onRequestAccessTokenSuccess(code);
                    requestMedia(fm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {

            }
        });

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.add_product_container, authenticationDialog);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onRequestAccessTokenSuccess(String result) throws JSONException{
        JSONObject resultJSON = new JSONObject(result);
        model.accessToken = resultJSON.getString(KEY_ACCESS_TOKEN);
    }

    private void requestMedia(FragmentManager fm){
//        mediaDialog.setOnRequestMediaListener(new OnRequestMediaListener() {
//            @Override
//            public void onRequestAccessToken(FragmentManager fm) {
//                requestAccessToken(fm);
//            }
//        });
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.add_product_container, InstagramMediaFragment.createDialog(model));
        transaction.commit();
//        mediaDialog.show(fm, TAG);
    }

}
