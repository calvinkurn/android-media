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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatFragment
        extends BaseDaggerFragment
        implements DetailResChatFragmentListener.View{

    public static final int NEXT_STATUS_CURRENT = 1;

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
        rvChat.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
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
                presenter.sendIconPressed(etChat.getText().toString());
            }
        });

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
        for (ConversationDomain conversationDomain : detailResChatDomain.getConversation()) {
            if (conversationDomain.getAction().getType().equals("create")) {
                chatAdapter.addItem(new ChatCreateLeftViewModel
                        (detailResChatDomain.getShop(),
                                detailResChatDomain.getLast(),
                                conversationDomain));
            } else if (detailResChatDomain.getActionBy() == conversationDomain.getAction().getBy()) {
                chatAdapter.addItem(new ChatRightViewModel(
                        conversationDomain.getAction(),
                        conversationDomain.getMessage(),
                        conversationDomain.getCreateTime(),
                        conversationDomain.getAttachment(),
                        conversationDomain.getFlag()));
            } else {
                chatAdapter.addItem(new ChatLeftViewModel(
                        conversationDomain.getAction(),
                        conversationDomain.getMessage(),
                        conversationDomain.getCreateTime(),
                        conversationDomain.getAttachment(),
                        conversationDomain.getFlag()));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
