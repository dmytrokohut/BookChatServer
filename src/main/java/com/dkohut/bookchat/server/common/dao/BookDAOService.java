package com.dkohut.bookchat.server.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dkohut.bookchat.server.common.entity.Book;
import com.dkohut.bookchat.server.common.interfaces.IBookDAOService;

/**
 * This class implements method defined in IBookDAOService interface
 * 
 * @author Dmytro Kohut
 *
 */
@Repository
public class BookDAOService implements IBookDAOService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String QUERY_CREATE = "INSERT INTO books(title, genre, author, publication_date, price) "
												+ "VALUES(?, ?, ?, ?, ?);";
	private static final String QUERY_SELECT = "SELECT id, title, genre, author, publication_date, price FROM books "
												+ "WHERE title = ?;";
	private static final String QUERY_DELETE = "DELETE FROM books WHERE id=?;";
	
	/**
	 * This method create new book in database
	 * 
	 * @param book - object of Book class which contain info about book, 
	 * 				 which will be written into database
	 * @return response - String type data that represent success of transaction
	 */
	@Override
	public String create(Book book) {
		jdbcTemplate.update(QUERY_CREATE, book.getTitle(), book.getGenre(), book.getAuthor(), 
							book.getPublicationDate(), book.getPrice());
		return "SUCCESS";
	}

	/**
	 * This method search book in database by given title
	 * 
	 * @param title - String type data which contain a title of book, 
	 * 				  used for searching in database
	 * @return response - String type data that represent success of transaction
	 */
	@Override
	public Book select(String title) {
		return jdbcTemplate.queryForObject(QUERY_SELECT, (resultSet, rowNum) -> {
			return Book.newBuilder()
					.setId(resultSet.getInt("id"))
					.setTitle(resultSet.getString("title"))
					.setGenre(resultSet.getString("genre"))
					.setAuthor(resultSet.getString("author"))
					.setPublicationDate(resultSet.getString("publication_date"))
					.setPrice(resultSet.getFloat("price"))
					.build();
			
		}, new Object[] {title});
	}

	/**
	 * This method delete book in database book with given id
	 * 
	 * @param id - Integer type field which represent given id of book
	 * @return response - String type field that represent success of transaction
	 */
	@Override
	public String delete(Integer id) {
		jdbcTemplate.update(QUERY_DELETE, new Object[] {id} );
		return "SUCCESS";
	}
	
}
