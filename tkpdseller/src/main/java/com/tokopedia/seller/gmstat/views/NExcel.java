package com.tokopedia.seller.gmstat.views;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by normansyahputa on 10/5/16.
 */

public class NExcel {
    private float mWidth;//column width
    private float mHeight;//ploy line y
    private PointF mStart = new PointF();//Rectangle The lower left corner of the starting point
    private float mMidX;//The midpoint of the broken line x
    private int mColor;
    private float mNum; //The current number
    private float mMax; //Total data
    private String textMsg; //The information to display
    private String mXmsg; //Abscissa information
    private float mUpper;
    private float mLower;
    /**
     * unit
     */
    private String unit;

    public NExcel(float num, String mXmsg){
        this(0, num, mXmsg);
    }

    public NExcel(float lower, float upper, String mXmsg){
        this(lower, upper, mXmsg, Color.GRAY);
    }

    public NExcel(float lower, float upper, String mXmsg, int color){
        this(lower, upper, "", mXmsg, color);
    }


    public NExcel(float num, String unit, String mXmsg){
        this(0, num, unit, mXmsg, Color.GRAY);
    }


    // set koordinat y dengan nilai lower
    public NExcel(float lower, float upper, String unit, String mXmsg, int color){
        mUpper = upper;
        mLower = lower;
        mHeight = mNum = upper-lower;
        mStart.y = mLower;
        this.mXmsg = (mXmsg != null && !mXmsg.isEmpty())? mXmsg : mUpper+"";
        this.unit = unit;
        this.mColor = color;
    }

    public RectF getRectF(){
        return new RectF(mStart.x, mStart.y-mHeight, mStart.x+mWidth, mStart.y);
    }

    public PointF getMidPointF(){
        return new PointF(getMidX(), mStart.y-mHeight);
    }

    public String getTextMsg(){
        return textMsg;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public void setTextMsg(String textMsg){
        this.textMsg = textMsg;
    }

    public float getWidth(){
        return mWidth;
    }

    /**
     * currently used only for bar chart.
     * @param width
     */
    public void setWidth(float width){
        this.mWidth = width;
    }

    public float getHeight(){
        return mHeight;
    }

    public void setHeight(float height){
        this.mHeight = height;
    }

    public PointF getStart(){
        return mStart;
    }

    public void setStart(PointF start){
        this.mStart = start;
    }

    public float getMidX(){
        if(null != mStart) {
            mMidX = mStart.x+mWidth/2;
        }else {
            throw new RuntimeException("mStart Cannot be empty");
        }
        return mMidX;
    }

    public void setMidX(float midX){
        this.mMidX = midX;
    }

    public int getColor(){
        return mColor;
    }

    public void setColor(int color){
        mColor = color;
    }

    public float getNum(){
        return mNum;
    }

    public void setNum(float num){
        this.mNum = num;
    }

    public float getMax(){
        return mMax;
    }

    public void setMax(float max){
        this.mMax = max;
    }

    public String getXmsg(){
        return mXmsg;
    }

    public void setXmsg(String xmsg){
        this.mXmsg = xmsg;
    }

    public float getUpper(){
        return mUpper;
    }

    public void setUpper(float upper){
        mUpper = upper;
    }

    public float getLower(){
        return mLower;
    }

    public void setLower(float lower){
        mLower = lower;
    }
}
