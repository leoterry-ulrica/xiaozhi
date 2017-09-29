<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>在线预览office文档</title>
</head>
<body>

<form id="preview"  action="wopi/uploadOffice" method="post" enctype="multipart/form-data">
请上传一份office文档：<br>
<input type="file" name="file"/>

<br>
<br>
<input type="submit" value="预览" />

</form>
<!-- 
<script type="text/javascript">

  function preView() {
	  preview.submit();
  }
  
</script> -->

</body>
</html>