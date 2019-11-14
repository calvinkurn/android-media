package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.BannerModel;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 5/23/17.
 */

public class BannerPagerAdapter extends PagerAdapter {

    List<BannerModel> bannerList = new ArrayList<>();
    final Context context;
    private final String categoryId;
    private static final String URL = "url";

    public BannerPagerAdapter(Context context, List<BannerModel> bannerList, String categoryId) {
        this.context = context;
        this.bannerList = bannerList;
        this.categoryId = categoryId;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider_intermediary, container, false);

        final ImageView bannerImage = (ImageView) view.findViewById(R.id.image);
        if (bannerList.get(position).getUrl()!=null && bannerList.get(position).getUrl().length()>0) {
            bannerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventBannerClickCategory(categoryId,bannerList.get(position).getUrl());
                    boolean goToNative = true;
                    switch ((DeepLinkChecker.getDeepLinkType(context, bannerList.get(position).getUrl()))) {
                        case DeepLinkChecker.BROWSE:
                            goToNative = DeepLinkChecker.openBrowse(bannerList.get(position).getUrl(), context);
                            break;
                        case DeepLinkChecker.HOT:
                            goToNative = DeepLinkChecker.openHot(bannerList.get(position).getUrl(), context);
                            break;
                        case DeepLinkChecker.CATALOG:
                            goToNative = DeepLinkChecker.openCatalog(bannerList.get(position).getUrl(), context);
                            break;
                    }
                    if (!goToNative) {
                        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, bannerList.get(position).getUrl());
                    }

                }
            });
        }
        ImageHandler.LoadImage(
                bannerImage,
                bannerList.get(position).getImageUrl()
        );
        container.addView(view);
        return view;
    }

    private void eventBannerClickCategory(String parentCat, String bannerName){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.BANNER_CLICK,
                bannerName);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null && object instanceof View) container.removeView((View) object);
    }


}
