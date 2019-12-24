--
-- Table structure for table access_token_tbl
--
create table access_token_tbl (
    id           bigint primary key generated always as identity,
    jti          varchar(2000) not null,
    created_date timestamp     not null
);