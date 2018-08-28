package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionComplaintDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionOrderDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionOrderDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProblemAmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProductDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionProductImageDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionShippingDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.RequireViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionComplaintModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionOrderDetailModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionOrderModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionProblemAmountModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionProblemModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionProductImageModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionProductModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionShippingModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class SolutionSubscriber extends Subscriber<SolutionResponseDomain> {
    private final SolutionListFragmentListener.View mainView;

    public SolutionSubscriber(SolutionListFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.showErrorGetSolution(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SolutionResponseDomain solutionResponseDomain) {
        if (solutionResponseDomain != null) {
            mainView.showSuccessGetSolution(mappingSolutionResponseViewModel(solutionResponseDomain));
        }
    }

    private SolutionResponseViewModel mappingSolutionResponseViewModel(SolutionResponseDomain domain) {
        return new SolutionResponseViewModel(
                null,
                domain.getSolutions() != null ?
                        mappingSolutionViewModelList(domain.getSolutions()) :
                        new ArrayList<SolutionViewModel>(),
                domain.getRequire() != null ?
                        mappingRequireViewModel(domain.getRequire()) :
                        null,
                domain.getFreeReturn() != null ?
                        mappingFreeReturnViewModel(domain.getFreeReturn()) :
                        null,
                domain.getComplaints() != null ?
                        mappingSolutionComplaintDomain(domain.getComplaints()) :
                        null,
                null);
    }

    private List<SolutionViewModel> mappingSolutionViewModelList(List<SolutionDomain> domainList) {
        List<SolutionViewModel> viewModelList = new ArrayList<>();
        for (SolutionDomain solutionDomain : domainList) {
            viewModelList.add(new SolutionViewModel(
                    solutionDomain.getId(),
                    solutionDomain.getName(),
                    solutionDomain.getSolutionName(),
                    solutionDomain.getAmount() != null ?
                            mappingAmountViewModel(solutionDomain.getAmount()) :
                            null));
        }
        return viewModelList;
    }

    private RequireViewModel mappingRequireViewModel(RequireDomain domain) {
        return new RequireViewModel(domain.isAttachment());
    }

    private AmountViewModel mappingAmountViewModel(AmountDomain domain) {
        return new AmountViewModel(domain.getIdr(), domain.getInteger());
    }

    private FreeReturnViewModel mappingFreeReturnViewModel(FreeReturnDomain domain) {
        return new FreeReturnViewModel(domain.getInfo(), domain.getLink());
    }

    private static SolutionProblemModel mappingSolutionProblemDomain(SolutionProblemDomain response) {
        return new SolutionProblemModel(
                response.getType(),
                response.getName(),
                response.getTrouble(),
                response.getAmount() != null ?
                        mappingSolutionProblemAmountDomain(response.getAmount()) :
                        null,
                response.getMaxAmount() != null ?
                        mappingSolutionProblemAmountDomain(response.getMaxAmount()) :
                        null,
                response.getQty(),
                response.getRemark());
    }

    public static List<SolutionComplaintModel> mappingSolutionComplaintDomain(List<SolutionComplaintDomain> responseList) {
        List<SolutionComplaintModel> domainList = new ArrayList<>();
        for (SolutionComplaintDomain response : responseList) {
            SolutionComplaintModel domain = new SolutionComplaintModel(
                    response.getProblem() != null ? mappingSolutionProblemDomain(response.getProblem()) : null,
                    response.getShipping() != null ? mappingSolutionShippingDomain(response.getShipping()) : null,
                    response.getProduct() != null ? mappingSolutionProductDomain(response.getProduct()) : null,
                    response.getOrder() != null ? mappingSolutionOrderDomain(response.getOrder()) : null
            );
            domainList.add(domain);
        }
        return domainList;
    }

    private static SolutionProblemAmountModel mappingSolutionProblemAmountDomain(SolutionProblemAmountDomain response) {
        return new SolutionProblemAmountModel(response.getIdr(), response.getInteger());
    }

    private static SolutionShippingModel mappingSolutionShippingDomain(SolutionShippingDomain response) {
        return new SolutionShippingModel(response.getFee(), response.isChecked());
    }

    private static SolutionProductModel mappingSolutionProductDomain(SolutionProductDomain response) {
        return new SolutionProductModel(
                response.getName(),
                response.getPrice(),
                response.getImage() != null ?
                        mappingSolutionProductImageDomain(response.getImage()) :
                        null);
    }

    private static SolutionProductImageModel mappingSolutionProductImageDomain(SolutionProductImageDomain response) {
        return new SolutionProductImageModel(response.getFull(), response.getThumb());
    }

    private static SolutionOrderModel mappingSolutionOrderDomain(SolutionOrderDomain response) {
        return new SolutionOrderModel(response.getDetail() != null ? mappingSolutionOrderDetailDomain(response.getDetail()) : null);
    }

    private static SolutionOrderDetailModel mappingSolutionOrderDetailDomain(SolutionOrderDetailDomain response) {
        return new SolutionOrderDetailModel(response.getId(), response.isFreeReturn());
    }
}
