package com.tokopedia.seller.product.draft.view.presenter;

import java.util.ArrayList;

/**
 * Created by User on 10/10/2017.
 */

public class ResolutionImageException extends RuntimeException {
    private ArrayList<Integer> failedPositionArrayList = new ArrayList<>();

    public ResolutionImageException(ArrayList<Integer> failedPositionArrayList) {
        this.failedPositionArrayList = failedPositionArrayList;
    }

    public String getFailedPositionString() {
        String failedPosString = "";
        for (int i = 0, sizei = failedPositionArrayList.size(); i<sizei; i++ ) {
            if (i > 0) {
                failedPosString += ", ";
            }
            failedPosString += (i + 1);
        }
        return failedPosString;
    }

    public ArrayList<Integer> getFailedPositionArrayList() {
        return failedPositionArrayList;
    }
}
