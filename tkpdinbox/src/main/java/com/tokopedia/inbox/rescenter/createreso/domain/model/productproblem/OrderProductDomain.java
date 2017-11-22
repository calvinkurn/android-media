package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;


import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderProductDomain {
    @Nullable
    private String name;

    @Nullable
    private String thumb;

    @Nullable
    private String variant;

    private int quantity;

    @Nullable
    private AmountDomain amountDomain;


    public OrderProductDomain(@Nullable String name,
                              @Nullable String thumb,
                              @Nullable String variant,
                              int quantity,
                              @Nullable AmountDomain amount) {
        this.name = name;
        this.thumb = thumb;
        this.variant = variant;
        this.quantity = quantity;
        this.amountDomain = amount;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getThumb() {
        return thumb;
    }

    public void setThumb(@Nullable String thumb) {
        this.thumb = thumb;
    }

    @Nullable
    public String getVariant() {
        return variant;
    }

    public void setVariant(@Nullable String variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Nullable
    public AmountDomain getAmountDomain() {
        return amountDomain;
    }

    public void setAmountDomain(@Nullable AmountDomain amountDomain) {
        this.amountDomain = amountDomain;
    }
}
