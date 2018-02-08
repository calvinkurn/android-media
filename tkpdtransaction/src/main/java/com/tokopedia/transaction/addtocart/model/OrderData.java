package com.tokopedia.transaction.addtocart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shipment;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shop;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 11/03/2016.
 * Edited by Hafizh Herdi for KERO
 */
public class OrderData implements Parcelable {
    private static final String TAG = OrderData.class.getSimpleName();

    private String insurance;
    private String notes;
    private String productId;
    private Destination address;
    private String shipment;
    private String shipmentPackage;
    private int quantity;
    private String weight;
    private String initWeight;
    private String shopId;
    private int minOrder;
    private String priceItem;
    private String priceTotal;
    private Shop shop;
    private List<Shipment> shipments;
    private String catId;
    private Integer mustInsurance;

    public OrderData() {
    }

    public String getInitWeight() {
        return initWeight;
    }

    public void setInitWeight(String initWeight) {
        this.initWeight = initWeight;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Destination getAddress() {
        return address;
    }

    public void setAddress(Destination address) {
        this.address = address;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getShipmentPackage() {
        return shipmentPackage;
    }

    public void setShipmentPackage(String shipmentPackage) {
        this.shipmentPackage = shipmentPackage;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(String priceItem) {
        this.priceItem = priceItem;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public Integer getMustInsurance() {
        return mustInsurance;
    }

    public void setMustInsurance(Integer mustInsurance) {
        this.mustInsurance = mustInsurance;
    }

    protected OrderData(Parcel in) {
        insurance = in.readString();
        notes = in.readString();
        productId = in.readString();
        address = (Destination) in.readValue(Destination.class.getClassLoader());
        shipment = in.readString();
        shipmentPackage = in.readString();
        quantity = in.readInt();
        weight = in.readString();
        shopId = in.readString();
        minOrder = in.readInt();
        priceItem = in.readString();
        priceTotal = in.readString();
        shop = (Shop) in.readValue(Shop.class.getClassLoader());
        initWeight = in.readString();
        shipments = in.readArrayList(Shipment.class.getClassLoader());
        catId = in.readString();
        mustInsurance = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(insurance);
        dest.writeString(notes);
        dest.writeString(productId);
        dest.writeValue(address);
        dest.writeString(shipment);
        dest.writeString(shipmentPackage);
        dest.writeInt(quantity);
        dest.writeString(weight);
        dest.writeString(shopId);
        dest.writeInt(minOrder);
        dest.writeString(priceItem);
        dest.writeString(priceTotal);
        dest.writeValue(shop);
        dest.writeString(initWeight);
        dest.writeArray(shipments.toArray());
        dest.writeString(catId);
        dest.writeInt(mustInsurance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderData> CREATOR = new Parcelable.Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    public static OrderData createFromATCForm(AtcFormData data, ProductCartPass productCartPass) {
        OrderData orderData = new OrderData();
        orderData.setProductId(data.getForm().getProductDetail().getProductId());
        if (data.getForm().getDestination().isCompleted()) {
            orderData.setAddress(data.getForm().getDestination());
        }
        orderData.setProductId(data.getForm().getProductDetail().getProductId());
        orderData.setQuantity(Integer.parseInt(data.getForm().getProductDetail().getProductMinOrder()));
        orderData.setWeight(data.getForm().getProductDetail().getProductWeight());
        orderData.setShopId(productCartPass.getShopId());
        orderData.setMinOrder(productCartPass.getMinOrder());
        orderData.setPriceTotal(data.getForm().getProductDetail().getProductPrice());
        orderData.setPriceItem(productCartPass.getPrice());
        orderData.setShop(data.getShop());
        orderData.setInitWeight(data.getForm().getProductDetail().getProductWeight());
        orderData.setShipments(data.getForm().getShipment());
        orderData.setCatId(data.getForm().getProductDetail().getProductCatId());
        orderData.setMustInsurance(data.getForm().getProductDetail().getProductMustInsurance());
        return orderData;
    }
}
