package model;

/**
 * POJO representing a single government scheme record
 * along with its eligibility criteria.
 */
public class Scheme {

    private int schemeId;
    private String schemeName;
    private String description;
    private int minAge;
    private int maxAge;
    private double maxIncome;
    private String gender;
    private String category;
    private String occupation;
    private String state;
    private String benefits;
    private String officialLink;

    public Scheme() { }

    public Scheme(int schemeId, String schemeName, String description, int minAge, int maxAge,
                   double maxIncome, String gender, String category, String occupation,
                   String state, String benefits, String officialLink) {
        this.schemeId = schemeId;
        this.schemeName = schemeName;
        this.description = description;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxIncome = maxIncome;
        this.gender = gender;
        this.category = category;
        this.occupation = occupation;
        this.state = state;
        this.benefits = benefits;
        this.officialLink = officialLink;
    }

    // ---------- Getters and Setters ----------

    public int getSchemeId() { return schemeId; }
    public void setSchemeId(int schemeId) { this.schemeId = schemeId; }

    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }

    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }

    public double getMaxIncome() { return maxIncome; }
    public void setMaxIncome(double maxIncome) { this.maxIncome = maxIncome; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getOfficialLink() { return officialLink; }
    public void setOfficialLink(String officialLink) { this.officialLink = officialLink; }

    @Override
    public String toString() {
        return schemeName; // used directly when added to JList/JComboBox
    }
}
