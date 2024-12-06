package launcher;
import javafx.stage.Stage;
import database.DatabaseConnectionFactory;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;

import view.CustomerView;
import view.model.BookDTO;


import java.sql.Connection;
import java.util.List;

public class CustomerComponentFactory {
    private final CustomerView customerView;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private static CustomerComponentFactory instance;

    public static CustomerComponentFactory getInstance(Boolean componentForTest, Stage stage) {
        if (instance == null) {
            instance = new CustomerComponentFactory(componentForTest, stage);
        }
        return instance;
    }


    private CustomerComponentFactory(Boolean componentsForTest, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.customerView = new CustomerView(stage, bookDTOs);
    }



    public CustomerView getCustomerView() {
        return customerView;
    }

    public BookService getBookService() {
        return bookService;
    }

}

