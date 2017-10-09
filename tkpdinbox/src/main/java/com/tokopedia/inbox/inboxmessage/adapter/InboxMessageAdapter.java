package com.tokopedia.inbox.inboxmessage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.message.ListMessage;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageAttributes;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessage.presenter.InboxMessageFragmentPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 5/9/16.
 */
public class InboxMessageAdapter extends BaseLinearRecyclerViewAdapter
        implements InboxMessageConstant {

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_name)
        TextView userName;

        @BindView(R2.id.time)
        TextView time;

        @BindView(R2.id.user_ava)
        ImageView avatar;

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.main)
        View mainView;

        @BindView(R2.id.label)
        TextView label;

        TextView counterUnread;
//        @BindView(R2.id.reputation_view)
//        View viewReputation;
//
//        @BindView(R2.id.rep_rating)
//        TextView textPercentage;
//
//        @BindView(R2.id.rep_icon)
//        ImageView iconPercentage;
//
//        @BindView(R2.id.title)
//        TextView title;


        public ViewHolder(View itemView) {
            super(itemView);
            counterUnread = (TextView) itemView.findViewById(R.id.counter_unread);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<ListMessage> list;
    private ArrayList<ListMessage> listMove;
    private Context context;
    private InboxMessageFragmentPresenter presenter;
    private int selected = 0;
    SimpleDateFormat sdf;
    Locale id;
    boolean isActionEnabled = false;

    public InboxMessageAdapter(Context context, InboxMessageFragmentPresenter presenter) {
        super();
        this.context = context;
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.listMove = new ArrayList<>();
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
        this.isActionEnabled = false;

    }

    public static InboxMessageAdapter createAdapter(Context context, InboxMessageFragmentPresenter presenter) {
        return new InboxMessageAdapter(context, presenter);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_MESSAGE:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.message_item, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_MESSAGE:
                bindMessage((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }


    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_MESSAGE;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }


    private void bindMessage(ViewHolder holder, final int position) {

        MessageAttributes item = list.get(position).getAttributes();

//        holder.title.setText(list.get(position).getMessageTitle());
        holder.message.setText(item.getLastReplyMsg());
        holder.userName.setText(item.getContact().getAttributes().getName());
        holder.userName.setMovementMethod(new ScrollingMovementMethod());
        ImageHandler.loadImageCircle2(holder.avatar.getContext(), holder.avatar, item.getContact().getAttributes().getThumbnail());

        setTime(holder, position);
//        setDate(holder, position);
//        setHour(holder, position);

//        holder.textPercentage.setText(list.get(position).getUserReputation().getPositivePercentage());
//        setIconPercentage(holder, position);
        setLabel(holder, item.getContact().getAttributes().getTag());

        setSelectedStatus(holder, position);
        setReadStatus(holder, position);

//        holder.viewReputation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (selected == 0)
//                    ToolTipUtils.showToolTip(setViewToolTip(position), view);
//            }
//        });

//        holder.avatar.setOnClickListener(onGoToProfile(list.get(position)));
//        holder.userName.setOnClickListener(onGoToProfile(list.get(position)));
        holder.mainView.setOnClickListener(onMessageClicked(holder, position));

        if (isActionEnabled) {
            holder.mainView.setOnLongClickListener(onLongClickListener(holder, position));
        } else {
            holder.mainView.setOnLongClickListener(null);
        }
    }

    private void setSelectedStatus(ViewHolder holder, int position) {
        if (list.get(position).isChecked())
            setSelectedState(holder);
        else if (!list.get(position).isChecked())
            setUnselectedState(holder, position);
    }


    private void setReadStatus(ViewHolder holder, int position) {
        int readStatus = list.get(position).getAttributes().getReadStatus();
        int counter = list.get(position).getAttributes().getUnreads();
        if (readStatus == STATE_CHAT_UNREAD)
            setNotReadState(holder, counter);
        else if (readStatus == STATE_CHAT_READ)
            setReadState(holder);
    }


    private void setLabel(ViewHolder holder, String label) {
        holder.label.setText(label);
    }

    private void setTime(ViewHolder holder, int position) {
//        InboxTimeConverter.generateTime(list.get(position).getMessageCreateTimeFmt());
//        String time = android.text.format.DateFormat.getLongDateFormat(context).format(Long.parseLong(list.get(position).getAttributes().getLastReplyTime()));
        String time = ChatTimeConverter.formatTimeStamp(Long.parseLong(list.get(position).getAttributes().getLastReplyTime()));

        holder.time.setVisibility(View.VISIBLE);
        holder.time.setText(time);
    }

    private void setIconPercentage(ViewHolder holder, int position) {
//        if (list.get(position).getUserReputation().getNoReputation().equals("0")) {
//            setHasReputation(holder);
//        } else {
//            setNoReputation(holder);
//
//        }
    }
//
//    private void setHasReputation(ViewHolder holder) {
//        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
//        holder.textPercentage.setVisibility(View.VISIBLE);
//    }
//
//    private void setNoReputation(ViewHolder holder) {
//        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
//        holder.textPercentage.setVisibility(View.GONE);
//    }

    private View.OnLongClickListener onLongClickListener(final ViewHolder holder, final int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (list.get(position).isChecked()) {
                    setReadState(holder);
                    presenter.onDeselect(position);
                } else {
                    setSelectedState(holder);
                    presenter.onSelected(position);
                }

                return true;
            }
        };
    }

    private View.OnClickListener onMessageClicked(final ViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 0) {
                    presenter.goToDetailMessage(position, list.get(position));
                } else if (list.get(position).isChecked()) {
                    setReadState(holder);
                    presenter.onDeselect(position);
                } else {
                    setSelectedState(holder);
                    presenter.onSelected(position);
                }
            }
        };
    }

    private View.OnClickListener onGoToProfile(final InboxMessageItem messageItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageItem.getUserLabelId() != IS_ADMIN)
                    presenter.goToProfile(messageItem.getUserId());
            }
        };
    }

