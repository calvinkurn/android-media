package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public interface WalletDataSource {

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, Object> mapParams);

    Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData();
}
