package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentAttributes;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentProductProfile;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

import java.util.Date;

/**
 * Created by Hendri on 20/02/18.
 */

public class AttachedProductViewHolder extends AbstractViewHolder<AttachProductViewModel> {

    View view;
    View progressBarSendImage;
    TextView hour;
    TextView date;
    ImageView chatStatus;
    private TextView name;
    private TextView label;
    private TextView dot;
    private ImageView thumbnailsImage;
    private ImageView action;
    ChatRoomContract.View viewListener;
    private String ROLE_USER = "User";
    private Context context;
    private Integer productId;
    private String productName;
    private String productPrice;
    private long dateTimeInMilis;
    @LayoutRes
    public static final int LAYOUT = R.layout.attached_product_chat_item;

    public AttachedProductViewHolder(View itemView, final ChatRoomContract.View viewListener) {
        super(itemView);
        this.context = itemView.getContext();
        view = itemView;
//        message = itemView.findViewById(R.id.message);
        hour = itemView.findViewById(R.id.hour);
        date = itemView.findViewById(R.id.date);
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
//        attachment = itemView.findViewById(R.id.image);
        action = itemView.findViewById(R.id.left_action);
        progressBarSendImage = itemView.findViewById(R.id.progress_bar);
//        attachmentChatHelper = new AttachmentChatHelper();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(AttachProductViewModel element) {
        prerequisiteUISetup(element);
        Attachment attachmentModel = element.getAttachment();
        if (attachmentModel != null && attachmentModel.getType().equals(AttachmentChatHelper.PRODUCT_ATTACHED)) {
            View productContainerView = itemView.findViewById(R.id.attach_product_chat_container);
            if(element.isSender()) {
                productContainerView.setBackground(context.getResources().getDrawable(R.drawable.attach_product_right_bubble));
                setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT,productContainerView);
                chatStatus.setVisibility(View.VISIBLE);
            }
            else {
                productContainerView.setBackground(context.getResources().getDrawable(R.drawable.attach_product_left_bubble));
                setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT,productContainerView);
                chatStatus.setVisibility(View.GONE);
            }

            setupProductUI(attachmentModel.getAttributes(),productContainerView);
            if(element.getReplyTime() != null && element.getReplyTime().trim().length() > 0 && !element.isDummy()) {
                this.dateTimeInMilis = Long.parseLong(element.getReplyTime());
            }
        }
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
        params.addRule(alignment);
        view.setLayoutParams(params);
    }

    private void setupProductUI(AttachmentAttributes attachmentAttributes, View productContainer){
        AttachmentProductProfile productProfile = attachmentAttributes.getProductProfile();
        if(productProfile != null){
            this.productId = attachmentAttributes.getProductId();
            this.productName = productProfile.getName();
            this.productPrice = productProfile.getPrice();
            setUIValue(productContainer, R.id.attach_product_chat_image,productProfile.getImageUrl());
            setUIValue(productContainer,R.id.attach_product_chat_name,productProfile.getName());
            setUIValue(productContainer,R.id.attach_product_chat_price,productProfile.getPrice());
        }
    }

    private void setUIValue(View productContainer, int id, String value){
        View destination = productContainer.findViewById(id);
        if(destination instanceof TextView)
            ((TextView)destination).setText(value);
        else if(destination instanceof ImageView) {
//            Glide.with(view.getContext())
//                    .load(value)
//                    .asBitmap()
//                    .dontAnimate()
//                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
//                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
//                    .into(getRoundedImageViewTarget((ImageView)destination,12.0f));
//            ImageHandler.loadImageRounded2Target();
            ImageHandler.loadImageRounded2(destination.getContext(),(ImageView) destination,value);
//            ImageHandler.loadImageAndCache(, value);
            this.thumbnailsImage = (ImageView) destination;
        }
    }

    protected void prerequisiteUISetup(final AttachProductViewModel element){
        action.setVisibility(View.GONE);
        progressBarSendImage.setVisibility(View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(itemView.getContext(), view);
                if(productId != null && productName != null && productPrice != null){
                    viewListener.productClicked(productId,productName,productPrice,dateTimeInMilis);
                }
            }
        });

        date.setVisibility(View.VISIBLE);
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            time = DateFormat.getLongDateFormat(itemView.getContext()).format(new Date(myTime));
            date.setText(time);
            date.setVisibility(View.VISIBLE);
        }catch (NumberFormatException e){
            time = element.getReplyTime();
            date.setVisibility(View.GONE);
        }


        if (element.isShowTime()) {
            date.setVisibility(View.VISIBLE);
        }else {
            date.setVisibility(View.GONE);
        }

        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        }catch (NumberFormatException e){
            hourTime = element.getReplyTime();
        }

        if (element.isShowHour()) {
            hour.setVisibility(View.VISIBLE);
            chatStatus.setVisibility(View.VISIBLE);
        }else {
            hour.setVisibility(View.GONE);
            chatStatus.setVisibility(View.GONE);
        }

        hour.setText(hourTime);

        int imageResource;

        if(element.isReadStatus()){
            imageResource = R.drawable.ic_chat_read;
        }else {
            imageResource = R.drawable.ic_chat_unread;
        }
        if(element.isDummy()){
            imageResource = R.drawable.ic_chat_pending;
        }

        chatStatus.setImageResource(imageResource);

        if(element.getRole()!=null){
            if(element.getRole().toLowerCase().equals(ROLE_USER.toLowerCase())){
                name.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
                dot.setVisibility(View.GONE);
            }else{
                name.setText(element.getSenderName());
                label.setText(element.getRole());
                name.setVisibility(View.VISIBLE);
                dot.setVisibility(View.VISIBLE);
                label.setVisibility(View.VISIBLE);
            }
        }else {
            name.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
        }
    }

    private SpannableString highlight(Context context, Spanned span, String keyword) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(span);

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(MethodChecker.getColor(context,R.color.orange_300)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }

    public void onViewRecycled() {
          Glide.clear(thumbnailsImage);
    }


    private static BitmapImageViewTarget getRoundedImageViewTarget(final ImageView imageView, final float radius) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(radius);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }
}
