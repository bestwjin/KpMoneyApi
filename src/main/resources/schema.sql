create table tb_sprinkle_money (
	token char(3) primary key, 
	user_id varchar() not null,  
	sprinkle_date date,  
	sprinkle_amt bigint 
);