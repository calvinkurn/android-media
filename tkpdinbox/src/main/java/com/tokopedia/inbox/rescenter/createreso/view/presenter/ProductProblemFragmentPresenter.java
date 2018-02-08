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

    public static final int RESULT_SAVE = 2001;
    public static final int RESULT_SAVE_AND_CHOOSE_OTHER = 2002;

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
    public void loadProblemAndProduct(ProductProblemListViewModel productProblemViewModelList,
                                      List<ProblemResult> problemResultList) {
        mainView.populateProblemAndProduct(productProblemViewModelList);
        if (problemResultList != null) {
            if (problemResultList.size() != 0) {
                this.problemResultList = problemResultList;
                updateProblemResultList();
            }
        }
    }

    @Override
    public void addOrRemoveStringProblem(ProductProblemViewModel productProblemViewModel) {
        boolean isContain = false;
        for (ProblemResult problemResult : problemResultList) {
            if (problemResult.name.equals(productProblemViewModel.getProblem().getName())) {
                isContain = true;
            }
        }
        if (isContain) {
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
        removeProblemResult(productProblemViewModel);
    }

    public void updateProblemResultList() {
        updateContinueButton();
        mainView.onProblemResultListUpdated(problemResultList);
    }

    @Override
    public void updateProblemResultData() {

    }

    public ProblemResult getProblemResultItem(ProductProblemViewModel productProblemViewModel) {
        for (ProblemResult problemResult : problemResultList) {
            if (problemResult.id == productProblemViewModel.getOrder().getDetail().getId()) {
                return problemResult;
            }
        }
        return null;
    }

    @Override
    public void removeProblemResult(ProductProblemViewModel productProblemViewModel) {
        List<ProblemResult> tempList = new ArrayList<>();
        for (ProblemResult problemObject : problemResultList) {
            if (problemObject.type == 1) {
                if (!problemObject.name.equals(productProblemViewModel.getProblem().getName())) {
                    tempList.add(problemObject);
                }
            } else {
                if (productProblemViewModel.getOrder() == null || problemObject.id != productProblemViewModel.getOrder().getDetail().getId()) {
                    tempList.add(problemObject);
                }
            }
        }
        problemResultList = tempList;
        updateProblemResultList();
    }

    @Override
    public void processResultData(ProblemResult problemResult, int resultStepCode) {
        boolean isContain = false;
        List<ProblemResult> tempResultList = new ArrayList<>();
        for (ProblemResult problemObject : problemResultList) {
            if (problemObject.id == problemResult.id) {
                isContain = true;
                tempResultList.add(problemResult);
            } else {
                tempResultList.add(problemObject);
            }
        }
        if (!isContain) {
            addResultList(problemResult);
        } else {
            updateResultList(problemResult);
        }
        updateProblemResultList();
        if (resultStepCode == RESULT_SAVE) {
            buttonContinueClicked();
        }
    }

    public void addResultList(ProblemResult problemResult) {
        problemResultList.add(problemResult);
    }

    public void updateResultList(ProblemResult problemResult) {
        List<ProblemResult> tempList = new ArrayList<>();
        for (ProblemResult problemObject : problemResultList) {
            tempList.add((problemObject.id == problemResult.id) ? problemResult : problemObject);
        }
        problemResultList = tempList;
    }

    public void updateContinueButton() {
        if (problemResultList.size() != 0) {
            mainView.enableBottomButton();
        } else {
            mainView.disableBottomButton();
        }
    }

    @Override
    public void buttonContinueClicked() {
        ArrayList<ProblemResult> problemResultArrayList = new ArrayList<>();
        for (ProblemResult problemResult : problemResultList) {
            problemResultArrayList.add(problemResult);
        }
        mainView.saveData(problemResultArrayList);
    }
}
