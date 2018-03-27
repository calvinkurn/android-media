package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.UserMessage;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.imageannouncement.AdminImagePojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.Option;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.StatisticOption;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale.Product;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale.UpcomingSprintSalePojo;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessagesMapper {

    private static final String IMAGE = "image";

    private static final int DEFAULT_NO_POLL = 0;
    private static final String OPTION_TEXT = "Text";
    private static final String OPTION_IMAGE = "Image";


    @Inject
    public GroupChatMessagesMapper() {
    }

    public List<Visitable> map(List<BaseMessage> list) {
        List<Visitable> listViewModel = new ArrayList<>();
        for (BaseMessage message : list) {
            Visitable mappedMessage = mapMessage(message);
            if (mappedMessage != null
                    && !shouldHideMessage(mappedMessage))
                listViewModel.add(mapMessage(message));
        }
        return listViewModel;
    }

    private boolean shouldHideMessage(Visitable mappedMessage) {
        return mappedMessage instanceof VoteAnnouncementViewModel
                && (((VoteAnnouncementViewModel) mappedMessage).getVoteType().equals
                (VoteAnnouncementViewModel.POLLING_CANCEL)
                || ((VoteAnnouncementViewModel) mappedMessage).getVoteType().equals
                (VoteAnnouncementViewModel.POLLING_UPDATE));
    }

    private Visitable mapMessage(BaseMessage message) {
        try {
            if (message instanceof UserMessage) {
                return mapToUserMessage((UserMessage) message);
//        } else if (message instanceof AdminMessage) {
//            return mapToAnnouncement((AdminMessage) message);
//        } else if (message instanceof FileMessage
//                && ((FileMessage) message).getType().toLowerCase().contains(IMAGE)
//                && !TextUtils.isEmpty(((FileMessage) message).getUrl())) {
//            return mapToImageMessage((FileMessage) message);
            } else {
                return null;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Visitable mapToImageMessage(FileMessage message) {
        return new ImageAnnouncementViewModel(
                message.getUrl(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                false,
                "",
                false);
    }

    private Visitable mapToUserMessage(UserMessage message) {
        switch (message.getCustomType()) {
            case VoteAnnouncementViewModel.POLLING_START:
            case VoteAnnouncementViewModel.POLLING_FINISHED:
            case VoteAnnouncementViewModel.POLLING_CANCEL:
            case VoteAnnouncementViewModel.POLLING_UPDATE:
                return mapToPollingViewModel(message,
                        message.getData());
            case ChatViewModel.ADMIN_MESSAGE:
                return mapToAdminChat(message, message.getData());
            case ImageAnnouncementViewModel.ADMIN_ANNOUNCEMENT:
                return mapToAdminImageChat(message, message.getData());
            case SprintSaleAnnouncementViewModel.SPRINT_SALE:
                return mapToSprintSale(message, message.getData());
            default:
                return mapToUserChat(message);
        }
    }

    private Visitable mapToSprintSale(UserMessage message, String json) {
        //TODO ADD CANVIBRATE
        Gson gson = new Gson();
        UpcomingSprintSalePojo pojo = gson.fromJson(json, UpcomingSprintSalePojo.class);

        return new SprintSaleAnnouncementViewModel(
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                true,
                "",
                mapToListFlashSaleProducts(pojo.getUpcomingFlashsale().getProducts()),
                true,
                pojo.getUpcomingFlashsale().getCampaignName(),
                pojo.getUpcomingFlashsale().getStartDate(),
                pojo.getUpcomingFlashsale().getEndDate(),
                true

        );
    }

    private ArrayList<SprintSaleProductViewModel> mapToListFlashSaleProducts(List<Product> pojo) {
        ArrayList<SprintSaleProductViewModel> list = new ArrayList<>();
        for (Product product : pojo) {
            list.add(mapToFlashSaleProduct(product));
        }
        return list;
    }

    private SprintSaleProductViewModel mapToFlashSaleProduct(Product product) {
        return new SprintSaleProductViewModel(
                product.getName(),
                product.getImageUrl(),
                product.getDiscountPercentage(),
                product.getDiscountedPrice(),
                product.getOriginalPrice(),
                80,
                "Sudah mau habis");
    }

    private Visitable mapToUserChat(UserMessage message) {
        return new ChatViewModel(
                message.getMessage(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                false,
                false
        );
    }

    private Visitable mapToAdminImageChat(UserMessage message, String json) {
        //TODO ADD CANVIBRATE
        Gson gson = new Gson();
        AdminImagePojo pojo = gson.fromJson(json, AdminImagePojo.class);

        return new ImageAnnouncementViewModel(
                pojo.getImageUrl().trim(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                true,
                pojo.getRedirectUrl(),
                true);
    }

    private Visitable mapToAdminChat(UserMessage message, String data) {
        //TODO ADD CANVIBRATE
        return new ChatViewModel(
                message.getMessage(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                true,
                false
        );
    }

    private VoteAnnouncementViewModel mapToPollingViewModel(UserMessage message, String json) {
        //TODO ADD CANVIBRATE
        Gson gson = new Gson();
        ActivePollPojo pojo = gson.fromJson(json, ActivePollPojo.class);

        return new VoteAnnouncementViewModel(
                pojo.getDescription(),
                message.getCustomType(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                true,
                mappingToVoteInfoViewModel(pojo),
                true
        );
    }


    private boolean hasPoll(ActivePollPojo activePoll) {
        return activePoll != null
                && activePoll.getStatistic() != null
                && activePoll.getPollId() != DEFAULT_NO_POLL;
    }

    private VoteInfoViewModel mappingToVoteInfoViewModel(ActivePollPojo activePollPojo) {
        if (hasPoll(activePollPojo)) {
            return new VoteInfoViewModel(
                    String.valueOf(activePollPojo.getPollId()),
                    activePollPojo.getTitle(),
                    activePollPojo.getQuestion(),
                    mapToListOptions(activePollPojo.isIsAnswered(),
                            activePollPojo.getOptionType(),
                            activePollPojo.getStatistic().getStatisticOptions(),
                            activePollPojo.getOptions()),
                    String.valueOf(activePollPojo.getStatistic().getTotalVoter()),
                    activePollPojo.getPollType(),
                    getVoteOptionType(activePollPojo.getOptionType()),
                    activePollPojo.getStatus(),
                    activePollPojo.getStatusId(),
                    activePollPojo.isIsAnswered(),
                    VoteInfoViewModel.getStringVoteInfo(activePollPojo.getPollTypeId()),
                    activePollPojo.getWinnerUrl().trim(),
                    activePollPojo.getStartTime(),
                    activePollPojo.getEndTime()
            );
        } else {
            return null;
        }
    }


    private List<Visitable> mapToListOptions(boolean isAnswered, String optionType,
                                             List<StatisticOption> statisticOptions,
                                             List<Option> options) {
        List<Visitable> list = new ArrayList<>();
        for (int i = 0; i < statisticOptions.size(); i++) {

            StatisticOption statisticOptionPojo = statisticOptions.get(i);
            Option optionPojo = options.get(i);

            if (optionType.equalsIgnoreCase(OPTION_TEXT)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),
                        statisticOptionPojo.getOption(),
                        String.valueOf(Math.floor(statisticOptionPojo.getPercentage())),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            } else if (optionType.equalsIgnoreCase(OPTION_IMAGE)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),

                        statisticOptionPojo.getOption(),
                        optionPojo.getImageOption().trim(),
                        String.valueOf(Math.floor(statisticOptionPojo.getPercentage())),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            }

        }

        return list;
    }


    private int checkIfSelected(boolean isAnswered, boolean isSelected) {
        if (isAnswered && isSelected) {
            return VoteViewModel.SELECTED;
        } else if (isAnswered) {
            return VoteViewModel.UNSELECTED;
        } else {
            return VoteViewModel.DEFAULT;
        }
    }

    private String getVoteOptionType(String type) {
        if (type.equalsIgnoreCase(OPTION_IMAGE)) {
            return VoteViewModel.IMAGE_TYPE;
        }
        return VoteViewModel.BAR_TYPE;
    }

    private AdminAnnouncementViewModel mapToAnnouncement(AdminMessage message) {
        return new AdminAnnouncementViewModel(
                message.getMessage(),
                message.getCreatedAt(),
                message.getCreatedAt(),
                String.valueOf(message.getMessageId())
        );
    }

    public Visitable map(BaseMessage baseMessage) {
        return mapMessage(baseMessage);
    }
}
