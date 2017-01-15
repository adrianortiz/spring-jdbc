drop table if exists persona;

create table persona (
	id_persona identity primary key,
	nombre varchar(50) not null,
	ape_paterno varchar(50) not null,
	ape_materno varchar(50),
	email varchar(50) not null unique
);