package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemFragmentPresenter extends BaseDaggerPresenter<ProductProblemListFragment.View>
        implements ProductProblemListFragment.Presenter {

    public static final int RESULT_SAVE = 2001;
    public static final int RESULT_SAVE_AND_CHOOSE_OTHER = 2002;

    private ProductProblemListFragment.View mainView;
    List<ComplaintResult> complaintResults = new ArrayList<>();


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
                                      List<ComplaintResult> complaintResults) {
        mainView.populateProblemAndProduct(productProblemViewModelList);
        if (complaintResults != null) {
            if (complaintResults.size() != 0) {
                this.complaintResults = complaintResults;
                updateProblemResultList();
            }
        }
    }

    @Override
    public void addOrRemoveStringProblem(ProductProblemViewModel productProblemViewModel) {
        boolean isContain = false;
        for (ComplaintResult complaintResult : complaintResults) {
            if (complaintResult.problem.name.equals(productProblemViewModel.getProblem().getName())) {
                isContain = true;
            }
        }
        if (isContain) {
            deleteProblemResult(productProblemViewModel);
        } else {
            addProblemResult(productProblemViewModel);
        }
    }

    private void addProblemResult(ProductProblemViewModel productProblemViewModel) {
        ComplaintResult complaintResult = new ComplaintResult();
        ProblemResult problemResult = new ProblemResult();
        problemResult.name = productProblemViewModel.getProblem().getName();
        problemResult.type = productProblemViewModel.getProblem().getType();
        problemResult.trouble = productProblemViewModel.getStatusList().get(0).getTrouble().get(0).getId();
        complaintResult.problem = problemResult;
        complaintResults.add(complaintResult);
        updateProblemResultList();
    }

    private void deleteProblemResult(ProductProblemViewModel productProblemViewModel) {
        removeProblemResult(productProblemViewModel);
    }

    private void updateProblemResultList() {
        updateContinueButton();
        mainView.onProblemResultListUpdated(complaintResults);
    }

    @Override
    public void updateProblemResultData() {

    }

    public ComplaintResult getProblemResultItem(ProductProblemViewModel productProblemViewModel) {
        for (ComplaintResult complaintResult : complaintResults) {
            if (complaintResult.problem.id == productProblemViewModel.getOrder().getDetail().getId()) {
                return complaintResult;
            }
        }
        return null;
    }

    @Override
    public void removeProblemResult(ProductProblemViewModel productProblemViewModel) {
        List<ComplaintResult> tempList = new ArrayList<>();
        for (ComplaintResult complaint : complaintResults) {
            if (complaint.problem.type == 1) {
                if (!complaint.problem.name.equals(productProblemViewModel.getProblem().getName())) {
                    tempList.add(complaint);
                }
            } else {
                if (productProblemViewModel.getOrder() == null || complaint.problem.id != productProblemViewModel.getOrder().getDetail().getId()) {
                    tempList.add(complaint);
                }
            }
        }
        complaintResults = tempList;
        updateProblemResultList();
    }

    @Override
    public void processResultData(ComplaintResult complaintResult, int resultStepCode) {
        boolean isContain = false;
        for (ComplaintResult complaintObject : complaintResults) {
            if (complaintObject.problem.id == complaintResult.problem.id) {
                isContain = true;
                break;
            }
        }
        if (!isContain) {
            addResultList(complaintResult);
        } else {
            updateResultList(complaintResult);
        }
        updateProblemResultList();
        if (resultStepCode == RESULT_SAVE) {
            buttonContinueClicked();
        }
    }

    private void addResultList(ComplaintResult complaintResult) {
        complaintResults.add(complaintResult);
    }

    private void updateResultList(ComplaintResult complaintResult) {
        List<ComplaintResult> tempList = new ArrayList<>();
        for (ComplaintResult complainObject : complaintResults) {
            tempList.add((complainObject.problem.id == complaintResult.problem.id) ? complaintResult : complainObject);
        }
        complaintResults = tempList;
    }

    private void updateContinueButton() {
        if (complaintResults.size() != 0) {
            mainView.enableBottomButton();
        } else {
            mainView.disableBottomButton();
        }
    }

    @Override
    public void buttonContinueClicked() {
        ArrayList<ComplaintResult> problemResultArrayList = new ArrayList<>(complaintResults);
        mainView.saveData(problemResultArrayList);
    }
}
