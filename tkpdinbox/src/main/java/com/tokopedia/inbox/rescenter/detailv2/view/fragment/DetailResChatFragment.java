package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.VideoPickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.imagepicker.picker.main.view.VideoPickerActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.NextActionActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.TrackShippingActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResChatFragmentPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationProductDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;
import com.tokopedia.inbox.rescenter.product.ListProductActivity;
import com.tokopedia.inbox.rescenter.product.ProductDetailActivity;
import com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatFragment
        extends BaseDaggerFragment
        implements DetailResChatFragmentListener.View {

    public static final int NEXT_STATUS_CURRENT = 1;
    private static final int COUNT_MAX_ATTACHMENT = 5;
    private static final int REQUEST_EDIT_SOLUTION = 123;
    private static final int REQUEST_APPEAL_SOLUTION = 234;
    private static final int REQUEST_INPUT_SHIPPING = 345;
    private static final int REQUEST_EDIT_SHIPPING = 456;
    private static final int REQUEST_CHOOSE_ADDRESS = 678;
    private static final int REQUEST_CHOOSE_ADDRESS_MIGRATE_VERSION = 789;
    private static final int REQUEST_CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION = 890;
    private static final int REQUEST_EDIT_ADDRESS = 901;

    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    private static final int TOP_POSITION = 0;
    private static final int REQUEST_CODE_IMAGE_REPUTATION = 423;
    private static final int REQUEST_CODE_VIDEO = 238;
    public static final int MAX_VIDEO_SIZE_IN_KB = 20000;

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
    private FloatingActionButton fabChat;
    private ImageView ivNextStepStatic;
    private GlowingView glowingView;
    private FrameLayout ffChat;
    private ConversationDomain conversationDomain;

    private DetailResChatDomain detailResChatDomain;
    private LinearLayoutManager linearLayoutManager;
    private String resolutionId;
    private String lastConvId;
    private boolean isLoadingMore = false;
    private int oldAddressId;
    private int conversationId;
    private int buttonWidth;

    private Dialog resCenterDialog;
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
        tvNextStep = view.findViewById(R.id.tv_next_step);
        rvChat = view.findViewById(R.id.rv_chat);
        rvAttachment = view.findViewById(R.id.rv_attachment);
        mainView = view.findViewById(R.id.main_view);
        cvNextStep = view.findViewById(R.id.cv_next_step);
        etChat = view.findViewById(R.id.et_chat);
        ivSend = view.findViewById(R.id.iv_send);
        ivAttachment = view.findViewById(R.id.iv_attachment);
        progressBar = view.findViewById(R.id.progress_bar);
        actionButtonLayout = view.findViewById(R.id.layout_action);
        fabChat = view.findViewById(R.id.fab_chat);
        ffChat = view.findViewById(R.id.ff_chat);
        ivNextStepStatic = view.findViewById(R.id.iv_next_step_static);
        glowingView = view.findViewById(R.id.view_glowing);

        presenter.initContext(getActivity());

        fabChat.hide();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(new DetailChatTypeFactoryImpl(this));
        rvChat.setAdapter(chatAdapter);
        rvAttachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        attachmentAdapter = AttachmentAdapter.createAdapter(getActivity(), true);
        attachmentAdapter.setListener(getAttachmentAdapterListener());
        rvAttachment.setAdapter(attachmentAdapter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        buttonWidth = (displayMetrics.widthPixels / 4) - (1 * (int) getResources().getDimension(R.dimen.margin_vs));
        ffChat.setVisibility(View.GONE);
        initView();
    }

    private RecyclerView.OnScrollListener recyclerViewListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //hide FAB when reach bottom
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    fabChat.hide();
                } else {
                    UnifyTracking.eventTracking(getActivity(),
                            InboxAnalytics.eventResoChatImpressionGreenArrow(resolutionId));
                    fabChat.show();
                }

                int topVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (topVisibleItemPosition <= 1 &&
                        detailResChatDomain.getConversationList().getCanLoadMore() == 1 &&
                        !isLoadingMore) {
                    ConversationDomain topConversation = detailResChatDomain.getConversationList().
                            getConversationDomains().
                            get(0);
                    lastConvId = String.valueOf(topConversation.getResConvId());
                    presenter.doLoadMore(resolutionId, lastConvId, detailResChatDomain);
                    isLoadingMore = true;
                }
            }
        };
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
                        if (attachmentAdapter.isClickable) {
                            if (attachmentAdapter.getList().get(position) != null) {
                                attachmentAdapter.getList().remove(position);
                            }
                            if (attachmentAdapter.getList().size() == 0) {
                                rvAttachment.setVisibility(View.GONE);
                                initActionButton(detailResChatDomain.getButton());
                            }
                            attachmentAdapter.notifyItemRemoved(position);
                        }
                    }
                };
            }
        };
    }

    private void initView() {
        chatAdapter.clearData();
        actionButtonLayout.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        glowingView.setVisibility(View.GONE);
        ivNextStepStatic.setVisibility(View.GONE);
        presenter.initView(resolutionId);
    }

    @Override
    protected void setViewListener() {
        rvChat.addOnScrollListener(recyclerViewListener());
        cvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatClickNextAction(resolutionId));
                startActivity(NextActionActivity.newInstance(
                        getActivity(),
                        resolutionId,
                        detailResChatDomain.getNextAction(),
                        detailResChatDomain.getResolution().getStatus()));
                getBottomSheetActivityTransition();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendIconPressed(etChat.getText().toString(), attachmentAdapter.getList());
            }
        });

        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachmentAdapter.getList().size() < COUNT_MAX_ATTACHMENT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.dialog_upload_option));
                    builder.setPositiveButton(context.getString(R.string.title_video), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openVideoPicker();
                        }
                    }).setNegativeButton(context.getString(R.string.title_image), new DialogInterface.OnClickListener() {
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
        });

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickGreenArrow(resolutionId));
                scrollChatToBottom(false);
            }
        });
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

    private void openVideoPicker() {
        if (presenter.isDeviceSupportVideo() && presenter.isAllowToAddMoreVideo()) {
            VideoPickerBuilder builder = new VideoPickerBuilder(getString(R.string.choose_video), MAX_VIDEO_SIZE_IN_KB,
                    0, null);
            Intent intent = VideoPickerActivity.getIntent(getActivity(), builder);
            startActivityForResult(intent, REQUEST_CODE_VIDEO);
        }
    }

    @Override
    public void showDummyText() {
        ConversationDomain conversationDomain =
                getTempConversationDomain(etChat.getText().toString(), attachmentAdapter.getList());
        chatAdapter.addItem(new ChatRightViewModel(null, null, conversationDomain));
        chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
        scrollChatToBottom(false);
    }

    private void scrollChatToBottom(boolean isInitChat) {
        rvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    private ConversationDomain getTempConversationDomain(String message, List<AttachmentViewModel> attachmentList) {
        conversationDomain = new ConversationDomain(
                0,
                null,
                message.replaceAll("(\r\n|\n)", "<br />"),
                null,
                null,
                getDummySendingMessage(),
                attachmentList != null ?
                        getConversationAttachmentTemp(attachmentList)
                        : null,
                null,
                null,
                null,
                null,
                null);
        return conversationDomain;
    }

    private ConversationCreateTimeDomain getConversationCreateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DateFormatUtils.FORMAT_RESO);
        return new ConversationCreateTimeDomain(format.format(calendar.getTime()) + " WIB", "");
    }

    private ConversationCreateTimeDomain getDummySendingMessage() {
        return new ConversationCreateTimeDomain(context.getResources().getString(R.string.string_sending_message), "");
    }

    private List<ConversationAttachmentDomain> getConversationAttachmentTemp(List<AttachmentViewModel> attachmentList) {
        List<ConversationAttachmentDomain> domainList = new ArrayList<>();
        for (AttachmentViewModel attachment : attachmentList) {
            ConversationAttachmentDomain domain = new ConversationAttachmentDomain(
                    attachment.getFileType() == AttachmentViewModel.FILE_IMAGE ?
                            ConversationAttachmentDomain.TYPE_IMAGE :
                            ConversationAttachmentDomain.TYPE_VIDEO,
                    attachment.getFileLoc(),
                    attachment.getFileLoc());
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
    public void showChatProgressBar() {
        chatAdapter.showLoading();
    }

    @Override
    public void dismissChatProgressBar() {
        chatAdapter.removeLoading();
    }

    @Override
    public void errorInputMessage(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
        enableIvSend();
    }

    @Override
    public void successGetConversation(DetailResChatDomain detailResChatDomain) {
        this.detailResChatDomain = detailResChatDomain;
        mainView.setVisibility(View.VISIBLE);
        if (detailResChatDomain.getResolution().getStatus() == STATUS_CANCEL ||
                detailResChatDomain.getResolution().getStatus() == STATUS_FINISHED) {
            disableIvSend();
            etChat.clearFocus();
            ivNextStepStatic.setVisibility(View.VISIBLE);
        } else {
            ffChat.setVisibility(View.VISIBLE);
            enableIvSend();
            glowingView.setVisibility(View.VISIBLE);
            glowingView.renderData(new Object());

        }
    }

    @Override
    public void errorGetConversation(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.loadConversation(resolutionId);
            }
        });
    }

    @Override
    public void successGetConversationMore(ConversationListDomain conversationListDomain) {
        this.detailResChatDomain.getConversationList()
                .setCanLoadMore(conversationListDomain.getCanLoadMore());
        this.detailResChatDomain.getConversationList()
                .getConversationDomains().addAll(TOP_POSITION, conversationListDomain.getConversationDomains());
        isLoadingMore = false;
    }

    @Override
    public void errorGetConversationMore(String error) {
        if (resolutionId != null && lastConvId != null) {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.doLoadMore(resolutionId, lastConvId, detailResChatDomain);
                }
            }).showRetrySnackbar();
        } else {
            isLoadingMore = false;
        }
    }

    @Override
    public void initNextStep(NextActionDomain nextActionDomain) {
        for (NextActionDetailStepDomain nextStep : nextActionDomain.getDetail().getStep()) {
            if (nextStep.getStatus() == NEXT_STATUS_CURRENT) {
                tvNextStep.setText(nextStep.getName());
            }
        }
    }

    @Override
    public void initActionButton(final ButtonDomain buttonDomain) {
        actionButtonLayout.setVisibility(View.GONE);
        boolean isAcceptShown = false;
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
            LinearLayout llActionButton = actionButtonLayout.findViewById(R.id.ll_action_button);
            llActionButton.removeAllViews();
            llActionButton.addView(addButtonSeparator());

            if (buttonDomain.getReport() == 1) {
                final Button button = getChatActionButton(buttonDomain.getReportLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickAskHelp(resolutionId));
                        showActionDialog(buttonDomain.getReportLabel(), buttonDomain
                                .getReportText(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                presenter.actionAskHelp();
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        });
                    }
                });
                UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatImpressionAskHelp(resolutionId));
            }

            if (buttonDomain.getCancel() == 1) {
                Button button = getChatActionButton(buttonDomain.getCancelLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatClickCancelComplaint(resolutionId));
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatImpressionCancelComplaintDialog(resolutionId));
                        showActionDialog(buttonDomain.getCancelLabel(), buttonDomain
                                .getCancelText(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                presenter.actionCancelComplaint();
                                UnifyTracking.eventTracking(getActivity(),
                                        InboxAnalytics.eventResoChatClickYesCancelComplaintDialog(resolutionId));
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UnifyTracking.eventTracking(getActivity(),
                                        InboxAnalytics.eventResoChatClickBackCancelComplaintDialog(resolutionId));
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        });
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionCancelComplaint(resolutionId));
            }

            if (buttonDomain.getEdit() == 1) {
                Button button = getChatActionButton(buttonDomain.getEditLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatClickChangeSolution(resolutionId));
                        doEditSolution();
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionChangeSolution(resolutionId));
            }

            if (buttonDomain.getInputAddress() == 1) {
                Button button = getChatActionButton(buttonDomain.getInputAddressLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatClickInputAddress(resolutionId));
                        doInputAddress();
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionInputAddress(resolutionId));
            }

            if (buttonDomain.getAppeal() == 1) {
                Button button = getChatActionButton(buttonDomain.getAppealLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatClickAppeal(resolutionId));
                        doAppealSolution();
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionAppeal(resolutionId));
            }

            if (buttonDomain.getInputAWB() == 1) {
                Button button = getChatActionButton(buttonDomain.getInputAWBLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doInputAWB();
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionInputAWB(resolutionId));
            }
            if (buttonDomain.getFinish() == 1) {
                final Button button = getChatActionButton(buttonDomain.getFinishLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                isAcceptShown = true;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatClickAcceptSolution(resolutionId));
                        UnifyTracking.eventTracking(getActivity(),
                                InboxAnalytics.eventResoChatImpressionAcceptSolutionDialog(resolutionId));
                        showActionDialog(buttonDomain.getFinishLabel(), buttonDomain
                                .getFinishText(), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UnifyTracking.eventTracking(getActivity(),
                                        InboxAnalytics.eventResoChatClickYesAcceptSolutionDialog(resolutionId));
                                presenter.actionFinish();
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UnifyTracking.eventTracking(getActivity(),
                                        InboxAnalytics.eventResoChatClickBackAcceptSolutionDialog(resolutionId));
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (resCenterDialog != null)
                                    resCenterDialog.dismiss();
                            }
                        });
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionGetSolution(resolutionId));
            }

            if (!isAcceptShown) {
                if (buttonDomain.getAccept() == 1) {
                    Button button = getChatActionButton(buttonDomain.getAcceptLabel());
                    llActionButton.addView(button);
                    llActionButton.addView(addButtonSeparator());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UnifyTracking.eventTracking(getActivity(),
                                    InboxAnalytics.eventResoChatClickAcceptSolution(resolutionId));
                            UnifyTracking.eventTracking(getActivity(),
                                    InboxAnalytics.eventResoChatImpressionAcceptSolutionDialog(resolutionId));
                            showActionDialog(buttonDomain.getAcceptLabel(),
                                    buttonDomain.getAcceptTextLite(),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            UnifyTracking.eventTracking(getActivity(),
                                                    InboxAnalytics.eventResoChatClickYesAcceptSolutionDialog(resolutionId));
                                            presenter.actionAcceptSolution();
                                            if (resCenterDialog != null)
                                                resCenterDialog.dismiss();
                                        }
                                    }, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            UnifyTracking.eventTracking(getActivity(),
                                                    InboxAnalytics.eventResoChatClickBackAcceptSolutionDialog(resolutionId));
                                            if (resCenterDialog != null)
                                                resCenterDialog.dismiss();
                                        }
                                    }, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (resCenterDialog != null)
                                                resCenterDialog.dismiss();
                                        }
                                    });
                        }
                    });
                    UnifyTracking.eventTracking(getActivity(),
                            InboxAnalytics.eventResoChatImpressionGetSolution(resolutionId));
                }
            }

            if (buttonDomain.getRecomplaint() == 1) {
                Button button = getChatActionButton(buttonDomain.getRecomplainLabel());
                llActionButton.addView(button);
                llActionButton.addView(addButtonSeparator());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = CreateResCenterActivity.newRecomplaintInstance(
                                getActivity(),
                                String.valueOf(detailResChatDomain.getOrder().getId()),
                                String.valueOf(detailResChatDomain.getResolution().getId()));
                        startActivity(intent);
                    }
                });
                UnifyTracking.eventTracking(getActivity(),
                        InboxAnalytics.eventResoChatImpressionRecomplaint(resolutionId));
            }
        }
    }

    private Button getChatActionButton(String name) {
        Button button = new Button(getActivity());
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_chat_button_action));
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        button.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth,
                (int) getActivity().getResources().getDimension(R.dimen.dimens_chat_action_button_height)));
        button.setGravity(Gravity.CENTER);
        button.setText(name);
        button.setAllCaps(false);
        return button;
    }

    private View addButtonSeparator() {
        View spaceView = new View(getActivity());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.margin_vs),
                LinearLayout.LayoutParams.MATCH_PARENT));
        return spaceView;
    }

    private void showActionDialog(String title, String solution,
                                  View.OnClickListener acceptAction,
                                  View.OnClickListener backAction,
                                  View.OnClickListener closeAction) {
        resCenterDialog = new Dialog(getActivity());
        resCenterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resCenterDialog.setContentView(R.layout.layout_rescenter_dialog);
        TextView tvTitle = resCenterDialog.findViewById(R.id.tv_title);
        TextView tvSolution = resCenterDialog.findViewById(R.id.tv_solution);
        ImageView ivClose = resCenterDialog.findViewById(R.id.iv_close);
        Button btnBack = resCenterDialog.findViewById(R.id.btn_back);
        Button btnAccept = resCenterDialog.findViewById(R.id.btn_yes);
        String newTitle = title + "?";
        tvTitle.setText(newTitle);
        tvSolution.setText(MethodChecker.fromHtml(solution));
        btnBack.setOnClickListener(backAction);
        ivClose.setOnClickListener(closeAction);
        btnAccept.setOnClickListener(acceptAction);
        resCenterDialog.show();
    }

    private void doInputAWB() {
        startActivityForResult(
                InputShippingActivity.createNewPageIntentFromChat(getActivity(), resolutionId),
                REQUEST_INPUT_SHIPPING
        );
        getBottomSheetActivityTransition();
    }

    private void doInputAddress() {
        Intent intent = getChooseAddressIntent(false);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS);
    }

    private void doEditAddress() {
        Intent intent = getChooseAddressIntent(true);
        intent.putExtra("resolution_center", true);
        startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
    }

    public Intent getChooseAddressIntent(boolean isEditAddress) {
        return ChooseAddressActivity.createResolutionInstance(
                getActivity(), resolutionId, true, isEditAddress);
    }

    private Intent getIntentEditResCenter() {
        if (isSeller()) {
            return SolutionListActivity.newSellerEditInstance(getActivity(),
                    resolutionId, true);
        } else {
            return SolutionListActivity.newBuyerEditInstance(getActivity(),
                    resolutionId, true);
        }
    }

    private Intent getAppealResCenter() {
        return SolutionListActivity.newAppealInstance(getActivity(), resolutionId, true);
    }

    private boolean isSeller() {
        return detailResChatDomain.getActionBy() == ACTION_BY_SELLER;
    }

    @Override
    public void showSnackBar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void addAttachmentFile(AttachmentViewModel attachment) {
        attachmentAdapter.addImage(attachment);
        if (attachmentAdapter.getList().size() != 0) {
            rvAttachment.setVisibility(View.VISIBLE);
            actionButtonLayout.setVisibility(View.GONE);
        }
        attachmentAdapter.notifyDataSetChanged();
    }

    @Override
    public List<AttachmentViewModel> getAttachmentListFromAdapter() {
        return attachmentAdapter.getList();
    }

    @Override
    public void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel) {
        rvAttachment.setVisibility(View.GONE);
        attachmentAdapter.getList().clear();
        attachmentAdapter.notifyDataSetChanged();
        conversationDomain.setCreateTime(getConversationCreateTime());
        chatAdapter.replaceLastItem(new ChatRightViewModel(null, null, conversationDomain));
        initActionButton(detailResChatDomain.getButton());
        etChat.setText("");
        enableIvSend();
    }

    @Override
    public void errorReplyDiscussion(String error) {
        showErrorWithRefresh(error);
        chatAdapter.deleteLastItem();
        etChat.requestFocus();
        enableIvSend();
    }

    @Override
    public void successAcceptSolution() {
        dismissProgressBar();
        showSnackBar(getActivity().getString(R.string.string_success_accept));
        initView();
    }

    @Override
    public void errorAcceptSolution(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void successCancelComplaint() {
        dismissProgressBar();
        showSnackBar(getActivity().getString(R.string.string_success_cancel));
        initView();
        ffChat.setVisibility(View.GONE);
    }

    @Override
    public void errorCancelComplaint(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void successAskHelp() {
        dismissProgressBar();
        showSnackBar(getActivity().getString(R.string.string_success_help));
        initView();
    }

    @Override
    public void errorAskHelp(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void successInputAddress() {
        dismissProgressBar();
        initView();
    }

    @Override
    public void errorInputAddress(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void successEditAddress() {
        dismissProgressBar();
        initView();
    }

    @Override
    public void errorEditAddress(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void successFinishResolution() {
        dismissProgressBar();
        showSnackBar(getActivity().getString(R.string.string_success_finish));
        initView();
        ffChat.setVisibility(View.GONE);
    }

    @Override
    public void errorFinishResolution(String error) {
        dismissProgressBar();
        showErrorWithRefresh(error);
    }

    @Override
    public void intentToSeeAllProducts() {
        startActivity(ListProductActivity.newInstance(getActivity(), resolutionId));
    }

    @Override
    public void intentToEditAddress(int conversationId, int oldAddressId) {
        UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickChangeAddress(resolutionId));
        doEditAddress();
        this.conversationId = conversationId;
        this.oldAddressId = oldAddressId;
    }

    private void showErrorWithRefresh(String error) {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                error,
                Snackbar.LENGTH_LONG, getResources().getString(R.string.string_reload_page),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        initView();
                    }
                }).showRetrySnackbar();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DetailResChatActivity.REQUEST_GO_DETAIL:
                initView();
                break;
            case REQUEST_CODE_IMAGE_REPUTATION:
                presenter.handleImageResult(resultCode, data);
                break;
            case REQUEST_CODE_VIDEO:
                presenter.handleVideoResult(resultCode, data);
                break;
            case REQUEST_EDIT_SOLUTION:
                if (resultCode == Activity.RESULT_OK)
                    showSnackBar(getActivity().getString(R.string.string_success_edit_solution));
                initView();
                break;
            case REQUEST_APPEAL_SOLUTION:
                if (resultCode == Activity.RESULT_OK)
                    showSnackBar(getActivity().getString(R.string.string_success_appeal));
                initView();
                break;
            case REQUEST_INPUT_SHIPPING:
                if (resultCode == Activity.RESULT_OK)
                    showSnackBar(getActivity().getString(R.string.string_success_input_awb));
                initView();
                break;
            case REQUEST_EDIT_SHIPPING:
                if (resultCode == Activity.RESULT_OK) {
                    showSnackBar(getActivity().getString(R.string.string_success_edit_awb));
                    initView();
                }
                break;
            case REQUEST_CHOOSE_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressAcceptSolution(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_CHOOSE_ADDRESS_ACCEPT_ADMIN_SOLUTION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressAcceptAdminSolution(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_CHOOSE_ADDRESS_MIGRATE_VERSION:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.inputAddressMigrateVersion(destination != null ? destination.getAddressId() : null);
                }
                break;
            case REQUEST_EDIT_ADDRESS:
                if (resultCode == Activity.RESULT_OK) {
                    Destination destination = (Destination) data.getExtras().get(ManageAddressConstant.EXTRA_ADDRESS);
                    presenter.actionEditAddress(
                            destination != null ? destination.getAddressId() : null,
                            String.valueOf(oldAddressId),
                            String.valueOf(conversationId)
                    );
                }
            default:
                break;
        }
    }

    @Override
    public void onAddItemAdapter(List<Visitable> items) {
        chatAdapter.addAllItems(items);
    }

    @Override
    public void onAddItemWithPositionAdapter(int position, List<Visitable> items) {
        chatAdapter.addAllItemsOnPosition(position, items);
    }

    @Override
    public void onRefreshChatAdapter() {
        chatAdapter.notifyDataSetChanged();
        scrollChatToBottom(true);
    }

    @Override
    public void doAppealSolution() {
        startActivityForResult(getAppealResCenter(), REQUEST_APPEAL_SOLUTION);
    }

    @Override
    public void doEditSolution() {
        startActivityForResult(getIntentEditResCenter(), REQUEST_EDIT_SOLUTION);
    }

    @Override
    public void doTrackShipping(String shipmentID, String shipmentRef) {
        UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickTrack(resolutionId));
        startActivity(TrackShippingActivity.newInstance(
                getActivity(),
                shipmentID,
                shipmentRef)
        );
        getBottomSheetActivityTransition();
    }

    @Override
    public void doEditAwb(String conversationId,
                          String shippingId, String shippingRefNum) {
        UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickEditAwb(resolutionId));
        startActivityForResult(InputShippingActivity.createEditPageIntentFromChat(
                getActivity(),
                resolutionId,
                conversationId,
                shippingId,
                shippingRefNum),
                REQUEST_EDIT_SHIPPING);
        getBottomSheetActivityTransition();
    }

    public void getBottomSheetActivityTransition() {
        getActivity().overridePendingTransition(R.anim.pull_up, R.anim.push_down);
    }

    @Override
    public void goToProductDetail(ConversationProductDomain product) {
        UnifyTracking.eventTracking(getActivity(), InboxAnalytics.eventResoChatClickProductOnChat(resolutionId));
        startActivity(
                ProductDetailActivity.newInstance(context,
                        resolutionId,
                        String.valueOf(product.getResProdId()),
                        product.getName())
        );
    }

    @Override
    public void goToProductList(ConversationProductDomain product) {
        startActivity(
                ListProductActivity.newInstance(context, String.valueOf(resolutionId))
        );
    }

    @Override
    public void openImagePreview(ArrayList<String> imageUrls, int position) {
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void openVideoPlayer(String videoUrl) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerActivity.PARAMS_URL_VIDEO, videoUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void enableIvSend() {
        ivSend.setClickable(true);
        ivSend.setEnabled(true);
        etChat.setClickable(true);
        etChat.setEnabled(true);
        attachmentAdapter.isClickable = true;
    }

    @Override
    public void disableIvSend() {
        ivSend.setClickable(false);
        ivSend.setEnabled(false);
        etChat.setClickable(false);
        etChat.setEnabled(false);
        attachmentAdapter.isClickable = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}