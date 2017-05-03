package com.tokopedia.seller.myproduct.utils;

/**
 * Created by noiz354 on 4/7/16.
 */
public enum AddProductType {
    ADD(0), ADD_FROM_SOCIAL_MEDIA(1), EDIT(2), ADD_FROM_GALLERY(3), ADD_MULTIPLE_FROM_GALERY(4), COPY(5), MODIFY(6);

    int type;
    AddProductType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result = "";
        switch (this){
            case ADD:
                result = "tambahkan baru";
                break;
            case ADD_FROM_SOCIAL_MEDIA:
                result = "tambahkan baru dari social media seperti instagram";
                break;
            case EDIT:
                result = "edit product";
                break;
        }
        return super.toString();
    }
}
