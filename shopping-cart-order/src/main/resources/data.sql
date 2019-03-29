insert into order_table(order_id,cust_id,item_sku,ord_quantity)
values(1,1,1,2);
insert into order_table(order_id,cust_id,item_sku,ord_quantity)
values(2,2,2,3);
insert into order_table(order_id,cust_id,item_sku,ord_quantity)
values(3,3,3,4);

alter sequence hibernate_sequence restart with 4;