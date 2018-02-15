package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.OAuthInfoEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.ResponseHelpHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public interface WalletDataSource {

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, Object> mapParams);

    Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData();

    Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId);

    Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory);

    Observable<OAuthInfoEntity> getOAuthInfo();

    Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType);
}
