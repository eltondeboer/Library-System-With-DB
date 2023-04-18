import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Admin_controll extends JDialog{
    private JButton view_loans_btn;
    private JButton late_btn;
    private JButton return_btn;
    private JPanel Main_panel;
    private JTabbedPane tabbedPane1;
    private JPanel JP_View_loans;
    private JButton manage_btn;
    private JButton all_late_btn;
    private JButton signIn_btn;
    private JPanel JP_Late_Returns;
    private JPanel JP_Return_Loan;
    private JPanel JP_Loan_Item;
    private JPanel JP_Manage_Item;
    private JPanel JP_All_Late;
    private JPanel JP_Search_Results;
    private JButton loan_btn;
    private JTable Jtble_View_Loans;
    private JPanel JP_Sign_in;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JCheckBox show_pw;
    private JButton login_btn_ok;
    private JPanel JP_signed_out;
    private JButton reserveItem_btn;
    private JLabel Admin_actions_label;
    private JTextField textField1;
    private JPanel Searchbar;
    private JButton search_btn;
    private JPanel HomePage;


    public void signed_out_state() {
        view_loans_btn.setVisible(false);
        late_btn.setVisible(false);
        return_btn.setVisible(false);
        loan_btn.setVisible(false);
        reserveItem_btn.setVisible(false);
        manage_btn.setVisible(false);
        all_late_btn.setVisible(false);
        Admin_actions_label.setVisible(false);
    }
    public void signed_in_state(User user) {

        if (user.user_is_admin()){
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(true);
            all_late_btn.setVisible(true);
            Admin_actions_label.setVisible(true);
        }
        else{
            System.out.println(user.user_is_admin());
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(false);
            all_late_btn.setVisible(false);
            Admin_actions_label.setVisible(false);
        }

    }
    public Admin_controll (JFrame parent){
        super(parent);
        //Makes the searchbar not resize depending on window size
        Searchbar.setMaximumSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.setMinimumSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.setPreferredSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.putClientProperty("JPanel.sizeVariant", "small");
        //Setting the opening state to signed out
        signed_out_state();
        //Create database connection to grab tables to insert
        DataBase_tbles dbconn = new DataBase_tbles();
        //Sets the windows title, content pane, size, makes it reziseable, relitive to parent object, and makes it close when closed.
        setTitle("Library System");
        setContentPane(Main_panel);
        setMinimumSize(new Dimension(600, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Sets data from database in JTable and makes it non editable.
        Jtble_View_Loans.setModel(dbconn.get_items_test());
        Jtble_View_Loans.setEnabled(false);
        Jtble_View_Loans.setVisible(true);
        //Customizes UI to hide tabs
        tabbedPane1.setUI(new HiddenTabbedPaneUI());
        //Creates instant of user.
        User user1 = new User();
        //Opens HomePage on start
        tabbedPane1.setSelectedIndex(9);
        view_loans_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(0);
            }
        });

        late_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(1);
            }
        });

        return_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(2);
            }
        });

        signIn_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user1.signed_in){
                    user1.user_signout();
                    System.out.println(user1.name);
                    tabbedPane1.setSelectedIndex(8);
                    signIn_btn.setText("Sign In");
                    signed_out_state();
                }
                else{
                    tabbedPane1.setSelectedIndex(7);
                }
            }
        });

        //When user presses login data gets checked against the database through function "getAutenticateUser(email, password);"
        //Then gets either denied or signed in.
        //Depending on user type the UI gets set.
        login_btn_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean acces = false;
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                acces = user1.getAutenticateUser(email, password);
                    if(acces){
                        signIn_btn.setText("Sign out");
                        tabbedPane1.setSelectedIndex(0);
                        signed_in_state(user1);
                    }
                    else{
                        JOptionPane.showMessageDialog(Admin_controll.this,
                                "Email or Password Invalid");
                    }
                }
        });

        //Function so that the checkmark hides and shows password.
        show_pw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (show_pw.isSelected()) {
                    pfPassword.setEchoChar((char) 0);
                } else {
                    pfPassword.setEchoChar('*');
                }
            }
        });
        setVisible(true);
    }
}

