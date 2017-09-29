--登录情况
select *
  from dcm_log t
 where instr(t.description, 'gzpi_') > 0
   and t.datetime between date '2016-10-30' and date
 '2016-11-5'
   and t.eventname = 'login';

--获取项目资料信息
select *
  from tosuser.docversion doc
 where doc.object_class_id in
       (select object_id
          from tosuser.classdefinition cd
         where cd.symbolic_name = 'XZ_FILE_ZL')
   and doc.create_date between date '2016-10-23' and date
 '2016-10-28';
 
 --获取项目
 select *
   from tosuser.container c
  where c.object_class_id in
        (select object_id
           from tosuser.classdefinition cd
          where cd.superclass_id in
                (select object_id
                   from tosuser.classdefinition cd
                  where cd.symbolic_name = 'CmAcmCaseFolder'))
    and c.create_date between date '2016-10-23' and date '2016-10-28';
    
    
 --获取微作信息
 select *
   from tosuser.docversion doc
  where doc.object_class_id in
        (select cd.object_id
           from tosuser.classdefinition cd
          where cd.symbolic_name = 'XZ_FILE_WZ')
    and doc.create_date between date '2016-10-23' and date
  '2016-10-28';
  
  
  --aws product 清华同衡------------------------------start
  --登陆日志
select count(distinct t.description)
  from xzdbserver.dcm_log t
 where instr(t.description, 'thupdi_') > 0
   and t.datetime between date '2016-10-30' and date
 '2016-11-5'
   and t.eventname = 'login';

--项目资料
select count(*)
  from qinghth_tosuser.docversion doc
 where doc.object_class_id in
       (select object_id
          from qinghth_tosuser.classdefinition cd
         where cd.symbolic_name = 'XZ_FILE_ZL')
   and doc.create_date between date '2016-10-30' and date'2016-11-5';
 
 --项目
 select count(*)
   from qinghth_tosuser.container c
  where c.object_class_id in
        (select object_id
           from qinghth_tosuser.classdefinition cd
          where cd.superclass_id in
                (select object_id
                   from qinghth_tosuser.classdefinition cd
                  where cd.symbolic_name = 'CmAcmCaseFolder'))
    and c.create_date between date '2016-10-30' and date'2016-11-5';
    
    
 --微作
 select count(*)
   from qinghth_tosuser.docversion doc
  where doc.object_class_id in
        (select cd.object_id
           from qinghth_tosuser.classdefinition cd
          where cd.symbolic_name = 'XZ_FILE_WZ')
    and doc.create_date between date '2016-10-30' and date'2016-11-5';
    
  
  --aws product 清华同衡------------------------------end
  
  --aws product 上海数慧------------------------------start
  --登录情况
select count(distinct t.description)
  from xzdbserver.dcm_log t
 where instr(t.description, 'dist_') > 0

   and t.eventname = 'login'
      and t.datetime between date '2016-10-30' and date
 '2016-11-5';

