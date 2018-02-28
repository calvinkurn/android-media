package com.tokopedia.transaction.checkout.view.constants;

/**
 * Created by Irfan Khoirul on 30/01/18.
 */

public interface InsuranceConstant {

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
