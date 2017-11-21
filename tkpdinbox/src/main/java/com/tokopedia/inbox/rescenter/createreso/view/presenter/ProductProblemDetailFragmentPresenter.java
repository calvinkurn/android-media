package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragment;
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
        extends BaseDaggerPresenter<ProductProblemDetailFragment.View>
        implements ProductProblemDetailFragment.Presenter {

    public static final int RESULT_SAVE = 2001;
    public static final int RESULT_SAVE_AND_CHOOSE_OTHER = 2002;

    private Context context;
    private ProductProblemDetailFragment.View mainView;
    private ProblemResult problemResult;
    private ProductProblemViewModel productProblemViewModel;
    private HashMap<String, Integer> troubleHashMap = new HashMap<>();
    private int currentTroublePos = 0;
    private boolean isEditData;

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
        isEditData = true;
        this.problemResult = problemResult;
        currentTroublePos = problemResult.trouble;
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
        mainView.updatePlusMinusButton(problemResult.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
        mainView.updateComplainReasonValue(problemResult.remark);
        validateMainButton();

    }

    public void initProblemResult(ProductProblemViewModel productProblemViewModel) {
        problemResult.type = productProblemViewModel.getProblem().getType();
        problemResult.isDelivered = true;
        //init with first trouble item of delivered status
        updateSpinner(problemResult.isDelivered);
        getCanShowNotArrived();
        problemResult.trouble = 0;
        problemResult.remark = "";
        problemResult.id = productProblemViewModel.getOrder().getDetail().getId();
        problemResult.quantity = productProblemViewModel.getOrder().getProduct().getQuantity();
        problemResult.name = productProblemViewModel.getProblem().getName();
        problemResult.order.detail.id = productProblemViewModel.getOrder().getDetail().getId();
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
        mainView.updatePlusMinusButton(problemResult.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    @Override
    public void btnArrivedClicked() {
        problemResult.isDelivered = true;
        updateSpinner(true);
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
    }

    @Override
    public void btnNotArrivedClicked() {
        problemResult.isDelivered = false;
        updateSpinner(false);
        mainView.updateArriveStatusButton(problemResult.isDelivered, problemResult.canShowInfo);
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
                problemResult.trouble = statusViewModel.getTrouble().get(0).getId();
                if (!isEditData && isDelivered) {
                    problemResult.trouble = 0;
                }
                mainView.populateReasonSpinner(troubleStringArray, pos);
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
        validateMainButton();
    }

    @Override
    public void updateComplainReason(String reason) {
        if (reason.length() < 30) {
            mainView.updateComplainReasonView(false, context.getResources().getString(R.string.string_min_30_char));
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
        mainView.updatePlusMinusButton(problemResult.quantity,
                productProblemViewModel.getOrder().getProduct().getQuantity());
    }

    public void validateMainButton() {
        mainView.updateBottomMainButton(problemResult.trouble != 0 && !problemResult.remark.equals(""));
    }

    @Override
    public void btnSaveClicked(boolean isSaveAndChooseOtherButton) {
        mainView.saveData(problemResult, isSaveAndChooseOtherButton ?
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
        problemResult.canShowInfo = false;
    }
}
