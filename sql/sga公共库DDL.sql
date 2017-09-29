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
  is '企业信息表';
-- Add comments to the columns 
comment on column SGA_COMINFO.ID
  is 'ID';
comment on column SGA_COMINFO.NAME
  is '企业名称';
comment on column SGA_COMINFO.SHORTNAME
  is '企业简称，如清华同衡';
comment on column SGA_COMINFO.SysCode
  is '系统编号';
comment on column SGA_COMINFO.Address
  is '企业地址';
comment on column SGA_COMINFO.PersonCount
  is '企业人数';
comment on column SGA_COMINFO.LOGO
  is '企业logo';
comment on column SGA_COMINFO.STATUS is '企业状态。-1：删除:0：关闭；1：正常；';
comment on column SGA_COMINFO.realm is '域。存储ecm中桌面desktop的值。';

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
  is '企业详情';
-- Add comments to the columns 
comment on column SGA_COM_DETAIL.ID
  is 'ID';
comment on column SGA_COM_DETAIL.CID
  is '企业ID';
comment on column SGA_COM_DETAIL.description
  is '备注信息';
  
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
  is '项目信息表';
-- Add comments to the columns 
comment on column SGA_PROJECT.ID
  is 'ID';
comment on column SGA_PROJECT.NAME
  is '项目名称';
comment on column SGA_PROJECT.SysCode
  is '系统编码，对应case 的id号。';
comment on column SGA_PROJECT.TAG
  is '标签';
comment on column SGA_PROJECT.STATUS
  is '项目状态。0：关闭；1：招募中；2：合作中；3：合作结束';
comment on column SGA_PROJECT.CASECODE
  is '案例标识。跟ECM的Case identifier对应';
comment on column SGA_PROJECT.CREATETIME
  is '发布时间';
comment on column SGA_PROJECT.REGISTERCOUNT
  is '报名人数';
comment on column SGA_PROJECT.JOININCOUNT
  is '已招募人数';
comment on column SGA_PROJECT.CID
  is '企业ID';
comment on column SGA_PROJECT.Poster
  is '招募海报路径';
comment on column SGA_PROJECT.Direction
  is '招募方向，如：开发、测试等';
  
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
  is '项目详情';
-- Add comments to the columns 
comment on column SGA_Prj_DETAIL.ID
  is 'ID';
comment on column SGA_Prj_DETAIL.PID
  is '项目序列ID号';
comment on column SGA_Prj_DETAIL.description
  is '备注信息';
  
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
  is '企业与用户关联表';
-- Add comments to the columns 
comment on column SGA_COM_USER.ID
  is 'ID';
comment on column SGA_COM_USER.CID
  is '企业ID';
comment on column SGA_COM_USER.UserID
  is '用户ID';
comment on column SGA_COM_USER.Status
  is '用户在企业中的状态。-2：未报名；-1：拒绝；0：待审核；1：参与；2：待定';  
  
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
  is '用户表';
-- Add comments to the columns 
comment on column SGA_USER.ID
  is 'ID';
comment on column SGA_USER.LOGINNAME
  is '登录名';
comment on column SGA_USER.USERNAME
  is '用户名';
comment on column SGA_USER.SYSCODE
  is '系统编码';
comment on column SGA_USER.SEX
  is '性别。男：m；女：f';
comment on column SGA_USER.Position
  is '职位';
comment on column SGA_USER.Telephone
  is '手机号码';
comment on column SGA_USER.Email
  is '邮箱';
comment on column SGA_USER.unit
  is '单位';
comment on column SGA_USER.WeChat
  is '微信号';
comment on column SGA_USER.avatar
  is '头像信息';
comment on column SGA_USER.Status
  is '状态。-1：离职或者被删除；0：挂起；1：正常';
comment on column SGA_USER.CreateTime
  is '注册时间';
comment on column SGA_USER.RegisterType
  is '注册类型。0：默认自主注册；1：项目报名；2：项目邀请；3：企业邀请'; 
