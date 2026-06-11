INSERT INTO app_user (username, password, email, full_name, role, active, created_by, created_on)
SELECT
    'superadmin',
    '$2a$10$nqtRKEr9m3WnM8QUgFhaLevJTWTXkS2GWv7WC4D9bUGEiBMVIZbXS',
    'superadmin@resustainability.com',
    'Super Administrator',
    'SUPER_ADMIN',
    1,
    'SYSTEM',
    GETDATE()
    WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE username = 'superadmin'
);



IF COL_LENGTH('pcmaster', 'plantcode') IS NULL
    ALTER TABLE pcmaster ADD plantcode VARCHAR(100) NULL;


UPDATE pcmaster 
SET plantcode = 'NA'
WHERE plantcode IS NULL;


IF EXISTS (
    SELECT 1 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'pcmaster' 
    AND COLUMN_NAME = 'plantcode' 
    AND IS_NULLABLE = 'YES'
)
    ALTER TABLE pcmaster 
    ALTER COLUMN plantcode VARCHAR(100) NOT NULL;
    
    
    
 
