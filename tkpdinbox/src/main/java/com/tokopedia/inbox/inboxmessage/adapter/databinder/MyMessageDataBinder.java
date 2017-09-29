package com.tokopedia.inbox.inboxmessage.adapter.databinder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 5/19/16.
 */
public class MyMessageDataBinder extends DataBinder<MyMessageDataBinder.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.hour)
        TextView hour;

        @BindView(R2.id.date)
        TextView date;

        @BindView(R2.id.main)
        View main;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            message.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem actionCopy = menu.add(v.getId(), R.id.action_copy, 99, R.string.menu_copy);
                    actionCopy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == R.id.action_copy) {
                                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", message.getText());
                                clipboard.setPrimaryClip(clip);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }
            });
        }

    }

    ArrayList<ListReply> list;
    Context context;
    SimpleDateFormat sdf;
    Locale id;
    int canLoadMore = 0;

    public MyMessageDataBinder(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        this.list = new ArrayList<>();
        this.context = context;
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item_mine, parent, false));
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMsg());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());
        if (list.get(position).getReplyTime() == null) {
            holder.hour.setText(context.getString(R.string.title_sending));
        } else {

            holder.date.setVisibility(View.VISIBLE);

            long myTime = Long.parseLong(list.get(position).getReplyTime());

            String time = DateFormat.getLongDateFormat(context).format(new Date(myTime));
            holder.date.setText(time);

            if (position != 0) {
                long prevTime = Long.parseLong(list.get(position-1).getReplyTime());
                Calendar time1 = ChatTimeConverter.unixToCalendar(myTime);
                Calendar calBefore = ChatTimeConverter.unixToCalendar(prevTime);
                if(compareTime(time1, calBefore)){
                    holder.date.setVisibility(View.GONE);
                }
            }
//            String hour = DateFormat.format("hh:mm", myTime).toString();
            String hour = ChatTimeConverter.formatTime(Long.parseLong(list.get(position).getReplyTime()));
            holder.hour.setText(hour);
        }
    }

    private boolean compareTime(Calendar calCurrent, Calendar calBefore) {
        return calCurrent.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                && calCurrent.get(Calendar.MONTH) == calBefore.get(Calendar.MONTH)
                && calCurrent.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addReply(InboxMessageDetailItem list) {
//        this.list.add(list);
        notifyDataSetChanged();
    }

    public void addReply(ListReply item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<ListReply> list) {
        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    public void add(int position, InboxMessageDetailItem inboxMessageDetailItem) {
//        this.list.add(position, inboxMessageDetailItem);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
    }


    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
