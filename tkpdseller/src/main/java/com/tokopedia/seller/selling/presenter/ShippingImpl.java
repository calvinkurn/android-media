package com.tokopedia.seller.selling.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.manage.people.address.activity.ManagePeopleAddressActivity;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.seller.ShippingConfirmationDetail;
import com.tokopedia.seller.facade.FacadeActionShopTransaction;
import com.tokopedia.seller.facade.FacadeShopTransaction;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
import com.tokopedia.seller.selling.SellingService;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingShipping;
import com.tokopedia.seller.selling.model.ModelParamSelling;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingData;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.seller.selling.view.listener.SellingTransaction;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Toped10 on 7/28/2016.
 */
public class ShippingImpl extends Shipping {

    public static final int JNE_SHIPPING = 1;
    public static final int TIKI_SHIPPING = 2;
    public static final int RPX_SHIPPING = 3;
    public static final int POS_INDONESIA_SHIPPING = 4;
    public static final int WAHANA_SHIPPING = 6;
    public static final int PANDU_SHIPPING = 8;
    public static final int FIRST_SHIPPING = 9;
    public static final String LIST_MODEL = "ListModel";
    public static final String LIST_CHECKED = "ListChecked";
    public static final int GOJEK_SHIPPING = 10;
    public static final int SICEPAT_SHIPPING = 11;
    public static final int NINJA_EXPRESS_SHIPPING = 12;
    public static final int GRAB_SHIPPING = 13;
    public static final int JNT = 14;

    private List<Model> modelList= new ArrayList<>();
    //    private List<Fragment> detailList;
    private List<Model> checkedList= new ArrayList<>();
    private boolean isLoading;

    private FacadeShopTransaction facade;
    private Context context;

