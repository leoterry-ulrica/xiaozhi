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