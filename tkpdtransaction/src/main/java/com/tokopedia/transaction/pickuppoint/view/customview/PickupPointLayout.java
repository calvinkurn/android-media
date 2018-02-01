package com.tokopedia.transaction.pickuppoint.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 03/01/18.
 */

public class PickupPointLayout extends LinearLayout {

    @BindView(R2.id.tv_send_to_pick_up_booth)
    TextView tvSendToPickUpBooth;
    @BindView(R2.id.tv_pick_up_booth_name)
    TextView tvPickUpBoothName;
    @BindView(R2.id.btn_cancel_pick_up)
    ImageButton btnCancelPickUp;
    @BindView(R2.id.tv_pick_up_booth_address)
    TextView tvPickUpBoothAddress;
    @BindView(R2.id.tv_edit_pick_up_booth)
    TextView tvEditPickUpBooth;
    @BindView(R2.id.ll_content)
    LinearLayout llContent;

    private ViewListener listener;
    private Store store;

    public PickupPointLayout(Context context) {
        super(context);
        initView(context);
    }

    public PickupPointLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PickupPointLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_pickup_point, this, true);
        ButterKnife.bind(view);

        btnCancelPickUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && store != null) {
                    listener.onClearPickupPoint(store);
                }
            }
        });

        tvEditPickUpBooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && store != null) {
                    listener.onEditPickupPoint(store);
                }
            }
        });

        tvSendToPickUpBooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChoosePickupPoint();
                }
            }
        });
    }

    public void enableChooserButton(Context context) {
        tvSendToPickUpBooth.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green));
        tvSendToPickUpBooth.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(context, R.drawable.ic_chevron_right), null);
        tvSendToPickUpBooth.setVisibility(VISIBLE);
    }

    public void disableChooserButton(Context context) {
        tvSendToPickUpBooth.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvSendToPickUpBooth.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tvSendToPickUpBooth.setVisibility(VISIBLE);
    }

    public void hideChooserButton() {
        tvSendToPickUpBooth.setVisibility(GONE);
    }

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void setData(Context context, Store store) {
        this.store = store;
        tvPickUpBoothName.setText(store.getStoreName());
        tvPickUpBoothAddress.setText(store.getAddress());
        disableChooserButton(context);
        tvSendToPickUpBooth.setOnClickListener(null);
        llContent.setVisibility(VISIBLE);
    }

    public void unSetData(Context context) {
        enableChooserButton(context);
        store = null;
        tvSendToPickUpBooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChoosePickupPoint();
                }
            }
        });
        llContent.setVisibility(GONE);
    }

    public interface ViewListener {
        void onChoosePickupPoint();

        void onClearPickupPoint(Store oldStore);

        void onEditPickupPoint(Store oldStore);
    }

}
