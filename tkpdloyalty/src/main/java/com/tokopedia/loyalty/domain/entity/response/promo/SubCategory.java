package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class SubCategory {

    @SerializedName("term_id")
    @Expose
    private int termId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("term_group")
    @Expose
    private int termGroup;
    @SerializedName("term_taxonomy_id")
    @Expose
    private int termTaxonomyId;
    @SerializedName("taxonomy")
    @Expose
    private String taxonomy;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("parent")
    @Expose
    private int parent;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("filter")
    @Expose
    private String filter;

    public int getTermId() {
        return termId;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getTermGroup() {
        return termGroup;
    }

    public int getTermTaxonomyId() {
        return termTaxonomyId;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public String getDescription() {
        return description;
    }

    public int getParent() {
        return parent;
    }

    public int getCount() {
        return count;
    }

    public String getFilter() {
        return filter;
    }
}
