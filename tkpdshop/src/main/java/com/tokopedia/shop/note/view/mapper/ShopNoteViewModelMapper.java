package com.tokopedia.shop.note.view.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class ShopNoteViewModelMapper {

    @Inject
    public ShopNoteViewModelMapper() {
    }

    public List<Visitable> transform(List<ShopNote> shopNoteList) {
        List<Visitable> visitableList = new ArrayList<>();
        for (ShopNote shopNote : shopNoteList) {
            ShopNoteViewModel shopNoteViewModel = new ShopNoteViewModel();
            shopNoteViewModel.setNoteId(shopNote.getNoteId());
            shopNoteViewModel.setNoteStatus(shopNote.getNoteStatus());
            shopNoteViewModel.setNoteTitle(shopNote.getNoteTitle());
            visitableList.add(shopNoteViewModel);
        }
        return visitableList;
    }
}
