package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.entity.replacement.opportunitydata.DetailCancelRequest;
import com.tokopedia.core.network.entity.replacement.opportunitydata.DetailPreorder;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityData;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityList;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderCustomer;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDeadline;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDestination;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDetail;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderLast;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderPayment;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderProduct;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderShipment;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderShop;
import com.tokopedia.core.network.entity.replacement.opportunitydata.Paging;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.DetailCancelRequestViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.DetailPreorderViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityListPageViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderCustomerViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderDeadlineViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderDestinationViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderDetailViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderHistoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderLastViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderPaymentViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderProductViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderShipmentViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OrderShopViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityListUseCase extends UseCase<OpportunityListPageViewModel> {

    public static final String USER_ID = "user_id";
    public static final String PER_PAGE = "per_page";
    public static final String PAGE = "page";
    public static final String CAT_1 = "cat_1";
    public static final String CAT_2 = "cat_2";
    public static final String CAT_3 = "cat_3";
    public static final String SHIP_TYPE = "ship_type";
    public static final String ORDER_BY = "order_by";
    public static final String SHOP_ID = "shop_id";
    public static final String DEVICE_ID = "device_id";
    public static final String OS_TYPE = "os_type";


    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final GetOpportunityUseCase getOpportunityUseCase;
    private final GetOpportunityFilterUseCase getOpportunityFilterUseCase;

    public OpportunityListUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  ReplacementRepository repository,
                                  GetOpportunityUseCase getOpportunityUseCase,
                                  GetOpportunityFilterUseCase getOpportunityFilterUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.getOpportunityUseCase = getOpportunityUseCase;
        this.getOpportunityFilterUseCase = getOpportunityFilterUseCase;
    }

    @Override
    public Observable<OpportunityListPageViewModel> createObservable(RequestParams params) {
        return Observable.zip(
                getOpportunityListObservable(params),
                getOpportunityFilterObservable(params),
                new Func2<OpportunityModel,
                        OpportunityCategoryModel,
                        OpportunityListPageViewModel>() {
                    @Override
                    public OpportunityListPageViewModel call(OpportunityModel opportunityModel,
                                                             OpportunityCategoryModel opportunityCategoryModel) {
                        return getCombinedPageData(opportunityModel, opportunityCategoryModel);
                    }
                });
//        return Observable.just(getCombinedPageData(new OpportunityModel(), new OpportunityCategoryModel()));

    }

    private OpportunityListPageViewModel getCombinedPageData(OpportunityModel opportunityModel,
                                                             OpportunityCategoryModel opportunityCategoryModel) {

        OpportunityListPageViewModel viewModel = new OpportunityListPageViewModel();
        viewModel.setOpportunityViewModel(getOpportunityViewModel(opportunityModel));
        viewModel.setListCategory(getListCategory(opportunityCategoryModel));
        viewModel.setListShippingType(getListShippingType(opportunityCategoryModel));
        viewModel.setListSortingType(getListSortingType(opportunityCategoryModel));
        return viewModel;
    }

    private ArrayList<SortingTypeViewModel> getListSortingType(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<SortingTypeViewModel> list = new ArrayList<>();
        list.add(new SortingTypeViewModel("Jatuh Tempo", 1));
        list.add(new SortingTypeViewModel("Nilai Transaksi Tertinggi", 2));
        list.add(new SortingTypeViewModel("Nilai Transaksi Terendah", 3));

        return list;
    }

    private ArrayList<ShippingTypeViewModel> getListShippingType(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<ShippingTypeViewModel> list = new ArrayList<>();
        list.add(new ShippingTypeViewModel("Same Day", 1));
        list.add(new ShippingTypeViewModel("Next Day", 2));
        list.add(new ShippingTypeViewModel("Reguler Day", 3));

        return list;
    }

    private ArrayList<CategoryViewModel> getListCategory(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<CategoryViewModel> list = new ArrayList<>();
        CategoryViewModel cat1 = new CategoryViewModel();
        cat1.setCategoryId(1);
        cat1.setCategoryName("Category 1");
        cat1.setTreeLevel(1);

        CategoryViewModel cat2 = new CategoryViewModel();
        cat2.setCategoryId(2);
        cat2.setCategoryName("Category 2");
        cat2.setTreeLevel(2);

        ArrayList<CategoryViewModel> list2 = new ArrayList<>();
        CategoryViewModel cat11 = new CategoryViewModel();
        cat11.setCategoryId(11);
        cat11.setCategoryName("Category 1-1");
        cat11.setTreeLevel(2);
        CategoryViewModel cat12 = new CategoryViewModel();
        cat12.setCategoryId(12);
        cat12.setCategoryName("Category 1-2");
        cat12.setTreeLevel(2);

        list2.add(cat11);
        list2.add(cat12);

        cat1.setListChild(list2);


        list.add(cat1);
        list.add(cat2);

        return list;
    }

    private OpportunityViewModel getOpportunityViewModel(OpportunityModel opportunityModel) {

        OpportunityViewModel viewModel = new OpportunityViewModel();
        viewModel.setListOpportunity(
                getListOpportunityViewModel(opportunityModel.getOpportunityData().getOpportunityList()));
        viewModel.setPagingHandlerModel(
                getPagingModel(opportunityModel.getOpportunityData().getPaging()));
        return viewModel;
    }

    private PagingHandler.PagingHandlerModel getPagingModel(Paging paging) {
        PagingHandler.PagingHandlerModel pagingViewModel = new PagingHandler.PagingHandlerModel();
        pagingViewModel.setUriNext(paging.getUriNext());
        pagingViewModel.setUriPrevious(paging.getUriPrevious());
        pagingViewModel.setStartIndex(1);
        return pagingViewModel;
    }

    private ArrayList<OpportunityItemViewModel> getListOpportunityViewModel(List<OpportunityList> listOpportunity) {
        ArrayList<OpportunityItemViewModel> list = new ArrayList<>();

        for (OpportunityList opportunityItem : listOpportunity) {
            OpportunityItemViewModel opportunityItemViewModel = new OpportunityItemViewModel();
            opportunityItemViewModel.setOrderReplacementId(opportunityItem.getOrderReplacementId());
            opportunityItemViewModel.setOrderOrderId(opportunityItem.getOrderOrderId());
            opportunityItemViewModel.setOrderPaymentAt(opportunityItem.getOrderPaymentAt());
            opportunityItemViewModel.setOrderExpiredAt(opportunityItem.getOrderExpiredAt());
            opportunityItemViewModel.setOrderCashbackIdr(opportunityItem.getOrderCashbackIdr());
            opportunityItemViewModel.setOrderCashback(opportunityItem.getOrderCashback());
            opportunityItemViewModel.setOrderCustomer(getOrderCustomerViewModel(opportunityItem.getOrderCustomer()));
            opportunityItemViewModel.setOrderPayment(getOrderPaymentViewModel(opportunityItem.getOrderPayment()));
            opportunityItemViewModel.setOrderDetail(getOrderDetailViewModel(opportunityItem.getOrderDetail()));
            opportunityItemViewModel.setOrderDeadline(getOrderDeadlineViewModel(opportunityItem.getOrderDeadline()));
            opportunityItemViewModel.setOrderShop(getOrderShopViewModel(opportunityItem.getOrderShop()));
            opportunityItemViewModel.setOrderProducts(getOrderProductsViewModel(opportunityItem.getOrderProducts()));
            opportunityItemViewModel.setOrderShipment(getOrderShipmentViewModel(opportunityItem.getOrderShipment()));
            opportunityItemViewModel.setOrderLast(getOrderLastViewModel(opportunityItem.getOrderLast()));
            opportunityItemViewModel.setOrderHistory(getOrderHistoryViewModel(opportunityItem.getOrderHistory()));
            opportunityItemViewModel.setOrderDestination(getOrderDestinationViewModel(opportunityItem.getOrderDestination()));
            list.add(opportunityItemViewModel);
        }
        return list;
    }

    private OrderDestinationViewModel getOrderDestinationViewModel(OrderDestination orderDestination) {
        OrderDestinationViewModel orderDestinationViewModel = new OrderDestinationViewModel();
        orderDestinationViewModel.setReceiverPhoneIsTokopedia(orderDestination.getReceiverPhoneIsTokopedia());
        orderDestinationViewModel.setReceiverName(orderDestination.getReceiverName());
        orderDestinationViewModel.setAddressCountry(orderDestination.getAddressCountry());
        orderDestinationViewModel.setAddressPostal(orderDestination.getAddressPostal());
        orderDestinationViewModel.setAddressDistrict(orderDestination.getAddressDistrict());
        orderDestinationViewModel.setReceiverPhone(orderDestination.getReceiverPhone());
        orderDestinationViewModel.setAddressStreet(orderDestination.getAddressStreet());
        orderDestinationViewModel.setAddressCity(orderDestination.getAddressCity());
        orderDestinationViewModel.setAddressProvince(orderDestination.getAddressProvince());
        return orderDestinationViewModel;
    }

    private List<OrderHistoryViewModel> getOrderHistoryViewModel(List<OrderHistory> listOrderHistory) {
        List<OrderHistoryViewModel> list = new ArrayList<>();
        for (OrderHistory orderHistory : listOrderHistory) {
            OrderHistoryViewModel orderHistoryViewModel = new OrderHistoryViewModel();
            orderHistoryViewModel.setHistoryStatusDate(orderHistory.getHistoryStatusDate());
            orderHistoryViewModel.setHistoryStatusDateFull(orderHistory.getHistoryStatusDateFull());
            orderHistoryViewModel.setHistoryOrderStatus(orderHistory.getHistoryOrderStatus());
            orderHistoryViewModel.setHistoryComments(orderHistory.getHistoryComments());
            orderHistoryViewModel.setHistoryActionBy(orderHistory.getHistoryActionBy());
            orderHistoryViewModel.setHistoryBuyerStatus(orderHistory.getHistoryBuyerStatus());
            orderHistoryViewModel.setHistorySellerStatus(orderHistory.getHistorySellerStatus());
            list.add(orderHistoryViewModel);
        }
        return list;
    }

    private OrderLastViewModel getOrderLastViewModel(OrderLast orderLast) {
        OrderLastViewModel orderLastViewModel = new OrderLastViewModel();
        orderLastViewModel.setLastOrderId(orderLast.getLastOrderId());
        orderLastViewModel.setLastShipmentId(orderLast.getLastShipmentId());
        orderLastViewModel.setLastEstShippingLeft(String.valueOf(orderLast.getLastEstShippingLeft()));
        orderLastViewModel.setLastOrderStatus(orderLast.getLastOrderStatus());
        orderLastViewModel.setLastStatusDate(orderLast.getLastStatusDate());
        orderLastViewModel.setLastPodCode(String.valueOf(orderLast.getLastPodCode()));
        orderLastViewModel.setLastPodDesc(orderLast.getLastPodDesc());
        orderLastViewModel.setLastShippingRefNum(orderLast.getLastShippingRefNum());
        orderLastViewModel.setLastPodReceiver(String.valueOf(orderLast.getLastPodReceiver()));
        orderLastViewModel.setLastComments(orderLast.getLastComments());
        orderLastViewModel.setLastBuyerStatus(orderLast.getLastBuyerStatus());
        orderLastViewModel.setLastStatusDateWib(orderLast.getLastStatusDateWib());
        orderLastViewModel.setLastSellerStatus(orderLast.getLastSellerStatus());
        return orderLastViewModel;
    }

    private OrderShipmentViewModel getOrderShipmentViewModel(OrderShipment orderShipment) {
        OrderShipmentViewModel orderShipmentViewModel = new OrderShipmentViewModel();
        orderShipmentViewModel.setShipmentLogo(orderShipment.getShipmentLogo());
        orderShipmentViewModel.setShipmentPackageId(orderShipment.getShipmentPackageId());
        orderShipmentViewModel.setShipmentId(orderShipment.getShipmentId());
        orderShipmentViewModel.setShipmentProduct(orderShipment.getShipmentProduct());
        orderShipmentViewModel.setShipmentName(orderShipment.getShipmentName());
        orderShipmentViewModel.setSameDay(orderShipment.getSameDay());
        return orderShipmentViewModel;
    }

    private List<OrderProductViewModel> getOrderProductsViewModel(List<OrderProduct> orderProducts) {
        List<OrderProductViewModel> list = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductViewModel orderProductViewModel = new OrderProductViewModel();
            orderProductViewModel.setOrderDeliverQuantity(orderProduct.getOrderDeliverQuantity());
            orderProductViewModel.setProductWeightUnit(orderProduct.getProductWeightUnit());
            orderProductViewModel.setOrderDetailId(orderProduct.getOrderDetailId());
            orderProductViewModel.setProductStatus(orderProduct.getProductStatus());
            orderProductViewModel.setProductId(orderProduct.getProductId());
            orderProductViewModel.setProductCurrentWeight(orderProduct.getProductCurrentWeight());
            orderProductViewModel.setProductPicture(orderProduct.getProductPicture());
            orderProductViewModel.setProductPrice(orderProduct.getProductPrice());
            orderProductViewModel.setProductDescription(orderProduct.getProductDescription());
            orderProductViewModel.setProductNormalPrice(orderProduct.getProductNormalPrice());
            orderProductViewModel.setProductPriceCurrency(String.valueOf(orderProduct.getProductPriceCurrency()));
            orderProductViewModel.setProductNotes(String.valueOf(orderProduct.getProductNotes()));
            orderProductViewModel.setOrderSubtotalPrice(orderProduct.getOrderSubtotalPrice());
            orderProductViewModel.setProductQuantity(orderProduct.getProductQuantity());
            orderProductViewModel.setProductWeight(orderProduct.getProductWeight());
            orderProductViewModel.setOrderSubtotalPriceIdr(orderProduct.getOrderSubtotalPriceIdr());
            orderProductViewModel.setProductRejectQuantity(orderProduct.getProductRejectQuantity());
            orderProductViewModel.setProductUrl(orderProduct.getProductUrl());
            orderProductViewModel.setProductName(orderProduct.getProductName());
            list.add(orderProductViewModel);
        }
        return list;
    }

    private OrderShopViewModel getOrderShopViewModel(OrderShop orderShop) {
        OrderShopViewModel orderShopViewModel = new OrderShopViewModel();
        orderShopViewModel.setAddressPostal(orderShop.getAddressPostal());
        orderShopViewModel.setAddressDistrict(orderShop.getAddressDistrict());
        orderShopViewModel.setAddressCity(orderShop.getAddressCity());
        orderShopViewModel.setAddressStreet(orderShop.getAddressStreet());
        orderShopViewModel.setShipperPhone(orderShop.getShipperPhone());
        orderShopViewModel.setAddressCountry(String.valueOf(orderShop.getAddressCountry()));
        orderShopViewModel.setAddressProvince(orderShop.getAddressProvince());
        return orderShopViewModel;
    }

    private OrderDeadlineViewModel getOrderDeadlineViewModel(OrderDeadline orderDeadline) {
        OrderDeadlineViewModel orderDeadlineViewModel = new OrderDeadlineViewModel();
        orderDeadlineViewModel.setDeadlineProcessDayLeft(String.valueOf(orderDeadline.getDeadlineProcessDayLeft()));
        orderDeadlineViewModel.setDeadlineProcessHourLeft(String.valueOf(orderDeadline.getDeadlineProcessHourLeft()));
        orderDeadlineViewModel.setDeadlineProcess(orderDeadline.getDeadlineProcess());
        orderDeadlineViewModel.setDeadlinePoProcessDayLeft(String.valueOf(orderDeadline.getDeadlinePoProcessDayLeft()));
        orderDeadlineViewModel.setDeadlineShippingDayLeft(orderDeadlineViewModel.getDeadlineShippingDayLeft());
        orderDeadlineViewModel.setDeadlineShippingHourLeft(orderDeadlineViewModel.getDeadlineShippingHourLeft());
        orderDeadlineViewModel.setDeadlineShipping(orderDeadline.getDeadlineShipping());
        orderDeadlineViewModel.setDeadlineFinishDayLeft(String.valueOf(orderDeadline.getDeadlineFinishDayLeft()));
        orderDeadlineViewModel.setDeadlineFinishHourLeft(String.valueOf(orderDeadline.getDeadlineFinishHourLeft()));
        orderDeadlineViewModel.setDeadlineFinishDate(String.valueOf(orderDeadline.getDeadlineFinishDate()));
        orderDeadlineViewModel.setDeadlineColor(orderDeadline.getDeadlineColor());
        return orderDeadlineViewModel;
    }

    private OrderDetailViewModel getOrderDetailViewModel(OrderDetail orderDetail) {
        OrderDetailViewModel orderDetailViewModel = new OrderDetailViewModel();
        orderDetailViewModel.setDetailInsurancePrice(orderDetail.getDetailInsurancePrice());
        orderDetailViewModel.setDetailOpenAmount(orderDetail.getDetailOpenAmount());
        orderDetailViewModel.setDetailDropshipName(String.valueOf(orderDetail.getDetailDropshipName()));
        orderDetailViewModel.setDetailTotalAddFee(String.valueOf(orderDetail.getDetailTotalAddFee()));
        orderDetailViewModel.setDetailPartialOrder(orderDetail.getDetailPartialOrder());
        orderDetailViewModel.setDetailQuantity(orderDetail.getDetailQuantity());
        orderDetailViewModel.setDetailProductPriceIdr(orderDetail.getDetailProductPriceIdr());
        orderDetailViewModel.setDetailInvoice(orderDetail.getDetailInvoice());
        orderDetailViewModel.setDetailShippingPriceIdr(orderDetail.getDetailShippingPriceIdr());
        orderDetailViewModel.setDetailFreeReturn(String.valueOf(orderDetail.getDetailFreeReturn()));
        orderDetailViewModel.setDetailPdfPath(orderDetail.getDetailPdfPath());
        orderDetailViewModel.setDetailFreeReturnMsg(orderDetail.getDetailFreeReturnMsg());
        orderDetailViewModel.setDetailAdditionalFeeIdr(orderDetail.getDetailAdditionalFeeIdr());
        orderDetailViewModel.setDetailProductPrice(orderDetail.getDetailProductPrice());
        orderDetailViewModel.setDetailPreorder(getDetailPreorderViewModel(orderDetail.getDetailPreorder()));
        orderDetailViewModel.setDetailCancelRequest(getDetailCancelRequestViewModel(orderDetail.getDetailCancelRequest()));
        orderDetailViewModel.setDetailForceInsurance(String.valueOf(orderDetail.getDetailForceInsurance()));
        orderDetailViewModel.setDetailOpenAmountIdr(orderDetail.getDetailOpenAmountIdr());
        orderDetailViewModel.setDetailAdditionalFee(String.valueOf(orderDetail.getDetailAdditionalFee()));
        orderDetailViewModel.setDetailDropshipTelp(String.valueOf(orderDetail.getDetailDropshipTelp()));
        orderDetailViewModel.setDetailOrderId(orderDetail.getDetailOrderId());
        orderDetailViewModel.setDetailTotalAddFeeIdr(orderDetail.getDetailTotalAddFeeIdr());
        orderDetailViewModel.setDetailOrderDate(orderDetail.getDetailOrderDate());
        orderDetailViewModel.setDetailShippingPrice(orderDetail.getDetailShippingPrice());
        orderDetailViewModel.setDetailPayDueDate(orderDetail.getDetailPayDueDate());
        orderDetailViewModel.setDetailTotalWeight(String.valueOf(orderDetail.getDetailTotalWeight()));
        orderDetailViewModel.setDetailInsurancePriceIdr(orderDetail.getDetailInsurancePriceIdr());
        orderDetailViewModel.setDetailPdfUri(orderDetail.getDetailPdfUri());
        orderDetailViewModel.setDetailShipRefNum(orderDetail.getDetailShipRefNum());
        orderDetailViewModel.setDetailPrintAddressUri(orderDetail.getDetailPrintAddressUri());
        orderDetailViewModel.setDetailPdf(orderDetail.getDetailPdf());
        orderDetailViewModel.setDetailOrderStatus(orderDetail.getDetailOrderStatus());
        return orderDetailViewModel;
    }

    private DetailCancelRequestViewModel getDetailCancelRequestViewModel(DetailCancelRequest detailCancelRequest) {
        DetailCancelRequestViewModel detailCancelRequestViewModel = new DetailCancelRequestViewModel();
        detailCancelRequestViewModel.setCancelRequest(detailCancelRequest.getCancelRequest());
        detailCancelRequestViewModel.setReason(detailCancelRequest.getReason());
        detailCancelRequestViewModel.setReasonTime(detailCancelRequest.getReasonTime());
        return detailCancelRequestViewModel;
    }

    private DetailPreorderViewModel getDetailPreorderViewModel(DetailPreorder detailPreorder) {
        DetailPreorderViewModel detailPreorderViewModel = new DetailPreorderViewModel();
        detailPreorderViewModel.setPreorderStatus(detailPreorder.getPreorderStatus());
        detailPreorderViewModel.setPreorderProcessTimeType(String.valueOf(detailPreorder.getPreorderProcessTimeType()));
        detailPreorderViewModel.setPreorderProcessTimeTypeString(String.valueOf(detailPreorder.getPreorderProcessTimeTypeString()));
        detailPreorderViewModel.setPreorderProcessTime(String.valueOf(detailPreorder.getPreorderProcessTime()));
        return detailPreorderViewModel;
    }

    private OrderPaymentViewModel getOrderPaymentViewModel(OrderPayment orderPayment) {
        OrderPaymentViewModel orderPaymentViewModel = new OrderPaymentViewModel();
        orderPaymentViewModel.setPaymentProcessDueDate(orderPayment.getPaymentProcessDueDate());
        orderPaymentViewModel.setPaymentKomisi(orderPayment.getPaymentKomisi());
        orderPaymentViewModel.setPaymentVerifyDate(orderPayment.getPaymentVerifyDate());
        orderPaymentViewModel.setPaymentShippingDueDate(orderPayment.getPaymentShippingDueDate());
        orderPaymentViewModel.setPaymentProcessDayLeft(orderPayment.getPaymentProcessDueDate());
        orderPaymentViewModel.setPaymentGatewayId(orderPayment.getPaymentGatewayId());
        orderPaymentViewModel.setPaymentShippingDayLeft(String.valueOf(orderPayment.getPaymentShippingDayLeft()));
        orderPaymentViewModel.setPaymentGatewayName(orderPayment.getPaymentGatewayName());
        return orderPaymentViewModel;
    }

    private OrderCustomerViewModel getOrderCustomerViewModel(OrderCustomer orderCustomer) {
        OrderCustomerViewModel orderCustomerViewModel = new OrderCustomerViewModel();
        orderCustomerViewModel.setCustomerId(orderCustomer.getCustomerId());
        orderCustomerViewModel.setCustomerImage(orderCustomer.getCustomerImage());
        orderCustomerViewModel.setCustomerName(orderCustomer.getCustomerName());
        orderCustomerViewModel.setCustomerUrl(orderCustomer.getCustomerUrl());
        return orderCustomerViewModel;
    }

    private Observable<OpportunityCategoryModel> getOpportunityFilterObservable(RequestParams requestParams) {
//        return getOpportunityFilterUseCase.createObservable(
//                getOpportunityFilterParam(requestParams));
        return Observable.just(new OpportunityCategoryModel());

    }

    private RequestParams getOpportunityFilterParam(RequestParams requestParams) {
        RequestParams filterParam = RequestParams.create();
        filterParam.putString(GetOpportunityFilterUseCase.USER_ID,
                requestParams.getString(OpportunityListUseCase.USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.SHOP_ID,
                requestParams.getString(OpportunityListUseCase.SHOP_ID,
                        SessionHandler.getShopID(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.DEVICE_ID,
                requestParams.getString(OpportunityListUseCase.DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.OS_TYPE,
                requestParams.getString(OpportunityListUseCase.OS_TYPE, "1"));
        return filterParam;
    }

    private Observable<OpportunityModel> getOpportunityListObservable(RequestParams requestParams) {

        return getOpportunityUseCase
                .createObservable(getOpportunityListParam(requestParams));
    }

    private RequestParams getOpportunityListParam(RequestParams requestParams) {
        RequestParams getListParam = RequestParams.create();

        getListParam.putString(GetOpportunityUseCase.USER_ID,
                requestParams.getString(OpportunityListUseCase.USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())));

        getListParam.putString(GetOpportunityUseCase.DEVICE_ID,
                requestParams.getString(OpportunityListUseCase.DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));

        getListParam.putString(GetOpportunityUseCase.OS_TYPE,
                requestParams.getString(OpportunityListUseCase.OS_TYPE, "1"));

        getListParam.putString(GetOpportunityUseCase.PER_PAGE,
                requestParams.getString(OpportunityListUseCase.PER_PAGE, "10"));

        getListParam.putString(GetOpportunityUseCase.PAGE,
                requestParams.getString(OpportunityListUseCase.PAGE, "1"));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_1, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_1,
                    requestParams.getString(OpportunityListUseCase.CAT_1, ""));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_2, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_2,
                    requestParams.getString(OpportunityListUseCase.CAT_2, ""));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_3, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_3,
                    requestParams.getString(OpportunityListUseCase.CAT_3, ""));

        if (!requestParams.getString(GetOpportunityUseCase.SHIP_TYPE, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.SHIP_TYPE,
                    requestParams.getString(OpportunityListUseCase.SHIP_TYPE, ""));

        if (!requestParams.getString(GetOpportunityUseCase.ORDER_BY, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.ORDER_BY,
                    requestParams.getString(OpportunityListUseCase.ORDER_BY, ""));

        return getListParam;

    }

}
