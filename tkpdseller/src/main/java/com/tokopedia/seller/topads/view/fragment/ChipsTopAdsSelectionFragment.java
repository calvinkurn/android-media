package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.listener.ChipsTopAdsSelectionListener;
import com.tokopedia.seller.topads.view.adapter.OnRemoveListener;
import com.tokopedia.seller.topads.view.adapter.ChipsAdapter;
import com.tokopedia.seller.topads.view.models.ChipsEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ChipsTopAdsSelectionFragment extends BasePresenterFragment
    implements ChipsTopAdsSelectionListener
{
    public static final String TAG = "ChipsTopAdsSelectionFragment";
    private static final String EXTRA_DATAS = "SAVED_DATAS";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private OnRemoveListener onRemoveListener = new OnRemoveListener() {
        @Override
        public void onItemRemoved(int position) {
            datas.remove(position);
            Log.i("activity", "delete at " + position);
            adapter.notifyItemRemoved(position);
        }
    };

    private ArrayList<ChipsEntity<ProductDomain>> datas;

    public static Fragment newInstance() {
        return new ChipsTopAdsSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAdapter(savedInstanceState);
    }

    private void setupView(View contentView, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview_chips_top_ads_selection);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(getActivity())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER)
                .build();

        recyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));

        recyclerView.setLayoutManager(spanLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 50);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 50);
        recyclerView.setAdapter(adapter);
    }


    @SuppressWarnings("unchecked")
    private RecyclerView.Adapter createAdapter(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            datas = new ArrayList<>();
        }else{
            datas = savedInstanceState.getParcelableArrayList(EXTRA_DATAS);
        }
        adapter = new ChipsAdapter(datas, onRemoveListener);
        return adapter;
    }

    @Override
    public void onChecked(int position, ProductDomain data) {
        ChipsEntity.Builder<ProductDomain> builder = convertToRecyclerViewModel(data);
        datas.add(builder.build());
        adapter.notifyItemInserted(datas.size()-1);
    }

    public ChipsEntity.Builder<ProductDomain> convertToRecyclerViewModel(ProductDomain data) {
        return (ChipsEntity.Builder<ProductDomain>) ChipsEntity.newBuilder()
                .name(data.getName())
                .description(data.getGroupName())
                .rawData(data);
    }

    @Override
    public void onUnChecked(int position, ProductDomain data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_DATAS, new ArrayList<>(datas));
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.chips_top_ads_selection_fragment;
    }

    @Override
    protected void initView(View view) {
        setupView(view, null);
    }

    //unused methods
    @Override
    protected void onFirstTimeLaunched() {}

    @Override
    public void onSaveState(Bundle state) {}

    @Override
    public void onRestoreState(Bundle savedState) {}

    @Override
    protected boolean getOptionsMenuEnable() { return false; }

    @Override protected void initialPresenter() {}

    @Override protected void initialListener(Activity activity) {}

    @Override protected void setupArguments(Bundle arguments) {}

    @Override protected void setViewListener() {}

    @Override protected void initialVar() {}

    @Override protected void setActionVar() {}
    //unused methods


}
