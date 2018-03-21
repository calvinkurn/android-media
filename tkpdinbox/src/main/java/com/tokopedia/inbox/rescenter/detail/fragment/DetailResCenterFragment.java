package com.tokopedia.inbox.rescenter.detail.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.detail.customview.DetailView;
import com.tokopedia.inbox.rescenter.detail.customview.ReplyEditorView;
import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detail.dialog.TrackShippingDialog;
import com.tokopedia.inbox.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.inbox.rescenter.detail.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.inbox.rescenter.detail.presenter.DetailResCenterImpl;
import com.tokopedia.inbox.rescenter.detail.presenter.DetailResCenterPresenter;
import com.tokopedia.inbox.rescenter.edit.activity.EditResCenterActivity;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@Deprecated
@RuntimePermissions
public class DetailResCenterFragment extends BasePresenterFragment<DetailResCenterPresenter>
        implements DetailResCenterView {

    private static final String ARG_PARAM_RES_CENTER_PASS_DATA = "ARG_PARAM_RES_CENTER_PASS_DATA";
    private static final String TAG = DetailResCenterFragment.class.getSimpleName();
    private static final int EDIT_RESOLUTION_REQUEST_CODE = 6789;
    private static final int CHOOSE_ADDRESS = 7890;
    private static final int CHOOSE_ADDRESS_MIGRATE_VERSION = 7891;
    private static final int CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION = 7892;
    private static final int EDIT_ADDRESS = 5678;

    private ActivityParamenterPassData passData;
    private DetailResCenterData apiModelData;

    private ResCenterView mListener;
    private TkpdProgressDialog normalLoading;

    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.loading)
    View loadingView;
    @BindView(R2.id.custom_view_reply_editor)
    ReplyEditorView replyEditorView;
    @BindView(R2.id.custom_view_detail_rescenter)
    DetailView detailView;

    private String ahrefEditAddressURL;

    public static DetailResCenterFragment newInstance(ActivityParamenterPassData activityParamenterPassData) {
        DetailResCenterFragment fragment = new DetailResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_RES_CENTER_PASS_DATA, activityParamenterPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getResolutionID() {
        return passData.getResCenterId();
    }

    @Override
    public void onReceiveServiceResult(int resultCode, Bundle resultData) {
        presenter.onReceiveServiceResult(resultCode, resultData);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Log.d(TAG, "onFirstTimeLaunched()");
        presenter.onFirstTimeLaunched(this, passData);
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.saveState(state, passData, apiModelData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.restoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void showConfirmationDialog(int messageDialog, ConfirmationDialog.Listener listener) {
        ConfirmationDialog.Builder(getActivity())
                .initView()
                .initValue(getActivity().getString(messageDialog))
                .initListener(listener)
                .show();
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new DetailResCenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        mListener = (ResCenterView) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARG_PARAM_RES_CENTER_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_res_center_detail;
    }

    @Override
    protected void initView(View view) {
        prepareLoadingView();
    }

    private void prepareLoadingView() {
        normalLoading = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        detailView.setListener(this);
        replyEditorView.setListener(this);
    }

    @Override
    protected void initialVar() {
        presenter.setInteractionListener(mListener);
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void setProgressLoading(boolean param) {
        if (param) {
            showMainView(false);
            showLoading(true);
            hideKeyboard();
        } else {
            showMainView(true);
            showLoading(false);
        }
    }

    private void hideKeyboard() {
        if (getView() != null) {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
        }
    }

    @Override
    public void setFailSaveRespond() {
        if (!(mainView == null || loadingView == null)) {
            mainView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToastMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void showTimeOutMessage() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void loadDetailResCenterData(DetailResCenterData responseData) {
        apiModelData = responseData;
        replyEditorView.renderData(responseData.getDetail());
        detailView.renderData(responseData.getDetail());
    }

    @Override
    public void setErrorComment(String param) {
        replyEditorView.setError(param);
    }

    @Override
    public void setAttachmentArea(boolean param) {
        replyEditorView.setAttachmentVisibility(param);
    }

    @Override
    public void showAttachment(List<AttachmentResCenterVersion2DB> data) {
        replyEditorView.renderAttachmentData(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_RESOLUTION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    this.refreshPage();
                }
                break;
            case CHOOSE_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionInputAddress(getActivity(), destination.getAddressId());
                }
                break;
            case CHOOSE_ADDRESS_MIGRATE_VERSION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionInputAddressMigrateVersion(getActivity(), destination.getAddressId());
                }
                break;
            case CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionInputAddressAcceptAdminSolution(getActivity(), destination.getAddressId());
                }
                break;
            case EDIT_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionEditAddress(getActivity(), destination.getAddressId(), ahrefEditAddressURL);
                }
                break;
            default:
                presenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showLoadingDialog(boolean param) {
        if (param) {
            normalLoading.showDialog();
        } else {
            normalLoading.dismiss();
        }
    }

    @Override
    public void showTrackingDialog(ResCenterTrackShipping.TrackShipping trackShipping) {
        TrackShippingDialog.Builder(getActivity())
                .initView()
                .initValue(trackShipping)
                .show();
    }

    @Override
    public void showInvalidTrackingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(MethodChecker.fromHtml(getActivity().getString(R.string.error_520_tracking)));
        builder.setPositiveButton(getActivity().getString(R.string.title_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void refreshPage() {
        presenter.refreshPage(getActivity(), passData);
    }

    @Override
    public void setReplyAreaEmpty() {
        replyEditorView.setReplyBoxEmpty();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void showLoading(boolean isVisible) {
        if (loadingView != null) {
            loadingView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showMainView(boolean isVisible) {
        if (mainView != null) {
            mainView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showTimeOutView(NetworkErrorHelper.RetryClickedListener clickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
    }

    @Override
    public void showErrorView(String message) {
        if (message != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        }
    }

    @Override
    public void setErrorWvLogin() {
        setFailSaveRespond();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                onFirstTimeLaunched();
            }
        });
    }

    @Override
    public void setOnSendClickListener(String text) {
        presenter.onButtonSendClick(getActivity(), text);
    }


    @Override
    public void setOnAttachmentClickListener() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DetailResCenterFragmentPermissionsDispatcher.actionImagePickerWithCheck(DetailResCenterFragment.this);
            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DetailResCenterFragmentPermissionsDispatcher.actionCameraWithCheck(DetailResCenterFragment.this);
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        presenter.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        presenter.actionImagePicker();
    }

    @Override
    public void actionAcceptAdmin(String paramID) {
        presenter.actionAcceptAdminSolution();
    }

    @Override
    public void actionFinishRetur(String paramID) {
        presenter.actionFinishReturSolution();
    }

    @Override
    public void actionAcceptResolution(String paramID) {
        presenter.actionAcceptSolution();
    }

    @Override
    public void actionReportResolution(String paramID) {
        presenter.actionReportResolution();
    }

    @Override
    public void actionCancelResolution(String paramID) {
        presenter.actionCancelResolution();
    }

    @Override
    public void openInputShippingRef() {
        presenter.onNewShippingClickListener(getActivity());
    }

    @Override
    public void openEditShippingRef(String url) {
        presenter.onEditShippingClickListener(getActivity(), url);
    }

    @Override
    public void openTrackShippingRef(String url) {
        presenter.requestTrackDelivery(getActivity(), url);
    }

    @Override
    public void openAppealSolution(String paramID) {
        startActivityForResult(
                EditResCenterActivity.newAppealInstance(getActivity(), passData, apiModelData),
                EDIT_RESOLUTION_REQUEST_CODE);
    }

    @Override
    public void openEditSolution(String paramID) {
        startActivityForResult(getIntentEditResCenter(), EDIT_RESOLUTION_REQUEST_CODE);
    }

    private Intent getIntentEditResCenter() {
        if (apiModelData.getDetail().getResolutionBy().getByCustomer() == 1) {
//            return EditResCenterActivity.newBuyerInstance(getActivity(), passData, apiModelData);
            return SolutionListActivity.newBuyerEditInstance(getActivity(),
                    passData.getResCenterId(), false);
        } else {
//            return EditResCenterActivity.newSellerInstance(getActivity(), passData, apiModelData);
            return SolutionListActivity.newSellerEditInstance(getActivity(),
                    passData.getResCenterId(), false);
        }
    }

    @Override
    public void openInputAddress() {
        Intent intent = getChooseAddressIntent(false);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, CHOOSE_ADDRESS);
    }

    @Override
    public void openInputAddressForAcceptAdmin() {
        Intent intent = getChooseAddressIntent(false);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION);
    }

    @Override
    public void openInputAddressMigrateVersion() {
        Intent intent = getChooseAddressIntent(false);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, CHOOSE_ADDRESS_MIGRATE_VERSION);
    }

    @Override
    public void openEditAddress(String url) {
        this.ahrefEditAddressURL = url;
        Intent intent = getChooseAddressIntent(true);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, EDIT_ADDRESS);
    }

    public Intent getChooseAddressIntent(boolean isEditAddress) {
        return ChooseAddressActivity.createResolutionInstance(
                getActivity(), getResolutionID(), false, isEditAddress);
    }
    @Override
    public void openAttachment(String url) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(url.substring(url.lastIndexOf("?url=") + 5));
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openShop() {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(apiModelData.getDetail().getResolutionShop().getShopId(), "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openInvoice() {
        AppUtils.InvoiceDialog(getActivity(),
                apiModelData.getDetail().getResolutionOrder().getOrderPdfUrl(),
                apiModelData.getDetail().getResolutionOrder().getOrderInvoiceRefNum());
    }

    @Override
    public void openPeople(String url) {
        if (getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
            startActivity(
                    ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .getTopProfileIntent(
                                    getActivity(),
                                    Uri.parse(url).getQueryParameter("id")));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DetailResCenterFragmentPermissionsDispatcher.onRequestPermissionsResult(DetailResCenterFragment.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onPermissionDenied(getActivity(),listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onNeverAskAgain(getActivity(),listPermission);
    }

    @Override
    public void openVideoPlayer(String url) {
        String urlVideo = Uri.parse(url).getQueryParameter("url_video");
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerActivity.PARAMS_URL_VIDEO, urlVideo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
