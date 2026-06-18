package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Entry-point screen: lets a user log in as ADMIN or USER.
 * Default credentials (seeded in DB):
 *   admin / admin123  -> ADMIN dashboard
 *   user  / user123   -> USER (citizen) dashboard
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Government Scheme Finder - Login");
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Government Scheme Finder", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        JLabel lblHint = new JLabel("<html><i>Demo: admin/admin123 or user/user123</i></html>", SwingConstants.CENTER);
        gbc.gridy = 4;
        panel.add(lblHint, gbc);

        add(panel);

        btnLogin.addActionListener(e -> attemptLogin());
        // allow pressing Enter inside password field to submit
        txtPassword.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose(); // close login window

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            new AdminDashboard().setVisible(true);
        } else {
            new UserDashboard().setVisible(true);
        }
    }

    public static void main(String[] args) {
        // Use the system look-and-feel for a more native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
