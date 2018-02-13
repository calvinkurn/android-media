package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BannerViewModel implements Visitable<HomeTypeFactory> {

    private List<BannerSlidesModel> slides;

    public List<BannerSlidesModel> getSlides() {
        return slides;
    }

    public void setSlides(List<BannerSlidesModel> slides) {
        this.slides = slides;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
