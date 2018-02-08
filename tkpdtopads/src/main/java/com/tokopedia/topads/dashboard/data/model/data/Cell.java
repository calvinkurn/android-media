package com.tokopedia.topads.dashboard.data.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cell {

    @SerializedName("date.day")
    @Expose
    private int dateDay;
    @SerializedName("date.month")
    @Expose
    private int dateMonth;
    @SerializedName("date.year")
    @Expose
    private int dateYear;
    @SerializedName("impression_sum")
    @Expose
    private int impressionSum;
    @SerializedName("click_sum")
    @Expose
    private int clickSum;
    @SerializedName("ctr_percentage")
    @Expose
    private float ctrPercentage;
    @SerializedName("conversion_sum")
    @Expose
    private int conversionSum;
    @SerializedName("cost_avg")
    @Expose
    private float costAvg;
    @SerializedName("cost_sum")
    @Expose
    private int costSum;
    @SerializedName("impression_sum_fmt")
    @Expose
    private String impressionSumFmt;
    @SerializedName("click_sum_fmt")
    @Expose
    private String clickSumFmt;
    @SerializedName("ctr_percentage_fmt")
    @Expose
    private String ctrPercentageFmt;
    @SerializedName("conversion_sum_fmt")
    @Expose
    private String conversionSumFmt;
    @SerializedName("cost_avg_fmt")
    @Expose
    private String costAvgFmt;
    @SerializedName("cost_sum_fmt")
    @Expose
    private String costSumFmt;

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getImpressionSum() {
        return impressionSum;
    }

    public void setImpressionSum(int impressionSum) {
        this.impressionSum = impressionSum;
    }

    public int getClickSum() {
        return clickSum;
    }

    public void setClickSum(int clickSum) {
        this.clickSum = clickSum;
    }

    public float getCtrPercentage() {
        return ctrPercentage;
    }

    public void setCtrPercentage(float ctrPercentage) {
        this.ctrPercentage = ctrPercentage;
    }

    public int getConversionSum() {
        return conversionSum;
    }

    public void setConversionSum(int conversionSum) {
        this.conversionSum = conversionSum;
    }

    public float getCostAvg() {
        return costAvg;
    }

    public void setCostAvg(float costAvg) {
        this.costAvg = costAvg;
    }

    public int getCostSum() {
        return costSum;
    }

    public void setCostSum(int costSum) {
        this.costSum = costSum;
    }

    public String getImpressionSumFmt() {
        return impressionSumFmt;
    }

    public void setImpressionSumFmt(String impressionSumFmt) {
        this.impressionSumFmt = impressionSumFmt;
    }

    public String getClickSumFmt() {
        return clickSumFmt;
    }

    public void setClickSumFmt(String clickSumFmt) {
        this.clickSumFmt = clickSumFmt;
    }

    public String getCtrPercentageFmt() {
        return ctrPercentageFmt;
    }

    public void setCtrPercentageFmt(String ctrPercentageFmt) {
        this.ctrPercentageFmt = ctrPercentageFmt;
    }

    public String getConversionSumFmt() {
        return conversionSumFmt;
    }

    public void setConversionSumFmt(String conversionSumFmt) {
        this.conversionSumFmt = conversionSumFmt;
    }

    public String getCostAvgFmt() {
        return costAvgFmt;
    }

    public void setCostAvgFmt(String costAvgFmt) {
        this.costAvgFmt = costAvgFmt;
    }

    public String getCostSumFmt() {
        return costSumFmt;
    }

    public void setCostSumFmt(String costSumFmt) {
        this.costSumFmt = costSumFmt;
    }

    public Date getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = getDateDay() + "/" + getDateMonth() + "/" + getDateYear();
        Date date = new Date();
        try {
            date = formatter.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
