package com.tokopedia.seller.product.manage.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.tkpd.library.utils.CommonUtils;
/**
 * Created by yoshua on 16/05/18.
 */
public class AddSticker {
    private Bitmap logo;
    private String name;
    private String price;
    public Bitmap getLogo() {
        return logo;
    }
    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void recycleBitmap(Bitmap bitmap){
        if(bitmap!=null){
            bitmap.recycle();
        }
    }
    public int dpToPx(float dp, Context context) {
        return CommonUtils.convertDpToPixel(dp, context);
    }
    public Paint getTextPaint(Paint paint, Context context, int textSize){
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(dpToPx(textSize, context));
        paint.setStrokeWidth(dpToPx(1, context));
        paint.setShadowLayer(dpToPx(5, context), dpToPx(2, context), dpToPx(2, context), Color.BLACK);
        return paint;
    }

    public void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int x, int y){
        canvas.drawBitmap(bitmap, x, y, paint);
    }
    public void drawText(Canvas canvas, Rect bounds, String text, Paint paint, int x, int y){
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, x, y, paint);
    }
    public void drawLogo(Context context, Canvas canvas, Paint paint){
        int xCoord = dpToPx(50, context);
        int yCoord = dpToPx(50, context);
        drawBitmap(canvas, getLogo(), paint, xCoord, yCoord);
    }
    public void drawNameText(Context context, Canvas canvas, Rect bounds, Paint paint, int sourceWidth, int sourceHeight){
        Paint paintText = getTextPaint(paint, context, 40);
        paintText.getTextBounds(getName(), 0, getName().length(), bounds);
        int xCoord = sourceWidth - bounds.width() - dpToPx(50, context);
        int yCoord = sourceHeight - dpToPx(80, context);
        drawText(canvas, bounds, getName(), paintText, xCoord, yCoord);
    }
    public void drawPriceText(Context context, Canvas canvas, Rect bounds, Paint paint, int sourceWidth, int sourceHeight){
        Paint paintText = getTextPaint(paint, context, 20);
        paintText.getTextBounds(getPrice(), 0, getPrice().length(), bounds);
        int xCoord = sourceWidth - bounds.width() - dpToPx(50, context);
        int yCoord = sourceHeight - dpToPx(50, context);
        drawText(canvas, bounds, getPrice(), paintText, xCoord, yCoord);
    }
    public Bitmap addStickerToBitmap(Bitmap source,Context context){
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        Canvas canvas = new Canvas(source);
        Paint paint = new Paint();
        Rect bounds = new Rect();
        drawLogo(context, canvas, paint);
        drawNameText(context, canvas, bounds, paint, sourceWidth, sourceHeight);
        drawPriceText(context, canvas, bounds, paint, sourceWidth, sourceHeight);
        recycleBitmap(getLogo());
        return source;
    }
    public static class Builder {
        private Bitmap logo;
        private String name;
        private String price;
        public Builder setLogo(Bitmap logo) {
            this.logo = logo;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }
        public AddSticker build() {
            AddSticker addSticker = new AddSticker();
            addSticker.setName(name);
            addSticker.setPrice(price);
            addSticker.setLogo(logo);
            return addSticker;
        }
    }
}