create table IF NOT EXISTS tb_sprinkle_money (
	seq int auto_increment primary key, 
	user_id int not null,	
	room_id varchar(10) not null,
	token char(3) not null, 
	sprinkle_amt long not null,
	receiver_count int default 0, 
	reg_date datetime	
) engine=InnoDB;

create table IF NOT EXISTS tb_rcv_money (
	token char(3) not null primary key, 
	rcv_user_id varchar(20) not null,	
	rcv_amt long,
	reg_date datetime
) engine=InnoDB;