package com.tokopedia.inbox.inboxtalk.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.inbox.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.inbox.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.inbox.inboxtalk.interactor.InboxTalkCacheInteractor;
import com.tokopedia.inbox.inboxtalk.interactor.InboxTalkCacheInteractorImpl;
import com.tokopedia.inbox.inboxtalk.interactor.InboxTalkRetrofitInteractor;
import com.tokopedia.inbox.inboxtalk.interactor.InboxTalkRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxtalk.listener.InboxTalkActivityView;
import com.tokopedia.inbox.inboxtalk.listener.InboxTalkView;
import com.tokopedia.core.talk.model.model.InboxTalk;
import com.tokopedia.core.talk.model.model.InboxTalkListModel;
import com.tokopedia.core.talkview.model.TalkPass;
import com.tokopedia.core.util.NewPagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public class InboxTalkPresenterImpl implements InboxTalkPresenter {

    InboxTalkView view;
    InboxTalkRetrofitInteractor facade;
    InboxTalkCacheInteractor cacheInteractor;
    InboxTalkActivityView listener;
    int positionAction;
    String nextPage;

    public InboxTalkPresenterImpl(InboxTalkFragment view) {
        this.view = view;
        nextPage = "0";
        facade = InboxTalkRetrofitInteractorImpl.createInstance(this);
        cacheInteractor = InboxTalkCacheInteractorImpl.createInstance(this);
        if (view.getActivity() instanceof InboxTalkActivity)
            listener = (InboxTalkActivity) view.getActivity();
    }


    @Override
    public void getInboxTalk(final Context context, final Map<String, String> param) {
        param.put("page", nextPage);
        if (!nextPage.equals("0") && !nextPage.equals("") && nextPage != null) {
            request(context, param);
        } else {
            view.cancelRequest();
        }
    }

    @Override
    public void refreshInboxTalk(final Context context, final Map<String, String> param) {
        param.put("page", "1");
        request(context, param);
    }

    private void request(Context context, final Map<String, String> param) {
        final int page = Integer.parseInt(param.get("page"));
        facade.getInboxTalk(context, param, new InboxTalkRetrofitInteractor.GetInboxTalkListener() {
            @Override
            public void onError(String error) {
                view.onTimeoutResponse(error, page);
            }

            @Override
            public void onThrowable(Throwable e) {
                view.onTimeoutResponse(page);
            }

            @Override
            public void onTimeout() {
                view.onTimeoutResponse(page);
            }

            @Override
            public void onSuccess(JSONObject result) {
                InboxTalkListModel model = facade.parseList(result);
                NewPagingHandler.PagingBean paging = facade.parsePaging(result);
                nextPage = paging.getNextPage();
                if (nextPage != null && !nextPage.equals("0") && !nextPage.equals("")) {
                    view.setLoadingFooter();
                } else {
                    nextPage = "0";
                    view.removeLoadingFooter();
                }
                if (page == 1 && model.getList() != null) {
                    cacheInteractor.storeFirstPage(param.get("nav"), param.get("filter"), result);
                }
                view.onConnectionResponse(model.getList(), page, model.getIsUnread());
            }
        });
    }

    @Override
    public void unSubscribe() {
        facade.unSubscribe();
        cacheInteractor.unsubscribe();
    }

    @Override
    public void saveState(Bundle state, List<RecyclerViewItem> items, int position, String filterString) {
        if (items != null) {
            state.putSerializable("list", (Serializable) items);
            state.putInt("paging", Integer.parseInt(nextPage));
            state.putString("filter", filterString);
            if (nextPage != null && !nextPage.equals("0") && !nextPage.equals(""))
                state.putInt("position", position - 1);
            else
                state.putInt("position", position);
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        List<RecyclerViewItem> list = (List<RecyclerViewItem>) savedState.getSerializable("list");
        int position = savedState.getInt("position");
        int page = savedState.getInt("paging");
        nextPage = String.valueOf(page);
        boolean hasNext = savedState.getBoolean("hasNext");
        String filterString = savedState.getString("filter");
        if (nextPage != null && !nextPage.equals("0") && !nextPage.equals("")) {
            view.setLoadingFooter();
        } else {
            nextPage = "0";
            view.removeLoadingFooter();
        }
        view.onStateResponse(list, position, page, hasNext, filterString);
    }

    @Override
    public void getInboxTalkFromCache(Map<String, String> param) {
        cacheInteractor.getInboxTalkFromCache(param.get("nav"),
                new InboxTalkCacheInteractor.GetInboxTalkListener() {
                    @Override
                    public void onError(String error) {
                        view.onCacheNoResult();
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        view.onCacheNoResult();
                    }

                    @Override
                    public void onTimeout() {
                        view.onCacheNoResult();
                    }

                    @Override
                    public void onSuccess(JSONObject result) {
                        if (result != null) {
                            InboxTalkListModel model = facade.parseList(result);
//                    PagingHandler.PagingHandlerModel paging = facade.parsePaging(result);
                            NewPagingHandler.PagingBean paging = facade.parsePaging(result);
                            nextPage = paging.getNextPage();
                            if (nextPage != null && !nextPage.equals("0") && !nextPage.equals("")) {
                                view.setLoadingFooter();
                            } else {
                                nextPage = "0";
                                view.removeLoadingFooter();
                            }
                            view.onCacheResponse(model.getList(), model.getIsUnread());
                        } else {
                            view.onCacheNoResult();
                        }
                    }
                });
    }


    @Override
    public void followTalk(InboxTalk talk, int position) {
        setPositionAction(position);
        TalkPass pass = new TalkPass();
        pass.setTalkId(talk.getTalkId());
        pass.setShopId(talk.getTalkShopId());
        pass.setProductId(talk.getTalkProductId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_FOLLOW, pass);
        if (listener != null) listener.followTalk(bundle);
    }


    @Override
    public void deleteTalk(InboxTalk talk, int position) {
        setPositionAction(position);
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getTalkId());
        param.setShopId(talk.getTalkShopId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_DELETE, param);
        if (listener != null) listener.deleteTalk(bundle);
    }


    @Override
    public void reportTalk(InboxTalk talk, int position) {
        setPositionAction(position);
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getTalkId());
        param.setShopId(talk.getTalkShopId());
        param.setProductId(talk.getTalkProductId());
        param.setTextMessage(talk.getTalkMessage());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_REPORT, param);
        if (listener != null) listener.reportTalk(bundle);
    }

    @Override
    public int getPositionAction() {
        return positionAction;
    }


    public void setPositionAction(int position) {
        positionAction = position;
    }
}
