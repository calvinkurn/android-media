package com.tokopedia.transaction.purchase.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

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

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_message);
        }
    }
}
