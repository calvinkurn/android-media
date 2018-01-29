package com.tokopedia.tokocash.network;

import com.tokopedia.tokocash.network.api.TokoCashApi;
import com.tokopedia.tokocash.network.model.RefreshTokenEntity;
import com.tokopedia.tokocash.network.model.WalletTokenEntity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 10/12/17.
 */

public class WalletTokenRefresh {

    private TokoCashSession tokoCashSession;
    private Retrofit retrofit;

    public WalletTokenRefresh(TokoCashSession tokoCashSession, Retrofit retrofit) {
        this.tokoCashSession = tokoCashSession;
        this.retrofit = retrofit;
    }

    public String refreshToken() throws IOException {

        Call<RefreshTokenEntity> responseCall = retrofit.create(TokoCashApi.class).getTokenWalletSynchronous();

        WalletTokenEntity walletTokenEntity = null;
        try {
            walletTokenEntity = responseCall.clone().execute().body().getData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (walletTokenEntity != null) {
            tokoCashSession.setTokenWallet(walletTokenEntity.getToken());
        }
        return walletTokenEntity.getToken();
    }
}