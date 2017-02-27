package com.tokopedia.seller.topads.view.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdDetailProductMapper {

    public static TopAdsDetailShopViewModel convert(TopAdsDetailShopDomainModel domainModel) {
        TopAdsDetailShopViewModel viewModel = new TopAdsDetailShopViewModel();
        viewModel.setId(Long.parseLong(domainModel.getAdId()));
        viewModel.setType(Integer.parseInt(domainModel.getAdType()));
        viewModel.setGroupId(Long.parseLong(domainModel.getGroupId()));
        viewModel.setShopId(Long.parseLong(domainModel.getShopId()));
        viewModel.setItemId(Long.parseLong(domainModel.getItemId()));
        viewModel.setStatus(Integer.parseInt(domainModel.getStatus()));
        viewModel.setPriceBid(domainModel.getPriceBid());
        if (!TextUtils.isEmpty(domainModel.getAdBudget())) {
            viewModel.setBudget(Float.parseFloat(domainModel.getAdBudget()));
        }
        viewModel.setPriceDaily(domainModel.getPriceDaily());
        viewModel.setStickerId(Integer.valueOf(domainModel.getStickerId()));
        viewModel.setSchedule(domainModel.getAdSchedule());
        viewModel.setStartDate(domainModel.getAdStartDate());
        viewModel.setStartTime(domainModel.getAdStartTime());
        viewModel.setEndDate(domainModel.getAdEndDate());
        viewModel.setEndTime(domainModel.getAdEndTime());
        viewModel.setImage(domainModel.getAdImage());
        viewModel.setTitle(domainModel.getAdTitle());
        return viewModel;
    }
}
