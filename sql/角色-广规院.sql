delete from dcm_role t where t.rolecode like 'R_Project_%' and t.roletype = 2;
commit;

insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Manager', '��Ŀ������', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Support', '��Ŀָ��', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_Assistant', '��Ŀ����', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_DPProManager', 'רҵ������', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_DPDesigner', '�����Ա', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPFirstExaminer', '����', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPSecondExaminer', '���', 2);
insert into dcm_role(id,rolecode,rolename,roletype)
values(seq_dcm_oid.nextval, 'R_Project_CPThirdExaminer', '��', 2);
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