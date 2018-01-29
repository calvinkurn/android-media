package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.UberProductPresenter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductAdapter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingPassData;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberProductFragment extends BaseFragment implements UberProductContract.View, RideProductItemClickListener {

    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";

    @BindView(R2.id.ride_product_list)
    RecyclerView rideProductsRecyclerView;
    @BindView(R2.id.crux_cabs_home_ad)
    LinearLayout adsContainerLinearLayout;
    @BindView(R2.id.crux_cabs_ad_title)
    TextView adsTitleTextView;
    @BindView(R2.id.crux_cabs_ad_cross)
    ImageView mAdsCrossImageView;
    @BindView(R2.id.empty_product_list)
    View errorLayout;
    @BindView(R2.id.tv_error_desc)
    TextView errorDescriptionTextView;
    @BindView(R2.id.empty_list_retry)
    TextView retryButtonTextView;
    @BindView(R2.id.product_list_progress)
    ProgressBar mProgressBar;
    @BindView(R2.id.layout_error_view)
    View errorView;
    @BindView(R2.id.layout_progress_view)
    View progressView;
    @BindView(R2.id.tv_sort_fare)
    TextView fareHeaderView;

    @Inject
    UberProductPresenter presenter;

    boolean isCompleteLocations;
    private PlacePassViewModel source, destination;
    private RideProductAdapter adapter;
    private OnFragmentInteractionListener interactionListener;

    public interface OnFragmentInteractionListener {
        void onProductClicked(ConfirmBookingPassData confirmBookingPassData);

        void onMinimumTimeEstCalculated(String timeEst);

        void showEnterDestError();

        void showEnterSourceLocationActiity();

        void actionProductListHeaderClick();

        void actionAdsShowed();

        void actionAdsHidden();

        void renderNearbyRides(NearbyRides nearbyRides);

        void renderNearbyCabs(List<ProductEstimate> productEstimates);
    }

    public static UberProductFragment newInstance() {
        return new UberProductFragment();
    }

    public static UberProductFragment newInstance(PlacePassViewModel source, PlacePassViewModel destination) {
        UberProductFragment fragment = new UberProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SOURCE, source);
        bundle.putParcelable(EXTRA_DESTINATION, destination);
        fragment.setArguments(bundle);
        return fragment;
    }

    public UberProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
        setViewListener();
        if (getArguments() != null) {
            source = getArguments().getParcelable(EXTRA_SOURCE);
            destination = getArguments().getParcelable(EXTRA_DESTINATION);
            updateProductList(source, destination);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_SOURCE, source);
        outState.putParcelable(EXTRA_DESTINATION, destination);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            source = savedInstanceState.getParcelable(EXTRA_SOURCE);
            destination = savedInstanceState.getParcelable(EXTRA_DESTINATION);
        }
    }

    private void setViewListener() {
        RideProductTypeFactory placeAutoCompleteTypeFactory = new RideProductAdapterTypeFactory(this);
        adapter = new RideProductAdapter(placeAutoCompleteTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rideProductsRecyclerView.setLayoutManager(layoutManager);
        rideProductsRecyclerView.setHasFixedSize(true);
        rideProductsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uber_product;
    }

    @Override
    public void showMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }


    @Override
    public void onProductSelected(RideProductViewModel rideProductViewModel) {
        if (isCompleteLocations) {
            //if fare is not find and product is not estimated, do not handle click event
            if (!rideProductViewModel.isEnabled()) {
                return;
            }

            ConfirmBookingPassData confirmBookingPassData = new ConfirmBookingPassData();
            confirmBookingPassData.setSeatCount(1);
            confirmBookingPassData.setSource(source);
            confirmBookingPassData.setDestination(destination);
            confirmBookingPassData.setProductDisplayName(rideProductViewModel.getProductName());
            confirmBookingPassData.setProductId(rideProductViewModel.getProductId());
            confirmBookingPassData.setPriceFmt(rideProductViewModel.getProductPriceFmt());

            confirmBookingPassData.setProductImage(rideProductViewModel.getProductImage());
            confirmBookingPassData.setHeaderTitle(
                    String.format(
                            getString(R.string.confirm_booking_header_title_format),
                            rideProductViewModel.getProductName(),
                            rideProductViewModel.getTimeEstimate())
            );
            confirmBookingPassData.setMaxCapacity(rideProductViewModel.getCapacity());
            confirmBookingPassData.setCancellationFee(rideProductViewModel.getCancellationFee());
            interactionListener.onProductClicked(confirmBookingPassData);
        } else {
            //show message to enter destianation
            interactionListener.showEnterDestError();
            NetworkErrorHelper.showSnackbar(getActivity(), "Please enter destination");
        }
    }


    public void updateProductList(PlacePassViewModel source, PlacePassViewModel destination) {
        this.destination = destination;
        this.source = source;
        isCompleteLocations = source != null && destination != null;
        if (source != null && destination != null)
            presenter.actionGetRideProducts(source, destination);
        else
            presenter.actionGetRideProducts(source);
    }

    @Override
    public void renderProductList(List<Visitable> datas) {
        adapter.clearData();
        adapter.setElement(datas);

    }

    @Override
    public List<Visitable> getProductList() {
        if (adapter == null) {
            return null;
        }

        return adapter.getElements();
    }

    @OnClick(R2.id.crux_cabs_ad_cross)
    public void actionCloseAdsButtonClicked() {
        hideAdsBadges();
    }

    public void actionRetry() {
        if (source != null) {
            hideErrorMessageLayout();
            showProgress();
            presenter.actionGetRideProducts(source, destination);
        } else {
            //open a dialog to enter source location
            interactionListener.showEnterSourceLocationActiity();
        }
    }

    @Override
    public void showAdsBadges(String message) {
        interactionListener.actionAdsShowed();
        adsContainerLinearLayout.setVisibility(View.VISIBLE);
        adsTitleTextView.setText(String.valueOf(message));
    }

    @Override
    public void hideAdsBadges() {
        interactionListener.actionAdsHidden();
        adsContainerLinearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void hideProductList() {
        if (rideProductsRecyclerView != null) {
            rideProductsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProductList() {
        if (rideProductsRecyclerView != null) {
            rideProductsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage(String message, String btnText) {
        hideProgress();
        errorView.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        errorDescriptionTextView.setText(message);
        if (btnText != null && !btnText.isEmpty()) {
            retryButtonTextView.setVisibility(View.VISIBLE);
            retryButtonTextView.setText(btnText);
        } else {
            retryButtonTextView.setVisibility(View.INVISIBLE);
        }

        retryButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRetry();
            }
        });
    }

    @Override
    public void hideErrorMessage() {
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideErrorMessageLayout() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderFareProduct(Visitable productEstimate,
                                  String productId,
                                  int position,
                                  FareEstimate fareEstimate) {
        adapter.setChangedItem(position, productEstimate);
    }

    @Override
    public void actionMinimumTimeEstResult(String timeEst) {
        interactionListener.onMinimumTimeEstCalculated(timeEst);
    }

    @Override
    public void showFareListHeader() {
        fareHeaderView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFareListHeader() {
        fareHeaderView.setVisibility(View.GONE);
    }

    @Override
    public void displayNearbyCabs(List<ProductEstimate> productEstimates) {
        if (interactionListener != null)
            interactionListener.renderNearbyCabs(productEstimates);
    }

    @Override
    public PlacePassViewModel getSource() {
        return this.source;
    }

    @Override
    public void renderNearbyRides(NearbyRides nearbyRides) {
        interactionListener.renderNearbyRides(nearbyRides);
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        BookingRideComponent bookingRideComponent = DaggerBookingRideComponent
                .builder()
                .rideComponent(component)
                .build();
        bookingRideComponent.inject(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @OnClick(R2.id.layout_cab_booking_header)
    public void actionProductListHeaderClicked() {
        interactionListener.actionProductListHeaderClick();
    }
}
