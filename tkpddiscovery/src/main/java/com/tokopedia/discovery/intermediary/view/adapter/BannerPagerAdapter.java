package com.tokopedia.discovery.intermediary.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
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

    public BannerPagerAdapter(Context context, List<BannerModel> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_slider, container, false);

        ImageView bannerImage = (ImageView) view.findViewById(R.id.image);
        //promoImage.setOnClickListener(onPromoClicked(bannerList.get(position).promoUrl));
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
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}