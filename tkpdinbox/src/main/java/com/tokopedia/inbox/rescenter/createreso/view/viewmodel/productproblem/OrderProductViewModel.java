package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderProductViewModel {
    private String name;
    private String thumb;
    private String variant;
    private int quantity;
    private AmountViewModel amount;

    public OrderProductViewModel() {

    }

    public OrderProductViewModel(String name, String thumb, String variant, int quantity, AmountViewModel amount) {
        this.name = name;
        this.thumb = thumb;
        this.variant = variant;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AmountViewModel getAmount() {
        return amount;
    }

    public void setAmount(AmountViewModel amount) {
        this.amount = amount;
    }
}
