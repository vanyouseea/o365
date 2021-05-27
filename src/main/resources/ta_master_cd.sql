--sys use prop
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GLOBAL_REG','Y','Y to turn on the function to register the admin user',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_PASSWORD','Mjj@1234','the default password for the new created office user',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_ADMIN_ROLE_ID','62e90394-69f5-4237-9190-012177145e10','the default admin role to grant or revoke',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_1','62e90394-69f5-4237-9190-012177145e10','Global Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_2','fe930be7-5e62-47db-91af-98c3a49a38b1','User Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_3','e8611ab8-c189-46e8-94e1-60213ab1f814','Privileged Role Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_4','7be44c8a-adaf-4e2a-84d6-ab2649e08a13','Privileged Authentication Administrator',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('CUSTOM_USAGE_LOCATION_IND','N','Y to turn on the custom location function, if N, new office user usagelocation is same as organization',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_USAGE_LOCATION','US','the default location for the new created office user, only valid when CUSTOM_USAGE_LOCATION_IND=Y',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT','N','Y to turn on the app overall report everyday',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT_RANDOM_SEED','1000','the report default start date is 00:00 + (0~1000)mins everyday',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('FORCE_CHANGE_PASSWORD','Y','Y to force user change the password when they login',sysdate,null,sysdate,'o365',sysdate);


commit;
