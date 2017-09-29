----清理数据

--用户下载数据
truncate table dcm_download;
--项目组、讨论组数据
truncate table dcm_group;
--个人资料数据
truncate table dcm_personalmaterial;
--分享数据
truncate table dcm_share;
--社交化数据
truncate table dcm_socialresource;
--用户-空间域-角色
delete from dcm_userdomainrole udr where udr.domaintype = 'Domain_Project';
--附件信息
truncate table dcm_attachment;
--任务
truncate table dcm_task;
--任务与附件
truncate table dcm_task_material;
--用户学术成果
truncate table DCM_USER_ARTICLEINFO;
--用户执业资格
truncate table DCM_USER_CERTIFICATEINFO;
--用户教育经历
truncate table DCM_USER_EDUCATION;
--用户语言水平
truncate table DCM_USER_LANGUAGE;
--用户其它信息
truncate table DCM_USER_OTHERINFO;
--用户项目经历
truncate table dcm_user_prjexperience;
--用户职称信息
truncate table DCM_USER_TITLEINFO;
--用户培训经历
truncate table DCM_USER_TRAINING;
--用户工作经历
truncate table DCM_USER_WORKEXPERIENCE;
--清理频道数据
delete from dcm_dic_channel t where t.isbuildin = 0;
--清理日志
truncate table dcm_log;
--清理机构字典
truncate table dcm_dic_orgext;

commit;


--清理标签数据
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

--清理文档收藏、下载统计数据
--delete from QINGHTH_TOSUSER.UT_DISTSUMMARYDATA t;

--清理脑图数据
truncate table naotu_chennel_node;
truncate table naotu_folder_node;
truncate table naotu_kityminder_project;

--清理公共页数据
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

