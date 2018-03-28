package com.tokopedia.loyalty.view.data.mapper;

import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
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
            promoDetailData.add(promoData.getPromoCode());
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

        return holderData;
    }

    private PromoDetailTncHolderData convertToPromoDetailTncHolderData(PromoData promoData) {
        PromoDetailTncHolderData holderData = new PromoDetailTncHolderData();
        holderData.setTermAndConditions(promoData.getTermsAndConditions());
        return holderData;
    }

    private List<PromoCodeViewModel> getPromoCodeList() {
        List<SingleCodeViewModel> singleCodeViewModelList = new ArrayList<>();
        singleCodeViewModelList.add(getSingleCodeViewModel("PROMOABC"));
        singleCodeViewModelList.add(getSingleCodeViewModel("PROMODEF"));
        singleCodeViewModelList.add(getSingleCodeViewModel("PROMOGHI"));

        List<PromoCodeViewModel> promoCodeViewModelList = new ArrayList<>();
        promoCodeViewModelList.add(getPromoCodeViewModel("Promo Untuk Pelanggan BCA", singleCodeViewModelList));
        promoCodeViewModelList.add(getPromoCodeViewModel("Promo Untuk Pelanggan BRI", singleCodeViewModelList));
        promoCodeViewModelList.add(getPromoCodeViewModel("Promo Untuk Pelanggan Mandiri", singleCodeViewModelList));

        return promoCodeViewModelList;
    }

    private SingleCodeViewModel getSingleCodeViewModel(String singleCode) {
        SingleCodeViewModel singleCodeViewModel = new SingleCodeViewModel();
        singleCodeViewModel.setSingleCode(singleCode);
        return singleCodeViewModel;
    }

    private PromoCodeViewModel getPromoCodeViewModel(String title, List<SingleCodeViewModel> singleCodeViewModelList) {
        PromoCodeViewModel promoCodeViewModel1 = new PromoCodeViewModel();
        promoCodeViewModel1.setGroupCodeTitle(title);
        promoCodeViewModel1.setGroupCodeDescription("Kode 1 untuk LG G6 Plus & Kode 2 untuk LG Q6 Plus");
        promoCodeViewModel1.setGroupCode(singleCodeViewModelList);

        return promoCodeViewModel1;
    }
}