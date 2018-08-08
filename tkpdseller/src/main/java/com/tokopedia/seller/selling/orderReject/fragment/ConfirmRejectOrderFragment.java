package com.tokopedia.seller.selling.orderReject.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.orderReject.ConfirmRejectOrderActivity;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter.Type;
import com.tokopedia.seller.selling.orderReject.adapter.SimpleDividerItemDecoration;
import com.tokopedia.seller.selling.orderReject.model.DataResponseReject;
import com.tokopedia.seller.selling.orderReject.model.ModelEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ModelEditPrice;
import com.tokopedia.seller.selling.orderReject.model.ModelRejectOrder;
import com.tokopedia.seller.selling.presenter.listener.SellingView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erry on 6/3/2016.
 */
public class ConfirmRejectOrderFragment extends Fragment implements SellingView {

    public static final String PRODUCT_DETAIL_KEY = "product_detail_key";

    TextView reasonText;
    RecyclerView recyclerView;
    TextView confirmButton;
    ProgressBar progressBar;
    TextView titleEditProduct;

    private ProductListAdapter listAdapter;
    TkpdProgressDialog progressDialog;
    List<OrderProduct> orderProductList = new ArrayList<>();
    OrderShippingList orderShippingList;
    Type type;
    String orderId;
    private Bundle bundle;

