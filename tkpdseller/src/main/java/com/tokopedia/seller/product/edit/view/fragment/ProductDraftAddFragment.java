package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.di.component.DaggerProductDraftComponent;
import com.tokopedia.seller.product.edit.di.module.ProductDraftModule;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftAddFragment extends BaseProductDraftAddEditFragment<ProductDraftPresenter> {

    public static Fragment createInstance(String productDraftId) {
        ProductDraftAddFragment fragment = new ProductDraftAddFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.ADD;
    }

    @Override
    public void initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent.class))
                .productDraftModule(new ProductDraftModule())
                .build()
                .inject(this);
    }

}
