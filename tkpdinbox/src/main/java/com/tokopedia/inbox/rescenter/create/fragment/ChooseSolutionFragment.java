package com.tokopedia.inbox.rescenter.create.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.create.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.create.customdialog.BaseUploadImageDialog;
import com.tokopedia.inbox.rescenter.create.customdialog.UploadImageCreateResCenterDialog;
import com.tokopedia.inbox.rescenter.create.customview.AttachmentSectionCreateResCenterView;
import com.tokopedia.inbox.rescenter.create.customview.SolutionSectionCreateResCenterView;
import com.tokopedia.inbox.rescenter.create.listener.ChooseSolutionListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;
import com.tokopedia.inbox.rescenter.create.presenter.ChooseSolutionImpl;
import com.tokopedia.inbox.rescenter.create.presenter.ChooseSolutionPresenter;
import com.tokopedia.inbox.rescenter.create.util.LocalCacheManager;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChooseSolutionFragment extends BasePresenterFragment<ChooseSolutionPresenter>
        implements ChooseSolutionListener, AttachmentAdapter.AttachmentAdapterListener {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";

    @BindView(R2.id.invoice)
    TextView invoice;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.view_solution_section)
    SolutionSectionCreateResCenterView solutionSectionView;
    @BindView(R2.id.view_attachment_section)
    AttachmentSectionCreateResCenterView attachmenSectionView;
    @BindView(R2.id.action_submit)
    View submitButton;
    @BindView(R2.id.action_abort)
    View actionAbort;
    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.include_loading)
    View loading;

    private ActionParameterPassData passData;
    private List<AttachmentResCenterVersion2DB> attachmentData;
    private AttachmentAdapter attachmentAdapter;
    private UploadImageCreateResCenterDialog uploadImageDialog;

    public ChooseSolutionFragment() {
    }

    public static ChooseSolutionFragment newInstance(ActionParameterPassData passData) {
        ChooseSolutionFragment fragment = new ChooseSolutionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
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
    public void showLoading(boolean isVisible) {
        loading.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMainView(boolean isVisible) {
        mainView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void storeSolutionDataList(List<CreateResCenterFormData.SolutionData> solutionDataList) {
        passData.getFormData().setListSolution(solutionDataList);
    }

    @Override
    public void renderSolutionSpinner() {
        solutionSectionView.renderData(passData);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ChooseSolutionImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        passData = arguments.getParcelable(KEY_PARAM_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_choose_solution;
    }

    @Override
    protected void initView(View view) {
        renderInvoiceData(passData.getFormData().getForm());
        renderShopData(passData.getFormData().getForm());
    }

    private void renderShopData(CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(getString(R.string.title_purchase_from).replace("XYZ", form.getOrderShopName()));
        String mShopName = form.getOrderShopName();

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

    private void renderInvoiceData(final CreateResCenterFormData.FormValueData form) {
        SpannableString spannableString = new SpannableString(form.getOrderInvoiceRefNum());
        String invoiceRefNum = form.getOrderInvoiceRefNum();

        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        AppUtils.InvoiceDialog(
                                getActivity(),
                                form.getOrderPdfUrl(),
                                form.getOrderInvoiceRefNum()
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
    protected void setViewListener() {
        solutionSectionView.setListener(this);
        attachmenSectionView.setListener(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.setOnSubmitClick(getActivity());
            }
        });
        actionAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.dialog_discard_changes)
                        .setTitle(R.string.dialog_title_discard_changes)
                        .setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.onAbortCreateResolution();
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
        });
    }

    @Override
    protected void initialVar() {
        uploadImageDialog = new UploadImageCreateResCenterDialog(this, passData.getOrderID());
        if (RequestPermissionUtil.checkHasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
            attachmentData = LocalCacheManager.AttachmentCreateResCenter.Builder(passData.getOrderID()).getCache();
        else
            attachmentData = new ArrayList<>();
        attachmentAdapter = new AttachmentAdapter(this, attachmentData);

    }

    @Override
    public void onClickAddAttachment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_upload_option));
        builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ChooseSolutionFragmentPermissionsDispatcher.actionImagePickerWithCheck(ChooseSolutionFragment.this);
            }
        }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ChooseSolutionFragmentPermissionsDispatcher.actionCameraWithCheck(ChooseSolutionFragment.this);
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
        uploadImageDialog.showRemoveDialog(new UploadImageCreateResCenterDialog.onRemoveAttachmentListener() {
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
    protected void setActionVar() {
        attachmenSectionView.attachAdapter(attachmentAdapter);
    }

    @Override
    public void showAttachmentView(boolean isSetVisible) {
        if (isSetVisible) {
            attachmenSectionView.setVisibility(View.VISIBLE);
        } else {
            attachmenSectionView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImageGallery.RESULT_CODE || resultCode == Activity.RESULT_OK) {
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
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showErrorMessageFull(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
    }

    @Override
    public void showTimeOutFull(NetworkErrorHelper.RetryClickedListener clickedListener) {
        if (clickedListener != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.setOnSubmitClick(getActivity());
                }
            });
        }
    }

    @Override
    public void setPassData(ActionParameterPassData passData) {
        this.passData = passData;
    }

    @Override
    public ActionParameterPassData collectInputData() {
        passData.setAttachmentData(attachmentData);
        passData.setSolutionChoosen(solutionSectionView.getSolutionData());
        passData.setRefund(String.valueOf(solutionSectionView.getRefundBox().getText()));
        return passData;
    }

    @Override
    public void setRefundError(String errorMessage) {
        solutionSectionView.getRefundBox().setError(errorMessage);
        solutionSectionView.requestFocus();
    }

    @Override
    public void onGetResultCreateResCenter(int resultCode, Bundle resultData) {
        presenter.processResultCreateResCenter(resultCode, resultData);
    }

    @Override
    public void successFinish() {
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        presenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public List<AttachmentResCenterVersion2DB> getAttachmentData() {
        return attachmentData;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChooseSolutionFragmentPermissionsDispatcher.onRequestPermissionsResult(
                ChooseSolutionFragment.this, requestCode, grantResults);
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
