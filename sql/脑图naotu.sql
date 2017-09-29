--------------------------------------------------------
--  文件已创建 - 星期三-十二月-14-2016   
--------------------------------------------------------
DROP TABLE "NAOTU_CHENNEL_NODE" cascade constraints;
DROP TABLE "NAOTU_FOLDER_NODE" cascade constraints;
DROP TABLE "NAOTU_KITYMINDER_PROJECT" cascade constraints;
DROP SEQUENCE "HIBERNATE_SEQUENCE";
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 81 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table NAOTU_CHENNEL_NODE
--------------------------------------------------------

  CREATE TABLE "NAOTU_CHENNEL_NODE" 
   (	"ID" NUMBER(19,0), 
	"CHANNEL_CODE" VARCHAR2(255 CHAR), 
	"CHANNEL_ID" NUMBER(19,0), 
	"NODE_ID" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table NAOTU_FOLDER_NODE
--------------------------------------------------------

  CREATE TABLE "NAOTU_FOLDER_NODE" 
   (	"ID" NUMBER(19,0), 
	"FOLDER_ID" VARCHAR2(255 CHAR), 
	"NODE_ID" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table NAOTU_KITYMINDER_PROJECT
--------------------------------------------------------

  CREATE TABLE "NAOTU_KITYMINDER_PROJECT" 
   (	"ID" NUMBER(19,0), 
	"CASE_ID" VARCHAR2(255 CHAR), 
	"JSON_STUCTURE" LONG
   ) ;
REM INSERTING into NAOTU_CHENNEL_NODE
SET DEFINE OFF;
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (65,'5997C0D0-C03F-4C4F-BB3F-FCD7C6FF6BA9',472643,'b98inu4c5qo8');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (64,'799D6050-060E-4657-B655-2101618F4D39',472204,'b98ip1fx2l4c');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (66,'A4D6A0F5-256E-48B5-BC19-3DA0F1C490DB',472644,'b98iojg9e08o');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (16,'A8966098-7096-4A79-B7D1-471C4E536071',424108,'b93gfw9u81cs');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (18,'409E9349-6C96-44BD-9FD0-6634D0C4D40C',424109,'b93gg1ta20w0');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (20,'26A3C116-FE92-4AA5-8A46-1F337327071F',424110,'b93glxfmk48c');
Insert into NAOTU_CHENNEL_NODE (ID,CHANNEL_CODE,CHANNEL_ID,NODE_ID) values (23,'F690E5DE-D742-4AE2-85BF-C53CFD94FA56',424111,'b93gx3454yok');
REM INSERTING into NAOTU_FOLDER_NODE
SET DEFINE OFF;
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (9,'{406BD258-0000-C01C-8998-BD3EA61BE2B4}','{406BD258-0000-C01C-8998-BD3EA61BE2B4}');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (10,'{602ED758-0000-CB1B-9B0A-D95C0B5E9E55}','b93anef8nx4c');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (11,'{202FD758-0000-CA1D-A31E-9A5717C4C933}','b93ao03t5k0g');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (12,'{402FD758-0000-C818-B116-266B2BCE10CD}','b93ao3yzw4ws');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (29,'{707DD858-0000-C113-A035-7B4FD5EF5260}','b93ifm1va20o');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (61,'{B0CBF658-0000-CB10-9508-A9473EDDF206}','b98inu4c5qo8');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (62,'{90CCF658-0000-C615-BA52-10D968209EB0}','b98iojg9e08o');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (42,'脑图测试','脑图测试');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (63,'{20CDF658-0000-C317-AF87-029A4B6BB7FE}','b98ip1fx2l4c');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (17,'{E027D858-0000-CA18-A0C2-33AA1B63A9ED}','b93gg1ta20w0');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (19,'{E02ED858-0000-C319-B80D-18BA592D3730}','b93glxfmk48c');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (21,'{003CD858-0000-C318-B1DE-850FF98145C8}','b93gwndnuds8');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (22,'{403CD858-0000-C314-81B9-75AF3FA37386}','b93gx3454yok');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (28,'{A03DD858-0000-C618-8878-C9222A121C02}','b93gxs8bvhko');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (2,'null','null');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (4,'{70A51D58-0000-CD1C-B2CD-39592E44EF69}','{70A51D58-0000-CD1C-B2CD-39592E44EF69}');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (6,'{F03EB858-0000-C615-BF7D-09ECB439A5A1}','{F03EB858-0000-C615-BF7D-09ECB439A5A1}');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (7,'{6094BD58-0000-C61D-A111-D47B410257B6}','b8z2e47yu884');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (14,'{8068CE58-0000-C912-96B1-256AFCE6B658}','{8068CE58-0000-C912-96B1-256AFCE6B658}');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (15,'{B027D858-0000-CB1F-8541-2783B1DD0AD6}','b93gfw9u81cs');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (24,'{A03DD858-0000-C618-8878-C9222A121C02}','b93gxs8bvhko');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (25,'{A03DD858-0000-C618-8878-C9222A121C02}','b93gxs8bvhko');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (26,'{A03DD858-0000-C618-8878-C9222A121C02}','b93gxs8bvhko');
Insert into NAOTU_FOLDER_NODE (ID,FOLDER_ID,NODE_ID) values (27,'{E041D858-0000-C713-B547-45F6ACD3BCF9}','b93h1d16h1s8');
REM INSERTING into NAOTU_KITYMINDER_PROJECT
SET DEFINE OFF;
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (8,'{406BD258-0000-C01C-8998-BD3EA61BE2B4}','{"root":{"data":{"id":"{406BD258-0000-C01C-8998-BD3EA61BE2B4}","text":"脑图测试","created":1481078821481,"expandState":"expand"},"children":[{"data":{"id":"b98inu4c5qo8","created":1481609295798,"text":"名字修改","layout_right_offset":{"x":24,"y":-33}},"children":[{"data":{"id":"b98iojg9e08o","created":1481609350940,"text":"我不叫分支主题"},"children":[]}]},{"data":{"id":"b93gfw9u81cs","created":1481095231568,"text":"测试1","expandState":"expand","layout_right_offset":{"x":29,"y":45}},"children":[{"data":{"id":"b98ip1fx2l4c","created":1481609390100,"text":"123","layout_right_offset":{"x":79,"y":6}},"children":[]}]}]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (41,'脑图测试','{"root":{"data":{"id":"脑图测试","text":"脑图测试","created":1481519836737,"expandState":"expand"},"children":[]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (1,'null','{"root":{"data":{"id":null,"text":null,"created":1480643234205,"expandState":"expand"},"children":[]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (3,'{70A51D58-0000-CD1C-B2CD-39592E44EF69}','{"root":{"data":{"id":"{70A51D58-0000-CD1C-B2CD-39592E44EF69}","text":"local dev","created":1480644143810,"expandState":"expand"},"children":[{"data":{"id":"b8z2e47yu884","created":1480649356775,"text":"项目部署"},"children":[]}]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (5,'{F03EB858-0000-C615-BF7D-09ECB439A5A1}','{"root":{"data":{"id":"{F03EB858-0000-C615-BF7D-09ECB439A5A1}","text":"乱码","created":1480645114640,"expandState":"expand"},"children":[]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
Insert into NAOTU_KITYMINDER_PROJECT (ID,CASE_ID,JSON_STUCTURE) values (13,'{8068CE58-0000-C912-96B1-256AFCE6B658}','{"root":{"data":{"id":"{8068CE58-0000-C912-96B1-256AFCE6B658}","text":"test","created":1481095015737,"expandState":"expand"},"children":[]},"template":"right","theme":"fresh-green","version":"1.4.33"}');
--------------------------------------------------------
--  Constraints for Table NAOTU_FOLDER_NODE
--------------------------------------------------------

  ALTER TABLE "NAOTU_FOLDER_NODE" ADD PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
  ALTER TABLE "NAOTU_FOLDER_NODE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NAOTU_KITYMINDER_PROJECT
--------------------------------------------------------

  ALTER TABLE "NAOTU_KITYMINDER_PROJECT" ADD PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
  ALTER TABLE "NAOTU_KITYMINDER_PROJECT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NAOTU_CHENNEL_NODE
--------------------------------------------------------

  ALTER TABLE "NAOTU_CHENNEL_NODE" ADD PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
  ALTER TABLE "NAOTU_CHENNEL_NODE" MODIFY ("ID" NOT NULL ENABLE);
