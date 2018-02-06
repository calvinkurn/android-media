package com.tokopedia.inbox.inboxchat.domain.usecase;

import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DeleteMessageListUseCase extends UseCase<DeleteChatListViewModel>{

    MessageRepository messageRepository;

    public DeleteMessageListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<DeleteChatListViewModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return messageRepository.deleteMessage(object);
    }

    public static RequestParams generateParam(InboxMessagePass inboxMessagePass, int page)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("per_page", "10");
        requestParams.putString("platform","android");
        return requestParams;
    }

    public static RequestParams generateParam(List<Pair> listMove) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for(Pair item : listMove){
            ChatListViewModel first = (ChatListViewModel) item.first;
            array.add(Integer.valueOf(first.getId()));
        }
        object.add("list_msg_id", array);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
