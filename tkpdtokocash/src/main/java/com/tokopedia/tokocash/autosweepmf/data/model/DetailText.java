package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.SerializedName;

public class DetailText {
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String content;
    @SerializedName("tooltip_content")
    private String tooltipContent;

    public String getTooltipContent() {
        return tooltipContent;
    }

    public void setTooltipContent(String tooltipContent) {
        this.tooltipContent = tooltipContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DetailText{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tooltipContent='" + tooltipContent + '\'' +
                '}';
    }
}
