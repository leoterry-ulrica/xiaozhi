
declare
begin
  for cur in (select id, loginname, syscode from sga_user u) loop
    --�����ɫȨ�޿ռ�����Ϣ
    dbms_output.put_line('delete from dcm_userdomainrole udr where udr.usertype = 1 and udr.usercode ='' ' ||
                         cur.syscode||''';');
    --������Ŀ���û�����
    delete from sga_prj_user pu where pu.userid = cur.id;
    --������ҵ���û�����
    delete from sga_com_user cu where cu.userid = cur.id;
    --ɾ���û�
    --delete from sga_user u where u.id = cur.id;
    commit;
  end loop;
end;

