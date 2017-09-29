-- Create table
create table SGA_COMINFO
(
  ID          inteGER,
  NAME        varchar2(100),
  SHORTNAME   varchar2(50),
  SysCode     varchar2(38),
  Address     varchar2(255),
  PersonCount inteGER,
  LOGO        varchar2(500),
  CreateTime Date,
  status INTEGER,
  IMG VARCHAR2(500),
  realm VARCHAR2(25)
)
;
-- Add comments to the table 
comment on table SGA_COMINFO
  is '��ҵ��Ϣ��';
-- Add comments to the columns 
comment on column SGA_COMINFO.ID
  is 'ID';
comment on column SGA_COMINFO.NAME
  is '��ҵ����';
comment on column SGA_COMINFO.SHORTNAME
  is '��ҵ��ƣ����廪ͬ��';
comment on column SGA_COMINFO.SysCode
  is 'ϵͳ���';
comment on column SGA_COMINFO.Address
  is '��ҵ��ַ';
comment on column SGA_COMINFO.PersonCount
  is '��ҵ����';
comment on column SGA_COMINFO.LOGO
  is '��ҵlogo';
comment on column SGA_COMINFO.STATUS is '��ҵ״̬��-1��ɾ��:0���رգ�1��������';
comment on column SGA_COMINFO.realm is '�򡣴洢ecm������desktop��ֵ��';

alter table sga_cominfo add primary key(id);

-- Create table
create table SGA_COM_DETAIL
(
  ID   inteGER,
  CID  intEGER,
  description clob
)
;
-- Add comments to the table 
comment on table SGA_COM_DETAIL
  is '��ҵ����';
-- Add comments to the columns 
comment on column SGA_COM_DETAIL.ID
  is 'ID';
comment on column SGA_COM_DETAIL.CID
  is '��ҵID';
comment on column SGA_COM_DETAIL.description
  is '��ע��Ϣ';
  
  alter table SGA_COM_DETAIL add primary key(id);
  alter table SGA_COM_DETAIL
  add constraint FK_SGA_COM_DETAIL_CID foreign key (CID)
  references sga_cominfo (ID) on delete cascade;
  
  -- Create table
create table SGA_PROJECT
(
  ID            INTEGER not null,
  NAME          VARCHAR2(100),
  SysCode       VARCHAR2(38),
  TAG           VARCHAR2(255),
  STATUS        INTEGER,
  CASECODE      VARCHAR2(50),
  CREATETIME    DATE,
  REGISTERCOUNT INTEGER,
  JOININCOUNT   INTEGER,
  CID           INTEGER,
  Poster           VARCHAR2(500),
  Direction VARCHAR2(255)
);
-- Add comments to the table 
comment on table SGA_PROJECT
  is '��Ŀ��Ϣ��';
-- Add comments to the columns 
comment on column SGA_PROJECT.ID
  is 'ID';
comment on column SGA_PROJECT.NAME
  is '��Ŀ����';
comment on column SGA_PROJECT.SysCode
  is 'ϵͳ���룬��Ӧcase ��id�š�';
comment on column SGA_PROJECT.TAG
  is '��ǩ';
comment on column SGA_PROJECT.STATUS
  is '��Ŀ״̬��0���رգ�1����ļ�У�2�������У�3����������';
comment on column SGA_PROJECT.CASECODE
  is '������ʶ����ECM��Case identifier��Ӧ';
comment on column SGA_PROJECT.CREATETIME
  is '����ʱ��';
comment on column SGA_PROJECT.REGISTERCOUNT
  is '��������';
comment on column SGA_PROJECT.JOININCOUNT
  is '����ļ����';
comment on column SGA_PROJECT.CID
  is '��ҵID';
comment on column SGA_PROJECT.Poster
  is '��ļ����·��';
comment on column SGA_PROJECT.Direction
  is '��ļ�����磺���������Ե�';
  
  alter table SGA_PROJECT add primary key(id);
  alter table SGA_PROJECT add constraint FK_SGA_PROJECT_CID foreign key(CID)
  references SGA_COMINFO(ID) ON DELETE CASCADE; 
  -- Create/Recreate indexes 
  create unique index IDX_SGA_PROJECT_Syscode on SGA_PROJECT (syscode);
  
  -- Create table
create table SGA_Prj_Detail
(
  ID   inteGER,
  PID  intEGER,
  description VARCHAR2(2000)
)
;
-- Add comments to the table 
comment on table SGA_Prj_DETAIL
  is '��Ŀ����';
-- Add comments to the columns 
comment on column SGA_Prj_DETAIL.ID
  is 'ID';
comment on column SGA_Prj_DETAIL.PID
  is '��Ŀ����ID��';
