import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello world!");

//        Book book = new BookBuilder().setAuthor("Me")
//                .setTitle("Harry Potter")
//                .build();

        Book book_fram = new BookBuilder()
                .setTitle("Fram Ursul Polar")
                .setAuthor("Cezar Petrescu")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        System.out.println(book_fram.getAuthor());
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        //BookService bookService = new BookServiceImpl(bookRepository);

        bookRepository.save(book_fram);
        //bookRepository.save(book_fram);

        System.out.println(bookRepository.findAll());
        //System.out.println(bookRepository.findAll());

//        System.out.println("=============NEW==============");
//
//
//        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        BookRepository bookRepository = new BookRepositoryMySQL(connection);
//        BookService bookService = new BookServiceImpl(bookRepository);
//
//        Book book_fram = new BookBuilder()
//                .setTitle("Fram Ursul Polar")
//                .setAuthor("Cezar Petrescu")
//                .setPublishedDate(LocalDate.of(2010, 6, 2))
//                .build();
//        bookService.save(book_fram);
//
//        Book book = bookService.findById(1L);
//
//        System.out.println(book);






    }
}



