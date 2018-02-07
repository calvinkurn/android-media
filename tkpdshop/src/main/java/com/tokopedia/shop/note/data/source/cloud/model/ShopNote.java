
package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopNote {

    @SerializedName("note_title")
    @Expose
    private String noteTitle;
    @SerializedName("note_id")
    @Expose
    private String noteId;
    @SerializedName("note_status")
    @Expose
    private String noteStatus;

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

}