comment on column SGA_USER.USERPWD
  is '用户密码'; 

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
  is '项目与人员的关联表';
-- Add comments to the columns 
comment on column SGA_Prj_User.ID
  is 'ID';
comment on column SGA_Prj_User.PID
  is '项目ID';
comment on column SGA_Prj_User.UserID
  is '用户ID';
comment on column SGA_Prj_User.Status
  is '用户在项目中的状态。-1：拒绝；0：待审核；1：参与；2：待定';
comment on column SGA_Prj_User.description
  is '备注';
comment on column SGA_Prj_User.SortId
  is '排序号';
comment on column SGA_PRJ_USER.CreateTime is '加入项目组的时间';
comment on column SGA_PRJ_USER.direction is '方向。开发、测试、需求等等';

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
  is '用户附件表';
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ID
  is 'ID';
comment on column SGA_USER_ATTACHMENT.USERID
  is '用户ID';
comment on column SGA_USER_ATTACHMENT.AttachID
  is '附件ID';
comment on column SGA_USER_ATTACHMENT.AttachName
  is '附件名称';
comment on column SGA_USER_ATTACHMENT.CreateTime
  is '创建时间';

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
  is '邀请队列';
-- Add comments to the columns 
comment on column SGA_InviteQueue.ID
  is 'ID';
comment on column SGA_InviteQueue.SYSCODE
  is '系统编号，对应着RegisterType属性的值，如果RegisterType=0，则是个人guid；如果RegisterType=1，则是项目guid';
comment on column SGA_InviteQueue.CREATETIME
  is '邀请时间';
comment on column SGA_InviteQueue.EXPIRES
  is '有效期';
comment on column SGA_INVITEQUEUE.Mark
  is '标记。0：新创建；1：正常接受邀请，未完成注册（只是点击链接）；2：正常接受邀请，并完成注册；3：未接受邀请，自动过期：4：接受邀请，但已过期；';
comment on column SGA_INVITEQUEUE.RegisterType is '注册类型。0：默认自主注册；1：项目报名；2：项目邀请；3：企业邀请';

alter table SGA_InviteQueue add primary key(ID);

--添加企业的统计信息
create table SGA_COM_Statistics
(
  ID   inteGER,
  CID  intEGER,
  ProjectCount INTEGER,
  RegisterCount INTEGER,
  JoininCount INTEGER
);

comment on table SGA_COM_STATISTICS
  is '企业统计信息';
  
alter table SGA_COM_STATISTICS add primary key(id);
alter table SGA_COM_STATISTICS
  add constraint FK_SGA_COM_STATISTICS_CID foreign key (CID)
  references SGA_COMINFO (ID) on delete cascade;

comment on column SGA_COM_STATISTICS.ID
  is '主键ID';
comment on column SGA_COM_STATISTICS.CID
  is '企业ID';
comment on column SGA_COM_STATISTICS.PROJECTCOUNT
  is '项目个数';
comment on column SGA_COM_STATISTICS.REGISTERCOUNT
  is '报名人数';
comment on column SGA_COM_STATISTICS.JOININCOUNT
  is '招募人数';

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
  is '主键id';
comment on column SGA_FileRecord.RESID
  is '资源id';
comment on column SGA_FileRecord.Creator
  is '业务上的创建者';
comment on column SGA_FileRecord.CREATETIME
  is '创建时间';
comment on column SGA_FileRecord.RESTYPECODE
  is '资源类型';
comment on column SGA_FileRecord.DOMAINCODE
  is '空间域编码';
comment on column SGA_FileRecord.Agent
  is '代理者';
comment on column SGA_FileRecord.opType is '操作类型，上传（u,upload）或者下载（d,download）';
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
  is '项目与微作关系';
-- Add comments to the columns 
comment on column SGA_PRJ_WZ.ID
  is '主键ID';
comment on column SGA_PRJ_WZ.PID
  is '项目序列ID';
