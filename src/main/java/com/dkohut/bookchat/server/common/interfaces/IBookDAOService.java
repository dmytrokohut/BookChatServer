package com.dkohut.bookchat.server.common.interfaces;

import com.dkohut.bookchat.server.common.entity.Book;

/**
 * This interface defines a method for work with database
 * 
 * @author Dmytro Kohut
 *
 */
public interface IBookDAOService {
	/**
	 * This method create new book in database
	 * 
	 * @param book - object of Book class which contain info about book, 
	 * 				 which will be written into database
	 * @return response - String type data that represent success of transaction
	 */
	String create(Book book);
	
	/**
	 * This method search book in database by given title
	 * 
	 * @param title - String type data which contain a title of book, 
	 * 				  used for searching in database
	 * @return response - String type data that represent success of transaction
	 */
	Book select(String title);
	
	
	/**
	 * This method delete book in database book with given id
	 * 
	 * @param id - Integer type field which represent given id of book
	 * @return response - String type field that represent success of transaction
	 */
	String delete(Integer id);
}
