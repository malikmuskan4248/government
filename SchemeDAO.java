package dao;

import model.Scheme;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the `schemes` table.
 * Handles all CRUD operations and the core eligibility-matching query.
 */
public class SchemeDAO {

    /**
     * Returns every scheme that matches the citizen's profile.
     * A scheme matches when:
     *  - age is within [min_age, max_age]
     *  - income <= max_income
     *  - gender is 'Any' or matches exactly
     *  - category is 'Any' or matches exactly
     *  - occupation is 'Any' or matches exactly
     *  - state is 'All' or matches exactly
     */
    public List<Scheme> findEligibleSchemes(int age, double income, String gender,
                                             String category, String occupation, String state) {
        List<Scheme> result = new ArrayList<>();

        String sql = "SELECT * FROM schemes WHERE " +
                "? BETWEEN min_age AND max_age " +
                "AND ? <= max_income " +
                "AND (gender = 'Any' OR gender = ?) " +
                "AND (category = 'Any' OR category = ?) " +
                "AND (occupation = 'Any' OR occupation = ?) " +
                "AND (state = 'All' OR state = ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, age);
            ps.setDouble(2, income);
            ps.setString(3, gender);
            ps.setString(4, category);
            ps.setString(5, occupation);
            ps.setString(6, state);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /** Returns all schemes in the database (used by Admin panel). */
    public List<Scheme> getAllSchemes() {
        List<Scheme> list = new ArrayList<>();
        String sql = "SELECT * FROM schemes ORDER BY scheme_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Inserts a new scheme record. Returns true on success. */
    public boolean addScheme(Scheme s) {
        String sql = "INSERT INTO schemes " +
                "(scheme_name, description, min_age, max_age, max_income, gender, category, occupation, state, benefits, official_link) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bindSchemeParams(ps, s);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Updates an existing scheme record identified by schemeId. */
    public boolean updateScheme(Scheme s) {
        String sql = "UPDATE schemes SET scheme_name=?, description=?, min_age=?, max_age=?, " +
                "max_income=?, gender=?, category=?, occupation=?, state=?, benefits=?, official_link=? " +
                "WHERE scheme_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bindSchemeParams(ps, s);
            ps.setInt(12, s.getSchemeId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Deletes a scheme by its ID. */
    public boolean deleteScheme(int schemeId) {
        String sql = "DELETE FROM schemes WHERE scheme_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, schemeId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- helpers ----------

    private void bindSchemeParams(PreparedStatement ps, Scheme s) throws SQLException {
        ps.setString(1, s.getSchemeName());
        ps.setString(2, s.getDescription());
        ps.setInt(3, s.getMinAge());
        ps.setInt(4, s.getMaxAge());
        ps.setDouble(5, s.getMaxIncome());
        ps.setString(6, s.getGender());
        ps.setString(7, s.getCategory());
        ps.setString(8, s.getOccupation());
        ps.setString(9, s.getState());
        ps.setString(10, s.getBenefits());
        ps.setString(11, s.getOfficialLink());
    }

    private Scheme mapRow(ResultSet rs) throws SQLException {
        return new Scheme(
                rs.getInt("scheme_id"),
                rs.getString("scheme_name"),
                rs.getString("description"),
                rs.getInt("min_age"),
                rs.getInt("max_age"),
                rs.getDouble("max_income"),
                rs.getString("gender"),
                rs.getString("category"),
                rs.getString("occupation"),
                rs.getString("state"),
                rs.getString("benefits"),
                rs.getString("official_link")
        );
    }
}