--
--获取项目资料信息
select count(*)
  from DISTPRES_TOSUSER.docversion doc
 where doc.object_class_id in
       (select object_id
          from DISTPRES_TOSUSER.classdefinition cd
         where cd.symbolic_name = 'XZ_FILE_ZL')
   and doc.create_date between date '2016-10-30' and date'2016-11-5';
 
 --获取项目
 select count(*)
   from DISTPRES_TOSUSER.container c
  where c.object_class_id in
        (select object_id
           from DISTPRES_TOSUSER.classdefinition cd
          where cd.superclass_id in
                (select object_id
                   from DISTPRES_TOSUSER.classdefinition cd
                  where cd.symbolic_name = 'CmAcmCaseFolder'))
    and c.create_date between date '2016-10-30' and date'2016-11-5';
  
  --project count
  select count(*) from XZDBSERVER.DCM_GROUP g where G.REALM = 'dist' and G.GROUPCODE like 'XZ_CASETYPE_JYXM_%';
  --team count
  select count(*) from XZDBSERVER.DCM_GROUP g where G.REALM = 'dist' and G.GROUPCODE like 'XZ_CPTDGL_%';
  
  select distinct G.GROUPCODE from XZDBSERVER.dcm_group g ;
    
   --aws product 济南-----------------------------start
 --获取微作信息
 select count(*)
   from DISTPRES_TOSUSER.docversion doc
  where doc.object_class_id in
        (select cd.object_id
           from DISTPRES_TOSUSER.classdefinition cd
          where cd.symbolic_name = 'XZ_FILE_WZ')
    and doc.create_date between date '2016-10-30' and date'2016-11-5';
    
  --task count
  select count(*) from XZDBSERVER.DCM_TASK t;  
  
  select count(distinct T.TASKID) from XZDBSERVER.DCM_TASK t;
  --aws product 上海数慧------------------------------end
  
  --项目报名人员统计
  select distinct u.loginname 登录名,
                 u.username 用户名,
                 (case u.sex
                   when 'f' then
                    '女'
                   else
                    '男'
                 end) 性别,
                 u.createtime 登记时间,
                 u.address 地址,
                 u.telephone 联系电话,
                 u.email 邮箱,
                 u.unit 单位,
                 u.position 职位,
                 (case P2U.STATUS
                   when 0 then
                    '待审核'
                   when 1 then
                    '参与'
                   when 2 then
                    '审核中'
                   else
                    '拒绝'
                 end) 状态,
                 p.syscode 项目编号,
                 p.name 项目名称
   from sga_user u
  inner join sga_prj_user p2u on U.ID = P2U.USERID
  inner join sga_project p on p2u.pid = p.id
  order by u.loginname;
  
  --aws product 济南-----------------------------start
 --登录情况
select count(distinct t.description)
  from xzdbserver.dcm_log t
 where instr(t.description, 'jnup_') > 0

   and t.eventname = 'login'
      and t.datetime between date '2017-9-14' and date
 '2017-9-15';
 --用户总数
select count(*) from xzdbserver.dcm_user u where u.realm = 'jnup' 
 and u.currentstatus= 1 and u.datecreated  between date '2017-9-14' and date'2017-9-15';
 --共享个数
 select count(*) from xzdbserver.dcm_share s where s.realm = 'jnup' and 
 s.sharedatetime between date '2017-9-14' and date'2017-9-15';
--DISTPRES_TOSUSER
--获取资料信息
select count(*)
  from jinghy_tosuser.docversion doc
 where doc.object_class_id in
       (select object_id
          from jinghy_tosuser.classdefinition cd
         where cd.symbolic_name = 'XZ_FILE_ZL')
   and doc.create_date between date '2017-9-14' and date'2017-9-15';
 
 --获取项目
 select count(*)
   from jinghy_tosuser.container c
  where c.object_class_id in
        (select object_id
           from jinghy_tosuser.classdefinition cd
          where cd.superclass_id in
                (select object_id
                   from jinghy_tosuser.classdefinition cd
                  where cd.symbolic_name = 'CmAcmCaseFolder'))
    and c.create_date between date '2017-9-14' and date'2017-9-15';
  --项目总量（个）
  select count(*) from xzdbserver.dcm_group g where g.realm = 'jnup' 
  and (g.groupcode like 'XZ_CASETYPE_HZXM_%'or g.groupcode like 'XZ_CASETYPE_JYXM_%')
  and g.createtime between date '2017-9-14' and date'2017-9-15';
   --团队总量（个）
  select count(*) from xzdbserver.dcm_group g where g.realm = 'jnup' 
  and g.groupcode like 'XZ_CPTDGL_%'
  and g.createtime between date '2017-9-14' and date'2017-9-15';
  --任务总量（个）
  select count(*) from xzdbserver.dcm_task t where t.realm ='jnup' 
  and t.datecreated between date '2017-9-14' and date'2017-9-15';
 --获取所有微作信息
 select count(*)
   from jinghy_tosuser.docversion doc
  where doc.object_class_id in
        (select cd.object_id
           from jinghy_tosuser.classdefinition cd
          where cd.symbolic_name = 'XZ_FILE_WZ')
  and doc.create_date between date '2017-9-14' and date'2017-9-15';
  --普通微作（个）= 所有微作信息（个） - -任务总量（个）
    
    
    --aws product 济南-----------------------------end