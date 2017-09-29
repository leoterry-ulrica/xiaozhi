<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>上传文件</title>
</head>
<body>
测试上传任意文件入口：
<form action="rest/sysservice/uploadFile" method="post" enctype="multipart/form-data">
 <input type="file" name="file" />
 <input type="submit" value="Submit" />

</form>
<br>
<br>
测试上传个人文件入口：
<form action="rest/sysservice/uploadFile/person" method="post" enctype="multipart/form-data">
个人账号：<input type="text" name="userId"/>
<br>
 <input type="file" name="file" />
 <br>
  <br>
 <input type="submit" value="Submit" />

</form>
<br>
<br>
测试从外部上传文件到项目包
<form action="rest/sysservice/uploadFile/project/external" method="post" enctype="multipart/form-data">
属性<input type="text" name="properties"/>
<br>
 <input type="file" name="file" />
 <br>
 <br>
 <input type="submit" value="Submit" />

</form>
<br>
<br>
测试个人简历上传
<form action="rest/sysservice/uploadFile/resume" method="post" enctype="multipart/form-data">
JSON属性，{userCode:"108781A6-6F9C-4582-ADFC-D91FA7173FD3"}
<br>
<input type="text" name="properties"/>
<br>
 <input type="file" name="file" />
 <br>
 <br>
 <input type="submit" value="Submit" />
</form>

<br>
<br>
资讯上传
<form action="rest/sysservice/uploadFile/public" method="post" enctype="multipart/form-data">
JSON属性：{"realm":"dist", "userId":"ceadmin", "propertiesEx":{"fileType":"material | news","resourceType":"Res_Pck_Institute","domain":"85647EEF-00F5-44C9-A372-E2D0B98659D"}}
<br>
<textarea name="properties" ></textarea>
<!-- <input type="text" name="properties" value=""/> -->
<br>
 <input type="file" name="file" />
 <br>
 <br>
 <input type="submit" value="Submit" />
</form>

</body>
</html>