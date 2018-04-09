package com.tokopedia.events.view.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.adapter.LocationDateListAdapter;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.viewmodel.LocationDateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 23/02/18.
 */
public class LocationDateBottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R2.id.rv_locationdate)
    RecyclerView rvLocationDate;
    @BindView(R2.id.iv_close_bts)
    View closeBts;
    EventBookTicketPresenter mPresenter;
    List<LocationDateModel> mData;
    LocationDateListAdapter mAdapter;


    public LocationDateBottomSheetFragment() {
    }

    public void setData(List<LocationDateModel> dataset) {
        mData = dataset;
        if (mAdapter != null)
            mAdapter.setDataSet(mData);
    }

    public void setPresenter(EventBookTicketPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.locationdate_bottomsheet_layout, container, false);
        ButterKnife.bind(this, contentView);
        mAdapter = new LocationDateListAdapter(mData, getActivity(), mPresenter);
        rvLocationDate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvLocationDate.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.recycler_view_divider));
        rvLocationDate.addItemDecoration(decoration);
        return contentView;
    }

    @OnClick(R2.id.iv_close_bts)
    void closeBottomSheet() {
        dismiss();
    }
}
