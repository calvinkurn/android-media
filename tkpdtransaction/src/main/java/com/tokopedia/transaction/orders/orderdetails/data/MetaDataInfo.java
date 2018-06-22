package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaDataInfo {

    @SerializedName("entity_category_id")
    @Expose
    private String entity_category_id;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("entity_provider_id")
    @Expose
    private String entity_provider_id;

    @SerializedName("entity_image")
    @Expose
    private String entity_image;

    @SerializedName("entity_product_id")
    @Expose
    private String entity_product_id;

    @SerializedName("total_ticket_price")
    @Expose
    private String total_ticket_price;

    @SerializedName("entity_brand_name")
    @Expose
    private String entity_brand_name;

    @SerializedName("total_ticket_count")
    @Expose
    private String total_ticket_count;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("entity_product_name")
    @Expose
    private String entity_product_name;

    @SerializedName("entity_address")
    @Expose
    private EntityAddress entityaddress;


    public String getEntity_category_id ()
    {
        return entity_category_id;
    }

    public void setEntity_category_id (String entity_category_id)
    {
        this.entity_category_id = entity_category_id;
    }

    public String getEnd_date ()
    {
        return end_date;
    }

    public void setEnd_date (String end_date)
    {
        this.end_date = end_date;
    }

    public String getEntity_provider_id ()
    {
        return entity_provider_id;
    }

    public void setEntity_provider_id (String entity_provider_id)
    {
        this.entity_provider_id = entity_provider_id;
    }

    public String getEntity_image ()
    {
        return entity_image;
    }

    public void setEntity_image (String entity_image)
    {
        this.entity_image = entity_image;
    }

    public String getEntity_product_id ()
    {
        return entity_product_id;
    }

    public void setEntity_product_id (String entity_product_id)
    {
        this.entity_product_id = entity_product_id;
    }

    public String getTotal_ticket_price ()
    {
        return total_ticket_price;
    }

    public void setTotal_ticket_price (String total_ticket_price)
    {
        this.total_ticket_price = total_ticket_price;
    }

    public String getEntity_brand_name ()
    {
        return entity_brand_name;
    }

    public void setEntity_brand_name (String entity_brand_name)
    {
        this.entity_brand_name = entity_brand_name;
    }

    public String getTotal_ticket_count ()
    {
        return total_ticket_count;
    }

    public void setTotal_ticket_count (String total_ticket_count)
    {
        this.total_ticket_count = total_ticket_count;
    }

    public String getStart_date ()
    {
        return start_date;
    }

    public void setStart_date (String start_date)
    {
        this.start_date = start_date;
    }

    public String getEntity_product_name ()
    {
        return entity_product_name;
    }

    public void setEntity_product_name (String entity_product_name)
    {
        this.entity_product_name = entity_product_name;
    }

    public EntityAddress getEntityaddress () {
        return entityaddress;
    }

    public void setEntity_address (EntityAddress entityaddress)
    {
        this.entityaddress = entityaddress;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [entity_category_id = "+entity_category_id+", end_date = "+end_date+", entity_provider_id = "+entity_provider_id+", entity_image = "+entity_image+", entity_product_id = "+entity_product_id+", total_ticket_price = "+total_ticket_price+", entity_brand_name = "+entity_brand_name+", total_ticket_count = "+total_ticket_count+", start_date = "+start_date+", entity_product_name = "+entity_product_name+", entity_address = "+entityaddress+"]";
    }
}
