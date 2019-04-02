package com.tokopedia.discovery.intermediary.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 5/23/17.
 */

public class BannerPagerAdapter extends PagerAdapter {

    List<BannerModel> bannerList = new ArrayList<>();
    private final Context context;
    private final String categoryId;
    private OnPromoClickListener listener;

    public interface OnPromoClickListener {
        void onPromoClick(int pos, String categoryName, String name, String applink, String url,String imgUrl);
    }

    public BannerPagerAdapter(Context context,
                              List<BannerModel> bannerList,
                              String categoryId,
                              OnPromoClickListener listener) {
        this.context = context;
        this.bannerList = bannerList;
        this.categoryId = categoryId;
        this.listener = listener;
    }

    public BannerPagerAdapter(Context context,
                              List<BannerModel> bannerList,
                              String categoryId) {
        this.context = context;
        this.bannerList = bannerList;
        this.categoryId = categoryId;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider_intermediary, container, false);

        final ImageView bannerImage = (ImageView) view.findViewById(R.id.image);
        if (bannerList.get(position).getUrl() != null && bannerList.get(position).getUrl().length() > 0) {
            bannerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventBannerClickCategory(categoryId, bannerList.get(position).getUrl());
                    if (listener != null) {
                        BannerModel model = bannerList.get(position);
                        String categoryName =model.getCategoryName().toLowerCase();
                        listener.onPromoClick(
                                model.getPosition(),
                                categoryName,
                                model.getTitle(),
                                model.getApplink(),
                                model.getUrl(),
                                model.getImageUrl()
                        );
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
