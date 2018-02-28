package com.tokopedia.ride.bookingride.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.MessageViewHolder;
import com.tokopedia.ride.chat.utils.ChatMessage;
import com.tokopedia.ride.chat.utils.ItemSentViewUberSms;
import com.tokopedia.ride.chat.utils.ViewBuilder;
import com.tokopedia.ride.chat.utils.ViewBuilderInterface;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ChatViewListAdapter extends BaseAdapter {

    private final int STATUS_SENT = 0;
    private final int STATUS_RECEIVED = 1;
    private final int TICKER_MSG = -1;

    private ViewBuilderInterface viewBuilder = new ViewBuilder();

    private ArrayList<ChatMessage> chatMessages;
    private OnRetryTap onRetryTap;

    private Context context;
    private LayoutInflater inflater;

    public ChatViewListAdapter(Context context, ViewBuilderInterface viewBuilder) {
        this.chatMessages = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
                    holder = new MessageViewHolder(convertView);

                    convertView.setTag(holder);
                    break;
                case STATUS_RECEIVED:
                    convertView = viewBuilder.buildRecvView(context);
                    holder = new MessageViewHolder(convertView);
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

            ChatMessage chatMessage = chatMessages.get(position - 1);
            holder.setMessage(chatMessage.getMessage());
            holder.setTimestamp(chatMessage.getFormattedTime());

            if (holder.getUberSmsMessageView() instanceof ItemSentViewUberSms &&
                    chatMessage.getDeliveryStatus() != null) {

                switch (chatMessage.getDeliveryStatus()) {

                    case SENT_FAILURE:
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIconTag(chatMessage);
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIcon(context.getResources().getDrawable(R.drawable.ic_error_to_send), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (onRetryTap != null && view.getTag() != null) {
                                    onRetryTap.onRetryTap((ChatMessage) view.getTag());
                                }
                            }
                        });
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setDeliveryStatusIcon(null);
                        break;

                    case SENT_SUCCESS:
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIcon(null);
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setDeliveryStatusIcon(context.getResources().getDrawable(R.drawable.ic_sms_sent));
                        break;

                    case DELIVER_SUCCESS:
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIcon(null);
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setDeliveryStatusIcon(context.getResources().getDrawable(R.drawable.ic_sms_delivered));
                        break;

                    case DELIVER_FAILURE:
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIcon(null);
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setDeliveryStatusIcon(context.getResources().getDrawable(R.drawable.ic_sms_sent));
                        break;

                    default:
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setRetryIcon(null);
                        ((ItemSentViewUberSms) holder.getUberSmsMessageView()).setDeliveryStatusIcon(null);
                }

            }

        } else if (position == 0 && tickerViewHolder != null) {
            tickerViewHolder.tickerMsg.setText(R.string.sms_charges_msg);
        }

        return convertView;
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyDataSetChanged();
    }

    public void addMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
        notifyDataSetChanged();
    }

    public void removeMessage(int position) {
        if (this.chatMessages.size() > position) {
            this.chatMessages.remove(position);
        }
    }

    public void clearMessages() {
        this.chatMessages.clear();
        notifyDataSetChanged();
    }

    public void setOnRetryTap(OnRetryTap onRetryTap) {
        this.onRetryTap = onRetryTap;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    class TickerViewHolder {
        TextView tickerMsg;

        TickerViewHolder(View view) {
            tickerMsg = view.findViewById(R.id.ticker_msg);
        }
    }

    public interface OnRetryTap {
        public void onRetryTap(ChatMessage chatMessage);
    }
}

