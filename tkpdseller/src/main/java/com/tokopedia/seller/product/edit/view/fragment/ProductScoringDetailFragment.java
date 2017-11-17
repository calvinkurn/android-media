package com.tokopedia.seller.product.edit.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.ProductExtraConstant;
import com.tokopedia.seller.product.edit.di.component.DaggerProductScoringComponent;
import com.tokopedia.seller.product.edit.di.module.ProductScoringModule;
import com.tokopedia.seller.product.edit.utils.ScoringProductHelper;
import com.tokopedia.seller.product.edit.view.adapter.IndicatorScoringAdapter;
import com.tokopedia.seller.product.edit.view.listener.ProductScoringDetailView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.TotalScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductScoringDetailPresenter;

import javax.inject.Inject;


/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoringDetailFragment extends BaseDaggerFragment implements ProductScoringDetailView {

    private TextView valueScoreProduct;
    private RecyclerView recyclerViewIndicatorScore;

    @Inject
    ProductScoringDetailPresenter presenter;
    private IndicatorScoringAdapter adapter;
    private ValueIndicatorScoreModel valueIndicatorScoreModel;
    private ProgressDialog progressDialog;
    private SnackbarRetry snackbarRetry;

    public static ProductScoringDetailFragment createInstance(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        ProductScoringDetailFragment productScoringDetailFragment = new ProductScoringDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA, valueIndicatorScoreModel);
        productScoringDetailFragment.setArguments(bundle);
        return productScoringDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valueIndicatorScoreModel = getArguments().getParcelable(ProductExtraConstant.VALUE_PRODUCT_SCORING_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_score_detail, container, false);

        initalVar();
        initView(view);
        setListener();
        actionVar();

        return view;
    }

    private void initalVar() {
        adapter = new IndicatorScoringAdapter(getActivity());
    }

    private void actionVar() {
        if (valueIndicatorScoreModel != null) {
            presenter.getProductScoring(valueIndicatorScoreModel);
        }
    }

    private void setListener() {
        recyclerViewIndicatorScore.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewIndicatorScore.setAdapter(adapter);
    }

    private void initView(View view) {
        valueScoreProduct = (TextView) view.findViewById(R.id.value_score_product);
        recyclerViewIndicatorScore = (RecyclerView) view.findViewById(R.id.recycler_view_indicator_score);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));

        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    protected void initInjector() {
        DaggerProductScoringComponent
                .builder()
                .productScoringModule(new ProductScoringModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessGetScoringProduct(DataScoringProductView dataScoringProductView) {
        TotalScoringProductView totalScoringProductView = dataScoringProductView.getTotalScoringProductView();
        valueScoreProduct.setText(totalScoringProductView.getValueScoreProduct());
        valueScoreProduct.setTextColor(ScoringProductHelper.getColorOfScore(totalScoringProductView.getColor(), getActivity()));
        adapter.addData(dataScoringProductView.getIndicatorScoreView());
    }

    @Override
    public void onErrorGetScoringProduct() {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                getString(R.string.product_score_label_error_count), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getProductScoring(valueIndicatorScoreModel);
                    }
                });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}