package com.tokopedia.seller.opportunity.presenter.subscriber;

import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.entity.replacement.opportunitydata.DetailCancelRequest;
import com.tokopedia.core.network.entity.replacement.opportunitydata.DetailPreorder;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityList;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderCustomer;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDeadline;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDestination;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderDetail;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderHistory;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderLast;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderPayment;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderProduct;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderShipment;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderShop;
import com.tokopedia.core.network.entity.replacement.opportunitydata.Paging;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.DetailCancelRequestViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.DetailPreorderViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityViewModel;
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

import rx.Subscriber;

/**
 * Created by nisie on 4/5/17.
 */

public class GetOpportunitySubscriber extends Subscriber<OpportunityModel> {

    private final OpportunityListView viewListener;

    public GetOpportunitySubscriber(OpportunityListView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetOpportunity(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(OpportunityModel opportunityModel) {

        if (opportunityModel.isSuccess())
            viewListener.onSuccessGetOpportunity(mappingToViewModel(opportunityModel));
        else
            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_unknown));

    }

    public static OpportunityViewModel mappingToViewModel(OpportunityModel opportunityModel) {
        OpportunityViewModel viewModel = new OpportunityViewModel();
        viewModel.setListOpportunity(
                mappingOpportunityViewModel(
                        opportunityModel.getOpportunityData().getOpportunityList()));
        viewModel.setPagingHandlerModel(
                mappingPagingModel(
                        opportunityModel.getOpportunityData().getPaging()));
        return viewModel;
    }

    public static PagingHandler.PagingHandlerModel mappingPagingModel(Paging paging) {
        PagingHandler.PagingHandlerModel pagingViewModel = new PagingHandler.PagingHandlerModel();
        pagingViewModel.setUriNext(paging.getUriNext());
        pagingViewModel.setUriPrevious(paging.getUriPrevious());
        pagingViewModel.setStartIndex(1);
        return pagingViewModel;
    }

    public static ArrayList<OpportunityItemViewModel> mappingOpportunityViewModel(List<OpportunityList> listOpportunity) {
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
            opportunityItemViewModel.setReplacementMultiplierColor(opportunityItem
                    .getReplacementMultiplierColor() == null ? "" : opportunityItem
                    .getReplacementMultiplierColor());
            opportunityItemViewModel.setReplacementMultiplierText(opportunityItem
                    .getReplacementMultiplierValueStr() == null ? "" : opportunityItem.getReplacementMultiplierValueStr());
            opportunityItemViewModel.setReplacementMultiplierValue(opportunityItem.getReplacementMultiplierValue());
            opportunityItemViewModel.setReplacementTnc(opportunityItem.getReplacementTnc());
            list.add(opportunityItemViewModel);
        }
        return list;
    }

    public static OrderDestinationViewModel getOrderDestinationViewModel(OrderDestination orderDestination) {
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

    public static List<OrderHistoryViewModel> getOrderHistoryViewModel(List<OrderHistory> listOrderHistory) {
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

    public static OrderLastViewModel getOrderLastViewModel(OrderLast orderLast) {
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

    public static OrderShipmentViewModel getOrderShipmentViewModel(OrderShipment orderShipment) {
        OrderShipmentViewModel orderShipmentViewModel = new OrderShipmentViewModel();
        orderShipmentViewModel.setShipmentLogo(orderShipment.getShipmentLogo());
        orderShipmentViewModel.setShipmentPackageId(orderShipment.getShipmentPackageId());
        orderShipmentViewModel.setShipmentId(orderShipment.getShipmentId());
        orderShipmentViewModel.setShipmentProduct(orderShipment.getShipmentProduct());
        orderShipmentViewModel.setShipmentName(orderShipment.getShipmentName());
        orderShipmentViewModel.setSameDay(orderShipment.getSameDay());
        return orderShipmentViewModel;
    }

    public static List<OrderProductViewModel> getOrderProductsViewModel(List<OrderProduct> orderProducts) {
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
            orderProductViewModel.setProductNotes(orderProduct.getProductNotes());
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

    public static OrderShopViewModel getOrderShopViewModel(OrderShop orderShop) {
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

    public static OrderDeadlineViewModel getOrderDeadlineViewModel(OrderDeadline orderDeadline) {
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

    public static OrderDetailViewModel getOrderDetailViewModel(OrderDetail orderDetail) {
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

    public static DetailCancelRequestViewModel getDetailCancelRequestViewModel(DetailCancelRequest detailCancelRequest) {
        DetailCancelRequestViewModel detailCancelRequestViewModel = new DetailCancelRequestViewModel();
        detailCancelRequestViewModel.setCancelRequest(detailCancelRequest.getCancelRequest());
        detailCancelRequestViewModel.setReason(detailCancelRequest.getReason());
        detailCancelRequestViewModel.setReasonTime(detailCancelRequest.getReasonTime());
        return detailCancelRequestViewModel;
    }

    public static DetailPreorderViewModel getDetailPreorderViewModel(DetailPreorder detailPreorder) {
        DetailPreorderViewModel detailPreorderViewModel = new DetailPreorderViewModel();
        detailPreorderViewModel.setPreorderStatus(detailPreorder.getPreorderStatus());
        detailPreorderViewModel.setPreorderProcessTimeType(String.valueOf(detailPreorder.getPreorderProcessTimeType()));
        detailPreorderViewModel.setPreorderProcessTimeTypeString(String.valueOf(detailPreorder.getPreorderProcessTimeTypeString()));
        detailPreorderViewModel.setPreorderProcessTime(String.valueOf(detailPreorder.getPreorderProcessTime()));
        return detailPreorderViewModel;
    }

    public static OrderPaymentViewModel getOrderPaymentViewModel(OrderPayment orderPayment) {
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

    public static OrderCustomerViewModel getOrderCustomerViewModel(OrderCustomer orderCustomer) {
        OrderCustomerViewModel orderCustomerViewModel = new OrderCustomerViewModel();
        orderCustomerViewModel.setCustomerId(orderCustomer.getCustomerId());
        orderCustomerViewModel.setCustomerImage(orderCustomer.getCustomerImage());
        orderCustomerViewModel.setCustomerName(orderCustomer.getCustomerName());
        orderCustomerViewModel.setCustomerUrl(orderCustomer.getCustomerUrl());
        return orderCustomerViewModel;
    }
}
