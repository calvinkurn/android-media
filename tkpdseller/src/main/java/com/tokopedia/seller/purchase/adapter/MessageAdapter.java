package com.tokopedia.seller.purchase.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.purchase.adapter.viewholder.MessageViewHolder;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<String> message;

    public MessageAdapter(List<String> message) {
        this.message = message;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.textView.setText(message.get(position));
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

}
