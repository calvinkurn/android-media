package com.tokopedia.ride.common.configuration;

/**
 * Created by Vishal Gupta on 12/5/17.
 */

public interface PaymentMode {
    String WALLET = "wallet";
    String CC = "cc";

    String WALLET_DISPLAY_NAME = "TokoCash";
    String CC_DISPLAY_NAME = "Credit Card";
    String DEFAULT_DISPLAY_NAME = "Amount";
}

