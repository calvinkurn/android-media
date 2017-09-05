package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.OrderProductDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ShippingDetailDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ShippingDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusInfoDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.StatusTroubleDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep1UseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep2UseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.CreateResoStep1Subscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.LoadProductSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderDetailViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderProductViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ShippingDetailViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ShippingViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusInfoViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusTroubleViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/08/17.
 */

public class CreateResolutionCenterPresenter extends BaseDaggerPresenter<CreateResolutionCenter.View> implements CreateResolutionCenter.Presenter {
    private CreateResolutionCenter.View mainView;
    private GetProductProblemUseCase getProductProblemUseCase;
    private CreateResoStep1UseCase createResoStep1UseCase;
    private CreateResoStep2UseCase createResoStep2UseCase;
    private ProductProblemResponseDomain productProblemResponseDomain;

    private ResultViewModel resultViewModel;

    @Inject
    public CreateResolutionCenterPresenter(GetProductProblemUseCase getProductProblemUseCase,
                                           CreateResoStep1UseCase createResoStep1UseCase,
                                           CreateResoStep2UseCase createResoStep2UseCase) {
        this.getProductProblemUseCase = getProductProblemUseCase;
        this.createResoStep1UseCase = createResoStep1UseCase;
        this.createResoStep2UseCase = createResoStep2UseCase;
        resultViewModel = new ResultViewModel();
    }

