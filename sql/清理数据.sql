----��������

--�û���������
truncate table dcm_download;
--��Ŀ�顢����������
truncate table dcm_group;
--������������
truncate table dcm_personalmaterial;
--��������
truncate table dcm_share;
--�罻������
truncate table dcm_socialresource;
--�û�-�ռ���-��ɫ
delete from dcm_userdomainrole udr where udr.domaintype = 'Domain_Project';
--������Ϣ
truncate table dcm_attachment;
--����
truncate table dcm_task;
--�����븽��
truncate table dcm_task_material;
--�û�ѧ���ɹ�
truncate table DCM_USER_ARTICLEINFO;
--�û�ִҵ�ʸ�
truncate table DCM_USER_CERTIFICATEINFO;
--�û���������
truncate table DCM_USER_EDUCATION;
--�û�����ˮƽ
truncate table DCM_USER_LANGUAGE;
--�û�������Ϣ
truncate table DCM_USER_OTHERINFO;
--�û���Ŀ����
truncate table dcm_user_prjexperience;
--�û�ְ����Ϣ
truncate table DCM_USER_TITLEINFO;
--�û���ѵ����
truncate table DCM_USER_TRAINING;
--�û���������
truncate table DCM_USER_WORKEXPERIENCE;
--����Ƶ������
delete from dcm_dic_channel t where t.isbuildin = 0;
--������־
truncate table dcm_log;
--��������ֵ�
truncate table dcm_dic_orgext;

commit;


--�����ǩ����
delete from xz_tags t where instr(t.usercode,'system') = 0 ;
commit;
delete from xz_user_tags;
commit;
delete from xz_file_tags;
commit;
delete from xz_file_untags;
commit;
delete from xz_resourcecatalog_file_temp;
commit;
delete from xz_resourcecatalog_file;
commit;

--�����ĵ��ղء�����ͳ������
--delete from QINGHTH_TOSUSER.UT_DISTSUMMARYDATA t;

--������ͼ����
truncate table naotu_chennel_node;
truncate table naotu_folder_node;
truncate table naotu_kityminder_project;

--������ҳ����
truncate table sga.SGA_COM_DETAIL;
truncate table sga.SGA_PRJ_DETAIL;
truncate table sga.SGA_COM_USER;
truncate table sga.SGA_PRJ_USER;
truncate table sga.SGA_USER_ATTACHMENT;
truncate table sga.SGA_COM_STATISTICS;
truncate table sga.SGA_FILERECORD;
truncate table sga.SGA_PRJ_WZ;
truncate table sga.SGA_INVITEQUEUE;
truncate table sga.SGA_USER;
truncate table sga.SGA_PROJECT;
truncate table sga.SGA_COMINFO;

