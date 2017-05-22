package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromoCardViewModel implements Visitable<FeedPlusTypeFactory>{

    private String avatarUrl;
    private String promoterName;
    private String promoterDesc;
    private ArrayList<PromoViewModel> listProduct;

    public PromoCardViewModel(ArrayList<PromoViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public PromoCardViewModel(String avatarUrl, String promoterName, String promoterDesc, ArrayList<PromoViewModel> listProduct) {
        this.avatarUrl = avatarUrl;
        this.promoterName = promoterName;
        this.promoterDesc = promoterDesc;
        this.listProduct = listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPromoterName() {
        return promoterName;
    }

    public void setPromoterName(String promoterName) {
        this.promoterName = promoterName;
    }

    public String getPromoterDesc() {
        return promoterDesc;
    }

    public void setPromoterDesc(String promoterDesc) {
        this.promoterDesc = promoterDesc;
    }

    public ArrayList<PromoViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<PromoViewModel> listProduct) {
        this.listProduct = listProduct;
    }
}
