package com.tokopedia.tokocash.network;

import com.google.gson.GsonBuilder;
import com.tokopedia.tokocash.network.api.TokoCashApi;
import com.tokopedia.tokocash.network.model.RefreshTokenEntity;
import com.tokopedia.tokocash.di.OkHttpTokoCashQualifier;

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

        Call<String> responseCall = retrofit.create(TokoCashApi.class).getTokenWalletSynchronous();

        String tokenResponse = null;
        try {
            tokenResponse = responseCall.clone().execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        RefreshTokenEntity tokenEntity = null;
        if (tokenResponse != null) {
            tokenEntity = new GsonBuilder().create().fromJson(tokenResponse, RefreshTokenEntity.class);
            tokoCashSession.setTokenWallet(tokenEntity.getData().getToken());
        }
        return tokenEntity.getData().getToken();
    }
}