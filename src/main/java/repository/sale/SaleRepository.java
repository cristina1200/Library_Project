package repository.sale;

import model.Sale;

import java.util.List;

public interface SaleRepository {
    boolean save (Sale sale);
    List<Sale> findAll();
}
