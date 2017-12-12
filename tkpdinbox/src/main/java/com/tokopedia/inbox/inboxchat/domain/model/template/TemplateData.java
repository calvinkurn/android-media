package com.tokopedia.inbox.inboxchat.domain.model.template;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateData {
    @SerializedName("is_enable")
    @Expose
    private boolean isEnable;
    @SerializedName("templates")
    @Expose
    private List<String> templates = null;

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }
}
