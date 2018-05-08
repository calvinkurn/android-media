package com.tokopedia.inbox.inboxchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyAdapter extends RecyclerView.Adapter<QuickReplyAdapter.Holder> {

    private QuickReplyListViewModel quickReplyListViewModel;

    public QuickReplyAdapter(QuickReplyListViewModel quickReplyListViewModel) {
        this.quickReplyListViewModel = quickReplyListViewModel;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_quick_reply, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        QuickReplyViewModel model = quickReplyListViewModel.getQuickReplies().get(position);
        holder.text.setText(model.getMessage());
    }

    @Override
    public int getItemCount() {
        return quickReplyListViewModel.getQuickReplies().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text;
        public Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
