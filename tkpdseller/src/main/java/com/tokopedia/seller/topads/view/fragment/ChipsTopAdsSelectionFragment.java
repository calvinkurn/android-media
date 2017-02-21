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
import com.tokopedia.seller.topads.view.adapter.IItemsFactory;
import com.tokopedia.seller.topads.view.adapter.OnRemoveListener;
import com.tokopedia.seller.topads.view.adapter.ShortChipsFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ChipsTopAdsSelectionFragment extends BasePresenterFragment {
    public static final String TAG = "ChipsTopAdsSelectionFragment";
    private static final String EXTRA = "data";
    RecyclerView rvTest;
    Spinner spinnerPosition;
    Spinner spinnerMoveTo;
    private RecyclerView.Adapter adapter;
    private List<String> positions;
    private List items;
    private OnRemoveListener onRemoveListener = new OnRemoveListener() {
        @Override
        public void onItemRemoved(int position) {
            items.remove(position);
            Log.i("activity", "delete at " + position);
            adapter.notifyItemRemoved(position);
            updateSpinners();
        }
    };

    /**
     * replace here different data sets
     */
    private IItemsFactory itemsFactory = new ShortChipsFactory();

    public static Fragment newInstance() {
        return new ChipsTopAdsSelectionFragment();
    }

    private void setupView(View contentView, @Nullable Bundle savedInstanceState) {
        rvTest = (RecyclerView) contentView.findViewById(R.id.rvTest);
        spinnerPosition = (Spinner) contentView.findViewById(R.id.spinnerPosition);
        spinnerMoveTo = (Spinner) contentView.findViewById(R.id.spinnerMoveTo);
        adapter = createAdapter(savedInstanceState);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(getActivity())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER)
                .build();

        rvTest.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));

        positions = new LinkedList<>();
        for (int i = 0; i < items.size(); i++) {
            positions.add(String.valueOf(i));
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, positions);
        ArrayAdapter<String> spinnerAdapterMoveTo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, positions);
        spinnerPosition.setAdapter(spinnerAdapter);
        spinnerMoveTo.setAdapter(spinnerAdapterMoveTo);

        rvTest.setLayoutManager(spanLayoutManager);
        rvTest.getRecycledViewPool().setMaxRecycledViews(0, 10);
        rvTest.getRecycledViewPool().setMaxRecycledViews(1, 10);
        rvTest.setAdapter(adapter);
    }


    @SuppressWarnings("unchecked")
    private RecyclerView.Adapter createAdapter(Bundle savedInstanceState) {

        List<String> items;
        if (savedInstanceState == null) {
//            items = itemsFactory.getFewItems();
//            items = itemsFactory.getALotOfItems();
            items = itemsFactory.getItems();
        } else {
            items = savedInstanceState.getStringArrayList(EXTRA);
        }

        adapter = itemsFactory.createAdapter(items, onRemoveListener);
        this.items = items;

        return adapter;

    }

    private void updateSpinners() {
        positions = new LinkedList<>();
        for (int i = 0; i < items.size(); i++) {
            positions.add(String.valueOf(i));
        }

        int selectedPosition = Math.min(spinnerPosition.getSelectedItemPosition(), positions.size() - 1);
        int selectedMoveToPosition = Math.min(spinnerMoveTo.getSelectedItemPosition(), positions.size() - 1);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, positions);
        spinnerPosition.setAdapter(spinnerAdapter);
        selectedPosition = Math.min(spinnerAdapter.getCount() - 1, selectedPosition);
        spinnerPosition.setSelection(selectedPosition);

        ArrayAdapter<String> spinnerAdapterMoveTo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, positions);
        spinnerMoveTo.setAdapter(spinnerAdapterMoveTo);
        spinnerMoveTo.setSelection(selectedMoveToPosition);
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
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.chips_top_ads_selection_fragment;
    }

    @Override
    protected void initView(View view) {
        setupView(view, null);
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
}
