package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.activity.InboxChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.adapter.NewInboxChatAdapter;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.ReplyParcelableModel;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatFragment extends BaseDaggerFragment implements InboxChatContract.View, InboxMessageConstant, SearchInputView.Listener{

    RecyclerView mainList;

    SwipeToRefresh swipeToRefresh;

//    FloatingActionButton fab;

    View searchLoading;

    @Inject
    InboxChatPresenter presenter;

    NewInboxChatAdapter adapter;
    RefreshHandler refreshHandler;

    boolean isRetryShowing = false;
    public boolean isMustRefresh = false;

    LinearLayoutManager layoutManager;
    TkpdProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;
    Snackbar snackbarUndo;
    SearchInputView searchInputView;
    private InboxChatTypeFactory typeFactory;

    boolean isMultiActionEnabled = false;
    ActionMode.Callback callbackContext;
    ActionMode contextMenu;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static InboxChatFragment createInstance(String navigation) {
        InboxChatFragment fragment = new InboxChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_inbox_message, container, false);

        initView(parentView);
//        adapter = InboxChatAdapter.createAdapter(getActivity(), presenter);


        return parentView;
    }

    private void initView(View parentView) {
        searchLoading = parentView.findViewById(R.id.loading_search);
        mainList = (RecyclerView) parentView.findViewById(R.id.chat_list);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
//        fab = (FloatingActionButton) parentView.findViewById(R.id.fab);
        refreshHandler = new RefreshHandler(getActivity(), parentView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshData();
            }
        });
        searchInputView = (SearchInputView) parentView.findViewById(R.id.simpleSearchView);
        searchInputView.setListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setHasFixedSize(true);
        presenter.attachView(this);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        callbackContext = initCallbackActionMode();
        ((InboxChatActivity) getActivity()).showTabLayout(false);

        typeFactory = new InboxChatTypeFactoryImpl(this, presenter);
    }

    private ActionMode.Callback initCallbackActionMode() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                disableActions();
                getActivity().getMenuInflater().inflate(presenter.getMenuID(), menu);
                isMultiActionEnabled = true;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(String.valueOf(presenter.getSelected()));
//                switch (adapter.getChosenMessageStatus()) {
//                    case STATE_CHAT_READ:
//                        menu.findItem(R.id.action_mark_as_unread).setVisible(true);
//                        menu.findItem(R.id.action_mark_as_read).setVisible(false);
//                        break;
//                    case STATE_CHAT_UNREAD:
//                        menu.findItem(R.id.action_mark_as_read).setVisible(true);
//                        menu.findItem(R.id.action_mark_as_unread).setVisible(false);
//                        break;
//                    case STATE_CHAT_BOTH:
//                        menu.findItem(R.id.action_mark_as_unread).setVisible(true);
//                        menu.findItem(R.id.action_mark_as_read).setVisible(true);
//                        break;
//                    default:
//                        menu.findItem(R.id.action_mark_as_unread).setVisible(false);
//                        menu.findItem(R.id.action_mark_as_read).setVisible(false);
//                        break;
//                }
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                if (item.getItemId() == R.id.action_move_achieve) {
//                    presenter.moveInbox(ARCHIVE_ALL);
//                    mode.finish();
//                    return true;
//                } else if (item.getItemId() == R.id.action_move_trash) {
//                    presenter.moveInbox(DELETE_ALL);
//                    mode.finish();
//                    return true;
//                } else if (item.getItemId() == R.id.action_delete) {
//                    presenter.moveInbox(DELETE_FOREVER);
//                    mode.finish();
//                    return true;
//                } else if (item.getItemId() == R.id.action_move_inbox) {
//                    presenter.moveInbox(MOVE_ALL);
//                    mode.finish();
//                    return true;
//                } else if (item.getItemId() == R.id.action_mark_as_read) {
//                    presenter.markAsRead();
//                    mode.finish();
//                    return true;
//                } else if (item.getItemId() == R.id.action_mark_as_unread) {
//                    presenter.markAsUnread();
//                    mode.finish();
//                    return true;
//                } else {
//                    return false;
//                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//                adapter.setSelected(0);
//                adapter.clearSelection();
                isMultiActionEnabled = false;
                enableActions();
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new NewInboxChatAdapter(typeFactory, presenter);

        mainList.setAdapter(adapter);
        mainList.setLayoutManager(layoutManager);
        mainList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findLastCompletelyVisibleItemPosition();
                if (adapter.checkLoadMore(index)) {
                    presenter.onLoadMore();
                }
            }
        });
        searchLoading.setVisibility(View.VISIBLE);
        presenter.getMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                .appComponent(daggerAppComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
//            presenter.setUserVisibleHint(isVisibleToUser, isMustRefresh);
//            if (snackbarRetry != null) {
//                snackbarRetry.resumeRetrySnackbar();
//                if (!isVisibleToUser)
//                    snackbarRetry.pauseRetrySnackbar();
//            }
//            if (snackbarUndo != null) {
//                snackbarUndo.dismiss();
//            }
//            finishContextMode();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setOptionsMenu() {
        if (!isMultiActionEnabled) {
            contextMenu = ((AppCompatActivity) getActivity()).startSupportActionMode(callbackContext);
//            LayoutInflater mInflater = LayoutInflater.from(getActivity());
//            View mCustomView = mInflater.inflate(R.layout.header_chat, null);
//            contextMenu.setCustomView(mCustomView);

        }
        if (contextMenu != null) {
            contextMenu.invalidate();
            ((InboxChatActivity) getActivity()).showTabLayout(false);
            if (presenter.getSelected() == 0) {
                contextMenu.finish();
                ((InboxChatActivity) getActivity()).showTabLayout(true);
            }
            contextMenu.setTitle(String.valueOf(presenter.getSelected()) + " " + getString(R.string.title_inbox_chat));
        }

    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public void onGoToTimeMachine(String url) {
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }


    @Override
    public String getNav() {
        return getArguments().getString(PARAM_NAV, "");
    }

    @Override
    public void enableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(true);

//        if (adapter.getSelected() == 0) {
//            fab.show();
//            ((InboxChatActivity)getActivity()).showTabLayout(true);
//        }
//        if (presenter.hasActionListener()) {
//            adapter.setEnabled(true);
//        }
    }

    @Override
    public void disableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(false);
