--sysdate use prop
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GLOBAL_REG','Y','turn on the function to register the admin user',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_PASSWORD','Mjj@1234','the default password for the office new created user',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_ADMIN_ROLE_ID','62e90394-69f5-4237-9190-012177145e10','the default admin role to grant or revoke',sysdate,null,sysdate,'o365',sysdate);

commit;
