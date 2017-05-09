package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 4/4/17.
 */

public class SubmitImageEntity {


    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;
    @SerializedName("button")
    @Expose
    private Button button;
    @SerializedName("help_data")
    @Expose
    private HelpData helpData;
    @SerializedName("conversation_last")
    @Expose
    private List<ConversationLast> conversationLast = null;
    @SerializedName("solution_last")
    @Expose
    private SolutionLast solutionLast;

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public HelpData getHelpData() {
        return helpData;
    }

    public void setHelpData(HelpData helpData) {
        this.helpData = helpData;
    }

    public List<ConversationLast> getConversationLast() {
        return conversationLast;
    }

    public void setConversationLast(List<ConversationLast> conversationLast) {
        this.conversationLast = conversationLast;
    }

    public SolutionLast getSolutionLast() {
        return solutionLast;
    }

    public void setSolutionLast(SolutionLast solutionLast) {
        this.solutionLast = solutionLast;
    }

}
