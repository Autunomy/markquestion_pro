package com.hty.markquestion.util;

public class ImgTypeStatic {
    public static final String[] TYPE = new String[]{
      "png","jpg","JPG","PNG"
    };

    public static boolean isInType(String str){
        for (String s : TYPE) {
            if(s.equals(str)) return true;
        }
        return false;
    }
}
