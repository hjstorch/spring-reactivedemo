CREATE TABLE IF NOT EXISTS PRODUCT (
                                       price       numeric(38, 2),
    id          uuid not null primary key,
    description varchar(255),
    name        varchar(255) unique
    );

INSERT INTO PRODUCT (ID, NAME, DESCRIPTION) VALUES
                           (GEN_RANDOM_UUID(),'P1','Product one'),
                           (GEN_RANDOM_UUID(),'P2','Product two');
commit;