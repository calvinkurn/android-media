package com.tokopedia.seller.shopscore.view.recyclerview;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.view.widget.SquareProgressBar;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailViewHolder extends RecyclerView.ViewHolder {

    private final TextView shopScoreTitle;
    private final TextView shopScoreValue;
    private final SquareProgressBar shopScoreProgressBar;
    private final TextView shopScoreDescription;
    private final Context context;

    public ShopScoreDetailViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        shopScoreTitle = (TextView) itemView.findViewById(R.id.title_shop_score_detail);
        shopScoreValue = (TextView) itemView.findViewById(R.id.description_shop_score_value);
        shopScoreProgressBar = (SquareProgressBar) itemView.findViewById(R.id.progress_bar_shop_score_detail);
        shopScoreDescription = (TextView) itemView.findViewById(R.id.description_shop_score_detail);
    }

    public void setTitle(String title) {
        this.shopScoreTitle.setText(title);
    }

    public void setShopScoreValue(int value) {
        shopScoreValue.setText(String.valueOf(value));
        shopScoreProgressBar.setProgress(value);
    }

    public void setShopScoreDescription(String description) {
        if (description != null) {
            CharSequence sequence = MethodChecker.fromHtml(description);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            shopScoreDescription.setText(strBuilder);
            shopScoreDescription.setMovementMethod(LinkMovementMethod.getInstance());
            shopScoreDescription.setVisibility(View.VISIBLE);
        } else {
            shopScoreDescription.setVisibility(View.GONE);
        }
    }

    private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                if (span.getURL().equals("com.tokopedia.sellerapp")) {
                    startNewActivity(span.getURL());
                } else {
                    openLink(span.getURL());
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void startNewActivity(String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    private void openLink(String url) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setProgressBarColor(Integer progressBarColor) {
        shopScoreProgressBar.setProgressColor(progressBarColor);
    }
}
