package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.AttachmentFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.io.File;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by yoasfs on 30/08/17.
 */

@RuntimePermissions
public class AttachmentFragment extends BaseDaggerFragment implements AttachmentFragmentListener.View, AttachmentAdapterListener {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    private static final int REQUEST_CODE_GALLERY = 1243;
    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;

    private TkpdTextInputLayout tilInformation;
    private EditText etInformation;
    private Button btnContinue;
    private RecyclerView rvAttachment;

    private ResultViewModel resultViewModel;
    private AttachmentFragmentPresenter presenter;
    private AttachmentAdapter adapter;
    private ImageUploadHandler uploadImageDialog;


    private String[] extensions = {
            "jpg", "jpeg", "png", "mp4", "m4v", "mov", "ogv"
    };

    public static AttachmentFragment newInstance(ResultViewModel resultViewModel) {
        AttachmentFragment fragment = new AttachmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new AttachmentFragmentPresenter(getActivity(), this);
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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

    }

    @Override
    public void onRestoreState(Bundle savedState) {

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
        tilInformation = (TkpdTextInputLayout) view.findViewById(R.id.til_information);
        etInformation = (EditText) view.findViewById(R.id.et_information);
        btnContinue = (Button) view.findViewById(R.id.btn_upload);
        rvAttachment = (RecyclerView) view.findViewById(R.id.rv_attachment);
        adapter = new AttachmentAdapter(context, this);
        uploadImageDialog = ImageUploadHandler.createInstance(this);

        rvAttachment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvAttachment.setAdapter(adapter);
        tilInformation.setHint(context.getResources().getString(R.string.string_information));
        presenter.initResultViewModel(resultViewModel);

    }

    @Override
    public void populateDataToView() {

    }

    @Override
    public void updateView(Attachment attachment) {
        boolean isComplete = true;
        if (attachment.information.length() < 30) {
            tilInformation.setError(context.getResources().getString(R.string.string_min_30_char));
            isComplete = false;
        } else {
            tilInformation.hideErrorSuccess();
        }

        if (isComplete) {
            buttonSelected(btnContinue);
        } else {
            buttonDisabled(btnContinue);
        }

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
        button.setTextColor(ContextCompat.getColor(context, R.color.black_70));
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

            }
        });
    }

    @Override
    public void onAddAttachmentClicked() {

    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadImageDialog.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.RESOLUTION_CENTER_UPLOAD_VIDEO).equals("true")) {
            startActivityForResult(
                    GalleryActivity.createIntent(getActivity()),
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
                handleDefaultOldUploadImageHandlerResult(resultCode, data);
                break;
            case REQUEST_CODE_GALLERY:
                handleNewGalleryResult(resultCode, data);
                break;
            default:
                break;
        }
    }

    private void handleDefaultOldUploadImageHandlerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case GalleryBrowser.RESULT_CODE:
                if (data != null && data.getStringExtra(ImageGallery.EXTRA_URL) != null) {
                    onAddImageAttachment(data.getStringExtra(ImageGallery.EXTRA_URL), AttachmentViewModel.FILE_IMAGE);
                } else {
                    onFailedAddAttachment();
                }
                break;
            case Activity.RESULT_OK:
                if (uploadImageDialog != null && uploadImageDialog.getCameraFileloc() != null) {
                    onAddImageAttachment(uploadImageDialog.getCameraFileloc(), AttachmentViewModel.FILE_IMAGE);
                } else {
                    onFailedAddAttachment();
                }
                break;
        }
    }

    private void handleNewGalleryResult(int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (data != null && data.getParcelableExtra("EXTRA_RESULT_SELECTION") != null) {
//                MediaItem item = data.getParcelableExtra("EXTRA_RESULT_SELECTION");
//                if (checkAttachmentValidation(item)) {
//                    onAddImageAttachment(item.getRealPath(), getTypeFile(item));
//                }
//            } else {
//                onFailedAddAttachment();
//            }
//        }
    }

    private void onFailedAddAttachment() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.failed_upload_image));
    }

    private void onAddImageAttachment(String fileLoc, int typeFile) {
//        attachmentList.setVisibility(View.VISIBLE);
//        AttachmentViewModel attachment = new AttachmentViewModel();
//        attachment.setFileLoc(fileLoc);
//        attachment.setFileType(typeFile);
//        attachmentAdapter.addImage(attachment);
    }

    private boolean checkAttachmentValidation(MediaItem item) {
//        boolean isExtensionAllow = false;
//        for (String extension : extensions) {
//            String path = item.getRealPath();
//            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
//                Log.d("hangnadi validation", "checkAttachmentValidation: " + extension + "\npath : " + path);
//                isExtensionAllow = true;
//            }
//        }
//        if (!isExtensionAllow) {
//            NetworkErrorHelper.showSnackbar(
//                    getActivity(),
//                    getActivity().getString(R.string.error_reply_discussion_resolution_file_not_allowed)
//            );
//            return false;
//        }
//
//        int countVideoAlreadyAdded = 0;
//        if (item.isVideo()) {
//            for (AttachmentViewModel model :attachmentAdapter.getList()) {
//                if (model.isVideo()) {
//                    countVideoAlreadyAdded++;
//                }
//            }
//        }
//        if (countVideoAlreadyAdded == MAXIMAL_VIDEO_CONTENT_ALLOW) {
//            NetworkErrorHelper.showSnackbar(
//                    getActivity(),
//                    getActivity().getString(R.string.error_reply_discussion_resolution_reach_max)
//            );
//            return false;
//        }
//
//        if (item.isVideo()) {
//            File file = new File(item.getRealPath());
//            long length = file.length() / 1024;
//            if (length >= 20000) {
//                NetworkErrorHelper.showSnackbar(
//                        getActivity(),
//                        getActivity().getString(R.string.error_reply_discussion_resolution_reach_max_size)
//                );
//                return false;
//            }
//        }

        return true;
    }
}
