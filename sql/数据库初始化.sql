drop table DCM_RESTYPE;
drop table DCM_PRIVILEGE;
drop table DCM_ROLE;
drop table DCM_PRIVTEMPLATE;
drop table DCM_USERDOMAINROLE;
drop table DCM_GROUP;
drop table DCM_ORGANIZATION;
drop table DCM_USER;
drop table DCM_ORG_USER;
drop sequence SEQ_DCM_OID;

-- Create sequence 
create sequence SEQ_DCM_OID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


create table DCM_RESTYPE(ID   INTEGER not null primary key,RESTYPECODE VARCHAR2(50),RESTYPENAME VARCHAR2(100));
--create sequence BDE_DCM_RESTYPE minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_RESTYPE  is '资源类型';
comment on column DCM_RESTYPE.RESTYPECODE  is '资源类型代码';
comment on column DCM_RESTYPE.RESTYPENAME  is '资源类型名称';

create table DCM_PRIVILEGE(ID   INTEGER not null primary key,PRIVCODE VARCHAR2(50),PRIVNAME VARCHAR2(50),PRIVVALUE NUMBER(19));
--create sequence BDE_DCM_PRIVILEGE minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_PRIVILEGE  is '权限表';
comment on column DCM_PRIVILEGE.PRIVCODE  is '权限编码，以Pri_前缀';
comment on column DCM_PRIVILEGE.PRIVNAME  is '权限名称';
comment on column DCM_PRIVILEGE.PRIVVALUE  is '权限值，用于做位运算';


create table DCM_ROLE(ID   INTEGER not null primary key,ROLECODE VARCHAR2(50),ROLENAME VARCHAR2(50));
--create sequence BDE_DCM_ROLE minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_ROLE  is '角色信息';
comment on column DCM_ROLE.ROLECODE  is '角色编码，以R_前缀';
comment on column DCM_ROLE.ROLENAME  is '角色名称';


create table DCM_PRIVTEMPLATE(ID   INTEGER not null primary key,RESTYPECODE VARCHAR2(50),RESTYPESTATUS INTEGER,ROLECODE VARCHAR2(50),PRIVCODE VARCHAR2(50));
--create sequence BDE_DCM_PRITEMPLATE minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_PRIVTEMPLATE  is '权限模板';
comment on column DCM_PRIVTEMPLATE.RESTYPECODE  is '资源类型编码';
comment on column DCM_PRIVTEMPLATE.RESTYPESTATUS  is '资源类型状态，在办项目和归档项目，值：1（在办）；0（归档）';
comment on column DCM_PRIVTEMPLATE.ROLECODE  is '角色编码';
comment on column DCM_PRIVTEMPLATE.PRIVCODE  is '权限编码';


create table DCM_USERDOMAINROLE(ID   INTEGER not null primary key,USERID INTEGER,DOMAINCODE VARCHAR2(50),DOMAINTYPE VARCHAR2(50),ROLECODE VARCHAR2(50));
--create sequence BDE_DCM_USERDOMAINROLE minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_USERDOMAINROLE  is '用户-空间域-角色';
comment on column DCM_USERDOMAINROLE.USERID  is '用户id';
comment on column DCM_USERDOMAINROLE.DOMAINCODE  is '空间域编码';
comment on column DCM_USERDOMAINROLE.DOMAINTYPE  is '空间域类型，存放DCM_Group、DCM_Organization和DCM_User的属性DomainType数据';
comment on column DCM_USERDOMAINROLE.ROLECODE  is '角色编码';


create table DCM_GROUP(ID   INTEGER not null primary key,DOMAINTYPE VARCHAR2(50),GROUPNAME VARCHAR2(100),GROUPCODE VARCHAR2(50),Creator VARCHAR2(20), CreateTime Date, ModifiedTime DATE);
--create sequence BDE_DCM_GROUP minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_GROUP  is '组表（项目组/讨论组）';
comment on column DCM_GROUP.DOMAINTYPE  is '对应着模型PM_UserDomainRole的属性DomainType，值有：Domain_Project（项目组）/Domain_Discussion（讨论组）';
comment on column DCM_GROUP.GROUPNAME  is '项目组或者讨论组的名称';
comment on column DCM_GROUP.GROUPCODE  is '项目组或者讨论组编码，以GC前缀';
comment on column DCM_GROUP.Creator is '创建者名称';
comment on column DCM_GROUP.CreateTime is '创建时间';
comment on column DCM_GROUP.ModifiedTime is '修改时间';


create table DCM_ORGANIZATION(ID   INTEGER not null primary key,PARENTID INTEGER,DOMAINTYPE VARCHAR2(50),ORGNAME VARCHAR2(100),ORDERID INTEGER,ORGCODE VARCHAR2(50));
--create sequence BDE_DCM_ORGANIZATION minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_ORGANIZATION  is '组织机构表';
comment on column DCM_ORGANIZATION.PARENTID  is '父节点ID';
comment on column DCM_ORGANIZATION.DOMAINTYPE  is '空间域类型，对应着模型DCM_UserDomainRole的属性DomainType，值有：Domain_Institute（院）/Domain_Department（所）';
comment on column DCM_ORGANIZATION.ORGNAME  is '机构名称';
comment on column DCM_ORGANIZATION.ORDERID  is '排序号';
comment on column DCM_ORGANIZATION.ORGCODE  is '机构编码，以OC前缀';


create table DCM_USER(ID   INTEGER not null primary key,USERNAME VARCHAR2(20),LOGINNAME VARCHAR2(20),USERPWD VARCHAR2(100), 
USERCODE VARCHAR(36), EMAIL VARCHAR2(50),CURRENTSTATUS INTEGER,DATECREATED date,DATELASTACTIVITY date,
TELEPHONE VARCHAR2(50),DOMAINTYPE VARCHAR2(50), SEX VARCHAR2(5),Isbuildin NUMBER(2), Avatar VARCHAR2(255));
--create sequence BDE_DCM_USER minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_USER  is '人员信息';
comment on column DCM_USER.USERNAME  is '用户名';
comment on column DCM_USER.LOGINNAME  is '登录名';
comment on column DCM_USER.USERPWD  is '用户密码';
comment on column DCM_USER.EMAIL  is '邮箱';
comment on column DCM_USER.CURRENTSTATUS  is '当前状态。-1：离职或者被删除；0：挂起；1：正常';
comment on column DCM_USER.DATECREATED  is '创建时间';
comment on column DCM_USER.DATELASTACTIVITY  is '最后活动时间';
comment on column DCM_USER.TELEPHONE  is '联系电话';
comment on column DCM_USER.DOMAINTYPE  is '对应着模型DCM_UserDomainRole的属性DomainType，值有：Domain_Person（用户）';
comment on column DCM_USER.USERCODE  is '用户编码，以UC前缀';
comment on column DCM_USER.SEX  is '性别，值：男/女';
comment on column DCM_USER.ISBUILDIN  is '是否内置用户，1：是；0：否';
comment on column DCM_USER.Avatar is '用户头像，存储相对路径';


create table DCM_ORG_USER(ID   INTEGER not null primary key,USERID INTEGER,ORGID INTEGER);
--create sequence BDE_DCM_ORG_USER minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20;
comment on table DCM_ORG_USER  is '机构与人员关联表';
comment on column DCM_ORG_USER.USERID  is '用户id';
comment on column DCM_ORG_USER.ORGID  is '机构id';

create table DCM_Share(
   ID INTEGER NOT NULL PRIMARY KEY, 
   ResId VARCHAR2(38), 
   ResTypeCode VARCHAR2(50),
   SourceDomainType VARCHAR2(50),
   SourceDomainCode VARCHAR2(50),
   TargetDomainType VARCHAR2(50),
   TargetDomainCode VARCHAR2(50),
   PrivCodes VARCHAR2(500), 
   ShareDateTime DATE, 
   ExpiryDateTime DATE,
   SHARER VARCHAR2(20));
   
comment on table DCM_Share IS '资源共享表';
comment on column DCM_Share.ID is '主键id';
comment on column DCM_Share.ResId is '资源id，分两类：1、包id，代指ce里Folder根目录，特指院包/所包/项目包，不包括组包和个人包；2、文件夹id，除了ce根目录外的子文件夹类型';
comment on column DCM_Share.ResTypeCode is '资源类型编码';
comment on column DCM_Share.SourceDomainType is '源头空间域类型';
comment on column DCM_Share.SourceDomainCode is '源头空间域编码，分享的源头';
comment on column DCM_Share.TargetDomainType is '目标空间域类型';
comment on column DCM_Share.TargetDomainCode is '目标空间域编码，分享的目标地方';
comment on column DCM_Share.PrivCodes is '权限编码集合，多个code之间用逗号分隔';
comment on column DCM_Share.ShareDateTime is '分享时间';
comment on column DCM_Share.ExpiryDateTime is '有效期';
comment on column DCM_Share.SHARER is '分享者';

-- Add/modify columns 
alter table DCM_SHARE add status integer;
-- Add comments to the columns 
comment on column DCM_SHARE.status
  is '状态。1：有效；0：无效';

--初始化数据
truncate table DCM_RESTYPE;
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_UI_HomePage','首页');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Pck_Institute','院包');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Pck_Department','所包');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Pck_Project','项目包');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Pck_Person','个人包');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Pck_Group','组包');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Space_Institute','院空间');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Space_Department','所空间');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Space_Project','项目');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Space_Discussion','讨论组');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Res_Space_Person','个人');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Document','文件');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Folder','文件夹');
insert into DCM_RESTYPE values(SEQ_DCM_OID.nextval,'Document_WZ','微作');
commit;


truncate table dcm_privilege;
--CE本身支持权限
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'READ','查看所有属性', 1 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'WRITE','修改所有属性',2);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'VIEW_CONTENT','查看内容', 128 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'LINK','归档到文件夹中',16);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'PUBLISH','发布',2048);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'CREATE_INSTANCE','新建', 256 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'CHANGE_STATE','更改状态', 1024 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'MINOR_VERSION','次要版本控制', 64 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'MAJOR_VERSION','主要版本控制', 4 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'DELETE','物理删除', 65536 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'READ_ACL','读许可权', 131072 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'WRITE_ACL','修改许可权', 262144 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'WRITE_OWNER','修改所有者', 524288 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'UNLINK','从文件夹取消归档', 32 );
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'CREATE_CHILD','创建子文件夹', 512 );
--扩展部分
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_UI_Enabled','可用',4294967296);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_UI_Disabled','不可用',34359738368);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_Download','下载',8589934592);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_Print','打印',17179869184);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_Share','共享',137438953472);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_Remove','逻辑删除',274877906944);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_MemberAssignment','人员管理',549755813888);
insert into dcm_privilege values(SEQ_DCM_OID.nextval,'Priv_Search','搜索',68719476736);

commit;
--系统角色
truncate table dcm_role;
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Institute_Manager','院管',0);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Institute_Member','院员',0);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Department_Manager','所管',1);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Department_Member','所员',1);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_Manager','项目负责人',2);
--insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CoreMember','项目组核心成员');
--项目组核心成员：把这一类角色拆分成多个子角色
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_DPProManager','规划设计专业负责人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_DPDesigner','规划设计人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_DPFirstExaminer','规划设计初审人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_DPSecondExaminer','规划设计审核人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_DPThirdExaminer','规划设计审定人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CPProManager','市政规划设计专业负责人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CPDesigner','市政规划设计人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CPFirstExaminer','市政规划设计初审人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CPSecondExaminer','市政规划设计审核人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_CPThirdExaminer','市政规划设计审定人',2);
commit;

insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_Support','项目组辅助成员',2);

insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_BidHead','项目投标负责人',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_BidMember','项目投标人员',2);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Group_Manager','组管',3);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Group_Member','组员',3);
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Owner','所有者',4);
commit;

	



truncate table dcm_user;
insert into dcm_user
values
  (SEQ_DCM_OID.nextval,
   '系统管理员',
   'admin',
   'F130E1769CFE9F5E9843133D0DD40C',
   'UC0001',
   '',
   1,
   sysdate,
   sysdate,
   '',
   'Domain_Person',
   '男',
   1);
   commit;
   
   --初始化权限模板数据
    truncate table dcm_privtemplate;
  --==================<院包=======================
  ------------------->院管
 --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','Priv_Print');
