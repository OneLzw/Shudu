package com.type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.type.utils.HttpClientHelper;



public class HttpTest {
	
	@Test
	public void phoneTest () {
		String filePath = "C://Users//Administrator//Desktop//txt//phone.txt";
		File file = new File(filePath);
		List<String> phoneList = null;
		try {
			phoneList = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String pathUrl = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location";
		Map<String , Object> params = new HashMap<>();
		String url = "";
		String phone = null;
		String result = null;
		String target = null;
		List<String> targetList = new ArrayList<>();
		for (int i = 0 ; i < phoneList.size() ; i++) {
			phone = phoneList.get(i);
			url = pathUrl + "?tel=" + phone;
			result = HttpClientHelper.sendGet2(url,params,"utf-8");
			System.out.println(phone);
			target = getNumberJSON(result , phone);
			targetList.add(target);
		}
		try {
			FileUtils.writeLines(new File("C://Users//Administrator//Desktop//txt//phone1.txt"),targetList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public String getNumberJSON (String jsonStr , String phone) {
		String pc = null;
		if (jsonStr == null) {
			return pc;
		}
		JSONObject json = JSON.parseObject(jsonStr);
		if (json == null) {
			return pc;
		}
		JSONObject response = json.getJSONObject("response");
		if (response == null) {
			return pc;
		}
		JSONObject phoneJson = response.getJSONObject(phone);
		if (phoneJson == null) {
			return pc;
		}
		JSONObject detail = phoneJson.getJSONObject("detail");
		if (detail == null) {
			return pc;
		}
		String province = detail.getString("province");
		String operator = detail.getString("operator");
		String city = null;
		JSONArray area = detail.getJSONArray("area");
		if (area != null) {
			JSONObject are = area.getJSONObject(0);
			city = are.getString("city");
		}
		if (city == null) {
			pc = phone + "," + operator + "," + province;
		} else {
			pc = phone + "," + operator + "," + province + "," + city;
		}
		return pc;
	}
}
