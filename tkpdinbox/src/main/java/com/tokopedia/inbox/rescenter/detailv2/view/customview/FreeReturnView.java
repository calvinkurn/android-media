package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.createreso.view.activity.FreeReturnActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.FreeReturnData;

/**
 * Created by yfsx on 14/12/17.
 */

public class FreeReturnView extends BaseView<FreeReturnData, DetailResCenterFragmentView> {

    TextView tvFreeReturn;

    public FreeReturnView(Context context) {
        super(context);
    }

    public FreeReturnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        tvFreeReturn = (TextView) view.findViewById(R.id.tv_free_return);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_freereturn_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull FreeReturnData data) {
        setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(data.getFreeReturnText())) {
            tvFreeReturn.setText(MethodChecker.fromHtml(data.getFreeReturnText()));
            tvFreeReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.setOnFreeReturnClicked();
                }
            });
        } else setVisibility(GONE);
    }
}
