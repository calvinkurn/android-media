package com.tokopedia.discovery.newdiscovery.hotlist.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistPromoInfo;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistQueryModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistBannerMapper implements Func1<Response<TkpdResponse>, HotlistBannerModel> {

    @SuppressWarnings("UnusedParameters")
    public HotlistBannerMapper(Gson gson) {

    }

    @Override
    public HotlistBannerModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                return mappingIntoDomainModel(response.body().convertDataObj(HotListBannerModel.class));
            } else {
                throw new MessageErrorException(response.body().getErrorMessageJoined());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private HotlistBannerModel mappingIntoDomainModel(HotListBannerModel pojoModel) {
        HotlistBannerModel domainModel = new HotlistBannerModel();
        domainModel.setHotlistTitle(pojoModel.info.title);
        domainModel.setBannerImage(pojoModel.info.coverImg);
        domainModel.setBannerDesc(pojoModel.info.hotlistDescription);
        domainModel.setHotlistPromoInfo(pojoModel.promoInfo != null ? mappingBannerPromo(pojoModel.promoInfo) : null);
        domainModel.setHotlistQueryModel(mappingQuery(pojoModel.query));
        domainModel.setDisableTopads(pojoModel.disableTopads == 1);
        return domainModel;
    }

    private HotlistPromoInfo mappingBannerPromo(HotListBannerModel.PromoInfo promoInfo) {
        HotlistPromoInfo hotlistPromoInfo = new HotlistPromoInfo();
        hotlistPromoInfo.setMinimunTransaction(promoInfo.getMinTx());
        hotlistPromoInfo.setTitle(promoInfo.getText());
        hotlistPromoInfo.setApplinkTermCondition(promoInfo.getTcApplink());
        hotlistPromoInfo.setUrlTermCondition(promoInfo.getTcLink());
        hotlistPromoInfo.setPromoPeriod(promoInfo.getPromoPeriod());
        hotlistPromoInfo.setVoucherCode(promoInfo.getVoucherCode());
        return hotlistPromoInfo;
    }

    private HotlistQueryModel mappingQuery(HotListBannerModel.Query pojoQuery) {
        HotlistQueryModel queryModel = new HotlistQueryModel();
        queryModel.setOrderBy(pojoQuery.ob);
        queryModel.setQueryKey(pojoQuery.q);
        queryModel.setHotlistID(pojoQuery.hot_id);
        queryModel.setShopID(pojoQuery.shop_id);
        queryModel.setFilterGoldMerchant(pojoQuery.fshop);
        queryModel.setPriceMax(pojoQuery.pmax);
        queryModel.setPriceMin(pojoQuery.pmin);
        queryModel.setCategoryID(pojoQuery.sc);
        queryModel.setNegativeKeyword(pojoQuery.negative_keyword);
        return queryModel;
    }

}
