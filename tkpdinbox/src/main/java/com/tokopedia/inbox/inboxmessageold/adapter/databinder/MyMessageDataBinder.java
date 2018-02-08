package com.tokopedia.inbox.inboxmessageold.adapter.databinder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R2;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                    MenuItem actionCopy = menu.add(v.getId(), com.tokopedia.core.R.id.action_copy, 99, com.tokopedia.core.R.string.menu_copy);
                    actionCopy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int i = item.getItemId();
                            if (i == com.tokopedia.core.R.id.action_copy) {
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

    ArrayList<InboxMessageDetailItem> list;
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
                .inflate(com.tokopedia.core.R.layout.listview_my_message_detail, parent, false));
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessageReply());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());
        if (list.get(position).getMessageReplyTimeFmt() == null) {
            holder.hour.setText(context.getString(com.tokopedia.core.R.string.title_sending));
            holder.date.setVisibility(View.GONE);
        } else {
            try {
                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(list.get(position).getMessageReplyDateFmt());
                CommonUtils.dumper("NISNIS Message " + position + " " + list.get(position).getMessageReply().toString() + " "
                        + canLoadMore);
                if (position != 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(list.get(position).getMessageReplyTimeFmt()));
                    Calendar calBefore = Calendar.getInstance();
                    calBefore.setTime(sdf.parse(list.get(position - 1).getMessageReplyTimeFmt()));
                    if (cal.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                            && cal.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR)) {
                        holder.date.setVisibility(View.GONE);
                    }
                }

            } catch (ParseException e) {
                holder.date.setText("");
            }

            holder.hour.setText(list.get(position).getMessageReplyHourFmt());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addReply(InboxMessageDetailItem list) {
        this.list.add(list);
        notifyDataSetChanged();
    }

    public void addAll(List<InboxMessageDetailItem> list) {
        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    public void add(int position, InboxMessageDetailItem inboxMessageDetailItem) {
        this.list.add(position, inboxMessageDetailItem);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
    }


    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
