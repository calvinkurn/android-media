package com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core2.R2;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;

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

public class MyDiscussionDataBinder extends DataBinder<MyDiscussionDataBinder.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.hour)
        TextView hour;

        @BindView(R2.id.date)
        TextView date;

        TextView titleAttachment;

        @BindView(R2.id.image_holder)
        RecyclerView imageHolder;

        AttachmentAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            titleAttachment = (TextView) itemView.findViewById(R.id.title_attachment);
        }
    }

    private ArrayList<DiscussionItemViewModel> list;
    private Context context;
    private SimpleDateFormat sdf;

    public MyDiscussionDataBinder(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        this.list = new ArrayList<>();
        this.context = context;
        Locale id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat(DiscussionItemViewModel.DISCUSSION_DATE_TIME_FORMAT, id);
    }


    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_my_res_center_discussion, parent, false));
        holder.adapter = AttachmentAdapter.createAdapter(context, false);
        holder.adapter.setListener(onProductImageActionListener(holder.adapter.getList()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.imageHolder.setLayoutManager(layoutManager);
        holder.imageHolder.setAdapter(holder.adapter);

        return holder;
    }

    private AttachmentAdapter.ProductImageListener onProductImageActionListener(final ArrayList<AttachmentViewModel> list) {
        return new AttachmentAdapter.ProductImageListener() {

            @Override
            public View.OnClickListener onImageClicked(final int position, final AttachmentViewModel imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imageUpload.isVideo()) {
                            openVideoPlayer(imageUpload.getUrl());
                        } else {
                            openProductPreview(list, position);
                        }
                    }
                };
            }

            @Override
            public View.OnClickListener onDeleteImage(int position, AttachmentViewModel imageUpload) {
                return null;
            }
        };
    }

    private void openProductPreview(ArrayList<AttachmentViewModel> list, int position) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (AttachmentViewModel model : list) {
            imageUrls.add(model.getImgLarge());
        }
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void openVideoPlayer(String urlVideo) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerActivity.PARAMS_URL_VIDEO, urlVideo);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessage());
        holder.message.setMovementMethod(new SelectableSpannedMovementMethod());
        setImage(holder, list.get(position));
        if (list.get(position).getMessageReplyTimeFmt() == null) {
            holder.hour.setText(context.getString(R.string.title_sending));
            holder.date.setVisibility(View.GONE);
        } else {
            try {

                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(list.get(position).getMessageReplyTimeFmt()));

                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(list.get(position).getMessageReplyDateFmt());
                if (position != 0) {
                    Calendar calBefore = Calendar.getInstance();
                    calBefore.setTime(sdf.parse(list.get(position - 1).getMessageReplyTimeFmt()));

                    if (cal.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                            && cal.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR)) {
                        holder.date.setVisibility(View.GONE);
                    }
                }

                holder.hour.setText(list.get(position).getMessageReplyHourFmt());
            } catch (ParseException e) {
                holder.date.setText("");
            }
        }
    }

    private void setImage(ViewHolder holder, DiscussionItemViewModel discussionItemViewModel) {
        if (isHasAttachment(discussionItemViewModel)) {
            holder.imageHolder.setVisibility(View.VISIBLE);
            holder.adapter.addList(discussionItemViewModel.getAttachment());
            holder.titleAttachment.setVisibility(View.VISIBLE);
        } else {
            holder.imageHolder.setVisibility(View.GONE);
            holder.titleAttachment.setVisibility(View.GONE);
        }
    }

    private boolean isHasAttachment(DiscussionItemViewModel discussionItemViewModel) {
        return discussionItemViewModel.getAttachment() != null
                && discussionItemViewModel.getAttachment().size() > 0;
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


}
