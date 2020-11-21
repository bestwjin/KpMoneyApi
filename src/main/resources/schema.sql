create table IF NOT EXISTS tb_sprinkle (
	seq bigint IDENTITY primary key, 
	user_id int not null,	
	room_id varchar(10) not null,
	token char(3) not null, 
	sprinkle_amt long not null,
	receiver_count int default 0, 
	reg_date datetime	
) engine=InnoDB;

create table IF NOT EXISTS tb_distrbt (
	seq bigint IDENTITY primary key,
	sprinkle_seq bigint not null, 
	token char(3) not null, 
	rcv_room_id varchar(20) not null, 
	rcv_user_id varchar(20),
	rcv_amt long not null,
	received_yn boolean default false, 
	reg_date datetime	
) engine=InnoDB;