--共享
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','Priv_Share');
 ------------------->院员
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Member','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Member','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Member','Priv_Print');

 --==================院包>=======================

 --==================<所包=======================
 ------------------->所管
--新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','Priv_Print');
--共享
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','Priv_Share');
------------------->所员
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Member','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Member','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Member','Priv_Print');
 --==================所包>=======================
 
  --==================<项目包=====================
  
  ------>未归档项目==================>
  ------------------->项目负责人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Manager','Priv_Print');

------------------>项目核心成员：被拆分为：
/*                    规划设计专业负责人
                    规划设计人
                    规划设计初审人
                    规划设计审核人
                    规划设计审定人
                    市政规划设计专业负责人
                    市政规划设计人
                    市政规划设计初审人
                    市政规划设计审核人
                    市政规划设计审定人*/

/*  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CoreMember','Priv_Print');*/

------------------>规划设计专业负责人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPProManager','Priv_Print');

------------------>规划设计人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPDesigner','Priv_Print');

------------------>规划设计初审人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPFirstExaminer','Priv_Print');

------------------>规划设计审核人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPSecondExaminer','Priv_Print');

------------------>规划设计审定人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_DPThirdExaminer','Priv_Print');

------------------>市政规划设计专业负责人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPProManager','Priv_Print');

------------------>市政规划设计人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPDesigner','Priv_Print');

------------------>市政规划设计初审人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPFirstExaminer','Priv_Print');

------------------>市政规划设计审核人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPSecondExaminer','Priv_Print');

------------------>市政规划设计审定人
  --新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','WRITE_ACL');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_CPThirdExaminer','Priv_Print');


------------------>项目辅助成员
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Assistant','READ_ACL');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Assistant','Priv_Print');

------------------>所管
--共享
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Department_Manager','Priv_Share');

  ------<未归档项目==================<
  
  
  ------>归档项目==================>
  ------------------->项目负责人

--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Manager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Manager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Manager','Priv_Print');

------------------>项目核心成员：被拆分为：
/*                    规划设计专业负责人
                    规划设计人
                    规划设计初审人
                    规划设计审核人
                    规划设计审定人
                    市政规划设计专业负责人
                    市政规划设计人
                    市政规划设计初审人
                    市政规划设计审核人
                    市政规划设计审定人*/
 
--浏览
/*insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CoreMember','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CoreMember','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CoreMember','Priv_Print');*/

------------------>规划设计专业负责人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPProManager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPProManager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPProManager','Priv_Print');

------------------>规划设计人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPDesigner','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPDesigner','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPDesigner','Priv_Print');

------------------>规划设计初审人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPFirstExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPFirstExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPFirstExaminer','Priv_Print');

------------------>规划设计审核人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPSecondExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPSecondExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPSecondExaminer','Priv_Print');

------------------>规划设计审定人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPThirdExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPThirdExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_DPThirdExaminer','Priv_Print');

------------------>市政规划设计专业负责人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPProManager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPProManager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPProManager','Priv_Print');

------------------>市政规划设计人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPDesigner','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPDesigner','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPDesigner','Priv_Print');

------------------>市政规划设计初审人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPFirstExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPFirstExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPFirstExaminer','Priv_Print');

------------------>市政规划设计审核人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPSecondExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPSecondExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPSecondExaminer','Priv_Print');

------------------>市政规划设计审定人
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPThirdExaminer','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPThirdExaminer','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_CPThirdExaminer','Priv_Print');


------------------>项目辅助成员
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Assistant','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Assistant','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Project_Assistant','Priv_Print');


------------------>所管
--共享
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',0,'R_Department_Manager','Priv_Share');

 ------<归档项目==================<
  
  

--==================项目包>=======================

--==================<项目=======================
------------------->所管
--创建项目
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Department_Manager','CREATE_INSTANCE');
--删除项目
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Department_Manager','DELETE');

------------------->项目负责人
--人员管理
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_Manager','Priv_MemberAssignment');
--查看项目
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_Manager','VIEW_CONTENT');

------------------->项目组核心成员：被拆分为：
/*                    规划设计专业负责人
                    规划设计人
                    规划设计初审人
                    规划设计审核人
                    规划设计审定人
                    市政规划设计专业负责人
                    市政规划设计人
                    市政规划设计初审人
                    市政规划设计审核人
                    市政规划设计审定人*/
--查看项目
/*insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CoreMember','VIEW_CONTENT');*/

--查看项目
------------------->规划设计专业负责人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_DPProManager','VIEW_CONTENT');
------------------->规划设计人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_DPDesigner','VIEW_CONTENT');
------------------->规划设计初审人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_DPFirstExaminer','VIEW_CONTENT');
------------------->规划设计审核人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_DPSecondExaminer','VIEW_CONTENT');
------------------->规划设计审定人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_DPThirdExaminer','VIEW_CONTENT');
------------------->市政规划设计专业负责人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CPProManager','VIEW_CONTENT');
------------------->市政规划设计人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CPDesigner','VIEW_CONTENT');
------------------->市政规划设计初审人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CPFirstExaminer','VIEW_CONTENT');
------------------->市政规划设计审核人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CPSecondExaminer','VIEW_CONTENT');
------------------->市政规划设计审定人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_CPThirdExaminer','VIEW_CONTENT');

------------------->项目组辅助成员
--查看项目
/*insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_Member','VIEW_CONTENT');*/
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Project',1,'R_Project_Assistant','VIEW_CONTENT');


--==================项目>=======================


--==================<讨论组=======================
------------------->组管
--创建讨论组
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Discussion',1,'R_Group_Manager','CREATE_INSTANCE');
--删除讨论组
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Discussion',1,'R_Group_Manager','DELETE');
--成员管理
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Discussion',1,'R_Group_Manager','Priv_MemberAssignment');
--查看讨论组
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Discussion',1,'R_Group_Manager','VIEW_CONTENT');

------------------->组员
--查看讨论组
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Space_Discussion',1,'R_Group_Member','VIEW_CONTENT');

--==================讨论组>=======================

--==================<组包========================
------------------->组管
--移除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Manager','DELETE');
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Manager','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Manager','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Manager','Priv_Print');

------------------->组员
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Member','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Member','Priv_Print');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Group',1,'R_Group_Member','Priv_Print');

--==================组包>=======================

--==================<个人包=====================
------------------->个人
--共享
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','Priv_Share');
--增加
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','WRITE_ACL');--WRITE？
--浏览
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','READ_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','Priv_Download');
--打印
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Person',1,'R_Owner','Priv_Print');

 --==================个人包>=====================
 
 
 insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'王勇良','wangyl',1,sysdate,sysdate,'Domain_Person','女',0);

insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'张赛男','zhangsn',1,sysdate,sysdate,'Domain_Person','男',0);

insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'韦富杰','weifj',1,sysdate,sysdate,'Domain_Person','男',0);

insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'陈艳平','chenyp',1,sysdate,sysdate,'Domain_Person','女',0);

insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'靳志明','jinzhm',1,sysdate,sysdate,'Domain_Person','女',0);

 insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'程洋','chengyang',1,sysdate,sysdate,'Domain_Person','女',0);

 insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin) 
values(seq_dcm_oid.nextval,'ceadmin','ceadmin',1,sysdate,sysdate,'Domain_Person','男',1);

--初始化个人空间和对应的角色
--清除数据
delete from dcm_userdomainrole udr where udr.domaintype = 'Domain_Person';

insert into dcm_userdomainrole 
select seq_dcm_oid.nextval,u.id,u.loginname,'Domain_Person','R_Owner',sysdate from dcm_user u;

--<==================视图创建=====================>
--文档类
CREATE OR REPLACE VIEW V$DOCVERSION
AS
SELECT OBJECT_ID,
       OBJECT_CLASS_ID,
       CREATOR,
       CREATE_DATE,
       MODIFY_USER,
       MODIFY_DATE,
       CONTENT_SIZE,
       MIME_TYPE,
       MAJOR_VERSION_NUMBER,
       MINOR_VERSION_NUMBER,
       VERSION_STATUS,
       U1708_DOCUMENTTITLE,
       U74D8_GZPI_ASSOCIATETASK,
       U9868_GZPI_CONTENT,
       U0746_GZPI_UPVOTECOUNT,
       U6018_GZPI_ASSOCIATETACHE,
       U22E8_GZPI_ASSOCIATEPROJECT,
       U1F38_GZPI_SPATIALDOMAIN,
       UADD8_GZPI_ORGANIZATION,
       U5A58_GZPI_REGION,
       UB7B8_GZPI_FILETYPE,
       UB8F8_GZPI_RESOURCETYPE
  FROM ICMUSER.DOCVERSION T
 WHERE T.OBJECT_CLASS_ID NOT IN
       (select t.object_id
          from ICMUSER.classdefinition t
         where t.symbolic_name = 'GZPI_FILE_WZ');
         
         
    --升级脚本     
    --修改于：20160330

alter table DCM_GROUP add guid varchar2(38);
comment on column DCM_GROUP.guid is '唯一标识符';

drop table DCM_SocialResource;
--创建资源社交化数据表
create table DCM_SocialResource
(
  ID          number,
  resId       varchar2(38),
  parentResId varchar2(38),
  resTypeCode varchar2(50),
  isFavorite  integer,
  isLike      integer,
  Tag         varchar2(10),
  creator  varchar2(20)
)
;
-- Add comments to the table 
comment on table DCM_SocialResource
  is '资源社交化';
-- Add comments to the columns 
comment on column DCM_SocialResource.resId
  is '指的是资源guid';
 comment on column DCM_SocialResource.parentResId
  is '父资源id';
comment on column DCM_SocialResource.resTypeCode
  is '这里的资源类型可以是通用文档、微作等资源。跟dcm_restype的resTypeCode对应';
comment on column DCM_SocialResource.isFavorite
  is '值：0和1，0：未收藏
1：收藏';
comment on column DCM_SocialResource.isLike
  is '值：0和1，0：未点赞
1：点赞';
comment on column DCM_SocialResource.Tag
  is '是指标记的颜色，直接存储颜色值，如：#FFFFFF';
comment on column DCM_SocialResource.creator
  is '创建者';
 
  --升级脚本     
  --修改于：20160411
  
 create table DCM_Region
(
  ID       number,
  province varchar2(10),
  city     varchar2(10),
  town     varchar2(10)
)
;
-- Add comments to the table 
comment on table DCM_Region
  is '行政区划字典表';
-- Add comments to the columns 
comment on column DCM_Region.province
  is '省份';
comment on column DCM_Region.city
  is '市';
comment on column DCM_Region.county
  is '县';

--初始化数据
truncate table DCM_Region;
insert into dcm_region values(seq_dcm_oid.nextval,'广东省','广州市','白云区');
insert into dcm_region values(seq_dcm_oid.nextval,'广东省','广州市','越秀区');
insert into dcm_region values(seq_dcm_oid.nextval,'广东省','茂名市','信宜县');
 
 commit;
 
 --升级脚本
 --20160414
 -- --加入项目组或者讨论组的时间
alter table DCM_USERDOMAINROLE add CREATETIME DATE default SYSDATE;
-- Add comments to the columns 
comment on column DCM_USERDOMAINROLE.CREATETIME
  is '时间';
  
  --20160416
  -- Add/modify columns 
alter table DCM_SOCIALRESOURCE add TIMELIKE date default sysdate;
-- Add comments to the columns 
comment on column DCM_SOCIALRESOURCE.TIMELIKE
  is '点赞时间';

-- Add/modify columns 
alter table DCM_SOCIALRESOURCE add TIMEFAVORITE date default sysdate;
alter table DCM_SOCIALRESOURCE add TIMETAG date default sysdate;
-- Add comments to the columns 
comment on column DCM_SOCIALRESOURCE.TIMEFAVORITE
  is '收藏时间';
comment on column DCM_SOCIALRESOURCE.TIMETAG
  is '标签时间';
  
  --20160419
  -- Create table
create table DCM_MaterialDimension
(
  ID        number,
  ParentID  number,
  Name      varchar2(50),
  Depth     integer,
  IsBuildIn integer,
  OrderID integer
)
;
-- Add comments to the table 
comment on table DCM_MaterialDimension
  is '资料维度模型';
