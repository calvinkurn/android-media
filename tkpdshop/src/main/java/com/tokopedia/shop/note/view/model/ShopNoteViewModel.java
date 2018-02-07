package com.tokopedia.shop.note.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.note.view.adapter.ShopNoteTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopNoteViewModel implements Visitable<ShopNoteTypeFactory> {

    private String noteTitle;
    private String noteId;
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

    @Override
    public int type(ShopNoteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
