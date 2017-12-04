package com.tokopedia.inbox.rescenter.detailv2.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.detailv2.view.customdialog.TrackShippingDialog;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.AddressReturView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.AwbReturView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ButtonView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.DetailView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.HistoryView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ListProductView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.SolutionView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.StatusView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterFragmentImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.activity.ResCenterDiscussionActivity;
import com.tokopedia.inbox.rescenter.historyaction.HistoryActionActivity;
import com.tokopedia.inbox.rescenter.historyaddress.HistoryAddressActivity;
import com.tokopedia.inbox.rescenter.historyawb.HistoryShippingActivity;
import com.tokopedia.inbox.rescenter.product.ListProductActivity;
import com.tokopedia.inbox.rescenter.product.ProductDetailActivity;
import com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailResCenterFragment extends BaseDaggerFragment
        implements DetailResCenterFragmentView {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_VIEW_DATA = "view_data";
    private static final int REQUEST_EDIT_SOLUTION = 123;
    private static final int REQUEST_APPEAL_SOLUTION = 234;
    private static final int REQUEST_INPUT_SHIPPING = 345;
    private static final int REQUEST_EDIT_SHIPPING = 456;
    private static final int REQUEST_CHOOSE_ADDRESS = 678;
    private static final int REQUEST_CHOOSE_ADDRESS_MIGRATE_VERSION = 789;
    private static final int REQUEST_CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION = 890;
    private static final int REQUEST_EDIT_ADDRESS = 901;

    protected Bundle savedState;

    View loading;
    View mainView;
    ButtonView buttonView;
    StatusView statusView;
    AwbReturView awbReturView;
    AddressReturView addressReturView;
    DetailView detailView;
    ListProductView listProductView;
    SolutionView solutionView;
    HistoryView historyView;

    private TkpdProgressDialog normalLoading;

    private String resolutionID;
    private DetailViewModel viewData;

    @Inject
    DetailResCenterFragmentImpl presenter;

    public static DetailResCenterFragment createInstance(String resolutionID) {
        DetailResCenterFragment fragment = new DetailResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean isSeller() {
        return !SessionHandler.getLoginID(getActivity())
                .equals(getViewData().getDetailData().getBuyerID());
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public DetailViewModel getViewData() {
        return viewData;
    }

    public void setViewData(DetailViewModel model) {
        this.viewData = model;
    }

    @Override
    public void showLoading(boolean isShow) {
        loading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingDialog(boolean show) {
        if (show) {
            normalLoading.showDialog();
        } else {
            normalLoading.dismiss();
        }
    }

    @Override
    public void setupArguments(Bundle arguments) {
        setResolutionID(arguments.getString(EXTRA_PARAM_RESOLUTION_ID));
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail_rescenter;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunch();
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        showLoading(false);
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setViewData((DetailViewModel) savedState.getParcelable(EXTRA_PARAM_VIEW_DATA));
        if (getViewData().isTimeOut()) {
            doOnInitTimeOut();
        } else if (!getViewData().isSuccess() && getViewData().isTimeOut()) {
            doOnInitFailed();
        } else {
            renderData();
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putParcelable(EXTRA_PARAM_VIEW_DATA, getViewData());
    }

    @Override
    protected void initView(View view) {
        loading = view.findViewById(R.id.loading);
        mainView = view.findViewById(R.id.main_view);
        buttonView = (ButtonView) view.findViewById(R.id.button_view);
        statusView = (StatusView) view.findViewById(R.id.status_view);
        awbReturView = (AwbReturView) view.findViewById(R.id.awb_view);
        addressReturView = (AddressReturView) view.findViewById(R.id.address_retur_view);
        detailView = (DetailView) view.findViewById(R.id.detail_view);
        listProductView = (ListProductView) view.findViewById(R.id.product_view);
        solutionView = (SolutionView) view.findViewById(R.id.solution_view);
        historyView = (HistoryView) view.findViewById(R.id.history_view);

        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        buttonView.setListener(this);
        statusView.setListener(this);
        awbReturView.setListener(this);
        addressReturView.setListener(this);
        detailView.setListener(this);
        listProductView.setListener(this);
        solutionView.setListener(this);
        historyView.setListener(this);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        ResolutionDetailComponent resolutionDetailComponent =
                DaggerResolutionDetailComponent.builder()
                        .appComponent(appComponent)
                        .resolutionDetailModule(new ResolutionDetailModule(this))
                        .build();
        resolutionDetailComponent.inject(this);
    }

    @Override
    public void showTimeOutMessage() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showSnackBar(String messageError) {
        if (messageError == null) {
            showTimeOutMessage();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), messageError);
        }
    }

    private void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener) {
        if (message != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
        }
    }

    @Override
    public void doOnInitTimeOut() {
        showLoading(false);
        showEmptyState(null, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setOnFirstTimeLaunch();
            }
        });
    }

    @Override
    public void doOnInitSuccess() {
        showLoading(false);
        if (getViewData().isSuccess()) {
            renderData();
        } else {
            showEmptyState(getViewData().getMessageError(), null);
        }
    }

    private void renderData() {
        if (getViewData().getButtonData() != null) {
            buttonView.renderData(getViewData().getButtonData());
        }
        if (getViewData().getStatusData() != null) {
            statusView.renderData(getViewData().getStatusData());
        }
        if (getViewData().getAwbData() != null) {
            awbReturView.renderData(getViewData().getAwbData());
        }
        if (getViewData().getAddressReturData() != null) {
            addressReturView.renderData(getViewData().getAddressReturData());
        }
        if (getViewData().getDetailData() != null) {
            detailView.renderData(getViewData().getDetailData());
        }
        if (getViewData().getProductData() != null) {
            listProductView.renderData(getViewData().getProductData());
        }
        if (getViewData().getSolutionData() != null) {
            solutionView.renderData(getViewData().getSolutionData());
        }
        if (getViewData().getHistoryData() != null) {
            historyView.renderData(getViewData().getHistoryData());
        }
    }

    @Override
    public void doOnInitFailed() {
        showLoading(false);
        showEmptyState(getViewData().getMessageError(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                }
                break;
            case REQUEST_APPEAL_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                }
                break;
            case REQUEST_EDIT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                }
                break;
            case REQUEST_INPUT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshPage();
                }
                break;
            case REQUEST_CHOOSE_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressAcceptSolution(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressAcceptAdminSolution(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_CHOOSE_ADDRESS_MIGRATE_VERSION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressMigrateVersion(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_EDIT_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionEditAddress(
                            destination != null ? destination.getAddressId() : null,
                            viewData.getAddressReturData().getAddressID(),
                            viewData.getAddressReturData().getConversationID()
                    );
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showConfirmationDialog(String messageDialog, ConfirmationDialog.Listener listener) {
        ConfirmationDialog.Builder(getActivity())
                .initView()
                .initValue(messageDialog)
                .initListener(listener)
                .show();
    }

    @Override
    public void setOnActionCancelResolutionClick() {
        showConfirmationDialog(getActivity().getString(R.string.msg_rescen_cancel),
                new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        presenter.cancelResolution();
                    }
                });
    }

    @Override
    public void setOnActionAcceptProductClick() {
        showConfirmationDialog(getActivity().getString(R.string.msg_rescen_finish),
                new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        presenter.finishReturProduct();
                    }
                });
    }

    @Override
    public void setOnActionAcceptSolutionClick() {
        showConfirmationDialog(getActivity().getString(R.string.msg_accept_sol),
                new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        if (getViewData().getButtonData().isAcceptReturSolution()) {
                            Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
                            intent.putExtra("resolution_center", true);
                            startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS);
                        } else {
                            presenter.acceptSolution();
                        }
                    }
                });
    }

    @Override
    public void setOnActionInputAddressClick() {
        Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS_MIGRATE_VERSION);
    }

    @Override
    public void setOnActionAcceptAdminSolutionClick() {
        showConfirmationDialog(getActivity().getString(R.string.msg_accept_admin),
                new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        if (getViewData().getButtonData().isAcceptReturSolution()) {
                            Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
                            intent.putExtra("resolution_center", true);
                            startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION);
                        } else {
                            presenter.acceptAdminSolution();
                        }
                    }
                });
    }

    @Override
    public void setOnActionHelpClick() {
        showConfirmationDialog(getActivity().getString(R.string.msg_rescen_help),
                new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        presenter.askHelpResolution();
                    }
                });
    }

    @Override
    public void setOnActionAppealClick() {
        startActivityForResult(
                SolutionListActivity.newAppealInstance(getActivity(), resolutionID),
                REQUEST_APPEAL_SOLUTION);
    }

    @Override
    public void setOnActionInputAwbNumberClick() {
        startActivityForResult(
                InputShippingActivity.createNewPageIntent(getActivity(), getResolutionID()),
                REQUEST_INPUT_SHIPPING
        );
    }

    @Override
    public void setOnActionEditSolutionClick() {
        startActivityForResult(getIntentEditResCenter(), REQUEST_EDIT_SOLUTION);
    }

    private Intent getIntentEditResCenter() {
        if (isSeller()) {
            return SolutionListActivity.newSellerEditInstance(getActivity(),
                    resolutionID);
        } else {
            return SolutionListActivity.newBuyerEditInstance(getActivity(),
                    resolutionID);
        }
    }

    @Override
    public void setOnActionMoreProductClick() {
        startActivity(ListProductActivity.newInstance(getActivity(), getResolutionID()));
    }

    @Override
    public void setOnActionDiscussClick() {
        startActivity(
                ResCenterDiscussionActivity.createIntent(
                        getActivity(),
                        getResolutionID(),
                        viewData.getDetailData().isReceived(),
                        !viewData.getDetailData().isCancel() && !viewData.getDetailData().isFinish()
                )
        );
    }

    @Override
    public void setOnActionMoreHistoryClick() {
        startActivity(HistoryActionActivity.newInstance(getActivity(), getResolutionID()));
    }

    @Override
    public void setOnActionTrackAwbClick(String shipmentID, String shipmentRef) {
        presenter.trackReturProduck(shipmentID, shipmentRef);
    }

    @Override
    public void setOnActionAwbHistoryClick() {
        startActivityForResult(
                HistoryShippingActivity.newInstance(
                        getActivity(),
                        getResolutionID(),
                        getViewData().getButtonData().isShowInputAwb()
                ),
                REQUEST_INPUT_SHIPPING
        );
    }

    @Override
    public void setOnActionAddressHistoryClick() {
        startActivity(HistoryAddressActivity.newInstance(getActivity(), getResolutionID()));
    }

    @Override
    public void setOnActionEditAddressClick() {
        Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
    }

    @Override
    public void setOnActionProductClick(String productID) {
        startActivity(ProductDetailActivity.newInstance(getActivity(), getResolutionID(), productID));
    }

    @Override
    public void setOnActionPeopleDetailClick(String buyerID) {
        startActivity(PeopleInfoNoDrawerActivity.createInstance(getActivity(), buyerID));
    }

    @Override
    public void setOnActionShopDetailClick(String shopID) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(shopID, "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void setOnActionInvoiceClick(String invoice, String url) {
        AppUtils.InvoiceDialog(getActivity(), url, invoice);
    }

    @Override
    public void doOnTrackingTimeOut() {
        showLoadingDialog(false);
        showTimeOutMessage();
    }

    @Override
    public void doOnTrackingSuccess(TrackingDialogViewModel model) {
        showLoadingDialog(false);
        TrackShippingDialog.Builder(getActivity())
                .initView()
                .initValue(model)
                .show();
    }

    @Override
    public void doOnTrackingFailed() {
        showLoadingDialog(false);
        showSnackBar(null);
    }

    @Override
    public void doOnTrackingError(String messageError) {
        showLoadingDialog(false);
        showSnackBar(messageError);
    }

    @Override
    public void doOnActionSucess() {
        showLoadingDialog(false);
        presenter.refreshPage();
    }

    @Override
    public void doOnActionError(String messageError) {
        showLoadingDialog(false);
        showSnackBar(messageError);
    }

    @Override
    public void doOnActionError() {
        showLoadingDialog(false);
        showSnackBar(null);
    }

    @Override
    public void doOnActionTimeOut() {
        showLoadingDialog(false);
        showTimeOutMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }
}
