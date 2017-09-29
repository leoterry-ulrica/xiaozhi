
package com.dist.bdf.base.constants;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.base.utils.StringUtil;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

/**
 * 通过读取mimetype.properties文件获取
 * @author weifj
 * @version 1.0，2016/03/19，weifj，创建mime类型
 */
public class MimeType {
	
	private static Logger logger = LoggerFactory.getLogger(MimeType.class);
	
	/*private static Map<String, String> MimeMap = new HashMap<String, String>(){

		*//**
		 * 
		 *//*
		private static final long serialVersionUID = 1L;
		{
            put(".323" ,"text/h323" );
            put(".aaf" ,"application/octet-stream" );
            put(".aca" ,"application/octet-stream" );
            put(".accdb" ,"application/msaccess" );
            put(".accde" ,"application/msaccess" );
            put(".accdt" ,"application/msaccess" );
            put(".acx" ,"application/internet-property-stream" );
            put(".afm" ,"application/octet-stream" );
            put(".ai" ,"application/postscript" );
            put(".aif" ,"audio/x-aiff" );
            put(".aifc" ,"audio/aiff" );
            put(".aiff" ,"audio/aiff" );
            put(".application" ,"application/x-ms-application" );
            put(".art" ,"image/x-jg" );
            put(".asd" ,"application/octet-stream" );
            put(".asf" ,"video/x-ms-asf" );
            put(".asi" ,"application/octet-stream" );
            put(".asm" ,"text/plain" );
            put(".asr" ,"video/x-ms-asf" );
            put(".asx" ,"video/x-ms-asf" );
            put(".atom" ,"application/atom+xml" );
            put(".au" ,"audio/basic" );
            put(".avi" ,"video/x-msvideo" );
            put(".axs" ,"application/olescript" );
            put(".bas" ,"text/plain" );
            put(".bcpio" ,"application/x-bcpio" );
            put(".bin" ,"application/octet-stream" );
            put(".bmp" ,"image/bmp" );
            put(".c" ,"text/plain" );
            put(".cab" ,"application/octet-stream" );
            put(".calx" ,"application/vnd.ms-office.calx" );
            put(".cat" ,"application/vnd.ms-pki.seccat" );
            put(".cdf" ,"application/x-cdf" );
            put(".chm" ,"application/octet-stream" );
            put(".class" ,"application/x-java-applet" );
            put(".clp" ,"application/x-msclip" );
            put(".cmx" ,"image/x-cmx" );
            put(".cnf" ,"text/plain" );
            put(".cod" ,"image/cis-cod" );
            put(".cpio" ,"application/x-cpio" );
            put(".cpp" ,"text/plain" );
            put(".crd" ,"application/x-mscardfile" );
            put(".crl" ,"application/pkix-crl" );
            put(".crt" ,"application/x-x509-ca-cert" );
            put(".csh" ,"application/x-csh" );
            put(".css" ,"text/css" );
            put(".csv" ,"application/octet-stream" );
            put(".cur" ,"application/octet-stream" );
            put(".dcr" ,"application/x-director" );
            put(".deploy" ,"application/octet-stream" );
            put(".der" ,"application/x-x509-ca-cert" );
            put(".dib" ,"image/bmp" );
            put(".dir" ,"application/x-director" );
            put(".disco" ,"text/xml" );
            put(".dll" ,"application/x-msdownload" );
            put(".dll.config" ,"text/xml" );
            put(".dlm" ,"text/dlm" );
            put(".doc" ,"application/msword" );
            put(".docm" ,"application/vnd.ms-word.document.macroEnabled.12" );
            put(".docx" ,"application/vnd.openxmlformats-officedocument.wordprocessingml.document" );
            put(".dot" ,"application/msword" );
            put(".dotm" ,"application/vnd.ms-word.template.macroEnabled.12" );
            put(".dotx" ,"application/vnd.openxmlformats-officedocument.wordprocessingml.template" );
            put(".dsp" ,"application/octet-stream" );
            put(".dtd" ,"text/xml" );
            put(".dvi" ,"application/x-dvi" );
            put(".dwf" ,"drawing/x-dwf" );
            put(".dwp" ,"application/octet-stream" );
            put(".dxr" ,"application/x-director" );
            put(".eml" ,"message/rfc822" );
            put(".emz" ,"application/octet-stream" );
            put(".eot" ,"application/octet-stream" );
            put(".eps" ,"application/postscript" );
            put(".etx" ,"text/x-setext" );
            put(".evy" ,"application/envoy" );
            put(".exe" ,"application/octet-stream" );
            put(".exe.config" ,"text/xml" );
            put(".fdf" ,"application/vnd.fdf" );
            put(".fif" ,"application/fractals" );
            put(".fla" ,"application/octet-stream" );
            put(".flr" ,"x-world/x-vrml" );
            put(".flv" ,"video/x-flv" );
            put(".gif" ,"image/gif" );
            put(".gtar" ,"application/x-gtar" );
            put(".gz" ,"application/x-gzip" );
            put(".h" ,"text/plain" );
            put(".hdf" ,"application/x-hdf" );
            put(".hdml" ,"text/x-hdml" );
            put(".hhc" ,"application/x-oleobject" );
            put(".hhk" ,"application/octet-stream" );
            put(".hhp" ,"application/octet-stream" );
            put(".hlp" ,"application/winhlp" );
            put(".hqx" ,"application/mac-binhex40" );
            put(".hta" ,"application/hta" );
            put(".htc" ,"text/x-component" );
            put(".htm" ,"text/html" );
            put(".html" ,"text/html" );
            put(".htt" ,"text/webviewhtml" );
            put(".hxt" ,"text/html" );
            put(".ico" ,"image/x-icon" );
            put(".ics" ,"application/octet-stream" );
            put(".ief" ,"image/ief" );
            put(".iii" ,"application/x-iphone" );
            put(".inf" ,"application/octet-stream" );
            put(".ins" ,"application/x-internet-signup" );
            put(".isp" ,"application/x-internet-signup" );
            put(".IVF" ,"video/x-ivf" );
            put(".jar" ,"application/java-archive" );
            put(".java" ,"application/octet-stream" );
            put(".jck" ,"application/liquidmotion" );
            put(".jcz" ,"application/liquidmotion" );
            put(".jfif" ,"image/pjpeg" );
            put(".jpb" ,"application/octet-stream" );
            put(".jpe" ,"image/jpeg" );
            put(".jpeg" ,"image/jpeg" );
            put(".jpg" ,"image/jpeg" );
            put(".js" ,"application/x-javascript" );
            put(".jsx" ,"text/jscript" );
            put(".latex" ,"application/x-latex" );
            put(".lit" ,"application/x-ms-reader" );
            put(".lpk" ,"application/octet-stream" );
            put(".lsf" ,"video/x-la-asf" );
            put(".lsx" ,"video/x-la-asf" );
            put(".lzh" ,"application/octet-stream" );
            put(".m13" ,"application/x-msmediaview" );
            put(".m14" ,"application/x-msmediaview" );
            put(".m1v" ,"video/mpeg" );
            put(".m3u" ,"audio/x-mpegurl" );
            put(".man" ,"application/x-troff-man" );
            put(".manifest" ,"application/x-ms-manifest" );
            put(".map" ,"text/plain" );
            put(".mdb" ,"application/x-msaccess" );
            put(".mdp" ,"application/octet-stream" );
            put(".me" ,"application/x-troff-me" );
            put(".mht" ,"message/rfc822" );
            put(".mhtml" ,"message/rfc822" );
            put(".mid" ,"audio/mid" );
            put(".midi" ,"audio/mid" );
            put(".mix" ,"application/octet-stream" );
            put(".mmf" ,"application/x-smaf" );
            put(".mno" ,"text/xml" );
            put(".mny" ,"application/x-msmoney" );
            put(".mov" ,"video/quicktime" );
            put(".movie" ,"video/x-sgi-movie" );
            put(".mp2" ,"video/mpeg" );
            put(".mp3" ,"audio/mpeg" );
            put(".mpa" ,"video/mpeg" );
            put(".mpe" ,"video/mpeg" );
            put(".mpeg" ,"video/mpeg" );
            put(".mpg" ,"video/mpeg" );
            put(".mpp" ,"application/vnd.ms-project" );
            put(".mpv2" ,"video/mpeg" );
            put(".ms" ,"application/x-troff-ms" );
            put(".msi" ,"application/octet-stream" );
            put(".mso" ,"application/octet-stream" );
            put(".mvb" ,"application/x-msmediaview" );
            put(".mvc" ,"application/x-miva-compiled" );
            put(".nc" ,"application/x-netcdf" );
            put(".nsc" ,"video/x-ms-asf" );
            put(".nws" ,"message/rfc822" );
            put(".ocx" ,"application/octet-stream" );
            put(".oda" ,"application/oda" );
            put(".odc" ,"text/x-ms-odc" );
            put(".ods" ,"application/oleobject" );
            put(".one" ,"application/onenote" );
            put(".onea" ,"application/onenote" );
            put(".onetoc" ,"application/onenote" );
            put(".onetoc2" ,"application/onenote" );
            put(".onetmp" ,"application/onenote" );
            put(".onepkg" ,"application/onenote" );
            put(".osdx" ,"application/opensearchdescription+xml" );
            put(".p10" ,"application/pkcs10" );
            put(".p12" ,"application/x-pkcs12" );
            put(".p7b" ,"application/x-pkcs7-certificates" );
            put(".p7c" ,"application/pkcs7-mime" );
            put(".p7m" ,"application/pkcs7-mime" );
            put(".p7r" ,"application/x-pkcs7-certreqresp" );
            put(".p7s" ,"application/pkcs7-signature" );
            put(".pbm" ,"image/x-portable-bitmap" );
            put(".pcx" ,"application/octet-stream" );
            put(".pcz" ,"application/octet-stream" );
            put(".pdf" ,"application/pdf" );
            put(".pfb" ,"application/octet-stream" );
            put(".pfm" ,"application/octet-stream" );
            put(".pfx" ,"application/x-pkcs12" );
            put(".pgm" ,"image/x-portable-graymap" );
            put(".pko" ,"application/vnd.ms-pki.pko" );
            put(".pma" ,"application/x-perfmon" );
            put(".pmc" ,"application/x-perfmon" );
            put(".pml" ,"application/x-perfmon" );
            put(".pmr" ,"application/x-perfmon" );
            put(".pmw" ,"application/x-perfmon" );
            put(".png" ,"image/png" );
            put(".pnm" ,"image/x-portable-anymap" );
            put(".pnz" ,"image/png" );
            put(".pot" ,"application/vnd.ms-powerpoint" );
            put(".potm" ,"application/vnd.ms-powerpoint.template.macroEnabled.12" );
            put(".potx" ,"application/vnd.openxmlformats-officedocument.presentationml.template" );
            put(".ppam" ,"application/vnd.ms-powerpoint.addin.macroEnabled.12" );
            put(".ppm" ,"image/x-portable-pixmap" );
            put(".pps" ,"application/vnd.ms-powerpoint" );
            put(".ppsm" ,"application/vnd.ms-powerpoint.slideshow.macroEnabled.12" );
            put(".ppsx" ,"application/vnd.openxmlformats-officedocument.presentationml.slideshow" );
            put(".ppt" ,"application/vnd.ms-powerpoint" );
            put(".pptm" ,"application/vnd.ms-powerpoint.presentation.macroEnabled.12" );
            put(".pptx" ,"application/vnd.openxmlformats-officedocument.presentationml.presentation" );
            put(".prf" ,"application/pics-rules" );
            put(".prm" ,"application/octet-stream" );
            put(".prx" ,"application/octet-stream" );
            put(".ps" ,"application/postscript" );
            put(".psd" ,"application/octet-stream" );
            put(".psm" ,"application/octet-stream" );
            put(".psp" ,"application/octet-stream" );
            put(".pub" ,"application/x-mspublisher" );
            put(".qt" ,"video/quicktime" );
            put(".qtl" ,"application/x-quicktimeplayer" );
            put(".qxd" ,"application/octet-stream" );
            put(".ra" ,"audio/x-pn-realaudio" );
            put(".ram" ,"audio/x-pn-realaudio" );
            put(".rar" ,"application/octet-stream" );
            put(".ras" ,"image/x-cmu-raster" );
            put(".rf" ,"image/vnd.rn-realflash" );
            put(".rgb" ,"image/x-rgb" );
            put(".rm" ,"application/vnd.rn-realmedia" );
            put(".rmi" ,"audio/mid" );
            put(".roff" ,"application/x-troff" );
            put(".rpm" ,"audio/x-pn-realaudio-plugin" );
            put(".rtf" ,"application/rtf" );
            put(".rtx" ,"text/richtext" );
            put(".scd" ,"application/x-msschedule" );
            put(".sct" ,"text/scriptlet" );
            put(".sea" ,"application/octet-stream" );
            put(".setpay" ,"application/set-payment-initiation" );
            put(".setreg" ,"application/set-registration-initiation" );
            put(".sgml" ,"text/sgml" );
            put(".sh" ,"application/x-sh" );
            put(".shar" ,"application/x-shar" );
            put(".sit" ,"application/x-stuffit" );
            put(".sldm" ,"application/vnd.ms-powerpoint.slide.macroEnabled.12" );
            put(".sldx" ,"application/vnd.openxmlformats-officedocument.presentationml.slide" );
            put(".smd" ,"audio/x-smd" );
            put(".smi" ,"application/octet-stream" );
            put(".smx" ,"audio/x-smd" );
            put(".smz" ,"audio/x-smd" );
            put(".snd" ,"audio/basic" );
            put(".snp" ,"application/octet-stream" );
            put(".spc" ,"application/x-pkcs7-certificates" );
            put(".spl" ,"application/futuresplash" );
            put(".src" ,"application/x-wais-source" );
            put(".ssm" ,"application/streamingmedia" );
            put(".sst" ,"application/vnd.ms-pki.certstore" );
            put(".stl" ,"application/vnd.ms-pki.stl" );
            put(".sv4cpio" ,"application/x-sv4cpio" );
            put(".sv4crc" ,"application/x-sv4crc" );
            put(".svg" ,"image/svg+xml" );
            put(".swf" ,"application/x-shockwave-flash" );
            put(".t" ,"application/x-troff" );
            put(".tar" ,"application/x-tar" );
            put(".tcl" ,"application/x-tcl" );
            put(".tex" ,"application/x-tex" );
            put(".texi" ,"application/x-texinfo" );
            put(".texinfo" ,"application/x-texinfo" );
            put(".tgz" ,"application/x-compressed" );
            put(".thmx" ,"application/vnd.ms-officetheme" );
            put(".thn" ,"application/octet-stream" );
            put(".tif" ,"image/tiff" );
            put(".tiff" ,"image/tiff" );
            put(".toc" ,"application/octet-stream" );
            put(".tr" ,"application/x-troff" );
            put(".trm" ,"application/x-msterminal" );
            put(".tsv" ,"text/tab-separated-values" );
            put(".ttf" ,"application/octet-stream" );
            put(".txt" ,"text/plain" );
            put(".log" ,"text/plain" );
            put(".u32" ,"application/octet-stream" );
            put(".uls" ,"text/iuls" );
            put(".ustar" ,"application/x-ustar" );
            put(".vbs" ,"text/vbscript" );
            put(".vcf" ,"text/x-vcard" );
            put(".vcs" ,"text/plain" );
            put(".vdx" ,"application/vnd.ms-visio.viewer" );
            put(".vml" ,"text/xml" );
            put(".vsd" ,"application/vnd.visio" );
            put(".vss" ,"application/vnd.visio" );
            put(".vst" ,"application/vnd.visio" );
            put(".vsto" ,"application/x-ms-vsto" );
            put(".vsw" ,"application/vnd.visio" );
            put(".vsx" ,"application/vnd.visio" );
            put(".vtx" ,"application/vnd.visio" );
            put(".wav" ,"audio/wav" );
            put(".wax" ,"audio/x-ms-wax" );
            put(".wbmp" ,"image/vnd.wap.wbmp" );
            put(".wcm" ,"application/vnd.ms-works" );
            put(".wdb" ,"application/vnd.ms-works" );
            put(".wks" ,"application/vnd.ms-works" );
            put(".wm" ,"video/x-ms-wm" );
            put(".wma" ,"audio/x-ms-wma" );
            put(".wmd" ,"application/x-ms-wmd" );
            put(".wmf" ,"application/x-msmetafile" );
            put(".wml" ,"text/vnd.wap.wml" );
            put(".wmlc" ,"application/vnd.wap.wmlc" );
            put(".wmls" ,"text/vnd.wap.wmlscript" );
            put(".wmlsc" ,"application/vnd.wap.wmlscriptc" );
            put(".wmp" ,"video/x-ms-wmp" );
            put(".wmv" ,"video/x-ms-wmv" );
            put(".wmx" ,"video/x-ms-wmx" );
            put(".wmz" ,"application/x-ms-wmz" );
            put(".wps" ,"application/vnd.ms-works" );
            put(".wri" ,"application/x-mswrite" );
            put(".wrl" ,"x-world/x-vrml" );
            put(".wrz" ,"x-world/x-vrml" );
            put(".wsdl" ,"text/xml" );
            put(".wvx" ,"video/x-ms-wvx" );
            put(".x" ,"application/directx" );
            put(".xaf" ,"x-world/x-vrml" );
            put(".xaml" ,"application/xaml+xml" );
            put(".xap" ,"application/x-silverlight-app" );
            put(".xbap" ,"application/x-ms-xbap" );
            put(".xbm" ,"image/x-xbitmap" );
            put(".xdr" ,"text/plain" );
            put(".xht" ,"application/xhtml+xml" );
            put(".xhtml" ,"application/xhtml+xml" );
            put(".xla" ,"application/vnd.ms-excel" );
            put(".xlam" ,"application/vnd.ms-excel.addin.macroEnabled.12" );
            put(".xlc" ,"application/vnd.ms-excel" );
            put(".xlm" ,"application/vnd.ms-excel" );
            put(".xls" ,"application/vnd.ms-excel" );
            put(".xlsb" ,"application/vnd.ms-excel.sheet.binary.macroEnabled.12" );
            put(".xlsm" ,"application/vnd.ms-excel.sheet.macroEnabled.12" );
            put(".xlsx" ,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
            put(".xlt" ,"application/vnd.ms-excel" );
            put(".xltm" ,"application/vnd.ms-excel.template.macroEnabled.12" );
            put(".xltx" ,"application/vnd.openxmlformats-officedocument.spreadsheetml.template" );
            put(".xlw" ,"application/vnd.ms-excel" );
            put(".xml" ,"text/xml" );
            put(".xof" ,"x-world/x-vrml" );
            put(".xpm" ,"image/x-xpixmap" );
            put(".xps" ,"application/vnd.ms-xpsdocument" );
            put(".xsd" ,"text/xml" );
            put(".xsf" ,"text/xml" );
            put(".xsl" ,"text/xml" );
            put(".xslt" ,"text/xml" );
            put(".xsn" ,"application/octet-stream" );
            put(".xtp" ,"application/octet-stream" );
            put(".xwd" ,"image/x-xwindowdump" );
            put(".z" ,"application/x-compress" );
            put(".zip" ,"application/x-zip-compressed" );
            put(".dwg" ,"application/autocad" );
            put(".rmvb" ,"audio/x-pn-realaudio" );
            put(".mp2" ,"audio/x-mpeg" );
            put(".mp3" ,"audio/x-mpeg" );
            put(".mp4" ,"video/mp4" );
            put(".mpg4" ,"video/mp4" );
            put(".avi" ,"video/x-msvideo" );
             
		}
	};*/
	