-- Add comments to the columns 
comment on column DCM_MaterialDimension.ID
  is '主键ID，从integer类型改为number类型';
comment on column DCM_MaterialDimension.ParentID
  is '父节点ID';
comment on column DCM_MaterialDimension.Name
  is '名字';
comment on column DCM_MaterialDimension.Depth
  is '深度，如一级维度、二级维度等等';
comment on column DCM_MaterialDimension.IsBuildIn
  is '是否内置。1：是；0：否';
comment on column DCM_MATERIALDIMENSION.ORDERID
  is '排序号';
  
  --初始化数据
  declare
  begin
  truncate table dcm_materialdimension;
  commit;
  
  insert into  dcm_materialdimension 
values(seq_dcm_oid.nextval,-1,'文种',0,1,0);
insert into  dcm_materialdimension 
values(seq_dcm_oid.nextval,-1,'区域',0,1,1);

insert into  dcm_materialdimension 
values(seq_dcm_oid.nextval,-1,'组织',0,1,2);

insert into  dcm_materialdimension 
values(seq_dcm_oid.nextval,-1,'业务',0,1,3);

commit;
end;
/
declare
  v_id number default - 1;
begin
  --【文种】的一级维度
  select id into v_id from dcm_materialdimension m where m.name = '文种';

  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '招投标资料', 1, 0, 0);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '合同资料', 1, 0, 1);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '调研资料', 1, 0, 2);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '策划资料', 1, 0, 3);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '阶段成果资料', 1, 0, 4);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '审查资料', 1, 0, 5);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '成果资料', 1, 0, 6);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '档案资料', 1, 0, 7);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '评奖资料', 1, 0, 8);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '图片素材', 1, 0, 9);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '学习资料', 1, 0, 10);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '标准规范', 1, 0, 11);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '基础地理资料', 1, 0, 12);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '所务资料', 1, 0, 13);

commit;
 --【所务资料】的二级维度
  select id
    into v_id
    from dcm_materialdimension m
   where m.name = '所务资料';
   
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '人事管理', 2, 0, 0);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '财务管理', 2, 0, 1);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '物资管理', 2, 0, 2);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '制度管理', 2, 0, 3);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '部门备忘', 2, 0, 4);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, 'ISO管理', 2, 0, 5);
  insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '常用软件', 2, 0, 6);
  commit;
  
  --【区域】的一级
    select id
    into v_id
    from dcm_materialdimension m
   where m.name = '区域';
   
     insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '中国', 1, 0, 0);
    
        insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '国外', 1, 0, 1);
    
    commit;
    
    --【中国】的二级
     select id
    into v_id
    from dcm_materialdimension m
   where m.name = '中国';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '广东省', 2, 0, 0);
    
    commit;
    
    --【广东省】的三级
      select id
    into v_id
    from dcm_materialdimension m
   where m.name = '广东省';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '广州市', 3, 0, 0);
    
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '深圳市', 3, 0, 1);
    
    commit;
    
    --【广州市】的四级
       select id
    into v_id
    from dcm_materialdimension m
   where m.name = '广州市';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '花都区', 4, 0, 0);
    
     insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '越秀区', 4, 0, 1);
    commit;
    
    --【深圳市】的四级
    select id
    into v_id
    from dcm_materialdimension m
   where m.name = '深圳市';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '龙岗区', 4, 0, 0);
    
     insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '福田区', 4, 0, 1);
    commit;
    
    --【组织】的一级
      select id
    into v_id
    from dcm_materialdimension m
   where m.name = '组织';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '政府规划编制部', 1, 0, 0);
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划研究中心/低碳规划部', 1, 0, 1);
    
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划设计一所', 1, 0, 2);
    
        insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划设计二所', 1, 0, 3);
    
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划设计三所', 1, 0, 4);
    
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划设计四所/开发区分院（规划）', 1, 0, 5);
    
     insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '规划设计五所/南沙分院（规划）', 1, 0, 6);
    
     insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '城市设计策划所/海南分院', 1, 0, 7);
    
         insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '区域规划设计所/上海分院', 1, 0, 8);
    
         insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '交通规划设计所', 1, 0, 9);
    
        insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '市政规划设计一所', 1, 0, 10);
    
           insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '市政规划设计二所', 1, 0, 11);
    
           insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '景观与旅游规划设计所', 1, 0, 12);
    
    commit;
    
    --【业务】的一级
      select id
    into v_id
    from dcm_materialdimension m
   where m.name = '业务';
   
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '概念规划/研究', 1, 0, 0);
      insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '总体规划', 1, 0, 1);
    
          insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '控制性详细规划', 1, 0, 2);
    
        
          insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '城市设计', 1, 0, 3);
    
         insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '修建性详细规划', 1, 0, 4);
    
            insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '市政/交通专项规划', 1, 0, 5);
  
   insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '景观风貌规划', 1, 0, 6);
    
       insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '其他专项规划/研究', 1, 0, 7);
    
           insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '村庄规划', 1, 0, 8);
    
               insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '工程设计', 1, 0, 9);
    
                   insert into dcm_materialdimension
  values
    (seq_dcm_oid.nextval, v_id, '其他', 1, 0, 10);
    
    commit;
    
end;


--20160427
--创建我的资料模型表
-- Create table
create table DCM_PersonalMaterial
(
  ID             inteGER,
  ResId           varchar2(38),
  ParentResId       varchar2(38),
  RESTYPECODE varchar2(50),
  DateCreated    date,
  Creator        varchar2(20),
  IsFolder       integer
)
;
-- Add comments to the table 
comment on table DCM_PersonalMaterial
  is '个人资料';
-- Add comments to the columns 
comment on column DCM_PersonalMaterial.ID
  is '主键id';
comment on column DCM_PersonalMaterial.ResId
  is 'CE资源id';
comment on column DCM_PersonalMaterial.ParentResId
  is 'CE父资源id';
comment on column DCM_PersonalMaterial.RESTYPECODE
  is '对应dcm_restype的resTypeCode其中几个：Res_Pck_Person（个人包）、Res_Pck_Project（项目包）、Res_Pck_Institute（院包）、Res_Pck_Department（所包）';
comment on column DCM_PersonalMaterial.DateCreated
  is '创建时间';
comment on column DCM_PersonalMaterial.Creator
  is '创建者';
comment on column DCM_PersonalMaterial.IsFolder
  is '是否文件夹。1：文件夹；0：文件';
  
  alter table DCM_PersonalMaterial add primary key(id);
  
  --20160428
  --环节和任务
  
  
  --20160503
  -- Add/modify columns 
alter table DCM_SOCIALRESOURCE modify TIMELIKE default null;
alter table DCM_SOCIALRESOURCE modify TIMEFAVORITE default null;
alter table DCM_SOCIALRESOURCE modify TIMETAG default null;

--记录下载数据
create table DCM_Download
(
  ID         inteGER,
  ResId      varchar2(38),
  Downloader varchar2(20),
  createTime date
)
;
-- Add comments to the table 
comment on table DCM_Download
  is '下载记录表';
-- Add comments to the columns 
comment on column DCM_Download.ID
  is 'ID';
comment on column DCM_Download.ResId
  is '资源id';
comment on column DCM_Download.Downloader
  is '下载者';
comment on column DCM_Download.createTime
  is '下载时间';
  
  alter table dcm_download add primary key(id);
  -- Add/modify columns 
alter table DCM_DOWNLOAD add ResTypeCode varchar2(50);
-- Add comments to the columns 
comment on column DCM_DOWNLOAD.ResTypeCode
  is '资源类型编码';

--20160504，添加共享信息-分享者
-- Add/modify columns 
alter table DCM_SHARE add sharer varchar2(20);
-- Add comments to the columns 
comment on column DCM_SHARE.sharer
  is '分享者';
  
  --测试数据
  --机构下人的角色
  insert into dcm_userdomainrole 
values(seq_dcm_oid.nextval, 780, 'OC1456225385167','Domain_Department','R_Department_Manager',sysdate);


--添加某个用户
/*insert into dcm_user(id,username,loginname,userpwd,datecreated,datelastactivity,currentstatus,domaintype,sex,isbuildin)
values(seq_dcm_oid.nextval,'张晨旻','zhangchm','F130E1769CFE9F5E9843133D0DD40C',sysdate,sysdate,1,'Domain_Person','男',0);

insert into dcm_org_user 
values(seq_dcm_oid.nextval, 2111,742);

insert into dcm_userdomainrole 
values(seq_dcm_oid.nextval, 2111,'zhangchm','Domain_Person','R_Owner',sysdate);*/

--添加机构
--insert into dcm_organization values(seq_dcm_oid.nextval, 6056, 'Domain_Department','企划部',5,'378117207505420AE05319C8A8C0EA94','企划部');


--添加机构别名
-- Add/modify columns 
alter table DCM_ORGANIZATION add alias varchar2(50);
-- Add comments to the columns 
comment on column DCM_ORGANIZATION.alias
  is '机构别名';
  
  --添加下载记录模型属性，2016/05/19
  -- Add/modify columns 
alter table DCM_DOWNLOAD add DomainCode varchar2(50);
-- Add comments to the columns 
comment on column DCM_DOWNLOAD.DomainCode
  is '空间域编码';
  alter table DCM_USER add QQ varchar2(20);
-- Add comments to the columns 
comment on column DCM_USER.QQ
  is 'qq号码';
  
  --2016/06/23
  -- Add/modify columns 
alter table DCM_USER add phone VARCHAR2(50);
alter table DCM_USER add major VARCHAR2(50);
-- Add comments to the columns 
comment on column DCM_USER.phone
  is '座机电话';
comment on column DCM_USER.major
  is '专业';
  
  /*create table DCM_User_IDInfo
(
  ID              integer,
  name            varchar2(50),
  ValidTime       date,
  attachmentId    varchar2(36),
  attachmentName  varchar2(50),
  USERID integer
)
;*/
-- Add comments to the table 
/*comment on table DCM_User_IDInfo
  is '证件信息';
-- Add comments to the columns 
comment on column DCM_User_IDInfo.ID
  is 'ID';
comment on column DCM_User_IDInfo.name
  is '证书名称';
comment on column DCM_User_IDInfo.ValidTime
  is '有效时间';
comment on column DCM_User_IDInfo.attachmentId
  is '附件id';
comment on column DCM_User_IDInfo.attachmentName
  is '附件名称';
comment on column DCM_User_IDInfo.USERID
  is '关联表dcm_user的主键id';*/
  
  create table DCM_User_TitleInfo
(
  ID              integer,
  name            varchar2(50),
  GetTime       date,
  attachmentId    varchar2(36),
  attachmentName  varchar2(50),
  USERID integer
)
;
-- Add comments to the table 
comment on table DCM_User_TitleInfo
  is '职称信息';
-- Add comments to the columns 
comment on column DCM_User_TitleInfo.ID
  is 'ID';
comment on column DCM_User_TitleInfo.name
  is '证件名称';
comment on column DCM_User_TitleInfo.GetTime
  is '有效时间';
comment on column DCM_User_TitleInfo.attachmentId
  is '附件id';
comment on column DCM_User_TitleInfo.attachmentName
  is '附件名称';
comment on column DCM_User_TitleInfo.USERID
  is '关联表dcm_user的主键id';
  
  create table DCM_User_CertificateInfo
(
  ID              integer,
  name            varchar2(50),
  GetTime       date,
  attachmentId    varchar2(36),
  attachmentName  varchar2(50),
  USERID integer
)
;
-- Add comments to the table 
comment on table DCM_User_CertificateInfo
  is '执业资格';
-- Add comments to the columns 
comment on column DCM_User_CertificateInfo.ID
  is 'ID';
comment on column DCM_User_CertificateInfo.name
  is '证书名称';
comment on column DCM_User_CertificateInfo.GetTime
  is '有效时间';
comment on column DCM_User_CertificateInfo.attachmentId
  is '附件id';
comment on column DCM_User_CertificateInfo.attachmentName
  is '附件名称';

comment on column DCM_User_CertificateInfo.USERID
  is '关联表dcm_user的主键id';
  
  
  create table DCM_User_ArticleInfo
