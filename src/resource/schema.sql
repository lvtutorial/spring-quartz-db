--- Create table USERS------------
 CREATE TABLE USERS (  
  ID            NUMBER(19) not null,
  CREATED_BY    VARCHAR2(255 CHAR),
  CREATED_DATE  TIMESTAMP(6),
  FULLNAME      VARCHAR2(255 CHAR),
  LDAP          NUMBER(1),
  MODIFIED_BY   VARCHAR2(255 CHAR),
  MODIFIED_DATE TIMESTAMP(6),
  PASSWORD      VARCHAR2(255 CHAR),
  USERNAME      VARCHAR2(50 CHAR),
  LANG          VARCHAR2(50 CHAR),
  EMAIL         VARCHAR2(255 CHAR)
 );  
 -- Create/Recreate primary, unique and foreign key constraints 
alter table USERS  add primary key (ID)  using index;

-- Create table ROLES-----
create table ROLES
(
  ID   NUMBER(19) not null,
  CODE VARCHAR2(50 CHAR),
  NAME VARCHAR2(255 CHAR)
);
alter table ROLES  add primary key (ID)  using index; 

-- Create table USER_ROLE
create table USER_ROLE
(
  USER_ID NUMBER(19) not null,
  ROLE_ID NUMBER(19) not null
);
alter table USER_ROLE  add primary key (USER_ID, ROLE_ID)  using index;
alter table USER_ROLE  add constraint FKBC16F46A56DE2B75 foreign key (USER_ID)  references USERS (ID);
alter table USER_ROLE  add constraint FKBC16F46AB188D39F foreign key (ROLE_ID)  references ROLES (ID);
 
--insert data
--roles 
insert into roles (ID, CODE, NAME)
values (1, 'A01', 'admin');
insert into roles (ID, CODE, NAME)
values (2, 'A02', 'user');
--users
insert into users (ID, CREATED_BY, CREATED_DATE, FULLNAME, LDAP, MODIFIED_BY, MODIFIED_DATE, PASSWORD, USERNAME)
values (1, '', '', 'Tran Ngoc Linh', 0, '', '', 'e4bfe7ba80f3f7d84b0edff3d941f956', 'ngoclinh.tran');
--user_role
insert into user_role (ROLE_ID, USER_ID)
values (1, 1);
insert into user_role (ROLE_ID, USER_ID)
values (2, 1);
--------

-- Create table
create table JOB_INFO
(
  ID            NUMBER(19) not null,
  CREATED_BY    VARCHAR2(255 CHAR),
  CREATED_DATE  DATE,
  ENABLE        NUMBER(1) default 0 not null,
  JOB_NAME      VARCHAR2(255 CHAR),
  JOB_SCHEDULE  VARCHAR2(100 CHAR),
  MODIFIED_BY   VARCHAR2(255 CHAR),
  MODIFIED_DATE DATE,
  PATH_FILE     VARCHAR2(500 CHAR),
  MANUAL_FILE   VARCHAR2(500 CHAR)
);
alter table JOB_INFO  add primary key (ID)  using index;
insert into JOB_INFO (ID, CREATED_BY, CREATED_DATE, ENABLE, JOB_NAME, JOB_SCHEDULE, MODIFIED_BY, MODIFIED_DATE, PATH_FILE, MANUAL_FILE)
values (1, 'ngoclinh.tran', to_date('26-09-2016', 'dd-mm-yyyy'), 0, 'job1', '26 10 * * *', 'ngoclinh.tran', to_date('14-02-2017', 'dd-mm-yyyy'), '/usr/share/jboss-as-7.1.1.Final/test_bi', '/usr/share/jboss-as-7.1.1.Final/bi_export_manual/');

--job message
create table JOB_MESSAGE
(
  ID           NUMBER(19) not null,
  CREATED_BY   VARCHAR2(255 CHAR),
  CREATED_DATE TIMESTAMP(6),
  FILE_NAME    VARCHAR2(500 CHAR),
  STATUS       VARCHAR2(500 CHAR),
  TYPE         VARCHAR2(255 CHAR)
);
alter table JOB_MESSAGE add primary key (ID) using index;

--create table to store upload excel
create table TMPFCFULLNV3
(
  NO      BINARY_DOUBLE,
  FCCODE  BINARY_DOUBLE not null,
  FCNAME  NVARCHAR2(255),
  GENDER  NVARCHAR2(255),
  DOB     DATE,
  IDNO    DATE,
  EFFDATE DATE,
  REASON  NVARCHAR2(255),
  YMD     VARCHAR2(8)
);
create index TMPFC_IDX on TMPFCFULLNV3 (FCCODE, YMD);