comment on column SGA_Prj_DETAIL.description
  is '��ע��Ϣ';
  
  alter table SGA_Prj_DETAIL add primary key(id);
  alter table SGA_PRJ_DETAIL
  add constraint FK_SGA_Prj_DETAIL_PID foreign key (PID)
  references sga_project (ID) on delete cascade;


  -- Create table
create table SGA_COM_USER
(
  ID  inteGER,
  CID inteGER,
  UserID inteGER,
  Status inteGER
)
;
-- Add comments to the table 
comment on table SGA_COM_USER
  is '��ҵ���û�������';
-- Add comments to the columns 
comment on column SGA_COM_USER.ID
  is 'ID';
comment on column SGA_COM_USER.CID
  is '��ҵID';
comment on column SGA_COM_USER.UserID
  is '�û�ID';
comment on column SGA_COM_USER.Status
  is '�û�����ҵ�е�״̬��-2��δ������-1���ܾ���0������ˣ�1�����룻2������';  
  
 alter table SGA_COM_USER add primary key(id);
 
 -- Create table
create table SGA_USER
(
  ID         inteGER,
  LOGINNAME  varchar2(50),
  USERNAME   varchar2(100),
  SYSCODE    varchar2(38),
  SEX        varchar2(2),
  Position   varchar2(100),
  Telephone  varchar2(11),
  Email      varchar2(50),
  unit       varchar2(255),
  WeChat     varchar2(50),
  avatar     varchar2(255),
  Status     inteGER,
  CreateTime date,
  RegisterType INTEGER,
  Sources INTEGER,
  USERPWD VARCHAR2(256)
)
;
-- Add comments to the table 
comment on table SGA_USER
  is '�û���';
-- Add comments to the columns 
comment on column SGA_USER.ID
  is 'ID';
comment on column SGA_USER.LOGINNAME
  is '��¼��';
comment on column SGA_USER.USERNAME
  is '�û���';
comment on column SGA_USER.SYSCODE
  is 'ϵͳ����';
comment on column SGA_USER.SEX
  is '�Ա��У�m��Ů��f';
comment on column SGA_USER.Position
  is 'ְλ';
comment on column SGA_USER.Telephone
  is '�ֻ�����';
comment on column SGA_USER.Email
  is '����';
comment on column SGA_USER.unit
  is '��λ';
comment on column SGA_USER.WeChat
  is '΢�ź�';
comment on column SGA_USER.avatar
  is 'ͷ����Ϣ';
comment on column SGA_USER.Status
  is '״̬��-1����ְ���߱�ɾ����0������1������';
comment on column SGA_USER.CreateTime
  is 'ע��ʱ��';
comment on column SGA_USER.RegisterType
  is 'ע�����͡�0��Ĭ������ע�᣻1����Ŀ������2����Ŀ���룻3����ҵ����'; 
comment on column SGA_USER.USERPWD
  is '�û�����'; 

alter table sga_user add primary key(id);

-- Create table
create table SGA_Prj_User
(
  ID      inteGER,
  PID     inteGER,
  UserID  inteGER,
  Status inteGER,
  description    varchar2(255),
  SORTID      INTEGER,
  CreateTime DATE,
  direction VARCHAR2(20)
)
;
-- Add comments to the table 
comment on table SGA_Prj_User
  is '��Ŀ����Ա�Ĺ�����';
-- Add comments to the columns 
comment on column SGA_Prj_User.ID
  is 'ID';
comment on column SGA_Prj_User.PID
  is '��ĿID';
comment on column SGA_Prj_User.UserID
  is '�û�ID';
comment on column SGA_Prj_User.Status
  is '�û�����Ŀ�е�״̬��-1���ܾ���0������ˣ�1�����룻2������';
comment on column SGA_Prj_User.description
  is '��ע';
comment on column SGA_Prj_User.SortId
  is '�����';
comment on column SGA_PRJ_USER.CreateTime is '������Ŀ���ʱ��';
comment on column SGA_PRJ_USER.direction is '���򡣿��������ԡ�����ȵ�';

alter table SGA_Prj_User add primary key(id);

-- Create table
create table SGA_USER_ATTACHMENT
(
  ID         inteGER,
  USERID     inteGER,
  AttachID   varchar2(38),
  AttachName varchar2(255),
  CreateTime date
)
;
-- Add comments to the table 
comment on table SGA_USER_ATTACHMENT
  is '�û�������';
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ID
  is 'ID';
comment on column SGA_USER_ATTACHMENT.USERID
  is '�û�ID';
comment on column SGA_USER_ATTACHMENT.AttachID
  is '����ID';
comment on column SGA_USER_ATTACHMENT.AttachName
  is '��������';
comment on column SGA_USER_ATTACHMENT.CreateTime
  is '����ʱ��';

alter table SGA_USER_ATTACHMENT add primary key(id);

 -- Create sequence 
