package com.tokopedia.discovery.intermediary.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 5/23/17.
 */

public class BannerPagerAdapter extends PagerAdapter {

    List<BannerModel> bannerList = new ArrayList<>();
    private final Context context;
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
                    UnifyTracking.eventBannerClickCategory(categoryId,bannerList.get(position).getUrl());
                    switch ((DeepLinkChecker.getDeepLinkType(bannerList.get(position).getUrl()))) {
                        case DeepLinkChecker.BROWSE:
                            DeepLinkChecker.openBrowse(bannerList.get(position).getUrl(), context);
                            break;
                        case DeepLinkChecker.HOT:
                            DeepLinkChecker.openHot(bannerList.get(position).getUrl(), context);
                            break;
                        case DeepLinkChecker.CATALOG:
                            DeepLinkChecker.openCatalog(bannerList.get(position).getUrl(), context);
                            break;
                        default:
                            Intent intent = new Intent(context, BannerWebView.class);
                            intent.putExtra(URL, bannerList.get(position).getUrl());
                            context.startActivity(intent);
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
