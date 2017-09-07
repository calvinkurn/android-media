package com.tokopedia.seller.common.repository;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.common.datasource.SellerLoginDataSource;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class SellerLoginRepositoryImpl implements SellerLoginRepository {

    private SellerLoginDataSource sellerLoginDataSource;
    private Context context;

    @Inject
    public SellerLoginRepositoryImpl(SellerLoginDataSource sellerLoginDataSource, Context context) {
        this.sellerLoginDataSource = sellerLoginDataSource;
        this.context = context;
    }


    @Override
    public Observable<Boolean> saveLoginTime() {
        String userId = SessionHandler.getLoginID(context);
        return sellerLoginDataSource.saveLoginTime(userId);
    }
}
