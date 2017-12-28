package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.inboxchat.viewholder.ItemTemplateChatViewHolder;

import java.util.List;

/**
 * Created by stevenfredian on 12/11/17.
 */

public class TemplateChatContract {

    public interface View extends CustomerView {

        void setTemplate(List<Visitable> listTemplate);

        void onDrag(ItemTemplateChatViewHolder ini);

        void onEnter(String message);

        void setChecked(boolean b);

        void reArrange(int from);
    }

    interface Presenter extends CustomerPresenter<View> {

        void setArrange(boolean enabled);

        void setArrange(int from);
    }
}
