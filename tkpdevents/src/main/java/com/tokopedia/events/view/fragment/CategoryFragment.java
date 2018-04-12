package com.tokopedia.events.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.adapter.EventCategoryAdapter;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.IFragmentLifecycleCallback;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ashwanityagi on 21/11/17.
 */

public class CategoryFragment extends TkpdBaseV4Fragment implements IFragmentLifecycleCallback {

    @BindView(R2.id.txt_category_title)
    TextView categoryTitle;
    @BindView(R2.id.txt_show_all)
    TextView showAll;
    @BindView(R2.id.recyclerview_event)
    RecyclerView recyclerview;
    LinearLayoutManager linearLayoutManager;
    EventCategoryAdapter eventCategoryAdapter;

    private static final String ARG_PARAM_EXTRA_EVENTS_DATA = "ARG_PARAM_EXTRA_EVENTS_DATA";
    private static final String ARG_FRAGMENTPOSITION = "ARG_FRAG_POS";

    CategoryViewModel categoryViewModel;
    int mFragmentPos;
    ICategoryFragmentCallbacks fragmentCallbacks;


    public static Fragment newInstance(CategoryViewModel categoryViewModel, int fragmentPosition) {
        Log.d("FRAGMENTINSTANCE", "created new instance");
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_EVENTS_DATA, categoryViewModel);
        args.putInt(ARG_FRAGMENTPOSITION, fragmentPosition);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("FRAGMENTINSTANCE", "onCreate");
        super.onCreate(savedInstanceState);
        this.categoryViewModel = getArguments().getParcelable(ARG_PARAM_EXTRA_EVENTS_DATA);
        this.mFragmentPos = getArguments().getInt(ARG_FRAGMENTPOSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FRAGMENTINSTANCE", "onCreateView");
        View view = inflater.inflate(R.layout.event_category_view, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        eventCategoryAdapter = new EventCategoryAdapter(getActivity(), categoryViewModel.getItems());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(eventCategoryAdapter);

        return view;
    }


    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void fragmentResume() {
        int lastIndex = linearLayoutManager.findLastVisibleItemPosition();
        for (int i = 0; i <= lastIndex; i++) {
            if (!categoryViewModel.getItems().get(i).isTrack())
                UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_IMPRESSION, categoryViewModel.getItems().get(i).getTitle()
                        + " - " + i);
            categoryViewModel.getItems().get(i).setTrack(true);
        }
        eventCategoryAdapter.enableTracking();
    }

    @Override
    public void fragmentPause() {
        eventCategoryAdapter.disableTracking();
    }

    public interface ICategoryFragmentCallbacks {
        int getCurrentPagerPosition();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("FRAGMENTINSTANCE", "onAttach");
        super.onAttach(activity);
        fragmentCallbacks = (EventsHomeActivity) activity;
    }
}
