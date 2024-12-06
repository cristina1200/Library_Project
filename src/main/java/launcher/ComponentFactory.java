package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
//import javafx.stage.Stage;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.sale.SaleRepository;
import repository.sale.SaleRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.util.*;
import java.sql.Connection;
public class ComponentFactory {
    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final SaleService saleService;
    private final SaleRepository saleRepository;

    private static volatile ComponentFactory instance;

//constructor
    private ComponentFactory(Boolean componentsForTest, Stage primaryStage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, bookDTOs);

        this.saleRepository = new SaleRepositoryMySQL(connection);
        this.saleService = new SaleServiceImpl(saleRepository);
        this.bookController = new BookController(bookView, bookService,saleService);


    }


    public static ComponentFactory getInstance(Boolean componentForTest, Stage primaryStage) {
        if (instance == null) {
            synchronized (ComponentFactory.class) {
                if (instance == null) {
                    instance = new ComponentFactory(componentForTest, primaryStage);
                }
            }
        }
        return instance;
    }


}