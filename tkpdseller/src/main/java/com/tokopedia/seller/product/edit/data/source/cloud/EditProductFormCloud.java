package com.tokopedia.seller.product.edit.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.edit.data.source.cloud.api.EditProductFormApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.EditProductFormServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import rx.Observable;
/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormCloud {
    public static final String SHOP_ID = "shop_id";
    public static final String PRODUCT_ID = "product_id";
    private final Context context;
    private final EditProductFormApi api;

    @Inject
    public EditProductFormCloud(@ApplicationContext Context context, EditProductFormApi api) {
        this.context = context;
        this.api = api;
    }

    public Observable<EditProductFormServiceModel> fetchEditProductForm(String productId) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        String shopId = SessionHandler.getShopID(context);
        params.put(SHOP_ID, shopId);
        params.put(PRODUCT_ID, productId);
        return api.fetchEditProductForm(AuthUtil.generateParamsNetwork(context, params)).map(new GetData<EditProductFormServiceModel>());
    }
}
