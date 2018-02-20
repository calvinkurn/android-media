package com.tokopedia.ride.chat.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.ride.R;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ChatViewListAdapter extends BaseAdapter {

    private final int STATUS_SENT = 0;
    private final int STATUS_RECEIVED = 1;
    private final int TICKER_MSG = -1;

    private int bubbleBackgroundRcv, bubbleBackgroundSend;
    private float bubbleElevation;
    private ViewBuilderInterface viewBuilder = new ViewBuilder();

    private ArrayList<ChatMessage> chatMessages;

    private Context context;
    private LayoutInflater inflater;

    ChatViewListAdapter(Context context, ViewBuilderInterface viewBuilder, int bubbleBackgroundRcv, int bubbleBackgroundSend, float bubbleElevation) {
        this.chatMessages = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.bubbleBackgroundRcv = bubbleBackgroundRcv;
        this.bubbleBackgroundSend = bubbleBackgroundSend;
        this.bubbleElevation = bubbleElevation;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public int getCount() {
        return chatMessages.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) return new Object();
        return chatMessages.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return -1;
        return chatMessages.get(position - 1).getType().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder = null;
        TickerViewHolder tickerViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case STATUS_SENT:
                    convertView = viewBuilder.buildSentView(context);
                    holder = new MessageViewHolder(convertView, bubbleBackgroundRcv, bubbleBackgroundSend);
                    convertView.setTag(holder);
                    break;
                case STATUS_RECEIVED:
                    convertView = viewBuilder.buildRecvView(context);
                    holder = new MessageViewHolder(convertView, bubbleBackgroundRcv, bubbleBackgroundSend);
                    convertView.setTag(holder);
                    break;
                case TICKER_MSG:
                    convertView = inflater.inflate(R.layout.ticker_msg_view, null);
                    tickerViewHolder = new TickerViewHolder(convertView);
                    convertView.setTag(tickerViewHolder);
                    break;
            }

        } else if (position == 0) {
            tickerViewHolder = (TickerViewHolder) convertView.getTag();
        } else {
            holder = (MessageViewHolder) convertView.getTag();
        }

        if (position != 0 && holder != null) {
            holder.setMessage(chatMessages.get(position - 1).getMessage());
            holder.setTimestamp(chatMessages.get(position - 1).getFormattedTime());
            holder.setElevation(bubbleElevation);
            holder.setBackground(type);
            String sender = chatMessages.get(position - 1).getSender();
            if (sender != null) {
                holder.setSender(sender);
            }
        } else if (position == 0 && tickerViewHolder != null) {
            tickerViewHolder.tickerMsg.setText(R.string.sms_charges_msg);
        }

        return convertView;
    }

    void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyDataSetChanged();
    }

    void addMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
        notifyDataSetChanged();
    }

    void removeMessage(int position) {
        if (this.chatMessages.size() > position) {
            this.chatMessages.remove(position);
        }
    }

    void clearMessages() {
        this.chatMessages.clear();
        notifyDataSetChanged();
    }

    class TickerViewHolder {
        TextView tickerMsg;

        TickerViewHolder(View view) {
            tickerMsg = view.findViewById(R.id.ticker_msg);
        }
    }
}

