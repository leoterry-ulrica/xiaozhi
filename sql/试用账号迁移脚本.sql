--创建guid
/*create function CreateGUID return varchar2 
is 
guid varchar(64); 
begin 
guid := SYS_GUID(); 
return substr(guid,1,8)||'-'||substr(guid,9,4)|| 
'-'||substr(guid,13,4)||'-'||substr(guid,17,4) 
||'-'||substr(guid,21,12); 
end CreateGUID; 
*/

/*create table dcm_user_bak 
as
select * from dcm_user;*/

/*create table dcm_organization_bak 
as
select * from dcm_organization;*/
/*create table dcm_org_user_bak
as
select * from dcm_org_user;*/

/*truncate table dcm_user;
insert into dcm_user
select * from dcm_user_bak;

truncate table dcm_organization;
insert into dcm_organization
select * from dcm_organization_bak;

truncate table dcm_org_user;
insert into dcm_org_user
select * from dcm_org_user_bak;*/

--试用账号表迁移到正式账号表
declare
 v_count number;
 v_instituteId number; --院id
 v_departId number;    --部门id
 v_userId number;      --用户id
 v_guid varchar2(38); --机构guid编码
 
begin
  for cur in (select t.company comp, t.companycode compcode, t.department dep, t.username, t.sex, t.position from dcm_user_try t) loop
  
    --判断院是否已存在
    select count(*) into v_count from dcm_organization org where org.orgname = cur.compcode;
    
    if v_count =0 then
    
     select createguid() into v_guid from dual;
     select seq_dcm_oid.nextval into v_instituteId from dual;
     insert into dcm_organization values(v_instituteId, -1, 'Domain_Institute', cur.compcode, 0, v_guid, cur.comp, 'o='||cur.compcode||',dc=xiaozhi' );
     commit;
    
    else 
      select id into v_instituteId from dcm_organization org where org.orgname = cur.compcode;
      
    end if;
    
    --判断部门是否已存在
    select count(*) into v_count from dcm_organization org where org.orgname = cur.dep;
    if v_count =0 then
       select createguid() into v_guid from dual;
       v_departId := seq_dcm_oid.nextval;
       insert into dcm_organization values(v_departId,v_instituteId , 'Domain_Department', cur.dep, 0, v_guid, cur.dep, 'cn='||cur.dep||',cn='||cur.compcode||',o='||cur.compcode||',dc=xiaozhi' );
       commit;
       
    else 
       select id into v_departId from dcm_organization org where org.orgname = cur.dep; 
    end if;
    
    --人员判断(登录名=企业缩写_用户名)
     select count(*) into v_count from dcm_user u where u.loginname = cur.compcode||'_'||cur.USERNAME;
     if v_count =0 then
       select createguid() into v_guid from dual;
       v_userid := seq_dcm_oid.nextval;
       insert into dcm_user(id,username,loginname,currentstatus,datecreated,datelastactivity,domaintype,sex,isbuildin,position,usercode, REALM, userpwd) 
       values(v_userid,cur.username,cur.compcode||'_'||cur.username,1,sysdate,sysdate,'Domain_Person',cur.sex,0, cur.position, v_guid, cur.compcode, 'pass123');
       commit;
     else 
       select id, usercode into v_userId, v_guid from dcm_user u where u.loginname = cur.compcode||'_'||cur.USERNAME;
       if v_guid is null or v_guid = '' then
         select createguid() into v_guid from dual;
         update dcm_user u set u.position = cur.position, u.usercode = v_guid, u.realm = cur.compcode where u.loginname = cur.compcode||'_'||cur.USERNAME;
       end if;
       --
       commit;
     end if;
     
     --机构和用户关联（设定人员都是属于某个部门级别的）
     select count(*) into v_count from dcm_org_user o2u where o2u.USERID = v_userid and o2u.orgid = v_departId;
     if v_count =0 then
       insert into dcm_org_user values(seq_dcm_oid.nextval, v_userid, v_departId);
       commit;
      
     end if;
    
  end loop;


end;

--添加角色权限
declare
  v_count  number;
  v_domain varchar2(38);
  v_userId number;
begin
  for cur in (select t.company comp, t.department dep, t.username, t.role
                from dcm_user_try t) loop
  
    --获取用户id
    select count(*)
      into v_count
      from dcm_user u
     where u.username = cur.username;
    if v_count > 0 then
      --获取用户id
      select id
        into v_userId
        from dcm_user u
       where u.username = cur.username;
      --所或者院
      select count(*)
        into v_count
        from dcm_organization t
       where t.alias = cur.dep
         and rownum < 2;
      if v_count > 0 then
      
        --所长
        if cur.role = '所长' then
          select count(*)
            into v_count
            from dcm_userdomainrole udr
           where udr.userid = v_userId
             and udr.domaincode = v_domain
             and udr.rolecode = 'R_Department_Leader';
          if v_count = 0 then
            --获取空间域
            select t.orgcode
              into v_domain
              from dcm_organization t
             where t.alias = cur.dep
               and rownum < 2;
            insert into dcm_userdomainrole
            values
              (seq_dcm_oid.nextval,
               v_userId,
               v_domain,
               'Domain_Department',
               'R_Department_Leader',
               sysdate);
            commit;
          end if;
          --院长
        else
          if cur.role = '院长' then
            select count(*)
              into v_count
              from dcm_userdomainrole udr
             where udr.userid = v_userId 
               and udr.domaincode = v_domain
               and udr.rolecode = 'R_Institute_Leader';
            if v_count = 0 then
              --获取空间域
              select t.orgcode
                into v_domain
                from dcm_organization t
               where t.alias = cur.dep
                 and rownum < 2;
              insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 v_userId,
                 v_domain,
                 'Domain_Institute',
                 'R_Institute_Leader',
                 sysdate);
              commit;
            end if;
          end if;
        
        end if;
      end if;
    end if;
  end loop;

end;

--手工输入


select *  from dcm_user u where u.username = '恽爽';
-----------院长
  insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142447,
                 '4025C686-972C-467E-B895-72777C0FFA0A',
                 'Domain_Institute',
                 'R_Institute_Leader',
                 sysdate);
   commit;
   -----------所长
   select id, org.orgcode from dcm_organization org where org.orgname='详细规划研究中心';
     insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 143174,
                 'CBF99B81-2E0F-44AE-9716-253891831FE6',
                 'Domain_Department',
                 'R_Department_Leader',
                 sysdate);
   commit;
   
   
   select id  from dcm_user u where u.username = '曹宇钧';
-----------院长
  insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142449,
                 '4025C686-972C-467E-B895-72777C0FFA0A',
                 'Domain_Institute',
                 'R_Institute_Leader',
                 sysdate);
   commit;
   
    -----------所长
   select id, org.orgcode from dcm_organization org where org.orgname='详细规划研究中心';
     insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142449,
                 'CBF99B81-2E0F-44AE-9716-253891831FE6',
                 'Domain_Department',
                 'R_Department_Leader',
                 sysdate);
   commit;
   
   
   
