package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.NextActionActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResChatFragmentPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by yoasfs on 10/6/17.
 */

@RuntimePermissions
public class DetailResChatFragment
        extends BaseDaggerFragment
        implements DetailResChatFragmentListener.View {

    public static final int NEXT_STATUS_CURRENT = 1;
    private static final int COUNT_MAX_ATTACHMENT = 5;

    public static final String CREATE = "create";
    public static final String EDIT_SOLUTION = "edit_solution";
    public static final String REPLY_CONVERSATION = "reply_conversation";
    public static final String ACCEPT_ADMIN_SOLUTION = "accept_admin_resolution";
    public static final String ACTION_EARLY = "action_early";
    public static final String ACTION_FINAL = "action_final";
    public static final String APPEAL_RESOLUTION = "appeal_resolution";
    public static final String CANCEL = "cancel";
    public static final String EDIT_ADDRESS = "edit_address";
    public static final String EDIT_RESI = "edit_resi";
    public static final String FINISH = "finish";
    public static final String HANDLE_RESOLUTION = "handle_resolution";
    public static final String INPUT_ADDRESS = "input_address";
    public static final String INPUT_RESI = "input_resi";
    public static final String REJECT_ADMIN_RESOLUTION = "reject_admin_resolution";
    public static final String REPORT_RESOLUTION = "report_resolution";
    public static final String ENTER_RETUR_SESSION_RESOLUTION = "enter_retur_session_resolution";
    public static final String ACTION_RESET = "action_reset";


    private TextView tvNextStep;
    private RecyclerView rvChat, rvAttachment;
    private ProgressBar progressBar;
    private LinearLayout mainView;
    private CardView cvNextStep;
    private ChatAdapter chatAdapter;
    private AttachmentAdapter attachmentAdapter;
    private EditText etChat;
    private ImageView ivSend, ivAttachment;
    private View actionButtonLayout;
    private ImageUploadHandler uploadImageDialog;

    private String resolutionId;
    private DetailResChatDomain detailResChatDomain;

    @Inject
    DetailResChatFragmentPresenter presenter;

    public static DetailResChatFragment newBuyerInstance(String resolutionId) {
        DetailResChatFragment fragment = new DetailResChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailResChatActivity.PARAM_RESOLUTION_ID, resolutionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DetailResChatFragment newSellerInstance(String resolutionId) {
        DetailResChatFragment fragment = new DetailResChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailResChatActivity.PARAM_RESOLUTION_ID, resolutionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
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
                    uploadImageDialog.REQUEST_CODE_GALLERY
            );
        } else {
            uploadImageDialog.actionImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DetailResChatFragmentPermissionsDispatcher.onRequestPermissionsResult(DetailResChatFragment.this, requestCode, grantResults);
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
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerResolutionDetailComponent daggerCreateResoComponent =
                (DaggerResolutionDetailComponent) DaggerResolutionDetailComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
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
        state.putString(DetailResChatActivity.PARAM_RESOLUTION_ID, resolutionId);

    }

    @Override
    public void onRestoreState(Bundle savedState) {
        resolutionId = savedState.getString(DetailResChatActivity.PARAM_RESOLUTION_ID);

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resolutionId = arguments.getString(DetailResChatActivity.PARAM_RESOLUTION_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail_res_chat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        uploadImageDialog = ImageUploadHandler.createInstance(getActivity());
        tvNextStep = (TextView) view.findViewById(R.id.tv_next_step);
        rvChat = (RecyclerView) view.findViewById(R.id.rv_chat);
        rvAttachment = (RecyclerView) view.findViewById(R.id.rv_attachment);
        mainView = (LinearLayout) view.findViewById(R.id.main_view);
        cvNextStep = (CardView) view.findViewById(R.id.cv_next_step);
        etChat = (EditText) view.findViewById(R.id.et_chat);
        ivSend = (ImageView) view.findViewById(R.id.iv_send);
        ivAttachment = (ImageView) view.findViewById(R.id.iv_attachment);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        actionButtonLayout = view.findViewById(R.id.layout_action);

        actionButtonLayout.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        presenter.initView(resolutionId);
        presenter.initUploadImageHandler(getActivity(), uploadImageDialog);
        rvChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(new DetailChatTypeFactoryImpl(this));
        rvChat.setAdapter(chatAdapter);
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        attachmentAdapter = AttachmentAdapter.createAdapter(getActivity(), true);
        attachmentAdapter.setListener(getAttachmentAdapterListener());
        rvAttachment.setAdapter(attachmentAdapter);
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
                        if (attachmentAdapter.getList().size() == 0) {
                            rvAttachment.setVisibility(View.GONE);
                        }
                        attachmentAdapter.notifyDataSetChanged();
                    }
                };
            }
        };
    }
    @Override
    protected void setViewListener() {
        cvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NextActionActivity.newInstance(
                        getActivity(),
                        resolutionId,
                        detailResChatDomain.getNextAction()));
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationDomain conversationDomain;
                if (attachmentAdapter.getList().size() == 0) {
                    conversationDomain = getTempConversationDomain(etChat.getText().toString());
                } else {
                    conversationDomain = getTempConversationDomain(etChat.getText().toString(), attachmentAdapter.getList());
                }

                chatAdapter.addItem(new ChatRightViewModel(null, null, conversationDomain));
                chatAdapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                presenter.sendIconPressed(etChat.getText().toString(), attachmentAdapter.getList());
            }
        });

        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachmentAdapter.getList().size() < COUNT_MAX_ATTACHMENT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.dialog_upload_option));
                    builder.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DetailResChatFragmentPermissionsDispatcher.actionImagePickerWithCheck(DetailResChatFragment.this);
                        }
                    }).setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DetailResChatFragmentPermissionsDispatcher.actionCameraWithCheck(DetailResChatFragment.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUploadHandler.REQUEST_CODE:
                presenter.handleDefaultOldUploadImageHandlerResult(resultCode, data);
                break;
            case ImageUploadHandler.REQUEST_CODE_GALLERY:
                presenter.handleNewGalleryResult(resultCode, data);
                break;
            default:
                break;
        }
    }

    private ConversationDomain getTempConversationDomain(String message) {
        return  new ConversationDomain(
                0,
                null,
                message,
                null,
                null,
                getConversationCreateTime(),
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private ConversationDomain getTempConversationDomain(String message, List<AttachmentViewModel> attachmentList) {
        return  new ConversationDomain(
                0,
                null,
                message,
                null,
                null,
                getConversationCreateTime(),
                getConversationAttachmentTemp(attachmentList),
                null,
                null,
                null,
                null,
                null);
    }

    private ConversationCreateTimeDomain getConversationCreateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DateFormatUtils.FORMAT_T_Z);
        return new ConversationCreateTimeDomain(format.format(calendar.getTime()), "");
    }

    private List<ConversationAttachmentDomain> getConversationAttachmentTemp(List<AttachmentViewModel> attachmentList) {
        List<ConversationAttachmentDomain> domainList = new ArrayList<>();
        for (AttachmentViewModel attachment : attachmentList) {
            ConversationAttachmentDomain domain = new ConversationAttachmentDomain(
                    attachment.getFileType() == AttachmentViewModel.FILE_IMAGE ?
                            "image" :
                            "video",
                    attachment.getImgThumb(),
                    attachment.getImgLarge());
            domainList.add(domain);
        }
        return domainList;
    }

    @Override
    public void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void populateView(DetailResChatDomain detailResChatDomain) {
        this.detailResChatDomain = detailResChatDomain;
        mainView.setVisibility(View.VISIBLE);
        initAllData(detailResChatDomain);
    }

    @Override
    public void errorInputMessage(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void successGetConversation(DetailResChatDomain detailResChatDomain) {
        this.detailResChatDomain = detailResChatDomain;
        mainView.setVisibility(View.VISIBLE);
        initAllData(detailResChatDomain);
    }

    @Override
    public void errorGetConversation(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.loadConversation(resolutionId);
            }
        });
    }

    private void initAllData(DetailResChatDomain detailResChatDomain) {
        initNextStep(detailResChatDomain.getNextAction());
        initActionButton(detailResChatDomain.getButton());
        initChatData(detailResChatDomain);
    }

    private void initNextStep(NextActionDomain nextActionDomain) {
        for (NextActionDetailStepDomain nextStep : nextActionDomain.getDetail().getStep()) {
            if (nextStep.getStatus() == NEXT_STATUS_CURRENT) {
                tvNextStep.setText(nextStep.getName());
            }
        }
    }

    private void initActionButton(final ButtonDomain buttonDomain) {
        actionButtonLayout.setVisibility(View.GONE);
        if (buttonDomain.getReport() == 1
                || buttonDomain.getCancel() == 1
                || buttonDomain.getEdit() == 1
                || buttonDomain.getInputAddress() == 1
                || buttonDomain.getAppeal() == 1
                || buttonDomain.getInputAWB() == 1
                || buttonDomain.getAccept() == 1
                || buttonDomain.getAcceptReturn() == 1
                || buttonDomain.getAcceptByAdmin() == 1
                || buttonDomain.getAcceptByAdminReturn() == 1
                || buttonDomain.getFinish() == 1
                || buttonDomain.getRecomplaint() == 1) {
            actionButtonLayout.setVisibility(View.VISIBLE);
            LinearLayout llActionButton = (LinearLayout) actionButtonLayout.findViewById(R.id.ll_action_button);
            llActionButton.addView(addButtonSeparator());

            if (buttonDomain.getReport() == 1) {
                Button button = getChatActionButton(buttonDomain.getReportLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getCancel() == 1) {
                Button button = getChatActionButton(buttonDomain.getCancelLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getEdit() == 1) {
                Button button = getChatActionButton(buttonDomain.getEditLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getInputAddress() == 1) {
                Button button = getChatActionButton(buttonDomain.getInputAddressLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getAppeal() == 1) {
                Button button = getChatActionButton(buttonDomain.getAppealLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getInputAWB() == 1) {
                Button button = getChatActionButton(buttonDomain.getInputAWBLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getAccept() == 1) {
                Button button = getChatActionButton(buttonDomain.getAcceptLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAcceptSolutionDialog(buttonDomain.getAcceptText());
                    }
                });
            }

            if (buttonDomain.getAcceptReturn() == 1) {
                Button button = getChatActionButton(buttonDomain.getAcceptReturnLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getAcceptByAdmin() == 1) {
                Button button = getChatActionButton(buttonDomain.getAcceptByAdminLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getAcceptByAdminReturn() == 1) {
                Button button = getChatActionButton(buttonDomain.getAcceptByAdminReturnLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getFinish() == 1) {
                Button button = getChatActionButton(buttonDomain.getFinishLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }

            if (buttonDomain.getRecomplaint() == 1) {
                Button button = getChatActionButton(buttonDomain.getRecomplainLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
            }
        }
    }

    private Button getChatActionButton(String name) {
        Button button = new Button(getActivity());
        button.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_chat_button_action));
        button.setTextColor(getActivity().getResources().getColor(R.color.tkpd_main_green));
        button.setLayoutParams(new LinearLayout.LayoutParams((int)getActivity().getResources().getDimension(R.dimen.dimens_chat_action_button_width),
                (int)getActivity().getResources().getDimension(R.dimen.dimens_chat_action_button_height)));
        button.setText(name);
        button.setAllCaps(false);
        return button;
    }

    private View addButtonSeparator() {
        View spaceView = new View(getActivity());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.margin_vvs),
                LinearLayout.LayoutParams.MATCH_PARENT));
        return spaceView;
    }

    private void initChatData(DetailResChatDomain detailResChatDomain) {
        int lastAction = 0;
        for (ConversationDomain conversationDomain : detailResChatDomain.getConversation()) {
            String actionType = conversationDomain.getAction().getType();
            if (actionType.equals(CREATE)) {
                chatAdapter.addItem(new ChatCreateLeftViewModel
                        (detailResChatDomain.getShop(),
                                detailResChatDomain.getLast(),
                                conversationDomain));
            } else if (actionType.equals(REPLY_CONVERSATION)) {
                boolean isShowTitle;
                if (lastAction == conversationDomain.getAction().getBy()) {
                    isShowTitle = false;
                } else {
                    lastAction = conversationDomain.getAction().getBy();
                    isShowTitle = true;
                }
                if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                    chatAdapter.addItem(new ChatRightViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain));
                } else {
                    chatAdapter.addItem(new ChatLeftViewModel(
                            detailResChatDomain.getShop(),
                            detailResChatDomain.getCustomer(),
                            conversationDomain,
                            isShowTitle));
                }
            } else {
                if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                    chatAdapter.addItem(new ChatNotSupportedRightViewModel(
                            conversationDomain));
                } else {
                    chatAdapter.addItem(new ChatNotSupportedLeftViewModel(
                            conversationDomain));
                }
            }

        }
        chatAdapter.notifyDataSetChanged();
    }

    private void showAcceptSolutionDialog(String acceptText) {
        final Dialog dialog = new Dialog(getActivity());
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnAccept = (Button) dialog.findViewById(R.id.btn_accept_solution);
        tvSolution.setText(MethodChecker.fromHtml(acceptText));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showSnackBarError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void addAttachmentFile(AttachmentViewModel attachment) {
        attachmentAdapter.addImage(attachment);
        if (attachmentAdapter.getList().size() != 0) {
            rvAttachment.setVisibility(View.VISIBLE);
        }
        attachmentAdapter.notifyDataSetChanged();
    }

    @Override
    public List<AttachmentViewModel> getAttachmentListFromAdapter() {
        return attachmentAdapter.getList();
    }

    @Override
    public void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel) {
        attachmentAdapter.getList().clear();
        attachmentAdapter.notifyDataSetChanged();
        rvAttachment.setVisibility(View.GONE);
        etChat.setText("");
    }

    @Override
    public void errorReplyDiscussion(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
        chatAdapter.deleteLastItem();
        etChat.requestFocus();
    }

    @Override
    public void successAcceptSolution() {
        dismissProgressBar();
        presenter.initView(resolutionId);
    }

    @Override
    public void errorAcceptSolution(String error) {
        dismissProgressBar();
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
