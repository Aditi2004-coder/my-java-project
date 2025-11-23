import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegistrationForm {

    public static void main(String[] args) {

        Frame frame = new Frame("Registration Form");
        frame.setLayout(new BorderLayout());

        // ------------------ HEADER ------------------
        Panel header = new Panel(new FlowLayout(FlowLayout.CENTER));
        Label title = new Label("STUDENT REGISTRATION");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        // ------------------ LEFT FORM PANEL ------------------
        Panel formPanel = new Panel();

        // NO GAP between label and textfield
        formPanel.setLayout(new GridLayout(13, 2, 0, 2));

        // Labels
        Label lblFirst = new Label("First Name:");
        Label lblFather = new Label("Father's Name:");
        Label lblSurname = new Label("Surname:");
        Label lblAge = new Label("Age:");
        Label lblEmail = new Label("Email ID:");
        Label lblDOB = new Label("Birth Date:");
        Label lblGender = new Label("Gender:");
        Label lblLang = new Label("Known Languages:");
        Label lblCity = new Label("City:");
        Label lblPhone = new Label("Phone No:");
        Label lblAddress = new Label("Address:");

        // TextFields (INCREASED SIZE)
        final TextField txtFirstName = new TextField(90);
        final TextField txtFatherName = new TextField(90);
        final TextField txtSurname = new TextField(90);
        final TextField txtEmail = new TextField(90);
        final TextField txtDOB = new TextField("DD/MM/YYYY", 90);
        final TextField txtCity = new TextField(90);
        final TextField txtPhone = new TextField(90);
        final TextField txtAddress = new TextField(90);

        txtDOB.setForeground(Color.GRAY);

        // Age dropdown
        final Choice ageChoice = new Choice();
        for (int i = 16; i <= 90; i++) ageChoice.add(String.valueOf(i));

        // Gender radio
        final CheckboxGroup genderGroup = new CheckboxGroup();
        final Checkbox rbMale = new Checkbox("Male", genderGroup, true);
        final Checkbox rbFemale = new Checkbox("Female", genderGroup, false);
        final Checkbox rbOther = new Checkbox("Other", genderGroup, false);

        // Languages
        final Checkbox cbSQL = new Checkbox("SQL");
        final Checkbox cbJava = new Checkbox("Java");
        final Checkbox cbPython = new Checkbox("Python");
        final Checkbox cbC = new Checkbox("C");
        final Checkbox cbDSA = new Checkbox("DSA");
        final Checkbox cbCpp = new Checkbox("C++");

        // Submit Button — SAME WIDTH AS TEXTFIELD
        Button submitBtn = new Button("Submit");
        submitBtn.setPreferredSize(new Dimension(60, 30));  // EXACT width

        // DOB Placeholder behavior
        txtDOB.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (txtDOB.getText().equals("DD/MM/YYYY")) {
                    txtDOB.setText("");
                    txtDOB.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtDOB.getText().trim().isEmpty()) {
                    txtDOB.setText("DD/MM/YYYY");
                    txtDOB.setForeground(Color.GRAY);
                }
            }
        });

        // ----- ADD COMPONENTS (NO GAP) -----
        formPanel.add(lblFirst); formPanel.add(noGap(txtFirstName));
        formPanel.add(lblFather); formPanel.add(noGap(txtFatherName));
        formPanel.add(lblSurname); formPanel.add(noGap(txtSurname));
        formPanel.add(lblAge); formPanel.add(noGap(ageChoice));
        formPanel.add(lblEmail); formPanel.add(noGap(txtEmail));
        formPanel.add(lblDOB); formPanel.add(noGap(txtDOB));

        Panel genderPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(rbMale); genderPanel.add(rbFemale); genderPanel.add(rbOther);
        formPanel.add(lblGender); formPanel.add(genderPanel);

        Panel langPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        langPanel.add(cbSQL); langPanel.add(cbJava); langPanel.add(cbPython);
        langPanel.add(cbC); langPanel.add(cbDSA); langPanel.add(cbCpp);
        formPanel.add(lblLang); formPanel.add(langPanel);

        formPanel.add(lblCity); formPanel.add(noGap(txtCity));
        formPanel.add(lblPhone); formPanel.add(noGap(txtPhone));
        formPanel.add(lblAddress); formPanel.add(noGap(txtAddress));

        formPanel.add(new Label());
        formPanel.add(submitBtn);

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        scrollPane.add(formPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ---------- Submit action ----------
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // --- DATABASE WRITE (your DB code) ---
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "INSERT INTO registration (firstname, fathername, surname, age, email, dob, gender, languages, city, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, txtFirstName.getText().trim());
                    pst.setString(2, txtFatherName.getText().trim());
                    pst.setString(3, txtSurname.getText().trim());
                    pst.setInt(4, Integer.parseInt(ageChoice.getSelectedItem()));
                    pst.setString(5, txtEmail.getText().trim());
                    pst.setString(6, txtDOB.getText().equals("DD/MM/YYYY") ? "" : txtDOB.getText().trim());

                    Checkbox selectedGender = genderGroup.getSelectedCheckbox();
                    pst.setString(7, selectedGender != null ? selectedGender.getLabel() : "");

                    StringBuilder languages = new StringBuilder();
                    if (cbSQL.getState()) languages.append("SQL ");
                    if (cbJava.getState()) languages.append("Java ");
                    if (cbPython.getState()) languages.append("Python ");
                    if (cbC.getState()) languages.append("C ");
                    if (cbDSA.getState()) languages.append("DSA ");
                    if (cbCpp.getState()) languages.append("C++ ");

                    pst.setString(8, languages.toString().trim());
                    pst.setString(9, txtCity.getText().trim());
                    pst.setString(10, txtPhone.getText().trim());
                    pst.setString(11, txtAddress.getText().trim());
                    pst.executeUpdate();

                    pst.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // ---- CLEAR ALL FIELDS AFTER SUBMIT ----
                txtFirstName.setText("");
                txtFatherName.setText("");
                txtSurname.setText("");
                ageChoice.select(0);
                txtEmail.setText("");
                txtDOB.setText("DD/MM/YYYY");
                txtDOB.setForeground(Color.GRAY);
                rbMale.setState(true);
                cbSQL.setState(false); cbJava.setState(false); cbPython.setState(false);
                cbC.setState(false); cbDSA.setState(false); cbCpp.setState(false);
                txtCity.setText("");
                txtPhone.setText("");
                txtAddress.setText("");
            }
        });

        // ------------------ FOOTER ------------------
        Panel footer = new Panel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.LIGHT_GRAY);
        Label footerText = new Label("CSMSS Chh. Shahu College of Engineering");
        footerText.setFont(new Font("Arial", Font.BOLD, 14));
        footer.add(footerText);
        frame.add(footer, BorderLayout.SOUTH);

        // Frame properties
        frame.setSize(660, 500);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }

    // Helper: Remove horizontal gap
    static Panel noGap(Component c) {
        Panel p = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.add(c);
        return p;
    }
}
