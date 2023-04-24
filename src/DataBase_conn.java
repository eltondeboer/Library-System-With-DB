import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DataBase_conn {

    public DefaultTableModel view_loans_table(User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ISBN = i.ISBN Where userID=? and Returned = 0";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher");
                String returndate = resultSet.getString("ReturnDate");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, publisher, returndate, ISBN});
            }


            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
    public DefaultTableModel view_late_userReturns_table(User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ISBN = i.ISBN Where userID=? and curdate() > l.ReturnDate";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher");
                String returndate = resultSet.getString("ReturnDate");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, publisher, returndate, ISBN});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    //Uses the stored procedure in the database to search and returns a table model with the results.
    public DefaultTableModel search_results(String search_text, JLabel txt_label) {
        txt_label.setText("Search above for title, type or clasification");

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Classification", "Publisher", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String classification = resultSet.getString("Classification");
                String publisher = resultSet.getString("Publisher");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, classification, publisher, ISBN});
            }

            if (resultSet == null) {
                txt_label.setText("No results found for: " + search_text);
            } else {
                txt_label.setText("Search results for: " + search_text);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
    //Connects to database and adds user to the database.
    public void add_user( JTextField name_tf,
                          JTextField phone_tf,
                          JTextField email_tf,
                          JPasswordField pass_pf){

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        String name = name_tf.getText();
        String phone = phone_tf.getText();
        String email = email_tf.getText();
        String password = String.valueOf(pass_pf.getPassword());

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `library`.`user` (`userName`, `userType`, `userMail`, `userPhoneNumber`, `userPass`) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "Student");
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                System.out.println("Failed to add new User");
            }
            else{
                System.out.println("User added succesfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean return_loan(JTextField return_BC_tf, User user){
        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        String barcode = return_BC_tf.getText();

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "UPDATE loan SET Returned = 1 WHERE (Barcode = ?) and userID = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, barcode);
            preparedStatement.setString(2, user.user_id);

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                return false;
            }
            else{
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String loan_book (JTextField loan_BC_tf, User user){
        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        String barcode = loan_BC_tf.getText();

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO loan (userID, Barcode, BorrowDate, Returned) VALUES (?, ?, CURDATE(), '0');";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);
            preparedStatement.setString(2, barcode);

            preparedStatement.executeUpdate();
            return "Success";

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Refrence Copy can not be loaned")) {
                return "Refrence";
            } else {
                return "Barcode";
            }
        }
    }
    public void get_recipt (String barcode,
                              JLabel title,
                              JLabel isbn,
                              JLabel loan_date,
                              JLabel return_date){

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.Title, i.ISBN, l.BorrowDate, l.ReturnDate FROM loan l Join item_copy ic ON ic.Barcode = l.Barcode Join item i ON ic.ISBN = i.ISBN Where l.Barcode = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, barcode);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title_recived = resultSet.getString("Title");
                String ISBN_recived = resultSet.getString("ISBN");
                String Date_of_loan = resultSet.getString("BorrowDate");
                String Date_of_return = resultSet.getString("ReturnDate");
                title.setText("Title: " + title_recived);
                isbn.setText("ISBN: " + ISBN_recived);
                loan_date.setText("Date of Loan: " + Date_of_loan);
                return_date.setText("Return Date: " + Date_of_return);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public DefaultTableModel search_results_manage(String search_text) {
        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ISBN", "Title", "Type", "Director/Author", "Classification", "Publisher"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String classification = resultSet.getString("Classification");
                String publisher = resultSet.getString("Publisher");
                model.addRow(new Object[]{ISBN, title, type, dir_auth, classification, publisher});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