    public static ConfirmRejectOrderFragment newInstance(OrderShippingList orderShippingList, String reason, Type type, String orderid) {

        Bundle args = new Bundle();
        args.putParcelable(ConfirmRejectOrderActivity.ORDERS, Parcels.wrap(orderShippingList));
        args.putString(ConfirmRejectOrderActivity.REASON, reason);
        args.putSerializable(ConfirmRejectOrderActivity.TYPE, type);
        args.putString(ConfirmRejectOrderActivity.ORDER_ID, orderid);
        ConfirmRejectOrderFragment fragment = new ConfirmRejectOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_reject_order, container, false);
        reasonText = (TextView) view.findViewById(R.id.reason);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        confirmButton = (TextView) view.findViewById(R.id.confirm_button);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);
        titleEditProduct = (TextView) view.findViewById(R.id.title_edit_product);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirm();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderId = getArguments().getString(ConfirmRejectOrderActivity.ORDER_ID);
        type = (Type) getArguments().getSerializable(ConfirmRejectOrderActivity.TYPE);
        orderShippingList = Parcels.unwrap(getArguments().getParcelable(ConfirmRejectOrderActivity.ORDERS));
        orderProductList = orderShippingList.getOrderProducts();
        listAdapter = new ProductListAdapter(getActivity(), type, orderProductList);
        setTitleEditProduct();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        reasonText.setText(getArguments().getString(ConfirmRejectOrderActivity.REASON));
        recyclerView.setVisibility(View.GONE);
        getProductDetail();
    }

    private void setTitleEditProduct() {
        switch (type){
            case stock:
                titleEditProduct.setText(getString(R.string.title_edit_stok_reject_order));
                break;
            case varian:
                titleEditProduct.setText(getString(R.string.title_edit_product_reject_order));
                break;
            case price:
                titleEditProduct.setText(getString(R.string.title_edit_price_reject_order));
                break;
            default:
                titleEditProduct.setText(getString(R.string.title_edit_stok_reject_order));
                break;
        }
    }

    private void getProductDetail() {
        bundle = new Bundle();
        bundle.putParcelable(PRODUCT_DETAIL_KEY, Parcels.wrap(orderProductList));
        ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.GET_PRODUCT_FORM_EDIT, bundle);
    }

    public void onConfirm() {
        switch (type) {
            case stock:
                ModelRejectOrder modelRejectOrder = new ModelRejectOrder();
                modelRejectOrder.setAction_type("reject");
                modelRejectOrder.setReason_code("1"); // 1 is out of stock
                modelRejectOrder.setList_product_id(listAdapter.getStockEmptyList());
                modelRejectOrder.setOrder_id(orderId);
                bundle = new Bundle();
                bundle.putParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER, bundle);
                break;
            case varian:
                modelRejectOrder = new ModelRejectOrder();
                modelRejectOrder.setAction_type("reject");
                modelRejectOrder.setReason_code("2"); // 2 is variant unavailable
                modelRejectOrder.setList_product_id(listAdapter.getStockEmptyList());
                modelRejectOrder.setOrder_id(orderId);
                bundle = new Bundle();
                bundle.putParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                List<ModelEditDescription> modelEditDescriptions = listAdapter.getModelEditDescriptions();
                if (modelEditDescriptions.size() <= 0) {
                    ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER, bundle);
                } else {
                    bundle.putParcelable(ModelEditDescription.MODEL_EDIT_DESCRIPTION_KEY, Parcels.wrap(modelEditDescriptions));
                    ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_WITH_DESCRIPTION, bundle);
                }
                break;
            case price:
                modelRejectOrder = new ModelRejectOrder();
                modelRejectOrder.setAction_type("reject");
                modelRejectOrder.setReason_code("3"); // 3 is wrong price or weight
                modelRejectOrder.setList_product_id(listAdapter.getStockEmptyList());
                modelRejectOrder.setOrder_id(orderId);
                bundle = new Bundle();
                bundle.putParcelable(ModelRejectOrder.MODEL_REJECT_ORDER_KEY, Parcels.wrap(modelRejectOrder));
                List<ModelEditPrice> modelEditPrices = listAdapter.getModelEditPrice();
                if (modelEditPrices.size() <= 0) {
                    ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER, bundle);
                } else {
                    bundle.putParcelable(ModelEditPrice.MODEL_EDIT_PRICE_KEY, Parcels.wrap(modelEditPrices));
                    ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_WITH_PRICE, bundle);
                }
                break;
        }
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case SellingService.REJECT_ORDER:
            case SellingService.REJECT_ORDER_WITH_PRICE:
            case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
                DataResponseReject result = Parcels.unwrap(data.getParcelable(DataResponseReject.MODEL_DATA_REJECT_RESPONSE_KEY));
                if (result.getIsSuccess() == 1) {
                    progressDialog.dismiss();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                break;
            case SellingService.GET_PRODUCT_FORM_EDIT:
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                orderProductList.clear();
                List<OrderProduct> orderProducts = Parcels.unwrap(data.getParcelable(PRODUCT_DETAIL_KEY));
                orderProductList.addAll(orderProducts);
                listAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        progressDialog.dismiss();
        String error = (String) data[0];
        switch (type) {
            case SellingService.REJECT_ORDER_WITH_DESCRIPTION:
                NetworkErrorHelper.showDialogCustomMSG(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_WITH_DESCRIPTION, bundle);
                        }
                    }
                }, error);
                break;
            case SellingService.REJECT_ORDER_WITH_PRICE:
                NetworkErrorHelper.showDialogCustomMSG(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER_WITH_PRICE, bundle);
                        }
                    }
                }, error);
                break;
            case SellingService.REJECT_ORDER:
                NetworkErrorHelper.showDialogCustomMSG(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.REJECT_ORDER, bundle);
                        }
                    }
                }, error);
                break;
            case SellingService.GET_PRODUCT_FORM_EDIT:
                progressBar.setVisibility(View.GONE);
                SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.get_product_detail_failed), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((ConfirmRejectOrderActivity) getActivity()).ConfirmRejectOrder(SellingService.GET_PRODUCT_FORM_EDIT, bundle);
                        }
                    }
                });
                snackbarRetry.showRetrySnackbar();
                break;
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        progressDialog.dismiss();
        String error = (String) data[0];
        if (error != null) {
            showDialogError(getActivity(), error);
        }
    }

    private void showDialogError(Context context, String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        msg.setText(error);
        dialog.setView(promptsView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }
}
