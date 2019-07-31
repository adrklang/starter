package com.lh.auto.validcode.config;

import com.lh.auto.validcode.consts.ValidCodeConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
public class ValidCodeProperties {
    //验证码宽度
    private Integer width = ValidCodeConst.WIDTH;
    //验证码高度
    private Integer height = ValidCodeConst.HEIGHT;
    //验证码字符随机颜色
    private List<String> colors = Arrays.asList(ValidCodeConst.COLORS);
    //验证码内容
    private List<char[]> contents = Arrays.asList(ValidCodeConst.CONTENTS);
    //验证码背景颜色
    private String bgColor = ValidCodeConst.BGCOLOR;
    //验证码干扰线颜色
    private List<String> interferingLineColors = Arrays.asList(ValidCodeConst.INTERFERINGLINECOLORS);
    //生成验证码长度
    private Integer length = ValidCodeConst.LENGTH;
    //验证码字间距
    private Integer letterSpace = ValidCodeConst.LETTERSPACE;
    //验证码字符大小
    private Integer fontSize = ValidCodeConst.FONTSIZE;
    //验证码字符样式
    private String fontFamily = ValidCodeConst.FONT_FAMILY;
    //验证码是否加粗
    private Boolean isBold = ValidCodeConst.isBlod;
    private Integer range = ValidCodeConst.RANGE;
}
