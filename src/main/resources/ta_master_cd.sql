
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GLOBAL_REG','Y','turn on the function to register the admin user',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_PASSWORD','Mjj@1234','the default password for the office new created user',sysdate,null,sysdate,'o365',sysdate);
commit;
