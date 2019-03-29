package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusInfoViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusTroubleViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemDetailFragmentPresenter
        extends BaseDaggerPresenter<ProductProblemDetailFragmentListener.View>
        implements ProductProblemDetailFragmentListener.Presenter {

    public static final int RESULT_SAVE = 2001;
    public static final int RESULT_SAVE_AND_CHOOSE_OTHER = 2002;

    private Context context;
    private ProductProblemDetailFragmentListener.View mainView;
    private ComplaintResult complaintResult;
    private ProductProblemViewModel productProblemViewModel;
    private HashMap<String, Integer> troubleHashMap = new HashMap<>();
    private int currentTroublePos = 0;
    private boolean isEditData;

    public ProductProblemDetailFragmentPresenter(Context context) {
        this.context = context;
        complaintResult = new ComplaintResult();
    }

    @Override
    public void attachView(ProductProblemDetailFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void populateData(ProductProblemViewModel productProblemViewModel, ComplaintResult complaintResult) {
        this.productProblemViewModel = productProblemViewModel;
        if (complaintResult != null) {
            updateProblemResult(complaintResult);
        } else {
            initProblemResult(productProblemViewModel);
        }
        mainView.populateDataToScreen(productProblemViewModel);
    }

    private void updateProblemResult(ComplaintResult complaintResult) {
        this.complaintResult = complaintResult;
        ProblemResult problemResult = complaintResult.problem;
        isEditData = true;
        currentTroublePos = problemResult.trouble;
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
        mainView.updatePlusMinusButton(problemResult.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
        mainView.updateComplainReasonValue(problemResult.remark);
        validateMainButton();

    }

    private void initProblemResult(ProductProblemViewModel productProblemViewModel) {
        complaintResult.problem.type = productProblemViewModel.getProblem().getType();
        complaintResult.problem.isDelivered = true;
        //init with first trouble item of delivered status
        updateSpinner(complaintResult.problem.isDelivered);
        getCanShowNotArrived();
        complaintResult.problem.trouble = 0;
        complaintResult.problem.remark = "";
        complaintResult.problem.amount = 0;
        complaintResult.problem.id = productProblemViewModel.getOrder().getDetail().getId();
        complaintResult.problem.quantity = productProblemViewModel.getOrder().getProduct().getQuantity();
        complaintResult.problem.name = productProblemViewModel.getProblem().getName();
        complaintResult.order.detail.id = productProblemViewModel.getOrder().getDetail().getId();
        mainView.updateArriveStatusButton(complaintResult.problem.isDelivered, complaintResult.problem.canShowInfo);
        mainView.updatePlusMinusButton(complaintResult.problem.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    @Override
    public void btnArrivedClicked() {
        complaintResult.problem.isDelivered = true;
        updateSpinner(true);
        mainView.updateArriveStatusButton(complaintResult.problem.isDelivered, complaintResult.problem.canShowInfo);
    }

    @Override
    public void btnNotArrivedClicked() {
        complaintResult.problem.isDelivered = false;
        updateSpinner(false);
        mainView.updateArriveStatusButton(complaintResult.problem.isDelivered, complaintResult.problem.canShowInfo);
    }


    @Override
    public void updateSpinner(boolean isDelivered) {
        troubleHashMap = new HashMap<>();
        int pos = 0;
        for (StatusViewModel statusViewModel : productProblemViewModel.getStatusList()) {
            if (isDelivered == statusViewModel.isDelivered()) {
                String[] troubleStringArray = new String[isDelivered ?
                        statusViewModel.getTrouble().size() + 1 :
                        statusViewModel.getTrouble().size()];
                int i = 0;
                if (isDelivered) {
                    troubleHashMap.put("Pilih Masalah", 0);
                    troubleStringArray[i] = "Pilih Masalah";
                    i++;
                }
                for (StatusTroubleViewModel statusTroubleViewModel : statusViewModel.getTrouble()) {
                    troubleHashMap.put(statusTroubleViewModel.getName(), statusTroubleViewModel.getId());
                    if (currentTroublePos == statusTroubleViewModel.getId()) {
                        pos = i;
                    }
                    troubleStringArray[i] = statusTroubleViewModel.getName();
                    i++;
                }
                complaintResult.problem.trouble = statusViewModel.getTrouble().get(0).getId();
                if (!isEditData && isDelivered) {
                    complaintResult.problem.trouble = 0;
                }
                mainView.populateReasonSpinner(troubleStringArray, pos);
            }
        }
    }

    private void getCanShowNotArrived() {
        for (StatusViewModel statusViewModel : productProblemViewModel.getStatusList()) {
            if (!statusViewModel.isDelivered()) {
                StatusInfoViewModel statusInfoViewModel = statusViewModel.getInfo();
                complaintResult.problem.canShowInfo = statusInfoViewModel.isShow();
            }
        }
    }

    @Override
    public void updateTroubleValue(String trouble) {
        complaintResult.problem.trouble = troubleHashMap.get(trouble);
        validateMainButton();
    }

    @Override
    public void updateComplainReason(String reason) {
        if (reason.length() < 30) {
            mainView.updateComplainReasonView(false, context.getResources().getString(R.string.string_min_30_char));
            complaintResult.problem.remark = "";
        } else {
            complaintResult.problem.remark = reason;
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

    private void updatePlusMinusView(int diff) {
        complaintResult.problem.quantity += diff;
        mainView.updatePlusMinusButton(complaintResult.problem.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    private void validateMainButton() {
        mainView.updateBottomMainButton(complaintResult.problem.trouble != 0 && !complaintResult.problem.remark.equals(""));
    }

    @Override
    public void btnSaveClicked(boolean isSaveAndChooseOtherButton) {
        mainView.saveData(complaintResult, isSaveAndChooseOtherButton ?
                RESULT_SAVE_AND_CHOOSE_OTHER :
                RESULT_SAVE);
    }

    @Override
    public long getDuration(String deliveryDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(deliveryDate);
            Date currentTime = new Date();
            long duration = date.getTime() - currentTime.getTime();
            return duration;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String getDeliveryDate(List<StatusViewModel> statusList) {
        for (StatusViewModel statusViewModel : statusList) {
            if (!statusViewModel.isDelivered() && statusViewModel.getInfo() != null) {
                return statusViewModel.getInfo().getDate();
            }
        }
        return "";
    }

    @Override
    public void onDisableInfoView() {
        complaintResult.problem.canShowInfo = false;
    }
}
