package service.sale;

import model.Sale;
import repository.sale.SaleRepository;

public class SaleServiceImpl implements SaleService
{
    private final SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }
    @Override
    public boolean save(Sale sale) {
        return saleRepository.save(sale);
    }
}