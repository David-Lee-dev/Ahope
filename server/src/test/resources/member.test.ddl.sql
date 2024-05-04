CREATE TABLE member (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    last_gacha_timestamp BIGINT NULL,
    remain_ticket INT DEFAULT 0
);