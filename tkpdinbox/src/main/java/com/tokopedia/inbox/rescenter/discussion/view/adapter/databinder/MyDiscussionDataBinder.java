package com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder;

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

import com.tokopedia.core.R2;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 3/29/17.
 */

public class MyDiscussionDataBinder extends DataBinder<MyDiscussionDataBinder.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.hour)
        TextView hour;

        @BindView(R2.id.date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu,
                                        View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem actionCopy = contextMenu.add(view.getId(), R.id.action_copy, 99, R.string.menu_copy);
            actionCopy.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.action_copy) {
                ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", message.getText());
                clipboard.setPrimaryClip(clip);
                return true;
            } else {
                return false;
            }
        }
    }

    ArrayList<DiscussionItemViewModel> list;
    Context context;
    SimpleDateFormat sdf;
    Locale id;
    int canLoadMore = 0;

    public MyDiscussionDataBinder(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        this.list = new ArrayList<>();
        this.context = context;
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat(DiscussionItemViewModel.DISCUSSION_DATE_TIME_FORMAT, id);
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_my_res_center_discussion, parent, false));
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessage());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());
        if (list.get(position).getMessageReplyTimeFmt() == null) {
            holder.hour.setText(context.getString(R.string.title_sending));
            holder.date.setVisibility(View.GONE);
        } else {
            try {

                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(list.get(position).getMessageReplyTimeFmt()));

                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(list.get(position).getMessageReplyTimeFmt());
                if (position != 0) {
                    Calendar calBefore = Calendar.getInstance();
                    calBefore.setTime(sdf.parse(list.get(position - 1).getMessageReplyTimeFmt()));

                    if (cal.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                            && cal.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR)) {
                        holder.date.setVisibility(View.GONE);
                    }
                }


                holder.hour.setText(list.get(position).getMessageReplyHourFmt());
                if (position != list.size() - 1) {
                    Calendar calAfter = Calendar.getInstance();
                    calAfter.setTime(sdf.parse(list.get(position + 1).getMessageReplyTimeFmt()));

                    if (cal.get(Calendar.HOUR) == calAfter.get(Calendar.HOUR)
                            && cal.get(Calendar.MINUTE) == calAfter.get(Calendar.MINUTE)) {
                        holder.date.setVisibility(View.GONE);
                    }
                }
            } catch (ParseException e) {
                holder.date.setText("");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addReply(DiscussionItemViewModel list) {
        this.list.add(list);
        notifyDataSetChanged();
    }

    public void addAll(List<DiscussionItemViewModel> list) {
        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    public void add(int position, DiscussionItemViewModel DiscussionItemViewModel) {
        this.list.add(position, DiscussionItemViewModel);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
    }


    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
