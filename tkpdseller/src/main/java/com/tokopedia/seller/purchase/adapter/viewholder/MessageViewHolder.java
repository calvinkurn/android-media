package com.tokopedia.seller.purchase.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by Fajar Ulin Nuha on 12/10/18.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public MessageViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_message);
    }
}
