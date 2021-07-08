--sys use prop
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GLOBAL_REG','Y','Y为允许再次注册O365管理员',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_PASSWORD','Mjj@1234','Office用户的默认密码',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_ADMIN_ROLE_ID','62e90394-69f5-4237-9190-012177145e10','默认授予或撤回的管理员角色ID',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_1','62e90394-69f5-4237-9190-012177145e10','Global Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_2','fe930be7-5e62-47db-91af-98c3a49a38b1','User Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_3','e8611ab8-c189-46e8-94e1-60213ab1f814','Privileged Role Administrator',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('SEARCH_ROLE_4','7be44c8a-adaf-4e2a-84d6-ab2649e08a13','Privileged Authentication Administrator',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('CUSTOM_USAGE_LOCATION_IND','N','Y为允许自定义新建用户位置,N为新建用户的位置跟随全局位置',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('DEFAULT_USAGE_LOCATION','US','默认位置,仅在CUSTOM_USAGE_LOCATION_IND=Y时有效',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT','N','Y为自动开启overall report',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT_CRON','0 0 4 */2 * ?','The overall report cron schedule',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT_SEED','1000','The maximum delay mins for the cron GEN_APP_RPT_CRON(Range:1~1000)',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT_DELAY_MINS_AUTO','15','the maximum delay mins when process next APP automatically',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('GEN_APP_RPT_DELAY_MINS_MANUAL','0','the maximum delay mins when process next APP manually',sysdate,null,sysdate,'o365',sysdate);

insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('FORCE_CHANGE_PASSWORD','Y','Y为强制新用户在第一次登录时修改密码',sysdate,null,sysdate,'o365',sysdate);

--license desc
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('c42b9cae-ea4f-4ab7-9717-81576235ccac','DEVELOPERPACK_E5','E5 开发者',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('f5a9147f-b4f8-4924-a9f0-8fadaac4982f','ENTERPRISEPACK_EDULRG','E3 远程教育',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('e4fa3838-3d01-42df-aa28-5e0a4c68604b','ENTERPRISEPACK_FACULTY','E3 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('8fc2205d-4e51-4401-97f0-5c89ef1aafbb','ENTERPRISEPACK_STUDENT','E3 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('98b6e773-24d4-4c0d-a968-6e787a1f8204','ENTERPRISEPACKPLUS_STUDENT','A3 学生（O365）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('476aad1e-7a7f-473c-9d20-35665a5cbd4f','ENTERPRISEPACKPLUS_STUUSEBNFT','A3 学生使用权益',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('a4585165-0533-458a-97e3-c400570268c4','ENTERPRISEPREMIUM_FACULTY','E5 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('9a320620-ca3d-4705-a79d-27c135c96e05','ENTERPRISEPREMIUM_NOPSTNCONF_FACULTY','E5 教职员工（不含音频会议）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('1164451b-e2e5-4c9e-8fa6-e5122d90dbdc','ENTERPRISEPREMIUM_NOPSTNCONF_STUDENT','E5 学生（不含音频会议）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('bc86c9cd-3058-43ba-9972-141678675ac1','ENTERPRISEPREMIUM_NOPSTNCONF_STUUSEBNFT','A5 学生音频会议',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('ee656612-49fa-43e5-b67e-cb1fdf7699df','ENTERPRISEPREMIUM_STUDENT','E5 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('f6e603f1-1a6d-4d32-a730-34b809cb9731','ENTERPRISEPREMIUM_STUUSEBNFT','A5 学生权益（O365）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('16732e85-c0e3-438e-a82f-71f39cbe2acb','ENTERPRISEWITHSCAL_FACULTY','E4 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('05e8cabf-68b5-480f-a930-2143d472d959','ENTERPRISEWITHSCAL_STUDENT','E4 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('f30db892-07e9-47e9-837c-80727f46fd3d','FLOW_FREE','FLOW_FREE',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('4b590615-0888-425a-a965-b3bf7789848d','M365EDU_A3_FACULTY','A3 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('7cfd9a2b-e110-4c39-bf20-c6a3f36a3121','M365EDU_A3_STUDENT','A3 学生（MS365）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('18250162-5d87-4436-a834-d795c15c80f3','M365EDU_A3_STUUSEBNFT','A3 学生权益',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('e97c048c-37a4-45fb-ab50-922fbf07a370','M365EDU_A5_FACULTY','A5 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('e578b273-6db4-4691-bba0-8d691f4da603','M365EDU_A5_NOPSTNCONF_FACULTY','A5 教职员工（不含音频会议）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('a25c01ce-bab1-47e9-a6d0-ebe939b99ff9','M365EDU_A5_NOPSTNCONF_STUDENT','A5 学生（不含音频会议）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('81441ae1-0b31-4185-a6c0-32b6b84d419f','M365EDU_A5_NOPSTNCONF_STUUSEBNFT','A5 学生权益（不含音频会议）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('46c119d4-0379-4a9d-85e4-97c66d3f909e','M365EDU_A5_STUDENT','A5 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('31d57bc7-3a05-4867-ab53-97a17835a411','M365EDU_A5_STUUSEBNFT','A5 学生权益（MS365）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('3b555118-da6a-4418-894f-7df1e2096870','O365_BUSINESS_ESSENTIALS','MS365商业基础工程反馈计划',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('a403ebcc-fae0-4ca2-8c8c-7a907fd6c235','POWER_BI_STANDARD','POWER_BI_STANDARD',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('8c4ce438-32a7-4ac5-91a6-e22ae08d9c8b','RIGHTSMANAGEMENT_ADHOC','权益管理（手动）',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('a19037fc-48b4-4d57-b079-ce44b7832473','STANDARDPACK_FACULTY','E1 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('d37ba356-38c5-4c82-90da-3d714f72a382','STANDARDPACK_STUDENT','E1 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('94763226-9b3c-4e75-a931-5c89701abe66','STANDARDWOFFPACK_FACULTY','A1 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('af4e28de-6b52-4fd3-a5f4-6bf708a304d3','STANDARDWOFFPACK_FACULTY_DEVICE','A1 教职员工 设备',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('43e691ad-1491-4e8c-8dc9-da6b8262c03b','STANDARDWOFFPACK_HOMESCHOOL_FAC','自学教育 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('afbb89a7-db5f-45fb-8af0-1bc5c5015709','STANDARDWOFFPACK_HOMESCHOOL_STU','自学教育 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('78e66a63-337a-4a9a-8959-41c6654dfb56','STANDARDWOFFPACK_IW_FACULTY','A1P 教职员工',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('314c4481-f395-4525-be8b-2ec4bb1e9d91','STANDARDWOFFPACK_STUDENT','A1 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('e82ae690-a2d5-4d76-8d30-7c6e01e6022e','STANDARDWOFFPACK_IW_STUDENT','A1P 学生',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('160d609e-ab08-4fce-bc1c-ea13321942ac','STANDARDWOFFPACK_STUDENT_DEVICE','A1 学生 设备',sysdate,null,sysdate,'o365',sysdate);

--update mask info
update ta_office_info set mask_app_id='*****',mask_secret_id='*****';

--update wx info
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_IND','N','Y to enable the login verify on wx',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_TOKEN','','callback token',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CORPID','','corp id',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CORPSECRET','','corp secret',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_AGENTID','','corp agent id',sysdate,null,sysdate,'o365',sysdate);
insert into ta_master_cd(key_ty,cd,decode,start_dt,end_dt,create_dt,last_update_id,last_update_dt) values ('WX_CALLBACK_AESKEY','','callback aeskey',sysdate,null,sysdate,'o365',sysdate);


commit;
