package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.RideProductDependencyInjection;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.UberProductPresenter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductAdapter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberProductFragment extends BaseFragment implements UberProductContract.View, RideProductItemClickListener {
    OnFragmentInteractionListener mInteractionListener;
    UberProductContract.Presenter mPresenter;

    @BindView(R2.id.ride_product_list)
    RecyclerView mRideProductsRecyclerView;
    @BindView(R2.id.crux_cabs_home_ad)
    LinearLayout mAdsContainerLinearLayout;
    @BindView(R2.id.crux_cabs_ad_title)
    TextView mAdsTitleTextView;
    @BindView(R2.id.crux_cabs_ad_cross)
    ImageView mAdsCrossImageView;
    @BindView(R2.id.empty_product_list)
    LinearLayout mEmptyProductLinearLayout;
    @BindView(R2.id.tv_error_desc)
    TextView mErrorDescriptionTextView;
    @BindView(R2.id.empty_list_retry)
    TextView mRetryButtonTextView;

    RideProductAdapter mAdapter;

    public interface OnFragmentInteractionListener {
        void onProductClicked(RideProductViewModel rideProductViewModel);
    }

    public static UberProductFragment newInstance() {
        return new UberProductFragment();
    }

    public UberProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = RideProductDependencyInjection.createPresenter("X9hkRQ6OjJHrZNcfQKT5dbdZC28zJLjQcM31xTP8");
        mPresenter.attachView(this);
        mPresenter.initialize();
        setViewListener();
    }

    private void setViewListener() {
        RideProductTypeFactory placeAutoCompleteTypeFactory = new RideProductAdapterTypeFactory(this);
        mAdapter = new RideProductAdapter(placeAutoCompleteTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRideProductsRecyclerView.setLayoutManager(layoutManager);
        mRideProductsRecyclerView.setHasFixedSize(true);
        mRideProductsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uber_product;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }


    @Override
    public void onProductSelected(RideProductViewModel rideProductViewModel) {
        mInteractionListener.onProductClicked(rideProductViewModel);
    }


    public void updateProductList(PlacePassViewModel source, PlacePassViewModel destination) {
        mPresenter.actionGetRideProducts(source, destination);
    }

    @Override
    public void renderProductList(List<Visitable> datas) {
        mAdapter.clearData();
        mAdapter.setElement(datas);
    }

    @OnClick(R2.id.crux_cabs_ad_cross)
    public void actionCloseAdsButtonClicked() {
        hideAdsBadges();
    }

    @Override
    public void showAdsBadges(String message) {
        mAdsContainerLinearLayout.setVisibility(View.VISIBLE);
        mAdsTitleTextView.setText(String.valueOf(message));
    }

    @Override
    public void hideAdsBadges() {
        mAdsContainerLinearLayout.setVisibility(View.GONE);
    }
}
