package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;

/**
 * Created by alvarisi on 12/13/17.
 */

public abstract class FlightOrderBaseViewHolder<T> extends AbstractViewHolder<T> {
    private AppCompatImageView ivOverflow;

    public FlightOrderBaseViewHolder(View itemView) {
        super(itemView);
        initViewListener(itemView);
    }

    private void initViewListener(View itemView) {
        ivOverflow = (AppCompatImageView) itemView.findViewById(R.id.iv_overflow);
        ivOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    protected CharSequence getAirportTextForView(String airportId, String cityCode, String cityName) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        if (TextUtils.isEmpty(airportId)) {
            // id is more than one
            if (TextUtils.isEmpty(cityCode)) {
                text.append(cityName);
                return makeBold(itemView.getContext(), text);
            } else {
                text.append(cityCode);
            }
        } else {
            text.append(airportId);
        }
        makeBold(itemView.getContext(), text);
        if (!TextUtils.isEmpty(cityName)) {
            SpannableStringBuilder cityNameText = new SpannableStringBuilder(cityName);
            makeSmall(cityNameText);
            text.append("\n");
            text.append(cityNameText);
        }
        return text;
    }

    private SpannableStringBuilder makeSmall(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new RelativeSizeSpan(0.75f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private SpannableStringBuilder makeBold(Context context, SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.25f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)),
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    protected void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_flight_order, popup.getMenu());
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked());
        popup.show();
    }

    protected abstract void onHelpOptionClicked();

    protected abstract void onDetailOptionClicked();

    private class OnMenuPopupClicked implements PopupMenu.OnMenuItemClickListener {

        OnMenuPopupClicked() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_detail) {
                onDetailOptionClicked();
                return true;
            } else if (item.getItemId() == R.id.action_help) {
                onHelpOptionClicked();
                return true;
            } else {
                return false;
            }
        }
    }
}
