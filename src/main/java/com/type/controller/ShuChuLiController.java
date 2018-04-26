package com.type.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.type.service.IShuChuLiService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Deprecated
@RestController
public class ShuChuLiController{
	
    @Resource
    IShuChuLiService shuChuLiService;

    @RequestMapping(value="countshu", method = RequestMethod.POST , produces = "application/json;")
    public String countshu (@RequestParam("longnumber")String longnumber) throws Exception {
    	int[][] numberArray = shuChuLiService.getIntArray(longnumber);
        //重复性检测，看同一个九宫格内是否有相同的数字
        boolean nultiCheck = shuChuLiService.getRightShu(numberArray);
        if (!nultiCheck) {
            return "hisroty";
        }
        // 和检测，看九宫格内横与竖之和是否为15
        boolean perfectCheck = shuChuLiService.getPerfectShu(numberArray);
        if (perfectCheck) {
            shuChuLiService.writeToTxt(numberArray , "" , true);
        } else {
            shuChuLiService.writeToTxt(numberArray , "" , false);
        }
        shuChuLiService.getFinishShu(numberArray);
        return "history";
    }

    @RequestMapping(value = "/history")
    public ModelAndView history (ModelMap modelMap) {
        Map<String, List<int[][]>> history = shuChuLiService.getHistory();
        List<int[][]> list = history.get("list");
        List<int[][]> perfectList = history.get("perfectList");
        modelMap.put("list" , list);
        modelMap.put("perfectList" , perfectList);
        return new ModelAndView("history" , modelMap);
    }
}
