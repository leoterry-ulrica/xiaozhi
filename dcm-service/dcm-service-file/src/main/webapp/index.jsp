<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>上传图片</title>
</head>
<body>
<form action="rest/sysservice/uploadAvatar" method="post" enctype="multipart/form-data">
<input type="file" name="file" />
登录名：<input type="text" name="loginName"/>
<input type="submit" value="Submit" />

</form>
</body>
</html>