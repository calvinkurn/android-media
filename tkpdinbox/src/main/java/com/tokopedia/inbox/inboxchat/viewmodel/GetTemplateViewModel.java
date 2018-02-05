package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;

import java.util.List;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateViewModel {

    boolean isSuccess;

    boolean isEnabled;

    private List<Visitable> listTemplate;

    public void setListTemplate(List<Visitable> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public List<Visitable> getListTemplate() {
        return listTemplate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
