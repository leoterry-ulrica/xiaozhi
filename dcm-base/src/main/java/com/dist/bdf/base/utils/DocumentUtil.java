package com.dist.bdf.base.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public final class DocumentUtil {
	private static final String CodeName = "UTF-8";
	private static SimpleDateFormat DateTimeConvert = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat DateConvert = new SimpleDateFormat("yyyy-MM-dd");

	/**
	* 把Blob类型转换为byte数组类型用流的形式读取
	* @param blob
	* @return
	*/
	/**
	* 把Blob类型转换为byte数组类型
	* @param blob
	* @return
	*/
	/*public static byte[] serializableBlobToBytes(SerializableBlob serializableBlob) {

		java.sql.Blob blob = serializableBlob.getWrappedBlob();
		BufferedInputStream is = null;

		try {
			java.io.InputStream pist = blob.getBinaryStream();
			is = new BufferedInputStream(pist);
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;

			while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}*/

	/**
	 * byte转换成SerializableBlob
	 * （未测试）
	 * @param blob
	 * @param bytes
	 * @return 转换结果
	 */
	/*	public static org.hibernate.lob.SerializableBlob byteToBlob(byte[] bytes)
		{
			org.hibernate.lob.SerializableBlob blob=null;
			try
			{
				blob=(org.hibernate.lob.SerializableBlob)org.hibernate.Hibernate.createBlob(bytes);
			}
			catch(Exception ex)
			{
				blob=null;
			}
			return blob;
		}*/
	/**
	 * 字符串转换成日期
	 * @param timestr 日期字符串
	 * @return 日期
	 */
	public static Date stringToDate(String timestr) {
		try {
			return DateTimeConvert.parse(timestr);
		} catch (ParseException ex) {
			return null;
		}
	}

	/**
	 * 字符串转换成日期
	 * @param timestr 日期字符串(不包括时间)
	 * @return 日期
	 */
	public static Date stringToDate2(String timestr) {
		try {
			return DateConvert.parse(timestr);
		} catch (ParseException ex) {
			return null;
		}
	}

	/**
	 * 日期转换成字符串
	 * @param tempDate 日期
	 * @return 字符串
	 */
	public static String dateToString(Date tempDate) {
		return DateTimeConvert.format(tempDate);
	}

	/**
	 * 创建xml文档
	 * @return 返回xml文档
	 */
	public static Document createDocument() {
		try {
			Document returnDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			return returnDocument;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据传入String，获取document对象
	 * @param data
	 * @return
	 */
	/*	public static Document CreateDocument(String data)
		{
			try {
				DocumentBuilder builder =DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = builder.parse(new ByteArrayInputStream(data.getBytes()));
				return document;
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return null;
	    }
		}*/
	/** 
	 转换返回值
	 
	 @param doc 文档 
	 @return 转换后的返回值
	*/
	public static String convertXMLToString(Document doc) {
		ByteArrayOutputStream outStream = null;
		Source source = null;
		Result result = null;
		Transformer xformer = null;
		try {
			source = new DOMSource(doc);
			outStream = new ByteArrayOutputStream();
			OutputStreamWriter write = new OutputStreamWriter(outStream);
			result = new StreamResult(write);

			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, CodeName);
			xformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return outStream.toString();
	}

	/**
	 * 将字符串转换成xml文档
	 * @param data 带转换的字符串数据
	 * @return 返回xml文档
	 */
	public static Document convertStringToDocument(String data) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将字符串转换成xml文档
	 * @param data
	 * @param charsetName 字符集名称
	 * @return
	 */
	public static Document convertStringToDocument(String data, String charsetName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(data.getBytes(charsetName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 返回带utf8头的字符串
	 * @param xml xml数据字符串
	 * @return 返回字符串
	 */
	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object 
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes(CodeName));
			xmlUTF8 = URLEncoder.encode(xmString, CodeName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlUTF8;
	}

	public static Document buildDocument(String path) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//logger.debug("Construct document builder success.");
			Document doc = builder.parse(new File(path));
			return doc;
			//logger.debug("Build xml document success.");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			//logger.error("Construct document builder error:"+e);
		} catch (SAXException e) {
			// logger.error("Parse xml file error:"+e);
		} catch (IOException e) {
			//  logger.error("Read xml file error:"+e);
		}
		return null;
	}
	
	public static Document buildDocument(InputStream in) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//logger.debug("Construct document builder success.");
			Document doc = builder.parse(in);
			return doc;
			//logger.debug("Build xml document success.");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			//logger.error("Construct document builder error:"+e);
		} catch (SAXException e) {
			// logger.error("Parse xml file error:"+e);
		} catch (IOException e) {
			//  logger.error("Read xml file error:"+e);
		}
		return null;
	}
}