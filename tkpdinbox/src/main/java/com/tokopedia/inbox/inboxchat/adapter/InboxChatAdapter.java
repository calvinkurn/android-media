package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatAdapter extends BaseLinearRecyclerViewAdapter
        implements InboxMessageConstant {


    public void showTimeMachine() {

    }

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

        ImageView checked;

        TextView section;

        public ViewHolder(View itemView) {
            super(itemView);
            counterUnread = (TextView) itemView.findViewById(R.id.counter_unread);
            section = (TextView) itemView.findViewById(R.id.section);
            checked = (ImageView) itemView.findViewById(R.id.checked);

            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<ChatListViewModel> list;
    private ArrayList<ChatListViewModel> listMove;
    private Context context;
    private InboxChatPresenter presenter;
    private int selected = 0;
    SimpleDateFormat sdf;
    Locale id;
    boolean isActionEnabled = false;

    public InboxChatAdapter(Context context, InboxChatPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.listMove = new ArrayList<>();
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
        this.isActionEnabled = false;

    }

    public static InboxChatAdapter createAdapter(Context context, InboxChatPresenter presenter) {
        return new InboxChatAdapter(context, presenter);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_MESSAGE:
                return new InboxChatAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.message_item, viewGroup, false));
            default:

                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_MESSAGE:
                bindMessage((InboxChatAdapter.ViewHolder) holder, position);
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


    private void bindMessage(InboxChatAdapter.ViewHolder holder, final int position) {

        ChatListViewModel item = list.get(position);

        if(item.getSpanMode() == ChatListViewModel.SPANNED_MESSAGE){
            holder.message.setText(highlight(holder.message.getContext(), item.getSpan(), presenter.getKeyword()));
            holder.userName.setText(item.getName());
        }else if(item.getSpanMode() == ChatListViewModel.SPANNED_CONTACT){
            holder.userName.setText(highlight(holder.message.getContext(), item.getSpan(), presenter.getKeyword()));
            holder.message.setText(item.getMessage());
        }else {
            holder.message.setText(item.getMessage());
            holder.userName.setText(item.getName());
        }

        if(item.getSectionSize()>0){
            holder.section.setText(item.getSectionSize() + " ditemukan");
            holder.section.setVisibility(View.VISIBLE);
        }else {
            holder.section.setVisibility(View.GONE);
        }

        ImageHandler.loadImageCircle2(holder.avatar.getContext(), holder.avatar, item.getImage());

        long unixdate = Long.parseLong(item.getTime());
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixdate * 1000);
        System.out.println("Formatted Date:" + formatter.format(calendar.getTime()));

        setTime(holder, position);
        setLabel(holder, item.getLabel());

        setSelectedStatus(holder, position);
        setReadStatus(holder, position);


//        holder.avatar.setOnClickListener(onGoToProfile(list.get(position)));
//        holder.userName.setOnClickListener(onGoToProfile(list.get(position)));
        holder.mainView.setOnClickListener(onMessageClicked(holder));

        if (isActionEnabled) {
            holder.mainView.setOnLongClickListener(onLongClickListener(holder));
        } else {
            holder.mainView.setOnLongClickListener(null);
        }
    }

    private SpannableString highlight(Context context, Spanned span, String keyword) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(span);

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new ForegroundColorSpan(MethodChecker.getColor(context,R.color.medium_green)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }

    private void setSelectedStatus(InboxChatAdapter.ViewHolder holder, int position) {
        if (list.get(position).isChecked())
            setSelectedState(holder);
        else if (!list.get(position).isChecked())
            setUnselectedState(holder, position);
    }

    private void setReadStatus(InboxChatAdapter.ViewHolder holder, int position) {
        int readStatus = list.get(position).getReadStatus();
        int counter = list.get(position).getUnreadCounter();
        if (readStatus == STATE_CHAT_UNREAD)
            setNotReadState(holder, counter);
        else if (readStatus == STATE_CHAT_READ)
            setReadState(holder);
    }

    private void setLabel(InboxChatAdapter.ViewHolder holder, String label) {
        if(label!=null && label.length()>0){
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setText(label);
        }else {
            holder.label.setVisibility(View.GONE);
        }
    }

    private void setTime(InboxChatAdapter.ViewHolder holder, int position) {
        String time = ChatTimeConverter.formatTimeStamp(Long.parseLong(list.get(position).getTime()));
        holder.time.setVisibility(View.VISIBLE);
        holder.time.setText(time);
    }

    private View.OnLongClickListener onLongClickListener(final InboxChatAdapter.ViewHolder holder) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
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

    private View.OnClickListener onMessageClicked(final InboxChatAdapter.ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (selected == 0) {
                    list.get(position).setReadStatus(STATE_CHAT_READ);
                    list.get(position).setUnreadCounter(0);
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

    private void setSelectedState(InboxChatAdapter.ViewHolder holder) {
        Context context = holder.mainView.getContext();
        holder.mainView.setBackgroundColor(context.getResources().getColor(R.color.green_selected));
        holder.checked.setVisibility(View.VISIBLE);
    }

    private void setUnselectedState(InboxChatAdapter.ViewHolder holder, int position) {
        holder.mainView.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.checked.setVisibility(View.GONE);
    }

    private void setReadState(InboxChatAdapter.ViewHolder holder) {
        holder.counterUnread.setVisibility(View.GONE);
        holder.userName.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

    }

    private void setNotReadState(InboxChatAdapter.ViewHolder holder, int counter) {
        holder.counterUnread.setVisibility(View.VISIBLE);
        holder.userName.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));

        if(counter > 0) {
            holder.counterUnread.setText(String.valueOf(counter));
        }else {
            holder.counterUnread.setVisibility(View.GONE);
        }
    }

    public void setList(List<ChatListViewModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<ChatListViewModel> list) {
        int index = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public ArrayList<ChatListViewModel> getList() {
        return list;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void clearSelection() {
        for (ChatListViewModel message : list) {
            message.setChecked(false);
        }
        listMove.clear();
        notifyDataSetChanged();
    }

    public void addChecked(int position) {
        ChatListViewModel item = list.get(position);
        listMove.add(item);
        list.get(position).setChecked(true);
        this.selected++;
        notifyItemChanged(position);
    }

    public void removeChecked(int position) {
        listMove.remove(list.get(position));
        list.get(position).setChecked(false);
        this.selected--;
        notifyItemChanged(position);
    }

    public void removeAllChecked() {
        for (ChatListViewModel moveItem : listMove) {
            for (ChatListViewModel inboxMessageItem : list) {
                if (moveItem.getId() == inboxMessageItem.getId()) {
                    list.remove(inboxMessageItem);
                    break;
                }
            }
        }

        listMove.clear();
        notifyDataSetChanged();
    }

    public ArrayList<ChatListViewModel> getListMove() {
        return listMove;
    }

    public void setListMove(ArrayList<ChatListViewModel> listMove) {
        this.listMove.addAll(listMove);
        notifyDataSetChanged();
    }

    public void setEnabled(boolean isActionEnabled) {
        this.isActionEnabled = isActionEnabled;
        notifyDataSetChanged();
    }


    public void moveToTop(String senderId, String lastReply, boolean showNotif) {
        String currentId;
        for (int i = 0; i < list.size(); i++) {
            currentId = String.valueOf(list.get(i).getId());
            if(currentId.equals(senderId)){
                ChatListViewModel temp = list.get(i);
                if(showNotif){
                    int unread = temp.getUnreadCounter();
                    unread++;
                    temp.setMessage(lastReply);
                    temp.setUnreadCounter(unread);
                    temp.setReadStatus(STATE_CHAT_UNREAD);
                }
                list.remove(i);
                notifyItemRemoved(i);
                list.add(0, temp);
                notifyItemInserted(0);
                notifyItemRangeChanged(0, i);
                presenter.moveViewToTop();
                break;
            }
        }
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

        for (ChatListViewModel inboxMessageItem : listMove) {
            if (inboxMessageItem.getReadStatus() == STATE_CHAT_READ)
                statusRead = STATE_CHAT_READ;

            if (inboxMessageItem.getReadStatus() == STATE_CHAT_UNREAD) {
                statusUnread = STATE_CHAT_UNREAD;
            }
        }

        if (statusRead == STATE_CHAT_READ && statusUnread == STATE_CHAT_UNREAD) {
            return STATE_CHAT_BOTH;
        } else if (statusRead == STATE_CHAT_READ) {
            return STATE_CHAT_READ;
        } else if (statusUnread == STATE_CHAT_UNREAD) {
            return STATE_CHAT_UNREAD;
        } else {
            return -1;
        }
    }

    public boolean checkLoadMore(int index) {
        return isLoading() && isLastItemPosition(index);
    }
}