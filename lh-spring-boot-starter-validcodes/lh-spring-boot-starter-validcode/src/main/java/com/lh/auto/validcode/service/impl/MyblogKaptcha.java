package com.lh.auto.validcode.service.impl;

import com.google.common.primitives.Chars;
import com.lh.auto.validcode.config.MyblogValidCodeProperties;
import com.lh.auto.validcode.model.ValidCode;
import com.lh.auto.validcode.service.ValidCodeService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class MyblogKaptcha implements ValidCodeService {
    private MyblogValidCodeProperties properties;
    private BufferedImage bufferedImage;
    private Graphics graphics;
    private Random random = new Random();

    public MyblogKaptcha(MyblogValidCodeProperties myblogValidCodeProperties) {
        this.properties = myblogValidCodeProperties;
    }

    public MyblogKaptcha() {
        this.properties = new MyblogValidCodeProperties();
    }

    public void config(MyblogValidCodeProperties properties) {
        this.properties = properties;
    }

    private void init() {
        bufferedImage = new BufferedImage(properties.getWidth(), properties.getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.decode(properties.getBgColor()));
        graphics.fillRect(0, 0, properties.getWidth(), properties.getHeight());
        graphics.setFont(new Font(properties.getFontFamily(), properties.getIsBold() ? Font.BOLD : Font.PLAIN, properties.getFontSize()));
        System.gc();//清理上一次操作留下的缓存
    }

    //返回ValidCode对象
    @Override
    public ValidCode resultValidCode() throws IOException {
        ValidCode validCode = new ValidCode();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String resultValidCodeStr = drawValidCode();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        validCode.setValidStr(resultValidCodeStr)
                .setValidSteam(byteArrayOutputStream);
        byteArrayOutputStream.close();
        return validCode;
    }

    //画验证码
    private String drawValidCode() {
        init();
        String resultStr = getRandomChar();
        int space = (properties.getLength() - 1) * properties.getLetterSpace();
        int left = (properties.getWidth() - space - properties.getLength() * properties.getFontSize()) / 2 + properties.getLetterSpace();
        int y1 = properties.getFontSize() + properties.getRange();
        int y2 = properties.getRange();
        for (int i = 0; i < properties.getLength(); i++) {
            int colorsLength = properties.getColors().size();
            String randomColor = properties.getColors().get(random.nextInt(colorsLength));
            graphics.setColor(Color.decode(randomColor));
            if (i == 0) {
                graphics.drawString(String.valueOf(resultStr.charAt(i)), left + i * (properties.getLetterSpace() + properties.getFontSize()), random.nextInt(y1) + y2);
            } else {
                graphics.drawString(String.valueOf(resultStr.charAt(i)), left + i * (properties.getLetterSpace() + properties.getFontSize()), random.nextInt(y1) + y2);
            }
        }
        for (int i = 0; i < properties.getWidth() + 10; i += 10) {
            int colorsLength = properties.getInterferingLineColors().size();
            String randomColor = properties.getColors().get(random.nextInt(colorsLength));
            graphics.setColor(Color.decode(randomColor));
            graphics.drawLine(i, 0, i - 10, properties.getHeight());
        }
        graphics.dispose();
        return resultStr;
    }

    //获取随机字符串
    private String getRandomChar() {
        char[] concat = new char[0];
        List<char[]> contents = properties.getContents();
        for (char[] chars : contents) {
            concat = Chars.concat(concat, chars);
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < properties.getLength(); i++) {
            buffer.append(concat[random.nextInt(concat.length)]);
        }
        return buffer.toString();
    }

    //创建验证码，输出到outputStream,imageType:图片类型，jpg,png..
    @Override
    public String createValidCode(OutputStream outputStream, String imageType) throws IOException {
        String resultStr = drawValidCode();
        ImageIO.write(bufferedImage, imageType, outputStream);
        outputStream.close();
        return resultStr;
    }

    //输出到outputStream 默认图片类型jpg
    @Override
    public String createValidCode(OutputStream outputStream) throws IOException {
        String resultStr = drawValidCode();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        outputStream.close();
        return resultStr;
    }

}
