package com.tokopedia.tokocash.network.model;

/**
 * Created by nabillasabbaha on 11/17/17.
 */

public class RefreshTokenEntity {

   private WalletTokenEntity data;

    public WalletTokenEntity getData() {
        return data;
    }

    public void setData(WalletTokenEntity data) {
        this.data = data;
    }
}
