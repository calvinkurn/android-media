package com.tokopedia.inbox.rescenter.discussion.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.gallery.GalleryType;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.discussion.di.component.DaggerResolutionDiscussionComponent;
import com.tokopedia.inbox.rescenter.discussion.di.component.ResolutionDiscussionComponent;
import com.tokopedia.inbox.rescenter.discussion.di.module.ResolutionDiscussionModule;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.ResCenterDiscussionAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.LoadMoreDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase.PARAM_FLAG_RECEIVED;

/**
 * Created by nisie on 3/29/17.
 */

@RuntimePermissions
public class ResCenterDiscussionFragment extends BaseDaggerFragment
        implements ResCenterDiscussionView {

    private static final String PARAM_RESOLUTION_ID = "PARAM_RESOLUTION_ID";
    private static final String ARGS_DATA = "ARGS_DATA";
    private static final String ARGS_ATTACHMENT = "ARGS_ATTACHMENT";
    private static final String ARGS_CAN_LOAD_MORE = "ARGS_CAN_LOAD_MORE";
    private static final String ARGS_REPLY = "ARGS_REPLY";
    private static final String PARAM_FLAG_ALLOW_REPLY = "PARAM_FLAG_ALLOW_REPLY";
    private static final String PARAM_IS_ERROR = "PARAM_IS_ERROR";
    private static final String PARAM_STRING_ERROR = "PARAM_STRING_ERROR";
    private static final int REQUEST_CODE_GALLERY = 1243;
    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;
    private ResCenterDiscussionAdapter adapter;
    private AttachmentAdapter attachmentAdapter;
    private RecyclerView discussionList;
    private RecyclerView attachmentList;
    private EditText replyEditText;
    private ImageView sendButton;
    private ImageView attachButton;
    private View replyView;
    private LinearLayoutManager layoutManager;
    private ImageUploadHandler uploadImageDialog;
    private TkpdProgressDialog progressDialog;
    private Bundle savedInstance;
    private boolean flagAllowReply;
    private boolean isError;
    private String errorMessage;

    @Inject
    ResCenterDiscussionPresenterImpl presenter;
    private String[] extensions = {
            "jpg", "jpeg", "png", "mp4", "m4v", "mov", "ogv"
    };

    public static Fragment createInstance(String resolutionID, boolean flagReceived, boolean flagAllowReply) {
        Fragment fragment = new ResCenterDiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESOLUTION_ID, resolutionID);
        bundle.putBoolean(PARAM_FLAG_RECEIVED, flagReceived);
        bundle.putBoolean(PARAM_FLAG_ALLOW_REPLY, flagAllowReply);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = new Bundle();
        if (savedInstanceState != null) {
            savedInstance.putAll(savedInstanceState);
        }
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putBoolean(PARAM_FLAG_ALLOW_REPLY, flagAllowReply);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        flagAllowReply = savedState.getBoolean(PARAM_FLAG_ALLOW_REPLY);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_DISCUSSION;
    }

    @Override
    protected void initInjector() {
        ResolutionDetailComponent resolutionDetailComponent = getComponent(ResolutionDetailComponent.class);
        ResolutionDiscussionComponent discussionComponent =
                DaggerResolutionDiscussionComponent.builder()
                        .resolutionDetailComponent(resolutionDetailComponent)
                        .resolutionDiscussionModule(new ResolutionDiscussionModule(this))
                        .build();
        discussionComponent.inject(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        flagAllowReply = arguments.getBoolean(PARAM_FLAG_ALLOW_REPLY);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_res_center_discussion;
    }

    @Override
    protected void initView(View view) {
        replyView = view.findViewById(R.id.reply_view);
        replyEditText = (EditText) view.findViewById(R.id.reply_box);
        sendButton = (ImageView) view.findViewById(R.id.send_button);
        attachButton = (ImageView) view.findViewById(R.id.attach_but);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = ResCenterDiscussionAdapter.createAdapter(getActivity(), onLoadMore());
        discussionList = (RecyclerView) view.findViewById(R.id.discussion_list);
        discussionList.setLayoutManager(layoutManager);
        discussionList.setAdapter(adapter);
        attachmentList = (RecyclerView) view.findViewById(R.id.list_image_upload);
        attachmentAdapter = AttachmentAdapter.createAdapter(getActivity(), true);
        attachmentAdapter.setListener(getAttachmentAdapterListener());
        attachmentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        attachmentList.setAdapter(attachmentAdapter);
        restoreDataFromInstanceState(savedInstance);
    }

    private void restoreDataFromInstanceState(Bundle savedInstance) {
        if (savedInstance != null) {
            if (savedInstance.getParcelableArrayList(ARGS_DATA) != null) {
                List<DiscussionItemViewModel> list = savedInstance.getParcelableArrayList(ARGS_DATA);
                if (list == null || list.size() == 0) {
                    if (savedInstance.getBoolean(PARAM_IS_ERROR, false)) {
                        onErrorGetDiscussion(savedInstance.getString(PARAM_STRING_ERROR));
                    } else {
                        adapter.showEmptyFull(true);
                    }
                } else {
                    adapter.setList(list);
                    adapter.setCanLoadMore(savedInstance.getBoolean(ARGS_CAN_LOAD_MORE, false));
                }
            }
            if (savedInstance.getParcelableArrayList(ARGS_ATTACHMENT) != null) {
                List<AttachmentViewModel> list = savedInstance.getParcelableArrayList(ARGS_ATTACHMENT);
                if (list == null || list.size() == 0)
                    attachmentList.setVisibility(View.GONE);
                else {
                    attachmentList.setVisibility(View.VISIBLE);
                    attachmentAdapter.addList(list);
                }
            }
            replyEditText.setText(savedInstance.getString(ARGS_REPLY,""));
        }
    }

    private AttachmentAdapter.ProductImageListener getAttachmentAdapterListener() {
        return new AttachmentAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onImageClicked(final int position, AttachmentViewModel imageUpload) {
                return null;
            }

            @Override
            public View.OnClickListener onDeleteImage(final int position, AttachmentViewModel imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attachmentAdapter.getList().remove(position);
                        attachmentAdapter.notifyDataSetChanged();
                    }
                };
            }
        };
    }

    private LoadMoreDataBinder.LoadMoreListener onLoadMore() {
        return new LoadMoreDataBinder.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }
        };
    }

    @Override
    protected void setViewListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTemporaryMessage();
                setViewEnabled(false);

                presenter.setDiscussionText(replyEditText.getText().toString());
                presenter.setAttachment(attachmentAdapter.getList());
                presenter.setResolutionId(getResolutionID());
                presenter.setFlagReceived(getFlagReceived());
                if (TrackingUtils.getGtmString(AppEventTracking.GTM.RESOLUTION_CENTER_UPLOAD_VIDEO).equals("true")) {
                    presenter.sendReplySupportVideo();
                } else {
                    presenter.sendReply();
                }
            }
        });

        replyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                replyEditText.setError(null);
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachmentAdapter.getList().size() < 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.dialog_upload_option));
                    builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ResCenterDiscussionFragmentPermissionsDispatcher.actionImagePickerWithCheck(ResCenterDiscussionFragment.this);
                        }
                    }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ResCenterDiscussionFragmentPermissionsDispatcher.actionCameraWithCheck(ResCenterDiscussionFragment.this);
                        }
                    });

                    Dialog dialog = builder.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.max_upload_detail_res_center));
                }
            }
        });

    }

    private boolean getFlagReceived() {
        return getArguments().getBoolean(PARAM_FLAG_RECEIVED);
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

    private void addTemporaryMessage() {
        adapter.showEmpty(false);
        DiscussionItemViewModel tempMessage = new DiscussionItemViewModel();
        tempMessage.setMessage(replyEditText.getText().toString());

        if (attachmentAdapter.getList().size() > 0)
            tempMessage.setAttachment(attachmentAdapter.getList());

        adapter.addReply(tempMessage);
        scrollToBottom();
    }

    private void scrollToBottom() {
        layoutManager.scrollToPosition(adapter.getItemCount() - 1);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadImageDialog = ImageUploadHandler.createInstance(this);
        replyView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetDiscussion(List<DiscussionItemViewModel> list,
                                       boolean canLoadMore) {
        setViewEnabled(true);
        finishLoading();
        adapter.setCanLoadMore(canLoadMore);
        replyView.setVisibility(flagAllowReply ? View.VISIBLE : View.GONE);
        if (list.size() > 0) {
            adapter.setList(list);
        } else {
            adapter.showEmptyFull(true);
        }
    }

    @Override
    public void onSuccessSendReply(DiscussionItemViewModel discussionItemViewModel) {
        attachmentAdapter.getList().clear();
        attachmentAdapter.notifyDataSetChanged();
        attachmentList.setVisibility(View.GONE);
        adapter.showEmpty(false);
        setViewEnabled(true);
        replyEditText.setText("");
        adapter.remove(adapter.getData().size() - 1);
        adapter.addReply(discussionItemViewModel);
        adapter.notifyDataSetChanged();
        finishLoadingProgress();
        scrollToBottom();
    }

    private void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public String getResolutionID() {
        return getArguments().getString(PARAM_RESOLUTION_ID) != null ?
                getArguments().getString(PARAM_RESOLUTION_ID) : "";
    }

    @Override
    public void onErrorSendReply(String errorMessage) {
        finishLoadingProgress();
        setViewEnabled(true);
        adapter.remove(adapter.getData().size() - 1);
        adapter.notifyDataSetChanged();
        if (adapter.getData().size() == 0)
            adapter.showEmptyFull(true);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        replyEditText.requestFocus();
    }

    @Override
    public void finishLoading() {
        if (adapter.isLoading()) {
            adapter.showLoading(false);
        }
    }

    @Override
    public void showLoading() {
        if (adapter.getData().size() == 0) {
            adapter.showLoadingFull(true);
        } else {
            adapter.showLoading(true);
        }
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
        replyEditText.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        attachButton.setEnabled(isEnabled);
    }

    @Override
    public void onErrorGetDiscussion(String errorMessage) {
        finishLoading();
        if (adapter.getData().size() == 0) {
            isError = true;
            this.errorMessage = errorMessage;
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    isError = false;
                    presenter.initData();
                }
            });
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }
    }

    @Override
    public void onSuccessLoadMore(List<DiscussionItemViewModel> discussionItemViewModels,
                                  boolean canLoadMore) {
        setViewEnabled(true);
        finishLoading();
        adapter.setCanLoadMore(canLoadMore);
        adapter.setList(discussionItemViewModels);
    }

    @Override
    public String getLastConversationId() {
        return adapter.getData().get(0).getConversationId();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
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

    private void handleNewGalleryResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getParcelableExtra("EXTRA_RESULT_SELECTION") != null) {
                MediaItem item = data.getParcelableExtra("EXTRA_RESULT_SELECTION");
                if (checkAttachmentValidation(item)) {
                    onAddImageAttachment(item.getRealPath(), getTypeFile(item));
                }
            } else {
                onFailedAddAttachment();
            }
        }
    }

    private boolean checkAttachmentValidation(MediaItem item) {
        boolean isExtensionAllow = false;
        for (String extension : extensions) {
            String path = item.getRealPath();
            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
                Log.d("hangnadi validation", "checkAttachmentValidation: " + extension + "\npath : " + path);
                isExtensionAllow = true;
            }
        }
        if (!isExtensionAllow) {
            NetworkErrorHelper.showSnackbar(
                    getActivity(),
                    getActivity().getString(R.string.error_reply_discussion_resolution_file_not_allowed)
            );
            return false;
        }

        int countVideoAlreadyAdded = 0;
        if (item.isVideo()) {
            for (AttachmentViewModel model :attachmentAdapter.getList()) {
                if (model.isVideo()) {
                    countVideoAlreadyAdded++;
                }
            }
        }
        if (countVideoAlreadyAdded == MAXIMAL_VIDEO_CONTENT_ALLOW) {
            NetworkErrorHelper.showSnackbar(
                    getActivity(),
                    getActivity().getString(R.string.error_reply_discussion_resolution_reach_max)
            );
            return false;
        }

        if (item.isVideo()) {
            File file = new File(item.getRealPath());
            long length = file.length() / 1024;
            if (length >= 20000) {
                NetworkErrorHelper.showSnackbar(
                        getActivity(),
                        getActivity().getString(R.string.error_reply_discussion_resolution_reach_max_size_video)
                );
                return false;
            }
        }

        return true;
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

    private int getTypeFile(MediaItem item) {
        if (item.isVideo()) {
            return AttachmentViewModel.FILE_VIDEO;
        } else if (item.isImage()) {
            return AttachmentViewModel.FILE_IMAGE;
        } else {
            return AttachmentViewModel.UNKNOWN;
        }
    }

    private void onFailedAddAttachment() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.failed_upload_image));
    }

    private void onAddImageAttachment(String fileLoc, int typeFile) {
        attachmentList.setVisibility(View.VISIBLE);
        AttachmentViewModel attachment = new AttachmentViewModel();
        attachment.setFileLoc(fileLoc);
        attachment.setFileType(typeFile);
        attachmentAdapter.addImage(attachment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribeObservable();
        progressDialog = null;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ResCenterDiscussionFragmentPermissionsDispatcher.onRequestPermissionsResult(ResCenterDiscussionFragment.this, requestCode, grantResults);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARGS_DATA,
                new ArrayList<DiscussionItemViewModel>(adapter.getData()));
        outState.putParcelableArrayList(ARGS_ATTACHMENT,
                new ArrayList<AttachmentViewModel>(attachmentAdapter.getList()));
        outState.putString(ARGS_REPLY, replyEditText.getText().toString());
        outState.putBoolean(ARGS_CAN_LOAD_MORE, adapter.canLoadMore());
        outState.putBoolean(PARAM_IS_ERROR, isError);
        outState.putString(PARAM_STRING_ERROR, errorMessage);
        super.onSaveInstanceState(outState);
    }
}
