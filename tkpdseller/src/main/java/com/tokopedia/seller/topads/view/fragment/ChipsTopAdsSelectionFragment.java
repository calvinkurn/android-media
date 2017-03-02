package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.adapter.ChipsAdapter;
import com.tokopedia.seller.topads.view.adapter.OnRemoveListener;
import com.tokopedia.seller.topads.view.listener.AddProductListInterface;
import com.tokopedia.seller.topads.view.listener.ChipsTopAdsSelectionListener;
import com.tokopedia.seller.topads.view.model.ChipsEntity;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ChipsTopAdsSelectionFragment extends BasePresenterFragment
        implements ChipsTopAdsSelectionListener {
    public static final String TAG = "ChipsTopAdsSelectionFragment";
    private static final String EXTRA_DATAS = "SAVED_DATAS";

    private RecyclerView recyclerView;
    private ChipsAdapter adapter;
    private ArrayList<ChipsEntity<TopAdsProductViewModel>> datas;
    private AddProductListInterface addProductListInterface;
    private OnRemoveListener onRemoveListener = new OnRemoveListener() {
        @Override
        public void onItemRemoved(int position) {
            ChipsEntity<TopAdsProductViewModel> removedModel = datas.remove(position);
            Log.i("activity", "delete at " + position);
            adapter.notifyItemRemoved(position);

            if (!isActivityInterfaceEmpty()) {
                TopAdsProductViewModel rawData = removedModel.getRawData();
                addProductListInterface.removeSelection(rawData);
                addProductListInterface.notifyUnchecked(rawData);
            }

            boolean mapEmpty = isMapEmpty();
            Log.d("MNORMANSYAH", "map empty " + mapEmpty);
            if (!isActivityInterfaceEmpty() && mapEmpty) {
                addProductListInterface.disableNextButton();
                addProductListInterface.hideBottomBecauseEmpty();
            }
        }
    };

    public static Fragment newInstance() {
        return new ChipsTopAdsSelectionFragment();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity != null && activity instanceof AddProductListInterface) {
            addProductListInterface = (AddProductListInterface) activity;
        }
    }

    private boolean isActivityInterfaceEmpty() {
        return addProductListInterface == null;
    }

    private boolean isMapEmpty() {
        return !isActivityInterfaceEmpty() && addProductListInterface.sizeSelection() == 0;
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

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(spanLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 50);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 50);
        recyclerView.setAdapter(adapter);
    }


    @SuppressWarnings("unchecked")
    private RecyclerView.Adapter createAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            datas = new ArrayList<>();

            for (TopAdsProductViewModel topAdsProductViewModel : addProductListInterface.selections()) {
                datas.add(convertToRecyclerViewModel(topAdsProductViewModel).build());
            }
        } else {
            datas = savedInstanceState.getParcelableArrayList(EXTRA_DATAS);
        }
        adapter = new ChipsAdapter(datas, onRemoveListener);
        return adapter;
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {
        ChipsEntity.Builder<TopAdsProductViewModel> builder = convertToRecyclerViewModel(datas.size(), data);
        datas.add(builder.build());
        int localLocation = datas.size() - 1;
        Log.d("MNORMANSYAH", "position " + position + " localLocation " + localLocation);
        adapter.notifyItemInserted(localLocation);
    }

    public ChipsEntity.Builder<TopAdsProductViewModel> convertToRecyclerViewModel(int position, TopAdsProductViewModel data) {
        return (ChipsEntity.Builder<TopAdsProductViewModel>) ChipsEntity.newBuilder()
                .name(data.getName())
                .description(data.getGroupName())
                .rawData(data)
                .position(position);
    }

    public ChipsEntity.Builder<TopAdsProductViewModel> convertToRecyclerViewModel(TopAdsProductViewModel data) {
        return convertToRecyclerViewModel(0, data);
    }

    private List<TopAdsProductViewModel> convertTo() {
        List<TopAdsProductViewModel> chipsEntities =
                new ArrayList<>();
        for (ChipsEntity<TopAdsProductViewModel> data : datas) {
            ChipsEntity<TopAdsProductViewModel> data1 = (ChipsEntity<TopAdsProductViewModel>) data;
            chipsEntities.add(data1.getRawData());
        }
        return chipsEntities;
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        List<TopAdsProductViewModel> chipsEntities = convertTo();
        int localLocation = chipsEntities.indexOf(data);
        adapter.remove(localLocation);

        if (!isActivityInterfaceEmpty() && isMapEmpty()) {
            addProductListInterface.disableNextButton();
            addProductListInterface.hideBottomBecauseEmpty();
        }
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        return addProductListInterface.isSelected(data);
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
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    public void onRestoreState(Bundle savedState) {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void notifyUnchecked(TopAdsProductViewModel position) {
        throw new RuntimeException("this is not for this class");
    }

    @Override
    public boolean isExistingGroup() {
        if(addProductListInterface != null){
            return addProductListInterface.isHideEtalase();
        }
        return false;
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }
    //unused methods


}
