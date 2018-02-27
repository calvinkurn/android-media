package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by alvarisi on 2/27/18.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        HomeHeaderTokoCashAndTokoPointState.TOKOCASH_ERROR,
        HomeHeaderTokoCashAndTokoPointState.TOKOCASH_SUCCESS,
        HomeHeaderTokoCashAndTokoPointState.TOKOPOINT_ERROR,
        HomeHeaderTokoCashAndTokoPointState.TOKOPOINT_SUCCESS
})
public @interface HomeHeaderTokoCashAndTokoPointState {
    int TOKOCASH_ERROR = 11;
    int TOKOCASH_SUCCESS = 12;
    int TOKOPOINT_ERROR = 21;
    int TOKOPOINT_SUCCESS = 22;
}
