package com.zhang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


/**
 * 验证码服务
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    //长度
    private int width = 100;

    //宽度
    private int height = 32;

    //图片内容在图片上的起始位置
    private int drawY = 20;

    //图片上文字的间隔
    private int space = 20;

    //验证码上文字的数量
    private int charCount = 4;



    // 验证码范围,去掉0(数字)和O(拼音)容易混淆的(小写的1和L也可以去掉,大写不用了)
    private String[] chars = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 定义方法：生成验证码内容，在一个图片上，写入文字
     */
    @GetMapping("/code")
    public void makeCaptchaCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        /**
         * 验证码：需要在内存中绘制一个图片BufferedImage，
         * 向这个图片中写入文字，把绘制好的图片响应给请求
         */
        //创建一个背景透明的画板
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //生成一个画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色
        g.setColor(makeColor());
        //将整个画板画为白色
        g.fillRect(0,0,width,height);


        /**
         * 画内容
         */
        // 创建字体,可以修改为其它的
        Font font = new Font("Fixedsys", Font.BOLD,16);
        g.setFont(font);
        g.setColor(Color.BLACK);

        StringBuffer buffer = new StringBuffer();
        // 生成随机数
        Random random = new Random();
        int len = chars.length;
        for (int i = 0; i < charCount; i++) {
            int nextInt = random.nextInt(len);
            buffer.append(chars[nextInt]);
            g.setColor(makeColor());
            g.drawString(chars[nextInt],(i+1)*space,drawY);
            //绘制干扰线
            g.setColor(makeColor());
            int dot[] = makeLineDot();
            g.drawLine(dot[0],dot[1],dot[2],dot[3]);

        }


        //把生成的验证码存储到session中
        request.getSession().setAttribute("code",buffer.toString());

        //设置没有缓冲
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        //设置响应数据格式
        response.setContentType("image/png");

        //获取响应输出流
        ServletOutputStream out = response.getOutputStream();

        //将画板加载到响应输出流
        ImageIO.write(image,"png",out);
        out.flush();
        out.close();

    }

    /**
     * 获取随机画笔颜色
     * @return
     */
    private Color makeColor(){
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r,g,b);
    }

    /**
     * 获取干扰线起始点坐标
     * @return
     */
    private int[] makeLineDot(){
        Random random = new Random();
        int x1 = random.nextInt(width/2);
        int y1 = random.nextInt(height);

        int x2 = random.nextInt(width);
        int y2 = random.nextInt(height);

        return new int[]{x1,y1,x2,y2};
    }

}
