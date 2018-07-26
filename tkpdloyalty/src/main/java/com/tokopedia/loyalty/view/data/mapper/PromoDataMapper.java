package com.tokopedia.loyalty.view.data.mapper;

import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoDetailInfoHolderData;
import com.tokopedia.loyalty.view.data.PromoDetailTncHolderData;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDataMapper {

    @Inject
    public PromoDataMapper() {

    }

    public List<Object> convert(PromoData promoData) {
        List<Object> promoDetailData = new ArrayList<>();

        promoDetailData.add(convertToPromoDetailInfoHolderData(promoData));
        if (promoData.getPromoCodeList().isEmpty()) {
            SingleCodeViewModel viewModel = new SingleCodeViewModel();
            viewModel.setPromoName(promoData.getTitle());
            viewModel.setSingleCode(promoData.getPromoCode());
            promoDetailData.add(viewModel);
        } else {
            promoDetailData.addAll(promoData.getPromoCodeList());
        }
        promoDetailData.add(convertToPromoDetailTncHolderData(promoData));

        return promoDetailData;
    }

    private PromoDetailInfoHolderData convertToPromoDetailInfoHolderData(PromoData promoData) {
        PromoDetailInfoHolderData holderData = new PromoDetailInfoHolderData();

        holderData.setThumbnailImageUrl(promoData.getThumbnailImage());
        holderData.setTitle(promoData.getTitle());
        holderData.setPromoPeriod(promoData.getPeriodFormatted());
        holderData.setMinTransaction(promoData.getMinTransaction());

        holderData.setPromoData(promoData);

        return holderData;
    }

    private PromoDetailTncHolderData convertToPromoDetailTncHolderData(PromoData promoData) {
        PromoDetailTncHolderData holderData = new PromoDetailTncHolderData();
        holderData.setTermAndConditions(promoData.getTermsAndConditions());
        return holderData;
    }

}