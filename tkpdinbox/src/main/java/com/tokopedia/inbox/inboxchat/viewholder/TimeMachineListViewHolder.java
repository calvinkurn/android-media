package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineModel;

/**
 * @author by nisie on 10/25/17.
 */

public class TimeMachineListViewHolder extends AbstractViewHolder<TimeMachineModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.time_machine_inbox_layout;

    protected TextView timeMachineText;

    public TimeMachineListViewHolder(View itemView) {
        super(itemView);
        timeMachineText = (TextView) itemView.findViewById(R.id.time_machine_text);

        Spannable spannable = new SpannableString(MainApplication.getAppContext().getString(R.string
                .time_machine));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(MethodChecker.getColor(MainApplication
                                          .getAppContext(), R.color.medium_green));
                              }
                          }
                , MainApplication.getAppContext().getString(R.string.time_machine).indexOf
                        ("Riwayat")
                , MainApplication.getAppContext().getString(R.string.time_machine).length()
                , 0);

        timeMachineText.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void bind(TimeMachineModel element) {

    }
}
