package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import java.util.ArrayList;
import java.util.List;

public class CartItem implements Parcelable {

    @SerializedName("cart_total_logistic_fee_idr")
    @Expose
    private String cartTotalLogisticFeeIdr;
    @SerializedName("cart_shipping_rate_idr")
    @Expose
    private String cartShippingRateIdr;
    @SerializedName("cart_total_logistic_fee")
    @Expose
    private String cartTotalLogisticFee;
    @SerializedName("cart_cannot_insurance")
    @Expose
    private Integer cartCannotInsurance;
    @SerializedName("cart_insurance_price")
    @Expose
    private String cartInsurancePrice;
    @SerializedName("cart_total_product")
    @Expose
    private int cartTotalProduct;
    @SerializedName("cart_is_price_changed")
    @Expose
    private String cartIsPriceChanged;
    @SerializedName("cart_insurance_price_idr")
    @Expose
    private String cartInsurancePriceIdr;
    @SerializedName("cart_is_exception_error_delete")
    @Expose
    private String cartIsExceptionErrorDelete;
    @SerializedName("cart_partial")
    @Expose
    private String cartPartial;
    @SerializedName("cart_destination")
    @Expose
    private CartDestination cartDestination;
    @SerializedName("cart_product_type")
    @Expose
    private Integer cartProductType;
    @SerializedName("cart_is_allow_checkout")
    @Expose
    private Integer cartIsAllowCheckout;
    @SerializedName("cart_can_process")
    @Expose
    private Integer cartCanProcess;
    @SerializedName("cart_total_product_price_idr")
    @Expose
    private String cartTotalProductPriceIdr;
    @SerializedName("cart_total_product_active")
    @Expose
    private String cartTotalProductActive;
    @SerializedName("cart_error_message_2")
    @Expose
    private String cartErrorMessage2;
    @SerializedName("cart_insurance_prod")
    @Expose
    private Integer cartInsuranceProd;
    @SerializedName("cart_error_message_1")
    @Expose
    private String cartErrorMessage1;
    @SerializedName("cart_shop")
    @Expose
    private CartShop cartShop;
    @SerializedName("cart_total_amount_idr")
    @Expose
    private String cartTotalAmountIdr;
    @SerializedName("cart_logistic_fee")
    @Expose
    private String cartLogisticFee;
    @SerializedName("cart_total_error")
    @Expose
    private Integer cartTotalError = 0;
    @SerializedName("cart_total_product_price")
    @Expose
    private String cartTotalProductPrice;
    @SerializedName("cart_products")
    @Expose
    private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
    @SerializedName("cart_customer_id")
    @Expose
    private Integer cartCustomerId;
    @SerializedName("cart_total_amount")
    @Expose
    private String cartTotalAmount;
    @SerializedName("cart_force_insurance")
    @Expose
    private Integer cartForceInsurance;
    @SerializedName("cart_total_weight")
    @Expose
    private String cartTotalWeight;
    @SerializedName("cart_shipping_rate")
    @Expose
    private String cartShippingRate;
    @SerializedName("cart_shipments")
    @Expose
    private CartShipments cartShipments;
    @SerializedName("cart_total_cart_count")
    @Expose
    private String cartTotalCartCount;
    @SerializedName("cart_string")
    @Expose
    private String cartString;
    @SerializedName("cart_rates_string")
    @Expose
    private String cartRatesString;
    @SerializedName("cart_cat_id")
    @Expose
    private String cartCatId;

    // Not confirmed yet
    @SerializedName("store")
    @Expose
    private Store store;

    public String getCartTotalLogisticFeeIdr() {
        return cartTotalLogisticFeeIdr;
    }

    public void setCartTotalLogisticFeeIdr(String cartTotalLogisticFeeIdr) {
        this.cartTotalLogisticFeeIdr = cartTotalLogisticFeeIdr;
    }

    public String getCartShippingRateIdr() {
        return cartShippingRateIdr;
    }

    public void setCartShippingRateIdr(String cartShippingRateIdr) {
        this.cartShippingRateIdr = cartShippingRateIdr;
    }

    public String getCartTotalLogisticFee() {
        return cartTotalLogisticFee;
    }

    public void setCartTotalLogisticFee(String cartTotalLogisticFee) {
        this.cartTotalLogisticFee = cartTotalLogisticFee;
    }

    public Integer getCartCannotInsurance() {
        return cartCannotInsurance;
    }

    public void setCartCannotInsurance(Integer cartCannotInsurance) {
        this.cartCannotInsurance = cartCannotInsurance;
    }

    public String getCartInsurancePrice() {
        return cartInsurancePrice;
    }

    public void setCartInsurancePrice(String cartInsurancePrice) {
        this.cartInsurancePrice = cartInsurancePrice;
    }

