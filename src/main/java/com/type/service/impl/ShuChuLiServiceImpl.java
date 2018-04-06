package com.type.service.impl;

import com.type.service.IShuChuLiService;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ShuChuLiServiceImpl implements IShuChuLiService {
    private String path = "C:\\Users\\Administrator\\Desktop\\txt";
    @Override
    public boolean getRightShu(int[][] numberArray) {
        // 9 multi
        boolean right = true;
        List<Integer> numberList = new ArrayList<>();
        for (int i = 0 ; i < numberArray.length ; i++) {
            for (int j = 0 ; j <numberArray[i].length ; j++ ) {
                int oneNumber = numberArray[i][j];
                if (oneNumber == 0) {
                    continue;
                }
                boolean contain = numberList.contains(oneNumber);
                if (contain) {
                    right = false;
                    break;
                } else {
                    numberList.add(oneNumber);
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
    @Override
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


    @Override
    public void writeToTxt(int[][] numberArray , String nameIndex , boolean perfect) throws  Exception {
        File file = null;
        if (perfect) {
//            file = new File("C:\\Users\\Administrator\\Desktop\\txt\\perfect\\data" + nameIndex + ".txt");
            file = new File(path + "\\perfect\\data" + nameIndex + ".txt");
        } else {
            file = new File(path + "\\data" + nameIndex + ".txt");
        }

        if (file.exists()) {
            int namei = StringUtils.isEmpty(nameIndex)? 0 : Integer.parseInt(nameIndex) + 1;
            writeToTxt(numberArray , namei + "" , perfect);
            return;
        }
        String numberString = getString(numberArray);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(numberString.getBytes());
        fos.close();

    }

    @Override
    public List<List<Integer>> getFinishShu(int[][] data) {
        List<List<Integer>> finishShu = new ArrayList<>();
        if (data[1][1] != 5) {
            return null;
        }
        //missing nimber at x,y
        Map<String , List<Integer>> emptyMap = getEmpty(data);
        findTheOnly(emptyMap);
        return finishShu;
    }

    @Override
    public Map<String , List<int[][]>> getHistory() {
        Map<String , List<int[][]>> dataMap = new HashMap<>();
        List<int[][]> perfectList = new ArrayList<>();
        List<int[][]> list = new ArrayList<>();
        File file1 = new File(path);
        File[] fileArray = file1.listFiles();
        if (fileArray == null || fileArray.length > 0) {
        	return dataMap;
        }
        for (int i = 0 ; i < fileArray.length ; i++ ) {
            File file = fileArray[i];
            if (file.isDirectory()) {
                continue;
            }
            int[][] intArray = getIntArray(file);
            list.add(intArray);
        }
        dataMap.put("list" , list);

        File filePerfect = new File(path + "\\perfect");
        File[] perfectFileArray = filePerfect.listFiles();
        for (int i = 0 ; i < perfectFileArray.length ; i++) {
            int[][] intArray = getIntArray(perfectFileArray[i]);
            perfectList.add(intArray);
        }
        dataMap.put("perfectList" , perfectList);
        return dataMap;
    }

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

    public  Map<String , List<Integer>> getEmpty(int[][] data){
        Map<String , List<Integer>> emptyMap = new HashMap<>();
        List<Integer> emptyList = null;
        for (int i = 0 ; i < data.length ; i++) {
            int [] curRow = data[i];
            for (int j = 0 ; j < curRow.length ; j++) {
                emptyList = new ArrayList<>();
                if (curRow[j] != 0) {
                    continue;
                }
                emptyList = getEmptyList(data , i , j);
                emptyMap.put(i + "," + j , emptyList);
            }
        }
        return emptyMap;
    }

    public List<Integer> getEmptyList(int[][] data , int m , int n){
        List<Integer> list = getList();
        list = getNeedNumber(data , list);
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

    public List<Integer> getNeedNumber (int[][] data , List<Integer> list){
        for (int i = 0 ; i <data.length ; i++) {
            for (int j = 0 ; j < data[i].length ; j++) {
                int number = data[i][j];
                if (number == 0) {
                    continue;
                }
                list.remove(list.indexOf(number));
            }
        }
        return list;
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
            System.out.println(key);
            List<Integer> numberArray = emptyDataMap.get(key);
            for (Integer number : numberArray) {
                System.out.print(number + " ,");
            }
            System.out.println();
        }
        System.out.println("end");
    }

    public List<Integer> getList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1 ; i < 10 ; i++) {
            list.add(i);
        }
        return list;
    }
}