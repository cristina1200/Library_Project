package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import mapper.BookMapper;
import model.Sale;
import service.book.BookService;
import service.sale.SaleService;
import service.user.CurrentUserService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final SaleService saleService;

    //constructor
    public BookController(BookView bookView, BookService bookService, SaleService saleService) {
        this.bookView = bookView;
        this.bookService = bookService;
        this.saleService = saleService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addSelectionTableListener(new SelectionTableListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
    }
    private void refreshTableData() {
        List<BookDTO> allBooks = bookService.findAll().stream()
                .map(BookMapper::convertBookToBookDTO)
                .toList();

        bookView.getBooksObservableList().setAll(allBooks);
        bookView.getBookTableView().refresh();
    }


    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String price=bookView.getPrice();
            String stock=bookView.getStock();

            //ne asiguram ca title si author sunt comletate
            if (title.isEmpty() || author.isEmpty()){
                bookView.displayAlertMessage("Empty fields", "Error", "Please fill in all fields.");
                bookView.getBooksObservableList().get(0).setTitle("No Name");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setAuthor(author)
                        .setTitle(title)
                        .setPrice(Double.parseDouble(price))  // ne asiguram ca valoarea este valida
                        .setStock(Long.parseLong(stock))  //ne asiguram ca valoarea este valida
                        .build();

                //convertim inputul intr-un obiect bookdto
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook) {
                    bookView.displayAlertMessage("Save Successful", "Book Added", "Book was successfully added to the database.");
                    bookView.addBookToObservableList(bookDTO);//adaugam in tabel
                } else {
                    bookView.displayAlertMessage("Save Not Successful", "Book was not added", "There was a problem at adding the book into the database.");
                }
            }
        }
    }

    private class SelectionTableListener implements ChangeListener{

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            BookDTO selectedBookDTO = (BookDTO) newValue;
            System.out.println("Book Author: " + selectedBookDTO.getAuthor() + " Title: " + selectedBookDTO.getTitle() + " Price: " + selectedBookDTO.getPrice() + " Stock: " + selectedBookDTO.getStock());
        }
    }


    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null){
                //stergem cartea din baza de date
                boolean deletionSuccessfull = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessfull){
                    //stergem din observable
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.displayAlertMessage("Deletion not successful", "Deletion Process", "There was a problem in the deletion process. Please restart the application and try again!");
                }
            } else {
                bookView.displayAlertMessage("Deletion not successful", "Deletion Process", "You need to select a row from table before pressing the delete button!");
            }
        }
    }


    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                TextInputDialog quantityDialog = new TextInputDialog();
                quantityDialog.setTitle("Sell Book");
                quantityDialog.setHeaderText("Enter quantity: " + bookDTO.getTitle());
                quantityDialog.setContentText("Quantity:");

                String title = bookDTO.getTitle();
                String author = bookDTO.getAuthor();
                double price = bookDTO.getPrice();
                long stock = bookDTO.getStock();

                quantityDialog.showAndWait().ifPresent(quantityText -> {
                    try {
                        //il pune pe user sa adauge o cantitate
                        int quantity = Integer.parseInt(quantityText);

                        if (quantity <= 0) {
                            bookView.displayAlertMessage("Invalid Quantity", "Error", "The quantity must be a positive number.");
                            return;
                        }

                        if (quantity > stock) {
                            bookView.displayAlertMessage("Insufficient Stock", "Error", "Not enough stock available");
                            return;
                        }
                        //calculeaza pretul
                        double totalPrice = price * quantity;

                        //reactualizam stocul
                        bookDTO.setStock(bookDTO.getStock() - quantity);

                        boolean success = bookService.update(BookMapper.convertBookDTOToBook(bookDTO));
                        System.out.println(String.format("New stock: %d", stock - quantity));

                        refreshTableData();
                        if (success) {
                            //salvam comanda
                            Sale sale = new Sale(null, title, CurrentUserService.getCurrentUsername(), quantity, totalPrice, LocalDateTime.now());
                            boolean saleSaved = saleService.save(sale);

                            bookView.displayAlertMessage("Sale Successful", "Book Sold",
                                    "Successfully sold " + quantity + " copies of " + bookDTO.getTitle());
                        }
                    }catch (NumberFormatException e) {
                        bookView.displayAlertMessage("Invalid Input", "Error",
                                "Please enter a valid number for the quantity.");
                    }
                });
            } else {
                bookView.displayAlertMessage("No Selection", "Sell Process",
                        "You need to select a book before pressing the Sell button.");
            }
        }
    }

}