package com.tokopedia.loyalty.view.data.mapper;

import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoDetailInfoHolderData;
import com.tokopedia.loyalty.view.data.PromoDetailTncHolderData;

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
        promoDetailData.addAll(promoData.getPromoCodeList());
        promoDetailData.add(convertToPromoDetailTncHolderData(promoData));

        return promoDetailData;
    }

    private PromoDetailInfoHolderData convertToPromoDetailInfoHolderData(PromoData promoData) {
        PromoDetailInfoHolderData holderData = new PromoDetailInfoHolderData();

        holderData.setThumbnailImageUrl(promoData.getThumbnailImage());
        holderData.setTitle(promoData.getTitle());
        holderData.setPromoPeriod(promoData.getPeriodFormatted());
        holderData.setMinTransaction(promoData.getMinTransaction());

        return holderData;
    }

    private PromoDetailTncHolderData convertToPromoDetailTncHolderData(PromoData promoData) {
        PromoDetailTncHolderData holderData = new PromoDetailTncHolderData();
        holderData.setTermAndConditions(promoData.getTermsAndConditions());
        return holderData;
    }

    private List<PromoCodeViewModel> getPromoCodeList() {
        List<PromoCodeViewModel> promoCodeViewModelList = new ArrayList<>();

        PromoCodeViewModel promoCodeViewModel1 = new PromoCodeViewModel();
        promoCodeViewModel1.setGroupCodeTitle("CREDIT CARD BANK BRI");
        promoCodeViewModel1.setGroupCodeDescription("Kode 1 untuk LG G6 Plus & Kode 2 untuk LG Q6 Plus");

        promoCodeViewModelList.add(promoCodeViewModel1);

        PromoCodeViewModel promoCodeViewModel2 = new PromoCodeViewModel();
        promoCodeViewModel2.setGroupCodeTitle("CREDIT CARD BANK MANDIRI");
        promoCodeViewModel2.setGroupCodeDescription("Kode 1 untuk LG G6 Plus & Kode 2 untuk LG Q6 Plus");

        promoCodeViewModelList.add(promoCodeViewModel2);

        return promoCodeViewModelList;
    }
}