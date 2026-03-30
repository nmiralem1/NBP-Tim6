-- ADD CONTINENT TO COUNTRIES
BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE NBPT6.COUNTRIES ADD CONTINENT VARCHAR2(100)';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -1430 THEN
      NULL; -- Ignore if column already exists
    ELSE
      RAISE;
    END IF;
END;
/

-- UPDATE EXISTING DATA WITH CONTINENT INFORMATION
UPDATE NBPT6.COUNTRIES SET CONTINENT = 'Europe' WHERE NAME IN ('France', 'Italy', 'Germany', 'Austria', 'Bosnia and Herzegovina', 'Serbia', 'Croatia', 'Hungary') AND CONTINENT IS NULL;
UPDATE NBPT6.COUNTRIES SET CONTINENT = 'Asia' WHERE NAME IN ('Turkey', 'Japan', 'China') AND CONTINENT IS NULL;

-- ENSURE REVIEWS FOREIGN KEY CONSTRAINT (TO NBP_USER)
-- First, identify any orphan reviews (if any exist during development)
-- For a clean lab DB, we assume it's safe to add the constraint directly.
-- ENSURE REVIEWS FOREIGN KEY CONSTRAINT (TO NBP_USER)
BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE NBPT6.REVIEWS 
    ADD CONSTRAINT fk_review_user 
    FOREIGN KEY (USER_ID) 
    REFERENCES NBP.NBP_USER(ID)
    ON DELETE CASCADE';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -2275 THEN
      NULL; -- Ignore if constraint already exists
    ELSE
      RAISE;
    END IF;
END;
/
