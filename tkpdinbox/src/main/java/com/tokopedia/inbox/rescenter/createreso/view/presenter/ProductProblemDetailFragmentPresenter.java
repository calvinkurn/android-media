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
    public void populateData(ProductProblemViewModel productProblemViewModel) {
        this.productProblemViewModel = productProblemViewModel;
        initProblemResult(productProblemViewModel);
        mainView.populateDataToScreen(productProblemViewModel);
    }

    public void initProblemResult(ProductProblemViewModel productProblemViewModel) {
        problemResult.type = productProblemViewModel.getProblem().getType();
        //init with first trouble item of undelivered status
        updateSpinner(problemResult.isDelivered);
        getCanShowNotArrived();
        problemResult.remark = "";
        problemResult.quantity = 1;
        problemResult.order.detail.id = productProblemViewModel.getOrder().getDetail().getId();
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
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
    }

    @Override
    public void updateComplainReason(String reason) {
        if (reason.length() < 30) {
            mainView.updateComplainReasonView(false,"Minimal 30 karakter");
        } else {
            problemResult.remark = reason;
            mainView.updateComplainReasonView(true,"");
        }
    }
}
