CREATE TABLE metadata (
    id UUID PRIMARY KEY,
    image_url VARCHAR(255) UNIQUE,
    count INT default 0,
    grade INT default 0,
    category VARCHAR(255)
);
