package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

//adauga functionalitatea de caching
public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {
    private Cache<Book> cache;

    //constructor
    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()){
            return cache.load();
        }

        List<Book> books = decoratedBookRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {

        if (cache.hasResult()){
            return cache.load()
                    .stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedBookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }

    @Override
    public boolean updateStock(long id, int newStock) {
        cache.invalidateCache();

        boolean updated = decoratedBookRepository.updateStock(id, newStock);
        if (updated && cache.hasResult()) {

            cache.load()
                    .stream()
                    .filter(book -> book.getId().equals(id))
                    .forEach(book -> book.setStock(newStock));
        }

        return updated;
    }

    @Override
    public boolean update(Book book) {
        boolean result = decoratedBookRepository.update(book);
        if (result) {
            cache.invalidateCache();
        }
        return result;
    }
}