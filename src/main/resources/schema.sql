create table IF NOT EXISTS tb_sprinkle (
	seq bigint IDENTITY primary key, 
	user_id int not null,	
	room_id varchar(10) not null,
	token char(3) not null, 
	sprinkle_amt bigint not null default 0,
	received_amt bigint default 0,
	receiver_count int default 0, 
	reg_date datetime	
);

create table IF NOT EXISTS tb_distrbt (
	seq bigint IDENTITY primary key,
	sprinkle_seq bigint not null, 
	token char(3) not null, 
	rcv_room_id varchar(20) not null, 
	rcv_user_id varchar(20),
	rcv_amt bigint not null default 0,
	received_yn boolean default false	
);