--权限模板，济南-------------------------------------------
-----系统管理员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_EDIT_INS', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_MGNT_ORG', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_MGNT_USER', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_System', 1, 'R_SYS_ADMIN', 'PRIV_GRANT_USER', 0, 'team'  from dual;

-----院级决策者
-->院级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_CREATE_PRJ', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 1, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_TASK_SUMMARY', 1, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_DECISION_MAKER', 'PRIV_HOT', 1, 'team'  from dual;
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DECISION_MAKER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DECISION_MAKER', 'Priv_Download', 0, 'team'  from dual;
commit;

-----院级资料管理员
-->院级空间（无）
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Move', 0, 'team'  from dual;
commit;

-----院级一般成员
-->院级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Institute', 1, 'R_I_MEMBER', 'PRIV_CREATE_TEAM', 0, 'team'  from dual;
-->院级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_MEMBER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_MEMBER', 'Priv_Download', 0, 'team'  from dual;
commit;

-----所级决策者
-->所级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_DECISION_MAKER', 'PRIV_TASK_SUMMARY', 0, 'team'  from dual;
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DECISION_MAKER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DECISION_MAKER', 'Priv_Download', 0, 'team'  from dual;
commit;

-----所级资料管理员
-->所级空间（无）
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Move', 0, 'team'  from dual;
commit;

-----所级一般成员
-->所级空间
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Department', 1, 'R_D_MEMBER', 'PRIV_CREATE_TEAM', 0, 'team'  from dual;
-->所级资料
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_MEMBER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_MEMBER', 'Priv_Download', 0, 'team'  from dual;
commit;

--3、项目
--3.1 项目空间
--3.1.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_DELETE_PROJECT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_MemberAssignment', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'Priv_ProjectManagement', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;

--3.1.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_MemberAssignment', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'Priv_ProjectManagement', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
--3.1.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
--3.1.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_PARTNER', 'Priv_ProjectWork', 0, 'team'  from dual;
--3.2 项目包
--3.2.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Move', 0, 'team'  from dual;

--3.2.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Move', 0, 'team'  from dual;
--3.2.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Move', 0, 'team'  from dual;
--3.2.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'CREATE_CHILD', 0, 'team'  from dual;

--3.3 脑图
--3.3.1 项目负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_LEADER', 'CREATE_BRAINMAP', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_LEADER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--3.3.2 项目助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_ASSISTANT', 'CREATE_BRAINMAP', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_ASSISTANT', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--3.3.3 项目成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_MEMBER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--3.3.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_PARTNER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
commit;


--4、团队
--4.1 团队空间
--4.1.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_DELETE_PROJECT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_MemberAssignment', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'Priv_ProjectManagement', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
--4.1.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_MemberAssignment', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'Priv_ProjectManagement', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
--4.1.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
--4.1.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_PARTNER', 'Priv_ProjectWork', 0, 'team'  from dual;
--4.2 团队包
--4.2.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Move', 0, 'team'  from dual;
--4.2.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Move', 0, 'team'  from dual;

--4.2.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Move', 0, 'team'  from dual;
--4.2.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'CREATE_CHILD', 0, 'team'  from dual;

--4.3 脑图
--4.3.1 团队负责人
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_LEADER', 'CREATE_BRAINMAP', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_LEADER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--4.3.2 团队助理
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_ASSISTANT', 'CREATE_BRAINMAP', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_ASSISTANT', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--4.3.3 团队成员
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_MEMBER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
--4.3.4 合作者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_T_PARTNER', 'VIEW_BRAINMAP', 0, 'team'  from dual;
commit;

--3、项目
--3.1 项目空间
--3.1.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_DELETE_PROJECT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_MemberAssignment', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_PUBLISH_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_EDIT_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_DELETE_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_JOIN_TASK', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_ProjectWork', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'Priv_ProjectManagement', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_DECISION_MAKER', 'PRIV_VIEW_PRJMIN', 0, 'team'  from dual;

