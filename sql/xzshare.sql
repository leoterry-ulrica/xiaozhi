
create sequence SEQ_DCM_SHARE
minvalue 1
maxvalue 999999999999999999999999999
start with 81
increment by 1
cache 20;

-- Create table
create table XZ_SHARE_WZ
(
  id         NUMBER not null,
  caseid     VARCHAR2(50),
  wzid       VARCHAR2(50),
  email      VARCHAR2(100),
  code       VARCHAR2(10),
  desktop    VARCHAR2(20),
  createtime DATE,
  username   VARCHAR2(50)
)
tablespace XZDBSERVER_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table XZ_SHARE_WZ
  is '共享出来的微作';
-- Add comments to the columns 
comment on column XZ_SHARE_WZ.caseid
  is '项目标识';
comment on column XZ_SHARE_WZ.wzid
  is '微作标识';
comment on column XZ_SHARE_WZ.email
  is '被共享者电子邮箱';
comment on column XZ_SHARE_WZ.code
  is '生成的验证码';
comment on column XZ_SHARE_WZ.desktop
  is '桌面';
comment on column XZ_SHARE_WZ.createtime
  is '共享时间';
comment on column XZ_SHARE_WZ.username
  is '共享者用户名';
-- Create/Recreate primary, unique and foreign key constraints 
alter table XZ_SHARE_WZ
  add constraint PK_XZ_SHARE_WZ primary key (ID)
  using index 
  tablespace XZDBSERVER_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

-- Create table
create table XZ_SHARE_USER
(
  id    NUMBER not null,
  email VARCHAR2(100),
  name  VARCHAR2(50)
)
tablespace XZDBSERVER_TABLESPACE
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table XZ_SHARE_USER
  add constraint PK_XZ_SHARE_USER primary key (ID)
  using index 
  tablespace XZDBSERVER_TABLESPACE
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
