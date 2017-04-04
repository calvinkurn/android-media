package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import android.widget.Button;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionValidationEntity {


    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("conversation_last")
    @Expose
    private List<ConversationLast> conversationLast = null;
    @SerializedName("button")
    @Expose
    private Button button;
    @SerializedName("solution_last")
    @Expose
    private SolutionLast solutionLast;
    @SerializedName("help_data")
    @Expose
    private HelpData helpData;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<ConversationLast> getConversationLast() {
        return conversationLast;
    }

    public void setConversationLast(List<ConversationLast> conversationLast) {
        this.conversationLast = conversationLast;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public SolutionLast getSolutionLast() {
        return solutionLast;
    }

    public void setSolutionLast(SolutionLast solutionLast) {
        this.solutionLast = solutionLast;
    }

    public HelpData getHelpData() {
        return helpData;
    }

    public void setHelpData(HelpData helpData) {
        this.helpData = helpData;
    }
}