create sequence SEQ_SGA_OID
minvalue 1
maxvalue 999999999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create table
create table SGA_InviteQueue
(
  ID         inteGER,
  SYSCODE    varchar2(38),
  CREATETIME date,
  EXPIRES date,
  Mark INTEGER,
  RegisterType INTEGER 
)
;
-- Add comments to the table 
comment on table SGA_InviteQueue
  is '�������';
-- Add comments to the columns 
comment on column SGA_InviteQueue.ID
  is 'ID';
comment on column SGA_InviteQueue.SYSCODE
  is 'ϵͳ��ţ���Ӧ��RegisterType���Ե�ֵ�����RegisterType=0�����Ǹ���guid�����RegisterType=1��������Ŀguid';
comment on column SGA_InviteQueue.CREATETIME
  is '����ʱ��';
comment on column SGA_InviteQueue.EXPIRES
  is '��Ч��';
comment on column SGA_INVITEQUEUE.Mark
  is '��ǡ�0���´�����1�������������룬δ���ע�ᣨֻ�ǵ�����ӣ���2�������������룬�����ע�᣻3��δ�������룬�Զ����ڣ�4���������룬���ѹ��ڣ�';
comment on column SGA_INVITEQUEUE.RegisterType is 'ע�����͡�0��Ĭ������ע�᣻1����Ŀ������2����Ŀ���룻3����ҵ����';

alter table SGA_InviteQueue add primary key(ID);

--�����ҵ��ͳ����Ϣ
create table SGA_COM_Statistics
(
  ID   inteGER,
  CID  intEGER,
  ProjectCount INTEGER,
  RegisterCount INTEGER,
  JoininCount INTEGER
);

comment on table SGA_COM_STATISTICS
  is '��ҵͳ����Ϣ';
  
alter table SGA_COM_STATISTICS add primary key(id);
alter table SGA_COM_STATISTICS
  add constraint FK_SGA_COM_STATISTICS_CID foreign key (CID)
  references SGA_COMINFO (ID) on delete cascade;

comment on column SGA_COM_STATISTICS.ID
  is '����ID';
comment on column SGA_COM_STATISTICS.CID
  is '��ҵID';
comment on column SGA_COM_STATISTICS.PROJECTCOUNT
  is '��Ŀ����';
comment on column SGA_COM_STATISTICS.REGISTERCOUNT
  is '��������';
comment on column SGA_COM_STATISTICS.JOININCOUNT
  is '��ļ����';

-- Create table
create table SGA_FileRecord
(
  ID          INTEGER not null,
  RESID       VARCHAR2(38),
  Creator    VARCHAR2(38),
  CREATETIME  DATE,
  RESTYPECODE VARCHAR2(50),
  DOMAINCODE  VARCHAR2(50),
  Agent       VARCHAR2(38),
  opType VARCHAR(5)
);
-- Add comments to the columns 
comment on column SGA_FileRecord.ID
  is '����id';
comment on column SGA_FileRecord.RESID
  is '��Դid';
comment on column SGA_FileRecord.Creator
  is 'ҵ���ϵĴ�����';
comment on column SGA_FileRecord.CREATETIME
  is '����ʱ��';
comment on column SGA_FileRecord.RESTYPECODE
  is '��Դ����';
comment on column SGA_FileRecord.DOMAINCODE
  is '�ռ������';
comment on column SGA_FileRecord.Agent
  is '������';
comment on column SGA_FileRecord.opType is '�������ͣ��ϴ���u,upload���������أ�d,download��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SGA_FileRecord
  add primary key (ID);
  
  -- Create table
create table SGA_PRJ_WZ
(
  ID         INTEGER,
  PID        INTEGER,
  WZID       VARCHAR2(38),
  CREATOR    VARCHAR2(38),
  CREATETIME DATE
);
-- Add comments to the table 
comment on table SGA_PRJ_WZ
  is '��Ŀ��΢����ϵ';
-- Add comments to the columns 
comment on column SGA_PRJ_WZ.ID
  is '����ID';
comment on column SGA_PRJ_WZ.PID
  is '��Ŀ����ID';
comment on column SGA_PRJ_WZ.WZID
  is '΢��ID';
comment on column SGA_PRJ_WZ.CREATOR
  is '������';
comment on column SGA_PRJ_WZ.CREATETIME
  is '����ʱ��';
  
 alter table SGA_PRJ_WZ add primary key(id);
 alter table SGA_PRJ_WZ
  add constraint FK_SGA_PRJ_WZ_PID foreign key (PID)
  references sga_project (ID) on delete cascade;

--2017-02-06
-- Add/modify columns 
alter table SGA_PROJECT modify REGISTERCOUNT default 0;
alter table SGA_PROJECT modify JOININCOUNT default 0;

