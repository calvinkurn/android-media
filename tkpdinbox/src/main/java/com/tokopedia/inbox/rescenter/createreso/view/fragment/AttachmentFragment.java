package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.gallery.GalleryType;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.AttachmentFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by yoasfs on 30/08/17.
 */

@RuntimePermissions
public class AttachmentFragment extends BaseDaggerFragment implements AttachmentFragmentListener.View, AttachmentAdapterListener {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    private static final int REQUEST_CODE_GALLERY = 1243;
    private static final int COUNT_MAX_ATTACHMENT = 5;
    private static final int COUNT_MIN_STRING = 30;

    private TkpdTextInputLayout tilInformation;
    private EditText etInformation;
    private Button btnContinue;
    private RecyclerView rvAttachment;

    private ResultViewModel resultViewModel;
    private AttachmentFragmentPresenter presenter;
    private AttachmentAdapter adapter;
    private ImageUploadHandler uploadImageDialog;


    public static AttachmentFragment newInstance(ResultViewModel resultViewModel) {
        AttachmentFragment fragment = new AttachmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        uploadImageDialog = ImageUploadHandler.createInstance(this);
        presenter = new AttachmentFragmentPresenter(getActivity(), this, uploadImageDialog);
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AttachmentFragmentPermissionsDispatcher.onRequestPermissionsResult(AttachmentFragment.this, requestCode, grantResults);
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
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        resultViewModel.message.remark = etInformation.getText().toString();
        state.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        resultViewModel = savedState.getParcelable(RESULT_VIEW_MODEL_DATA);
        presenter.initResultViewModel(resultViewModel);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_attachment;
    }

    @Override
    protected void initView(View view) {
        setupUI(view);
        tilInformation = (TkpdTextInputLayout) view.findViewById(R.id.til_information);
        etInformation = (EditText) view.findViewById(R.id.et_information);
        btnContinue = (Button) view.findViewById(R.id.btn_upload);
        rvAttachment = (RecyclerView) view.findViewById(R.id.rv_attachment);
        adapter = new AttachmentAdapter(context, COUNT_MAX_ATTACHMENT, this);

        buttonDisabled(btnContinue);
        rvAttachment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvAttachment.setAdapter(adapter);
        tilInformation.setHint(context.getResources().getString(R.string.string_information));
        presenter.initResultViewModel(resultViewModel);

    }

    @Override
    public void populateDataToView(ResultViewModel resultViewModel) {
        adapter.updateAdapter(resultViewModel.attachmentList);
        etInformation.setText(resultViewModel.message.remark);
    }

    @Override
    public void updateView(Attachment attachment) {
        boolean isComplete = true;
        if (attachment.information.length() < COUNT_MIN_STRING) {
            tilInformation.setError(context.getResources().getString(R.string.string_min_30_char));
            isComplete = false;
        } else {
            tilInformation.hideErrorSuccess();
        }
        if (adapter.getList().size() == 0) {
            isComplete = false;
        }
        if (isComplete) {
            buttonSelected(btnContinue);
        } else {
            buttonDisabled(btnContinue);
        }
    }

    @Override
    public void addAttachmentFile(AttachmentViewModel attachmentViewModel) {
        adapter.addAttachment(attachmentViewModel);
        resultViewModel.attachmentList = adapter.getList();
    }

    public void buttonSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_enable));
        button.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_disable));
        button.setTextColor(ContextCompat.getColor(context, R.color.black_38));
    }

    @Override
    protected void setViewListener() {
        etInformation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onInformationStringChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btnContinueClicked();
                UnifyTracking.eventCreateResoStep3Continue();
            }
        });
    }

    @Override
    public void onAddAttachmentClicked() {
        if (adapter.getList().size() < COUNT_MAX_ATTACHMENT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.dialog_upload_option));
            builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AttachmentFragmentPermissionsDispatcher.actionImagePickerWithCheck(AttachmentFragment.this);
                }
            }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AttachmentFragmentPermissionsDispatcher.actionCameraWithCheck(AttachmentFragment.this);
                }
            });

            Dialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.max_upload_detail_res_center));
        }

    }

    @Override
    public void onEmptyAdapter() {
        presenter.onAdapterChanged(adapter.getList());
    }

    @Override
    public List<AttachmentViewModel> getAttachmentListFromAdapter() {
        return adapter.getList();
    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadImageDialog.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.RESOLUTION_CENTER_UPLOAD_VIDEO).equals("true")) {
            startActivityForResult(
                    GalleryActivity.createIntent(getActivity(), GalleryType.ofAll()),
                    REQUEST_CODE_GALLERY
            );
        } else {
            uploadImageDialog.actionImagePicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUploadHandler.REQUEST_CODE:
                presenter.handleDefaultOldUploadImageHandlerResult(resultCode, data);
                break;
            case REQUEST_CODE_GALLERY:
                presenter.handleNewGalleryResult(resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    public void showSnackBarError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}
