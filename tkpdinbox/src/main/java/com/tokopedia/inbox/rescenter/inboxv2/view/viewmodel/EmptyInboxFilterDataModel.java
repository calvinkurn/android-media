package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactory;

/**
 * Created by yfsx on 01/02/18.
 */

public class EmptyInboxFilterDataModel implements Visitable<ResoInboxTypeFactory> {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int type(ResoInboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
