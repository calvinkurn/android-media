package com.tokopedia.inbox.inboxchat.chattemplate.view.listener;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.TemplateChatSettingAdapter;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 12/11/17.
 */

public class TemplateChatContract {

    public interface View extends CustomerView {

        void setTemplate(List<Visitable> listTemplate);

        void onDrag(ItemTemplateChatViewHolder ini);

        void onEnter(String message, int adapterPosition);

        void setChecked(boolean b);

        void reArrange(int from, int to);

        ArrayList<String> getList();

        TemplateChatSettingAdapter getAdapter();

        void successRearrange();

        void showError(String errorMessage);

        void successSwitch();

        void showLoading();

        void finishLoading();

        void revertArrange(int from, int to);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void switchTemplateAvailability(boolean enabled);

        void setArrange(boolean enabled, ArrayList<Integer> arrayList, int from, int to);
    }
}
