package com.tokopedia.seller.opportunity.snapshot.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ReturnInfo;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/2/17.
 */
public class FreeReturnView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    LinearLayout layoutFreeReturn;
    ImageView imageFreeReturn;
    TextView textFreeReturn;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        layoutFreeReturn = (LinearLayout) findViewById(R.id.layout_free_return);
        imageFreeReturn = (ImageView) findViewById(R.id.image_free_return);
        textFreeReturn = (TextView) findViewById(R.id.text_free_return);
    }

    public FreeReturnView(Context context) {
        super(context);
    }

    public FreeReturnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_free_return_product_snapshot;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        ReturnInfo returnInfo = data.getInfo().getReturnInfo();
        if (returnInfo.getContent().equals("")) {
            setVisibility(GONE);
        } else {
            layoutFreeReturn.setBackgroundColor(Color.parseColor(returnInfo.getColorHex()));
            textFreeReturn.setText(MethodChecker.fromHtml(returnInfo.getContent()));
            textFreeReturn.setMovementMethod(new SelectableSpannedMovementMethod());

            Spannable sp = (Spannable) textFreeReturn.getText();
            URLSpan[] urls = sp.getSpans(0, textFreeReturn.getText().length(), URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(textFreeReturn.getText());
            for (final URLSpan url : urls) {
                style.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = HomeRouter.getBannerWebviewActivity(
                                getContext(), url.getURL()
                        );

                        getContext().startActivity(intent);
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textFreeReturn.setText(style);

            ImageHandler.LoadImage(imageFreeReturn, returnInfo.getIcon());
            setVisibility(VISIBLE);
        }
    }
}
