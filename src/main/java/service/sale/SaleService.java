package service.sale;

import model.Book;
import model.Sale;
import repository.sale.SaleRepository;

import java.time.LocalDate;

public interface SaleService {
    boolean save (Sale sale);

}