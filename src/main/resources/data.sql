
-- Drop tables if they exist (to avoid errors if the tables already exist)
DROP TABLE IF EXISTS money_account CASCADE;
DROP TABLE IF EXISTS user_account CASCADE;

-- Create user_account table
CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_on TIMESTAMP DEFAULT NOW(),
    updated_on TIMESTAMP DEFAULT NOW(),
    username VARCHAR(255) NOT NULL UNIQUE
);

-- Create money_account table
CREATE TABLE money_account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_on TIMESTAMP DEFAULT NOW(),
    updated_on TIMESTAMP DEFAULT NOW(),
    user_id UUID REFERENCES user_account(id) ON DELETE CASCADE,
    currency VARCHAR(10) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL
);


-- Make sure the pgcrypto extension is enabled (this allows UUID generation)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Inserting Users into the 'user' table (assuming 'user' table exists with required fields)
INSERT INTO user_account (id, created_on, updated_on, username)
VALUES
    (gen_random_uuid(), NOW(), NOW(), 'john'),
    (gen_random_uuid(), NOW(), NOW(), 'jane');

-- Inserting MoneyAccounts into the 'money_account' table (assuming 'money_account' table exists with required fields)
INSERT INTO money_account (id, created_on, updated_on, user_id, currency, amount)
VALUES
    (gen_random_uuid(), NOW(), NOW(), (SELECT id FROM user_account WHERE username = 'john'), 'USD', 1000.00),
    (gen_random_uuid(), NOW(), NOW(), (SELECT id FROM user_account WHERE username = 'john'), 'EUR', 2000.50),
    (gen_random_uuid(), NOW(), NOW(), (SELECT id FROM user_account WHERE username = 'jane'), 'USD', 1500.75),
    (gen_random_uuid(), NOW(), NOW(), (SELECT id FROM user_account WHERE username = 'jane'), 'EUR', 500.25);
