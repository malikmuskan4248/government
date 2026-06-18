package ui;

import dao.SchemeDAO;
import model.Scheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Citizen-facing screen. User enters their profile details
 * and the app lists every scheme they are eligible for.
 */
public class UserDashboard extends JFrame {

    private JTextField txtAge, txtIncome;
    private JComboBox<String> cmbGender, cmbCategory, cmbOccupation, cmbState;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;

    private final SchemeDAO schemeDAO = new SchemeDAO();
    private List<Scheme> lastResults;

    public UserDashboard() {
        setTitle("Government Scheme Finder - Citizen Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildResultPanel(), BorderLayout.CENTER);
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Enter Your Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Age:"), gbc);
        txtAge = new JTextField(6);
        gbc.gridx = 1;
        panel.add(txtAge, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Annual Income (Rs):"), gbc);
        txtIncome = new JTextField(8);
        gbc.gridx = 3;
        panel.add(txtIncome, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Gender:"), gbc);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gbc.gridx = 1;
        panel.add(cmbGender, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Category:"), gbc);
        cmbCategory = new JComboBox<>(new String[]{"General", "OBC", "SC", "ST"});
        gbc.gridx = 3;
        panel.add(cmbCategory, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Occupation:"), gbc);
        cmbOccupation = new JComboBox<>(new String[]{"Student", "Farmer", "Unemployed", "Self-Employed", "Salaried", "Any"});
        gbc.gridx = 1;
        panel.add(cmbOccupation, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("State:"), gbc);
        cmbState = new JComboBox<>(new String[]{
                "Haryana", "Punjab", "Delhi", "Uttar Pradesh", "Rajasthan",
                "Maharashtra", "Gujarat", "Other"
        });
        gbc.gridx = 3;
        panel.add(cmbState, gbc);

        // Row 3 - search button
        JButton btnSearch = new JButton("Find Eligible Schemes");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panel.add(btnSearch, gbc);

        btnSearch.addActionListener(e -> performSearch());

        return panel;
    }

    private JPanel buildResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Eligible Schemes"));

        tableModel = new DefaultTableModel(new String[]{"ID", "Scheme Name", "Benefits"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(e -> showDetails());

        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setPreferredSize(new Dimension(880, 250));

        detailsArea = new JTextArea(8, 60);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Scheme Details"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, detailsScroll);
        splitPane.setResizeWeight(0.55);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void performSearch() {
        try {
            int age = Integer.parseInt(txtAge.getText().trim());
            double income = Double.parseDouble(txtIncome.getText().trim());
            String gender = (String) cmbGender.getSelectedItem();
            String category = (String) cmbCategory.getSelectedItem();
            String occupation = (String) cmbOccupation.getSelectedItem();
            String state = (String) cmbState.getSelectedItem();

            lastResults = schemeDAO.findEligibleSchemes(age, income, gender, category, occupation, state);

            tableModel.setRowCount(0); // clear old rows
            detailsArea.setText("");

            if (lastResults.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No schemes matched your profile. Try adjusting the details.",
                        "No Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Scheme s : lastResults) {
                tableModel.addRow(new Object[]{s.getSchemeId(), s.getSchemeName(), s.getBenefits()});
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for Age and Income.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showDetails() {
        int row = resultTable.getSelectedRow();
        if (row < 0 || lastResults == null || row >= lastResults.size()) return;

        Scheme s = lastResults.get(row);
        String text = "Scheme: " + s.getSchemeName() + "\n\n" +
                "Description: " + s.getDescription() + "\n\n" +
                "Benefits: " + s.getBenefits() + "\n\n" +
                "Eligible Age: " + s.getMinAge() + " - " + s.getMaxAge() + "\n" +
                "Max Income: Rs " + s.getMaxIncome() + "\n" +
                "Category: " + s.getCategory() + "  |  Occupation: " + s.getOccupation() +
                "  |  State: " + s.getState() + "\n\n" +
                "Official Link: " + s.getOfficialLink();

        detailsArea.setText(text);
    }
}