    public int getCartTotalProduct() {
        return cartTotalProduct;
    }

    public void setCartTotalProduct(int cartTotalProduct) {
        this.cartTotalProduct = cartTotalProduct;
    }

    public String getCartIsPriceChanged() {
        return cartIsPriceChanged;
    }

    public void setCartIsPriceChanged(String cartIsPriceChanged) {
        this.cartIsPriceChanged = cartIsPriceChanged;
    }

    public String getCartInsurancePriceIdr() {
        return cartInsurancePriceIdr;
    }

    public void setCartInsurancePriceIdr(String cartInsurancePriceIdr) {
        this.cartInsurancePriceIdr = cartInsurancePriceIdr;
    }

    public String getCartIsExceptionErrorDelete() {
        return cartIsExceptionErrorDelete;
    }

    public void setCartIsExceptionErrorDelete(String cartIsExceptionErrorDelete) {
        this.cartIsExceptionErrorDelete = cartIsExceptionErrorDelete;
    }

    public String getCartPartial() {
        return cartPartial;
    }

    public void setCartPartial(String cartPartial) {
        this.cartPartial = cartPartial;
    }

    public CartDestination getCartDestination() {
        return cartDestination;
    }

    public void setCartDestination(CartDestination cartDestination) {
        this.cartDestination = cartDestination;
    }

    public Integer getCartProductType() {
        return cartProductType;
    }

    public void setCartProductType(Integer cartProductType) {
        this.cartProductType = cartProductType;
    }

    public Integer getCartIsAllowCheckout() {
        return cartIsAllowCheckout;
    }

    public void setCartIsAllowCheckout(Integer cartIsAllowCheckout) {
        this.cartIsAllowCheckout = cartIsAllowCheckout;
    }

    public Integer getCartCanProcess() {
        return cartCanProcess;
    }

    public void setCartCanProcess(Integer cartCanProcess) {
        this.cartCanProcess = cartCanProcess;
    }

    public String getCartTotalProductPriceIdr() {
        return cartTotalProductPriceIdr;
    }

    public void setCartTotalProductPriceIdr(String cartTotalProductPriceIdr) {
        this.cartTotalProductPriceIdr = cartTotalProductPriceIdr;
    }

    public String getCartTotalProductActive() {
        return cartTotalProductActive;
    }

    public void setCartTotalProductActive(String cartTotalProductActive) {
        this.cartTotalProductActive = cartTotalProductActive;
    }

    public String getCartErrorMessage2() {
        if(cartErrorMessage2 == null) return "";
        else return cartErrorMessage2;
    }

    public void setCartErrorMessage2(String cartErrorMessage2) {
        this.cartErrorMessage2 = cartErrorMessage2;
    }

    public Integer getCartInsuranceProd() {
        return cartInsuranceProd;
    }

    public void setCartInsuranceProd(Integer cartInsuranceProd) {
        this.cartInsuranceProd = cartInsuranceProd;
    }

    public String getCartErrorMessage1() {
        if(cartErrorMessage1 == null) return "";
        else return cartErrorMessage1;
    }

    public void setCartErrorMessage1(String cartErrorMessage1) {
        this.cartErrorMessage1 = cartErrorMessage1;
    }

    public CartShop getCartShop() {
        return cartShop;
    }

    public void setCartShop(CartShop cartShop) {
        this.cartShop = cartShop;
    }

    public String getCartTotalAmountIdr() {
        return cartTotalAmountIdr;
    }

    public void setCartTotalAmountIdr(String cartTotalAmountIdr) {
        this.cartTotalAmountIdr = cartTotalAmountIdr;
    }

    public String getCartLogisticFee() {
        return cartLogisticFee;
    }

    public void setCartLogisticFee(String cartLogisticFee) {
        this.cartLogisticFee = cartLogisticFee;
    }

    public Integer getCartTotalError() {
        return cartTotalError;
    }

    public void setCartTotalError(Integer cartTotalError) {
        this.cartTotalError = cartTotalError;
    }

    public String getCartTotalProductPrice() {
        return cartTotalProductPrice;
    }

