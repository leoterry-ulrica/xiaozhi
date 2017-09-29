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

</body>
</html>