(
  ID              integer,
  name            varchar2(50),
  PublishTime       date,
  summary varchar2(1024),
  USERID integer
)
;
-- Add comments to the table 
comment on table DCM_User_ArticleInfo
  is '学术成果';
-- Add comments to the columns 
comment on column DCM_User_ArticleInfo.ID
  is 'ID';
comment on column DCM_User_ArticleInfo.name
  is '证书名称';
comment on column DCM_User_ArticleInfo.PublishTime
  is '有效时间';
  comment on column DCM_User_ArticleInfo.summary
  is '发表说明';

comment on column DCM_User_ArticleInfo.USERID
  is '关联表dcm_user的主键id';
  
  
  --2016/07/12
  create table DCM_Task
(
  ID           inteGER,
  TaskId       varchar2(38),
  ParentTaskId varchar2(38),
  DateCreated  date
)
;
-- Add comments to the table 
comment on table DCM_Task
  is '任务';
-- Add comments to the columns 
comment on column DCM_Task.ID
  is '主键id';
comment on column DCM_Task.TaskId
  is '任务id';
comment on column DCM_Task.ParentTaskId
  is '父任务id';
comment on column DCM_Task.DateCreated
  is '创建时间';

alter table DCM_Task add primary key(id);

-- Create table
/*create table DCM_Task_Material
(
  ID         integer,
  TaskId     varchar2(36),
  MaterialId varchar2(36)
)
;*/
-- Add comments to the table 
/*comment on table DCM_Task_Material
  is '任务关联附件';
-- Add comments to the columns 
comment on column DCM_Task_Material.ID
  is '主键id';
comment on column DCM_Task_Material.TaskId
  is '任务id';
comment on column DCM_Task_Material.MaterialId
  is '材料id';

alter table DCM_Task_Material add primary key(id);*/


-- Add/modify columns 
alter table DCM_REGION add TYPE inteGER;
-- Add comments to the columns 
comment on column DCM_REGION.TYPE
  is '1：国内；0：国外';
  
  -- Add/modify columns 
  --默认是国内
alter table DCM_REGION modify TYPE default 1;

-- Add/modify columns 
alter table DCM_TASK add caseIdentifier varchar2(50);
-- Add comments to the columns 
comment on column DCM_TASK.caseIdentifier
  is '案例标识';

--2016/07/15
-- Add/modify columns 
alter table DCM_USER add nativeplace varchar2(20);
alter table DCM_USER add department varchar2(50);
alter table DCM_USER add position varchar2(50);
-- Add comments to the columns 
comment on column DCM_USER.nativeplace
  is '籍贯';
comment on column DCM_USER.department
  is '部门';
comment on column DCM_USER.position
  is '职位';


--2016/07/18
-- Create table
create table DCM_User_Education
(
  ID             inteGER,
  Academy        varchar2(50),
  College        varchar2(50),
  Major          varchar2(50),
  Degree         varchar2(20),
   TimeStart      date,
  TimeEnd        date,
  UserId         inteGER
)
;
-- Add comments to the table 
comment on table DCM_User_Education
  is '教育经历';
-- Add comments to the columns 
comment on column DCM_User_Education.ID
  is '主键ID';
comment on column DCM_User_Education.TimeStart
  is '开始时间';
comment on column DCM_User_Education.TimeEnd
  is '结束时间';
comment on column DCM_User_Education.Academy
  is '院校';
comment on column DCM_User_Education.College
  is '学院';
comment on column DCM_User_Education.Major
  is '专业';
comment on column DCM_User_Education.Degree
  is '学位';
comment on column DCM_User_Education.UserId
  is '关联表dcm_user的主键id';

alter table DCM_User_Education add primary key(id);

create table DCM_User_Experience
(
  ID             inteGER,
  Unit           varchar2(20),
  Department     varchar2(20),
  Position       varchar2(15),
  Detail         VARCHAR2(1024),
  TimeStart      date,
  TimeEnd        date,
  UserId         integer
)
;
-- Add comments to the table 
comment on table DCM_User_Experience
  is '工作经历';
-- Add comments to the columns 
comment on column DCM_User_Experience.ID
  is '主键ID';
comment on column DCM_User_Experience.Unit
  is '单位';
comment on column DCM_User_Experience.Department
  is '部门';
comment on column DCM_User_Experience.Position
  is '职位';
comment on column DCM_User_Experience.Detail
  is '工作详情';
comment on column DCM_User_Experience.TimeStart
  is '开始时间';
comment on column DCM_User_Experience.TimeEnd
  is '结束时间';
comment on column DCM_User_Experience.UserId
  is '关联dcm_user的id';

alter table DCM_User_Experience add primary key(id);


create table DCM_User_Language
(
  ID             inteGER,
  Type           varchar2(15),
  Levels         varchar2(10),
  UserId         inteGER
)
;
-- Add comments to the table 
comment on table DCM_User_Language
  is '语言水平';
-- Add comments to the columns 
comment on column DCM_User_Language.ID
  is '主键ID';
comment on column DCM_User_Language.Type
  is '语种';
comment on column DCM_User_Language.Levels
  is '等级';
comment on column DCM_User_Language.UserId
  is '关联表dcm_user的id';
  
  alter table DCM_User_Language add primary key(id);
  
  
  create table DCM_User_Training
(
  ID             inteGER,
  TrainTime      date,
  Name           varchar2(50),
  Address        varchar2(255),
  Organizer      varchar2(100),
  UserId         inteGER
)
;
-- Add comments to the table 
comment on table DCM_User_Training
  is '培训经历';
-- Add comments to the columns 
comment on column DCM_User_Training.ID
  is '主键ID';
comment on column DCM_User_Training.TrainTime
  is '培训时间';
comment on column DCM_User_Training.Name
  is '培训名称';
comment on column DCM_User_Training.Address
  is '培训地点';
comment on column DCM_User_Training.Organizer
  is '举办方';
comment on column DCM_User_Training.UserId
  is '关联表dcm_user的主键id';
  
  alter table DCM_User_Training add primary key(id);
  
-- Add/modify columns 
alter table DCM_USER add Birthday date;
-- Add comments to the columns 
comment on column DCM_USER.Birthday
  is '出生时间';
  
  create table DCM_User_PrjExperience
(
  ID             integer,
  ProjectName    varchar2(50),
  ProjectCode    varchar2(50),
  position       varchar2(15),
  TimeStart      date,
  TimeEnd        date,
  REFWORKEXPERIENCEID         inteGER
)
;
-- Add comments to the table 
comment on table DCM_User_PrjExperience
  is '项目经历';
-- Add comments to the columns 
comment on column DCM_User_PrjExperience.ID
  is '主键id';
comment on column DCM_User_PrjExperience.ProjectName
  is '项目名称';
comment on column DCM_User_PrjExperience.ProjectCode
  is '项目编号';
comment on column DCM_User_PrjExperience.position
  is '职位';
comment on column DCM_User_PrjExperience.TimeStart
  is '开始时间';
comment on column DCM_User_PrjExperience.TimeEnd
  is '结束时间';
comment on column DCM_User_PrjExperience.REFWORKEXPERIENCEID
  is '关联dcm_user_workexperience的主键id';

alter table DCM_User_PrjExperience add primary key(id);


create table DCM_Attachment
(
  ID             inteGER,
  AttachmentId   varchar2(38),
  AttachmentName varchar2(50),
  AttachmentDesc varchar2(255),
  ReferenceId    inteGER
)
;
-- Add comments to the table 
comment on table DCM_Attachment
  is '附件信息表';
-- Add comments to the columns 
comment on column DCM_Attachment.ID
  is '主键id';
comment on column DCM_Attachment.AttachmentId
  is '附件id';
comment on column DCM_Attachment.AttachmentName
  is '附件名称';
comment on column DCM_Attachment.AttachmentDesc
  is '附件描述';
comment on column DCM_Attachment.ReferenceId
  is '关联外键id，可以是证件信息，可以是项目经历信息表id，可以是工作经历信息表id等等';

alter table DCM_Attachment add primary key(id);
  
  
  create table DCM_USER_OTHERINFO
(
  ID             INTEGER,
  ATTACHMENTNAME VARCHAR2(50),
  ATTACHMENTDESC VARCHAR2(255),
  USERID         INTEGER
);

comment on column DCM_USER_OTHERINFO.ATTACHMENTNAME
  is '附件名称';
comment on column DCM_USER_OTHERINFO.ATTACHMENTDESC
  is '附件说明';
comment on column DCM_USER_OTHERINFO.USERID
  is '关联dcm_user的主键id';
  
  alter table DCM_USER_OTHERINFO add primary key(id);

  -- Add/modify columns 
alter table DCM_USER add speciality varchar2(100);
-- Add comments to the columns 
comment on column DCM_USER.speciality
  is '特长';


--2016/07/20
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Project_Assistant','项目助理');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Institute_Leader','院领导');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Department_Leader','所领导');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Senior_Commissioner','高级专员');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Institue_ProjectCommissioner','项管专员');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Senior_Engineer','高级总工');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Specialized_Engineer','专业总工');
insert into dcm_role values(SEQ_DCM_OID.nextval,'R_Comman_Engineer','总工');
	
commit;

--2016/07/25
-- Add/modify columns 
alter table DCM_ROLE add RoleType inteGER;
-- Add comments to the columns 
comment on column DCM_ROLE.RoleType
  is '角色类型。0：院级；1：所级；2：项目级；3：组（讨论组，项目组）；4：所有者';
  
update dcm_role t set t.roletype = 0 where t.rolecode like 'R_Institute_%';
update dcm_role t set t.roletype = 0 where t.rolecode = 'R_Senior_Commissioner';
update dcm_role t set t.roletype = 0 where t.rolecode = 'R_Senior_Engineer';
update dcm_role t set t.roletype = 0 where t.rolecode = 'R_Specialized_Engineer';
update dcm_role t set t.roletype = 0 where t.rolecode = 'R_Comman_Engineer';

update dcm_role t set t.roletype = 1 where t.rolecode like 'R_Department%';

update dcm_role t set t.roletype = 2 where t.rolecode like 'R_Project_%';

update dcm_role t set t.roletype = 3 where t.rolecode like 'R_Group_%';

update dcm_role t set t.roletype = 4 where t.rolecode = 'R_Owner';

commit;


--2016/07/27 初始化权限模板数据
/*insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode)
values(seq_dcm_oid.nextval, 1, 'R_Institute_Leader','Priv_ProjectManagement');
*/

-- Add/modify columns 
alter table DCM_ROLE add comments varchar2(255);
-- Add comments to the columns 
comment on column DCM_ROLE.comments
  is '备注';

--添加院长、所长和总工的项目管理权限
insert into  dcm_privilege values(seq_dcm_oid.nextval, 'Priv_ProjectManagement','项目管理', 549755813888*2);
--添加院长、所长和总工的项目管理权限
insert into  dcm_privilege values(seq_dcm_oid.nextval, 'Priv_ProjectWork','项目工作', 1099511627776*2);
commit;



create table DCM_DIC_DOMAINTYPE
(
  ID   INTEGER not null,
  NAME VARCHAR2(20),
  CODE VARCHAR2(20)
)

-- Add comments to the table 
comment on table DCM_DIC_DomainType
  is '字典-空间域类型';
-- Add comments to the columns 
comment on column DCM_DIC_DomainType.ID
  is '主键ID';
comment on column DCM_DIC_DomainType.name
  is '空间域类型名称';
comment on column DCM_DIC_DomainType.code
  is '空间域类型编码';
  
  alter table DCM_DIC_DomainType add primary key(id);
  
  insert into DCM_DIC_DomainType(id,Name,Code) values(seq_dcm_oid.nextval, '院','Domain_Institute');
  insert into DCM_DIC_DomainType(id,Name,Code) values(seq_dcm_oid.nextval, '所','Domain_Department');
  insert into DCM_DIC_DomainType(id,Name,Code) values(seq_dcm_oid.nextval, '项目组','Domain_Project');
  insert into DCM_DIC_DomainType(id,Name,Code) values(seq_dcm_oid.nextval, '讨论组','Domain_Discussion');
   insert into DCM_DIC_DomainType(id,Name,Code) values(seq_dcm_oid.nextval, '用户','Domain_Person');
   
   commit;