--2017-02-22
-- Add/modify columns 
alter table SGA_PROJECT add region varchar2(100);
-- Add comments to the columns 
comment on column SGA_PROJECT.region
  is '����ʡ����';

 alter table SGA_COM_USER add constraint FK_SGA_COM_USER_UID
 foreign key(CID) REFERENCES SGA_COMINFO(ID) 
 ON DELETE CASCADE; 
 alter table SGA_COM_USER add constraint FK_SGA_COM_USER_CID
 foreign key(USERID) REFERENCES SGA_USER(ID)
 ON DELETE CASCADE;
 
alter table SGA_Prj_User add constraint
FK_sga_project_user_PID FOREIGN KEY(PID)
REFERENCES SGA_PROJECT(ID)
ON DELETE CASCADE;
alter table SGA_Prj_User add constraint
FK_SGA_PROJECT_USER_UID foreign key(USERID)
references SGA_USER(ID)
on delete cascade;

-- Add/modify columns�����б�Ҫ�𣿣�
alter table SGA_USER add lasttime date default sysdate;
comment on column SGA_USER.lasttime is '��һ�ε�¼ʱ��'; 

------------------�����Ѹ��µ�aws��2017/03/24

--2017/04/13
--��������ҵ��Ϣ����
rename sga_cominfo to sga_company;
--��������ҵ�����Ϣ����
rename sga_com_statistics to sga_com_audit;
--�û�����id�����û�����
update sga_prj_wz t set t.creator = (select u.syscode from sga_user u where u.id = t.creator);
commit;

------------------�����Ѹ��µ�aws��2017/04/14
--�������
-- Add/modify columns 
alter table SGA_USER_ATTACHMENT add AttachType inteGER;
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.AttachType
  is '�������͡�0������';
  
-- Add/modify columns 
alter table SGA_USER_ATTACHMENT rename column ATTACHID to ATTACHVID;
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ATTACHVID
  is '�����汾ϵ��ID';
 
insert into sga_user(id,loginname,username,syscode,sex,position,status,registertype,createtime,lasttime)
values(seq_sga_oid.nextval, 'admin','����Ա','1DCC08EC-7BA3-4BA6-A4B2-42055F75712C','m','����',1,0,sysdate,sysdate);
commit;
------------------�����Ѹ��µ�aws��2017/04/18

-- Add/modify columns 
alter table SGA_USER add address varchar2(100);
-- Add comments to the columns 
comment on column SGA_USER.address
  is '��ϵ��ַ';
 
------------------�����Ѹ��µ�aws��2017/04/25
  
-- Add/modify columns 
alter table SGA_USER add openid varchar2(50);
-- Add comments to the columns 
comment on column SGA_USER.openid
  is '΢��Ӧ��Ψһid';

------------------�����Ѹ��µ�aws��2017/04/25
--2017/04/26
-- Add comments to the columns 
comment on column SGA_PROJECT.REGISTERCOUNT
  is '����������������20170426��';
comment on column SGA_PROJECT.JOININCOUNT
  is '����ļ������������20170426��';

--2017/04/27
-- Add/modify columns 
alter table SGA_PRJ_USER add formid varchar2(32);
-- Add comments to the columns 
comment on column SGA_PRJ_USER.formid
  is '����΢��С����ı�id����ʱ֮�ã�';
 
------------------�����Ѹ��µ�aws��2017/04/27
-- Add comments to the columns 
comment on column SGA_PRJ_USER.STATUS
  is '�û�����Ŀ�е�״̬��-1���ܾ���0������ˣ�1�����룻2������';

------------------�����Ѹ��µ�aws��2017/04/30
 -- Add/modify columns 
alter table SGA_PROJECT add posterNAME varchar2(25);
-- Add comments to the columns 
comment on column SGA_PROJECT.posterNAME
  is '��������';
  
------------------�����Ѹ��µ�aws��2017/05/03

 -- Add/modify columns 
alter table sga_company add logoNAME varchar2(25);
-- Add comments to the columns 
comment on column sga_company.logoNAME
  is 'logo����';
  
   -- Add/modify columns 
alter table sga_company add imgNAME varchar2(25);
-- Add comments to the columns 
comment on column sga_company.imgNAME
  is '����ͼ����';
  
  ------------------�����Ѹ��µ�aws��2017/05/17
  
  -- Add/modify columns 
alter table SGA_USER_ATTACHMENT add MIMETYPE VARCHAR2(50);
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.MIMETYPE
  is '�ļ�����';
  
   ------------------�����Ѹ��µ�aws��2017/06/17
   
  -- Add/modify columns 
alter table SGA_USER_ATTACHMENT add ATTACHID varchar2(38);
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ATTACHID
  is '����id';

   ------------------�����Ѹ��µ�aws��2017/07/03