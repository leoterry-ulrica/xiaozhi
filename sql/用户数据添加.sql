/**
һ���滮רҵ����
רҵ�ܹ���������
�߼�רԱ���

�����滮�о�����_��̼���й滮�о���

���쵼��������
������Ա������
��Ŀ�����ˣ�����
�滮ʦ������Ա�����׵�
*/

--�û��ͻ���
--�û�����������
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '������';
 
 --��ɫ��רҵ�ܹ�
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
    
    
    --�û������
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '�';
 
 --��ɫ���߼�רԱ
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
    
    
    
        --�û�����������
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '������';
 
 --��ɫ�����쵼
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
    
    
    
     --�û���������
     
     insert into dcm_user(id,username,loginname,userpwd,usercode,email,currentstatus,datecreated,datelastactivity,telephone,domaintype,sex,isbuildin,avatar)
values
  (SEQ_DCM_OID.nextval,
   '����',
   '����',
   '3B2C0435BA0B325E54C868E85391F5',
   'UC0001000001',
   '',
   1,
   sysdate,
   sysdate,
   '',
   'Domain_Person',
   'Ů',
   0,
   'avatar/default/nf.jpg');
   commit;
   
select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '����';
 
 insert into dcm_org_user(id,userid,orgid)
 values(seq_dcm_oid.nextval, 20698, 20345);
 
 --��ɫ��������Ա
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
    
    
    --�û������׵�
         insert into dcm_user(id,username,loginname,userpwd,usercode,email,currentstatus,datecreated,datelastactivity,telephone,domaintype,sex,isbuildin,avatar)
values
  (SEQ_DCM_OID.nextval,
   '�׵�',
   '�׵�',
   '3B8F94FD35278F09FA192DDC4CCFD8',
   'UC0001000002',
   '',
   1,
   sysdate,
   sysdate,
   '',
   'Domain_Person',
   '��',
   0,
   'avatar/default/lm.jpg');
   commit;
    
    select u.id userid, u.username, u.loginname, o.id orgid, o.orgname, o.orgcode
  from dcm_org_user org2user
 inner join dcm_user u on org2user.userid = u.id
 inner join dcm_organization o on org2user.orgid = o.id
 where u.loginname = '�׵�';
 
     insert into dcm_org_user(id,userid,orgid)
 values(seq_dcm_oid.nextval, 20699, 20345);
 
 --��ɫ���滮ʦ����Ա��
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
 