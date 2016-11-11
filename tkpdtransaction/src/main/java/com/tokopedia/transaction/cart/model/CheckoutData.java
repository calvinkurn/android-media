package com.tokopedia.transaction.cart.model;

/**
 * @author anggaprasetiyo on 11/4/16.
 */

public class CheckoutData {

    private String lpFlag = "1";
    private String depositAmount;
    private String dropShipString;
    private String step;
    private String gateway;
    private String token;
    private String partialString;
    private String usedDeposit;

    private CheckoutData(Builder builder) {
        setLpFlag(builder.lpFlag);
        setDepositAmount(builder.depositAmount);
        setDropShipString(builder.dropShipString);
        setStep(builder.step);
        setGateway(builder.gateway);
        setToken(builder.token);
        setPartialString(builder.partialString);
        setUsedDeposit(builder.usedDeposit);
    }

    public String getLpFlag() {
        return lpFlag;
    }

    public void setLpFlag(String lpFlag) {
        this.lpFlag = lpFlag;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDropShipString() {
        return dropShipString;
    }

    public void setDropShipString(String dropShipString) {
        this.dropShipString = dropShipString;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPartialString() {
        return partialString;
    }

    public void setPartialString(String partialString) {
        this.partialString = partialString;
    }

    public String getUsedDeposit() {
        return usedDeposit;
    }

    public void setUsedDeposit(String usedDeposit) {
        this.usedDeposit = usedDeposit;
    }


    public static final class Builder {
        private String lpFlag = "1";
        private String depositAmount;
        private String dropShipString;
        private String step;
        private String gateway;
        private String token;
        private String partialString;
        private String usedDeposit;

        public Builder() {
        }

        public Builder lpFlag(String val) {
            lpFlag = val;
            return this;
        }

        public Builder depositAmount(String val) {
            depositAmount = val;
            return this;
        }

        public Builder dropShipString(String val) {
            dropShipString = val;
            return this;
        }

        public Builder step(String val) {
            step = val;
            return this;
        }

        public Builder gateway(String val) {
            gateway = val;
            return this;
        }

        public Builder token(String val) {
            token = val;
            return this;
        }

        public Builder partialString(String val) {
            partialString = val;
            return this;
        }

        public Builder usedDeposit(String val) {
            usedDeposit = val;
            return this;
        }

        public CheckoutData build() {
            return new CheckoutData(this);
        }
    }
}