//        if (fab.isShown())
//            fab.hide();
//        adapter.setEnabled(false);
    }

    @Override
    public void finishContextMode() {

    }

    @Override
    public boolean hasRetry() {
        return isRetryShowing;
    }

    @Override
    public void overridePendingTransition(int i, int i1) {

    }

    @Override
    public String getFilter() {
        return null;
    }

    @Override
    public String getKeyword() {
        return searchInputView.getSearchText();
    }

    @Override
    public void showEmptyState(String localizedMessage) {

    }

    @Override
    public void showError(String localizedMessage) {

    }

    @Override
    public void moveViewToTop() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(layoutManager.findFirstVisibleItemPosition() < 2){
                    layoutManager.scrollToPosition(0);
                }
            }
        });
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public NewInboxChatAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        progressDialog.dismiss();
        adapter.removeLoading();
        searchLoading.setVisibility(View.GONE);
    }

    @Override
    public void setMustRefresh(boolean isMustRefresh) {
        this.isMustRefresh = isMustRefresh;
    }

    @Override
    public void removeError() {
        adapter.showEmptyFull(false);
//        adapter.showRetry(false);
//        adapter.showRetryFull(false);
        isRetryShowing = false;
//        NetworkErrorHelper.hideEmptyState(getView());
    }

    public void restackList(Bundle data) {
        Log.i("restackList: ", data.toString());
        String senderId = data.getString("sender_id");
        String lastReply = data.getString("summary");
        adapter.moveToTop(senderId, lastReply, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DETAIL_MESSAGE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().getBoolean(MUST_REFRESH))
                presenter.refreshData();
            else {
                Bundle bundle = data.getExtras();
                ReplyParcelableModel model = bundle.getParcelable("parcel");
                adapter.moveToTop(model.getSenderId(), model.getMsg(), false);
            }
        }
    }


    @Override
    public void finishSearch() {
        searchLoading.setVisibility(View.GONE);
    }

    @Override
    public void onSearchSubmitted(String text) {
        if(text.length()>0) {
            presenter.initSearch(text);
            searchLoading.setVisibility(View.VISIBLE);
        }else {
            presenter.refreshData();
        }
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}
