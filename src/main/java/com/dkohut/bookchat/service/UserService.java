package com.dkohut.bookchat.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.dkohut.bookchat.common.entity.LoginMessage;
import com.dkohut.bookchat.common.entity.ResponseEnum;
import com.dkohut.bookchat.common.entity.ResponseMessage;
import com.dkohut.bookchat.common.entity.User;
import com.dkohut.bookchat.common.entity.UserServiceGrpc;
import com.dkohut.bookchat.common.interfaces.IUserDAOService;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;

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
	public void registration(User user, StreamObserver<ResponseMessage> responseObserver) throws NullPointerException {
		
		LOGGER.info("New request " + responseObserver);
		
		try {
			ResponseEnum responseDAO = userDAOService.create(user);
		
			responseObserver.onNext(ResponseMessage.newBuilder()
					.setResponse(responseDAO)
					.build());
			
			LOGGER.info("User was created");			
			
			responseObserver.onCompleted();	
			
		} catch(RuntimeException e) {
			responseObserver.onError(Status.INTERNAL.asRuntimeException());
			LOGGER.info("User creation has failed");
		}
		
			
	}
	
	/**
	 * This method get login and password from client and give it to the select method 
	 * that return User-object if user with such login and password exists and exception  if
	 * such user does't exists
	 * 
	 * @param login - String type object that contain login of user
	 * @param password - String type object that contain password of user
	 * 
	 * @return responseObserver - message that contain info whether access granted or access denied
	 */
	@Override
	public void login(LoginMessage request, StreamObserver<User> responseObserver) throws NullPointerException {

		LOGGER.info("New request: " + responseObserver);
		
		try {
			User user = userDAOService.select(request.getLogin(), request.getPassword());

			responseObserver.onNext(user);
			
			LOGGER.info("Access granted for user with login=" + user.getLogin() + 
						" and password=" + user.getPassword());
		
			responseObserver.onCompleted();
		
		} catch(RuntimeException e) {
			responseObserver.onError(Status.INTERNAL.asRuntimeException());	
			LOGGER.info("Access denied for user with login=" + request.getLogin() + 
					" and password=" + request.getPassword());
		}
		
	}
	
}
