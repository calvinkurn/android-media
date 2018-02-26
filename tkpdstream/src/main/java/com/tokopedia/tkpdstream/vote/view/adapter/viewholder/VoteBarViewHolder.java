package com.tokopedia.tkpdstream.vote.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteBarViewHolder extends AbstractViewHolder<VoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_option_bar;
    ProgressBar progressBar;
    TextView option;
    TextView percent;

    public VoteBarViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.text_view);
        percent = itemView.findViewById(R.id.percent);
    }

    @Override
    public void bind(VoteViewModel element) {
        progressBar.setProgress(element.getPercentage());
        option.setText(element.getOption());
        percent.setText(String.format("%d%%", element.getPercentage()));
    }
}
