package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

/**
 * Data Access Object for the `users` table.
 * Handles login authentication.
 */
public class UserDAO {

    /**
     * Validates username/password and returns the matching User,
     * or null if credentials are invalid.
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // invalid credentials
    }
}
