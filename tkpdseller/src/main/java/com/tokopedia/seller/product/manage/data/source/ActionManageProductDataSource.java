package com.tokopedia.seller.product.manage.data.source;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.manage.data.mapper.DeleteProductCloudMapper;
import com.tokopedia.seller.product.manage.data.model.ResponseDeleteProductData;
import com.tokopedia.seller.product.manage.data.model.ResponseEditPriceData;
import com.tokopedia.seller.product.manage.data.mapper.EditPriceCloudMapper;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionManageProductDataSource {
    private ActionManageProductDataSourceCloud actionManageProductDataSourceCloud;
    private Context context;

    public ActionManageProductDataSource(ActionManageProductDataSourceCloud actionManageProductDataSourceCloud, Context context) {
        this.actionManageProductDataSourceCloud = actionManageProductDataSourceCloud;
        this.context = context;
    }

    public Observable<Boolean> editPrice(TKPDMapParam<String, String> params) {
        return actionManageProductDataSourceCloud.editPrice(AuthUtil.generateParamsNetwork(context,params))
                .map(new SimpleDataResponseMapper<ResponseEditPriceData>())
                .map(new EditPriceCloudMapper());
    }

    public Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params) {
        return actionManageProductDataSourceCloud.deleteProduct(AuthUtil.generateParamsNetwork(context, params))
                .map(new SimpleDataResponseMapper<ResponseDeleteProductData>())
                .map(new DeleteProductCloudMapper());
    }
}
