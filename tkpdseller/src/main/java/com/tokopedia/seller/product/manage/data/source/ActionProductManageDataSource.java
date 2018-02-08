package com.tokopedia.seller.product.manage.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.manage.data.mapper.DeleteProductCloudMapper;
import com.tokopedia.seller.product.manage.data.model.ResponseDeleteProductData;
import com.tokopedia.seller.product.manage.data.model.ResponseEditPriceData;
import com.tokopedia.seller.product.manage.data.mapper.EditPriceCloudMapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionProductManageDataSource {
    private ActionProductManageDataSourceCloud actionProductManageDataSourceCloud;
    private Context context;

    @Inject
    public ActionProductManageDataSource(ActionProductManageDataSourceCloud actionProductManageDataSourceCloud, @ApplicationContext Context context) {
        this.actionProductManageDataSourceCloud = actionProductManageDataSourceCloud;
        this.context = context;
    }

    public Observable<Boolean> editPrice(TKPDMapParam<String, String> params) {
        return actionProductManageDataSourceCloud.editPrice(AuthUtil.generateParamsNetwork(context,params))
                .map(new SimpleDataResponseMapper<ResponseEditPriceData>())
                .map(new EditPriceCloudMapper());
    }

    public Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params) {
        return actionProductManageDataSourceCloud.deleteProduct(AuthUtil.generateParamsNetwork(context, params))
                .map(new SimpleDataResponseMapper<ResponseDeleteProductData>())
                .map(new DeleteProductCloudMapper());
    }
}
