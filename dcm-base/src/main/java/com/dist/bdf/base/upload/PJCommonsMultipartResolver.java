package com.dist.bdf.base.upload;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 重写CommonsMultipartResolver以监听文件上传进度
 * @author Van
 * @date 2012-12-12
 */
public class PJCommonsMultipartResolver extends CommonsMultipartResolver {

    private HttpServletRequest request;

/*    public PJCommonsMultipartResolver(ServletContext servletContext) {
    	super(servletContext);
    }*/
    protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
        ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
        upload.setSizeMax(-1);
        if (request != null) {
            HttpSession session = request.getSession();
            PJProgressListener uploadProgressListener = new PJProgressListener(session);
            upload.setProgressListener(uploadProgressListener);
        }
        return upload;
    }

    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
        this.request = request;// 获取到request,要用到session
        return super.resolveMultipart(request);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {

        HttpSession session = request.getSession();
		String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);

        PJProgressListener uploadProgressListener = new PJProgressListener(session);
        fileUpload.setProgressListener(uploadProgressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

}
