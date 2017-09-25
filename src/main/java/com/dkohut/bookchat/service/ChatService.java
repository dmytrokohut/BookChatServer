package com.dkohut.bookchat.service;

import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;

import com.dkohut.bookchat.common.entity.ChatMessage;
import com.dkohut.bookchat.common.entity.ChatMessageFromServer;
import com.dkohut.bookchat.common.entity.ChatServiceGrpc;

import io.grpc.stub.StreamObserver;

/**
 * This service tracking activity related to chat messages
 * 
 * @author Dmytro Kohut
 *
 */
@GRpcService
public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
	
	private static final Logger LOGGER = Logger.getLogger(ChatService.class.getName());
	
	private static LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers = new LinkedHashSet<>();
	
	/**
	 * This method get message from client and sent it to each member of chat
	 * 
	 *  @param name - String type data that contain a name of sender
	 *  @param message - String the data that contain content of message
	 *  
	 *  @return responseObserver - message that contain the same name of sender and the same content 
	 */
	@Override 
	public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver){
		 
		observers.add(responseObserver);
		LOGGER.info("New request: " + responseObserver);
		
		return new StreamObserver<ChatMessage>() {

			@Override
			public void onCompleted() {
				observers.remove(responseObserver);				
			}

			@Override
			public void onError(Throwable t) {
				observers.remove(responseObserver);
			}

			@Override
			public void onNext(ChatMessage chatMessage) {
				for(StreamObserver<ChatMessageFromServer> observer : observers) {
					observer.onNext(ChatMessageFromServer.newBuilder()
							.setName(chatMessage.getName())
							.setMessage(chatMessage.getMessage())
							.build());
				}
			}
			
		};
		
	}
	
}
