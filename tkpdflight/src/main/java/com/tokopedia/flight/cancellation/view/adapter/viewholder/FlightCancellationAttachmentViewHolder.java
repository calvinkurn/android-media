package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.common.view.FlowableTextViewWithImageView;

import java.io.File;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentViewHolder extends AbstractViewHolder<FlightCancellationAttachmentViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_cancellation_attachment;

    private AppCompatImageView ivAttachment;
    private AppCompatTextView tvFilename;
    private AppCompatImageView ivCross;
    private FlightCancellationAttachmentViewModel element;

    private FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener;

    public FlightCancellationAttachmentViewHolder(View itemView, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener) {
        super(itemView);
        setupView(itemView);
        this.interactionListener = interactionListener;
    }

    private void setupView(View view) {
        ivAttachment = (AppCompatImageView) view.findViewById(R.id.iv_attachment);
        tvFilename = (AppCompatTextView) view.findViewById(R.id.tv_filename);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bind(FlightCancellationAttachmentViewModel element) {
        this.element = element;
        tvFilename.setText(element.getFilename());
        tvFilename.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (tvFilename.getRight())) {
                        if (interactionListener != null) {
                            interactionListener.deleteAttachement(FlightCancellationAttachmentViewHolder.this.element);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        if (element.getFilepath() != null)
        Glide.with(itemView.getContext())
                .load(new File(element.getFilepath()))
                .asBitmap()
                .centerCrop()
                .into(getRoundedImageViewTarget(ivAttachment, 5.0f));
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
