package com.dkohut.bookchat.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dkohut.bookchat.common.entity.ResponseEnum;
import com.dkohut.bookchat.common.entity.User;
import com.dkohut.bookchat.common.interfaces.IUserDAOService;

/**
 * This class realizes methods defined in IUserDAOService interface 
 * 
 * @author Dmytro Kohut
 *
 */
@Repository
public class UserDAOService implements IUserDAOService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String QUERY_SELECT = "SELECT id, login, password, name, email FROM users "
												+ "WHERE login = ? AND password = ?;";
	private static final String QUERY_CREATE = "INSERT INTO users(login, password, name, email) "
												+ "VALUES(?, ?, ?, ?);";

	/**
	 * This method will be getting user by given login and password
	 * and return it as object.
	 * 
	 * @param login - String type data which represent login entered by user
	 * @param password - String type data which represent password entered by user
	 * @return user - User type date which contain info about user
	 */
	@Override
	public User select(String login, String password) {
		return jdbcTemplate.queryForObject(QUERY_SELECT, (resultSet, rowNum) -> {
			return User.newBuilder()
					.setId(resultSet.getInt("id"))
					.setLogin(resultSet.getString("login"))
					.setPassword(resultSet.getString("password"))
					.setName(resultSet.getString("name"))
					.setEmail(resultSet.getString("email"))
					.build();
			
		}, new Object[] {login, password});
	}

	/**
	 * This method create a new user in database and return response 
	 * about success of transaction
	 * 
	 * @param user - User type object that contain info about user,
	 * 				 which will be written into database
	 * @return response - String type data which represent success of transaction
	 */
	@Override
	public ResponseEnum create(User user) {
		jdbcTemplate.update(QUERY_CREATE, user.getLogin(), user.getPassword(), user.getName(), user.getEmail());
		
		return ResponseEnum.SUCCESS;
	}
	
}
