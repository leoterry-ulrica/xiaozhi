<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>上传图片</title>
</head>
<body>
<form action="rest/sysservice/user/avatar.set" method="post" enctype="multipart/form-data">
<input type="file" name="file" /><br/><br/>
用户id（序列）：<input type="text" name="userId"/><br/>
起始坐标X：<input type="text" name="originX"/><br/>
起始坐标y：<input type="text" name="originY"/><br/>
宽度：<input type="text" name="width"/><br/>
高度：<input type="text" name="height"/><br/><br/>
<input type="submit" value="Submit" />

</form>
</body>
</html>