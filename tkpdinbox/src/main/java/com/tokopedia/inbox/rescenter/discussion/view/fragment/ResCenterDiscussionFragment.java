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
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.ResCenterDiscussionAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.LoadMoreDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenter;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.ArrayList;
import java.util.List;

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
public class ResCenterDiscussionFragment extends BasePresenterFragment<ResCenterDiscussionPresenter>
        implements ResCenterDiscussionView {

    private static final String PARAM_RESOLUTION_ID = "PARAM_RESOLUTION_ID";
    private static final String ARGS_DATA = "ARGS_DATA";
    private static final String ARGS_ATTACHMENT = "ARGS_ATTACHMENT";
    private static final String ARGS_CAN_LOAD_MORE = "ARGS_CAN_LOAD_MORE";
    private static final String ARGS_REPLY = "ARGS_REPLY";
    private ResCenterDiscussionAdapter adapter;
    private AttachmentAdapter attachmentAdapter;
    private RecyclerView discussionList;
    private RecyclerView attachmentList;
    private EditText replyEditText;
    private ImageView sendButton;
    private ImageView attachButton;
    private LinearLayoutManager layoutManager;
    private ImageUploadHandler uploadImageDialog;
    private TkpdProgressDialog progressDialog;
    private Bundle savedInstance;

    public static Fragment createInstance(String resolutionID, boolean flagReceived) {
        Fragment fragment = new ResCenterDiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESOLUTION_ID, resolutionID);
        bundle.putBoolean(PARAM_FLAG_RECEIVED, flagReceived);

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
        presenter = new ResCenterDiscussionPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_res_center_discussion;
    }

    @Override
    protected void initView(View view) {
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
                if (list == null || list.size() == 0)
                    adapter.showEmptyFull(true);
                else {
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
                presenter.sendReply();
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

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadImageDialog.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        uploadImageDialog.actionImagePicker();
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
    protected void initialVar() {
        uploadImageDialog = ImageUploadHandler.createInstance(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSuccessGetDiscussion(List<DiscussionItemViewModel> list,
                                       boolean canLoadMore) {
        setViewEnabled(true);
        finishLoading();
        adapter.setCanLoadMore(canLoadMore);
        if (list.size() > 0)
            adapter.setList(list);
        else
            adapter.showEmptyFull(true);
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
        if (adapter.getData().size() == 0)
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.initData();
                }
            });
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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
        if (requestCode == ImageUploadHandler.REQUEST_CODE) {
            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    if (data != null && data.getStringExtra(ImageGallery.EXTRA_URL) != null) {
                        onAddImageAttachment(data.getStringExtra(ImageGallery.EXTRA_URL));
                    } else {
                        onFailedAddAttachment();
                    }
                    break;
                case Activity.RESULT_OK:
                    if (uploadImageDialog != null && uploadImageDialog.getCameraFileloc() != null) {
                        onAddImageAttachment(uploadImageDialog.getCameraFileloc());
                    } else {
                        onFailedAddAttachment();
                    }
                    break;
            }
        }
    }

    private void onFailedAddAttachment() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.failed_upload_image));
    }

    private void onAddImageAttachment(String fileLoc) {
        attachmentList.setVisibility(View.VISIBLE);
        AttachmentViewModel attachment = new AttachmentViewModel();
        attachment.setFileLoc(fileLoc);
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

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

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
        super.onSaveInstanceState(outState);
    }
}
