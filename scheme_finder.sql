-- =========================================================
-- Government Scheme Finder - Database Schema
-- =========================================================

CREATE DATABASE IF NOT EXISTS scheme_finder_db;
USE scheme_finder_db;

-- ---------------------------------------------------------
-- Table: schemes
-- Stores all government scheme details and eligibility rules
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS schemes (
    scheme_id       INT AUTO_INCREMENT PRIMARY KEY,
    scheme_name     VARCHAR(150) NOT NULL,
    description     TEXT,
    min_age         INT DEFAULT 0,
    max_age         INT DEFAULT 120,
    max_income      DOUBLE DEFAULT 999999999,
    gender          VARCHAR(10) DEFAULT 'Any',      -- Male / Female / Any
    category        VARCHAR(20) DEFAULT 'Any',      -- General/OBC/SC/ST/Any
    occupation      VARCHAR(50) DEFAULT 'Any',      -- Farmer/Student/Unemployed/Self-Employed/Any
    state           VARCHAR(50) DEFAULT 'All',      -- Specific state or 'All' for central schemes
    benefits        TEXT,
    official_link   VARCHAR(255)
);

-- ---------------------------------------------------------
-- Table: users
-- Stores basic login credentials (Admin / Citizen)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(50) NOT NULL,
    role        VARCHAR(10) DEFAULT 'USER'  -- 'ADMIN' or 'USER'
);

-- ---------------------------------------------------------
-- Seed: default users
-- ---------------------------------------------------------
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('user', 'user123', 'USER');

-- ---------------------------------------------------------
-- Seed: sample schemes (edit / add freely)
-- ---------------------------------------------------------
INSERT INTO schemes (scheme_name, description, min_age, max_age, max_income, gender, category, occupation, state, benefits, official_link) VALUES
('PM Kisan Samman Nidhi', 'Income support scheme for small and marginal farmers.', 18, 120, 200000, 'Any', 'Any', 'Farmer', 'All', 'Rs 6000 per year in 3 installments', 'https://pmkisan.gov.in'),

('National Scholarship for Students', 'Scholarship for meritorious students from economically weaker sections.', 15, 30, 250000, 'Any', 'Any', 'Student', 'All', 'Tuition fee waiver and stipend', 'https://scholarships.gov.in'),

('Pradhan Mantri Awas Yojana', 'Housing scheme for urban and rural poor.', 21, 70, 300000, 'Any', 'Any', 'Any', 'All', 'Subsidy on home loan interest', 'https://pmaymis.gov.in'),

('Sukanya Samriddhi Yojana', 'Savings scheme for the welfare of girl children.', 0, 10, 999999999, 'Female', 'Any', 'Any', 'All', 'High interest savings account for girl child', 'https://nsiindia.gov.in'),

('National Pension Scheme for Unorganized Workers', 'Pension scheme for unemployed/unorganized sector workers.', 18, 40, 150000, 'Any', 'Any', 'Unemployed', 'All', 'Monthly pension after age 60', 'https://npscra.nsdl.co.in'),

('Stand-Up India Scheme', 'Loan scheme for SC/ST and women entrepreneurs.', 18, 65, 999999999, 'Any', 'SC', 'Self-Employed', 'All', 'Bank loans between 10 lakh to 1 crore', 'https://standupmitra.in'),

('Haryana Free Bicycle Scheme', 'State scheme for school students of Haryana.', 12, 18, 200000, 'Any', 'Any', 'Student', 'Haryana', 'Free bicycle for school commute', 'https://haryana.gov.in');
