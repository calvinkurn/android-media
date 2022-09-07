//package com.tokopedia.sellerapp.domain
//
//import com.google.android.gms.wearable.MessageClient
//import com.google.android.gms.wearable.NodeClient
//import com.tokopedia.sellerapp.data.repository.OrderRepository
//import com.tokopedia.sellerapp.util.Action
//import javax.inject.Inject
//
//class OrderUseCase @Inject constructor(
//    private val orderRepository: OrderRepository
//) {
//    suspend fun sendMessagesToNodes(
//        action: Action,
//        nodeClient: NodeClient,
//        messageClient: MessageClient
//    ){
//        orderRepository.sendMessagesToNodes(action, nodeClient, messageClient)
//    }
//}