package com.dkohut.bookchat.service;

import org.apache.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.dkohut.bookchat.common.entity.Book;
import com.dkohut.bookchat.common.entity.BookServiceGrpc;
import com.dkohut.bookchat.common.entity.DeleteBookMessage;
import com.dkohut.bookchat.common.entity.ResponseEnum;
import com.dkohut.bookchat.common.entity.ResponseMessage;
import com.dkohut.bookchat.common.entity.SearchBookMessage;
import com.dkohut.bookchat.common.interfaces.IBookDAOService;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;;

/**
 * This service control activity related to creating, selecting and deleting of books in system
 * 
 * @author Dmytro Kohut
 *
 */
@GRpcService
public class BookService extends BookServiceGrpc.BookServiceImplBase {

	@Autowired
	private IBookDAOService bookDAOService;
	
	private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());
	
	/**
	 * This method get message with title of book and give it to select method
	 * 
	 * @param title - Sting type object that contain title of book
	 * @return book - Book type object that contain info about book which user would like to get 
	 */
	@Override
	public void searchBook(SearchBookMessage request, StreamObserver<Book> responseObserver) {
		
		try {
			Book book = bookDAOService.select(request.getTitle());
		
			responseObserver.onNext(Book.newBuilder()
					.setId(book.getId())
					.setTitle(book.getTitle())
					.setGenre(book.getGenre())
					.setAuthor(book.getAuthor())
					.setPublicationDate(book.getPublicationDate())
					.setPrice(book.getPrice())
					.build());
			
			responseObserver.onCompleted();
			
			LOGGER.info("Book with title=\"" + request.getTitle() + "\" was found.");

		} catch(RuntimeException e) {
			responseObserver.onError(Status.INTERNAL.asRuntimeException());
			LOGGER.info("Book with title=\"" + request.getTitle() + "\" was not found.");
		}	
		
	}
	
	/**
	 * This method get message with object of Book type and give it to the create method in 
	 * BookDAOService class
	 * 
	 * @param book - Book type object that contain info about book which needed to create in database
	 * @return responseObserver - String type object that cotain info about success of transaction
	 */
	@Override
	public void createBook(Book book, StreamObserver<ResponseMessage> responseObserver) {		
		
		try {
			ResponseEnum responseDAO = bookDAOService.create(book);
			
			responseObserver.onNext(ResponseMessage.newBuilder()
					.setResponse(responseDAO)
					.build());
				
				
			responseObserver.onCompleted();
				
			LOGGER.info("Book was created.");					
			
		} catch(RuntimeException e) {
			responseObserver.onError(Status.INTERNAL.asRuntimeException());
			LOGGER.info("Book creating was failed.");
		}	
	}
	
	/**
	 * This method get message with id of book and give it to the delete method in BookDAOService class
	 * 
	 * @param id - int type data that contain id of book user want to delete
	 * @return responseObserver - message with info about success of transaction
	 */
	@Override
	public void deleteBook(DeleteBookMessage request, StreamObserver<ResponseMessage> responseObserver) {
		
		
		try {
			ResponseEnum responseDAO = bookDAOService.delete(request.getId());
			
			responseObserver.onNext(ResponseMessage.newBuilder()
					.setResponse(responseDAO)
					.build());
			
			responseObserver.onCompleted();
			
			LOGGER.info("Book was successfully deleted.");			
			
		} catch(RuntimeException e) {
			responseObserver.onError(Status.INTERNAL.asRuntimeException());
			LOGGER.info("Book deleting was unsuccessful.");
		}		
	}
	
}
