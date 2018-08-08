package com.tokopedia.tkpdpdp.estimasiongkir;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.constant.RatesEstimationConstant;
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.RatesEstimationDetailPresenter;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter.RatesEstimationServiceAdapter;

import javax.inject.Inject;

public class RatesEstimationDetailFragment extends BaseDaggerFragment implements RatesEstimationDetailView{
    private static final int VIEW_CONTENT = 1;
    private static final int VIEW_LOADING = 2;

    @Inject RatesEstimationDetailPresenter presenter;
    @Inject UserSession userSession;

    private String productId;
    private String productWeightFmt;

    private TextView shippingDestination;
    private View loadingView;
    private AppBarLayout appBarLayout;
    TextView shippingWeight;
    private TextView shippingReceiverName;
    private TextView shippingReceiverAddress;
    RecyclerView recyclerView;
    private RatesEstimationServiceAdapter adapter = new RatesEstimationServiceAdapter();

    public static RatesEstimationDetailFragment createInstance(String productId, String productWeightFmt){
        Bundle bundle = new Bundle();
        bundle.putString(RatesEstimationConstant.PARAM_PRODUCT_ID, productId);
        bundle.putString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeightFmt);
        RatesEstimationDetailFragment fragment = new RatesEstimationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        getComponent(RatesEstimationComponent.class).inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rates_estimation_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString(RatesEstimationConstant.PARAM_PRODUCT_ID, "");
            productWeightFmt = getArguments().getString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, "");
        }

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        loadingView = view.findViewById(R.id.loading_state_view);
        shippingDestination = view.findViewById(R.id.shipping_destination);
        shippingWeight = view.findViewById(R.id.shipping_weight);
        shippingReceiverName = view.findViewById(R.id.shipping_receiver_name);
        shippingReceiverAddress = view.findViewById(R.id.shipping_receiver_address);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        shippingWeight.setText(getString(R.string.rate_est_detail_weight_fmt, productWeightFmt));

        getCostEstimation();
    }

    private void getCostEstimation() {
        setViewState(VIEW_LOADING);
        presenter.getCostEstimation(GraphqlHelper.loadRawString(getResources(), R.raw.gql_pdp_estimasi_ongkir), productId);
    }

    private void setViewState(int viewLoading) {
        switch (viewLoading){
            case VIEW_LOADING:{
                loadingView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            } break;
            case VIEW_CONTENT:{
                loadingView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            } break;
        }
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onErrorLoadRateEstimaion(Throwable throwable) {

    }

    @Override
    public void onSuccesLoadRateEstimaion(RatesModel ratesModel) {
        shippingDestination.setText(getString(R.string.rate_est_detail_dest_fmt, ratesModel.getTexts().getTextDestination()));
        String title = getString(R.string.shipping_receiver_text,userSession.getName(), "Alamat Kantor");
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, userSession.getName().length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.font_black_disabled_38)),
                userSession.getName().length()+1, title.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        shippingReceiverName.setText(spannableString);
        shippingReceiverAddress.setText(String.format("%s\n%s", "0817 1234 5678",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2, Tokopedia Lt. 2, Jakarta"));
        adapter.updateShippingServices(ratesModel.getAttributes());
        setViewState(VIEW_CONTENT);
    }
}