//    private View setViewToolTip(final int pos) {
//        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
//            @Override
//            public void setView(View view) {
//                TextView smile = (TextView) view.findViewById(R.id.text_smile);
//                TextView netral = (TextView) view.findViewById(R.id.text_netral);
//                TextView bad = (TextView) view.findViewById(R.id.text_bad);
//                smile.setText("" + list.get(pos).getUserReputation().getPositive());
//                netral.setText("" + list.get(pos).getUserReputation().getNeutral());
//                bad.setText("" + list.get(pos).getUserReputation().getNegative());
//            }
//
//            @Override
//            public void setListener() {
//
//            }
//        });
//    }

    private void setSelectedState(ViewHolder holder) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(holder.avatar, R.drawable.ic_check_circle_48dp);
    }

    private void setUnselectedState(ViewHolder holder, int position) {
//        ImageHandler.loadImageCircle2(context, holder.avatar, list.get(position).getUserImage());
    }

    private void setReadState(ViewHolder holder) {
        holder.counterUnread.setVisibility(View.GONE);
    }

    private void setNotReadState(ViewHolder holder, int counter) {
        holder.counterUnread.setVisibility(View.VISIBLE);
        if(counter > 0) {
            holder.counterUnread.setText(String.valueOf(counter));
        }else {
            holder.counterUnread.setVisibility(View.GONE);
        }
    }

    public void setList(List<ListMessage> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<ListMessage> getList() {
        return list;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void clearSelection() {
        for (ListMessage message : list) {
            message.setIsChecked(false);
        }
        listMove.clear();
        notifyDataSetChanged();
    }

    public void addChecked(int position) {
        ListMessage item = list.get(position);
        item.setPosition(position);
        listMove.add(item);
        list.get(position).setIsChecked(true);
        this.selected++;
        notifyDataSetChanged();
    }

    public void removeChecked(int position) {
        listMove.remove(list.get(position));
        list.get(position).setIsChecked(false);
        this.selected--;
        notifyDataSetChanged();
    }

    public void removeAllChecked() {
        for (ListMessage moveItem : listMove) {
            for (ListMessage inboxMessageItem : list) {
                if (moveItem.getMsgId() == inboxMessageItem.getMsgId()) {
                    list.remove(inboxMessageItem);
                    break;
                }
            }
        }

        listMove.clear();
        notifyDataSetChanged();
    }

    public ArrayList<ListMessage> getListMove() {
        return listMove;
    }

    public void setListMove(ArrayList<ListMessage> listMove) {
        this.listMove.addAll(listMove);
        notifyDataSetChanged();
    }

    public void setEnabled(boolean isActionEnabled) {
        this.isActionEnabled = isActionEnabled;
        notifyDataSetChanged();
    }

//    public void markAsRead() {
//        for (InboxMessageItem moveItem : listMove) {
//            for (InboxMessageItem inboxMessageItem : list) {
//                if (moveItem.getMessageId() == inboxMessageItem.getMessageId()) {
//                    inboxMessageItem.setMessageReadStatus(STATE_READ);
//                    break;
//                }
//            }
//        }
//
//        listMove.clear();
//        notifyDataSetChanged();
//    }
//
//    public void markAsUnread() {
//        for (InboxMessageItem moveItem : listMove) {
//            for (InboxMessageItem inboxMessageItem : list) {
//                if (moveItem.getMessageId() == inboxMessageItem.getMessageId()) {
//                    inboxMessageItem.setMessageReadStatus(STATE_NOT_READ);
//                    break;
//                }
//            }
//        }
//
//        listMove.clear();
//        notifyDataSetChanged();
//    }

    public int getChosenMessageStatus() {
        int statusRead = -1;
        int statusUnread = -1;

        for (ListMessage inboxMessageItem : listMove) {
            if (inboxMessageItem.getAttributes().getReadStatus() == STATE_READ)
                statusRead = STATE_READ;

            if (inboxMessageItem.getAttributes().getReadStatus() == STATE_NOT_READ) {
                statusUnread = STATE_NOT_READ;
            }
        }

        if (statusRead == STATE_READ && statusUnread == STATE_NOT_READ) {
            return STATE_BOTH;
        } else if (statusRead == STATE_READ) {
            return STATE_READ;
        } else if (statusUnread == STATE_NOT_READ) {
            return STATE_NOT_READ;
        } else {
            return -1;
        }
    }

}
