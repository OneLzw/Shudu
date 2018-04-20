package com.type.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.type.service.NineService;

@Service
public class NineServiceImpl implements NineService {
	
	/**
	 * 数独计算方法
	 * @param data
	 * @return 返回已经计算完毕的数组，如果无法计算完毕，则返回null
	 */
    public int[][] getNine(int[][] data) {
        int[][] dataClone = dataClone(data);
        Map<String, List<Integer>> emptyDataMap = getEmpty(data , null);
        updateData(emptyDataMap , dataClone);
        xunHuan(dataClone);
        boolean hasEmptyOne =true;
        while (hasEmptyOne) {
            //排除横，竖，小九宫格都有的数，获得基本的可填入数组
            emptyDataMap = getEmpty(dataClone , emptyDataMap);
            //如果在某个小九宫格内，某个可填入数只出现了一次，则确定该数填入该位置
            getTheOnlyOne(emptyDataMap , dataClone);
            //如果在一个小九宫格内，可以确定某个数只能填入确定的一列，则删除该列其他小九宫格内的该数
            getLineRowOne(emptyDataMap); //hard
            //由三个小九宫格确定，如果已经确定了两个小九宫格内的两列内必定填入某个数，
            //则第三个小九宫格的另一列必定填入该数
            getThreeRowOrLine(emptyDataMap , dataClone); //hard
            getTheOnlyOne(emptyDataMap , dataClone);
            //将所有只能填入一个数的位置，填入该数，从map中删除该坐标
            hasEmptyOne = updateData(emptyDataMap , dataClone);
            xunHuan(dataClone);
        }
        //检查数独是否完整
        boolean perfect = checkPerfect(dataClone);
        if (perfect) {
        	//打印数独
            xunHuan(dataClone );
            return dataClone;
        } else {
        	//尝试所有可填入两个数的位置，逐一进行尝试，看是否能填完数组
            int[][] perfectNine = tryOneOfTwo(emptyDataMap , dataClone);
            if (perfectNine != null){
                xunHuan(perfectNine);
                return perfectNine;
            }
        }
        return null;
    }
    /**
     * 尝试所有可填入两个数的位置，逐一进行尝试，看是否能填完数组
     * @param emptyDataMap
     * @param dataClone
     * @return
     */
    public int[][] tryOneOfTwo(Map<String, List<Integer>> emptyDataMap , int[][] dataClone) {
        boolean perfect = false;
        boolean hasEmptyOne = true;
        List<Integer> curList = new ArrayList<>();
        Map<String, List<Integer>> emptyDataMapClone = null;
        int[][] dataCloneTWO = new int[dataClone.length][];

        Set<String> keySet = emptyDataMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext() && !perfect) {
            String key = iterator.next();
            curList = emptyDataMap.get(key);
            if (curList != null && curList.size() == 2) {
                out1 : for (int i = 0 ; i < curList.size(); i++) {
                    hasEmptyOne = true;
                    emptyDataMapClone = cloneMap(emptyDataMap);
                    emptyDataMapClone.get(key).remove(i);
                    dataCloneTWO = dataClone(dataClone);
                    while (hasEmptyOne && !perfect) {
                        emptyDataMapClone = getEmpty(dataCloneTWO , emptyDataMapClone);
                        getTheOnlyOne(emptyDataMapClone , dataCloneTWO);
                        getLineRowOne(emptyDataMapClone); //hard
                        getThreeRowOrLine(emptyDataMapClone , dataCloneTWO); //hard
                        getTheOnlyOne(emptyDataMapClone , dataCloneTWO);
                        hasEmptyOne = updateData(emptyDataMapClone , dataCloneTWO);
                        perfect = checkPerfect(dataCloneTWO);
                        if (perfect) {
                            break out1;
                        }
                    }
                }
            }
        }
        if (perfect) {
            return dataCloneTWO;
        }
        return null;
    }
    
    /**
     * 以行或者列进行筛查
     * 该方法逻辑：在某一列，某个数在该小九宫格的这一列只出现了一次，并且三个小九宫格都是这样，
     * 则分别取得这三行的坐标，看看除了这三个位置，在小九宫格内这一行还出现了几次，
     * 如果出现了4次，则分别取得每一列的坐标，进行比对，唯一出现的那一列，最终填入该数，例如：
     * 2 2
     *   2 2 
     * 2 2
     * 最终在中间位置的右边填入2  
     * @param emptyDataMap
     * @param dataClone
     * @return
     */
    public boolean getThreeRowOrLine (Map<String, List<Integer>> emptyDataMap , int[][] dataClone) {
        if (emptyDataMap == null || emptyDataMap.size() == 0) {
            return false;
        }
        List<Integer> curList = null;
        for (int i = 0 ; i < dataClone.length ; i++) {
            for (int j = 0 ; j < dataClone.length ; j++) {
                curList = emptyDataMap.get(i + "," + j);
                if (curList == null || curList.size() < 2) {
                    continue;
                }
                findThree(i , -1 , emptyDataMap);
                findThree(-1 , j , emptyDataMap);
            }
        }
        return false;
    }
    
    /**
     * 查看小九内这一列内有没有单独出现的数，再看这个数是不是在每个小九宫格内都单独出现，找出这个数
     * @param x
     * @param y
     * @param emptyDataMap
     * @return
     */
    public boolean findThree (int x , int y , Map<String, List<Integer>> emptyDataMap) {
        if (emptyDataMap == null || emptyDataMap.size() == 0) {
            return false;
        }
        List<Integer> finalCommonList = new ArrayList<>();
        List<Integer> commonList = new ArrayList<>();
        List<Integer> currentList = null;
        for (int i = 0 ; i < 9 ; i++) {
            if (i == 3 || i == 6) {
                commonList = new ArrayList<>();
            }

            if (y == -1) {
                currentList = emptyDataMap.get(x + "," + i);
            } else if (x == -1) {
                currentList = emptyDataMap.get(i + "," + y);
            }

            if (currentList != null && currentList.size() > 1) {
                commonList.addAll(currentList);
            }
            if (i == 2 || i == 5 || i == 8) {
            	//找出一个小九内某列单独存在的数
                List<Integer> aloneNumber = getAloneNumber(commonList);
                if (aloneNumber == null || aloneNumber.size() == 0) {
                    break;
                }
                finalCommonList.addAll(aloneNumber);
            }
        }
        if (finalCommonList.size() == 0) {
            return false;
        }
        /**
         * 共同某列共同存在，小九内单独存在的数
         */
        List<Integer> aloneThreeNumber = getAloneThreeNumber(finalCommonList);
        //检查该数，是否除了小九内的该行，其他地方不存在
        aloneThreeNumber = checkAloneThreeNumber( x, y , aloneThreeNumber , emptyDataMap);
        if (aloneThreeNumber == null || aloneThreeNumber.size() != 1) {
            return false;
        }
        removeAloneThreeNumber(x , y , emptyDataMap , aloneThreeNumber);
        return false;
    }

    public List<Integer> getAloneThreeNumber (List<Integer> finalCommonList) {
        List<Integer> aloneThreeNumber = new ArrayList<>();
        if (finalCommonList == null || finalCommonList.size() == 0) {
            return aloneThreeNumber;
        }
        int count = 0;
        int curNumber = 0;
        for (int i = 0 ; i < finalCommonList.size() ; i++ ) {
            count = 0;
            curNumber = finalCommonList.get(i);
            for (int j = 0 ; j < finalCommonList.size() ; j++) {
                if (curNumber == finalCommonList.get(j)){
                    count ++;
                }
            }
            if (count == 3) {
                if (aloneThreeNumber.contains(curNumber)){
                    continue;
                }
                aloneThreeNumber.add(curNumber);
            }
        }
        return aloneThreeNumber;
    }

    public List<Integer> checkAloneThreeNumber (int x ,int y , List<Integer> aloneThreeNumber , Map<String, List<Integer>> emptyDataMap) {
        if (aloneThreeNumber == null || aloneThreeNumber.size() == 0) {
            return null;
        }
        int aloneIndexI = -1;
        int aloneIndexJ = -1;
        List<Integer> finalAloneThreeNumber = new ArrayList<>();
        int curAloneThreeNumber = -1;
        List<Integer> curList = null;
        boolean check = false;
        for (int m = 0 ; m < aloneThreeNumber.size() ; m++) {
            curAloneThreeNumber = aloneThreeNumber.get(m);
            if (y == -1) {
                aloneIndexI = x;
                for (int i = 0 ; i < 9 ; i++) {
                    curList = emptyDataMap.get(x + "," + i);
                    if (curList == null) {
                        continue;
                    }
                    if (curList.contains(curAloneThreeNumber)) {
                        aloneIndexJ = i;
                        //行进入，获取包含有该数的列
                        check = checkNineAloneThreeNumber(aloneIndexI , aloneIndexJ , "y" , curAloneThreeNumber , emptyDataMap);
                        if (!check) {
                            break;
                        }
                    }
                }
            } else if (x == -1) {
                aloneIndexJ = y;
                for (int i = 0 ; i < 9 ; i++) {
                    curList = emptyDataMap.get(i + "," + y);
                    if (curList == null) {
                        continue;
                    }
                    if (curList.contains(curAloneThreeNumber)) {
                        aloneIndexI = i;
                        check = checkNineAloneThreeNumber(aloneIndexI , aloneIndexJ , "x" , curAloneThreeNumber , emptyDataMap);
                        if (!check) {
                            break;
                        }
                    }
                }
            }
            if (check) {
                finalAloneThreeNumber.add(curAloneThreeNumber);
            }
        }
        return finalAloneThreeNumber;
    }

    public boolean checkNineAloneThreeNumber(int x, int y , String ignore , int curNumber , Map<String, List<Integer>> emptyDataMap) {
        boolean check = true;
        String strX = getLimit(x);
        int startX = Integer.parseInt(strX.split(",")[0]);
        int endX = Integer.parseInt(strX.split(",")[1]);
        String StrY = getLimit(y);
        int startY = Integer.parseInt(StrY.split(",")[0]);
        int endY = Integer.parseInt(StrY.split(",")[1]);
        List<Integer> curList = null;
        out1 : for (int i = startX ; i <= endX ; i++) {
            if ("x".equals(ignore) && i == x) {
                continue;
            }
            for (int j = startY ; j <= endY ; j++) {
                if ("y".equals(ignore) && j == y) {
                    continue;
                }
                curList = emptyDataMap.get(i + "," + j);
                if (curList != null && curList.contains(curNumber)){
                    check = false;
                    break out1;
                }
            }
        }
        return check;
    }

    public List<Integer> getAloneNumber (List<Integer> commonList) {
        if (commonList == null && commonList.size() == 0) {
            return null;
        }
        List<Integer> aloneNumberList = new ArrayList<>();
        int curNumber = 0;
        out1 : for (int i = 0 ; i < commonList.size() ; i++) {
            curNumber = commonList.get(i);
            for (int j = 0 ; j < commonList.size() ; j++) {
                if (i == j) {
                    continue;
                }
                if (curNumber == commonList.get(j)) {
                    continue out1;
                }
            }
            aloneNumberList.add(curNumber);
        }
        return aloneNumberList;
    }

    public boolean removeAloneThreeNumber (int x , int y , Map<String, List<Integer>> emptyDataMap , List<Integer> aloneThreeNumber) {
        if (aloneThreeNumber == null || aloneThreeNumber.size() == 0) {
            return false;
        }
        List<String> keyList = null;
        List<Integer> currentList = new ArrayList<>();
        int start = 0;
        int end = 0;
        int commonIndex = 0;
        String indexStr = null;
        for (Integer theThreeNumber : aloneThreeNumber) {
            keyList = new ArrayList<>();
            for (int i = 0 ; i < 9 ; i++) {
                if (y == -1) {
                    currentList = emptyDataMap.get(x + "," + i);
                    commonIndex = x;
                    indexStr = getLimit(x);
                } else if (x == -1) {
                    currentList = emptyDataMap.get(i + "," + y);
                    commonIndex = y;
                    indexStr = getLimit(y);
                }
                if (currentList == null || currentList.size() == 0) {
                    continue;
                }
                if (currentList.contains(theThreeNumber)) {
                    start = Integer.parseInt(indexStr.split(",")[0]);
                    end = Integer.parseInt(indexStr.split(",")[1]);
                    String str = null;
                    for (int j = start ; j <= end ; j++) {
                        if (j == commonIndex) {
                            continue;
                        }
                        if (y == -1) {
                            str = j + "," + i;
                        } else if (x == -1) {
                            str = i + "," + j;
                        }
                        if (emptyDataMap.get(str) == null){
                            continue;
                        }
                        keyList.add(str);
                    }
                }
            }
            String onlyKey = getThreeOnlyKey(keyList , x , y);
            if (onlyKey == null) {
                return false;
            }
            currentList = emptyDataMap.get(onlyKey);
            if (currentList == null || currentList.size() == 0) {
                return false;
            }
            Iterator<Integer> iterator = currentList.iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (next != theThreeNumber) {
                    iterator.remove();
                }
            }
        }
        return false;
    }

    /**
     *
     * @param keyList
     * @param x
     * @param y y== -1 , need to find different x
     * @return
     */
    public String getThreeOnlyKey (List<String> keyList , int x , int y) {
        if (keyList == null || keyList.size() == 0) {
            return null;
        }

        if (keyList.size() > 4) {
            return null;
        }
        String key = null;
        String curChar = null;
        boolean hasSame = false;
        for (int i = 0 ; i < keyList.size() ; i++) {
            hasSame = false;
            key = keyList.get(i);
            if (y == -1) {
                curChar = key.substring(0 , 1);
            } else if (x == -1) {
                curChar = key.substring(key.length() - 1 , key.length());
            }
            for (int j = 0 ; j < keyList.size() ; j++) {
                if (i == j) {
                    continue;
                }
                if (y == -1) {
                    if (keyList.get(j).startsWith(curChar)){
                        hasSame = true;
                        break;
                    }
                } else if (x == -1) {
                    if (keyList.get(j).endsWith(curChar)){
                        hasSame = true;
                        break;
                    }
                }
            }
            if (!hasSame) {
                break;
            }
        }
        return key;
    }

    /**
     * 用于某一小列确定一个数的方法、
     * @param emptyDataMap
     * @return
     */
    public boolean getLineRowOne (Map<String, List<Integer>> emptyDataMap) {
        if (emptyDataMap == null || emptyDataMap.size() == 0) {
            return false;
        }
        boolean hasLineRowOne = false;
        Set<Map.Entry<String, List<Integer>>> entrySet = emptyDataMap.entrySet();
        Iterator<Map.Entry<String, List<Integer>>> iterator = entrySet.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<Integer>> entry = iterator.next();
            String key = entry.getKey();
            String[] keyArray = key.split(",");
            int x = Integer.parseInt(keyArray[0]);
            int y = Integer.parseInt(keyArray[1]);
            getLineAndRow(emptyDataMap , x , y);
            getLineAndRowNew(emptyDataMap , x , y);
        }
        return false;
    }
    /**
     * 该小九宫格内的某一个列都由某个数，而其他列没有，则该数必定填入该小九宫格的该列中，删除该列其他小九宫格内的该数
     * @param emptyDataMap
     * @param m
     * @param n
     * @return
     */
    public boolean getLineAndRowNew(Map<String, List<Integer>> emptyDataMap , int m , int n) {
        List<List<Integer>> rowOrLineFillList = new ArrayList<>();
        List<List<Integer>> otherOOLFillList = new ArrayList<>();
        List<Integer> commonNumberList = null;
        List<Integer> curFillList = null;
        String indexStrx = getLimit(m);
        int curIStart = Integer.parseInt(indexStrx.split(",")[0]);
        int curIEnd = Integer.parseInt(indexStrx.split(",")[1]);
        indexStrx = getLimit(n);
        int curJStart = Integer.parseInt(indexStrx.split(",")[0]);
        int curJEnd = Integer.parseInt(indexStrx.split(",")[1]);
        for (int i = curIStart ; i <= curIEnd ; i++) {
        	//获得小九宫格内一行或一列的数据
            rowOrLineFillList = new ArrayList<>();
            otherOOLFillList = new ArrayList<>();
            for (int j = curJStart ; j <= curJEnd ; j++) {
                curFillList = emptyDataMap.get(i + "," + j);
                if (curFillList == null) {
                    continue;
                }
                rowOrLineFillList.add(curFillList);
            }
            //获得这一行或这一列共有的某个数
            commonNumberList = getTheCommonNumber(rowOrLineFillList);
            for (int x = curIStart ; x <= curIEnd ; x++) {
                if (x == i) {
                    continue;
                }
                for (int y = curJStart ; y <= curJEnd ; y++) {
                    curFillList = emptyDataMap.get(x + "," + y);
                    if (curFillList == null) {
                        continue;
                    }
                    otherOOLFillList.add(curFillList);
                }
            }
            //检查其他一行行或列是否包含这个数，返回其他行或者列没有的数的集合
            commonNumberList = checkCommonNumberList(commonNumberList , otherOOLFillList);
            if (commonNumberList != null && commonNumberList.size() == 0) {
            	//删除其他行或者列内的该数
                removeOtherLORCommonNumber(commonNumberList , i , -1 , emptyDataMap , curIStart , curIEnd);
            }

        }

        for (int j = curJStart ; j <= curJEnd ; j++) {
            rowOrLineFillList = new ArrayList<>();
            otherOOLFillList = new ArrayList<>();
            for (int i = curIStart ; i <= curIEnd ; i++) {
                curFillList = emptyDataMap.get(i + "," + j);
                if (curFillList == null) {
                    continue;
                }
                rowOrLineFillList.add(curFillList);
            }
            commonNumberList = getTheCommonNumber(rowOrLineFillList);
            for (int y = curJStart ; y <= curJEnd ; y++) {
                if (y == j) {
                    continue;
                }
                for (int x = curIStart ; x <= curIEnd ; x++) {
                    curFillList = emptyDataMap.get(x + "," + y);
                    if (curFillList == null) {
                        continue;
                    }
                    otherOOLFillList.add(curFillList);
                }
            }
            commonNumberList = checkCommonNumberList(commonNumberList , otherOOLFillList);
            if (commonNumberList != null && commonNumberList.size() != 0) {
                removeOtherLORCommonNumber(commonNumberList , -1 , j , emptyDataMap , curIStart , curIEnd);
            }
        }
        return false;
    }
    /**
     * 找出所有集合中所共有的数
     * @param rowOrLineFillList
     * @return
     */
    public List<Integer> getTheCommonNumber ( List<List<Integer>> rowOrLineFillList) {
        List<Integer> commonList = new ArrayList<>();
        List<Integer> curList = null;
        int commonNumber = 0;
        boolean common = true;
        for (int i = 0 ; i < rowOrLineFillList.size() ; i++) {
            curList = rowOrLineFillList.get(i);
            if (curList.size() == 1) {
                continue;
            }
            out2 : for (int j = 0 ; j < curList.size() ; j++) {
                commonNumber = curList.get(j);
                common = true;
                for (int m = 0 ; m < rowOrLineFillList.size() ; m++) {
                    if (i == m) {
                        continue;
                    }
                    if (!rowOrLineFillList.get(m).contains(commonNumber)) {
                        common = false;
                        continue out2;
                    }
                }
                if (common) {
                    if (!commonList.contains(commonNumber)) {
                        commonList.add(commonNumber);
                    }
                }
            }
        }
        return commonList;
    }
    /**
     * 检查其他一行行或列是否包含这个数，返回其他行或者列没有的数的集合
     * @param commonNumberList
     * @param otherOOLFillList
     * @return
     */
    public List<Integer> checkCommonNumberList (List<Integer> commonNumberList , List<List<Integer>> otherOOLFillList) {
        if (commonNumberList == null || commonNumberList.size() == 0) {
            return null;
        }
        if (otherOOLFillList == null || otherOOLFillList.size() == 0) {
            return commonNumberList;
        }
        int curNumber = 0;
        boolean common = true;
        List<Integer> curList = null;
        List<Integer> checkCommonList = new ArrayList<>();
        for (int i = 0 ; i < commonNumberList.size() ; i++) {
            curNumber = commonNumberList.get(i);
            common = true;
            for (int j = 0 ; j < otherOOLFillList.size() ; j++) {
                curList = otherOOLFillList.get(j);
                if (curList.contains(curNumber)) {
                    common = false;
                    break;
                }
            }
            if (common) {
                if (!checkCommonList.contains(curNumber)) {
                    checkCommonList.add(curNumber);
                }
            }
        }
        return checkCommonList;
    }
    /**
     * x,y 位置所在的小九宫格已经确定了某个数在某一列,删除其他小九宫格内的该列的该数
     * @param commonNumberList
     * @param x
     * @param y
     * @param emptyDataMap
     * @param start
     * @param end
     * @return
     */
    public boolean removeOtherLORCommonNumber (List<Integer> commonNumberList , int x , int y , Map<String, List<Integer>> emptyDataMap , int start , int end) {
        if (commonNumberList == null || commonNumberList.size() == 0 ) {
            return false;
        }
        boolean hasChange = false;
        List<Integer> curList = null;
        for (int i = 0 ; i < 9 ; i++) {
            if (i >= start && i<= end) {
                continue;
            }
            if (y == -1) {
                curList = emptyDataMap.get(x + "," + i);
            } else if (x == -1) {
                curList = emptyDataMap.get(i + "," + y);
            }
            if (curList == null || curList.size() == 1) {
                continue;
            }
            for (Integer curNumber : commonNumberList) {
                if (curList.contains(curNumber)) {
                    curList.remove(curList.indexOf(curNumber));
                    hasChange = true;
                }
            }
        }
        return hasChange;
    }

    public boolean getLineAndRow (Map<String, List<Integer>> emptyDataMap , int m , int n) {
        List<Integer> allFillList = new ArrayList<>();
        List<Integer> curFillList = null;
        int curIStart = 0;
        int curIEnd = 0;
        int curJStart = 0;
        int curJEnd = 0;
        String indexStrx = getLimit(m);
        curIStart = Integer.parseInt(indexStrx.split(",")[0]);
        curIEnd = Integer.parseInt(indexStrx.split(",")[1]);
        String indexStry = getLimit(n);
        curJStart = Integer.parseInt(indexStry.split(",")[0]);
        curJEnd = Integer.parseInt(indexStry.split(",")[1]);
        int firstI = -1;
        int firstJ = -1;
        boolean line = true;
        boolean row = true;
        out1 : for (int i = curIStart ; i <= curIEnd ; i++) {
            for (int j = curJStart ; j <= curJEnd ; j++) {
                curFillList = emptyDataMap.get(i + "," + j);
                if (curFillList == null) {
                    continue;
                }
                if (firstI == -1 && firstJ == -1) {
                    firstI = i;
                    firstJ = j;
                } else {
                    if (firstI != i && line == true) {
                        line = false;
                    }
                    if (firstJ != j && row == true) {
                        row = false;
                    }
                }
                if (line || row) {
                    allFillList.addAll(curFillList);
                } else {
                    break out1;
                }
            }
        }

        if (line == true) {
            removeRowOrLine(emptyDataMap , firstI , -1 , allFillList , curJStart , curJEnd);
        }

        if (row == true) {
            removeRowOrLine(emptyDataMap , -1 , firstJ , allFillList , curIStart , curIEnd);
        }
        return false;
    }

    public void removeRowOrLine (Map<String, List<Integer>> emptyDataMap , int m , int n , List<Integer> allFillList , int start , int end) {
        List<Integer> curList = null;
        for (int i = 0 ; i < 9 ; i++) {
            if (i >= start && i <= end ) {
                continue;
            }
            if (n == -1) {//line
                curList = emptyDataMap.get(m + "," + i);
            } else if (m == -1)  {
                curList = emptyDataMap.get(i + "," + n);
            }
            if (curList == null) {
                continue;
            }
            for (int j = 0 ; j < allFillList.size() ; j++) {
                int fillNumber = allFillList.get(j);
                boolean contains = curList.contains(fillNumber);
                if (!contains) {
                    continue;
                }
                curList.remove(curList.indexOf(fillNumber));
            }
        }
    }
    

    /**
     * 更新数组，将空位上只可以填入一个数的位置，填入该数
     * @param emptyDataMap
     * @param dataClone
     * @return
     */
    public boolean updateData (Map<String, List<Integer>> emptyDataMap , int[][] dataClone) {
        if (emptyDataMap == null || emptyDataMap.size() == 0) {
            return false;
        }
        boolean hasEmptyOne = false;
        List<Integer> needNumberList = null;
        Set<Map.Entry<String, List<Integer>>> entrySet = emptyDataMap.entrySet();
        Iterator<Map.Entry<String, List<Integer>>> iterator = entrySet.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<Integer>> entry = iterator.next();
            String key = entry.getKey();
            needNumberList = entry.getValue();
            if (needNumberList != null && needNumberList.size() == 0) {
                iterator.remove();
            }
            if (needNumberList != null && needNumberList.size() == 1) {
                String[] keyArray = key.split(",");
                dataClone[Integer.parseInt(keyArray[0])][Integer.parseInt(keyArray[1])] = needNumberList.get(0);
                hasEmptyOne = true;
                iterator.remove();
            }
        }
        return hasEmptyOne;
    }
    /**
     * 获得某个位置唯一能填入的数
     * 遍历map，获得坐标，逐一筛查
     * @param emptyDataMap
     * @param dataClone
     * @return
     */
    public boolean getTheOnlyOne (Map<String, List<Integer>> emptyDataMap , int[][] dataClone) {
        if (emptyDataMap == null || emptyDataMap.size() == 0) {
            return false;
        }
        boolean hasOnlyOne = false;

        List<Integer> needNumberList = null;
        Set<String> keys = emptyDataMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] keyArray = key.split(",");
            needNumberList = emptyDataMap.get(key);
            if (needNumberList != null && needNumberList.size() != 1) {
                boolean hsaRemove = fillAndRemove(emptyDataMap , dataClone , Integer.parseInt(keyArray[0]),  Integer.parseInt(keyArray[1]));
                if (hsaRemove) {
                    hasOnlyOne = true;
                }
            }
        }
        return hasOnlyOne;
    }
    
    /**
     * 获得该坐标下，小九宫格所有空位置可填入数的集合
     * @param emptyDataMap
     * @param dataClone
     * @param m
     * @param n
     * @return
     */
    public boolean fillAndRemove (Map<String, List<Integer>> emptyDataMap , int[][] dataClone , int m , int n) {
        boolean hasFill = false;
        List<List<Integer>> nineFillList = new ArrayList<>();
        List<Integer> curFillList = null;
        int curIStart = 0;
        int curIEnd = 0;
        int curJStart = 0;
        int curJEnd = 0;
        String indexStrx = getLimit(m);
        curIStart = Integer.parseInt(indexStrx.split(",")[0]);
        curIEnd = Integer.parseInt(indexStrx.split(",")[1]);
        String indexStry = getLimit(n);
        curJStart = Integer.parseInt(indexStry.split(",")[0]);
        curJEnd = Integer.parseInt(indexStry.split(",")[1]);
        for (int i = curIStart ; i <= curIEnd ; i++) {
            for (int j = curJStart ; j <= curJEnd ; j++) {
                curFillList = emptyDataMap.get(i + "," + j);
                if (curFillList == null) {
                    continue;
                }
                nineFillList.add(curFillList);
            }
        }
        boolean hasRemove = removeTheMore(nineFillList);
        return hasRemove;
    }
    
    /**
     * 找出那个小九宫格内某个位置的固定的数，
     * 它在这个集合中只出现了一次，找到它，并删除该位置集合内的其他数
     * @param nineFillList 小九宫格内所有位置可填入数的集合的集合
     * @return
     */
    public boolean removeTheMore (List<List<Integer>> nineFillList) {
        boolean hasRemove = false;
        if (nineFillList == null || nineFillList.size() == 0) {
            return hasRemove;
        }
        boolean canRemove = true;
        int theNumber = 0;
        for (int i = 0 ; i < nineFillList.size() ; i++) {
            canRemove = true;
            theNumber = 0;
            List<Integer> curList = nineFillList.get(i);
            if (curList.size() == 1) {
                continue;
            }
            for (int curNumber : curList) {
                canRemove = true;
                for (int j = 0 ; j < nineFillList.size() ; j++) {
                    if (i == j) {
                        continue;
                    }
                    List<Integer> otherList = nineFillList.get(j);
                    boolean contains = otherList.contains(curNumber);
                    if (contains) {
                        canRemove = false;
                        break;
                    }
                }
                if (canRemove) {
                    theNumber = curNumber;
                    break;
                }
            }
            if (canRemove) {
                for (int j = 0 ; j < curList.size() ; j++) {
                    int index = curList.indexOf(theNumber);
                    if (j == index) {
                        continue;
                    }
                    curList.remove(j);
                }
                hasRemove = true;
            }
        }
        return hasRemove;
    }
    
    /**
     * 简单的重复性检测
     * @param numberArray
     * @return true 通过 ， false 不通过
     */
    @Override
    public boolean checkMultiShu(int[][] numberArray) {
        boolean right = true;
        List<Integer> rowNumberList = null;
        List<Integer> lineNumberList = null;
        int oneNumber = 0;
        int curLineNumber = 0;
        for (int i = 0 ; i < numberArray.length ; i++) {
        	rowNumberList = new ArrayList<>();
        	lineNumberList = new ArrayList<>();
            for (int j = 0 ; j <numberArray[i].length ; j++ ) {
                oneNumber = numberArray[i][j];
                curLineNumber = numberArray[j][i];
                if (oneNumber == 0) {
                    continue;
                }
                boolean contain = rowNumberList.contains(oneNumber);
                if (contain) {
                    right = false;
                    break;
                } else {
                	rowNumberList.add(oneNumber);
                }
                
                if (curLineNumber == 0) {
                	continue;
                }
                boolean containLine = lineNumberList.contains(curLineNumber);
                if (containLine) {
                    right = false;
                    break;
                } else {
                	lineNumberList.add(curLineNumber);
                }
            }
        }
        return right;
    }

    /**
     * 15
     * @param data
     * @return
     */
    public boolean getPerfectShu(int[][] data) {
        int sum = 0;
        boolean result = false;
        for (int i = 0 ; i < data.length ; i++) {
            sum = 0;
            int [] curRow = data[i];
            for (int j = 0 ; j < curRow.length ; j++) {
                sum += curRow[j];
            }
            if (sum != 15) {
                break;
            }
            sum = 0;
            for (int k = 0 ; k < data.length ; k++) {
                sum += data[k][i];
            }
            if (sum != 15) {
                break;
            }
        }
        if (sum == 15) {
            result = true;
        }
        return result;
    }


