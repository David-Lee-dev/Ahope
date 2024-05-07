CREATE TABLE metadata (
    id UUID PRIMARY KEY,
    image_url VARCHAR(255) UNIQUE,
    count INT DEFAULT 0,
    grade INT DEFAULT 0,
    weight INT NOT NULL,
    active BOOLEAN DEFAULT false,
    category VARCHAR(255)
);