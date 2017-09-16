package com.tokopedia.gm.featured.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;

import java.util.List;

/**
 * Created by normansyahputa on 9/7/17.
 */

public interface GMFeaturedProductView extends CustomerView {

    void onSearchLoaded(@NonNull List<GMFeaturedProductModel> list, int totalItem);

    void onLoadSearchError(Throwable t);

    void onPostSuccess();
}
