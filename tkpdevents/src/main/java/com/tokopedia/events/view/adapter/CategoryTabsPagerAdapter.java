package com.tokopedia.events.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.events.R;
import com.tokopedia.events.view.customview.EventCategoryView;

import java.util.List;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryTabsPagerAdapter extends PagerAdapter {
    List<EventCategoryView> categoryList;
    private Context context;

    public CategoryTabsPagerAdapter(List<EventCategoryView> categoryList,Context context) {

        this.categoryList = categoryList;
        this.context=context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (EventCategoryView) view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.event_category_view_new, container, false);
        EventCategoryView eventCategoryView=categoryList.get(position);
        ((LinearLayout)view).addView(eventCategoryView);
        ((ViewPager) container).addView(view);
        return eventCategoryView;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "sd";
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
         ((ViewPager) container).removeView((EventCategoryView) object);
    }
}