--2016/08/01
-- Add/modify columns 
alter table DCM_GROUP add OrgCode varchar2(50);
alter table DCM_GROUP add OrgType varchar2(50);
-- Add comments to the columns 
comment on column DCM_GROUP.OrgCode
  is '部门编码，记录牵头部门的编码';
comment on column DCM_GROUP.OrgType
  is '部门类型，记录牵头部门的类型，Domain_Department（所）和Domain_Institute（院）';
  
  

--2016/08/02
--针对项目域，所领导拥有的权限
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Department_Leader','Priv_ProjectManagement');

commit;
--针对项目域，项目成员新增“项目工作”和“项目管理”权限，
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_Manager','Priv_ProjectWork');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_Manager','Priv_ProjectManagement');
commit;

--专业总工的项目管理权限
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Specialized_Engineer','Priv_ProjectManagement');
commit;

--高级总工的项目管理权限
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Senior_Engineer','Priv_ProjectManagement');
commit;


--假设肖静（17060）是数据中心的所领导
insert into dcm_userdomainrole(id,userid,domaincode,domaintype,rolecode,createtime)
values(seq_dcm_oid.nextval,17060,'{C1FC41BA-9F8F-4C08-B460-79F575249CA1}','Domain_Department','R_Department_Leader',sysdate);

--规划设计专业负责人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPProManager','Priv_ProjectManagement');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPProManager','Priv_ProjectWork');
commit;

--规划设计人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPDesigner','Priv_ProjectManagement');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPDesigner','Priv_ProjectWork');
commit;

--规划设计初审人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPFirstExaminer','Priv_ProjectManagement');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPFirstExaminer','Priv_ProjectWork');
commit;

--规划设计审核人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPSecondExaminer','Priv_ProjectManagement');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPSecondExaminer','Priv_ProjectWork');
commit;

--规划设计审定人
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPThirdExaminer','Priv_ProjectManagement');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Space_Project',1,'R_Project_DPThirdExaminer','Priv_ProjectWork');
commit;

--为院员添加查看文档内容的权限
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Member','VIEW_CONTENT');
commit;


--为所员添加查看文档内容的权限
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Department_Member','VIEW_CONTENT');
commit;

--初始化用户[韦富杰]为所[数据中心]的所员
insert into dcm_userdomainrole(id,userid,domaincode,domaintype,rolecode,createtime)
values(seq_dcm_oid.nextval,287,'{C1FC41BA-9F8F-4C08-B460-79F575249CA1}','Domain_Department','R_Department_Member',sysdate);
commit;

--2016/08/03
-- Add/modify columns 
alter table DCM_SHARE add ShareType inteGER default 0;
-- Add comments to the columns 
comment on column DCM_SHARE.ShareType
  is '共享类型。0：一般共享；1：事项共享';
  
  --2016/08/04
  -- Add/modify columns 
alter table DCM_TASK_MATERIAL add datecreated date default sysdate;


  --新增流程日志记录（事项流转）
create table DCM_ProcessLog
(
  ID          inteGER,
  TaskId      varchar2(38),
  actiontype  inteGER,
  FromUser    varchar2(20),
  ToUser      varchar2(20),
  datecreated date default sysdate,
  IP varchar2(20),
  Operation varchar2(500),
  COMMENTS    varchar2(255)
)
;
-- Add comments to the table 
comment on table DCM_ProcessLog
  is '流程日志记录';
-- Add comments to the columns 
comment on column DCM_ProcessLog.ID
  is '主键';
comment on column DCM_ProcessLog.TaskId
  is '任务id';
comment on column DCM_ProcessLog.actiontype
  is '操作类型。1：新增；-1：删除：2：发送；3：修改';
comment on column DCM_ProcessLog.FromUser
  is '发送者';
comment on column DCM_ProcessLog.ToUser
  is '接收者';
comment on column DCM_ProcessLog.datecreated
  is '创建时间';
comment on column DCM_ProcessLog.COMMENTS
  is '备注';
  comment on column DCM_PROCESSLOG.IP
  is '操作ip';
 comment on column DCM_PROCESSLOG.Operation
  is '操作内容';
  
  alter table DCM_ProcessLog add primary key(id);
  
  --2016/08/08
  --初始化权限模板数据
  --版本控制
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Manager', 'MAJOR_VERSION');

insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_DPProManager', 'MAJOR_VERSION');

insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_DPDesigner', 'MAJOR_VERSION');

commit;

---------------------以上已经同步到云服务器-----------------


--2016/08/16
-- Add/modify columns 
alter table DCM_USER_PRJEXPERIENCE add USERID inteGER;
-- Add comments to the columns 
comment on column DCM_USER_PRJEXPERIENCE.USERID
  is '关联dcm_user的id';
  
 --2016/08/08 修改长度
-- Add/modify columns 
alter table DCM_ATTACHMENT modify ATTACHMENTNAME VARCHAR2(100);

  -- Add/modify columns 
alter table DCM_USER_PRJEXPERIENCE add ProjectDesc clob;
-- Add comments to the columns 
comment on column DCM_USER_PRJEXPERIENCE.ProjectDesc
  is '项目描述';
  
  
  -- Add/modify columns 
alter table DCM_USER_ARTICLEINFO add journal varchar2(100);
alter table DCM_USER_ARTICLEINFO add author varchar2(50);
-- Add comments to the columns 
comment on column DCM_USER_ARTICLEINFO.PUBLISHTIME
  is '发表时间';
comment on column DCM_USER_ARTICLEINFO.journal
  is '发表期刊';
comment on column DCM_USER_ARTICLEINFO.author
  is '作者';
  
  --加大属性长度
  -- Add/modify columns 
alter table DCM_USER_WORKEXPERIENCE modify UNIT VARCHAR2(100);
alter table DCM_USER_WORKEXPERIENCE modify DEPARTMENT VARCHAR2(100);

--20160824 
-- Add/modify columns 
alter table DCM_TASK add TASKNAME varchar2(50);
-- Add comments to the columns 
comment on column DCM_TASK.TASKNAME
  is '任务名称';


--创建索引
create  index index_dcm_user_loginname on dcm_user(loginname);


---------------------以上已经同步到云服务器-----------------

--20160828
-- Add/modify columns 
alter table DCM_USER add DN varchar2(100);
-- Add comments to the columns 
comment on column DCM_USER.DN
  is 'ldap中的dn';

--20160906
-- Add/modify columns 
alter table DCM_ORGANIZATION add dn varchar2(100);
-- Add comments to the columns 
comment on column DCM_ORGANIZATION.dn
  is 'ldap中机构的dn';

---------------------以上已经同步到云服务器  2016/09/08-----------------

--修改用户编码长度：36->38
alter table DCM_USER modify USERCODE VARCHAR2(38);

-- Add/modify columns 
alter table DCM_SOCIALRESOURCE modify CREATOR VARCHAR2(38);

-- Add/modify columns 
alter table DCM_DOWNLOAD modify DOWNLOADER VARCHAR2(38);


-- Add/modify columns 
alter table DCM_SHARE modify SHARER VARCHAR2(38);

-- Add/modify columns 
alter table DCM_PERSONALMATERIAL modify CREATOR VARCHAR2(38);


-- Add/modify columns 
alter table DCM_Group modify CREATOR VARCHAR2(38);


-- Add/modify columns 
alter table DCM_USER modify LOGINNAME VARCHAR2(100);

---------------------以上已经同步到aws生产环境  2016/10/10-----------------

create table DCM_DIC_channel
(
  ID         inteGER PRIMARY KEY,
  NAME       varchar2(20),
  CODE VARCHAR2(38),
  ISBUILDIN  inteGER,
  CREATETIME date default sysdate
)
;
-- Add comments to the table 
comment on table DCM_DIC_channel
  is '频道字典';
-- Add comments to the columns 
comment on column DCM_DIC_channel.ID
  is '主键ID';
comment on column DCM_DIC_channel.NAME
  is '频道名称';
comment on column DCM_DIC_channel.CODE
  is '频道编码';
comment on column DCM_DIC_channel.ISBUILDIN
  is '是否内置用户，1：是；0：否';
comment on column DCM_DIC_channel.CREATETIME
  is '创建时间';
  
 INSERT INTO DCM_DIC_channel VALUES(SEQ_DCM_OID.NEXTVAL, '默认频道','default',1， sysdate);
 
 COMMIT;
 
  INSERT INTO DCM_DIC_channel VALUES(SEQ_DCM_OID.NEXTVAL, '其他','other',0， sysdate);
 
 COMMIT;
 
 create table DCM_USER_TRY
(
  ID         inteGER,
  SORTID     inteGER,
  company    varchar2(100),
  DEPARTMENT varchar2(100),
  USERNAME   varchar2(50),
  SEX        varchar2(5),
  Position   varchar2(50),
  COMPANYCODE VARCHAR2(50)
)
;
-- Add comments to the table 
comment on table DCM_USER_TRY
  is '试用账号';
-- Add comments to the columns 
comment on column DCM_USER_TRY.ID
  is 'ID';
comment on column DCM_USER_TRY.SORTID
  is '排序号';
comment on column DCM_USER_TRY.company
  is '企业';
comment on column DCM_USER_TRY.COMPANYCODE
  is '企业编码';
comment on column DCM_USER_TRY.DEPARTMENT
  is '部门';
comment on column DCM_USER_TRY.USERNAME
  is '用户名';
comment on column DCM_USER_TRY.SEX
  is '性别';
comment on column DCM_USER_TRY.Position
  is '岗位';
  
  -- Add/modify columns 
alter table DCM_USER add realm varchar2(25);
-- Add comments to the columns 
comment on column DCM_USER.realm
  is '域名字';
  
  

---------------------以上已经同步到aws生产环境  2016/10/14-----------------

-- Add/modify columns 
alter table DCM_DIC_CHANNEL add CaseId varchar2(38);
-- Add comments to the columns 
comment on column DCM_DIC_CHANNEL.CaseId
  is 'case的id';
  

---------------------以上已经同步到aws生产环境  2016/10/15-----------------

-- Add/modify columns 
alter table DCM_USER_TRY add Role varchar2(50);
-- Add comments to the columns 
comment on column DCM_USER_TRY.Role
  is '角色';
  
---------------------以上已经同步到aws生产环境  2016/10/16-----------------  
  

select * from dcm_privtemplate t where t.restypecode = 'Res_Pck_Institute'  and t.restypestatus = 1 and t.privcode='Priv_Download'


 
--20161020，建立索引
alter table DCM_SOCIALRESOURCE add primary key(id);
-- Create/Recreate indexes 
create index IDX_DCM_SOCIALRESOURCE on DCM_SOCIALRESOURCE (resid, restypecode, creator);

-- Create/Recreate indexes 
create index IDX_DCM_USERDOMAINROLE on DCM_USERDOMAINROLE (userid, domaincode, rolecode);

-- Create/Recreate indexes 
create index IDX_DCM_SHARE on DCM_SHARE (RESID, TARGETDOMAINCODE, STATUS);
  
-- Create/Recreate indexes 
create index IDX_DCM_PRIVTemp on DCM_PRIVTEMPLATE (restypecode, restypestatus, rolecode);

-- Create/Recreate indexes 
create index IDX_DCM_ORG on DCM_ORGANIZATION (parentid, domaintype, orgcode);

-- Create/Recreate indexes 
create index IDX_DCM_ORG_USER on DCM_ORG_USER (ORGID);


-- Add/modify columns 
alter table DCM_SHARE add ISFOLDER inteGER;
-- Add comments to the columns 
comment on column DCM_SHARE.ISFOLDER
  is '是否文件夹。1：文件夹；0：文件';
  
  
  create table DCM_LOG
(
  ID          NUMBER  PRIMARY KEY,
  CATEGORY       VARCHAR2(256),
  EVENTNAME      VARCHAR2(256),
  DATETIME       DATE,
  MACHINEADDRESS VARCHAR2(1024),
  HANDLERS       VARCHAR2(38),
  DESCRIPTION    VARCHAR2(2048),
  SYSTEMNAME     VARCHAR2(128)
);
-- Add comments to the columns 
comment on column DCM_LOG.CATEGORY
  is '日志级别。系统：system';
comment on column DCM_LOG.EVENTNAME
  is '事件名称。登录：login；登出：logout';
comment on column DCM_LOG.DATETIME
  is '发生时间';
