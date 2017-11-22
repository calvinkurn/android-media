package com.tokopedia.events.view.customview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.adapter.EventCategoryAdapter;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashwanityagi on 15/11/17.
 */


public class EventCategoryView extends LinearLayout {

    @BindView(R2.id.txt_category_title)
    TextView categoryTitle;
    @BindView(R2.id.txt_show_all)
    TextView showAll;
    @BindView(R2.id.recyclerview_event)
    RecyclerView recyclerview;

    private Context context;

    public EventCategoryView(Context context) {
        super(context);
        init(context);
    }

    public EventCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.event_category_view, this, true);
        ButterKnife.bind(this);

    }

    public void renderData(List<CategoryItemsViewModel> categoryItems,String title) {
        categoryTitle.setText(title);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(context, categoryItems);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventCategoryAdapter);
    }
}
