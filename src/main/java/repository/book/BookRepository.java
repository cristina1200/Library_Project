//Main book operations
package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);        //searches a book by its id. Optional avoids null checks
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll();

}
