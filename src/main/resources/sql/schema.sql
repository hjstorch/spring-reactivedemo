CREATE TABLE IF NOT EXISTS PRODUCT(
                         price       numeric(38, 2),
                         id          uuid not null primary key,
                         description varchar(255),
                         name        varchar(255) unique
);
commit;