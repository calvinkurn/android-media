package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusInfoViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusTroubleViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusViewModel;

import java.util.HashMap;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemDetailFragmentPresenter extends BaseDaggerPresenter<ProductProblemDetailFragment.View> implements ProductProblemDetailFragment.Presenter {

    public static final int RESULT_SAVE = 2001;
    public static final int RESULT_SAVE_AND_CHOOSE_OTHER = 2002;

    private Context context;
    private ProductProblemDetailFragment.View mainView;
    private ProblemResult problemResult;
    private ProductProblemViewModel productProblemViewModel;
    private HashMap<String, Integer> troubleHashMap = new HashMap<>();

    public ProductProblemDetailFragmentPresenter(Context context) {
        this.context = context;
        problemResult = new ProblemResult();
    }

    @Override
    public void attachView(ProductProblemDetailFragment.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void populateData(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult) {
        this.productProblemViewModel = productProblemViewModel;
        if (problemResult != null) {
            updateProblemResult(problemResult);
        } else {
            initProblemResult(productProblemViewModel);
        }
        mainView.populateDataToScreen(productProblemViewModel);
    }

    public void updateProblemResult(ProblemResult problemResult) {
        this.problemResult = problemResult;
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
        mainView.updatePlusMinusButton(problemResult.quantity, productProblemViewModel.getOrder().getProduct().getQuantity());
        mainView.updateComplainReasonValue(problemResult.remark);
        validateMainButton();

    }

    public void initProblemResult(ProductProblemViewModel productProblemViewModel) {
        problemResult.type = productProblemViewModel.getProblem().getType();
        problemResult.isDelivered = false;
        //init with first trouble item of undelivered status
        updateSpinner(problemResult.isDelivered);
        getCanShowNotArrived();
        problemResult.remark = "";
        problemResult.quantity = 1;
        problemResult.name = productProblemViewModel.getProblem().getName();
        problemResult.order.detail.id = productProblemViewModel.getOrder().getDetail().getId();
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
        mainView.updatePlusMinusButton(problemResult.quantity, productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    @Override
    public void btnArrivedClicked() {
        problemResult.isDelivered = true;
        updateSpinner(true);
    }

    @Override
    public void btnNotArrivedClicked() {
        problemResult.isDelivered = false;
        updateSpinner(false);
    }


    @Override
    public void updateSpinner(boolean isDelivered) {
        troubleHashMap = new HashMap<>();
        for (StatusViewModel statusViewModel : productProblemViewModel.getStatusList()) {
            if (isDelivered == statusViewModel.isDelivered()) {
                String[] troubleStringArray = new String[statusViewModel.getTrouble().size()];
                problemResult.trouble = statusViewModel.getTrouble().get(0).getId();
                int i = 0;
                for (StatusTroubleViewModel statusTroubleViewModel : statusViewModel.getTrouble()) {
                    troubleHashMap.put(statusTroubleViewModel.getName(), statusTroubleViewModel.getId());
                    troubleStringArray[i] = statusTroubleViewModel.getName();
                    i++;
                }
                mainView.populateReasonSpinner(troubleStringArray);
            }
        }
//        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
    }

    public void getCanShowNotArrived() {
        for (StatusViewModel statusViewModel : productProblemViewModel.getStatusList()) {
            if (!statusViewModel.isDelivered()) {
                StatusInfoViewModel statusInfoViewModel = statusViewModel.getInfo();
                problemResult.canShowInfo = statusInfoViewModel.isShow();
            }
        }
    }

    @Override
    public void updateTroubleValue(String trouble) {
        problemResult.trouble = troubleHashMap.get(trouble);
        validateMainButton();
    }

    @Override
    public void updateComplainReason(String reason) {
        if (reason.length() < 30) {
            mainView.updateComplainReasonView(false, "Minimal 30 karakter");
            problemResult.remark = "";
        } else {
            problemResult.remark = reason;
            mainView.updateComplainReasonView(true, "");
        }
        validateMainButton();
    }

    @Override
    public void btnInfoClicked() {
        mainView.showInfoDialog(productProblemViewModel);
    }

    @Override
    public void increaseQty() {
        updatePlusMinusView(1);
    }

    @Override
    public void decreaseQty() {
        updatePlusMinusView(-1);
    }

    public void updatePlusMinusView(int diff) {
        problemResult.quantity += diff;
        mainView.updatePlusMinusButton(problemResult.quantity, productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    public void validateMainButton() {
        mainView.updateBottomMainButton((!problemResult.remark.equals("") && problemResult.trouble != 0));
    }

    @Override
    public void btnSaveClicked(boolean isSaveAndChooseOtherButton) {
        mainView.saveData(problemResult, isSaveAndChooseOtherButton ? RESULT_SAVE_AND_CHOOSE_OTHER : RESULT_SAVE);
    }
}
