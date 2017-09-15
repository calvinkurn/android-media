package com.tokopedia.seller.goldmerchant.featured.domain.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductGETModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class FeaturedProductMapper implements Func1<FeaturedProductGETModel, FeaturedProductDomainModel> {

    @Inject
    public FeaturedProductMapper() {
    }

    public static FeaturedProductDomainModel convert(FeaturedProductGETModel featuredProductGETModel) {
        FeaturedProductDomainModel featuredProductDomainModel
                = new FeaturedProductDomainModel();
        ArrayList<FeaturedProductDomainModel.Datum> data = new ArrayList<>();
        for (FeaturedProductGETModel.Datum datum : featuredProductGETModel.getData()) {
            FeaturedProductDomainModel.Datum res = new FeaturedProductDomainModel.Datum();
            res.setProductId(datum.getProductId());
            res.setCashback(datum.isCashback());

            res.setCashbackDetail(convert(datum.getCashbackDetail()));
            res.setImageUri(datum.getImageUri());
            res.setName(datum.getName());
            res.setPreorder(datum.isPreorder());
            res.setPrice(datum.getPrice());
            res.setReturnable(datum.isReturnable());
            res.setUri(datum.getUri());
            res.setWholesale(datum.isWholesale());
            res.setBadges(convert(datum.getBadges()));
            res.setLabels(convert2(datum.getLabels()));
            res.setWholesaleDetail(convert3(datum.getWholesaleDetail()));

            data.add(res);
        }

        featuredProductDomainModel.setData(data);
        return featuredProductDomainModel;
    }

    private static List<FeaturedProductDomainModel.WholesaleDetail> convert3(List<FeaturedProductGETModel.WholesaleDetail> wholesaleDetail) {
        List<FeaturedProductDomainModel.WholesaleDetail> details = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(details))
            return details;

        for (int i = 0; i < wholesaleDetail.size(); i++) {
            FeaturedProductGETModel.WholesaleDetail detail = wholesaleDetail.get(i);

            FeaturedProductDomainModel.WholesaleDetail wholesaleDetail1 = new FeaturedProductDomainModel.WholesaleDetail();

            wholesaleDetail1.setPrice(detail.getPrice());
            wholesaleDetail1.setQty(detail.getQty());

            details.add(wholesaleDetail1);
        }

        return details;
    }

    private static List<FeaturedProductDomainModel.Label> convert2(List<FeaturedProductGETModel.Label> labels) {
        List<FeaturedProductDomainModel.Label> details = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(labels))
            return details;

        for (int i = 0; i < labels.size(); i++) {
            FeaturedProductGETModel.Label detail = labels.get(i);

            FeaturedProductDomainModel.Label wholesaleDetail1 = new FeaturedProductDomainModel.Label();

            wholesaleDetail1.setTitle(detail.getTitle());
            wholesaleDetail1.setColor(detail.getColor());

            details.add(wholesaleDetail1);
        }

        return details;
    }

    public static FeaturedProductDomainModel.CashbackDetail convert(FeaturedProductGETModel.CashbackDetail cashbackDetail) {
        FeaturedProductDomainModel.CashbackDetail cashbackDetail1 = new FeaturedProductDomainModel.CashbackDetail();
        cashbackDetail1.setCashbackPercent(cashbackDetail.getCashbackPercent());
        cashbackDetail1.setCashbackStatus(cashbackDetail.getCashbackStatus());
        cashbackDetail1.setCashbackValue(cashbackDetail.getCashbackValue());
        cashbackDetail1.setIsCashbackExpired(cashbackDetail.getIsCashbackExpired());
        return cashbackDetail1;
    }

    public static List<FeaturedProductDomainModel.Badge> convert(List<FeaturedProductGETModel.Badge> data) {
        List<FeaturedProductDomainModel.Badge> badges = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(data))
            return badges;

        for (int i = 0; i < data.size(); i++) {
            FeaturedProductGETModel.Badge badge = data.get(i);

            FeaturedProductDomainModel.Badge badge1 = new FeaturedProductDomainModel.Badge();
            badge1.setImageUrl(badge.getImageUrl());
            badge1.setTitle(badge.getTitle());
            badges.add(badge1);
        }
        return badges;
    }

    @Override
    public FeaturedProductDomainModel call(FeaturedProductGETModel featuredProductGETModel) {
        return convert(featuredProductGETModel);
    }
}
