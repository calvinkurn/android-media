package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewBadgeDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewLabelDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.RecentView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.BadgeViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewDetailProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewSubscriber extends Subscriber<List<RecentViewProductDomain>> {

    private static final java.lang.String CASHBACK = "Cashback";
    private static final java.lang.String OFFICIAL_STORE = "Official Store";

    private final RecentView.View viewListener;

    public RecentViewSubscriber(RecentView.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetRecentView(ErrorHandler.getErrorMessage(e));

    }

    @Override
    public void onNext(List<RecentViewProductDomain> recentViewProductDomains) {
        if (!recentViewProductDomains.isEmpty()) {
            viewListener.onSuccessGetRecentView(convertToViewModel(recentViewProductDomains));
        } else {
            viewListener.onEmptyGetRecentView()
            ;
        }
    }

    private ArrayList<Visitable> convertToViewModel(List<RecentViewProductDomain> recentViewProductDomains) {
        ArrayList<Visitable> listProduct = new ArrayList<>();
        for (RecentViewProductDomain domain : recentViewProductDomains) {
            listProduct.add(new RecentViewDetailProductViewModel(
                    Integer.parseInt(domain.getId()),
                    domain.getName(),
                    domain.getPrice(),
                    domain.getImgUri(),
                    convertToCashback(domain.getLabels()),
                    domain.getWholesale() != null && domain.getWholesale().equals("1"),
                    domain.getPreorder() != null && domain.getPreorder().equals("1"),
                    domain.getFreeReturn() != null && domain.getFreeReturn().equals("1"),
                    domain.getWishlist(),
                    Integer.parseInt(domain.getRating() != null ? domain.getRating() : "0"),
                    domain.getIsGold() != null && domain.getIsGold().equals("1"),
                    convertToIsOfficial(domain.getBadges()),
                    domain.getShop().getName(),
                    domain.getShop().getLocation()
            ));
        }
        return listProduct;
    }

    private boolean convertToIsOfficial(List<RecentViewBadgeDomain> badges) {
        if (!badges.isEmpty()) {
            for (RecentViewBadgeDomain domain : badges) {
                if (domain.getTitle().contains(OFFICIAL_STORE)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String convertToCashback(List<RecentViewLabelDomain> labels) {
        if (!labels.isEmpty() && labels.get(0).getTitle().startsWith(CASHBACK))
            return labels.get(0).getTitle();
        else return "";
    }
}
