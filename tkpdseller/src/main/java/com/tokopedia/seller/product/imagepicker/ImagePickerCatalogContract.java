package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public interface ImagePickerCatalogContract {
    interface View extends BaseListViewListener<CatalogModelView>{

    }

    interface Presenter extends CustomerPresenter<View>{

        void getCatalogImage(String catalogId);
    }
}
