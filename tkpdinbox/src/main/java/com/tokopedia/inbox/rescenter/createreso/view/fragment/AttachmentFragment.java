package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.VideoPickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.imagepicker.picker.main.view.VideoPickerActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.AttachmentFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by yoasfs on 30/08/17.
 */

@RuntimePermissions
public class AttachmentFragment extends BaseDaggerFragment implements AttachmentFragmentListener.View, AttachmentAdapterListener {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    private static final int REQUEST_CODE_VIDEO = 1243;
    private static final int COUNT_MAX_ATTACHMENT = 5;
    private static final int COUNT_MIN_STRING = 30;
    private static final int REQUEST_CODE_IMAGE_REPUTATION = 3479;
    public static final int MAX_VIDEO_SIZE_IN_KB = 20000;

    private TkpdTextInputLayout tilInformation;
    private EditText etInformation;
    private Button btnContinue;
    private RecyclerView rvAttachment;

    private ResultViewModel resultViewModel;
    private AttachmentFragmentPresenter presenter;
    private AttachmentAdapter adapter;
    private ImageUploadHandler uploadImageDialog;


    public static AttachmentFragment newInstance(Bundle bundle) {
        AttachmentFragment fragment = new AttachmentFragment();
        fragment.setArguments(bundle);
        return fragment;
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
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (savedState != null) {
            resultViewModel = savedState.getParcelable(RESULT_VIEW_MODEL_DATA);
            presenter.initResultViewModel(resultViewModel);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        resultViewModel.message.remark = etInformation.getText().toString();
        outState.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, container, false);
        tilInformation = (TkpdTextInputLayout) view.findViewById(R.id.til_information);
        etInformation = (EditText) view.findViewById(R.id.et_information);
        btnContinue = (Button) view.findViewById(R.id.btn_upload);
        rvAttachment = (RecyclerView) view.findViewById(R.id.rv_attachment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupArguments(getArguments());
        initView();
        setViewListener();
    }

    private void setupArguments(Bundle arguments) {
        resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    private void initView() {
        adapter = new AttachmentAdapter(getActivity(), COUNT_MAX_ATTACHMENT, this);

        buttonDisabled(btnContinue);
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvAttachment.setAdapter(adapter);
        tilInformation.setHint(getActivity().getResources().getString(R.string.string_information));
        uploadImageDialog = ImageUploadHandler.createInstance(this);
        presenter = new AttachmentFragmentPresenter(getActivity(), this, uploadImageDialog);
        presenter.attachView(this);
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
            tilInformation.setError(getActivity().getResources().getString(R.string.string_min_30_char));
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
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_38));
    }

    private void setViewListener() {
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
                UnifyTracking.eventCreateResoStep3Continue(getActivity());
            }
        });
    }

    @Override
    public void onAddAttachmentClicked() {
        if (adapter.getList().size() < COUNT_MAX_ATTACHMENT) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getActivity().getString(R.string.dialog_upload_option));
            builder.setPositiveButton(getActivity().getString(R.string.title_video), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    actionVideoPicker();
                }
            }).setNegativeButton(getActivity().getString(R.string.title_image), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    openImagePicker();
                }
            });

            Dialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.max_upload_detail_res_center));
        }

    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, com.tokopedia.imagepicker.picker.gallery.type.GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                null
                , null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_REPUTATION);
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

    public void actionVideoPicker() {
        VideoPickerBuilder builder = new VideoPickerBuilder(getString(R.string.choose_video), MAX_VIDEO_SIZE_IN_KB,
                0, null);
        startActivityForResult(
                VideoPickerActivity.getIntent(getActivity(), builder),
                REQUEST_CODE_VIDEO
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_REPUTATION:
                presenter.handleImageResult(resultCode, data);
                break;
            case REQUEST_CODE_VIDEO:
                presenter.handleVideoResult(resultCode, data);
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
