package com.lh.auto.validcode.consts;

public interface ValidCodeConst {
    public static char[] CONTENTS = {
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };
    public static Integer WIDTH = 200;
    public static Integer HEIGHT = 60;
    public static Integer LENGTH = 6;
    public static String BGCOLOR = "#c5bcc5";
    public static String[] COLORS = {
            "#409eff","#409efa","#d075cc","#7b8822","#f36fed"
    };
    public static String[] INTERFERINGLINECOLORS = {
            "#409eff","#ff4040","#d075cc","#7b8822","#f36fed"
    };
    public static Integer LETTERSPACE = 5;
    public static Integer FONTSIZE = 30;
    public static String FONT_FAMILY = "楷体";
    public static Integer RANGE = 10;
    public static Boolean isBlod = false;
}
