package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.ticker.SelectableSpannedMovementMethod;
import com.tokopedia.discovery.R;

/**
 * Created by hangnadi on 10/9/17.
 */

public class HotlistViewPagerAdapter extends PagerAdapter {

    private final ItemClickListener mItemClickListener;
    private Context context;
    private String descText;

    public HotlistViewPagerAdapter(Context context, String desc, ItemClickListener mItemClickListener) {
        this.context=context;
        this.descText = desc;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_hotlist_banner_desc, container, false);
        TextView descTextView = (TextView) layout.findViewById(R.id.textview);


        if (position == 0) {
            descTextView.setBackgroundColor(0x00000000);
            descTextView.setText(null);

        } else if (position == 1) {
            if (descText != null) {
                descTextView.setBackgroundColor(0xB3000000);
                descTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
                descTextView.setMovementMethod(new SelectableSpannedMovementMethod());

                Spannable sp = (Spannable) fromHtml(descText);
                URLSpan[] urls = sp.getSpans(0, fromHtml(descText).length(), URLSpan.class);
                SpannableStringBuilder style = new SpannableStringBuilder(fromHtml(descText));
                style.clearSpans();

                for(URLSpan url : urls){
                    final String messageClickAble = url.getURL();
                    style.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            mItemClickListener.onHotlistDescClicked(messageClickAble);
                        }
                    }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                descTextView.setText(style);
                descTextView.setText(MethodChecker.fromHtml(descText));
            }
        }

        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(layoutParams);
        layout.invalidate();
        layout.requestLayout();

        container.addView(layout);
        return layout;
    }

    private Spanned fromHtml(String text) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null && object instanceof View) container.removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
