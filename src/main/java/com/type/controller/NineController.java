package com.type.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.type.service.NineService;

/**
 * 9*9方格计算
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/nine")
public class NineController {
	
	@Resource
	NineService nineService;
	
	@RequestMapping(value = "gotonine")
	public String goToNinePage() {
		return "nine/ninePage";
	}
	
	@RequestMapping(value="countnine", method = RequestMethod.POST , produces = "application/json;")
	@ResponseBody
    public JSON countshu (Model model , @RequestParam("longnumber")String longnumber) throws Exception {
		JSONObject json = new JSONObject();
    	int[][] numberArray = nineService.fromStringToArray(longnumber);
        //重复性检测
        boolean nultiCheck = nineService.checkMultiShu(numberArray);
        if (!nultiCheck) {//有重复数字
        	json.put("state", false);
        	json.put("msg", "行或列中有重复数字");
            return json;
        }
        int[][] perfectArray = nineService.getNine(numberArray);
        json.put("data", perfectArray);
        if (perfectArray == null) {
        	json.put("state", false);
        	json.put("msg", "计算失败");
        } else {
        	json.put("state", true);
        	json.put("msg", "计算成功");
        }
        return json;
    }
}
