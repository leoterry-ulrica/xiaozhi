--�����½��ļ��к��ƶ�Ȩ�ޣ��ɰ�realm��ֵ�滻�����⻧����
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Institute', 1, 'R_I_DATA_ADMIN', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Department', 1, 'R_D_DATA_ADMIN', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_LEADER', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_ASSISTANT', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_MEMBER', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_PARTNER', 'CREATE_CHILD', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_LEADER', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_ASSISTANT', 'Priv_Move', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_T_PARTNER', 'CREATE_CHILD', 0, 'team'  from dual;

insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'CREATE_CHILD', 0, 'team'  from dual;
insert into dcm_privtemplate(id, restypecode, restypestatus, rolecode, privcode, scope, realm)
select seq_dcm_oid.nextval, 'Res_Pck_Project', 1, 'R_P_DECISION_MAKER', 'Priv_Move', 0, 'team'  from dual;

---------------------�����Ѿ�ͬ����aws��������  2017/09/07-----------------