comment on column DCM_LOG.MACHINEADDRESS
  is '操作地址';
comment on column DCM_LOG.HANDLERS
  is '操作者';
comment on column DCM_LOG.DESCRIPTION
  is '描述';
comment on column DCM_LOG.SYSTEMNAME
  is '系统名称';
  
  --去除院员对院级资料的下载权限
  delete from dcm_privtemplate t where t.rolecode = 'R_Institute_Member' and t.privcode = 'Priv_Download' and t.restypecode = 'Res_Pck_Institute';

-- Create sequence 日志id
create sequence SEQ_DCM_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

---------------------以上已经同步到aws生产环境  2016/10/24-----------------  
  
-- Add/modify columns 
alter table DCM_GROUP add realm varchar2(25);
-- Add comments to the columns 
comment on column DCM_GROUP.realm
  is '域';
  
---------------------以上已经同步到local dev环境 2016/10/24-----------------   
---------------------以上已经同步到aws生产环境  2016/11/07------------------   


-- Add/modify 用户职位大小
alter table DCM_USER modify POSITION VARCHAR2(80);


create table DCM_UPLOAD
(
  ID          INTEGER not null,
  RESID       VARCHAR2(38),
  UPLOADER    VARCHAR2(38),
  CREATETIME  DATE,
  RESTYPECODE VARCHAR2(50),
  DOMAINCODE  VARCHAR2(50),
  PROXY       VARCHAR2(38)
);
-- Add comments to the columns 
comment on column DCM_UPLOAD.ID
  is '主键id';
comment on column DCM_UPLOAD.RESID
  is '资源id';
comment on column DCM_UPLOAD.UPLOADER
  is '真正的上传者';
comment on column DCM_UPLOAD.CREATETIME
  is '创建时间';
comment on column DCM_UPLOAD.RESTYPECODE
  is '资源类型';
comment on column DCM_UPLOAD.DOMAINCODE
  is '空间域编码';
comment on column DCM_UPLOAD.PROXY
  is '代理者';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DCM_UPLOAD
  add primary key (ID);
  
  
 --20161221 重新设计角色类型（针对广规院）--------begin
delete from dcm_role t where t.rolecode like 'R_Project_%' and t.roletype = 2;
commit;

insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Manager', '项目负责人', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Support', '项目指导', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Assistant', '项目助理', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_DPProManager', '专业负责人', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_DPDesigner', '设计人员', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPFirstExaminer', '初审', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPSecondExaminer', '审核', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPThirdExaminer', '审定', 2);
commit;
  
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','MAJOR_VERSION');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','CREATE_INSTANCE');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','DELETE');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','WRITE_ACL');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','READ_ACL');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','Priv_Download');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','Priv_Print');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 0, 'R_Project_Assistant','READ_ACL');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 0, 'R_Project_Assistant','Priv_Download');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','Priv_Print');
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Assistant','Priv_MemberAssignment');

----------------------end

--2016/12/26 添加项目合作者角色
INSERT INTO DCM_ROLE(ID,ROLECODE,ROLENAME,ROLETYPE)
VALUES(SEQ_DCM_OID.NEXTVAL, 'R_Project_Partner','合作者',2);
 
-- 添加属性：【是否大牛】
alter table DCM_USERDOMAINROLE add ISTOP inteGER;
-- Add comments to the columns 
comment on column DCM_USERDOMAINROLE.ISTOP
  is '是否大牛。1：是；0：否';
  
-- Add/modify columns 
alter table DCM_USERDOMAINROLE add LastTime date default SYSDATE;
-- Add comments to the columns 
comment on column DCM_USERDOMAINROLE.LastTime
  is '最后修改时间';

--添加项目小团队
create table DCM_Team
(
  ID        INTEGER,
  NAME      VARCHAR2(50),
  PROJECTID INTEGER,
  CREATETIME DATE
);
-- Add comments to the table 
comment on table DCM_Team
  is '项目小团队';
-- Add comments to the columns 
comment on column DCM_Team.ID
  is '主键ID';
comment on column DCM_Team.NAME
  is '组别名称';
comment on column DCM_Team.PROJECTID
  is '项目组ID';
comment on column DCM_Team.CREATETIME
  is '创建时间';
alter table DCM_Team add primary key(id);

alter table DCM_Team
  add constraint FK_DCM_Team foreign key (PROJECTID)
  references dcm_group (ID) on delete cascade;

-- 项目组别和成员关联表
create table DCM_Team_USER
(
  ID             INTEGER,
  TeamID INTEGER,
  USERID         INTEGER,
  CREATETIME DATE,
  IsLeader INTEGER default 0
);
-- Add comments to the table 
comment on table DCM_Team_USER
  is '项目组别与成员关系表';
-- Add comments to the columns 
comment on column DCM_Team_USER.ID
  is '主键ID';
comment on column DCM_Team_USER.TeamID
  is '团队id';
comment on column DCM_Team_USER.USERID
  is '用户id';
comment on column DCM_Team_USER.CREATETIME
  is '创建时间';
comment on column DCM_Team_USER.IsLeader
  is '是否队长。1：是；0：否';

alter table DCM_Team_USER add primary key(id);
alter table DCM_Team_USER
  add constraint FK_DCM_Team_USER foreign key (TeamID)
  references dcm_team (ID) on delete cascade;
  
--添加角色【合作者】的权限模板
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_Project_Partner','READ_ACL');

-- 添加任务的开始和结束时间
alter table DCM_TASK add BeginTime date;
alter table DCM_TASK add EndTime date;
-- Add comments to the columns 
comment on column DCM_TASK.BeginTime
  is '开始时间';
comment on column DCM_TASK.EndTime
  is '结束时间';
  
--添加所级-实习生角色
insert into dcm_role(id,rolecode,rolename,roletype,comments)
values(SEQ_DCM_OID.nextval,'R_Department_Intern','所级实习生',1,'');
commit;

insert into dcm_userdomainrole 
values(seq_dcm_oid.nextval, 623681,'9AB0D392-699C-F693-09CC-1FCC5D2E1934', 'Domain_Department', 'R_Department_Intern', sysdate,'',0,sysdate )

---------------------以上已经同步到aws生产环境  2017/01/16-----------------  

--20170214
--创建机构扩展的字典信息，包括职位和团队
create table dcm_dic_orgext
(
  ID    inteGER,
  NAME  varchar2(20),
  ORGID inteGER,
  Type INTEGER
)
;
-- Add comments to the table 
comment on table dcm_dic_orgext
  is '机构扩展字典信息';
-- Add comments to the columns 
comment on column dcm_dic_orgext.ID
  is '主键ID';
comment on column dcm_dic_orgext.NAME
  is '职位名称';
comment on column dcm_dic_orgext.ORGID
  is '组织ID';
comment on column dcm_dic_orgext.Type
  is '类型。0：职位；1：团队';
alter table dcm_dic_orgext add primary key(id);

--20170216
-- Add/modify columns 
alter table DCM_USER add TeamName varchar2(20);
-- Add comments to the columns 
comment on column DCM_USER.TeamName
  is '团队名称';
  
-- Add/modify columns 
alter table DCM_USER add superadmin inteGER default 0;
-- Add comments to the columns 
comment on column DCM_USER.superadmin
  is '是否超管。1：是；0：否';

--2017/02/20
-- Add/modify columns 
alter table DCM_TASK add target varchar2(2000);
alter table DCM_TASK add Focus varchar2(2000);
alter table DCM_TASK add Audience varchar2(2000);
alter table DCM_TASK add HardwareReq inteGER;
alter table DCM_TASK add SolutionTemp integer;
alter table DCM_TASK add LocalSupport inteGER;
alter table DCM_TASK add Headfeedback varchar2(2000);
alter table DCM_TASK add Customerfeedback varchar2(2000);
-- Add comments to the columns 
comment on column DCM_TASK.target
  is '任务目标';
comment on column DCM_TASK.Focus
  is '客户关注点';
comment on column DCM_TASK.Audience
  is '任务受众';
comment on column DCM_TASK.HardwareReq
  is '是否有硬件要求，1：是；0：否';
comment on column DCM_TASK.SolutionTemp
  is '是否有方案模板，1：是；0：否';
comment on column DCM_TASK.LocalSupport
  is '是否需要现场支持，1：是；0：否';
comment on column DCM_TASK.Headfeedback
  is '负责人反馈';
comment on column DCM_TASK.Customerfeedback
  is '客户反馈';

------------添加对脑图的权限控制
--资源：Res_BrainMap（脑图）

insert into dcm_restype
values(seq_dcm_oid.nextval, 'Res_BrainMap', '脑图');
commit;

insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_Forward','前移',4398046511104);
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_Backward','后移',8796093022208);
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_HigherLevel','上级',17592186044416);
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_LowerLevel','下级',35184372088832);
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_SameLevel','同级',70368744177664);
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_Move','移动',140737488355328);
commit;


--前移
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_Forward');
--后移
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_Backward');
--上级
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_HigherLevel');
--下级
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_LowerLevel');
--同级
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_SameLevel');
--删除
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'DELETE');
--编辑
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'WRITE_ACL');
  commit;
--移动
insert into dcm_privtemplate
values
  (seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_Project_Manager', 'Priv_Move');
  commit;
  
 -- Add/modify columns 
alter table DCM_TASK add ExpectPeople varchar2(500);
-- Add comments to the columns 
comment on column DCM_TASK.ExpectPeople
  is '预期人员';
  
alter table DCM_TASK add Type varchar2(20);
-- Add comments to the columns 
comment on column DCM_TASK.Type
  is '任务类型';

  
---------------------以上已经同步到aws生产环境  2017/02/22-----------------  
--2017/02/23
--添加“系统”资源
insert into dcm_restype 
values(seq_dcm_oid.nextval, 'Res_System', '系统');
commit;

--添加“项目助理”的人员管理权限
insert into dcm_privtemplate
values(seq_dcm_oid.nextval, 'Res_Space_Project',	1,	'R_Project_Assistant',	'Priv_MemberAssignment');
commit;

--添加角色类型字典：5-系统级别
-- Add comments to the columns 
comment on column DCM_ROLE.ROLETYPE
  is '角色类型。0：院级；1：所级；2：项目级；3：组（讨论组，项目组）；4：所有者;5:系统级别';
  
--添加超级管理员角色  
insert into dcm_role 
values(seq_dcm_oid.nextval, 'R_Sys_SuperAdmin', '超级管理员',5,'超级管理员角色');
commit;

--添加项目助理的权限模板
insert into dcm_privtemplate
  select seq_dcm_oid.nextval,
         t.restypecode,
         t.restypestatus,
         'R_Project_Assistant',
         t.privcode
    from dcm_privtemplate t
   where t.rolecode = 'R_Project_Manager'
     and t.restypestatus = 1
     and t.Restypecode in ('Res_Pck_Project', 'Res_BrainMap');
     
 --添加院管对所资料的权限模板
 insert into dcm_privtemplate
 values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Institute_Manager','READ_ACL');
 commit;
--添加版本管理权限
 insert into dcm_privtemplate
 values(seq_dcm_oid.nextval,'Res_Pck_Department',1,'R_Department_Manager','MAJOR_VERSION');
 commit;
 
  insert into dcm_privtemplate
 values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Manager','MAJOR_VERSION');
 commit;
 
   insert into dcm_privtemplate
 values(seq_dcm_oid.nextval,'Res_Pck_Institute',1,'R_Institute_Member','Priv_Download');
 commit;

---------------------以上已经同步到aws生产环境  2017/02/24-----------------  

--2017-03-07
--添加项目组一般成员权限

--新增
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Support','CREATE_INSTANCE');
--删除
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Support','DELETE');
--修改
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Support','WRITE_ACL');
--下载
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Support','Priv_Download');
--版本管理
insert into dcm_privtemplate(id,restypecode,restypestatus,rolecode,privcode)
values(seq_dcm_oid.nextval,'Res_Pck_Project',1,'R_Project_Support','MAJOR_VERSION');
commit;

---------------------以上已经同步到aws生产环境  2017/02/24----------------- 
 
--2017/03/07，修改院和所的角色
-----------------------院级--------------------
update dcm_role r set r.rolename = '一般成员' where r.rolecode = 'R_Institute_Member' and r.roletype = 0;
update dcm_role r set r.Roletype = -1 where r.rolecode = 'R_Institute_Manager';

insert into dcm_role
values(seq_dcm_oid.nextval, 'R_Ins_MaterialManager','资料管理员',0,'院级资料管理员');
insert into dcm_role
values(seq_dcm_oid.nextval, 'R_Ins_DecisionManager','决策管理员',0,'院级决策管理员');
commit;

-----------------------所级--------------------
update dcm_role r set r.rolename = '一般成员' where r.rolecode = 'R_Department_Member' and r.roletype = 1;
update dcm_role r set r.Roletype = -1 where r.rolecode = 'R_Department_Manager';
update dcm_role r set r.Roletype = -1 where r.rolecode = 'R_Department_Intern';


insert into dcm_role
values(seq_dcm_oid.nextval, 'R_Dep_MaterialManager','资料管理员',1,'所级资料管理员');
insert into dcm_role
values(seq_dcm_oid.nextval, 'R_Dep_DecisionManager','决策管理员',1,'所级决策管理员');
commit;

--2017-03-08
--添加权限
insert into dcm_privilege
values(seq_dcm_oid.nextval, 'Priv_Upload', '上传',281474976710656);

--添加权限模板，院级资料管理员（上传和分享）
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','Priv_Upload');  --R_MaterialManager
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','Priv_Share');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','CREATE_INSTANCE');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','DELETE');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','MAJOR_VERSION');
commit;
--添加权限模板，所级资料管理员（上传和分享）
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','Priv_Upload');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','Priv_Share');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','CREATE_INSTANCE');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','DELETE');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','MAJOR_VERSION');
commit;

