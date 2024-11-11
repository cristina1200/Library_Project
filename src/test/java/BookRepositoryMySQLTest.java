import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.*;
import repository.BookRepositoryMySQL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookRepositoryMySQLTest {

    private Connection connection;
    private BookRepositoryMySQL bookRepository;

    @BeforeAll
    public void setup() {
        // connection setup
        connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
    }

    @BeforeEach
    public void beforeEach() {
        bookRepository.removeAll();
    }

    @Test
    public void testSave() {
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();

        boolean saved = bookRepository.save(book);
        assertTrue(saved);


        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Ion", foundBook.get().getTitle());
    }

    @Test
    public void findAll(){
        List<Book> books = bookRepository.findAll();
        assertEquals(0, books.size());
    }

    @Test
    public void testDelete() {
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();
        bookRepository.save(book);

        boolean deleted = bookRepository.delete(book);
        assertTrue(deleted);
        assertTrue(bookRepository.findById(book.getId()).isEmpty());
    }

    @AfterAll
    public void tearDown() {
        // Close the connection if necessary
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
