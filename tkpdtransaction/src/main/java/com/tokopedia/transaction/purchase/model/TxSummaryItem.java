package com.tokopedia.transaction.purchase.model;

/**
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryItem {

    private String name;
    private int count;
    private String desc;

    public TxSummaryItem(String name, String desc, int count) {
        this.name = name;
        this.desc = desc;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