    public ShippingImpl(ShippingView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(@NonNull Context context) {
        view.setAdapter();
        view.setListener();
        if(!isAfterRotate) {
            if (isAllowLoading()) {
                view.setRefreshPullEnabled(false);
            }
            initData();
        }
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        modelList = Parcels.unwrap(argument.getParcelable(LIST_MODEL));
        checkedList = Parcels.unwrap(argument.getParcelable(LIST_CHECKED));
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(LIST_MODEL, Parcels.wrap(modelList));
        argument.putParcelable(LIST_CHECKED, Parcels.wrap(checkedList));
    }

    @Override
    public void initDataInstance(Context context) {
        this.context = context;
        facade = FacadeShopTransaction.createInstance(context);
        view.initHandlerAndAdapter();
        checkValidationToSendGoogleAnalytic(view.getUserVisible(), context);
    }

    @Override
    public void getShippingList(boolean isVisibleToUser) {
        if (isVisibleToUser && isDataEmpty() && !isLoading) {
            getShippingList();
        }
    }

    @Override
    public void updateListDataChecked(int position, boolean selected) {
        if (position >= 0 && position < modelList.size()) {
            (modelList.get(position)).Checked = selected;
        }
        view.notifyDataSetChanged(modelList);
    }

    @Override
    public void updateListDataChecked(boolean b) {
        for (int i = 0; i < modelList.size(); i++) {
            modelList.get(i).Checked = false;
        }
        view.notifyDataSetChanged(modelList);
    }

    public int getServiceAgent() {
        switch (view.getSelectedShipping()) {
            case "JNE":
                return JNE_SHIPPING;
            case "TIKI":
                return TIKI_SHIPPING;
            case "RPX":
                return RPX_SHIPPING;
            case "Pos Indonesia":
                return POS_INDONESIA_SHIPPING;
            case "Wahana":
                return WAHANA_SHIPPING;
            case "Pandu":
                return PANDU_SHIPPING;
            case "First":
                return FIRST_SHIPPING;
            case "GO-JEK":
                return GOJEK_SHIPPING;
            case "SiCepat":
                return SICEPAT_SHIPPING;
            case "Ninja Xpress":
                return NINJA_EXPRESS_SHIPPING;
            case "Grab":
                return GRAB_SHIPPING;
            case "J&T":
                return JNT;
            default:
                return 0;
        }
    }

    @Override
    public void cancelShipping(EditText remark, int pos, Dialog dialog, Context context) {
        if (remark.length() == 0) {
            remark.setError(context.getString(R.string.error_field_required));
        } else if (remark.length() < 5 && remark.length() > 0) {
            remark.setError(context.getString(R.string.char_should_min_5));
        } else if (remark.length() >= 5) {
            actionCancelShipping(pos, remark.getText().toString(), context);
            dialog.dismiss();
        }
    }

    private void actionCancelShipping(int pos, String remark, Context context) {
        view.showProgressDialog();
        FacadeActionShopTransaction facadeCancel = FacadeActionShopTransaction.createInstance(context, modelList.get(pos).OrderId);
        facadeCancel.setCompositeSubscription(compositeSubscription);
        facadeCancel.cancelShipping(remark, onCancelListener());
    }

    private FacadeActionShopTransaction.OnConfirmMultiShippingListener onCancelListener() {
        return new FacadeActionShopTransaction.OnConfirmMultiShippingListener() {
            @Override
            public void onSuccess() {
                view.hideProfressDialog();
                doRefresh();
            }

            @Override
            public void onFailed() {
                view.hideProfressDialog();
                doRefresh();
            }
        };
    }

    @Override
    public void doRefresh() {
        if (!isLoading && view.getUserVisible()) {
            view.getPaging().resetPage();
            if (!view.isRefreshing()) {
                clearData();
                view.addLoadingFooter();
            }
            getShippingList();
        }
    }

    private void clearData() {
        modelList.clear();
        view.notifyDataSetChanged(modelList);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if (ValidationTextUtil.isValidSalesQuery(query)) {
            view.hideFilter();
            onRefreshHandler();
        } else {
            showToastMessage(context.getString(R.string.keyword_min_3_char));
        }
    }

    @Override
    public void onQueryTextChange(String newText) {
        if (newText.length() == 0)
            doRefresh();
    }

    private void showToastMessage(@NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private FacadeShopTransaction.GetShippingListener onGetShipping() {
        return new FacadeShopTransaction.GetShippingListener() {
            @Override
            public void OnSuccess(List<Model> model, OrderShippingData Result) {
                if (view.getPaging().getPage() == 1)
                    modelList.clear();
                view.getPaging().setNewParameter(Result.getPaging());
                modelList.addAll(model);
                view.notifyDataSetChanged(modelList);
                onFinishConnection();
                view.setRefreshPullEnabled(true);
                view.showFab();

            }

            @Override
            public void OnNoResult() {
                onFinishConnection();
                if (view.getPaging().getPage() == 1)
                    clearData();
                    view.addEmptyView();
//                if (modelList.size() == 0) {
//                    view.addNoResult();
//                }
                view.getPaging().setHasNext(false);
                view.setRefreshPullEnabled(true);
                view.showFab();
                view.removeRetry();
            }

            @Override
            public void OnError() {
                onFinishConnection();
                if (modelList.size() == 0) {
                    view.addRetry();
                    view.hideFab();
                } else {
                    NetworkErrorHelper.showSnackbar((Activity) context);
                }
                view.setRefreshPullEnabled(true);
            }

            @Override
            public void onNetworkTimeOut() {
                finishTimeout();
                if (isDataEmpty()) {
                    view.setRefreshPullEnabled(false);
                    view.addRetry();
                    view.hideFab();
                } else {
                    try {
                        CommonUtils.UniversalToast(context, context.getString(R.string.msg_connection_timeout_toast));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    view.setRefreshPullEnabled(true);
                    if (!view.isRefreshing()) {
                        view.addRetry();
                    }
                }
            }
        };
    }

    @Override
    public void requestRefNumDialog(final int pos, final Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setPadding((int) context.getResources().getDimension(R.dimen.padding_small),
                (int) context.getResources().getDimension(R.dimen.padding_small),
                (int) context.getResources().getDimension(R.dimen.padding_small),
                (int) context.getResources().getDimension(R.dimen.padding_small));
        input.setText(modelList.get(pos).RefNum);
        alert.setView(input);
        alert.setPositiveButton(context.getResources().getString(R.string.title_ok), null);
        alert.setNegativeButton(context.getResources().getString(R.string.title_cancel), null);
        final AlertDialog mAlertDialog = alert.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
                        if (input.getText().toString().length() > 17) {
                            input.setError(context.getString(R.string.error_receipt_number_max));
                        } else if (input.getText().toString().length() < 8) {
                            input.setError(context.getString(R.string.error_receipt_number_min));
                        } else if (!p.matcher(input.getText().toString()).matches()) {
                            input.setError(context.getString(R.string.error_receipt_number_format));
                        } else {
                            modelList.get(pos).RefNum = input.getText().toString();
                            modelList.get(pos).RefNum = input.getText().toString();
                            checkRefNumError(pos, input.getText().toString(), context);
                            ShippingImpl.this.view.notifyDataSetChanged(modelList);
                            if (mAlertDialog.isShowing()) mAlertDialog.dismiss();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    @Override
    public void onOpenDetail(int pos, Context context) {
        Intent intent = ((TransactionRouter)MainApplication
                .getAppContext()).goToOrderDetail(context, modelList.get(pos).OrderId);
        context.startActivity(intent);
    }

    private void finishTimeout() {
        isLoading = false;
        view.removeLoading();
        view.enableFilter();
        view.setRefreshing(false);
    }

    @Override
    public void onFinishConnection() {
        view.enableFilter();
        view.removeRetry();
        view.removeLoading();
        isLoading = false;
        view.removeEmpty();
        view.finishRefresh();
    }

    private boolean isDataEmpty() {
        try {
            return (view.getPaging().getPage() == 1 && !isLoading && modelList.size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onScrollView(boolean isLastItemVisible) {
        if (!isLoading && isLastItemVisible && view.getPaging().CheckNextPage()) {
            view.setRefreshPullEnabled(false);
            view.getPaging().nextPage();
            view.addLoadingFooter();
            getShippingList();
        }
    }

    @Override
    public void onMultiConfirm(ActionMode actionMode, List<Integer> selecteds) {
        actionMode.finish();

        checkedList.clear();
        for (int i = 0; i < selecteds.size(); i++) {
            checkedList.add(modelList.get(selecteds.get(i)));
        }

        if (isRefNumValid()) {
            Bundle bundle = new Bundle();
            List<ModelParamSelling> modelParamSellings = new ArrayList<>();
            for (int i = 0; i < checkedList.size(); i++) {
                ModelParamSelling modelParamSelling = new ModelParamSelling();
                modelParamSelling.setActionType("confirm");
                modelParamSelling.setOrderId(checkedList.get(i).OrderId);
                modelParamSelling.setRefNum(checkedList.get(i).RefNum);
                modelParamSellings.add(modelParamSelling);
            }
            bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSellings));
            ((SellingTransaction) context).SellingAction(SellingService.CONFIRM_MULTI_SHIPPING, bundle);
            view.clearMultiSelector();
        }
    }

    private boolean isRefNumValid() {
        for (int i = 0; i < checkedList.size(); i++) {
            checkRefNumError(modelList.indexOf(checkedList.get(i)), modelList.get(modelList.indexOf(checkedList.get(i))).RefNum, context);
        }
        view.notifyDataSetChanged(modelList);
        for (int i = 0; i < checkedList.size(); i++) {
            if (checkedList.get(i).ErrorRow)
                return false;
        }

        return true;
    }

    public void getShippingList() {
        view.setRefreshPullEnabled(true);
        isLoading = true;
        view.disableFilter();
        facade.setCompositeSubscription(compositeSubscription);
        facade.getShippingV4(view.getPaging(), view.getSearchInvoice(), view.getDeadline(), getServiceAgent(), onGetShipping());
    }

    private void checkRefNumError(int pos, String refNum, Context context) {
        if (refNum.length() < 8 || refNum.length() > 17) {
            modelList.get(pos).ErrorRow = true;
            modelList.get(pos).ErrorMsg = context.getString(R.string.error_receipt_number);
        } else {
            modelList.get(pos).ErrorMsg = "";
            modelList.get(pos).ErrorRow = false;
        }
    }

    @Override
    public void updateRefNumBarcode(int getBarcodePosition, String barcode) {
        if (getBarcodePosition >= 0 && getBarcodePosition < modelList.size()) {
            modelList.get(getBarcodePosition).RefNum = barcode;
            view.notifyDataSetChanged(modelList);
        }
    }

    @Override
    public void moveToDetail(int position) {
        if(modelList != null && position >= 0 && modelList.get(position) != null) {
            Intent intent = ((TransactionRouter)MainApplication
                    .getAppContext()).goToOrderDetail(context, modelList.get(position).OrderId);
            view.moveToDetailResult(intent, FragmentSellingShipping.REQUEST_CODE_PROCESS_RESULT);
        }
    }

    @Override
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {

        }
    }

    private void initData() {
        if (isAllowLoading()) {
            view.addLoadingFooter();
        }
        if (view.getUserVisible() && isDataEmpty() && !isLoading) {
            getShippingList();
        }
    }

    private boolean isAllowLoading() {
        return (isLoading || isDataEmpty());
    }

    @Override
    public void onRefreshHandler() {
        if (!isLoading) {
            view.removeRetry();
            doRefresh();
        }
    }

    @Parcel
    public static class Model {
        public String UserName;
        public String AvatarUrl;
        public String Deadline;
        public String Invoice;
        public String Komisi;
        public String OrderId;
        public String BuyerId;
        public String Pdf;
        public String PdfUri;
        public String ShippingPrice;
        public String Permission;
        public String RefNum;
        public String ErrorMsg;
        public String ReceiverName;
        public String Dest;
        public String Shipping;
        public String SenderDetail;
        public boolean Checked;
        public OrderShippingList orderShippingList;
        public boolean ErrorRow;
    }
}
