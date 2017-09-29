/**
一、规划专业部：
专业总工：方正兴
高级专员：李凡

二、规划研究中心_低碳城市规划研究部

所领导：王建军
所管理员：聂婷
项目负责人：聂婷
规划师（所成员）：雷狄
*/

--用户和机构
--用户名：方正兴
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '方正兴';
 
 --角色：专业总工
 select * from dcm_userdomainrole udr where udr.userid = 5510 ;
 
 insert into dcm_userdomainrole
 values
   (seq_dcm_oid.nextval,
    5510,
    '00988c05-3774-11e6-bbd7-990e44830ff2',
    'Domain_Department',
    'R_Specialized_Engineer',
    sysdate);
    commit;
    
    
    --用户名：李凡
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '李凡';
 
 --角色：高级专员
 select * from dcm_userdomainrole udr where udr.userid = 5511 ;
 
 insert into dcm_userdomainrole
 values
   (seq_dcm_oid.nextval,
    5511,
    '00988c05-3774-11e6-bbd7-990e44830ff2',
    'Domain_Department',
    'R_Senior_Commissioner',
    sysdate);
    commit;
    
    
    
        --用户名：王建军
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '王建军';
 
 --角色：所领导
 select * from dcm_userdomainrole udr where udr.userid = 5489 ;
 
 insert into dcm_userdomainrole
 values
   (seq_dcm_oid.nextval,
    5489,
    '0093f81b-3774-11e6-bbd7-990e44830ff2',
    'Domain_Department',
    'R_Department_Leader',
    sysdate);
    commit;
    
    
    
     --用户名：聂婷
     
     insert into dcm_user(id,username,loginname,userpwd,usercode,email,currentstatus,datecreated,datelastactivity,telephone,domaintype,sex,isbuildin,avatar)
values
  (SEQ_DCM_OID.nextval,
   '聂婷',
   '聂婷',
   '3B2C0435BA0B325E54C868E85391F5',
   'UC0001000001',
   '',
   1,
   sysdate,
   sysdate,
   '',
   'Domain_Person',
   '女',
   0,
   'avatar/default/nf.jpg');
   commit;
   
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '聂婷';
 
 insert into dcm_org_user(id,userid,orgid)
 values(seq_dcm_oid.nextval, 20698, 20345);
 
 --角色：所管理员
 select * from dcm_userdomainrole udr where udr.userid = 20698 ;
 
 insert into dcm_userdomainrole
 values
   (seq_dcm_oid.nextval,
    20698,
    '0093f81b-3774-11e6-bbd7-990e44830ff2',
    'Domain_Department',
    'R_Department_Manager',
    sysdate);
    commit;
    
    
    --用户名：雷狄
         insert into dcm_user(id,username,loginname,userpwd,usercode,email,currentstatus,datecreated,datelastactivity,telephone,domaintype,sex,isbuildin,avatar)
values
  (SEQ_DCM_OID.nextval,
   '雷狄',
   '雷狄',
   '3B8F94FD35278F09FA192DDC4CCFD8',
   'UC0001000002',
   '',
   1,
   sysdate,
   sysdate,
   '',
   'Domain_Person',
   '男',
   0,
   'avatar/default/lm.jpg');
   commit;
    
    select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '雷狄';
 
     insert into dcm_org_user(id,userid,orgid)
 values(seq_dcm_oid.nextval, 20699, 20345);
 
 --角色：规划师（所员）
  select * from dcm_userdomainrole udr where udr.userid = 20699 ;
  
  insert into dcm_userdomainrole
 values
   (seq_dcm_oid.nextval,
    20699,
    '0093f81b-3774-11e6-bbd7-990e44830ff2',
    'Domain_Department',
    'R_Department_Member',
    sysdate);
    commit;
 