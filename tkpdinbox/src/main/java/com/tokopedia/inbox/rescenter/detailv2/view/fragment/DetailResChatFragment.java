package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.DateFormatUtils;
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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatFragment
        extends BaseDaggerFragment
        implements DetailResChatFragmentListener.View{

    public static final int NEXT_STATUS_CURRENT = 1;

    public static final String ACTION_CREATE = "create";

    private TextView tvNextStep;
    private RecyclerView rvChat;
    private ProgressBar progressBar;
    private LinearLayout mainView;
    private CardView cvNextStep;
    private ChatAdapter chatAdapter;
    private EditText etChat;
    private ImageView ivSend;

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
        tvNextStep = (TextView) view.findViewById(R.id.tv_next_step);
        rvChat = (RecyclerView) view.findViewById(R.id.rv_chat);
        mainView = (LinearLayout) view.findViewById(R.id.main_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        cvNextStep = (CardView) view.findViewById(R.id.cv_next_step);
        etChat = (EditText) view.findViewById(R.id.et_chat);
        ivSend = (ImageView) view.findViewById(R.id.iv_send);
        mainView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        presenter.initView(resolutionId);
        chatAdapter = new ChatAdapter(new DetailChatTypeFactoryImpl(this));
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                ConversationDomain conversationDomain = getTempConversationDomain(etChat.getText().toString());
                chatAdapter.addItem(new ChatRightViewModel(null, null, conversationDomain));
                chatAdapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                presenter.sendIconPressed(etChat.getText().toString());
            }
        });

    }

    public ConversationDomain getTempConversationDomain(String message) {
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

    public ConversationCreateTimeDomain getConversationCreateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DateFormatUtils.FORMAT_T_Z);
        return new ConversationCreateTimeDomain(format.format(calendar.getTime()), "");
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
        initNextStep(detailResChatDomain.getNextAction());
        initChatData(detailResChatDomain);
    }

    @Override
    public void errorInputMessage(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void successGetConversation(DetailResChatDomain detailResChatDomain) {
        this.detailResChatDomain = detailResChatDomain;
        mainView.setVisibility(View.VISIBLE);
        initNextStep(detailResChatDomain.getNextAction());
        initChatData(detailResChatDomain);
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

    private void initNextStep(NextActionDomain nextActionDomain) {
        for (NextActionDetailStepDomain nextStep : nextActionDomain.getDetail().getStep()) {
            if (nextStep.getStatus() == NEXT_STATUS_CURRENT) {
                tvNextStep.setText(nextStep.getName());
            }
        }
    }

    private void initChatData(DetailResChatDomain detailResChatDomain) {
        int lastAction = 0;
        for (ConversationDomain conversationDomain : detailResChatDomain.getConversation()) {
            if (conversationDomain.getAction().getType().equals(ACTION_CREATE)) {
                chatAdapter.addItem(new ChatCreateLeftViewModel
                        (detailResChatDomain.getShop(),
                                detailResChatDomain.getLast(),
                                conversationDomain));
            } else {
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
            }

        }
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel) {
        etChat.setText("");
    }

    @Override
    public void errorReplyDiscussion(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
        chatAdapter.deleteLastItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
