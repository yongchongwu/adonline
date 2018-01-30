package com.ifuture.adonline.util;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * Where条件生成
 */
public class SqlWhere {

    public static String DB_FORMAT = "YYYY-MM-DD";
    public static String MYSQL_FORMAT = "%Y-%m-%d";
    public static String DB_FORMAT_TIME = "YYYY-MM-DD HH24:MI:SS";
    public static String MYSQL_FORMAT_TIME = "%Y-%m-%d %H:%i:%s";

    public final static int NUMBER = 0;
    public final static int STR = 1;
    public final static int DATE = 2;

    public final static int LIKE_ALL = 0;
    public final static int LIKE_LEFT = 1;
    public final static int LIKE_RIGHT = 2;

    private String where = " ";
    private HttpServletRequest request;
    /**
     * 默认数据库的日期格式
     */
    private String defaultDbFormat = "YYYY-MM-DD";
    /**
     * 默认JAVA的日期格式
     */
    private String defaultFormat = "yyyy-MM-dd";
    /**
     * like表达式的符号
     */
    private String likeFormat = "%";
    /**
     * like表达式的类型
     *
     * 0两边加符号 1 左边 2右边
     */
    private int likeType = 0;

    private int tmpliketype;

    public SqlWhere() {
    }

    /**
     * Where条件生成
     *
     * @param where 初始值
     */
    public SqlWhere(String where) {
        this.where = where;
    }

