--20170216
select * from dcm_role t where t.roletype in (0,1);

update dcm_role r set r.rolename = '企业管理' where r.rolename ='院管'; 
update dcm_role r set r.rolename = '企业职员' where r.rolename ='院员'; 
update dcm_role r set r.rolename = '部门管理' where r.rolename ='所管'; 
update dcm_role r set r.rolename = '部门职员' where r.rolename ='所员'; 
update dcm_role r set r.rolename = '部门实习生' where r.rolename ='所级实习生'; 
commit;

update dcm_role r set r.roletype = -1 where r.rolename = '高级专员';
update dcm_role r set r.roletype = -1 where r.rolename = '高级总工';
update dcm_role r set r.roletype = -1 where r.rolename = '所领导';
update dcm_role r set r.roletype = -1 where r.rolename = '项管专员';
update dcm_role r set r.roletype = -1 where r.rolename = '院领导';
update dcm_role r set r.roletype = -1 where r.rolename = '专业总工';
update dcm_role r set r.roletype = -1 where r.rolename = '总工';
commit;
