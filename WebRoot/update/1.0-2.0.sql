/** 店铺新增结算信息**/
alter table es_store ADD commission decimal(20,2);
alter table es_store ADD bank_account_name VARCHAR(50);
alter table es_store ADD bank_account_number VARCHAR(50);
alter table es_store ADD bank_name VARCHAR(50);
alter table es_store ADD bank_code VARCHAR(50);
alter table es_store ADD bank_province VARCHAR(50);
alter table es_store ADD bank_city VARCHAR(50);
alter table es_store ADD bank_region VARCHAR(50);
alter table es_store ADD bank_provinceid int;
alter table es_store ADD bank_cityid int;
alter table es_store ADD bank_regionid int;
alter table es_order ADD commission decimal(20,2);


/** 订单增加 结算信息**/
alter table es_order ADD commission decimal(20,2);
alter table es_order ADD bill_status int(10); 
alter table es_order ADD bill_sn varchar(50); 

/** 退货单信息**/
alter table es_sellback_list ADD store_id int(10);
alter table es_sellback_list ADD bill_status int(10); 
alter table es_sellback_list ADD bill_sn varchar(50); 

/** 红包添加**/
alter table es_bonus_type ADD limit_num int(10);
alter table es_bonus_type ADD is_given int(10);

/** 新增店铺物流公司引用表 **/
CREATE TABLE es_store_logi_rel(
	logi_id int(10) ,
	store_id int(10)
);

/** 新增结算表 **/
CREATE TABLE es_bill(
	bill_id int(10) PRIMARY KEY,
	name varchar(255),
	start_time long(255),
	end_time long(255),
	price decimal(20,2),
	order_price decimal(20,2),
	commi_price decimal(20,2),
	returned_price decimal(20,2),
	returned_commi_price decimal(20,2)
);
/**结算详细表 **/
CREATE TABLE es_bill_detail(
	id int(10) PRIMARY KEY,
	sn varchar(255),
	bill_id int(10),
	price decimal(20,2),
	status int(10),
	start_time long(255),
	end_time long(255),
	bill_time long(255),
	store_id int(10),
	store_name varchar(255),
	returned_price decimal(20,2),
	returned_commi_price decimal(20,2)
	commi_price decimal(20,2),
	bill_price decimal(20,2)
);
/**2015-6-17 订单添加物流信息 **/
alter table es_order ADD logi_id int;
alter table es_order ADD logi_name VARCHAR(50);

/**订单添加货品json  **/
alter table es_order ADD items_json longtext;
