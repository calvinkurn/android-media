package com.tokopedia.flight.booking.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

/**
 * Created by alvarisi on 11/6/17.
 */

public class CardWithActionView extends BaseCustomView {

    private AppCompatTextView contentInfoAppCompatTextView;
    private AppCompatTextView contentAppCompatTextView;
    private AppCompatTextView subContentAppCompatTextView;
    private AppCompatTextView subContentInfoAppCompatTextView;
    private AppCompatTextView actionAppCompatTextView;
    private RelativeLayout actionLayout;

    private ActionListener actionListener;

    public interface ActionListener {
        void actionClicked();
    }

    public CardWithActionView(@NonNull Context context) {
        super(context);
        init();
    }


    public CardWithActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CardWithActionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.CardWithActionView);
        try {
            String content = styledAttributes.getString(R.styleable.CardWithActionView_cmv_content);
            String contentInfo = styledAttributes.getString(R.styleable.CardWithActionView_cmv_content_info);
            String subContent = styledAttributes.getString(R.styleable.CardWithActionView_cmw_subcontent);
            String subContentInfo = styledAttributes.getString(R.styleable.CardWithActionView_cmw_subcontent_info);
            String actionLabel = styledAttributes.getString(R.styleable.CardWithActionView_cmw_action);
            if (!TextUtils.isEmpty(content)) {
                contentAppCompatTextView.setText(String.valueOf(content));
            }
            if (!TextUtils.isEmpty(subContent)) {
                subContentAppCompatTextView.setText(String.valueOf(subContent));
            }
            if (!TextUtils.isEmpty(contentInfo)) {
                contentInfoAppCompatTextView.setText(String.valueOf(contentInfo));
            }
            if (!TextUtils.isEmpty(subContentInfo)) {
                subContentInfoAppCompatTextView.setText(String.valueOf(subContent));
            }
            if (!TextUtils.isEmpty(actionLabel)) {
                actionAppCompatTextView.setText(String.valueOf(actionLabel));
            }
        } finally {
            styledAttributes.recycle();
        }
    }


    private void init() {
        View view = inflate(getContext(), R.layout.widget_card_with_action, this);
        contentAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_content_message);
        contentInfoAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_content_info);
        subContentAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_subcontent_message);
        subContentInfoAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_subcontent_info);
        actionAppCompatTextView = (AppCompatTextView) view.findViewById(R.id.tv_action);
        actionLayout = (RelativeLayout) view.findViewById(R.id.action_layout);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        actionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.actionClicked();
                }
            }
        });
    }

    public void setActionListener(ActionListener listener) {
        actionListener = listener;
    }

    public void setContent(String content) {
        contentAppCompatTextView.setText(content);
    }

    public void setSubContent(String subContent) {
        subContentAppCompatTextView.setText(String.valueOf(subContent));
    }

    public void setContentInfo(String contentInfo) {
        contentInfoAppCompatTextView.setText(contentInfo);
    }

    public void setSubContentInfo(String subContentInfo) {
        subContentInfoAppCompatTextView.setText(subContentInfo);
    }

    public void setAction(String action) {
        actionAppCompatTextView.setText(action);
    }
}
