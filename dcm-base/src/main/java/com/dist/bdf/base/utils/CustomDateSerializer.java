package com.dist.bdf.base.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dist.bdf.base.constants.DateContants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 自定义时间格式
 * @author weifj
 * 2015-06-07
 */
public class CustomDateSerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date date, JsonGenerator jgen,SerializerProvider provider)
			throws IOException, JsonProcessingException {
		 SimpleDateFormat formatter = new SimpleDateFormat(DateContants.DATE_FORMAT);
		  String formattedDate = formatter.format(date);
		  jgen.writeString(formattedDate);
	}

}
