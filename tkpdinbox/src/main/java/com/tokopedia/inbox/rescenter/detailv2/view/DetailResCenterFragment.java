package com.tokopedia.inbox.rescenter.detailv2.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.FreeReturnActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.NextActionActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.TrackShippingActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
import com.tokopedia.inbox.rescenter.detailv2.view.customdialog.TrackShippingDialog;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.AddressReturView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.AwbReturView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ButtonView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.CancelComplaintView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.DetailView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.FreeReturnView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.HistoryView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ListProductView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.ProveView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.SolutionView;
import com.tokopedia.inbox.rescenter.detailv2.view.customview.TimeView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterFragmentImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat
        .NextActionDetailStepDomain;
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

    public static final int NEXT_STATUS_CURRENT = 1;
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

    public static final String ACTION_FINISH = "finish";
    public static final String ACTION_HELP = "help";
    public static final String ACTION_ACCEPT = "accept";
    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_INPUT_AWB = "input_awb";
    public static final String ACTION_EDIT_AWB = "edit_awb";
    public static final String ACTION_INPUT_ADDRESS = "input_address";
    public static final String ACTION_EDIT_ADDRESS = "edit_address";

    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

    protected Bundle savedState;

    View loading;
    NestedScrollView mainView;
    ButtonView buttonView;
    CardView cvNextStep, cvDiscussion;
    AwbReturView awbReturView;
    AddressReturView addressReturView;
    DetailView detailView;
    TimeView timeView;
    ListProductView listProductView;
    SolutionView solutionView;
    ProveView proveView;
    FreeReturnView freeReturnView;
    HistoryView historyView;
    CancelComplaintView cancelComplaintView;
    TextView tvNextStep;

    private TkpdProgressDialog normalLoading;
    private Dialog resCenterDialog;
    private ImageView ivNextStepStatic;
    private GlowingView glowingView;

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
    public int getResolutionStatus() {
        return getViewData().getDetailData().getResolutionStatus();
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
        buttonView = view.findViewById(R.id.button_view);
        cvNextStep = view.findViewById(R.id.cv_next_step);
        awbReturView = view.findViewById(R.id.awb_view);
        addressReturView = view.findViewById(R.id.address_retur_view);
        detailView = view.findViewById(R.id.detail_view);
        timeView = view.findViewById(R.id.time_view);
        tvNextStep = view.findViewById(R.id.tv_next_step);
        listProductView = view.findViewById(R.id.product_view);
        solutionView = view.findViewById(R.id.solution_view);
        proveView = view.findViewById(R.id.prove_view);
        historyView = view.findViewById(R.id.history_view);
        cancelComplaintView = view.findViewById(R.id.cancel_complaint_view);
        cvDiscussion = view.findViewById(R.id.cv_discussion);
        ivNextStepStatic = view.findViewById(R.id.iv_next_step_static);
        glowingView = view.findViewById(R.id.view_glowing);
        freeReturnView = view.findViewById(R.id.free_return_view);

        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        cvNextStep.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);
        glowingView.setVisibility(View.GONE);
        ivNextStepStatic.setVisibility(View.GONE);
    }

    @Override
    protected void setViewListener() {
        buttonView.setListener(this);
        awbReturView.setListener(this);
        addressReturView.setListener(this);
        detailView.setListener(this);
        timeView.setListener(this);
        listProductView.setListener(this);
        solutionView.setListener(this);
        proveView.setListener(this);
        historyView.setListener(this);
        freeReturnView.setListener(this);
        cancelComplaintView.setListener(this);

        cvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NextActionActivity.newInstance(
                        getActivity(),
                        resolutionID,
                        getViewData().getNextActionDomain(),
                        getViewData().getDetailData().getResolutionStatus()));
                getBottomSheetActivityTransition();
            }
        });

        cvDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
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
        if (getActivity() != null) {
            if (message != null) {
                NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, listener);
            } else {
                NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
            }
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
            showEmptyState(getViewData().getMessageError(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.setOnFirstTimeLaunch();
                }
            });
        }
    }

    private void renderData() {
        timeView.setVisibility(View.GONE);
        glowingView.setVisibility(View.GONE);
        ivNextStepStatic.setVisibility(View.GONE);
        cvNextStep.setVisibility(View.VISIBLE);
        cvDiscussion.setVisibility(View.VISIBLE);
        for (NextActionDetailStepDomain nextStep : getViewData().getNextActionDomain().getDetail().getStep()) {
            if (nextStep.getStatus() == NEXT_STATUS_CURRENT) {
                tvNextStep.setText(nextStep.getName());
            }
        }
        if (getViewData().getButtonData() != null) {
            buttonView.renderData(getViewData().getButtonData());
            if (getViewData().getButtonData().isCancelOn4thOrder()) {
                cancelComplaintView.renderData(getViewData().getButtonData());
            }
        }
        if (getViewData().getAwbData() != null) {
            awbReturView.renderData(getViewData().getAwbData());
        }
        if (getViewData().getAddressReturData() != null) {
            addressReturView.renderData(getViewData().getAddressReturData());
        }
        if (getViewData().getDetailData() != null) {
            detailView.renderData(getViewData().getDetailData());
            if (getViewData().getDetailData().isDeadlineVisibility()
                    && getViewData().getDetailData().getResponseDeadline() != null) {
                timeView.setVisibility(View.VISIBLE);
                timeView.renderData(getViewData().getDetailData());
            } else {
                timeView.setVisibility(View.GONE);
            }
            if (getViewData().getDetailData().getResolutionStatus() == STATUS_FINISHED
                    || getViewData().getDetailData().getResolutionStatus() == STATUS_CANCEL) {
                ivNextStepStatic.setVisibility(View.VISIBLE);
            } else {
                glowingView.setVisibility(View.VISIBLE);
                glowingView.renderData(new Object());
            }
        }
        if (getViewData().getProductData() != null
                && getViewData().getProductData().getProductList().size() != 0) {
            listProductView.renderData(getViewData().getProductData());
        }
        if (getViewData().getSolutionData() != null) {
            solutionView.renderData(getViewData().getSolutionData());
        }
        if (getViewData().getProveData() != null) {
            if (getViewData().getProveData().isCanShowProveData()) {
                proveView.renderData(getViewData().getProveData());
            }
        }
        if (getViewData().getHistoryData() != null) {
            historyView.renderData(getViewData().getHistoryData());
        }
        if (getViewData().getFreeReturnData() != null
                && getViewData().getFreeReturnData().isFreeReturnShow()) {
            freeReturnView.renderData(getViewData().getFreeReturnData());
        }
    }


    @Override
    public void doOnInitFailed() {
        showLoading(false);
        showEmptyState(getViewData().getMessageError(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.setOnFirstTimeLaunch();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    showSnackBar(getActivity().getString(R.string.string_success_edit_solution));
                    presenter.refreshPage();
                }
                break;
            case REQUEST_APPEAL_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    showSnackBar(getActivity().getString(R.string.string_success_appeal));
                    presenter.refreshPage();
                }
                break;
            case REQUEST_EDIT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    showSnackBar(getActivity().getString(R.string.string_success_edit_awb));
                    presenter.refreshPage();
                }
                break;
            case REQUEST_INPUT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    showSnackBar(getActivity().getString(R.string.string_success_input_awb));
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
        showActionDialog(getViewData().getButtonData().getCancelLabel(),
                getViewData().getButtonData().getCancelDialogText(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.cancelResolution();
                        if (resCenterDialog != null)
                            resCenterDialog.dismiss();
                    }
                });
    }

    @Override
    public void setOnActionFinishResolutionClick() {
        showActionDialog(getViewData().getButtonData().getFinishComplaintLabel(),
                getViewData().getButtonData().getFinishComplaintDialogText(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.finishReturProduct();
                        if (resCenterDialog != null)
                            resCenterDialog.dismiss();
                    }
                });
    }

    @Override
    public void setOnActionRecomplaintClick() {
        Intent intent = CreateResCenterActivity.newRecomplaintInstance(
                getActivity(),
                String.valueOf(getViewData().getDetailData().getOrderID()),
                resolutionID);
        startActivity(intent);
    }

    @Override
    public void setOnActionAcceptSolutionClick() {
        showActionDialog(getViewData().getButtonData().getAcceptLabel(),
                getViewData().getButtonData().getAcceptTextLite(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.acceptSolution();
                        if (resCenterDialog != null)
                            resCenterDialog.dismiss();
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
        showActionDialog(getViewData().getButtonData().getAskHelpLabel(),
                getViewData().getButtonData().getAskHelpDialogText(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.askHelpResolution();
                        if (resCenterDialog != null)
                            resCenterDialog.dismiss();
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
        getBottomSheetActivityTransition();
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
        startActivity(TrackShippingActivity.newInstance(
                getActivity(),
                shipmentID,
                shipmentRef)
        );
        getBottomSheetActivityTransition();
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
    public void setOnFreeReturnClicked() {
        startActivity(FreeReturnActivity
                .newInstance(getActivity(), getViewData().getFreeReturnData().getFreeReturnLink()));
    }

    @Override
    public void setOnActionProductClick(String productID, String productName) {
        startActivity(ProductDetailActivity.newInstance(getActivity(), getResolutionID(), productID, productName));
    }

    @Override
    public void setOnActionPeopleDetailClick(String buyerID) {
        startActivity(
                ((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getTopProfileIntent(getActivity(), buyerID));
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

    private RelativeLayout.LayoutParams getButtonInitParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(
                0,
                0,
                (int) getResources().getDimension(R.dimen.margin_small),
                (int) getResources().getDimension(R.dimen.margin_small));
        return params;
    }

    @Override
    public void setOnDiscussionButtonPosition(boolean isButtonAvailable) {
        RelativeLayout.LayoutParams params = getButtonInitParams();
        if (isButtonAvailable) {
            params.addRule(RelativeLayout.ABOVE, R.id.button_view);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        cvDiscussion.setLayoutParams(params);
    }

    @Override
    public void actionReturnToList() {
        getActivity().setResult(DetailResChatActivity.ACTION_GO_TO_LIST);
        getActivity().finish();
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
    public void doOnActionSuccess(String action) {
        showLoadingDialog(false);

        String message = "";
        if (action.equals(ACTION_HELP)) message = getResources().getString(R.string.string_success_help);
        else if (action.equals(ACTION_FINISH)) message = getResources().getString(R.string.string_success_finish);
        else if (action.equals(ACTION_ACCEPT)) message = getResources().getString(R.string.string_success_accept);
        else if (action.equals(ACTION_CANCEL)) message = getResources().getString(R.string.string_success_cancel);
        if (!TextUtils.isEmpty(message)) showSnackBar(message);
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
    public void hideTimeTicker() {
        timeView.setVisibility(View.GONE);
        presenter.refreshPage();
    }

    private void showActionDialog(String title, String solution, View.OnClickListener action) {
        resCenterDialog = new Dialog(getActivity());
        resCenterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resCenterDialog.setContentView(R.layout.layout_rescenter_dialog);
        TextView tvTitle = resCenterDialog.findViewById(R.id.tv_title);
        TextView tvSolution = resCenterDialog.findViewById(R.id.tv_solution);
        ImageView ivClose = resCenterDialog.findViewById(R.id.iv_close);
        Button btnBack = resCenterDialog.findViewById(R.id.btn_back);
        Button btnAccept = resCenterDialog.findViewById(R.id.btn_yes);
        String newTitle = title + "?";
        tvTitle.setText(newTitle);
        tvSolution.setText(MethodChecker.fromHtml(solution));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resCenterDialog.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resCenterDialog.dismiss();
            }
        });
        btnAccept.setOnClickListener(action);
        resCenterDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }

    public void getBottomSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.pull_up, R.anim.push_down);
    }

    public void getBottomBackSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }
}
