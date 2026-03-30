-- =========================
-- IMAGES FOR CITIES (Destinations)
-- =========================
ALTER TABLE NBPT6.CITIES ADD IMAGE_URL VARCHAR2(500);

-- =========================
-- IMAGES FOR ACCOMMODATIONS (Hotels)
-- =========================
ALTER TABLE NBPT6.ACCOMMODATIONS ADD IMAGE_URL VARCHAR2(500);

-- =========================
-- IMAGES FOR ACTIVITIES
-- =========================
ALTER TABLE NBPT6.ACTIVITIES ADD IMAGE_URL VARCHAR2(500);

-- =========================
-- IMAGES FOR TRIPS
-- =========================
ALTER TABLE NBPT6.TRIPS ADD IMAGE_URL VARCHAR2(500);

-- =========================
-- PRIVATE USER PROFILES
-- =========================
CREATE TABLE NBPT6.USER_PROFILES (
    USER_ID NUMBER PRIMARY KEY,
    IMAGE_URL VARCHAR2(500),
    BIO VARCHAR2(1000),
    
    CONSTRAINT fk_profile_user
    FOREIGN KEY (USER_ID)
    REFERENCES NBP.NBP_USER(ID)
    ON DELETE CASCADE
);
