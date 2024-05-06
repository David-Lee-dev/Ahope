CREATE TABLE card (
    id UUID PRIMARY KEY,
    seq BIGINT,
    member UUID,
    metadata UUID,
    foreign key (member) references member (id),
    foreign key (metadata) references metadata (id)
);
