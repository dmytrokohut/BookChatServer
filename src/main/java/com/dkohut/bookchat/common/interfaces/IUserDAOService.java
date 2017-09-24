package com.dkohut.bookchat.common.interfaces;

import com.dkohut.bookchat.common.entity.ResponseEnum;
import com.dkohut.bookchat.common.entity.User;

/**
 * This interface defines a methods which will be using for work with database
 * 
 * @author Dmytro Kohut
 *
 */
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
	ResponseEnum create(User user);
}
