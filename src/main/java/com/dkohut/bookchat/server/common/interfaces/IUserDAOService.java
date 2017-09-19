package com.dkohut.bookchat.server.common.interfaces;

import com.dkohut.bookchat.server.common.entity.User;

public interface IUserDAOService {
	/**
	 * This method will be getting user by given login and password
	 * and return it as object.
	 * 
	 * @param login - String type data which represent login entered by user
	 * @param password - String type data which represent password entered by user
	 * @return user - User type date which contain info about user
	 */
	User select(String login, String password);
	
	/**
	 * This method create a new user in database and return response 
	 * about success of transaction
	 * 
	 * @param user - User type object that contain info about user,
	 * 				 which will be written into database
	 * @return response - String type data which represent success of transaction
	 */
	String create(User user);
}
