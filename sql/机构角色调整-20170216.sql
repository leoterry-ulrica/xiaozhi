--20170216
select * from dcm_role t where t.roletype in (0,1);

update dcm_role r set r.rolename = '��ҵ����' where r.rolename ='Ժ��'; 
update dcm_role r set r.rolename = '��ҵְԱ' where r.rolename ='ԺԱ'; 
update dcm_role r set r.rolename = '���Ź���' where r.rolename ='����'; 
update dcm_role r set r.rolename = '����ְԱ' where r.rolename ='��Ա'; 
update dcm_role r set r.rolename = '����ʵϰ��' where r.rolename ='����ʵϰ��'; 
commit;

update dcm_role r set r.roletype = -1 where r.rolename = '�߼�רԱ';
update dcm_role r set r.roletype = -1 where r.rolename = '�߼��ܹ�';
update dcm_role r set r.roletype = -1 where r.rolename = '���쵼';
update dcm_role r set r.roletype = -1 where r.rolename = '���רԱ';
update dcm_role r set r.roletype = -1 where r.rolename = 'Ժ�쵼';
update dcm_role r set r.roletype = -1 where r.rolename = 'רҵ�ܹ�';
update dcm_role r set r.roletype = -1 where r.rolename = '�ܹ�';
commit;
