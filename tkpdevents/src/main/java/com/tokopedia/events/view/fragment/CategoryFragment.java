package com.tokopedia.events.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.adapter.EventCategoryAdapter;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryFragment extends TkpdBaseV4Fragment {

    @BindView(R2.id.txt_category_title)
    TextView categoryTitle;
    @BindView(R2.id.txt_show_all)
    TextView showAll;
    @BindView(R2.id.recyclerview_event)
    RecyclerView recyclerview;

    public static String ARG_PARAM_EXTRA_EVENTS_DATA = "ARG_PARAM_EXTRA_EVENTS_DATA";
    CategoryViewModel categoryViewModel;


    public static Fragment newInstance(CategoryViewModel categoryViewModel) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_EVENTS_DATA, categoryViewModel);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoryViewModel = getArguments().getParcelable(ARG_PARAM_EXTRA_EVENTS_DATA);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_category_view, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(getActivity(), categoryViewModel.getItems());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventCategoryAdapter);

        return view;
    }



    @Override
    protected String getScreenName() {
        return "";
    }
}
