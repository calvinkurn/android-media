package com.tokopedia.inbox.rescenter.historyaddress.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListHistoryAddress {
    @SerializedName("address")
    private Address address;
    @SerializedName("receiver")
    private Receiver receiver;
    @SerializedName("detail")
    private Detail detail;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public static class Address {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("street")
        private String street;
        @SerializedName("district")
        private District district;
        @SerializedName("city")
        private City city;
        @SerializedName("province")
        private Province province;
        @SerializedName("country")
        private String country;
        @SerializedName("postalCode")
        private String postalCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public District getDistrict() {
            return district;
        }

        public void setDistrict(District district) {
            this.district = district;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public Province getProvince() {
            return province;
        }

        public void setProvince(Province province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public static class District {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class City {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class Province {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class Receiver {
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class Detail {
        @SerializedName("id")
        private String id;
        @SerializedName("action")
        private Action action;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public static class Action {
            @SerializedName("name")
            private String name;
            @SerializedName("by")
            private int by;
            @SerializedName("createTimeStr")
            private String createTimeStr;
            @SerializedName("byStr")
            private String byStr;
            @SerializedName("createTimeFullStr")
            private String createTimestamp;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getBy() {
                return by;
            }

            public void setBy(int by) {
                this.by = by;
            }

            public String getCreateTimeStr() {
                return createTimeStr;
            }

            public void setCreateTimeStr(String createTimeStr) {
                this.createTimeStr = createTimeStr;
            }

            public String getCreateTimestamp() {
                return createTimestamp;
            }

            public void setCreateTimestamp(String createTimestamp) {
                this.createTimestamp = createTimestamp;
            }

            public String getByStr() {
                return byStr;
            }

            public void setByStr(String byStr) {
                this.byStr = byStr;
            }

        }
    }
}
