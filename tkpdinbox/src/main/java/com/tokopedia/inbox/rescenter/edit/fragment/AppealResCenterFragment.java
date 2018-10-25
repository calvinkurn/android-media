package com.tokopedia.inbox.rescenter.edit.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.rescenter.create.customdialog.BaseUploadImageDialog;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.edit.customdialog.UploadImageEditResCenterDialog;
import com.tokopedia.inbox.rescenter.edit.customview.AppealAttachmentView;
import com.tokopedia.inbox.rescenter.edit.customview.AppealSolutionView;
import com.tokopedia.inbox.rescenter.edit.listener.AppealResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.AppealResCenterImpl;
import com.tokopedia.inbox.rescenter.edit.presenter.AppealResCenterPresenter;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created on 8/31/16.
 */
@RuntimePermissions
public class AppealResCenterFragment extends BasePresenterFragment<AppealResCenterPresenter>
        implements AppealResCenterListener, AttachmentAdapter.AttachmentAdapterListener {

    private static final String ARGS_PARAM_PASS_DATA = "ARGS_PARAM_PASS_DATA";

    private ActionParameterPassData passData;

    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.include_loading)
    ProgressBar loading;
    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.view_edit_solution_section)
    AppealSolutionView solutionView;
    @BindView(R2.id.view_attachment_section)
    AppealAttachmentView attachmenSectionView;

    private List<AttachmentResCenterVersion2DB> attachmentData;
    private AttachmentAdapter attachmentAdapter;
    private UploadImageEditResCenterDialog uploadImageDialog;

    public static Fragment newInstance(ActionParameterPassData passData) {
        AppealResCenterFragment fragment = new AppealResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnLaunching(getActivity());
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(ARGS_PARAM_PASS_DATA, passData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        passData = savedState.getParcelable(ARGS_PARAM_PASS_DATA);
        setLoading(false);
        setMainView(true);
        if (passData.getAppealFormData() == null) {
            presenter.setOnLaunching(getActivity());
        } else {
            presenter.renderView(passData.getAppealFormData());
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new AppealResCenterImpl(this);
    }

    @Override
    public AppealResCenterPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARGS_PARAM_PASS_DATA);
    }

    @Override
    public String getResolutionID() {
        return getPassData().getResolutionID();
    }

    @Override
    public Context getBaseContext() {
        return getActivity();
    }

    @Override
    public DetailResCenterData getDetailData() {
        return getPassData().getDetailData();
    }

    @Override
    public ActionParameterPassData getPassData() {
        return passData;
    }

    @Override
    public void setPassData(ActionParameterPassData passData) {
        this.passData = passData;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_appeal_resolution;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        attachmenSectionView.setListener(this);
        solutionView.setListener(this);
    }

    @Override
    public void setLoading(boolean visible) {
        loading.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMainView(boolean visible) {
        mainView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTimeOutView(NetworkErrorHelper.RetryClickedListener rcListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), rcListener);
    }

    @Override
    public void setErrorView(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
    }

    @Override
    protected void initialVar() {
        uploadImageDialog = new UploadImageEditResCenterDialog(this, passData.getResolutionID());
        attachmentData = LocalCacheManager.AttachmentEditResCenter.Builder(passData.getResolutionID()).getCache();
        attachmentAdapter = new AttachmentAdapter(this, attachmentData);
    }

    @Override
    protected void setActionVar() {
        attachmenSectionView.attachAdapter(attachmentAdapter);
    }

    @Override
    public void renderShop(AppealResCenterFormData form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getForm().getResolutionOrder().getOrderShopName()));
        String mShopName = form.getForm().getResolutionOrder().getOrderShopName();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(mShopName),
                spannableString.toString().indexOf(mShopName) + mShopName.length(),
                0
        );

        shopName.setMovementMethod(LinkMovementMethod.getInstance());
        shopName.setText(spannableString);
    }

    @Override
    public void renderInvoice(final AppealResCenterFormData form) {
        SpannableString spannableString = new SpannableString(form.getForm().getResolutionOrder().getOrderInvoiceRefNum());
        String invoiceRefNum = form.getForm().getResolutionOrder().getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(getActivity(),
                                form.getForm().getResolutionOrder().getOrderPdfUrl(),
                                form.getForm().getResolutionOrder().getOrderInvoiceRefNum());
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                        ds.setColor(ContextCompat.getColor(getActivity(), R.color.href_link));
                    }
                },
                spannableString.toString().indexOf(invoiceRefNum),
                spannableString.toString().indexOf(invoiceRefNum) + invoiceRefNum.length(),
                0
        );

        invoice.setMovementMethod(LinkMovementMethod.getInstance());
        invoice.setText(spannableString);
    }

    @OnClick(R2.id.action_choose_solution)
    public void onButtonNextClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.setOnButtonNextClick(getActivity());
    }

    @OnClick(R2.id.action_abort)
    public void onButtonAbortClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_discard_changes)
                .setTitle(R.string.dialog_title_discard_changes)
                .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.action_keep, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void renderSolution(AppealResCenterFormData formData) {
        solutionView.renderData(formData);
    }

    @Override
    public void onClickAddAttachment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppealResCenterFragmentPermissionsDispatcher.actionImagePickerWithCheck(AppealResCenterFragment.this);
            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppealResCenterFragmentPermissionsDispatcher.actionCameraWithCheck(AppealResCenterFragment.this);
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadImageDialog.openCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        uploadImageDialog.openImagePicker();
    }

    @Override
    public void onClickOpenAttachment(View view, final int position) {
        uploadImageDialog.showRemoveDialog(new UploadImageEditResCenterDialog.onRemoveAttachmentListener() {
            @Override
            public void onRemoveClickListener() {
                attachmentData.get(position).delete();
                attachmentData.remove(position);
                attachmentAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onCancelClickListener() {

            }
        });
    }

    @Override
    public void onClickRemoveAttachment(View view, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImageDialog.onResult(requestCode, resultCode, data, new BaseUploadImageDialog.UploadImageDialogListener() {
            @Override
            public void onSuccess(List<AttachmentResCenterVersion2DB> data) {
                attachmentData.clear();
                attachmentData.addAll(data);
                attachmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                showErrorMessage(getActivity().getString(R.string.error_gallery_valid));
            }
        });
    }

    @Override
    public void showErrorMessage(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    @Override
    public void showLoading(boolean visible) {
        loading.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMainView(boolean visible) {
        mainView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener) {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setActivityResult() {
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public AppealSolutionView getSolutionView() {
        return solutionView;
    }

    @Override
    public AppealAttachmentView getAttachmenSectionView() {
        return attachmenSectionView;
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public List<AttachmentResCenterVersion2DB> getAttachmentData() {
        return attachmentData;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AppealResCenterFragmentPermissionsDispatcher.onRequestPermissionsResult(AppealResCenterFragment.this, requestCode, grantResults);
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
}
