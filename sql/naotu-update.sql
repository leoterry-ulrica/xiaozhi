create table Naotu_Node_Task
(
  id               NUMBER(19) not null,
  minderid         NUMBER(19),
  nodeid          VARCHAR2(64 CHAR),
  wzid    VARCHAR2(64 CHAR)
);
alter table Naotu_Node_Task
  add primary key (ID);
