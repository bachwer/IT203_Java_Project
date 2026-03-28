Create database restaurant_db;
USE restaurant_db;


SHOW TABLES;



create table menu_item
(
    id     INT PRIMARY key auto_increment,
    price  DECIMAL(10, 2),
    type   VARCHAR(150) not null,
    name   VARCHAR(150) not null,
    status ENUM ('AVAILABLE','DISABLED','OUT_OF_STOCK') NOT NULL DEFAULT 'AVAILABLE'
);



create table users (
    id int primary key auto_increment,
    name varchar(150) not null,
    password varchar(250) not null,
    role enum('CUSTOMER', 'CHEF', 'MANAGER') not null DEFAULT 'CUSTOMER',
    status enum('ACTIVE','DISABLE') not null DEFAULT 'ACTIVE'

);
insert users(name, password, role) value('admin', '$2a$10$3f2.e61.HJDDjKZyYWGCvevWj3.QnmAIPMyXdpK3iHGouReUpG5Ka', 'MANAGER');

create table orders (
    id int primary key auto_increment,
    userId int not null,
    tableId int not null,
    status enum('PENDING', 'COOKING', 'DONE', 'CANCELLED'),
    approved BOOLEAN DEFAULT FALSE,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (tableId) REFERENCES tableRs(id),

    UNIQUE (tableId)
);
drop table orders;

create table orderItem(
    id int primary key auto_increment,
    orderId int not null,
    menuItemId int not null,
    quantity int not null,
    status enum('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELLED')  not null DEFAULT 'PENDING'
);



create table review(
    id int primary key auto_increment,
    userId int not null,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment text default null,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
);


create table tableRs
(
    id int primary key auto_increment,
    name varchar(200) not null,
    capacity int not null,
    status enum('AVAILABLE', 'OCCUPIED') not null default 'AVAILABLE'
);




UPDATE tableRs SET name = ?, capacity = ?, status = ? WHERE id = ?;





SELECT
    oi.id,
    oi.orderId,
    oi.menuItemId,
    oi.quantity,
    oi.status,
    mi.name
FROM orderItem oi
         JOIN menu_item mi ON oi.menuItemId = mi.id
WHERE orderId = ?
ORDER BY id;


Select  oi.id, oi.orderId, oi.menuItemId, mi.name AS menu_item_name, oi.quantity, oi.status
    from orderItem oi join menu_item mi  ON oi.id = mi.id JOIN orders o ON oi.orderId = o.id
WHERE o.approved = TRUE AND oi.status <> 'SERVED' ORDER BY oi.id;




select * from users;

select * from orders;

select * from tableRs;
select * from review;