CREATE SEQUENCE node_seq;

CREATE TABLE nodes
(
    id integer primary key DEFAULT nextval('node_seq'),
    name text not null,
    x integer not null,
    y integer not null,
    nr_map integer not null
);

ALTER SEQUENCE node_seq
    OWNED BY nodes.id;


CREATE SEQUENCE street_seq;

CREATE TABLE streets
(
    id       integer primary key DEFAULT nextval('street_seq'),
    name     text not null,
    id_node_1 integer,
    id_node_2 integer,
    length   integer,
    nr_map integer not null
);

ALTER SEQUENCE street_seq
    OWNED BY streets.id;

CREATE SEQUENCE user_seq;

CREATE TABLE users
(
    id       integer primary key DEFAULT nextval('user_seq'),
    name     text not null,
    password text not null

);

ALTER SEQUENCE user_seq
    OWNED BY users.id;