comment on column SGA_PRJ_WZ.WZID
  is '微作ID';
comment on column SGA_PRJ_WZ.CREATOR
  is '创建者';
comment on column SGA_PRJ_WZ.CREATETIME
  is '创建时间';
  
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
  is '区域，省市县';

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

-- Add/modify columns（还有必要吗？）
alter table SGA_USER add lasttime date default sysdate;
comment on column SGA_USER.lasttime is '上一次登录时间'; 

------------------以上已更新到aws，2017/03/24

--2017/04/13
--重命名企业信息表名
rename sga_cominfo to sga_company;
--重命名企业审计信息表名
rename sga_com_statistics to sga_com_audit;
--用户序列id换成用户编码
update sga_prj_wz t set t.creator = (select u.syscode from sga_user u where u.id = t.creator);
commit;

------------------以上已更新到aws，2017/04/14
--添加属性
-- Add/modify columns 
alter table SGA_USER_ATTACHMENT add AttachType inteGER;
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.AttachType
  is '附件类型。0：简历';
  
-- Add/modify columns 
alter table SGA_USER_ATTACHMENT rename column ATTACHID to ATTACHVID;
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ATTACHVID
  is '附件版本系列ID';
 
insert into sga_user(id,loginname,username,syscode,sex,position,status,registertype,createtime,lasttime)
values(seq_sga_oid.nextval, 'admin','管理员','1DCC08EC-7BA3-4BA6-A4B2-42055F75712C','m','超管',1,0,sysdate,sysdate);
commit;
------------------以上已更新到aws，2017/04/18

-- Add/modify columns 
alter table SGA_USER add address varchar2(100);
-- Add comments to the columns 
comment on column SGA_USER.address
  is '联系地址';
 
------------------以上已更新到aws，2017/04/25
  
-- Add/modify columns 
alter table SGA_USER add openid varchar2(50);
-- Add comments to the columns 
comment on column SGA_USER.openid
  is '微信应用唯一id';

------------------以上已更新到aws，2017/04/25
--2017/04/26
-- Add comments to the columns 
comment on column SGA_PROJECT.REGISTERCOUNT
  is '报名人数（废弃，20170426）';
comment on column SGA_PROJECT.JOININCOUNT
  is '已招募人数（废弃，20170426）';

--2017/04/27
-- Add/modify columns 
alter table SGA_PRJ_USER add formid varchar2(32);
-- Add comments to the columns 
comment on column SGA_PRJ_USER.formid
  is '关联微信小程序的表单id（临时之用）';
 
------------------以上已更新到aws，2017/04/27
-- Add comments to the columns 
comment on column SGA_PRJ_USER.STATUS
  is '用户在项目中的状态。-1：拒绝；0：待审核；1：参与；2：待定';

------------------以上已更新到aws，2017/04/30
 -- Add/modify columns 
alter table SGA_PROJECT add posterNAME varchar2(25);
-- Add comments to the columns 
comment on column SGA_PROJECT.posterNAME
  is '海报名称';
  
------------------以上已更新到aws，2017/05/03

 -- Add/modify columns 
alter table sga_company add logoNAME varchar2(25);
-- Add comments to the columns 
comment on column sga_company.logoNAME
  is 'logo名称';
  
   -- Add/modify columns 
alter table sga_company add imgNAME varchar2(25);
-- Add comments to the columns 
comment on column sga_company.imgNAME
  is '背景图名称';
  
  ------------------以上已更新到aws，2017/05/17
  
  -- Add/modify columns 
alter table SGA_USER_ATTACHMENT add MIMETYPE VARCHAR2(50);
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.MIMETYPE
  is '文件类型';
  
   ------------------以上已更新到aws，2017/06/17
   
  -- Add/modify columns 
alter table SGA_USER_ATTACHMENT add ATTACHID varchar2(38);
-- Add comments to the columns 
comment on column SGA_USER_ATTACHMENT.ATTACHID
  is '附件id';

   ------------------以上已更新到aws，2017/07/03