create or replace synonym P_IFS_AUTO_TERMINATION
for kli_pre_prod.P_IFS_AUTO_TERMINATION@AGTP_EBAO.KOREALIFE.COM.VN

-------on ebao
--create dblink on ebao 
create database link IFS_AGTP.KOREALIFE.COM.VN
  connect to IFS identified by "ifs@123456"
  using 'agtp';
--create syn
create synonym TMPFCFULLNV3
for IFS.TMPFCFULLNV3@IFS_AGTP.KOREALIFE.COM.VN;

--create store procedure on KLI_PROD and grant to IFS
CREATE OR REPLACE PROCEDURE P_IFS_AUTO_TERMINATION(p_date in varchar2)
is
       v_change_id number;
       v_log_id    number;
       v_sys_date  date;
       l_date date:=to_date(p_date,'yyyyMMdd');
begin
       -- Test statements here
       pkg_pub_app_context.P_SET_APP_USER_ID(400);
       PKG_LS_CS_CI.p_generateChange(pkg_ls_pub_code_cst.CHANGE_TYPE__PARTY_CHG, pkg_pub_app_context.F_GET_APP_USER_ID, v_change_id);
       v_sys_date := pkg_pub_app_context.F_GET_USER_LOCAL_TIME;
       for cc in (select * from t_agent where AGENT_CODE IN (SELECT  FCCODE FROM  TMPFCFULLNV3 where YMD=p_date and FCCODE is not null)) loop
           select s_log_id.nextval into v_log_id from dual;
           insert into t_agent_log(log_id,change_id,log_type,agent_log_type,agent_id,gender,birthday,certi_type,certi_code,agent_status,real_name,
                                   agent_code,dept_id,head_id,branch_id,organ_id,agent_cate,log_comment,log_date,insert_time,agent_grade)
           values(v_log_id,v_change_id,'1','1',cc.agent_id,cc.gender,cc.birthday,cc.certi_type,cc.certi_code,cc.agent_status,cc.real_name,
                  cc.agent_code,cc.dept_id,cc.head_id,cc.branch_id,cc.organ_id,cc.agent_cate,'Patch as userrequest',v_sys_date,v_sys_date,cc.agent_grade);

       UPDATE  T_AGENT
         SET     agent_status = 1,--Termination
                status_reason = 1, -- not validate
            --   status_reason = 11, -- other
                 STATUS_DATE = l_date --Cancelation date
       WHERE   agent_id = cc.agent_id;
      end loop;
      --dbms_output.put_line('test nha '|| p_date);
end;
/

--on KLI_PROD
GRANT EXECUTE ON P_IFS_AUTO_TERMINATION to AGTP_PRE with grant option;


---20/04/2017
--on AGTP
-- Create table
create table USER_JOB
(
  USER_ID NUMBER(19) not null,
  JOB_ID  NUMBER(19) not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table USER_JOB
  add primary key (USER_ID, JOB_ID)
  using index;
alter table USER_JOB
  add constraint FK1ED755291ACF1E43 foreign key (JOB_ID)
  references USERS (ID);
alter table USER_JOB
  add constraint FK1ED75529AE0F8BF8 foreign key (USER_ID)
  references JOB_INFO (ID);
  
--on KLI_PROD 
grant select on t_batch_job_run to AGTP_PRO with grant option;
grant select on t_batch_job to AGTP_PRO with grant option;
grant select on t_batch_job_status to AGTP_PRO with grant option;


--on AGTP
--remember create synonm for 3 table above
create or replace view ebao_batch_job as
select t2.job_name, t1.status, t1.job_id, t3.status_name, t1.start_time, t1.process_date
from t_batch_job_run t1, t_batch_job t2, t_batch_job_status t3
where t1.job_id=t2.job_id
and t1.status=t3.status_id
and t1.daily_id=(select max(daily_id) from t_batch_job_run)--2934
--and trunc(t1.create_time)=date'2017-04-06'
order by t1.finish_post_time desc; 

---version 2
grant select on dba_tablespace_usage_metrics to ifs with grant option;
grant select on dba_tablespaces to ifs with grant option;
grant select on dba_data_files to ifs with grant option;

--grant to other user call scheduler job
GRANT SCHEDULER_ADMIN TO IFS;
GRANT MANAGE SCHEDULER TO IFS;