package com.type.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.type.service.IShuChuLiService;
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
	
	@Resource
	IShuChuLiService shuChuLiService;
	
	@RequestMapping(value = "gotonine")
	public String goToNinePage() {
		return "nine/ninePage";
	}
	
	@RequestMapping(value="countnine", method = RequestMethod.POST , produces = "application/json;")
    public String countshu (@RequestParam("longnumber")String longnumber) throws Exception {
    	int[][] numberArray = shuChuLiService.getIntArray(longnumber);
        //重复性检测，看同一个九宫格内是否有相同的数字
        boolean nultiCheck = shuChuLiService.getRightShu(numberArray);
        if (!nultiCheck) {//有重复数字
            return "nine/ninePage";
        }
        nultiCheck = shuChuLiService.getNine(null);
        if (nultiCheck) {//完美解答
        	 return "nine/ninePage";
        }
        return "nine/ninePage";
    }
}
