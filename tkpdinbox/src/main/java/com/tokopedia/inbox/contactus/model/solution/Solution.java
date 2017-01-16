package com.tokopedia.inbox.contactus.model.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Solution {

    @SerializedName("attachment")
    @Expose
    private int attachment;
    @SerializedName("breadcrumb")
    @Expose
    private String breadcrumb;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("note")
    @Expose
    private String note;


    /**
     *
     * @return
     *     The attachment
     */
    public boolean hasAttachment() {
        return attachment == 1;
    }
    /**
     *
     * @param attachment
     *     The attachment
     */
    public void setAttachment(int attachment) {
        this.attachment = attachment;
    }

    /**
     *
     * @return
     *     The breadcrumb
     */
    public String getBreadcrumb() {
        return breadcrumb;
    }

    /**
     *
     * @param breadcrumb
     *     The breadcrumb
     */
    public void setBreadcrumb(String breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    /**
     *
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The note
     */
    public String getNote() {
        return note;
    }

    /**
     *
     * @param note
     *     The note
     */
    public void setNote(String note) {
        this.note = note;
    }

}