--3.2 项目包
--3.2.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Upload', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'WRITE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Move', 0, 'team'  from dual;

--3.3 脑图
--3.3.5 项目决策者
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_DECISION_MAKER', 'CREATE_BRAINMAP', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_BrainMap', 1, 'R_P_DECISION_MAKER', 'VIEW_BRAINMAP', 0, 'team'  from dual;    

insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_DECISION_MAKER', '决策者', 2, '项目决策者', 'team' from dual;

commit;


--识别系统角色，带有域
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_DECISION_MAKER', '决策者', 0, '企业决策者', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_DATA_ADMIN', '资料管理员', 0, '企业资料管理员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_I_MEMBER', '一般成员', 0, '企业成员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_DECISION_MAKER', '决策者', 1, '部门决策者', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_DATA_ADMIN', '资料管理员', 1, '部门资料管理员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_D_MEMBER', '一般成员', 1, '部门成员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_LEADER', '负责人', 2, '项目负责人', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_ASSISTANT', '助理', 2, '项目助理', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_MEMBER', '一般成员', 2, '一般成员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_P_PARTNER', '合作者', 2, '合作者', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_SYS_ADMIN', '系统管理员', 5, '系统管理员', 'team' from dual;

insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_LEADER', '负责人', 6, '团队负责人', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_ASSISTANT', '助理', 6, '团队助理', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_MEMBER', '一般成员', 6, '团队成员', 'team' from dual;
insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_T_PARTNER', '合作者', 6, '合作者', 'team' from dual;
commit;

 ----------------------------以上已经同步到aws生产环境  2017/09/07--------------------
 
 --添加个人的权限编码
 insert into dcm_role(id,rolecode,rolename,roletype,comments,realm)
select seq_dcm_oid.nextval, 'R_OWNER', '所有者', 4, '所有者', 'team' from dual;
--添加个人权限模板
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'DELETE', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'VIEW_CONTENT', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'Priv_Move', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'Priv_Download', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'Priv_Share', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Person', 1, 'R_OWNER', 'Priv_Rename', 0, 'team'  from dual;

--select * from dcm_privtemplate t where t.restypecode = 'Res_Pck_Person' and t.rolecode = 'R_OWNER'

--补充项目一般成员和团队一般成员对项目包的权限
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Rename', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Rename', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'MAJOR_VERSION', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'MAJOR_VERSION', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_MEMBER', 'Priv_Move', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_MEMBER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_T_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_MEMBER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Project', 1, 'R_P_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Space_Person', 1, 'R_OWNER', 'CREATE_CHILD', 0, 'team'  from dual;

 ----------------------------以上已经同步到aws生产环境  2017/09/14--------------------
 
 --删除所级一般成员对所级资料的下载权限
 delete from dcm_privtemplate
 where restypecode = 'Res_Pck_Department'
   and restypestatus = 1
   and rolecode = 'R_D_MEMBER'
   and privcode = 'Priv_Download'
   and scope = 0
   and realm = 'team'
   
--添加排序号 
alter table DCM_ROLE add sortid inteGER default 0;
-- Add comments to the columns 
comment on column DCM_ROLE.sortid
  is '排序号';
  --初始化排序号
  update dcm_role t set t.sortid = (case t.rolecode
  when 'R_P_DECISION_MAKER' then 0
  when 'R_P_LEADER' then 1
  when 'R_P_ASSISTANT' then 2
  when 'R_P_PARTNER' then 3
  when 'R_P_MEMBER' then 4
  
  when 'R_T_LEADER' then 1
  when 'R_T_ASSISTANT' then 2
  when 'R_T_PARTNER' then 3
  when 'R_T_MEMBER' then 4
  else 0
 end) where t.roletype in (2, 6) and t.realm = 'jnup';
 select * from dcm_role t where t.roletype in (2, 6) and t.realm = 'jnup'
  