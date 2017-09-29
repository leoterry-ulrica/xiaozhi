<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>断点续传文件demo</title>
</head>
<body>
    <form id="fileForm" action="rest/sysservice/file.upload.breakpoint" enctype="multipart/form-data" method="post">
         <br/>
         输入文件md5：<input type="text" name="fileId"/><br/>
         是否已完成（true/false）：<input type="text" name="filecompleted"/><br/>
         
        上传文件:<input type="file" name="file"/><br/>
        <input type="submit" value="提交">
    </form> 
</body>
</html>