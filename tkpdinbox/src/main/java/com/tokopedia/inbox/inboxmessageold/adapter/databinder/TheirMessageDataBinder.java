package com.tokopedia.inbox.inboxmessageold.adapter.databinder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.inbox.inboxmessageold.presenter.InboxMessageDetailFragmentPresenter;
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
public class TheirMessageDataBinder extends DataBinder<TheirMessageDataBinder.ViewHolder>
        implements InboxMessageConstant {

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.user_ava)
        ImageView avatar;

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.hour)
        TextView hour;

        @BindView(R2.id.date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            message.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem actionCopy = contextMenu.add(view.getId(), com.tokopedia.core.R.id.action_copy, 99, com.tokopedia.core.R.string.menu_copy);
                    actionCopy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int i = menuItem.getItemId();
                            if (i == com.tokopedia.core.R.id.action_copy) {
                                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", message.getText());
                                clipboard.setPrimaryClip(clip);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    if (!(nav.equals(MESSAGE_TRASH) || list.get(getAdapterPosition()).getUserLabelId() == 1)) {
                        MenuItem actionReportSpam = contextMenu.add(view.getId(), com.tokopedia.core.R.id.action_report, 99, com.tokopedia.core.R.string.action_report_as_spam);
                        actionReportSpam.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int i = menuItem.getItemId();
                                if (i == com.tokopedia.core.R.id.action_report) {
                                    if (!(nav.equals(MESSAGE_TRASH) || list.get(getAdapterPosition()).getUserLabelId() == 1)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(com.tokopedia.core.R.string.dialog_spam);
                                        builder.setPositiveButton(com.tokopedia.core.R.string.title_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                presenter.flagSpam(getAdapterPosition(), list.get(getAdapterPosition()));
                                            }
                                        });
                                        builder.setNegativeButton(com.tokopedia.core.R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    ArrayList<InboxMessageDetailItem> list;
    Context context;
    InboxMessageDetailFragmentPresenter presenter;
    String nav;
    SimpleDateFormat sdf;
    Locale id;
    int canLoadMore = 0;

    public TheirMessageDataBinder(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        this.list = new ArrayList<>();
        this.context = context;
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(com.tokopedia.core.R.layout.listview_their_message_detail, parent, false));
    }

    @Override
    public void bindViewHolder(ViewHolder holder, final int position) {
        ImageHandler.loadImageCircle2(context, holder.avatar, list.get(position).getUserImage());
        holder.message.setText(list.get(position).getMessageReply());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());

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

        holder.avatar.setOnClickListener(onGoToProfile(list.get(position)));

    }

    private View.OnClickListener onGoToProfile(final InboxMessageDetailItem messageDetailItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageDetailItem.getUserLabelId() != IS_ADMIN)
                presenter.onGoToProfile(String.valueOf(messageDetailItem.getUserId()));
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setPresenter(InboxMessageDetailFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void addAll(List<InboxMessageDetailItem> list) {
        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    public void addReply(InboxMessageDetailItem item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public void add(int position, InboxMessageDetailItem inboxMessageDetailItem) {
        this.list.add(position, inboxMessageDetailItem);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
