package com.tokopedia.transaction.addtocart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by Angga.Prasetiyo on 28/03/2016.
 */
public class Insurance {
    private static final String TAG = Insurance.class.getSimpleName();

    public static final String INSURANCE = "1";
    public static final String NOT_INSURANCE = "0";

    private String label;
    private boolean insurance;

    public Insurance(String string, boolean inInsurance) {
        this.label = string;
        this.insurance = inInsurance;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public int getInsurance() {
        return (insurance) ? 1 : 0;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    @Override
    public String toString() {
        return label;
    }

    public static List<Insurance> createList(String[] arrayData) {
        List<Insurance> datas = new ArrayList<>();
        for (int i = 0; i < arrayData.length; i++) {
            datas.add(i == 0 ? new Insurance(arrayData[i], true)
                    : new Insurance(arrayData[i], false));
        }
        return datas;
    }
}
