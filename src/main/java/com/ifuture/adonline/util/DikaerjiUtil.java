package com.ifuture.adonline.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unchecked")
public class DikaerjiUtil {

    public static List<String> getMixData(String[][] sets) {
        List<String> strs = new ArrayList<String>();
        int solutions = 1;
        for (int i = 0; i < sets.length; solutions *= sets[i].length, i++) {
            ;
        }
        for (int i = 0; i < solutions; i++) {
            String str = "";
            int j = 1;
            for (String[] set : sets) {
                str += set[(i / j) % set.length];
                j *= set.length;
            }
            strs.add(str);
        }
        Collections.sort(strs);
        return strs;
    }

    public static List<String> getMixData(List<String[]> oldlist) {
        List<String> result = new LinkedList<String>();
        if (oldlist != null) {
            copyArrayToList(result, oldlist.get(0));
            for (int i = 1; i < oldlist.size(); i++) {
                ListIterator iterator = (ListIterator) result.iterator();
                while (iterator.hasNext()) {
                    String oldString = (String) iterator.next();
                    iterator.remove();
                    for (int j = 0; j < oldlist.get(i).length; j++) {
                        iterator.add(oldString + oldlist.get(i)[j]);
                    }
                }
            }
        }
        return result;
    }

    public static void copyArrayToList(List list, String[] array) {
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
    }

    public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> resultLists = new ArrayList<List<T>>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<T>());
            return resultLists;
        } else {
            List<T> firstList = lists.get(0);
            List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (T condition : firstList) {
                for (List<T> remainingList : remainingLists) {
                    ArrayList<T> resultList = new ArrayList<T>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }

    public static List<String> getMatchList(List<String> strs, String pattern) {
        List<String> result = new ArrayList<String>();
        for (String str : strs) {
            if (isKeyMatched(str, pattern)) {
                result.add(str);
            }
        }
        return result;
    }

    public static String generatePattenKeyStr(String prefix, Map<String, Object> params) {

        String patten = (null == prefix) ? "" : prefix;

        if (null != params) {
            String valStr = null;
            String keyStr = null;
            //对KEY排序
            Object[] keyArr = params.keySet().toArray();
            Arrays.sort(keyArr);
            //构建
            for (Object key : keyArr) {
                keyStr = String.valueOf(key);
                valStr = String.valueOf(params.get(key));
                if (StringUtils.isNotBlank(valStr)) {
                    patten += keyStr.toLowerCase() + "_" + valStr.toLowerCase() + ":";
                } else {
                    patten += keyStr.toLowerCase() + "_unknown:";
                }
            }
        }
        return StringUtils.stripEnd(patten, ":");
    }

    public static boolean isKeyMatched(String srcKey, String targetKey) {
        boolean flag = false;
        if (srcKey.equalsIgnoreCase(targetKey)) {
            flag = true;
        } else {
            String[] srcArr = srcKey.split(":");
            String[] targetArr = targetKey.split(":");
            for (int i = 0; i < srcArr.length; i++) {
                if (srcArr[i].equalsIgnoreCase(targetArr[i]) || srcArr[i].contains("unknown")
                    || targetArr[i].contains("unknown")) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

}
