package com.tokopedia.inbox.inboxmessageold.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.ToolTipUtils;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.adapter.InboxMessageDetailAdapter;
import com.tokopedia.inbox.inboxmessageold.listener.InboxMessageDetailFragmentView;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.inbox.inboxmessageold.presenter.InboxMessageDetailFragmentPresenter;
import com.tokopedia.inbox.inboxmessageold.presenter.InboxMessageDetailFragmentPresenterImpl;

import butterknife.BindView;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailFragment extends BasePresenterFragment<InboxMessageDetailFragmentPresenter>
        implements InboxMessageDetailFragmentView, InboxMessageConstant {

    public interface DoActionInboxMessageListener {
        void sendReply(Bundle param);

        void flagSpam(Bundle param);

        void undoFlagSpam(Bundle param);
    }

    @BindView(R2.id.message_list)
    RecyclerView mainList;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.msg_title)
    TextView title;

    @BindView(R2.id.add_comment_area)
    View replyView;

    @BindView(R2.id.add_url)
    ImageView attachmentButton;

    @BindView(R2.id.new_comment)
    EditText replyEditText;

    @BindView(R2.id.send_but)
    ImageView sendButton;

    @BindView(R2.id.header_date)
    TextView headerDate;

    @BindView(R2.id.reputation_view)
    View viewReputation;

    @BindView(R2.id.rep_rating)
    TextView reputationPercentage;

    @BindView(R2.id.rep_icon)
    ImageView reputationIcon;

    @BindView(R2.id.header)
    View header;

    LinearLayoutManager layoutManager;
    InboxMessageDetailAdapter adapter;
    RefreshHandler refreshHandler;

    public static InboxMessageDetailFragment createInstance(Bundle data) {
        InboxMessageDetailFragment fragment = new InboxMessageDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();
        presenter.setResultBundle();
        UnifyTracking.eventMessageDetail(getArguments().getString(PARAM_NAV, "N/A"));
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
        presenter = new InboxMessageDetailFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return com.tokopedia.core.R.layout.fragment_inbox_message_detail_old;
    }

    @Override
    protected void initView(View view) {
        KeyboardHandler.DropKeyboard(getActivity(), replyEditText);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        adapter = InboxMessageDetailAdapter.createAdapter(getActivity(), presenter);
        adapter.setNav(getArguments().getString(PARAM_NAV, ""));
        mainList.setAdapter(adapter);
        refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        title.bringToFront();
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        };
    }

    @Override
    protected void setViewListener() {
        sendButton.setOnClickListener(onSendMessage());
        attachmentButton.setOnClickListener(onAttachmentClicked());
        viewReputation.setOnClickListener(onReputationClicked());
        mainList.addOnScrollListener(onScroll());
        mainList.addOnLayoutChangeListener(onKeyboardShows());
    }

    private View.OnLayoutChangeListener onKeyboardShows() {
        return new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mainList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollToBottom();
                        }
                    }, 100);
                }
            }
        };
    }

    private View.OnClickListener onReputationClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(ToolTipUtils.setToolTip(context, com.tokopedia.core.R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        InboxMessageItem messageItem = getArguments().getParcelable(PARAM_MESSAGE);
                        if (messageItem != null) {
                            TextView smile = (TextView) view.findViewById(com.tokopedia.core.R.id.text_smile);
                            TextView netral = (TextView) view.findViewById(com.tokopedia.core.R.id.text_netral);
                            TextView bad = (TextView) view.findViewById(com.tokopedia.core.R.id.text_bad);
                            smile.setText(messageItem.getUserReputation().getPositive());
                            netral.setText(messageItem.getUserReputation().getNeutral());
                            bad.setText(messageItem.getUserReputation().getNegative());
                        }
                    }

                    @Override
                    public void setListener() {

                    }
                }), v);
            }
        };
    }

    private View.OnClickListener onSendMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendReply();
            }
        };
    }

    private View.OnClickListener onAttachmentClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getUrl();
            }
        };
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (headerDate != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        headerDate.setVisibility(View.GONE);
                    } else if (!headerDate.getText().toString().equals("")) {
                        headerDate.setVisibility(View.VISIBLE);
                    } else {
                        headerDate.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                CommonUtils.dumper("NISNIS " + layoutManager.findFirstVisibleItemPosition() + " " + adapter.canLoadMore());
                if (adapter.getData().size() == 0
                        || ((layoutManager.findFirstVisibleItemPosition() == 0 && adapter.canLoadMore() == 1)
                        || adapter.getData().get(
                        layoutManager.findFirstVisibleItemPosition()
                                - adapter.canLoadMore()).getMessageReplyTimeFmt() == null)) {
                    headerDate.setVisibility(View.GONE);
                } else {
                    headerDate.setText(adapter.getData().get(
                            layoutManager.findFirstVisibleItemPosition() - adapter.canLoadMore())
                            .getMessageReplyDateFmt());
                }
            }
        };
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public InboxMessageDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setHeader(InboxMessageDetail inboxMessageDetail) {
        title.setText(inboxMessageDetail.getMessageTitle());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(com.tokopedia.core.R.id.app_bar);

        if (toolbar != null && inboxMessageDetail.getConversationBetween() != null) {
            toolbar.setTitle(inboxMessageDetail.getOpponent().getUserName());
            InboxMessageItem messageItem = getArguments().getParcelable(PARAM_MESSAGE);
            if (messageItem != null)
                toolbar.setSubtitle(messageItem.getUserLabel());
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        if (inboxMessageDetail.getTextareaReply() != 1) {
            replyView.setVisibility(View.GONE);
        }

    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        adapter.showLoading(false);
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public void showUndoFlagSpam(View.OnClickListener listener) {
        SnackbarManager.make(getActivity(), getString(com.tokopedia.core.R.string.message_flag_spam), Snackbar.LENGTH_LONG)
                .setAction(getString(com.tokopedia.core.R.string.undo_but), listener).show();
    }

    @Override
    public EditText getReplyMessage() {
        return replyEditText;
    }

    @Override
    public void showError(String error) {
        if (error.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), error);
        }
    }

    @Override
    public void addUrlToReply(String url) {
        replyEditText.setText(replyEditText.getText() + url);
        replyEditText.setSelection(replyEditText.length());
    }

    @Override
    public void scrollToBottom() {
        layoutManager.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
        replyEditText.setEnabled(isEnabled);
        attachmentButton.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        mainList.setEnabled(isEnabled);
        if (isEnabled) {
            header.setVisibility(View.VISIBLE);
            replyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showSuccessUndoSpam(final int position) {
        SnackbarManager.make(getActivity(), getString(com.tokopedia.core.R.string.message_undo_flag_spam), Snackbar.LENGTH_LONG)
                .setAction(getString(com.tokopedia.core.R.string.see_message), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainList.smoothScrollToPosition(position);
                    }
                }).show();
    }

    @Override
    public void setRetry(String message, final View.OnClickListener listener) {
        finishLoading();
        setViewEnabled(false);
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                listener.onClick(getView());
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onSuccessSendReply(Bundle resultData) {
        InboxMessageDetail result = resultData.getParcelable(EXTRA_RESULT);
        if (result != null && result.getList().size() != 0) {
            adapter.remove(getAdapter().getData().size() - 1);
            setViewEnabled(true);
            getAdapter().addReply(result.getList().get(0));
            getAdapter().notifyDataSetChanged();
            finishLoading();
            getReplyMessage().setText("");
            scrollToBottom();
            presenter.updateCache(result.getList().get(0));
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(PARAM_SENT_MESSAGE, result.getList().get(0));
            bundle.putInt(PARAM_POSITION, getArguments().getInt(PARAM_POSITION, -1));
            intent.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, intent);
            UnifyTracking.eventMessageSend(getArguments().getString(PARAM_NAV, "N/A"));
        }
    }

    @Override
    public void onSuccessFlagSpam(Bundle resultData) {
        final int position = resultData.getInt(EXTRA_POSITION);
        final InboxMessageDetailItem inboxMessageDetailItem = resultData.getParcelable(EXTRA_FLAGGED_MESSAGE);
        if (inboxMessageDetailItem != null) {
            setViewEnabled(true);
            getAdapter().remove(position);
            getAdapter().notifyDataSetChanged();
            showUndoFlagSpam(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.undoFlagSpam(position, inboxMessageDetailItem);
                }
            });
            finishLoading();
            presenter.updateCacheFlagSpam(position);
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MUST_REFRESH, true);
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSuccessUndoFlagSpam(Bundle resultData) {

        final int position = resultData.getInt(EXTRA_POSITION);
        final InboxMessageDetailItem inboxMessageDetailItem = resultData.getParcelable(EXTRA_FLAGGED_MESSAGE);

        if (inboxMessageDetailItem != null) {
            setViewEnabled(true);
            getAdapter().add(position, inboxMessageDetailItem);
            getAdapter().notifyDataSetChanged();
            finishLoading();
            showSuccessUndoSpam(position);
            presenter.updateCacheUndoFlagSpam(position, inboxMessageDetailItem);
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MUST_REFRESH, true);
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);

    }

    @Override
    public void onFailedSendReply(Bundle resultData) {
        adapter.remove(getAdapter().getData().size() - 1);
        String error = resultData.getString(EXTRA_ERROR, "");
        finishLoading();
        setViewEnabled(true);
        showError(error);
    }

    @Override
    public void onFailedFlagSpam(Bundle resultData) {
        final int position = resultData.getInt(EXTRA_POSITION);
        final InboxMessageDetailItem inboxMessageDetailItem = resultData.getParcelable(EXTRA_FLAGGED_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, "");
        finishLoading();
        setViewEnabled(true);
        showError(error);
    }

    @Override
    public void onFailedUndoFlagSpam(Bundle resultData) {
        final int position = resultData.getInt(EXTRA_POSITION);
        final InboxMessageDetailItem inboxMessageDetailItem = resultData.getParcelable(EXTRA_FLAGGED_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, DEFAULT_ERROR_MOVE_INBOX);
        if (inboxMessageDetailItem != null)
            setRetry(error, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.undoFlagSpam(position, inboxMessageDetailItem);
                }
            });
    }

    @Override
    public void addTempMessage() {
        InboxMessageDetailItem tempMessage = new InboxMessageDetailItem();
        tempMessage.setMessageReply(getReplyMessage().getText().toString());
        adapter.addReply(tempMessage);
        scrollToBottom();
    }

    @Override
    public void setHasReputation(String positivePercentage) {
        reputationPercentage.setVisibility(View.VISIBLE);
        reputationPercentage.setText(positivePercentage);
        reputationIcon.setImageResource(com.tokopedia.core.R.drawable.ic_icon_repsis_smile_active);
    }

    @Override
    public void setNoReputation() {
        reputationPercentage.setVisibility(View.GONE);
        reputationIcon.setImageResource(com.tokopedia.core.R.drawable.ic_icon_repsis_smile);
    }

    @Override
    public void showEmptyState() {
        header.setVisibility(View.GONE);
        replyView.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getMessageDetail();
            }

        });
    }

    @Override
    public void showEmptyState(String error) {
        header.setVisibility(View.GONE);
        replyView.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getMessageDetail();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
