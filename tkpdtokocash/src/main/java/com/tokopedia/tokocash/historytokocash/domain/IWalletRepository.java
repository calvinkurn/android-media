package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.historytokocash.data.entity.OAuthInfoEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.ResponseHelpHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface IWalletRepository {

    Observable<TokoCashHistoryData> getTokoCashHistoryData(TKPDMapParam<String, Object> mapParams);

    Observable<List<HelpHistoryTokoCash>> getHelpHistoryData();

    Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId);

    Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory);

    Observable<OAuthInfoEntity> getOAuthInfo();

    Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType);
}
