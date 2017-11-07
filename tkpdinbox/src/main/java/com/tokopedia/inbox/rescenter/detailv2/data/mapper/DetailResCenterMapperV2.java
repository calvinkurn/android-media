package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterLastShipping;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2.DetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.LastSolutionResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionDetailStepResponse;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat.NextActionResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.AwbAttachmentDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ShippingDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastSolutionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yfsx on 07/11/17.
 */

public class DetailResCenterMapperV2 implements Func1<Response<TkpdResponse>, DetailResCenter> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Override
    public DetailResCenter call(Response<TkpdResponse> response) {
        DetailResponse detailResponse = response.body().convertDataObj(
                DetailResponse.class);
        DetailResCenter model = mappingResponse(detailResponse);
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                } else {
                    model.setSuccess(true);
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }

    public DetailResCenter mappingResponse(DetailResponse detailResponse) {
        return new DetailResCenter(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                detailResponse.getNextAction() != null ? mappingNextActionDomain(detailResponse.getNextAction()) : null);
    }
    private NextActionDomain mappingNextActionDomain(NextActionResponse response) {
        return new NextActionDomain(response.getLast(),
                response.getDetail() != null ?
                        mappingNextActionDetailDomain(response.getDetail()) :
                        null);
    }

    private NextActionDetailDomain mappingNextActionDetailDomain(NextActionDetailResponse response) {
        return new NextActionDetailDomain(response.getSolution(),
                response.getLast() != null ?
                        mappingLastDomain(response.getLast()) :
                        null,
                response.getStep() != null ?
                        mappingNextActionDetailStepDomainList(response.getStep()) :
                        null);
    }

    private LastDomain mappingLastDomain(LastResponse response) {
        return new LastDomain(response.getSolution() != null ?
                mappingLastSolutionDomain(response.getSolution()) :
                null,
                response.getProblem());
    }
    private List<NextActionDetailStepDomain> mappingNextActionDetailStepDomainList(
            List<NextActionDetailStepResponse> responseList) {
        List<NextActionDetailStepDomain> domain = new ArrayList<>();
        for (NextActionDetailStepResponse response : responseList) {
            domain.add(new NextActionDetailStepDomain(response.getStatus(), response.getName()));
        }
        return domain;
    }

    private LastSolutionDomain mappingLastSolutionDomain(LastSolutionResponse response) {
        return new LastSolutionDomain(response.getId(), response.getName(), response.getAmount());
    }

    private List<AwbAttachmentDomainModel> mappingShippingAttacment(List<DetailResCenterLastShipping.Attachments> attachmentList) {
        if (attachmentList == null || attachmentList.isEmpty()) {
            return null;
        }

        List<AwbAttachmentDomainModel> viewModels = new ArrayList<>();
        for (DetailResCenterLastShipping.Attachments items : attachmentList) {
            AwbAttachmentDomainModel viewModel = new AwbAttachmentDomainModel();
            viewModel.setImageUrl(items.getImageThumb());
            viewModel.setImageThumbUrl(items.getUrl());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
