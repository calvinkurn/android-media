package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemDetailActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.ProductProblemAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemItemListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.di.DaggerResolutionComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ChooseProductAndProblemFragment extends BaseDaggerFragment implements
        ProductProblemListFragment.View, ProductProblemItemListener {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";
    public static final String PROBLEM_RESULT_DATA = "problem_result_data";
    public static final String RESULT_DATA = "result_data";
    public static final String RESULT_STEP_CODE = "result_step_code";
    public static final int REQUEST_CODE = 1234;

    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";


    RecyclerView rvProductProblem;
    ProductProblemAdapter adapter;
    ProductProblemListViewModel productProblemListViewModel;
    List<ComplaintResult> complaintResults = new ArrayList<>();
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
        DaggerResolutionComponent resolutionComponent =
                (DaggerResolutionComponent)DaggerResolutionComponent.builder()
                        .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext())
                                .getBaseAppComponent()).build();
        resolutionComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_problem, container, false);
        rvProductProblem = (RecyclerView) view.findViewById(R.id.rv_product_problem);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupArguments(getArguments());
        initView();
        setViewListener();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void setupArguments(Bundle arguments) {
        productProblemListViewModel = arguments.getParcelable(PRODUCT_PROBLEM_DATA);
        complaintResults = arguments.getParcelableArrayList(PROBLEM_RESULT_LIST_DATA);

    }

    private void initView() {
        disableBottomButton();
        rvProductProblem.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductProblemAdapter(getActivity(), this);
        rvProductProblem.setAdapter(adapter);
        presenter.loadProblemAndProduct(productProblemListViewModel, complaintResults);
    }

    private void setViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buttonContinueClicked();
                UnifyTracking.eventCreateResoStep1Continue(getActivity());
            }
        });

    }

    @Override
    public void populateProblemAndProduct(ProductProblemListViewModel productProblemViewModelList) {
        adapter.updateAdapter(productProblemViewModelList.getProductProblemViewModels());
    }

    @Override
    public void onItemClicked(ProductProblemViewModel productProblemViewModel) {
        Intent intent = ProductProblemDetailActivity.getInstance(getActivity(),
                productProblemViewModel, presenter.getProblemResultItem(productProblemViewModel));
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
    public void onProblemResultListUpdated(List<ComplaintResult> problemResults) {
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
    public void saveData(ArrayList<ComplaintResult> problemResults) {
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
                presenter.processResultData((ComplaintResult) data.getParcelableExtra(RESULT_DATA), data.getIntExtra(RESULT_STEP_CODE, 0));
            }
        }
    }

}
