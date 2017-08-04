package com.tokopedia.session.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static com.tokopedia.core.util.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.tokopedia.core.util.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
import static com.tokopedia.core.util.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY;
import static com.tokopedia.core.util.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;

/**
 * Created by stevenfredian on 7/25/17.
 */

@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
public class TokopediaAuthenticator extends AbstractAccountAuthenticator {

    private String TAG = "UdinicAuthenticator";
    private final Context mContext;

    public TokopediaAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }
}
