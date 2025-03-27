create table if not exists address
(
    address_id uuid default gen_random_uuid() primary key,
    country    varchar,
    city       varchar,
    street     varchar,
    house      varchar,
    flat       varchar
);

create table if not exists deliveries
(
    delivery_id    uuid default gen_random_uuid() primary key,
    from_address   uuid,
    to_address     uuid,
    order_id       uuid,
    delivery_state varchar(50)
);

create table if not exists from_address
(
    delivery_id uuid references deliveries (delivery_id),
    address_id  uuid references address (address_id)
);

create table if not exists to_address
(
    delivery_id uuid references deliveries (delivery_id),
    address_id  uuid references address (address_id)
);