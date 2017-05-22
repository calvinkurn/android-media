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
    private ArrayList<PromoViewModel> listPromo;

    public PromoCardViewModel(ArrayList<PromoViewModel> listPromo) {
        this.listPromo = listPromo;
    }

    public PromoCardViewModel(String avatarUrl, String promoterName, String promoterDesc,
                              ArrayList<PromoViewModel> listPromo) {
        this.avatarUrl = avatarUrl;
        this.promoterName = promoterName;
        this.promoterDesc = promoterDesc;
        this.listPromo = listPromo;
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

    public ArrayList<PromoViewModel> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<PromoViewModel> listPromo) {
        this.listPromo = listPromo;
    }
}
