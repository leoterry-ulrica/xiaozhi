package com.dist.bdf.base.utils;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 阿里云根据经纬度获取地区名接口，往往需要调取外部服务（互联网）进行获取。
 * @author weifj
 *
 */
public class AddressUtil {

	private static Logger logger = LoggerFactory.getLogger(AddressUtil.class);
	/**
	 * 根据经纬度坐标获取地址
	 * @param log 经度  
	 * @param lat 纬度
	 * @return
	 */
	public static String getAdd(String log, String lat) {

		//参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)  
		String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line + "\n";
			}
			in.close();
		} catch (Exception e) {
			logger.error(">>>error in wapaction,and e is"+e.getMessage(), e);
		}
		logger.info(">>>address : "+res);
		return res;
	}
	/**
	 * 根据经纬度获取省
	 * @param log 经度
	 * @param lat 纬度
	 * @return
	 */
	public static String getProvince(String log, String lat){
		
		String add = getAdd(log, lat);
		JSONObject jsonObject = JSONObject.fromObject(add);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		
		return arr[0];
	}
	/**
	 * 根据经纬度获取市
	 * @param log 经度
	 * @param lat 纬度
	 * @return
	 */
	public static String getCity(String log, String lat){
		
		String add = getAdd(log, lat);
		JSONObject jsonObject = JSONObject.fromObject(add);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		
		return arr[1];
	}
	/**
	 * 根据经纬度获取区
	 * @param log 经度
	 * @param lat 纬度
	 * @return
	 */
	public static String getArea(String log, String lat){
		
		String add = getAdd(log, lat);
		JSONObject jsonObject = JSONObject.fromObject(add);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		
		return arr[2];
	}
	
	public static void main(String[] args) {
		// lat 39.97646       
		// log 116.3039   
		String add = getAdd("116.3039", "39.97646");
		JSONObject jsonObject = JSONObject.fromObject(add);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		System.out.println("省：" + arr[0] + "\n市：" + arr[1] + "\n区：" + arr[2]);
	}
}