--添加权限模板，院级决策管理员（项目管理权限）
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Space_Project',1, 'R_Ins_DecisionManager','Priv_ProjectManagement');--R_DecisionManager
--添加权限模板，所级决策管理员（项目管理权限）
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Space_Project',1, 'R_Dep_DecisionManager','Priv_ProjectManagement');

--更改以前的部门管理员角色名称
update dcm_userdomainrole t set t.rolecode = 'R_Dep_MaterialManager' where t.rolecode = 'R_Department_Manager'

update dcm_userdomainrole t set t.rolecode = 'R_Ins_MaterialManager' where t.rolecode = 'R_Institute_Manager'
commit;

--添加
alter table DCM_USERDOMAINROLE add UserCode varchar2(38);
alter table DCM_USERDOMAINROLE add UserType integer default 0;
-- Add comments to the columns 
comment on column DCM_USERDOMAINROLE.UserCode
  is '用户唯一编码，为了库与库之间UserId不冲突';
comment on column DCM_USERDOMAINROLE.UserType
  is '用户类型。0：内部用户；1：外部用户';
  
  --添加用户编码
  update dcm_userdomainrole t
     set t.usercode = (select u.usercode
                         from dcm_user u
                        where u.id = t.userid)
   where exists (select 1 from dcm_user u where u.id = t.userid);
   
   update dcm_userdomainrole t set t.usertype = 0 where t.usertype is null;
   commit;

---------------------以上已经同步到aws生产环境  2017/03/10----------------- 

--2017/03/15 补充权限模板，添加项目合作者的上传版本权限
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Project',1,'R_Project_Partner','MAJOR_VERSION');

--补全权限模板
insert into dcm_privtemplate
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','READ_ACL');

insert into dcm_privtemplate
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','READ_ACL');

commit;
---------------------以上已经同步到aws生产环境  2017/03/17----------------- 
--2017/03/21
-- Add/modify columns 
alter table DCM_TASK add responsetime date;
-- Add comments to the columns 
comment on column DCM_TASK.responsetime
  is '客户响应时间';

--添加权限模板，院级决策管理员对项目空间的权限
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Space_Project',1, 'R_Ins_DecisionManager','Priv_Statistics');--R_DecisionManager
--添加权限模板，所级决策管理员对项目空间的权限
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Space_Project',1, 'R_Dep_DecisionManager','Priv_Statistics');
commit;
--添加统计权限
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_Statistics','统计',562949953421312);
commit;

---------------------以上已经同步到aws生产环境  2017/03/24-----------------
insert into  dcm_privilege 
values(seq_dcm_oid.nextval, 'Priv_Rename','重命名',1125899906842624);
commit;

insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Institute',1, 'R_Ins_MaterialManager','Priv_Rename');
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Department',1, 'R_Dep_MaterialManager','Priv_Rename');
commit;

--为了汇总任务，添加属性【状态】
-- Add/modify columns 
alter table DCM_TASK add status varchar2(10);
-- Add comments to the columns 
comment on column DCM_TASK.status
  is '状态。585d68：待接受；0032FF：进行中；00C800：已完成；FF6432：停止';

--同步状态信息
update dcm_task t set t.status = (select sr.tag from dcm_socialresource sr where sr.resid = t.taskid);
update dcm_task t set t.status = '585d68' where t.status is null;
commit;


---------------------以上已经同步到aws生产环境  2017/04/01-----------------

--添加合作者角色的下载权限
insert into dcm_privtemplate 
values(seq_dcm_oid.nextval, 'Res_Pck_Project',1,'R_Project_Partner','Priv_Download');
commit;

-- Add comments to the columns 
comment on column DCM_USERDOMAINROLE.USERID
  is '用户id(已废弃)';

---------------------以上已经同步到aws生产环境  2017/04/14-----------------

-- Add/modify columns 
alter table DCM_TASK add Evaluation varchar2(20);
-- Add comments to the columns 
comment on column DCM_TASK.Evaluation
  is '任务评价';

-- Add/modify columns 
alter table DCM_TASK add realm varchar2(25);
-- Add comments to the columns 
comment on column DCM_TASK.realm
  is '域';
--更新域
update dcm_task t set t.realm = 'thupdi';
commit;

---------------------以上已经同步到aws生产环境  2017/04/25-----------------
--添加清华同衡用户【小智助理】的角色-所级资料管理员
insert into dcm_userdomainrole
  (id,
   userid,
   domaincode,
   domaintype,
   rolecode,
   createtime,
   istop,
   usercode,
   usertype)
values
  (seq_dcm_oid.nextval,
   697630,
   '3ECD8224-12DA-5E72-E053-0100007F4FEA',
   'Domain_Department',
   'R_Dep_MaterialManager',
   sysdate,
   0,
   'B85A24DF-0A84-4475-BC8F-2A19CA1AA9E7',
   0);
   
   
  insert into dcm_userdomainrole
  (id,
   userid,
   domaincode,
   domaintype,
   rolecode,
   createtime,
   istop,
   usercode,
   usertype)
values
  (seq_dcm_oid.nextval,
   697630,
   '3ECD8224-12DA-5E72-E053-0100007F4FEA',
   'Domain_Department',
   'R_Dep_DecisionManager',
   sysdate,
   0,
   'B85A24DF-0A84-4475-BC8F-2A19CA1AA9E7',
   0);
  --添加清华同衡用户【小智助理】的角色-院级资料管理员 
   insert into dcm_userdomainrole
  (id,
   userid,
   domaincode,
   domaintype,
   rolecode,
   createtime,
   istop,
   usercode,
   usertype)
values
  (seq_dcm_oid.nextval,
   697630,
   '4025C686-972C-467E-B895-72777C0FFA0A',
   'Domain_Institute',
   'R_Ins_MaterialManager',
   sysdate,
   0,
   'B85A24DF-0A84-4475-BC8F-2A19CA1AA9E7',
   0);
   
   
    insert into dcm_userdomainrole
  (id,
   userid,
   domaincode,
   domaintype,
   rolecode,
   createtime,
   istop,
   usercode,
   usertype)
values
  (seq_dcm_oid.nextval,
   697630,
   '4025C686-972C-467E-B895-72777C0FFA0A',
   'Domain_Institute',
   'R_Ins_DecisionManager',
   sysdate,
   0,
   'B85A24DF-0A84-4475-BC8F-2A19CA1AA9E7',
   0);
   
   
   ---------------------以上已经同步到aws生产环境  2017/05/09-----------------
   
   --为租户添加ceadmin，因为现在每个用户都跟域相关
   insert into dcm_user
     (id,
      username,
      loginname,
      userpwd,
      usercode,
      email,
      currentstatus,
      datecreated,
      datelastactivity,
      domaintype,
      sex,
      isbuildin,
      avatar,
      Dn,
      Realm,
      Superadmin)
     select seq_dcm_oid.nextval,
            username,
            loginname,
            userpwd,
            usercode,
            email,
            currentstatus,
            datecreated,
            datelastactivity,
            domaintype,
            sex,
            isbuildin,
            avatar,
            Dn,
            'thupdi',
            Superadmin
       from dcm_user u where u.loginname = 'ceadmin' and u.realm = 'thupdi';
     
    ---------------------以上已经同步到aws生产环境  2017/05/15-----------------
    
    -- Add/modify columns 
alter table DCM_DIC_ORGEXT add orgCode varchar2(38);
-- Add comments to the columns 
comment on column DCM_DIC_ORGEXT.orgCode
  is '机构编码，用来做关联作用';
  
  --同步数据
  update dcm_dic_orgext t set t.orgcode = (select orgcode from dcm_organization org where org.id = t.orgid);
   ---------------------以上已经同步到aws生产环境  2017/08/03-----------------
  
    -- Add/modify columns 
alter table DCM_ORGANIZATION add realm varchar2(25);
-- Add comments to the columns 
comment on column DCM_ORGANIZATION.realm
  is '域';
  --更新机构所属域
  update dcm_organization t
   set t.realm = 
   (case 
      when instr(t.dn, 'thupdi') > 0 then 'thupdi' 
      when instr(t.dn, 'dist') > 0 then 'dist' 
      when instr(t.dn, 'jnup') > 0 then 'jnup' 
      when instr(t.dn, 'gzpi') > 0 then 'gzpi' 
      end);
      
      -- Add/modify columns 
alter table DCM_ORGANIZATION add orgType varchar2(100);
-- Add comments to the columns 
comment on column DCM_ORGANIZATION.orgType
  is '机构类型，如：中方，外方';
  
  -- Add/modify columns 
alter table SGA_COMPANY add materialSourceCount varchar2(50);
alter table SGA_COMPANY add materialSourceDepartment varchar2(100);
-- Add comments to the columns 
comment on column SGA_COMPANY.materialSourceCount
  is '资料来源数';
comment on column SGA_COMPANY.materialSourceDepartment
  is '资料来源部门';
  
  -- Add/modify columns 
alter table DCM_GROUP add img varchar2(1024);
-- Add comments to the columns 
comment on column DCM_GROUP.img
  is '项目背景图url';
     ---------------------以上已经同步到aws生产环境  2017/08/06-----------------
  
  -- Create table
create table DCM_workgroup
(
  ID          inteGER,
  name        varchar2(50),
  projectGuid varchar2(38),
  realm       varchar2(25)
)
;
-- Add comments to the table 
comment on table DCM_workgroup
  is '工作组';
-- Add comments to the columns 
comment on column DCM_workgroup.ID
  is '主键ID';
comment on column DCM_workgroup.name
  is '工作组名称';
comment on column DCM_workgroup.projectGuid
  is '项目唯一标识';
comment on column DCM_workgroup.realm
  is '域名称';

  alter table DCM_workgroup add primary key(id);
  
  -- Create table
create table DCM_WG_Org
(
  ID      integer,
  wgid    integer,
  orgguid varchar2(38)
)
;
-- Add comments to the table 
comment on table DCM_WG_Org
  is '工作组和机构关联';
-- Add comments to the columns 
comment on column DCM_WG_Org.ID
  is '主键id';
comment on column DCM_WG_Org.wgid
  is '工作组id';
comment on column DCM_WG_Org.orgguid
  is '机构guid';
  
  alter table DCM_WG_Org add primary key(id);
    ---------------------以上已经同步到aws生产环境  2017/08/10-----------------
    
    
  -- Add comments to the columns 
comment on column DCM_ROLE.ROLETYPE
  is '角色类型。0：院级；1：所级；2：项目级；3：组（讨论组，项目组）；4：所有者；5:系统级别；6：团队级别';
