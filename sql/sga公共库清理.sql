
declare
begin
  for cur in (select id, loginname, syscode from sga_user u) loop
    --清理角色权限空间域信息
    dbms_output.put_line('delete from dcm_userdomainrole udr where udr.usertype = 1 and udr.usercode ='' ' ||
                         cur.syscode||''';');
    --清理项目与用户关联
    delete from sga_prj_user pu where pu.userid = cur.id;
    --清理企业与用户关联
    delete from sga_com_user cu where cu.userid = cur.id;
    --删除用户
    --delete from sga_user u where u.id = cur.id;
    commit;
  end loop;
end;

