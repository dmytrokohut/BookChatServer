package com.dkohut.bookchat.server.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.dkohut.bookchat.server.common.interfaces.IUserDAOService;

import io.grpc.stub.StreamObserver;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;

import com.dkohut.bookchat.server.common.entity.ResponseMessage;
import com.dkohut.bookchat.server.common.entity.User;
import com.dkohut.bookchat.server.common.entity.UserServiceGrpc;
import com.dkohut.bookchat.server.common.entity.ResponseEnum;
import com.dkohut.bookchat.server.common.entity.AccessEnum;
import com.dkohut.bookchat.server.common.entity.AccessInfo;
import com.dkohut.bookchat.server.common.entity.LoginMessage;

/**
 * This class control activity related to registration and login of users
 * 
 * @author Dmytro Kohut
 *
 */
@GRpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
	
	@Autowired
	private IUserDAOService userDAOService;
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
	
	/**
	 * This method get a new User-object and give it to the DAO-method 
	 * that create new instance of User in database
	 * 
	 * @param user - Object of User class, contain info about new user that you want to register
	 * @return responseObserver - message which contain info about success of transaction
	 */
	@Override
	public void registration(User user, StreamObserver<ResponseMessage> responseObserver) {
		String responseDAO = userDAOService.create(user);
		
		if(!responseDAO.isEmpty()) {
			responseObserver.onNext(ResponseMessage.newBuilder()
					.setResponse(ResponseEnum.SUCCESS)
					.build());
			
			LOGGER.info("User was created.");
		} else {
			responseObserver.onNext(ResponseMessage.newBuilder()
					.setResponse(ResponseEnum.ERROR)
					.build());
			
			LOGGER.info("User creation has failed.");
		}
		
		responseObserver.onError(null);
		responseObserver.onCompleted();		
	}
	
	/**
	 * This method get login and password from client and give it to the select method 
	 * that return User-object if user with such login and password exists and null if
	 * such user does't exists
	 * 
	 * @param login - String type object that contain login of user
	 * @param password - String type object that contain password of user
	 * 
	 * @return responseObserver - message that contain info whether access granted or access denied
	 */
	@Override
	public void login(LoginMessage request, StreamObserver<AccessInfo> responseObserver) {
		User user = userDAOService.select(request.getLogin(), request.getPassword());
		
		if(user.getLogin() != null && user.getPassword() != null) {
			responseObserver.onNext(AccessInfo.newBuilder()
					.setAccess(AccessEnum.ACCESS_GRANTED)
					.build());
				
			LOGGER.info("User with login=" + request.getLogin() + " and password=" + request.getPassword()
						 + " is allowed access.");
		} else {
			responseObserver.onNext(AccessInfo.newBuilder()
					.setAccess(AccessEnum.ACCESS_DENIED)
					.build());
			
			LOGGER.info("User access denied, the request login=" + request.getLogin() + ", password="
						 + request.getPassword());
		}
		
		responseObserver.onError(null);
		responseObserver.onCompleted();
	}
	
}
