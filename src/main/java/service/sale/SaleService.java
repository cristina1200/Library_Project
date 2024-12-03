package service.sale;

import model.Book;
import model.Sale;
import repository.sale.SaleRepository;

import java.time.LocalDate;

public class SaleService {
    private SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void sellBook(Book book, double price) {

        if (book != null) {
            Sale sale = new Sale(book, LocalDate.now(), price);
            saleRepository.save(sale);
         //    book.setInStock(book.getInStock() - 1);
            System.out.println("Sale completed: " + sale);
        }
    }
}