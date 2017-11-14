package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.activity.InboxChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.adapter.NewInboxChatAdapter;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.ReplyParcelableModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatFragment extends BaseDaggerFragment
        implements InboxChatContract.View, InboxMessageConstant
        , SearchInputView.Listener, SearchInputView.ResetListener
        , WebSocketInterface{

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
    private View notifier;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inbox_chat_organize, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_organize) {
            setOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mainList.requestFocus();
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
//        fab = (FloatingActionButton) parentView.findViewById(R.id.fab);
        refreshHandler = new RefreshHandler(getActivity(), parentView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                finishContextMode();
                presenter.refreshData();
            }
        });
        searchInputView = (SearchInputView) parentView.findViewById(R.id.simpleSearchView);
        searchInputView.setListener(this);
        searchInputView.setResetListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setHasFixedSize(true);
        presenter.attachView(this);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        callbackContext = initCallbackActionMode();
        notifier = parentView.findViewById(R.id.notifier);
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
                presenter.setInActionMode(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(String.valueOf(presenter.getSelected()));
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                if (item.getItemId() == R.id.action_delete){
                    List<Pair> temp = new ArrayList<>();
                    temp.addAll(adapter.getListMove());
                    askOption(presenter.getSelected(), temp).show();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
                isMultiActionEnabled = false;
                presenter.setInActionMode(false);
                enableActions();
            }
        };
    }


    private AlertDialog askOption(int selected, final List<Pair> listMove)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Apakah anda yakin menghapus "+selected+ " pesan?")
                .setIcon(R.drawable.ic_trash)

                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        presenter.deleteMessage(listMove);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

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
    public void setOptionsMenuFromSelect() {
        if (!isMultiActionEnabled) {
            contextMenu = ((AppCompatActivity) getActivity()).startSupportActionMode(callbackContext);
        }
        if (contextMenu != null) {
            contextMenu.invalidate();
            ((InboxChatActivity) getActivity()).showTabLayout(false);
            if (presenter.getSelected() == 0) {
                finishContextMode();
                ((InboxChatActivity) getActivity()).showTabLayout(true);
            }
            contextMenu.setTitle(String.valueOf(presenter.getSelected()) + " " + getString(R.string.title_inbox_chat));
        }
    }

    public void setOptionsMenu() {
        if (!isMultiActionEnabled) {
            contextMenu = ((AppCompatActivity) getActivity()).startSupportActionMode(callbackContext);
        }
        if (contextMenu != null) {
            contextMenu.invalidate();
            contextMenu.setTitle("Pilih chat yang dihapus");
        }
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public void onGoToTimeMachine(String url) {
        dropKeyboard();
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void removeList(List<Pair> originList, List<DeleteChatViewModel> list) {
        adapter.removeList(originList, list);
    }

    @Override
    public void setResultSearch(final InboxChatViewModel inboxChatViewModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.setResultSearch(inboxChatViewModel);
            }
        });
    }

    @Override
    public void setResultFetch(final InboxChatViewModel inboxChatViewModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.setResultFetch(inboxChatViewModel);
            }
        });
    }

    @Override
    public WebSocketInterface getInterface() {
        return this;
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
        if(contextMenu!=null){
            contextMenu.finish();
        }
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
    public void showError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }


    @Override
    public void showErrorWarningDelete(int maxMessageDelete) {
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.delete_message_warn) + " " + maxMessageDelete);
    }

    @Override
    public void showErrorFull() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getMessage();
            }
        });
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
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
//        adapter.removeLoading();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DETAIL_MESSAGE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().getBoolean(MUST_REFRESH))
                presenter.refreshData();
            else {
                Bundle bundle = data.getExtras();
                ReplyParcelableModel model = bundle.getParcelable("parcel");
                adapter.moveToTop(model.getMessageId(), model.getMsg(), false);
            }
        }

        presenter.recreateWebSocket();
    }


    @Override
    public void finishSearch() {
        searchLoading.setVisibility(View.GONE);
    }

    @Override
    public void onSearchSubmitted(String text) {
        refreshHandler.setPullEnabled(false);
        if(text.length()>0) {
            presenter.initSearch(text);
            searchLoading.setVisibility(View.VISIBLE);
            UnifyTracking.eventTopChatSearch(TopChatTrackingEventLabel.Category.INBOX_CHAT,
                    TopChatTrackingEventLabel.Action.INBOX_CHAT_SEARCH,
                    TopChatTrackingEventLabel.Name.INBOX_CHAT);
        }else {
            onSearchReset();

        }
        dropKeyboard();
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onSearchReset() {
        refreshHandler.setPullEnabled(true);
        presenter.resetSearch();
    }

    @Override
    public void onIncomingEvent(final WebSocketResponse response) {
        switch (response.getCode()) {
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING:
                adapter.showTyping(response.getData().getMsgId());
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                adapter.removeTyping(response.getData().getMsgId());
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.moveToTop(String.valueOf(response.getData().getMsgId()),
                                response.getData().getMessage().getCensoredReply(), true);
                    }
                });
            default:
                break;
        }
    }

    @Override
    public void onErrorWebSocket() {
        if (getActivity() != null) {
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
    }

    @Override
    public void onOpenWebSocket() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView title = (TextView) notifier.findViewById(R.id.title);
                title.setText("Terhubung!");
                TextView action = (TextView) notifier.findViewById(R.id.action);
                action.setVisibility(View.GONE);
            }
        });

        notifier.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifier.setVisibility(View.GONE);
            }
        }, 1500);
        presenter.resetAttempt();
    }


    @Override
    public void notifyConnectionWebSocket() {
        if (getActivity() != null && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.VISIBLE);
                    TextView title = (TextView) notifier.findViewById(R.id.title);

                    TextView action = (TextView) notifier.findViewById(R.id.action);

                    title.setText(R.string.error_no_connection_retrying);
                    action.setVisibility(View.VISIBLE);
                    action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        presenter.resetAttempt();
//                        presenter.createWebSocket();
                            notifier.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.closeWebsocket();
    }
}
