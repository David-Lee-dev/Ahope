CREATE TABLE member (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    last_gacha_timestamp BIGINT NULL,
    remain_ticket INT DEFAULT 0
);

INSERT INTO member (id, email, last_gacha_timestamp, remain_ticket)
VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'john.doe@example.com', '0', '1'),
    ('c1e03367-6c1b-4a82-b916-0b8a2d15c1a5', 'jane.smith@example.com', '0', '2'),
    ('d45b4219-f4c4-4b2b-bc5a-876ea85bd32e', 'michael.johnson@example.com', '0', '3'),
    ('a3e3d052-1c9d-40c4-83e7-936127cfbe15', 'emily.davis@example.com', '0', '0'),
    ('d45b4219-f4c4-4b2b-bc5a-876ea85bd325', 'duplicated@test.com', '0', '3')