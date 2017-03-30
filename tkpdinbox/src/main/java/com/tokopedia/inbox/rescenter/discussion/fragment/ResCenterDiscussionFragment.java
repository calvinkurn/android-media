package com.tokopedia.inbox.rescenter.discussion.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.inbox.rescenter.discussion.adapter.ResCenterDiscussionAdapter;
import com.tokopedia.inbox.rescenter.discussion.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.presenter.ResCenterDiscussionPresenter;
import com.tokopedia.inbox.rescenter.discussion.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.viewmodel.ResCenterDiscussionItemViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_SENT_MESSAGE;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionFragment extends BasePresenterFragment<ResCenterDiscussionPresenter>
        implements ResCenterDiscussionView {

    ResCenterDiscussionAdapter adapter;
    RecyclerView discussionList;
    EditText replyEditText;
    ImageView sendButton;
    ImageView attachButton;
    LinearLayoutManager layoutManager;

    public static Fragment createInstance() {
        return new ResCenterDiscussionFragment();
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
        presenter = new ResCenterDiscussionPresenterImpl(this);
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
    }

    private void addTemporaryMessage() {
        ResCenterDiscussionItemViewModel tempMessage = new ResCenterDiscussionItemViewModel();
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
    public void onSuccessGetDiscussion() {
        List<ResCenterDiscussionItemViewModel> list = new ArrayList<>();

        list.add(new ResCenterDiscussionItemViewModel("Message 1", "24 Jul 2016 11:45 WIB", "3045173"));
        list.add(new ResCenterDiscussionItemViewModel("Message 2", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 3", "24 Jul 2016 11:45 WIB", "3045173"));
        list.add(new ResCenterDiscussionItemViewModel("Message 4", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 5", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 6", "24 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 7", "25 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 8", "25 Jul 2016 11:45 WIB"));
        list.add(new ResCenterDiscussionItemViewModel("Message 3", "26 Jul 2016 11:45 WIB", "3045173"));

        adapter.setList(list);
    }

    @Override
    public void onSuccessSendDiscussion() {
        replyEditText.setText("");
        adapter.add(0, new ResCenterDiscussionItemViewModel("Message reply", "24 Jul 2016 11:45 WIB", "3045173"));
        adapter.remove(adapter.getData().size() - 1);
        setViewEnabled(true);
        adapter.addReply(new ResCenterDiscussionItemViewModel("Message reply success", "24 Jul 2016 11:45 WIB", "3045173"));
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

    private void finishLoading() {

    }

    private void setViewEnabled(boolean isEnabled) {
        replyEditText.setEnabled(isEnabled);
        sendButton.setEnabled(isEnabled);
        attachButton.setEnabled(isEnabled);
    }

}
