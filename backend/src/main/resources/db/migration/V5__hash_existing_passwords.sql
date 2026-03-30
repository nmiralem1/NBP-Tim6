-- HASH PASSWORDS FOR INITIAL USERS
-- Password: azra123
-- BCrypt hash for 'azra123': $2a$10$G9/9Z81n0/SvSaM7OaS1LOfa9.dYRqjJYOdkcNf.CjPOCtbbL0Hn.

UPDATE NBP.NBP_USER 
SET PASSWORD = '$2a$10$G9/9Z81n0/SvSaM7OaS1LOfa9.dYRqjJYOdkcNf.CjPOCtbbL0Hn.'
WHERE USERNAME IN ('azunic3', 'nmiralem1', 'nkadric1', 'aherak2', 'ahromic1');

COMMIT;
