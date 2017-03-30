package com.tokopedia.inbox.rescenter.discussion.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.ResCenterDiscussionAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenter;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.List;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionFragment extends BasePresenterFragment<ResCenterDiscussionPresenter>
        implements ResCenterDiscussionView {

    private static final String PARAM_RESOLUTION_ID = "PARAM_RESOLUTION_ID";
    ResCenterDiscussionAdapter adapter;
    RecyclerView discussionList;
    EditText replyEditText;
    ImageView sendButton;
    ImageView attachButton;
    LinearLayoutManager layoutManager;

    public static Fragment createInstance(String resolutionID) {
        Fragment fragment = new ResCenterDiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESOLUTION_ID, resolutionID);
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
        adapter = ResCenterDiscussionAdapter.createAdapter(getActivity());
        discussionList = (RecyclerView) view.findViewById(R.id.discussion_list);
        discussionList.setLayoutManager(layoutManager);
        discussionList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTemporaryMessage();
                setViewEnabled(false);

                presenter.setDiscussionText(replyEditText.getText().toString());
                presenter.sendDiscussion();
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
    }

    private void addTemporaryMessage() {
        DiscussionItemViewModel tempMessage = new DiscussionItemViewModel();
        tempMessage.setMessage(replyEditText.getText().toString());
        adapter.addReply(tempMessage);
        scrollToBottom();
    }

    private void scrollToBottom() {
        layoutManager.scrollToPosition(adapter.getItemCount() - 1);

    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSuccessGetDiscussion(List<DiscussionItemViewModel> list) {
        finishLoading();
        adapter.setList(list);
    }

    @Override
    public void onSuccessSendDiscussion() {
        replyEditText.setText("");
        adapter.add(0, new DiscussionItemViewModel("Message reply", "24 Jul 2016 11:45 WIB", "3045173"));
        adapter.remove(adapter.getData().size() - 1);
        setViewEnabled(true);
        adapter.addReply(new DiscussionItemViewModel("Message reply success", "24 Jul 2016 11:45 WIB", "3045173"));
        adapter.notifyDataSetChanged();
        finishLoading();
        scrollToBottom();
//        presenter.updateCache(result.getList().get(0));

//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(PARAM_SENT_MESSAGE, result.getList().get(0));
//        bundle.putInt(PARAM_POSITION, getArguments().getInt(PARAM_POSITION, -1));
//        intent.putExtras(bundle);
//        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public String getResolutionID() {
        return getArguments().getString(PARAM_RESOLUTION_ID) != null ?
                getArguments().getString(PARAM_RESOLUTION_ID) : "";
    }

    @Override
    public void onErrorSendReply(String errorMessage) {
        replyEditText.setError(errorMessage);
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

}
