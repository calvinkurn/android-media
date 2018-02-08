package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public interface ProductScoreRepository {

    Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel);
}
