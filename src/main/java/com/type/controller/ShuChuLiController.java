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

@RestController
public class ShuChuLiController{
	
    @Resource
    IShuChuLiService shuChuLiService;

    @RequestMapping(value="countshu", method = RequestMethod.POST , produces = "application/json;")
    public String countshu (@RequestParam("longnumber")String longnumber) throws Exception {
        String[] numbers = longnumber.split(";");
        int[][] numberArray = new int[numbers.length][numbers.length];
        String oneNumber = "0";
        int oneNumberInteger = 0;
        for (int i = 0 ; i < numbers.length ; i++) {
            String oneNunbers = numbers[i];
            String[] oneNumbersArray = oneNunbers.split(",");
            for (int j = 0 ; j < oneNumbersArray.length ; j++) {
                oneNumber = oneNumbersArray[j];
                oneNumberInteger = Integer.parseInt(oneNumber);
                numberArray[i][j] = oneNumberInteger;
            }
        }

        //multi check
        boolean nultiCheck = shuChuLiService.getRightShu(numberArray);
        if (!nultiCheck) {
            return "view/hisroty";
        }
        // sum check
        boolean perfectCheck = shuChuLiService.getPerfectShu(numberArray);
        if (perfectCheck) {
            shuChuLiService.writeToTxt(numberArray , "" , true);
        } else {
            shuChuLiService.writeToTxt(numberArray , "" , false);
        }
        shuChuLiService.getFinishShu(numberArray);
        return "view/history";
    }

    @RequestMapping(value = "/history")
    public ModelAndView history (ModelMap modelMap) {
        Map<String, List<int[][]>> history = shuChuLiService.getHistory();
        List<int[][]> list = history.get("list");
        List<int[][]> perfectList = history.get("perfectList");
        modelMap.put("list" , list);
        modelMap.put("perfectList" , perfectList);
        return new ModelAndView("view/history" , modelMap);
    }
}
