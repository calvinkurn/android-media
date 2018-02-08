package com.tokopedia.transaction.addtocart.utils;

/**
 * Created by Irfan Khoirul on 04/12/17.
 */

public interface KeroppiConstants {

    interface InsuranceType {
        int NO = 1;

        int OPTIONAL = 2;

        int MUST = 3;
    }

    interface InsuranceUsedType {
        int LOGISTIC_INSURANCE = 1;

        int TOKOPEDIA_INSURANCE = 2;
    }

    interface InsuranceUsedDefault {
        int NO = 1;

        int YES = 2;
    }
}
