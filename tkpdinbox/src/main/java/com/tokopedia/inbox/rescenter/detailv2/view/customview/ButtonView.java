package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ButtonViewAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;

/**
 * Created by hangnadi on 3/8/17.
 */
public class ButtonView extends BaseView<ButtonData, DetailResCenterFragmentView> {

    private ButtonData buttonData;
    private RecyclerView rvButton;
    private ButtonViewAdapter adapter;
    private GridLayoutManager mLayoutManager;
    private Context context;

    public ButtonView(Context context) {
        super(context);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_button_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);

        rvButton = (RecyclerView) view.findViewById(R.id.rv_button);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ButtonData data) {
        setVisibility(VISIBLE);
        setButtonData(data);
        adapter = new ButtonViewAdapter(context, listener, data.getResolutionStatus());
        rvButton.setAdapter(adapter);
        mLayoutManager = new GridLayoutManager(
                context,
                adapter.getAdapterSpanCount(),
                LinearLayoutManager.VERTICAL,
                false);
        mLayoutManager.setSpanSizeLookup(adapter.getSpanItem());
        rvButton.setLayoutManager(mLayoutManager);
        adapter.setButtonViewItemList(data.getButtonViewItemList());
        listener.setOnDiscussionButtonPosition(adapter.getItemCount() != 0 );
    }

    public ButtonData getButtonData() {
        return buttonData;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }
}
