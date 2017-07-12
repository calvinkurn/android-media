package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 7/10/17.
 */

public class GMSeeDetailViewHelper extends BaseGMViewHelper {
    private TextView seeDetailText;
    private ImageView seeDetailImage;
    private RelativeLayout seeDetailContainer;

    public GMSeeDetailViewHelper(Context context) {
        super(context);

    }

    @Override
    public void initView(View itemView) {
        seeDetailContainer = (RelativeLayout) itemView.findViewById(R.id.see_detail_container);
        seeDetailText = (TextView) itemView.findViewById(R.id.see_detail_text);
        seeDetailImage = (ImageView) itemView.findViewById(R.id.see_detail_image);
    }

    @Override
    public void bind(Object data) {

        if (data != null && data instanceof View.OnClickListener) {
            seeDetailContainer.setOnClickListener((View.OnClickListener) data);
        }

        seeDetailText.setText(R.string.see_detail_gm_description);
        setImageIcon();
    }

    protected void setImageIcon() {
        Drawable setDateNext = AppCompatDrawableManager.get().getDrawable(context
                , R.drawable.ic_set_date_next);
        seeDetailImage.setImageDrawable(setDateNext);
    }
}
