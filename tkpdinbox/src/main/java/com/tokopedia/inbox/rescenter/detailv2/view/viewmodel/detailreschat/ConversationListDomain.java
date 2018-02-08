package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 09/11/17.
 */

public class ConversationListDomain implements Parcelable {

    public static final Parcelable.Creator<ConversationListDomain> CREATOR = new Parcelable.Creator<ConversationListDomain>() {
        @Override
        public ConversationListDomain createFromParcel(Parcel source) {
            return new ConversationListDomain(source);
        }

        @Override
        public ConversationListDomain[] newArray(int size) {
            return new ConversationListDomain[size];
        }
    };
    private int canLoadMore;
    private List<ConversationDomain> conversationDomains;

    public ConversationListDomain(int canLoadMore, List<ConversationDomain> conversationDomains) {
        this.canLoadMore = canLoadMore;
        this.conversationDomains = conversationDomains;
    }

    protected ConversationListDomain(Parcel in) {
        this.canLoadMore = in.readInt();
        this.conversationDomains = in.createTypedArrayList(ConversationDomain.CREATOR);
    }

    public int getCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public List<ConversationDomain> getConversationDomains() {
        return conversationDomains;
    }

    public void setConversationDomains(List<ConversationDomain> conversationDomains) {
        this.conversationDomains = conversationDomains;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.canLoadMore);
        dest.writeTypedList(this.conversationDomains);
    }
}
