package com.tokopedia.tkpdstream.vote.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.tkpdstream.vote.view.model.VoteViewModel.IMAGE_TYPE;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class GetVoteMapper implements Func1<Response<DataResponse>, VoteInfoViewModel>{

    @Inject
    public GetVoteMapper() {
    }

//    @Override
//    public VoteInfoViewModel call(Response<DataResponse<List<VoteInfoPojo>>> dataResponseResponse) {
//
//        List<Visitable> list = new ArrayList<>();
//        String title = "Title";
//        for (int i = 0; i < 10; i++) {
//            VoteViewModel channelViewModel = new VoteViewModel("Option "+i, 10, VoteViewModel.DEFAULT);
//            list.add(channelViewModel);
//        }
//
//        return new VoteInfoViewModel(title, list, 100);
//    }

    @Override
    public VoteInfoViewModel call(Response<DataResponse> dataResponseResponse) {

        String voteType = IMAGE_TYPE;
        List<Visitable> list = new ArrayList<>();
        String title = "Menurut Toppers, siapa ya yang akan jadi pemain MVP di Piala Dunia Tahun Ini?";
        String cr7 = "http://01a4b5.medialib.edu.glogster.com/media/55/5527aa424a7bc417e364f92537e4daa0f366ab6a2373dfa8616f8977f7b9c685/cristiano-ronaldo-portual-goal.jpg";
        String messi = "https://static.independent.co.uk/s3fs-public/styles/article_small/public/thumbnails/image/2014/07/09/23/10-messi.jpg";
        VoteViewModel channelViewModel = new VoteViewModel("Cristiano Ronaldo", cr7,40, VoteViewModel.DEFAULT, voteType);
        list.add(channelViewModel);
        channelViewModel = new VoteViewModel("Lionel Messi", messi, 60, VoteViewModel.DEFAULT, voteType);
        list.add(channelViewModel);

        return new VoteInfoViewModel("1234", title, list, "1000", voteType
                , "Vote", false, "Info Pemenang", "www.google.com"
                , 1519722000, 1519758000);
    }
}
