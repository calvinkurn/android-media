package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemFragmentPresenter extends BaseDaggerPresenter<ProductProblemListFragment.View> implements ProductProblemListFragment.Presenter {

    private ProductProblemListFragment.View mainView;
    List<ProblemResult> problemResultList = new ArrayList<>();


    @Inject
    public ProductProblemFragmentPresenter() {
    }

    @Override
    public void attachView(ProductProblemListFragment.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void loadProblemAndProduct(ProductProblemListViewModel productProblemViewModelList) {
        mainView.populateProblemAndProduct(productProblemViewModelList);
    }

    @Override
    public void addOrRemoveStringProblem(ProductProblemViewModel productProblemViewModel) {
        boolean isContain = false;
        for (ProblemResult problemResult : problemResultList) {
            if (problemResult.name.equals(productProblemViewModel.getProblem().getName())) {
                isContain = true;
            }
        }
        if(isContain) {
            deleteProblemResult(productProblemViewModel);
        } else {
            addProblemResult(productProblemViewModel);
        }
    }

    public void addProblemResult(ProductProblemViewModel productProblemViewModel) {
        ProblemResult problemResult = new ProblemResult();
        problemResult.name = productProblemViewModel.getProblem().getName();
        problemResult.type = productProblemViewModel.getProblem().getType();
        problemResult.trouble = productProblemViewModel.getStatusList().get(0).getTrouble().get(0).getId();
        problemResultList.add(problemResult);
        updateProblemResultList();
    }

    public void deleteProblemResult(ProductProblemViewModel productProblemViewModel) {
        List<ProblemResult> tempList = new ArrayList<>();
        for (ProblemResult problemObject : problemResultList) {
            if (!problemObject.name.equals(productProblemViewModel.getProblem().getName())) {
                tempList.add(problemObject);
            }
        }
        problemResultList = tempList;
        updateProblemResultList();
    }

    public void updateProblemResultList() {
        updateContinueButton();
        mainView.onProblemResultListUpdated(problemResultList);
    }

    @Override
    public void updateProblemResultData() {

    }

    @Override
    public void removeProblemResult(ProductProblemViewModel productProblemViewModel) {
        List<ProblemResult> tempResultList = new ArrayList<>();
        for (ProblemResult problemObject : problemResultList) {
            if (!problemObject.name.equals(productProblemViewModel.getProblem().getName())) {
                tempResultList.add(problemObject);
            }
        }
        problemResultList = tempResultList;
        updateProblemResultList();
    }

    @Override
    public void processResultData(ProblemResult problemResult, int resultStepCode) {
        if (problemResultList.size() != 0) {
            List<ProblemResult> tempResultList = new ArrayList<>();
            for (ProblemResult problemObject : problemResultList) {
                if (problemObject.name.equals(problemResult.name)) {
                    tempResultList.add(problemResult);
                } else {
                    tempResultList.add(problemObject);
                }
            }
            problemResultList = tempResultList;
        } else {
            problemResultList.add(problemResult);
        }
        updateProblemResultList();
    }

    public void updateContinueButton() {
        if (problemResultList.size() != 0) {
            mainView.enableBottomButton();
        } else {
            mainView.disableBottomButton();
        }
    }
}
