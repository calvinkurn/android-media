package com.tokopedia.inbox.inboxchat.chatroom.viewmodel.productattachment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.DummyChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.SendableViewModel;

import java.util.Date;

/**
 * @author by nisie on 5/14/18.
 */
public class ProductAttachmentViewModel extends SendableViewModel implements Visitable<ChatRoomTypeFactory> {

    private Integer productId;
    private String productName;
    private String productPrice;
    private Long dateTimeInMilis;
    private String productUrl;
    private String productImage;

    /**
     * Constructor for API response.
     * {@link GetReplyMapper}
     * {@link GetReplyListUseCase}
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param isRead         is message already read by opponent
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     */
    public ProductAttachmentViewModel(String messageId, String fromUid,
                                      String from, String fromRole,
                                      String attachmentId, String attachmentType,
                                      String replyTime, boolean isRead,
                                      Integer productId, String productName,
                                      String productPrice, String productUrl,
                                      String productImage, boolean isSender, String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                "", isRead, false, isSender, message);
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.dateTimeInMilis = Long.parseLong(replyTime);
        this.productUrl = productUrl;
        this.productImage = productImage;
    }

    /**
     * Constructor for WebSocket. {@link WebSocketMapper}
     *
     * @param messageId      message Id
     * @param fromUid        user id of sender
     * @param from           username of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param productId      product id
     * @param productName    product name
     * @param productPrice   product price
     * @param productUrl     product url
     * @param productImage   product image url
     * @param startTime
     */
    public ProductAttachmentViewModel(String messageId, String fromUid,
                                      String from, String fromRole,
                                      String attachmentId, String attachmentType,
                                      String replyTime, Integer productId,
                                      String productName, String productPrice,
                                      String productUrl, String productImage,
                                      boolean isSender, String message, String startTime) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                startTime, false, false, isSender, message);
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.dateTimeInMilis = Long.parseLong(replyTime);
        this.productUrl = productUrl;
        this.productImage = productImage;
    }

    /**
     * Constructor for sending product attachment.
     *
     * @param loginID      current user id.
     * @param productId    product id
     * @param productName  product name
     * @param productPrice product price
     * @param productUrl   product url
     * @param productImage product image url
     * @param startTime    send time to validate dummy mesages.
     */
    public ProductAttachmentViewModel(String loginID, Integer productId, String productName,
                                      String productPrice, String productUrl,
                                      String productImage, String startTime) {
        super("", loginID, "", "", "",
                WebSocketMapper.TYPE_PRODUCT_ATTACHMENT, DummyChatViewModel.SENDING_TEXT,
                startTime, false, true, true, productUrl);
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.dateTimeInMilis = new Date().getTime();
        this.productUrl = productUrl;
        this.productImage = productImage;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public Long getDateTimeInMilis() {
        return dateTimeInMilis;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImage() {
        return productImage;
    }

}
