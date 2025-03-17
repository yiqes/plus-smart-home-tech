drop table if exists warehouse_product, bookings, booking_products;

create table if not exists warehouse_product
(
    product_id uuid primary key,
    quantity   integer,
    fragile    boolean,
    width      double precision not null,
    height     double precision not null,
    depth      double precision not null,
    weight     double precision not null
);

create table if not exists bookings
(
    shopping_cart_id uuid primary key,
    delivery_weight  double precision not null,
    delivery_volume  double precision not null,
    fragile          boolean          not null,
    order_id         uuid
);

create table if not exists booking_products
(
    shopping_cart_id uuid references bookings (shopping_cart_id) on delete cascade primary key,
    product_id       uuid not null,
    quantity         integer
)
