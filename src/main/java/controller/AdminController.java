package controller;

import com.itextpdf.layout.properties.TextAlignment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import database.Constants;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import mapper.UserMapper;
import model.Order;
import model.Right;
import model.validator.Notification;
import service.admin.AdminService;
import service.order.OrderService;
import service.user.AuthenticationService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;
import model.Role;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController
{
    private final AdminView adminView;
    private final AdminService adminService;
    private final AuthenticationService authenticationService;

    private final OrderService orderService;

    //constructor
    public AdminController(AdminView adminView, AdminService adminService, AuthenticationService authenticationService, OrderService orderService) {
        this.adminView = adminView;
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.orderService = orderService;

        // listeners pentru butoane
        this.adminView.addSaveButtonListener(new SaveButtonListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
        this.adminView.addGenerateReportListener(new GenerateReportListener());
        this.adminView.addViewBooksButtonListener(new ViewBooksButtonListener());
        this.adminView.addSelectionTableListener(new SelectionTableListener());
        this.adminView.addUpgradeButtonListener(new UpgradeListener());
    }


    private void refreshTable() {
        List<UserDTO> allUsers = adminService.findAll()
                .stream()
                .map(user -> new UserDTOBuilder()
                        .setUsername(user.getUsername())
                        .setRole(user.getRoles().stream()
                                .map(Role::getRole)
                                .collect(Collectors.joining(", ")))
                        .build())
                .toList();
        adminView.getUsersObservableList().setAll(allUsers);
        adminView.getUserTableView().refresh();
    }


    //functia de adaugare angajati
    private class SaveButtonListener implements EventHandler<ActionEvent>{
        public void handle (ActionEvent event){
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            //valideaza input-urile si inregistreaza angajatii folosind functia din suthenticationService
            Notification<Boolean> registerNotification = authenticationService.registerEmployee(username, password);

            if (registerNotification.hasErrors()) {
                adminView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                adminView.setActionTargetText("Register successful!");
            }
            refreshTable();
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            UserDTO userDTO = (UserDTO) adminView.getUserTableView().getSelectionModel().getSelectedItem();

            if(userDTO != null)
            {//apelam fucntia de delete din adminService pentru a-l sterge din baza de date
                boolean selectedUser = adminService.delete(UserMapper.convertUserDTOToUser(userDTO));
                //daca s-a selectat un user
                if(selectedUser){
                    adminView.removeUserFromObservableList(userDTO);
                } else{
                    adminView.displayAlertMessage("Deletion failed", "Error", "Couldn't delete user");
                }
            }else{
                //nu s-a selectat un user
                adminView.displayAlertMessage("Deletion failed", "Error", "User not selected");
            }
        }
    }


    private class ViewBooksButtonListener implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
        }

    }

    private class SelectionTableListener implements ChangeListener<UserDTO>{
        @Override
        public void changed(ObservableValue<? extends UserDTO>observable, UserDTO oldValue, UserDTO newValue){
            if(newValue != null)
            {
                System.out.println("Selectet user: " + newValue.getUsername());
            }else{
                System.out.println("No user selected!");
            }
        }
    }



    private class GenerateReportListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            try {
                //ia comenzile din luna precedenta
                List<Order> orders = orderService.getOrders();

                //daca nu s-au gasit comenzi
                if (orders.isEmpty()) {
                    adminView.displayAlertMessage("No data available", "Error", "No orders ");
                    return;
                }

                // altfel genereaze un report
                generatePdfReport(orders);

                adminView.displayAlertMessage("Report Generated", "PDF Report", "Sales report done.");
            } catch (Exception e) {
                e.printStackTrace();
                adminView.displayAlertMessage("Report not generated", " Error", "An error occurred while generating the report.");
            }
        }

        private void generatePdfReport(List<Order> orders) throws Exception {
            // Creeaza un pdfwriter pentru a scrie fisierul pdf
            String fileName = "Sales_Report.pdf";
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            PdfFont helveticaBold = PdfFontFactory.createFont(StandardFonts.COURIER);
            document.add(new Paragraph("Sales Report for Last Month")
                    .setFont(helveticaBold)
                    .setFontSize(24));

            // creeaza tabel pt datele din raport
            Table table = new Table(4);
            table.addCell("Order ID");
            table.addCell("Book Title");
           // table.addCell("Seller Name");
            table.addCell("Quantity");
            table.addCell("Total Price");

            // Adaugă fiecare comandă în tabel
            for (Order order : orders) {
                table.addCell(String.valueOf(order.getId()));
                table.addCell(order.getBookTitle());
               // table.addCell(order.getSellerName());
                table.addCell(String.valueOf(order.getQuantity()));
                table.addCell(String.valueOf(order.getTotalPrice()));
            }

            document.add(table);
            document.close();
        }


    }

    private class UpgradeListener  implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            UserDTO userDTO = (UserDTO) adminView.getUserTableView().getSelectionModel().getSelectedItem();
            if (userDTO == null) {
                adminView.displayAlertMessage("No user selected", "Error", "Select an user");
                return;
            }
            // se verifica daca user-ul este angajat
            if (!"EMPLOYEE".equalsIgnoreCase(userDTO.getRole())) {
                adminView.displayAlertMessage("User is not an eployee", "Error", "The user has to be an employee first.");
                return;
            }
            userDTO.setRole("admin");

            //modificam si lista de drepturi
            List<Right> adminRights = new ArrayList<>();
            for (String rightName : Constants.Rights.RIGHTS) {
                adminRights.add(new Right(null, rightName));
            }

            // Cream un nou rol de admin
            Role role = new Role(1L, "admin", adminRights);
            boolean updateSuccessful = adminService.updateUserRole(userDTO.getUsername(), role);
            if (updateSuccessful) {
                adminView.getUserTableView().refresh();
                adminView.displayAlertMessage("Employee upgraded", "SUCCESS", "This employee upgraded to admin status");
            } else {
                adminView.displayAlertMessage("The user couldn't be upgraded in the database", "Error", "Database error");
            }
        }
    }


}