    /**
     * Where条件生成
     */
    public SqlWhere(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Where条件生成
     *
     * @param where 初始值
     */
    public SqlWhere(HttpServletRequest request, String where) {
        this.request = request;
        this.where = where;
    }

    /**
     * 增加字符串的条件
     *
     * @param field 字段
     * @param val 值
     * @param exp 表达式
     */
    public void addWhereStr(String field, String val, String exp) {
        String valimp = replaceSQL(val);
        if (valimp != null && !valimp.equals("")) {
            if (exp.toUpperCase().equals("NULL")) {
                if (valimp.equals("0")) {
                    where = where + " and " + field + " is null ";
                } else {
                    where = where + " and " + field + " is not null ";
                }
            } else if (exp.toUpperCase().equals("LIKE")) {
                switch (likeType) {
                    case LIKE_ALL:
                        where = where + " and " + field + " " + exp + " '" + likeFormat + valimp
                            + likeFormat + "'";
                        break;
                    case LIKE_LEFT:
                        where =
                            where + " and " + field + " " + exp + " '" + likeFormat + valimp + "'";
                        break;
                    case LIKE_RIGHT:
                        where =
                            where + " and " + field + " " + exp + " '" + valimp + likeFormat + "'";
                        break;
                    default:
                        break;
                }
            } else {
                where = where + " and " + field + exp + "'" + valimp + "'";
            }
        }
    }

    /**
     * 增加数字的条件
     *
     * @param field 字段
     * @param val 值
     * @param exp 表达式
     */
    public void addWhereNumber(String field, String val, String exp) {
        if (val != null && !val.equals("")) {
            if (isNumber(val)) {
                where = where + " and " + field + exp + val;
            } else
            // 不是数字类型的加1=2 where条件不返回数据
            {
                where = where + " and 1=2";
            }
        }
    }

    /**
     * 增加日期的条件
     *
     * 默认数据库的日期格式为：YYYY-MM-DD 可以通过setDefaultDbFormat改变
     *
     * @param field 字段
     * @param val 值
     * @param exp 表达式
     */
    public void addWhereDate(String field, String val, String exp) {
        if (val != null && !val.equals("")) {
            addWhereDate(field, val, exp, defaultDbFormat);
        }
    }

    public void addWhereMysqlDate(String field, String val, String exp) {
        if (val != null && !val.equals("")) {
            addWhereMysqlDate(field, val, exp, SqlWhere.MYSQL_FORMAT);
        }
    }

    /**
     * 增加日期的条件
     *
     * @param field 字段
     * @param val 值
     * @param exp 表达式
     * @param format 数据库的日期格式
     */
    public void addWhereDate(String field, String val, String exp, String format) {
        String valimp = replaceSQL(val);
        if (valimp != null && !valimp.equals("")) {
            where = where + " and " + field + exp + "TO_DATE('" + valimp + "','" + format + "')";
        }
    }

    public void addWhereMysqlDate(String field, String val, String exp, String format) {
        String valimp = replaceSQL(val);
        if (valimp != null && !valimp.equals("")) {
            where = where + " and " + field + exp + "STR_TO_DATE('" + valimp + "','" + format + "')";
        }
    }

    /**
     * 增加条件
     *
     * @param field 字段
     * @param val 值
     * @param exp 表达式
     * @param type 数据类型
     *
     * SqlWhere.NUMBER 0，SqlWhere.STR 1，SqlWhere.DATE 2
     */
    public void addWhere(String field, String val, String exp, int type) {
        switch (type) {
            case NUMBER:
                addWhereNumber(field, val, exp);
                break;
            case STR:
                addWhereStr(field, val, exp);
                break;
            case DATE:
                addWhereDate(field, val, exp);
                break;
            default:
                break;
        }
    }

    /**
     * 从Request中增加条件
     *
     * @param p 参数
     * @param field 字段
     * @param exp 表达式
     * @param type 数据类型
     *
     * SqlWhere.NUMBER 0，SqlWhere.STR 1，SqlWhere.DATE 2
     */
    public void addParam(String p, String field, String exp, int type) {
        String val = request.getParameter(p);
        if (val == null) {
            return;
        }
        addWhere(field, val, exp, type);
    }

    /**
     * 从Request中增加条件 字段名称和p相同
     *
     * @param p 参数
     * @param exp 表达式
     * @param type 数据类型
     *
     * SqlWhere.NUMBER 0，SqlWhere.STR 1，SqlWhere.DATE 2
     */
    public void addParam(String p, String exp, int type) {
        addParam(p, p, exp, type);
    }

    /**
     * 从Request中增加条件 字段名称和p相同
     *
     * @param p 参数
     * @param exp 表达式
     * @param format 数据库的日期格式
     */
    public void addParam(String p, String exp, String format) {
        addParam(p, p, exp, format);
    }

    /**
     * 从Request中增加条件 字段名称和p相同
     *
     * @param p 参数
     * @param field 字段
     * @param exp 表达式
     * @param format 数据库的日期格式
     */
    public void addParam(String p, String field, String exp, String format) {
        String val = request.getParameter(p);
        if (val == null) {
            return;
        }
        addWhereDate(field, val, exp, format);
    }

    public void addParamEquals(String p, int type) {
        addParam(p, "=", type);
    }

    public void addParamEquals(String p, String field, int type) {
        addParam(p, field, "=", type);
    }

    public void addParamEquals(String p, String field, String format) {
        addParam(p, field, "=", format);
    }

    public void addParamLike(String p) {
        addParam(p, "like", STR);
    }

    public void addParamLike(String p, String field) {
        addParam(p, field, "like", STR);
    }

    public void addParamLikeL(String p) {
        tmpliketype = likeType;
        likeType = LIKE_LEFT;
        addParam(p, "like", STR);
        likeType = tmpliketype;
    }

    public void addParamLikeL(String p, String field) {
        tmpliketype = likeType;
        likeType = LIKE_LEFT;
        addParam(p, field, "like", STR);
        likeType = tmpliketype;
    }

    public void addParamLikeR(String p) {
        tmpliketype = likeType;
        likeType = LIKE_RIGHT;
        addParam(p, "like", STR);
        likeType = tmpliketype;
    }

    public void addParamLikeR(String p, String field) {
        tmpliketype = likeType;
        likeType = LIKE_RIGHT;
        addParam(p, field, "like", STR);
        likeType = tmpliketype;
    }

    public void addParamNull(String p) {
        addParam(p, "null", STR);
    }

    public void addParamNull(String p, String field) {
        addParam(p, field, "null", STR);
    }

    public void addEquals(String field, String val) {
        addWhereStr(field, val, "=");
    }

    public void addEquals(String field, Double val) {
        if (val != null) {
            addWhereNumber(field, val.toString(), "=");
        }
    }

    public void addEquals(String field, double val) {
        addWhereNumber(field, String.valueOf(val), "=");
    }

    public void addEquals(String field, Long val) {
        if (val != null) {
            addWhereNumber(field, val.toString(), "=");
        }
    }

    public void addEquals(String field, long val) {
        addWhereNumber(field, String.valueOf(val), "=");
    }

    public void addEquals(String field, Integer val) {
        if (val != null) {
            addWhereNumber(field, val.toString(), "=");
        }
    }

    public void addEquals(String field, int val) {
        addWhereNumber(field, String.valueOf(val), "=");
    }

    public void addEquals(String field, Date val) {
        if (val != null) {
            addWhereDate(field, DateTools.dateToStr(val, defaultFormat), "=");
        }
    }

    public void addEquals(String field, Date val, String dbformat, String format) {
        if (val != null) {
            addWhereDate(field, DateTools.dateToStr(val, format), "=", dbformat);
        }
    }

    public void addLikeR(String field, String val) {
        tmpliketype = likeType;
        likeType = LIKE_RIGHT;
        addWhereStr(field, val, "like");
        likeType = tmpliketype;
    }

    public void addLikeL(String field, String val) {
        tmpliketype = likeType;
        likeType = LIKE_LEFT;
        addWhereStr(field, val, "like");
        likeType = tmpliketype;
    }

    public void addLike(String field, String val) {
        addWhereStr(field, val, "like");
    }

    public void addIn(String field, String val, int type) {
        if (val != null && !val.equals("")) {
            switch (type) {
                case 0:
                    where = where + " and " + field + " in(" + replaceSQL(val) + ")";
                    break;
                case 1:
                    String[] arr = val.split(",");
                    String strVal = "";
                    for (int i = 0; i < arr.length; i++) {
                        strVal += "'" + replaceSQL(arr[i]) + "',";
                    }
                    if (strVal.lastIndexOf(",") != -1) {
                        strVal = strVal.substring(0, strVal.lastIndexOf(","));
                    }
                    where = where + " and " + field + " in(" + strVal + ")";
                    break;
            }
        }
    }

    /**
     * 反回where
     */
    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDefaultDbFormat() {
        return defaultDbFormat;
    }

    public void setDefaultDbFormat(String defaultDbFormat) {
        this.defaultDbFormat = defaultDbFormat;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }

    public void setDefaultFormat(String defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getLikeFormat() {
        return likeFormat;
    }

    public void setLikeFormat(String likeFormat) {
        this.likeFormat = likeFormat;
    }

    public int getLikeType() {
        return likeType;
    }

    public void setLikeType(int likeType) {
        this.likeType = likeType;
    }

    private boolean isNumber(String v) {
        try {
            Double.valueOf(v);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String replaceSQL(String v) {
        if (v == null) {
            return null;
        }
        String ret = v.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", "")
            .replaceAll("\\|", "").replaceAll(";", "");
        if (isSqlKey(ret)) {
            return "99999999";
        }
        return ret;
    }

    public static boolean isSqlKey(String str) {
        if (str == null) {
            return false;
        }
        String tmp = str.toLowerCase();
        String inj_str = "and|=|like|begin|alter|drop|exec|insert|select|delete|update|count|chr(|master|truncate|char(|declare|;";

        String inj_stra[] = inj_str.split("\\|");

        for (int i = 0; i < inj_stra.length; i++) {
            if (tmp.indexOf(inj_stra[i]) != -1) {
                return true;
            }
        }
        return false;
    }
}
