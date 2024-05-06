CREATE TABLE metadata (
    id UUID PRIMARY KEY,
    image_url VARCHAR(255) UNIQUE,
    count INT default 0,
    grade INT default 0,
    weight INT,
    active BOOLEAN,
    category VARCHAR(255)
);