--添加资源类型
  insert into dcm_restype 
  select seq_dcm_oid.nextval, 'Res_Space_Team', '团队空间' from dual;
  --添加域类型
  insert into dcm_dic_domaintype
  values(seq_dcm_oid.nextval, '系统', 'Domain_System');
  commit;
 --初始化超管（济南下测试用户）
 --韦富杰
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'jnup',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   '30E2BD75-2B00-47D7-805B-3F3AF21B1F34',
   0);
   --倪志航
   insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'jnup',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   'FEC8CBF9-0717-4637-9FE9-B5FD9FD3E207',
   0);
   --张赛男
      insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'jnup',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   '66F76346-F755-4577-BED7-FF8DDE0975E3',
   0);
   --王勇良
     insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'jnup',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   '74FCEECE-6F50-43EF-A57B-4A388D01B952',
   0);
     --梁婷
     insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'jnup',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   'C5D47D72-31BA-4B84-AB83-A03C0B2CB147',
   0);
commit;

-- 为角色添加域
alter table DCM_ROLE add realm varchar2(25) default 'common';
-- Add comments to the columns 
comment on column DCM_ROLE.realm
  is '域';
  
  -- 为权限模板添加域
alter table DCM_PRIVTEMPLATE add realm varchar2(25) default 'common';
-- Add comments to the columns 
comment on column DCM_PRIVTEMPLATE.realm
  is '域。默认为：common，公共';
  
  -- Add/modify columns 
alter table DCM_PRIVTEMPLATE add scope integer default 0;
-- Add comments to the columns 
comment on column DCM_PRIVTEMPLATE.scope
  is '权限范围。0：默认当前；1：全部';

--识别系统角色，带有域
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_DECISION_MAKER', '决策者', 0, '企业决策者', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_DATA_ADMIN', '资料管理员', 0, '企业资料管理员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_MEMBER', '一般成员', 0, '企业成员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_DECISION_MAKER', '决策者', 1, '部门决策者', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_DATA_ADMIN', '资料管理员', 1, '部门资料管理员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_MEMBER', '一般成员', 1, '部门成员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_LEADER', '负责人', 2, '项目负责人', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_ASSISTANT', '助理', 2, '项目助理', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_MEMBER', '一般成员', 2, '一般成员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_PARTNER', '合作者', 2, '合作者', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_SYS_ADMIN', '系统管理员', 5, '系统管理员', 'jnup' from dual;

insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_LEADER', '负责人', 6, '团队负责人', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_ASSISTANT', '助理', 6, '团队助理', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_MEMBER', '一般成员', 6, '团队成员', 'jnup' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_PARTNER', '合作者', 6, '合作者', 'jnup' from dual;

--识别系统权限
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_EDIT_INS', '编辑企业信息' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_MGNT_ORG', '管理组织机构' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_MGNT_USER', '管理用户信息' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_GRANT_USER', '分配用户权限' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_CREATE_PRJ', '创建项目' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_TASK_SUMMARY', '查看任务汇总' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_VIEW_PRJMIN', '查看项目基本信息' from dual;--ps：min代表项目信息的最小化，意为项目基本信息
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_HOT', '设置热门项目' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_CREATE_TEAM', '创建团队' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_PUBLISH_TASK', '发布任务' from dual;--发布项目任务或者团队任务
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_EDIT_TASK', '编辑任务' from dual;--编辑项目任务或者团队任务
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_DELETE_TASK', '删除任务' from dual;--删除项目任务或者团队任务
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_JOIN_TASK', '参与任务' from dual;--参与项目任务或者团队任务
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'PRIV_DELETE_PROJECT', '删除项目' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'CREATE_BRAINMAP', '创建脑图' from dual;
insert into dcm_privilege(id, privcode, privname)
select seq_dcm_oid.nextval, 'VIEW_BRAINMAP', '只读脑图' from dual;

--DELETE：删除项目和删除团队（点击菜单实时获取）
--Priv_MemberAssignment：管理项目人员和团队人员
--CREATE_INSTANCE：对脑图资源（Res_BrainMap）有创建权限（绘制项目脑图和绘制团队脑图）

--权限模板，济南-------------------------------------------
-----系统管理员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_EDIT_INS', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_MGNT_ORG', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_MGNT_USER', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_GRANT_USER', 0, 'jnup'  from dual;

-----院级决策者
-->院级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_CREATE_PRJ', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 1, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_TASK_SUMMARY', 1, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_HOT', 1, 'jnup'  from dual;
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DECISION_MAKER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DECISION_MAKER', 'Priv_Download', 0, 'jnup'  from dual;
commit;
--院级决策者示例数据，倪志航
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '408F19FE-3F17-47DF-9A2D-9911AE5793AF',
   'Domain_Institute',
   'R_I_DECISION_MAKER',
   sysdate,
   'FEC8CBF9-0717-4637-9FE9-B5FD9FD3E207',
   0);
   commit;
 
-----院级资料管理员
-->院级空间（无）
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'WRITE', 0, 'jnup'  from dual;
commit;
--院级资料管理员示例数据，韦富杰
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '408F19FE-3F17-47DF-9A2D-9911AE5793AF',
   'Domain_Institute',
   'R_I_DATA_ADMIN',
   sysdate,
   '30E2BD75-2B00-47D7-805B-3F3AF21B1F34',
   0);
   commit;
   
-----院级一般成员
-->院级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_MEMBER', 'PRIV_CREATE_TEAM', 0, 'jnup'  from dual;
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_MEMBER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_MEMBER', 'Priv_Download', 0, 'jnup'  from dual;
commit;
--院级一般成员示例数据，王勇良
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '408F19FE-3F17-47DF-9A2D-9911AE5793AF',
   'Domain_Institute',
   'R_I_MEMBER',
   sysdate,
   '74FCEECE-6F50-43EF-A57B-4A388D01B952',
   0);
   commit;
   
-----所级决策者
-->所级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_DECISION_MAKER', 'PRIV_TASK_SUMMARY', 0, 'jnup'  from dual;
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DECISION_MAKER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DECISION_MAKER', 'Priv_Download', 0, 'jnup'  from dual;
commit;
--所级决策者示例数据，倪志航
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '50FE89CC-8368-401F-91E6-0943B314B3A3',
   'Domain_Department',
   'R_D_DECISION_MAKER',
   sysdate,
   'FEC8CBF9-0717-4637-9FE9-B5FD9FD3E207',
   0);
   commit;
 
-----所级资料管理员
-->所级空间（无）
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'WRITE', 0, 'jnup'  from dual;
commit;
--所级资料管理员示例数据，韦富杰
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '50FE89CC-8368-401F-91E6-0943B314B3A3',
   'Domain_Department',
   'R_D_DATA_ADMIN',
   sysdate,
   '30E2BD75-2B00-47D7-805B-3F3AF21B1F34',
   0);
   commit;
   
-----所级一般成员
-->所级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_MEMBER', 'PRIV_CREATE_TEAM', 0, 'jnup'  from dual;
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_MEMBER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_MEMBER', 'Priv_Download', 0, 'jnup'  from dual;
commit;
--所级一般成员示例数据，王勇良
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   '50FE89CC-8368-401F-91E6-0943B314B3A3',
   'Domain_Department',
   'R_D_MEMBER',
   sysdate,
   '74FCEECE-6F50-43EF-A57B-4A388D01B952',
   0);
   commit;
   
---------------------------------------以上已执行------------------------------------
--3、项目
--3.1 项目空间
--3.1.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_DELETE_PROJECT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_MemberAssignment', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_ProjectManagement', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;

--3.1.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_MemberAssignment', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_ProjectManagement', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
--3.1.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
--3.1.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
--3.2 项目包
--3.2.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'WRITE', 0, 'jnup'  from dual;

--3.2.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'WRITE', 0, 'jnup'  from dual;
--3.2.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'WRITE', 0, 'jnup'  from dual;
--3.2.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'VIEW_CONTENT', 0, 'jnup'  from dual;

--3.3 脑图
--3.3.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_LEADER', 'CREATE_BRAINMAP', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_LEADER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--3.3.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_ASSISTANT', 'CREATE_BRAINMAP', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_ASSISTANT', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--3.3.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_MEMBER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--3.3.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_PARTNER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
commit;

--------------------以上已执行，2017.08.23
--4、团队
--4.1 团队空间
--4.1.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_DELETE_PROJECT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_MemberAssignment', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_ProjectManagement', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
--4.1.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_MemberAssignment', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_ProjectManagement', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
--4.1.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
--4.1.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
--4.2 团队包
--4.2.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'WRITE', 0, 'jnup'  from dual;
--4.2.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'WRITE', 0, 'jnup'  from dual;

--4.2.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'WRITE', 0, 'jnup'  from dual;
--4.2.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'VIEW_CONTENT', 0, 'jnup'  from dual;

--4.3 脑图
--4.3.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_LEADER', 'CREATE_BRAINMAP', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_LEADER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--4.3.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_ASSISTANT', 'CREATE_BRAINMAP', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_ASSISTANT', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--4.3.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_MEMBER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
--4.3.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_PARTNER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;
commit;

    ---------------------以上已经同步到aws生产环境  2017/08/25-----------------
    
    -- Create table
create table DCM_NAOTU_TEAM
(
  ID       inteGER,
  caseId varchar2(38),
  minderId inteGER,
  nodeid   varchar2(38),
  teamId   varchar2(38),
  realm varchar2(25)
)
;
-- Add comments to the table 
comment on table DCM_NAOTU_TEAM
  is '脑图与团队关联';
-- Add comments to the columns 
comment on column DCM_NAOTU_TEAM.ID
  is '主键id';
comment on column DCM_NAOTU_TEAM.caseId
  is 'case唯一标识符';
comment on column DCM_NAOTU_TEAM.minderId
  is '脑图id';
comment on column DCM_NAOTU_TEAM.nodeid
  is '脑图节点id';
comment on column DCM_NAOTU_TEAM.teamId
  is '团队id';
comment on column DCM_NAOTU_TEAM.realm
  is '域';

 alter table DCM_NAOTU_TEAM add primary key(id);
 
     ---------------------以上已经同步到aws生产环境  2017/08/30-----------------
 
 --3、项目
--3.1 项目空间
--3.1.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_DELETE_PROJECT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_MemberAssignment', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_PUBLISH_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_EDIT_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_DELETE_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_JOIN_TASK', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_ProjectWork', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_ProjectManagement', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 0, 'jnup'  from dual;

--3.2 项目包
--3.2.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Upload', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'DELETE', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'VIEW_CONTENT', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'MAJOR_VERSION', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Download', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Share', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Rename', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'WRITE', 0, 'jnup'  from dual;

--3.3 脑图
--3.3.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_DECISION_MAKER', 'CREATE_BRAINMAP', 0, 'jnup'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_DECISION_MAKER', 'VIEW_BRAINMAP', 0, 'jnup'  from dual;    

insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_DECISION_MAKER', '决策者', 2, '项目决策者', 'jnup' from dual;

commit;

   ---------------------以上已经同步到aws生产环境  2017/09/05-----------------
   
 --初始化超管（team租户）
 --team管理员
insert into dcm_userdomainrole
  (id, domaincode, domaintype, rolecode, createtime, usercode, usertype)
values
  (seq_dcm_oid.nextval,
   'team',
   'Domain_System',
   'R_SYS_ADMIN',
   sysdate,
   '4E4E1518-9797-6D4F-E51A-E408EBCCBB37',
   0);
   commit;
   ---------------------以上已经同步到aws生产环境  2017/09/06-----------------
   
   -- Add/modify columns 
alter table DCM_TEAM_USER add projectId integer;
-- Add comments to the columns 
comment on column DCM_TEAM_USER.projectId
  is '项目组id';
  --补全数据
  update dcm_team_user tu
     set tu.projectid = (select t.projectid
                           from dcm_team t
                          where t.id = tu.teamid);
  commit;
  
  -- 给共享添加域
alter table DCM_SHARE add realm varchar2(25);
-- Add comments to the columns 
comment on column DCM_SHARE.realm
  is '域';
  --更新域的数据
  update dcm_share t
   set t.realm = (select realm
                    from dcm_user u
                   where u.usercode = t.targetdomaincode
                     and t.targetdomaintype = 'Domain_Person');
                     
     ---------------------以上已经同步到aws生产环境  2017/09/14-----------------