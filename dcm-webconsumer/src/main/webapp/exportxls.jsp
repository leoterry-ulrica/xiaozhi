<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>导出excel文件</title>
</head>
<body>
测试导出excel文件：
<form action="rest/sysservice/v1/task/summaryexp/excel2" method="post" enctype="application/json">
参数输入：<input type="text" name="data"/>
<br>
 <br>
 <input type="submit" value="Submit" />
</form>
</body>
</html>