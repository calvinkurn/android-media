package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemDetailActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.ProductProblemAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemItemListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ChooseProductAndProblemFragment extends BaseDaggerFragment implements ProductProblemListFragment.View, ProductProblemItemListener {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";
    public static final String PROBLEM_RESULT_DATA = "problem_result_data";
    public static final String RESULT_DATA = "result_data";
    public static final String RESULT_STEP_CODE = "result_step_code";
    public static final int REQUEST_CODE = 1234;

    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";


    RecyclerView rvProductProblem;
    ProductProblemAdapter adapter;
    ProductProblemListViewModel productProblemListViewModel;
    List<ProblemResult> problemResultList = new ArrayList<>();
    Button btnContinue;

    public static ChooseProductAndProblemFragment newInstance(ProductProblemListViewModel productProblemListViewModel, ArrayList<ProblemResult> problemResultList) {
        ChooseProductAndProblemFragment fragment = new ChooseProductAndProblemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_PROBLEM_DATA, productProblemListViewModel);
        bundle.putParcelableArrayList(PROBLEM_RESULT_LIST_DATA, problemResultList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    ProductProblemFragmentPresenter presenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerCreateResoComponent daggerCreateResoComponent =
                (DaggerCreateResoComponent) DaggerCreateResoComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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
    protected void setupArguments(Bundle arguments) {
        productProblemListViewModel = arguments.getParcelable(PRODUCT_PROBLEM_DATA);
        problemResultList = arguments.getParcelableArrayList(PROBLEM_RESULT_LIST_DATA);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_problem;
    }

    @Override
    protected void initView(View view) {
        rvProductProblem = (RecyclerView) view.findViewById(R.id.rv_product_problem);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        disableBottomButton();
        rvProductProblem.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProductProblemAdapter(context, this);
        rvProductProblem.setAdapter(adapter);
        presenter.loadProblemAndProduct(productProblemListViewModel, problemResultList);
    }

    @Override
    protected void setViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buttonContinueClicked();
                UnifyTracking.eventCreateResoStep1Continue();
            }
        });

    }

    @Override
    public void populateProblemAndProduct(ProductProblemListViewModel productProblemViewModelList) {
        adapter.updateAdapter(productProblemViewModelList.getProductProblemViewModels());
    }

    @Override
    public void onItemClicked(ProductProblemViewModel productProblemViewModel) {
        Intent intent = new Intent(getActivity(), ProductProblemDetailActivity.class);
        intent.putExtra(PRODUCT_PROBLEM_DATA, productProblemViewModel);
        intent.putExtra(PROBLEM_RESULT_DATA, presenter.getProblemResultItem(productProblemViewModel));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRemoveProductProblem(ProductProblemViewModel productProblemViewModel) {
        presenter.removeProblemResult(productProblemViewModel);
    }

    @Override
    public void onStringProblemClicked(ProductProblemViewModel productProblemViewModel) {
        presenter.addOrRemoveStringProblem(productProblemViewModel);

    }

    @Override
    public void onProblemResultListUpdated(List<ProblemResult> problemResults) {
        adapter.clearAndUpdateSelectedItem(problemResults);

    }

    @Override
    public void enableBottomButton() {
        btnContinue.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_green));
        btnContinue.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        btnContinue.setClickable(true);
        btnContinue.setEnabled(true);
    }

    @Override
    public void disableBottomButton() {
        btnContinue.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        btnContinue.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_38));
        btnContinue.setClickable(false);
        btnContinue.setEnabled(false);
    }

    @Override
    public void saveData(ArrayList<ProblemResult> problemResults) {
        Intent output = new Intent();
        output.putParcelableArrayListExtra(PROBLEM_RESULT_LIST_DATA, problemResults);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.processResultData((ProblemResult) data.getParcelableExtra(RESULT_DATA), data.getIntExtra(RESULT_STEP_CODE, 0));
            }
        }
    }

}