	/**
	 * 文本类型
	 */
	//public static final String MIME_TEXT = "text/plain";
	/**
	 * pdf类型
	 */
	//public static final String MIME_PDF = "application/pdf";
	/**
	 * bmp
	 */
	public static final String MIME_BMP = "image/bmp";
	/**
	 * gif
	 */
	public static final String MIME_GIF = "image/gif";
	/**
	 * png
	 */
	public static final String MIME_PNG = "image/png";
	/**
	 * jpeg
	 */
	public static final String MIME_JPEG = "image/jpeg";
	/**
	 * jpg
	 */
	public static final String MIME_JPG = "image/jpg";
	/**
	 * tiff
	 */
	public static final String MIME_TIFF = "image/tiff";

	public static String getMimeType(String extension, String contentType, byte[] bytes) {

		String mimeType = "";
		if (StringUtil.isNullOrEmpty(extension)) {
			return mimeType;
		}
		if (!extension.startsWith(".")) {
			extension = ("." + extension);
		}

		mimeType = getMimeType(extension.toLowerCase());
		if (StringUtil.isNullOrEmpty(mimeType)) {
			try {
				MagicMatch match = Magic.getMagicMatch(bytes, true);
				mimeType = (null == match) ? contentType : match.getMimeType();// file.getContentType()不准确
			} catch (Exception ex) {
				ex.printStackTrace();
				mimeType = getDefaultMimeType();
			}
		}

		return mimeType;
	}
	private static Properties getMimeTypeProperties() {
		
		try{
			
		 Properties prop = new Properties();   
	     InputStream in = MimeType.class.getResourceAsStream("mimetype.properties");
	     prop.load(in);
	     
	     return prop;
	     
		}catch(Exception e){
			logger.error("读取本地properties文件失败：mimetype.properties");
			e.printStackTrace();
		}
		
		return null;
	     
	}
	/**
	 * 根据后缀获取mime type，如果都匹配不上，则返回默认值：application/octet-stream
	 * @param extension
	 * @return
	 */
	public static String getMimeType(String extension) {

		String mimeType = "";
		if (StringUtil.isNullOrEmpty(extension)) {
			return getDefaultMimeType();
		}
		if (!extension.startsWith(".")) {
			extension = ("." + extension);
		}
		Properties prop = getMimeTypeProperties();
		
		mimeType = (null == prop)? getDefaultMimeType() : prop.getProperty(extension);//MimeMap.get(extension.toLowerCase());
		
		if(StringUtil.isNullOrEmpty(mimeType)){
			mimeType = getDefaultMimeType();
		}
	
		return mimeType;
	}
	public static String getDefaultMimeType() {
		
		return "application/octet-stream";
	}
	public static void main(String[]args){
		
		System.out.println(MimeType.getMimeType(".pdf"));
	}
	
	/**
	 * 判断是否图片格式
	 * @param mimeType
	 * @return
	 */
	public static boolean isImg(String mimeType){
		
		boolean flag = false;
		switch (mimeType) {
		case MimeType.MIME_BMP:
			flag = true;
			break;
		case MimeType.MIME_GIF:
			flag = true;
			break;
		case MimeType.MIME_JPEG:
			flag = true;
			break;
		case MimeType.MIME_PNG:
			flag = true;
			break;
		case MimeType.MIME_JPG:
			flag = true;
			break;
		default:
			break;
		}
		return flag;
	}
}
