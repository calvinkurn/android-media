package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class EmptyChatListViewHolder extends AbstractViewHolder<EmptyChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.empty_chat_layout;
    private final InboxChatContract.View viewListener;

    Context context;
    ImageView logo;
    TextView title;
    TextView subtitle;

    public EmptyChatListViewHolder(View itemView, Context context, InboxChatContract.View viewListener) {
        super(itemView);
        this.context = context;
        this.viewListener = viewListener;

        logo = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(EmptyChatModel element) {
        if (element.getType() != EmptyChatModel.SEARCH) {
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_chat));
            title.setText(context.getString(R.string.no_existing_chat));
            subtitle.setVisibility(View.VISIBLE);

            if (element.isHasTimeMachine()) {
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

                subtitle.setText(spannable, TextView.BufferType.SPANNABLE);
                subtitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.onGoToTimeMachine(TkpdBaseURL.User.URL_INBOX_MESSAGE_TIME_MACHINE);
                    }
                });
            } else {
                subtitle.setText(context.getString(R.string.please_try_chat));
            }
        } else {
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_search));
            subtitle.setText(context.getString(R.string.no_existing_chat_user));
            title.setVisibility(View.GONE);

        }
    }
}