    public void setCartTotalProductPrice(String cartTotalProductPrice) {
        this.cartTotalProductPrice = cartTotalProductPrice;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public Integer getCartCustomerId() {
        return cartCustomerId;
    }

    public void setCartCustomerId(Integer cartCustomerId) {
        this.cartCustomerId = cartCustomerId;
    }

    public String getCartTotalAmount() {
        return cartTotalAmount;
    }

    public void setCartTotalAmount(String cartTotalAmount) {
        this.cartTotalAmount = cartTotalAmount;
    }

    public Integer getCartForceInsurance() {
        return cartForceInsurance;
    }

    public void setCartForceInsurance(Integer cartForceInsurance) {
        this.cartForceInsurance = cartForceInsurance;
    }

    public String getCartTotalWeight() {
        return cartTotalWeight;
    }

    public void setCartTotalWeight(String cartTotalWeight) {
        this.cartTotalWeight = cartTotalWeight;
    }

    public String getCartShippingRate() {
        return cartShippingRate;
    }

    public void setCartShippingRate(String cartShippingRate) {
        this.cartShippingRate = cartShippingRate;
    }

    public CartShipments getCartShipments() {
        return cartShipments;
    }

    public void setCartShipments(CartShipments cartShipments) {
        this.cartShipments = cartShipments;
    }

    public String getCartTotalCartCount() {
        return cartTotalCartCount;
    }

    public void setCartTotalCartCount(String cartTotalCartCount) {
        this.cartTotalCartCount = cartTotalCartCount;
    }

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }

    public String getCartCatId() {
        return cartCatId;
    }

    public void setCartCatId(String cartCatId) {
        this.cartCatId = cartCatId;
    }

    public String getCartRatesString() {
        return cartRatesString;
    }

    public void setCartRatesString(String cartRatesString) {
        this.cartRatesString = cartRatesString;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public CartItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cartTotalLogisticFeeIdr);
        dest.writeString(this.cartShippingRateIdr);
        dest.writeString(this.cartTotalLogisticFee);
        dest.writeValue(this.cartCannotInsurance);
        dest.writeString(this.cartInsurancePrice);
        dest.writeInt(this.cartTotalProduct);
        dest.writeString(this.cartIsPriceChanged);
        dest.writeString(this.cartInsurancePriceIdr);
        dest.writeString(this.cartIsExceptionErrorDelete);
        dest.writeString(this.cartPartial);
        dest.writeParcelable(this.cartDestination, flags);
        dest.writeValue(this.cartProductType);
        dest.writeValue(this.cartIsAllowCheckout);
        dest.writeValue(this.cartCanProcess);
        dest.writeString(this.cartTotalProductPriceIdr);
        dest.writeString(this.cartTotalProductActive);
        dest.writeString(this.cartErrorMessage2);
        dest.writeValue(this.cartInsuranceProd);
        dest.writeString(this.cartErrorMessage1);
        dest.writeParcelable(this.cartShop, flags);
        dest.writeString(this.cartTotalAmountIdr);
        dest.writeString(this.cartLogisticFee);
        dest.writeInt(this.cartTotalError);
        dest.writeString(this.cartTotalProductPrice);
        dest.writeTypedList(this.cartProducts);
        dest.writeValue(this.cartCustomerId);
        dest.writeString(this.cartTotalAmount);
        dest.writeValue(this.cartForceInsurance);
        dest.writeString(this.cartTotalWeight);
        dest.writeString(this.cartShippingRate);
        dest.writeParcelable(this.cartShipments, flags);
        dest.writeString(this.cartTotalCartCount);
        dest.writeString(this.cartString);
        dest.writeString(this.cartRatesString);
        dest.writeString(this.cartCatId);
        dest.writeParcelable(this.store, flags);
    }

    protected CartItem(Parcel in) {
        this.cartTotalLogisticFeeIdr = in.readString();
        this.cartShippingRateIdr = in.readString();
        this.cartTotalLogisticFee = in.readString();
        this.cartCannotInsurance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartInsurancePrice = in.readString();
        this.cartTotalProduct = in.readInt();
        this.cartIsPriceChanged = in.readString();
        this.cartInsurancePriceIdr = in.readString();
        this.cartIsExceptionErrorDelete = in.readString();
        this.cartPartial = in.readString();
        this.cartDestination = in.readParcelable(CartDestination.class.getClassLoader());
        this.cartProductType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartIsAllowCheckout = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartCanProcess = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartTotalProductPriceIdr = in.readString();
        this.cartTotalProductActive = in.readString();
        this.cartErrorMessage2 = in.readString();
        this.cartInsuranceProd = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartErrorMessage1 = in.readString();
        this.cartShop = in.readParcelable(CartShop.class.getClassLoader());
        this.cartTotalAmountIdr = in.readString();
        this.cartLogisticFee = in.readString();
        this.cartTotalError = in.readInt();
        this.cartTotalProductPrice = in.readString();
        this.cartProducts = in.createTypedArrayList(CartProduct.CREATOR);
        this.cartCustomerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartTotalAmount = in.readString();
        this.cartForceInsurance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cartTotalWeight = in.readString();
        this.cartShippingRate = in.readString();
        this.cartShipments = in.readParcelable(CartShipments.class.getClassLoader());
        this.cartTotalCartCount = in.readString();
        this.cartString = in.readString();
        this.cartRatesString = in.readString();
        this.cartCatId = in.readString();
        this.store = in.readParcelable(Store.class.getClassLoader());
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
