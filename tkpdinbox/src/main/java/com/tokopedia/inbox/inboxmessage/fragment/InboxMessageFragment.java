package com.tokopedia.inbox.inboxmessage.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.adapter.InboxMessageAdapter;
import com.tokopedia.inbox.inboxmessage.listener.InboxMessageView;
import com.tokopedia.inbox.inboxmessage.model.ActInboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessage.presenter.InboxMessageFragmentPresenter;
import com.tokopedia.inbox.inboxmessage.presenter.InboxMessageFragmentPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Nisie on 5/9/16.
 */
public class InboxMessageFragment extends BasePresenterFragment<InboxMessageFragmentPresenter>
        implements InboxMessageView, InboxMessageConstant {


    public void restackList(Bundle data) {
        Log.i("asdf", "restackList: ");
    }

    public interface DoActionInboxMessageListener {
        void archiveMessage(Bundle param);

        void undoArchiveMessage(Bundle param);

        void moveToInbox(Bundle param);

        void undoMoveToInbox(Bundle param);

        void deleteMessage(Bundle param);

        void undoDeleteMessage(Bundle param);

        void deleteMessageForever(Bundle param);

        void markAsRead(Bundle param);

        void markAsUnread(Bundle param);

    }

    public static InboxMessageFragment createInstance(String navigation) {
        InboxMessageFragment fragment = new InboxMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }


    RecyclerView mainList;

    SwipeToRefresh swipeToRefresh;

    FloatingActionButton fab;

    View filterLayout;
    EditText search;
    Button confirmButton;
    Button cancelButton;
    RadioButton radioAll;
    RadioButton radioUnread;
    RadioGroup viewFilterRadio;

    RefreshHandler refreshHandler;
    BottomSheetDialog bottomSheetDialog;
    LinearLayoutManager layoutManager;
    InboxMessageAdapter adapter;
    ActionMode.Callback moveInboxMode;
    ActionMode inboxMode;
    boolean isMultiActionEnabled = false;
    boolean isRetryShowing = false;
    public boolean isMustRefresh = false;

    TkpdProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;
    Snackbar snackbarUndo;
    ArrayList<InboxMessageItem> listMoveOnUndo;

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(FILTER_BUNDLE, presenter.getFilterParam());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        InboxMessagePass inboxMessagePass = savedState.getParcelable(FILTER_BUNDLE);
        presenter.restoreFilterBundle(inboxMessagePass);
        setRestoredFilter();

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxMessageFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_message;
    }

    @Override
    protected void initView(View view) {
        mainList = (RecyclerView) view.findViewById((R.id.chat_list));
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        filterLayout = getActivity().getLayoutInflater().inflate(R.layout.layout_filter_message, null);
        search = (EditText) filterLayout.findViewById(R.id.search);
        confirmButton = (Button) filterLayout.findViewById(R.id.button_confirm);
        cancelButton = (Button) filterLayout.findViewById(R.id.button_cancel);
        radioAll = (RadioButton) filterLayout.findViewById(R.id.radio_all);
        radioUnread = (RadioButton) filterLayout.findViewById(R.id.radio_unread);
        viewFilterRadio = (RadioGroup) filterLayout.findViewById(R.id.radio_read);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterLayout);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bsd = (BottomSheetDialog) dialog;
                FrameLayout frameLayout = (FrameLayout) bsd.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
        adapter = InboxMessageAdapter.createAdapter(getActivity(), presenter);
        refreshHandler = new RefreshHandler(getActivity(), swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshData();
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);
        moveInboxMode = onCallbackActionMode();
        setFilter();

    }

    private void setRestoredFilter() {
        InboxMessagePass inboxMessagePass = presenter.getFilterParam();
        if (inboxMessagePass != null) {
            search.setText(inboxMessagePass.getKeyword());
            if (inboxMessagePass.getFilter() != null && inboxMessagePass.getFilter().equals(PARAM_ALL)) {
                radioAll.setChecked(true);
            } else if (inboxMessagePass.getFilter() != null && inboxMessagePass.getFilter().equals(PARAM_UNREAD)) {
                radioUnread.setChecked(true);
            }
        }
    }

    private void setFilter() {
        if (getArguments().getString(PARAM_NAV, "").equals(MESSAGE_ALL))
            viewFilterRadio.setVisibility(View.VISIBLE);
        else
            viewFilterRadio.setVisibility(View.GONE);

    }

    private ActionMode.Callback onCallbackActionMode() {
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
                mode.setTitle(String.valueOf(adapter.getSelected()));
                switch (adapter.getChosenMessageStatus()) {
                    case STATE_READ:
                        menu.findItem(R.id.action_mark_as_unread).setVisible(true);
                        menu.findItem(R.id.action_mark_as_read).setVisible(false);
                        break;
                    case STATE_NOT_READ:
                        menu.findItem(R.id.action_mark_as_read).setVisible(true);
                        menu.findItem(R.id.action_mark_as_unread).setVisible(false);
                        break;
                    case STATE_BOTH:
                        menu.findItem(R.id.action_mark_as_unread).setVisible(true);
                        menu.findItem(R.id.action_mark_as_read).setVisible(true);
                        break;
                    default:
                        menu.findItem(R.id.action_mark_as_unread).setVisible(false);
                        menu.findItem(R.id.action_mark_as_read).setVisible(false);
                        break;
                }
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
                adapter.setSelected(0);
                adapter.clearSelection();
                isMultiActionEnabled = false;
                enableActions();
            }
        };
    }

    @Override
    protected void setViewListener() {
        adapter.setOnRetryListenerRV(onRetry());
        mainList.addOnScrollListener(onScroll());
        fab.setOnClickListener(onFabClicked());
        confirmButton.setOnClickListener(onSearchFilter());
        cancelButton.setOnClickListener(onCancelFilter());
    }

    private RetryDataBinder.OnRetryListener onRetry() {
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.getInboxMessage();
            }
        };
    }


    private View.OnClickListener onCancelFilter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                radioAll.setChecked(true);
                fab.show();

            }
        };
    }

    private View.OnClickListener onSearchFilter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                adapter.getList().clear();
                presenter.generateSearchParam();
                presenter.getInboxMessage();

                finishContextMode();

            }
        };
    }

    private View.OnClickListener onFabClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        };
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem);
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
    public void disableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(false);
        if (fab.isShown())
            fab.hide();
        adapter.setEnabled(false);
    }

    @Override
    public void enableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(true);

        if (adapter.getSelected() == 0)
            fab.show();

        if (presenter.hasActionListener()) {
            adapter.setEnabled(true);
        }
    }

    @Override
    public String getFilter() {
        if (radioAll.isChecked())
            return PARAM_ALL;
        else
            return PARAM_UNREAD;
    }

    @Override
    public String getKeyword() {
        return search.getText().toString();
    }

    @Override
    public InboxMessageAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        adapter.showLoading(false);
        progressDialog.dismiss();
    }

    @Override
    public void removeError() {
        adapter.showEmptyFull(false);
        adapter.showRetry(false);
        adapter.showRetryFull(false);
        isRetryShowing = false;
        NetworkErrorHelper.hideEmptyState(getView());


    }

    @Override
    public void showError(String message) {
        if (message.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void setRetry(String message, final View.OnClickListener listener) {
        finishLoading();
        disableActions();
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                listener.onClick(getView());
            }
        });
        snackbarRetry.showRetrySnackbar();
        isRetryShowing = true;
    }

    @Override
    public void setOptionsMenu() {
        if (!isMultiActionEnabled)
            inboxMode = ((AppCompatActivity) getActivity()).startSupportActionMode(moveInboxMode);

        if (inboxMode != null) {
            inboxMode.invalidate();
            if (adapter.getSelected() == 0) {
                inboxMode.finish();
            }
            inboxMode.setTitle(String.valueOf(adapter.getSelected()));
        }

    }

    @Override
    public void showUndoSnackBar(String message, View.OnClickListener onUndo) {
        snackbarUndo = SnackbarManager.make(getActivity(),
                message, Snackbar.LENGTH_LONG).setAction(getString(R.string.undo_but), onUndo)
        ;
        snackbarUndo.show();
    }


    public void finishContextMode() {
        if (!isMultiActionEnabled)
            inboxMode = ((AppCompatActivity) getActivity()).startSupportActionMode(moveInboxMode);

        if (inboxMode != null) {
            inboxMode.finish();
        }
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            presenter.setUserVisibleHint(isVisibleToUser, isMustRefresh);
            if (snackbarRetry != null) {
                snackbarRetry.resumeRetrySnackbar();
                if (!isVisibleToUser)
                    snackbarRetry.pauseRetrySnackbar();
            }
            if (snackbarUndo != null) {
                snackbarUndo.dismiss();
            }
            finishContextMode();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccessMoveArchive(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_ARCHIVE_MESSAGE);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
//            listMoveOnUndo = new ArrayList<InboxMessageItem>();
//            for (InboxMessageItem item : getAdapter().getListMove()) {
//                listMoveOnUndo.add(item);
//            }
            getAdapter().removeAllChecked();
            if (getAdapter().getList().size() == 0) {
                getAdapter().showEmptyFull(true);
                pass.setListMove(listMoveOnUndo);
                getAdapter().notifyDataSetChanged();
            }
            showUndoSnackBar(getString(R.string.message_undo_to_archive), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.clearListMove();
                    presenter.undoArchiveMessage(pass.getListMove());
                    getAdapter().showEmptyFull(false);
                    getAdapter().clearSelection();
                }
            });
        }
    }

    @Override
    public void onSuccessUndoMoveArchive(Bundle resultData) {
        finishLoading();
        enableActions();

        ActInboxMessagePass pass = resultData.getParcelable(PARAM_UNDO_ARCHIVE_MESSAGE);
        if (pass != null) {
            for (InboxMessageItem item : pass.getListMove()) {
//                getAdapter().getList().add(item.getPosition(), item);
            }
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessMoveToInbox(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_MOVE_TO_INBOX);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
            listMoveOnUndo = new ArrayList<InboxMessageItem>();
            for (InboxMessageItem item : pass.getListMove()) {
                listMoveOnUndo.add(item);
            }
            getAdapter().removeAllChecked();
            if (getAdapter().getList().size() == 0) {
                getAdapter().showEmptyFull(true);
                pass.setListMove(listMoveOnUndo);
                getAdapter().notifyDataSetChanged();
            }
            showUndoSnackBar(getString(R.string.message_undo_to_inbox), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.clearListMove();
                    presenter.undoMoveToInbox(pass.getListMove());
                    getAdapter().showEmptyFull(false);
                    getAdapter().clearSelection();
                }
            });
        }
    }

    @Override
    public void onSuccessUndoMoveToInbox(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_UNDO_MOVE_TO_INBOX);
        if (pass != null) {

            for (InboxMessageItem item : pass.getListMove()) {
//                getAdapter().getList().add(item.getPosition(), item);
            }
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessDeleteMessage(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_DELETE_MESSAGE);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
            listMoveOnUndo = new ArrayList<InboxMessageItem>();
            for (InboxMessageItem item : pass.getListMove()) {
                listMoveOnUndo.add(item);
            }
            getAdapter().removeAllChecked();
            if (getAdapter().getList().size() == 0) {
                getAdapter().showEmptyFull(true);
                pass.setListMove(listMoveOnUndo);
                getAdapter().notifyDataSetChanged();
            }
            showUndoSnackBar(getString(R.string.message_undo_to_trash), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.clearListMove();
                    presenter.undoDeleteMessage(pass.getListMove());
                    getAdapter().showEmptyFull(false);
                    getAdapter().clearSelection();
                }
            });
        }
    }

    @Override
    public void onSuccessUndoDeleteMessage(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_UNDO_DELETE_MESSAGE);
        if (pass != null) {
            for (InboxMessageItem item : pass.getListMove()) {
//                getAdapter().getList().add(item.getPosition(), item);
            }
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessDeleteForever(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_DELETE_FOREVER);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
//            getAdapter().removeAllChecked();
        }
    }

    @Override
    public void onFailedMoveArchive(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_ARCHIVE_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedUndoMoveArchive(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_UNDO_ARCHIVE_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedMoveToInbox(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_MOVE_TO_INBOX);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedUndoMoveToInbox(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_UNDO_MOVE_TO_INBOX);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedDeleteMessage(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_DELETE_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedUndoDeleteMessage(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_UNDO_DELETE_MESSAGE);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedDeleteForever(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(DELETE_FOREVER);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void showEmptyState() {
        isRetryShowing = true;
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getInboxMessage();
            }
        });
    }

    @Override
    public void showEmptyState(String error) {
        isRetryShowing = true;
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getInboxMessage();
            }
        });
    }

    @Override
    public boolean hasRetry() {
        return isRetryShowing;
    }

    @Override
    public void setMustRefresh(boolean isMustRefresh) {
        this.isMustRefresh = isMustRefresh;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DETAIL_MESSAGE && resultCode == Activity.RESULT_OK) {
//            presenter.setMessageRead(data);
            if (data.getExtras().getBoolean(MUST_REFRESH))
                presenter.refreshData();
        }
    }

    @Override
    public void onFailedMarkAsRead(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_MARK_AS_READ);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onFailedMarkAsUnread(Bundle resultData) {
        final ActInboxMessagePass param = resultData.getParcelable(PARAM_MARK_AS_UNREAD);
        String error = resultData.getString(EXTRA_ERROR, "");
        enableActions();
        finishLoading();
        showError(error);
    }

    @Override
    public void onSuccessMarkAsRead(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_MARK_AS_READ);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
//            getAdapter().markAsRead();
        }
    }

    @Override
    public void onSuccessMarkAsUnread(Bundle resultData) {
        finishLoading();
        enableActions();

        final ActInboxMessagePass pass = resultData.getParcelable(PARAM_MARK_AS_UNREAD);
        if (pass != null) {
//            getAdapter().setListMove(pass.getListMove());
//            getAdapter().markAsUnread();
        }
    }
}
