package com.tokopedia.shop.product.data.source.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DynamicFilterModel implements Parcelable{
    @SerializedName("process_time")
    @Expose
    String processTime;
    @SerializedName("data")
    @Expose
    DataValue data;
    @SerializedName("status")
    @Expose
    String status;

    public DynamicFilterModel() {
    }

    /**
     * @return The processTime
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * @param processTime The process_time
     */
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    /**
     * @return The data
     */
    public DataValue getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(DataValue data) {
        this.data = data;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * use this for listener
     */
    public static final class DynamicFilterContainer implements ObjContainer<DynamicFilterModel>, Parcelable {

        DynamicFilterModel dynamicFilterModel;

        public DynamicFilterContainer(DynamicFilterModel dynamicFilterModel) {
            this.dynamicFilterModel = dynamicFilterModel;
        }

        @Override
        public DynamicFilterModel body() {
            return dynamicFilterModel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.dynamicFilterModel, flags);
        }

        protected DynamicFilterContainer(Parcel in) {
            this.dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        }

        public static final Parcelable.Creator<DynamicFilterContainer> CREATOR = new Parcelable.Creator<DynamicFilterContainer>() {
            @Override
            public DynamicFilterContainer createFromParcel(Parcel source) {
                return new DynamicFilterContainer(source);
            }

            @Override
            public DynamicFilterContainer[] newArray(int size) {
                return new DynamicFilterContainer[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.processTime);
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.status);
    }

    protected DynamicFilterModel(Parcel in) {
        this.processTime = in.readString();
        this.data = in.readParcelable(DataValue.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Parcelable.Creator<DynamicFilterModel> CREATOR = new Parcelable.Creator<DynamicFilterModel>() {
        @Override
        public DynamicFilterModel createFromParcel(Parcel source) {
            return new DynamicFilterModel(source);
        }

        @Override
        public DynamicFilterModel[] newArray(int size) {
            return new DynamicFilterModel[size];
        }
    };

    public static class DataValue implements Parcelable {


        String selected;
        String selectedOb;

        @SerializedName("filter")
        @Expose
        List<Filter> filter = new ArrayList<>();
        @SerializedName("sort")
        @Expose
        List<Sort> sort = new ArrayList<>();

        /**
         * @return The filter
         */
        public List<Filter> getFilter() {
            return filter;
        }

        /**
         * @param filter The filter
         */
        public void setFilter(List<Filter> filter) {
            this.filter = filter;
        }

        /**
         * @return The sort
         */
        public List<Sort> getSort() {
            return sort;
        }

        /**
         * @param sort The sort
         */
        public void setSort(List<Sort> sort) {
            this.sort = sort;
        }

        public String getSelectedOb() {
            return selectedOb;
        }

        public void setSelectedOb(String selectedOb) {
            this.selectedOb = selectedOb;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.selected);
            dest.writeString(this.selectedOb);
            dest.writeTypedList(this.filter);
            dest.writeTypedList(this.sort);
        }

        public DataValue() {
        }

        protected DataValue(Parcel in) {
            this.selected = in.readString();
            this.selectedOb = in.readString();
            this.filter = in.createTypedArrayList(Filter.CREATOR);
            this.sort = in.createTypedArrayList(Sort.CREATOR);
        }

        public static final Creator<DataValue> CREATOR = new Creator<DataValue>() {
            @Override
            public DataValue createFromParcel(Parcel source) {
                return new DataValue(source);
            }

            @Override
            public DataValue[] newArray(int size) {
                return new DataValue[size];
            }
        };
    }

    public static class Filter implements Parcelable {

        public static final String TEMPLATE_NAME_LOCATION = "template_location";

        private static final String TEMPLATE_NAME_SEPARATOR = "template_separator";
        private static final String TEMPLATE_NAME_RATING = "template_rating";
        private static final String TEMPLATE_NAME_SIZE = "template_size";
        private static final String TEMPLATE_NAME_CATEGORY = "template_category";
        private static final String TEMPLATE_NAME_COLOR = "template_color";
        private static final String TEMPLATE_NAME_PRICE = "template_price";
        private static final String TEMPLATE_NAME_BRAND = "template_brand";

        @SerializedName("title")
        @Expose
        String title;
        @SerializedName("template_name")
        @Expose
        String templateName;
        @SerializedName("search")
        @Expose
        Search search;
        @SerializedName("options")
        @Expose
        List<Option> options = new ArrayList<>();

        public Filter() {
        }

        public boolean isSeparator() {
            return TEMPLATE_NAME_SEPARATOR.equals(templateName);
        }

        public boolean isCategoryFilter() {
            return TEMPLATE_NAME_CATEGORY.equals(templateName);
        }

        public boolean isColorFilter() {
            return TEMPLATE_NAME_COLOR.equals(templateName);
        }

        public boolean isPriceFilter() {
            return TEMPLATE_NAME_PRICE.equals(templateName);
        }

        public boolean isRatingFilter() {
            return TEMPLATE_NAME_RATING.equals(templateName);
        }

        public boolean isSizeFilter() {
            return TEMPLATE_NAME_SIZE.equals(templateName);
        }

        public boolean isLocationFilter() {
            return TEMPLATE_NAME_LOCATION.equals(templateName);
        }

        public boolean isBrandFilter() {
            return TEMPLATE_NAME_BRAND.equals(templateName);
        }

        public boolean isExpandableFilter() {
            return isCategoryFilter() || isColorFilter() || isRatingFilter()
                    || isSizeFilter() || isBrandFilter() || options.size() > 1;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        /**
         * @return The search
         */
        public Search getSearch() {
            return search;
        }

        /**
         * @param search The search
         */
        public void setSearch(Search search) {
            this.search = search;
        }

        /**
         * @return The options
         */
        public List<Option> getOptions() {
            return options;
        }

        /**
         * @param options The options
         */
        public void setOptions(List<Option> options) {
            this.options = options;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.templateName);
            dest.writeParcelable(this.search, flags);
            dest.writeTypedList(this.options);
        }

        protected Filter(Parcel in) {
            this.title = in.readString();
            this.templateName = in.readString();
            this.search = in.readParcelable(Search.class.getClassLoader());
            this.options = in.createTypedArrayList(Option.CREATOR);
        }

        public static final Creator<Filter> CREATOR = new Creator<Filter>() {
            @Override
            public Filter createFromParcel(Parcel source) {
                return new Filter(source);
            }

            @Override
            public Filter[] newArray(int size) {
                return new Filter[size];
            }
        };
    }

    public static class Sort implements Parcelable {

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }


        @Override
        public String toString() {
            return getName();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
        }

        public Sort() {
        }

        protected Sort(Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
        }

        public static final Creator<Sort> CREATOR = new Creator<Sort>() {
            @Override
            public Sort createFromParcel(Parcel source) {
                return new Sort(source);
            }

            @Override
            public Sort[] newArray(int size) {
                return new Sort[size];
            }
        };
    }

    public static class Search implements Parcelable {

        @SerializedName("searchable")
        @Expose
        int searchable;
        @SerializedName("placeholder")
        @Expose
        String placeholder;

        public Search() {
        }

        /**
         * @return The searchable
         */
        public int getSearchable() {
            return searchable;
        }

        /**
         * @param searchable The searchable
         */
        public void setSearchable(int searchable) {
            this.searchable = searchable;
        }

        /**
         * @return The placeholder
         */
        public String getPlaceholder() {
            return placeholder;
        }

        /**
         * @param placeholder The placeholder
         */
        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.searchable);
            dest.writeString(this.placeholder);
        }

        protected Search(Parcel in) {
            this.searchable = in.readInt();
            this.placeholder = in.readString();
        }

        public static final Creator<Search> CREATOR = new Creator<Search>() {
            @Override
            public Search createFromParcel(Parcel source) {
                return new Search(source);
            }

            @Override
            public Search[] newArray(int size) {
                return new Search[size];
            }
        };
    }

    public static class Option implements Parcelable {

        public static final String KEY_PRICE_MIN = "pmin";
        public static final String KEY_PRICE_MAX = "pmax";
        public static final String KEY_PRICE_MIN_MAX_RANGE = "pmin-pmax";
        public static final String KEY_PRICE_WHOLESALE = "wholesale";
        public static final String KEY_CATEGORY = "sc";
        public static final String KEY_RATING = "rt";

        public static final String INPUT_TYPE_TEXTBOX = "textbox";
        public static final String INPUT_TYPE_CHECKBOX = "checkbox";
        public static final String UID_SEPARATOR_SYMBOL = "#";
        public static final String METRIC_INTERNATIONAL = "International";

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;
        @SerializedName("hex_color")
        @Expose
        String hexColor;
        @SerializedName("metric")
        @Expose
        String metric;
        @SerializedName("total_data")
        @Expose
        String totalData;
        @SerializedName("key_min")
        @Expose
        String keyMin;
        @SerializedName("key_max")
        @Expose
        String keyMax;
        @SerializedName("val_min")
        @Expose
        String valMin;
        @SerializedName("val_max")
        @Expose
        String valMax;
        @SerializedName("icon")
        @Expose
        String iconUrl;
        @SerializedName("is_popular")
        @Expose
        boolean isPopular;
        @SerializedName("child")
        @Expose
        List<LevelTwoCategory> levelTwoCategoryList;

        String inputState = "";

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

        public String getHexColor() {
            return hexColor;
        }

        public void setHexColor(String hexColor) {
            this.hexColor = hexColor;
        }

        public String getMetric() {
            return metric;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public String getTotalData() {
            return totalData;
        }

        public void setTotalData(String totalData) {
            this.totalData = totalData;
        }

        public String getKeyMin() {
            return keyMin;
        }

        public void setKeyMin(String keyMin) {
            this.keyMin = keyMin;
        }

        public String getKeyMax() {
            return keyMax;
        }

        public void setKeyMax(String keyMax) {
            this.keyMax = keyMax;
        }

        public String getValMin() {
            return valMin;
        }

        public void setValMin(String valMin) {
            this.valMin = valMin;
        }

        public String getValMax() {
            return valMax;
        }

        public void setValMax(String valMax) {
            this.valMax = valMax;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getInputState() {
            return inputState;
        }

        public void setInputState(String inputState) {
            this.inputState = inputState;
        }

        public boolean isPopular() {
            return isPopular;
        }

        public void setPopular(boolean popular) {
            isPopular = popular;
        }

        public List<LevelTwoCategory> getLevelTwoCategoryList() {
            return levelTwoCategoryList;
        }

        public void setLevelTwoCategoryList(List<LevelTwoCategory> levelTwoCategoryList) {
            this.levelTwoCategoryList = levelTwoCategoryList;
        }

        public String getUniqueId() {
            return key + UID_SEPARATOR_SYMBOL + value;
        }

        public Option() {}

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
            dest.writeString(this.hexColor);
            dest.writeString(this.metric);
            dest.writeString(this.totalData);
            dest.writeString(this.keyMin);
            dest.writeString(this.keyMax);
            dest.writeString(this.valMin);
            dest.writeString(this.valMax);
            dest.writeString(this.iconUrl);
            dest.writeByte(this.isPopular ? (byte) 1 : (byte) 0);
            dest.writeTypedList(this.levelTwoCategoryList);
            dest.writeString(this.inputState);
        }

        protected Option(Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
            this.hexColor = in.readString();
            this.metric = in.readString();
            this.totalData = in.readString();
            this.keyMin = in.readString();
            this.keyMax = in.readString();
            this.valMin = in.readString();
            this.valMax = in.readString();
            this.iconUrl = in.readString();
            this.isPopular = in.readByte() != 0;
            this.levelTwoCategoryList = in.createTypedArrayList(LevelTwoCategory.CREATOR);
            this.inputState = in.readString();
        }

        public static final Creator<Option> CREATOR = new Creator<Option>() {
            @Override
            public Option createFromParcel(Parcel source) {
                return new Option(source);
            }

            @Override
            public Option[] newArray(int size) {
                return new Option[size];
            }
        };
    }

    public static class LevelTwoCategory implements Parcelable {

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;
        @SerializedName("total_data")
        @Expose
        String totalData;
        @SerializedName("child")
        @Expose
        List<LevelThreeCategory> levelThreeCategoryList;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

        public String getTotalData() {
            return totalData;
        }

        public void setTotalData(String totalData) {
            this.totalData = totalData;
        }

        public List<LevelThreeCategory> getLevelThreeCategoryList() {
            return levelThreeCategoryList;
        }

        public void setLevelThreeCategoryList(List<LevelThreeCategory> levelThreeCategoryList) {
            this.levelThreeCategoryList = levelThreeCategoryList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
            dest.writeString(this.totalData);
            dest.writeTypedList(this.levelThreeCategoryList);
        }

        public LevelTwoCategory() {
        }

        protected LevelTwoCategory(Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
            this.totalData = in.readString();
            this.levelThreeCategoryList = in.createTypedArrayList(LevelThreeCategory.CREATOR);
        }

        public static final Creator<LevelTwoCategory> CREATOR = new Creator<LevelTwoCategory>() {
            @Override
            public LevelTwoCategory createFromParcel(Parcel source) {
                return new LevelTwoCategory(source);
            }

            @Override
            public LevelTwoCategory[] newArray(int size) {
                return new LevelTwoCategory[size];
            }
        };
    }

    public static class LevelThreeCategory implements Parcelable {

        @SerializedName("name")
        @Expose
        String name;
        @SerializedName("key")
        @Expose
        String key;
        @SerializedName("value")
        @Expose
        String value;
        @SerializedName("input_type")
        @Expose
        String inputType;
        @SerializedName("total_data")
        @Expose
        String totalData;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The inputType
         */
        public String getInputType() {
            return inputType;
        }

        /**
         * @param inputType The input_type
         */
        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

        public String getTotalData() {
            return totalData;
        }

        public void setTotalData(String totalData) {
            this.totalData = totalData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.inputType);
            dest.writeString(this.totalData);
        }

        public LevelThreeCategory() {
        }

        protected LevelThreeCategory(Parcel in) {
            this.name = in.readString();
            this.key = in.readString();
            this.value = in.readString();
            this.inputType = in.readString();
            this.totalData = in.readString();
        }

        public static final Creator<LevelThreeCategory> CREATOR = new Creator<LevelThreeCategory>() {
            @Override
            public LevelThreeCategory createFromParcel(Parcel source) {
                return new LevelThreeCategory(source);
            }

            @Override
            public LevelThreeCategory[] newArray(int size) {
                return new LevelThreeCategory[size];
            }
        };
    }
}