//    public void writeToTxt(int[][] numberArray , String nameIndex , boolean perfect) throws  Exception {
//        File file = null;
//        if (perfect) {
////            file = new File("C:\\Users\\Administrator\\Desktop\\txt\\perfect\\data" + nameIndex + ".txt");
//            file = new File(path + "\\perfect\\data" + nameIndex + ".txt");
//        } else {
//            file = new File(path + "\\data" + nameIndex + ".txt");
//        }
//
//        if (file.exists()) {
//            int namei = StringUtils.isEmpty(nameIndex) ? 0 : Integer.parseInt(nameIndex) + 1;
//            writeToTxt(numberArray , namei + "" , perfect);
//            return;
//        }
//        String numberString = getString(numberArray);
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(numberString.getBytes());
//        fos.close();
//
//    }

    public List<List<Integer>> getFinishShu(int[][] data) {
        List<List<Integer>> finishShu = new ArrayList<>();
        if (data[1][1] != 5) {
            return null;
        }
        //missing nimber at x,y
        Map<String , List<Integer>> emptyMap = getEmpty(data , null);
        findTheOnly(emptyMap);
        return finishShu;
    }

//    public Map<String , List<int[][]>> getHistory() {
//        Map<String , List<int[][]>> dataMap = new HashMap<>();
//        List<int[][]> perfectList = new ArrayList<>();
//        List<int[][]> list = new ArrayList<>();
//        File file1 = new File(path);
//        File[] fileArray = file1.listFiles();
//        for (int i = 0 ; i < fileArray.length ; i++ ) {
//            File file = fileArray[i];
//            if (file.isDirectory()) {
//                continue;
//            }
//            int[][] intArray = getIntArray(file);
//            list.add(intArray);
//        }
//        dataMap.put("list" , list);
//
//        File filePerfect = new File(path + "\\perfect");
//        File[] perfectFileArray = filePerfect.listFiles();
//        for (int i = 0 ; i < perfectFileArray.length ; i++) {
//            int[][] intArray = getIntArray(perfectFileArray[i]);
//            perfectList.add(intArray);
//        }
//        dataMap.put("perfectList" , perfectList);
//        return dataMap;
//    }

    public int[][] getIntArray (File file){
        List<String> arrStrings = null;
        try {
            arrStrings = FileUtils.readLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[][] intArray = new int[arrStrings.size()][];
        int[] oneRow = null;
        for (int j = 0 ; j < arrStrings.size() ; j++) {
            String str = arrStrings.get(j);
            String[] arrs = str.split(",");
            oneRow = new int[arrs.length];
            for (int i = 0 ; i < arrs.length ; i++) {
                oneRow[i] = Integer.parseInt(arrs[i]);
            }
            intArray[j] = oneRow;
        }
        return intArray;
    }
    
    /**
     * 获取每一个空的位置可以填入的数字的集合，按照 <'x,y' , 集合>的方式存储所有空白处可以填入的数 
     * @param data
     * @param emptyMap
     * @return
     */
    public  Map<String , List<Integer>> getEmpty(int[][] data , Map<String , List<Integer>> emptyMap){
        if (emptyMap == null) {
            emptyMap = new HashMap<>();
        }
        List<Integer> emptyList = null;
        for (int i = 0 ; i < data.length ; i++) {
            int [] curRow = data[i];
            for (int j = 0 ; j < curRow.length ; j++) {
                if (curRow[j] != 0) {
                    continue;
                }
                if (emptyMap.get(i + "," + j) != null && emptyMap.get(i + "," + j).size() == 1) {
                    continue;
                }
                emptyList = getEmptyList(data , i , j);
                emptyMap.put(i + "," + j , emptyList);
            }
        }
        return emptyMap;
    }
    
    /**
     * 简单判断该位置可以填入的数
     * @param data
     * @param m
     * @param n
     * @return
     */
    public List<Integer> getEmptyList(int[][] data , int m , int n){
        List<Integer> list = getList();
        getNeedNumber(data , m , n , list);
        int[] row = data[m];
        for (int i = 0 ; i < row.length ; i++ ) {
            int curNumber = row[i];
            if (!list.contains(curNumber)) {
                continue;
            }
            list.remove(list.indexOf(curNumber));
        }

        for (int i = 0 ; i < data.length ; i++) {
            int curNumber = data[i][n];
            if (!list.contains(curNumber)) {
                continue;
            }
            list.remove(list.indexOf(curNumber));
        }
        return list;
    }
    
    /**
     * 该左边，在小九宫格内可以填入的数
     * @param data
     * @param m
     * @param n
     * @param list
     */
    public void getNeedNumber (int[][] data , int m , int n , List<Integer> list){
        int curIStart = 0;
        int curIEnd = 0;
        int curJStart = 0;
        int curJEnd = 0;
        String strX = getLimit(m);
        curIStart = Integer.parseInt(strX.split(",")[0]);
        curIEnd = Integer.parseInt(strX.split(",")[1]);

        String strY = getLimit(n);
        curJStart = Integer.parseInt(strY.split(",")[0]);
        curJEnd = Integer.parseInt(strY.split(",")[1]);
        for (int i = curIStart ; i <= curIEnd ; i++) {
            for (int j = curJStart ; j <= curJEnd ; j++) {
                int curNumber = data[i][j];
                if (curNumber == 0 || !list.contains(curNumber)) {
                    continue;
                }
                list.remove(list.indexOf(curNumber));
            }
        }
    }

    public String getString(int[][] numberArray){
        StringBuilder sbu = new StringBuilder();
        for (int i = 0 ; i < numberArray.length ; i++) {
            for (int j = 0 ; j < numberArray[i].length ; j++) {
                int curNumber = numberArray[i][j];
                if (j == numberArray[i].length - 1) {
                    sbu.append(curNumber);
                } else {
                    sbu.append(curNumber + ",");
                }
            }
            if (i != numberArray.length - 1) {
                sbu.append("\r\n");
            }
        }
        return sbu.toString();
    }

    public void findTheOnly (Map<String , List<Integer>> emptyDataMap) {
        Set<String> keys = emptyDataMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<Integer> numberArray = emptyDataMap.get(key);
            for (Integer number : numberArray) {
            }
        }
    }

    public List<Integer> getList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1 ; i < 10 ; i++) {
            list.add(i);
        }
        return list;
    }
    
    /**
     * 打印当前数组
     * @param data
     */
    public void xunHuan (int[][] data) {
    	System.out.println("======================");
        for (int i = 0 ; i < data.length ; i++) {
            for (int j = 0 ; j < data[i].length ; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String getLimit (int k) {
        int x = 0;
        int y = 0;
        if (k <= 2) {
            y = 2;
        }else if (k >= 3 && k <= 5) {
            x = 3;
            y = 5;
        } else {
            x = 6;
            y = 8;
        }
        return x + "," + y;
    }

    public Map<String, List<Integer>> cloneMap(Map<String, List<Integer>> originMap){
        Map<String, List<Integer>> mapClone = new HashMap<>();
        List<Integer> curList = new ArrayList<>();
        List<Integer> curCloneList = new ArrayList<>();
        Set<String> keySet = originMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            curList = originMap.get(key);
            curCloneList = new ArrayList<>();
            for (Integer curNumber : curList) {
                curCloneList.add(curNumber);
            }
            mapClone.put(key , curCloneList);
        }
        return mapClone;
    }

    public boolean checkPerfect (int[][] data) {
        boolean perfect = true;
        List<Integer> list = null;
        int curNumber = 0;
        for (int i = 0 ; i < data.length ; i++) {
            list =  getList();
            for (int j = 0 ; j < data[i].length ; j++) {
                curNumber = data[i][j];
                if (list.contains(curNumber)) {
                    list.remove(list.indexOf(curNumber));
                }
            }
            if (list.size() != 0) {
                perfect = false;
                break;
            }

            list =  getList();
            for (int j = 0 ; j < data[i].length ; j++) {
                curNumber = data[j][i];
                if (list.contains(curNumber)) {
                    list.remove(list.indexOf(curNumber));
                }
            }
            if (list.size() != 0) {
                perfect = false;
                break;
            }
        }
        return perfect;
    }
    
    /**
     * 数组克隆，复制新的数组
     * @param data
     * @return
     */
    public int[][] dataClone (int[][] data) {
        int[][] dataClone = new int[9][9];
        for (int i = 0 ; i < data.length ; i++) {
            for (int j = 0 ; j < data[i].length ; j++) {
                dataClone[i][j] = data[i][j];
            }
        }
        return dataClone;
    }
    
    @Override
    public int[][] fromStringToArray (String numberStr) {
    	int[][] array = new int[9][9];
    	if (StringUtils.isEmpty(numberStr)) {
    		return array;
    	}
    	String[] rows = numberStr.split(";");
    	String[] lines = null;
    	for (int i = 0 ; i < rows.length ; i++) {
    		lines = rows[i].split(",");
    		for (int j = 0 ; j < lines.length ; j++) {
    			array[i][j] = Integer.parseInt(lines[j]);
    		}
    	}
    	return array;
    }
}
