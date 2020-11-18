create table IF NOT EXISTS tb_sprinkle_money (
	token char(3) not null primary key, 
	user_id varchar(20) not null,	
	sprinkle_amt long,
	receiver_count int default 0, 
	reg_date datetime	
) engine=InnoDB;

create table IF NOT EXISTS tb_rcv_money (
	token char(3) not null primary key, 
	rcv_user_id varchar(20) not null,	
	rcv_amt long,
	reg_date datetime
) engine=InnoDB;

