package com.tokopedia.transaction.orders.orderlist.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;

public class DoubleTextView extends LinearLayout {
   LinearLayout layout = null;
   TextView topTextView = null;
   TextView bottomTextView = null;
   Context mContext = null;

   public DoubleTextView(Context context) {
      super(context);
      mContext = context;
   }

   public DoubleTextView(Context context, AttributeSet attrs) {
      super(context, attrs);

      mContext = context;

      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DoubleText);

      String topText = a.getString(R.styleable.DoubleText_topText);
      String bottomText = a.getString(R.styleable.DoubleText_bottomText);

      topText = topText == null ? "" : topText;
      bottomText = bottomText == null ? "" : bottomText;

      String service = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);

      layout = (LinearLayout) li.inflate(R.layout.custom_text_view_layout, this, true);

      topTextView = (TextView) layout.findViewById(R.id.top_text);
      bottomTextView = (TextView) layout.findViewById(R.id.bottom_text);

      topTextView.setText(topText);
      bottomTextView.setText(bottomText);
      a.recycle();
   }

   public DoubleTextView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
   }

   @SuppressWarnings("unused")
   public void setTopText(String text) {
      topTextView.setText(text);
   }

   @SuppressWarnings("unused")
   public void setBottomText(String text) {
      bottomTextView.setText(text);
   }

   @SuppressWarnings("unused")
   public String getTopText() {
      return topTextView.getText().toString();
   }

   @SuppressWarnings("unused")
   public String getBottomText() {
      return bottomTextView.getText().toString();
   }


}