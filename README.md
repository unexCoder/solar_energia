# solar_energia
### unexcoder

## web-store-app

## v.1.0.0

## DATABASE STRUCTURE

```
+-------------------------+
| Tables_in_solar_energia |
+-------------------------+
| articulo                |
| fabrica                 |
| imagen                  |
| usuario                 |
+-------------------------+

+-----------------+---------------+------+-----+---------+-------+
| Field           | Type          | Null | Key | Default | Extra |
+-----------------+---------------+------+-----+---------+-------+
| id              | binary(16)    | NO   | PRI | NULL    |       |
| descripcion     | varchar(1000) | YES  |     | NULL    |       |
| nombre_articulo | varchar(255)  | NO   |     | NULL    |       |
| nro_articulo    | int           | NO   | UNI | NULL    |       |
| fabrica_id      | binary(16)    | NO   | MUL | NULL    |       |
| imagen_id       | binary(16)    | YES  | UNI | NULL    |       |
+-----------------+---------------+------+-----+---------+-------+

+--------+--------------+------+-----+---------+-------+
| Field  | Type         | Null | Key | Default | Extra |
+--------+--------------+------+-----+---------+-------+
| id     | binary(16)   | NO   | PRI | NULL    |       |
| nombre | varchar(100) | NO   |     | NULL    |       |
+--------+--------------+------+-----+---------+-------+

+-----------+----------------------+------+-----+---------+-------+
| Field     | Type                 | Null | Key | Default | Extra |
+-----------+----------------------+------+-----+---------+-------+
| id        | binary(16)           | NO   | PRI | NULL    |       |
| apellido  | varchar(100)         | NO   |     | NULL    |       |
| email     | varchar(255)         | NO   | UNI | NULL    |       |
| nombre    | varchar(100)         | NO   |     | NULL    |       |
| password  | varchar(255)         | NO   |     | NULL    |       |
| rol       | enum('ADMIN','USER') | NO   |     | NULL    |       |
| imagen_id | binary(16)           | YES  | UNI | NULL    |       |
+-----------+----------------------+------+-----+---------+-------+

+-------------+--------------+------+-----+---------+-------+
| Field       | Type         | Null | Key | Default | Extra |
+-------------+--------------+------+-----+---------+-------+
| id          | binary(16)   | NO   | PRI | NULL    |       |
| contenido   | longblob     | NO   |     | NULL    |       |
| descripcion | varchar(500) | YES  |     | NULL    |       |
| mime        | varchar(100) | NO   |     | NULL    |       |
| nombre      | varchar(255) | NO   |     | NULL    |       |
+-------------+--------------+------+-----+---------+-------+
```