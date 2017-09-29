--����guid
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

--�����˺ű�Ǩ�Ƶ���ʽ�˺ű�
declare
 v_count number;
 v_instituteId number; --Ժid
 v_departId number;    --����id
 v_userId number;      --�û�id
 v_guid varchar2(38); --����guid����
 
begin
  for cur in (select t.company comp, t.companycode compcode, t.department dep, t.username, t.sex, t.position from dcm_user_try t) loop
  
    --�ж�Ժ�Ƿ��Ѵ���
    select count(*) into v_count from dcm_organization org where org.orgname = cur.compcode;
    
    if v_count =0 then
    
     select createguid() into v_guid from dual;
     select seq_dcm_oid.nextval into v_instituteId from dual;
     insert into dcm_organization values(v_instituteId, -1, 'Domain_Institute', cur.compcode, 0, v_guid, cur.comp, 'o='||cur.compcode||',dc=xiaozhi' );
     commit;
    
    else 
      select id into v_instituteId from dcm_organization org where org.orgname = cur.compcode;
      
    end if;
    
    --�жϲ����Ƿ��Ѵ���
    select count(*) into v_count from dcm_organization org where org.orgname = cur.dep;
    if v_count =0 then
       select createguid() into v_guid from dual;
       v_departId := seq_dcm_oid.nextval;
       insert into dcm_organization values(v_departId,v_instituteId , 'Domain_Department', cur.dep, 0, v_guid, cur.dep, 'cn='||cur.dep||',cn='||cur.compcode||',o='||cur.compcode||',dc=xiaozhi' );
       commit;
       
    else 
       select id into v_departId from dcm_organization org where org.orgname = cur.dep; 
    end if;
    
    --��Ա�ж�(��¼��=��ҵ��д_�û���)
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
     
     --�������û��������趨��Ա��������ĳ�����ż���ģ�
     select count(*) into v_count from dcm_org_user o2u where o2u.USERID = v_userid and o2u.orgid = v_departId;
     if v_count =0 then
       insert into dcm_org_user values(seq_dcm_oid.nextval, v_userid, v_departId);
       commit;
      
     end if;
    
  end loop;


end;

--��ӽ�ɫȨ��
declare
  v_count  number;
  v_domain varchar2(38);
  v_userId number;
begin
  for cur in (select t.company comp, t.department dep, t.username, t.role
                from dcm_user_try t) loop
  
    --��ȡ�û�id
    select count(*)
      into v_count
      from dcm_user u
     where u.username = cur.username;
    if v_count > 0 then
      --��ȡ�û�id
      select id
        into v_userId
        from dcm_user u
       where u.username = cur.username;
      --������Ժ
      select count(*)
        into v_count
        from dcm_organization t
       where t.alias = cur.dep
         and rownum < 2;
      if v_count > 0 then
      
        --����
        if cur.role = '����' then
          select count(*)
            into v_count
            from dcm_userdomainrole udr
           where udr.userid = v_userId
             and udr.domaincode = v_domain
             and udr.rolecode = 'R_Department_Leader';
          if v_count = 0 then
            --��ȡ�ռ���
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
          --Ժ��
        else
          if cur.role = 'Ժ��' then
            select count(*)
              into v_count
              from dcm_userdomainrole udr
             where udr.userid = v_userId 
               and udr.domaincode = v_domain
               and udr.rolecode = 'R_Institute_Leader';
            if v_count = 0 then
              --��ȡ�ռ���
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

--�ֹ�����


select *  from dcm_user u where u.username = '�ˬ';
-----------Ժ��
  insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142447,
                 '4025C686-972C-467E-B895-72777C0FFA0A',
                 'Domain_Institute',
                 'R_Institute_Leader',
                 sysdate);
   commit;
   -----------����
   select id, org.orgcode from dcm_organization org where org.orgname='��ϸ�滮�о�����';
     insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 143174,
                 'CBF99B81-2E0F-44AE-9716-253891831FE6',
                 'Domain_Department',
                 'R_Department_Leader',
                 sysdate);
   commit;
   
   
   select id  from dcm_user u where u.username = '�����';
-----------Ժ��
  insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142449,
                 '4025C686-972C-467E-B895-72777C0FFA0A',
                 'Domain_Institute',
                 'R_Institute_Leader',
                 sysdate);
   commit;
   
    -----------����
   select id, org.orgcode from dcm_organization org where org.orgname='��ϸ�滮�о�����';
     insert into dcm_userdomainrole
              values
                (seq_dcm_oid.nextval,
                 142449,
                 'CBF99B81-2E0F-44AE-9716-253891831FE6',
                 'Domain_Department',
                 'R_Department_Leader',
                 sysdate);
   commit;
   
   
   
