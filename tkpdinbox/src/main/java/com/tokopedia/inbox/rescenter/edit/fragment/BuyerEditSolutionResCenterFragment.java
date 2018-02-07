package com.tokopedia.inbox.rescenter.edit.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
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
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.create.customdialog.BaseUploadImageDialog;
import com.tokopedia.inbox.rescenter.edit.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.edit.customdialog.UploadImageEditResCenterDialog;
import com.tokopedia.inbox.rescenter.edit.customview.EditAttachmentView;
import com.tokopedia.inbox.rescenter.edit.customview.EditSolutionView;
import com.tokopedia.inbox.rescenter.edit.customview.MessageView;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditSolutionImpl;
import com.tokopedia.inbox.rescenter.edit.presenter.BuyerEditSolutionPresenter;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.RequestPermissionUtil;

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
 * Created on 8/26/16.
 */
@RuntimePermissions
public class BuyerEditSolutionResCenterFragment
        extends BasePresenterFragment<BuyerEditSolutionPresenter>
        implements BuyerEditSolutionListener, AttachmentAdapter.AttachmentAdapterListener {

    private static final String ARGS_PARAM_PASS_DATA = "pass_data";

    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.view_solution_section)
    EditSolutionView solutionSectionView;
    @BindView(R2.id.view_attachment_section)
    EditAttachmentView attachmenSectionView;
    @BindView(R2.id.view_message_section)
    MessageView messageView;
    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.include_loading)
    View loading;

    private ActionParameterPassData passData;
    private List<AttachmentResCenterVersion2DB> attachmentData;
    private AttachmentAdapter attachmentAdapter;
    private UploadImageEditResCenterDialog uploadImageDialog;

    public static Fragment newInstance(ActionParameterPassData passData) {
        BuyerEditSolutionResCenterFragment fragment = new BuyerEditSolutionResCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public BuyerEditSolutionResCenterFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunched(getActivity(), passData);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(ARGS_PARAM_PASS_DATA, passData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        passData = savedState.getParcelable(ARGS_PARAM_PASS_DATA);
        renderSolutionSpinner();
        showAttachmentView(passData.getTroubleCategoryChoosen().getAttachment() == 1);
        showLoading(false);
        showMainView(true);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new BuyerEditSolutionImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(ARGS_PARAM_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_solution_rescenter;
    }

    @Override
    public EditSolutionView getSolutionSectionView() {
        return solutionSectionView;
    }

    @Override
    public EditAttachmentView getAttachmenSectionView() {
        return attachmenSectionView;
    }

    @Override
    public MessageView getMessageView() {
        return messageView;
    }

    @Override
    protected void initView(View view) {
        renderInvoiceData(passData.getFormData().getForm());
        renderShopData(passData.getFormData().getForm());
    }

    private void renderShopData(EditResCenterFormData.Form form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getResolutionOrder().getOrderShopName()));
        String mShopName = form.getResolutionOrder().getOrderShopName();

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

    private void renderInvoiceData(final EditResCenterFormData.Form form) {
        SpannableString spannableString = new SpannableString(form.getResolutionOrder().getOrderInvoiceRefNum());
        String invoiceRefNum = form.getResolutionOrder().getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(
                                getActivity(),
                                form.getResolutionOrder().getOrderPdfUrl(),
                                form.getResolutionOrder().getOrderInvoiceRefNum()
                        );
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

    @Override
    public void showLoading(boolean isVisible) {
        loading.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMainView(boolean isVisible) {
        mainView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setViewListener() {
        attachmenSectionView.setListener(this);
        messageView.setListener(this);
        solutionSectionView.setListener(this);
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

    @OnClick(R2.id.action_submit)
    public void onSubmitClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.setOnSubmitClick(getActivity());
    }

    @OnClick(R2.id.action_abort)
    public void onAbortClick() {
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
    public void storeSolutionDataList(List<EditResCenterFormData.SolutionData> dataList) {
        passData.getFormData().setListSolution(dataList);
    }

    @Override
    public void renderSolutionSpinner() {
        solutionSectionView.renderData(passData);
    }

    @Override
    public void showAttachmentView(boolean visible) {
        if (visible) {
            attachmenSectionView.setVisibility(View.VISIBLE);
        } else {
            attachmenSectionView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTimeOutFull(NetworkErrorHelper.RetryClickedListener clickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
    }

    @Override
    public void showErrorMessageFull(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, null);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener) {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onClickAddAttachment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BuyerEditSolutionResCenterFragmentPermissionsDispatcher.actionImagePickerWithCheck(BuyerEditSolutionResCenterFragment.this);
            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BuyerEditSolutionResCenterFragmentPermissionsDispatcher.actionCameraWithCheck(BuyerEditSolutionResCenterFragment.this);
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
    public ActionParameterPassData getActionParameterPassData() {
        passData.setAttachmentData(attachmentData);
        passData.setSolutionChoosen(solutionSectionView.getSolutionChoosen());
        passData.setRefund(String.valueOf(solutionSectionView.getRefundBox().getText()));
        passData.setReplyMsg(String.valueOf(messageView.getMessageBox().getText()));
        return passData;
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
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BuyerEditSolutionResCenterFragmentPermissionsDispatcher.onRequestPermissionsResult(
                BuyerEditSolutionResCenterFragment.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