    @Override
    public void attachView(CreateResolutionCenter.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void loadProductProblem(String orderId) {
        mainView.showLoading();
        resultViewModel.orderId = orderId;
        getProductProblemUseCase.execute(getProductProblemUseCase.getProductProblemUseCaseParam(orderId), new LoadProductSubscriber(mainView));
    }

    @Override
    public void updateProductProblemResponseDomain(ProductProblemResponseDomain productProblemResponseDomain) {
        this.productProblemResponseDomain = productProblemResponseDomain;
    }

    @Override
    public void chooseProductProblemClicked() {
        ArrayList<ProblemResult> problemResults = new ArrayList<>();
        for (ProblemResult problemResult : resultViewModel.problem) {
            problemResults.add(problemResult);
        }
        mainView.transitionToChooseProductAndProblemPage(mappingDomainToViewModel(productProblemResponseDomain), problemResults);
    }

    @Override
    public void solutionClicked() {
        mainView.transitionToSolutionPage(resultViewModel);

    }

    @Override
    public void uploadProveClicked() {
        mainView.transitionToUploadProvePage(resultViewModel);
    }

    @Override
    public void addResultFromStep1(ArrayList<ProblemResult> problemResultList) {
        resultViewModel.problem = problemResultList;
        mainView.updateView(resultViewModel);
    }

    @Override
    public void addResultFromStep2(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        mainView.updateView(resultViewModel);
    }

    @Override
    public void addResultFromStep3(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        mainView.updateView(resultViewModel);
    }

    @Override
    public void createResoClicked() {
        createResoStep1UseCase.execute(createResoStep1UseCase.createResoStep1Params(resultViewModel), new CreateResoStep1Subscriber(mainView));
    }

    private ProductProblemListViewModel mappingDomainToViewModel(ProductProblemResponseDomain responseDomain) {
        List<ProductProblemViewModel> modelList = new ArrayList<>();
        if (responseDomain != null) {
            if (responseDomain.getProductProblemDomainList() != null) {
                for (ProductProblemDomain productProblemDomain : responseDomain.getProductProblemDomainList()) {
                    ProductProblemViewModel productProblemViewModel = new ProductProblemViewModel(
                            productProblemDomain.getProblemDomain() != null ? mappingProblemViewModel(productProblemDomain.getProblemDomain()) : null,
                            productProblemDomain.getOrderDomain() != null ? mappingOrderViewModel(productProblemDomain.getOrderDomain()) : null,
                            productProblemDomain.getStatusDomainList() != null ? mappingStatusViewModel(productProblemDomain.getStatusDomainList()) : null
                    );
                    modelList.add(productProblemViewModel);
                }
            }
        }
        ProductProblemListViewModel productProblemListViewModel = new ProductProblemListViewModel(modelList);
        return productProblemListViewModel;
    }

    private ProblemViewModel mappingProblemViewModel(ProblemDomain problemDomain) {
        return new ProblemViewModel(
                problemDomain.getType(),
                problemDomain.getName());
    }

    private OrderViewModel mappingOrderViewModel(OrderDomain orderDomain) {
        return new OrderViewModel(
                orderDomain.getDetailDomain() != null ? mappingOrderDetailViewModel(orderDomain.getDetailDomain()) : null,
                orderDomain.getProductDomain() != null ? mappingOrderProductViewModel(orderDomain.getProductDomain()) : null,
                orderDomain.getShippingDomain() != null ? mappingShippingViewModel(orderDomain.getShippingDomain()) : null);
    }

    private OrderDetailViewModel mappingOrderDetailViewModel(OrderDetailDomain orderDetailDomain) {
        return new OrderDetailViewModel(
                orderDetailDomain.getId(),
                orderDetailDomain.getReturnable());
    }

    private OrderProductViewModel mappingOrderProductViewModel(OrderProductDomain orderProductDomain) {
        return new OrderProductViewModel(orderProductDomain.getName(),
                orderProductDomain.getThumb(),
                orderProductDomain.getVariant(),
                orderProductDomain.getQuantity(),
                orderProductDomain.getAmountDomain() != null ? mappingAmountViewModel(orderProductDomain.getAmountDomain()) : null);
    }

    private AmountViewModel mappingAmountViewModel(AmountDomain amountDomain) {
        return new AmountViewModel(
                amountDomain.getIdr(),
                amountDomain.getInteger());
    }

    private ShippingViewModel mappingShippingViewModel(ShippingDomain shippingDomain) {
        return new ShippingViewModel(shippingDomain.getId(),
                shippingDomain.getName(),
                shippingDomain.getDetailDomain() != null ? mappingShippingDetailViewModel(shippingDomain.getDetailDomain()) : null);
    }

    private ShippingDetailViewModel mappingShippingDetailViewModel(ShippingDetailDomain shippingDetailDomain) {
        return new ShippingDetailViewModel(
                shippingDetailDomain.getId(),
                shippingDetailDomain.getName());
    }

    private List<StatusViewModel> mappingStatusViewModel(List<StatusDomain> statusDomains) {
        List<StatusViewModel> statusViewModelList = new ArrayList<>();
        for (StatusDomain statusDomain : statusDomains) {
            StatusViewModel statusViewModel = new StatusViewModel(
                    statusDomain.isDelivered(),
                    statusDomain.getName(),
                    statusDomain.getStatusTroubleDomainList() != null ? mappingStatusTroubleViewModel(statusDomain.getStatusTroubleDomainList()) : null,
                    statusDomain.getStatusInfoDomain() !=null ? mappingStatusInfoViewModel(statusDomain.getStatusInfoDomain()) : null);
            statusViewModelList.add(statusViewModel);
        }
        return statusViewModelList;
    }

    private List<StatusTroubleViewModel> mappingStatusTroubleViewModel(List<StatusTroubleDomain> statusTroubleDomains) {
        List<StatusTroubleViewModel> statusTroubleViewModelList = new ArrayList<>();
        for (StatusTroubleDomain statusTroubleDomain : statusTroubleDomains) {
            StatusTroubleViewModel statusTroubleViewModel = new StatusTroubleViewModel(
                    statusTroubleDomain.getId(),
                    statusTroubleDomain.getName());
            statusTroubleViewModelList.add(statusTroubleViewModel);
        }
        return statusTroubleViewModelList;
    }

    private StatusInfoViewModel mappingStatusInfoViewModel(StatusInfoDomain statusInfoDomain) {
        return new StatusInfoViewModel(
                statusInfoDomain.isShow(),
                statusInfoDomain.getDate());
    }

}
