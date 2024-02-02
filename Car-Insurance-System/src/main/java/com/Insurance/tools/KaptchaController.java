package com.Insurance.tools;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Autowired
    private DefaultKaptcha kaptchaProducer;

    @RequestMapping("/user/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        kaptchaRequest(request, response);
    }

    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha2(HttpServletRequest request, HttpServletResponse response) throws Exception{
        kaptchaRequest(request, response);
        return;

    }

    private void kaptchaRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captchaChangeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            String createText = kaptchaProducer.createText();
            request.getSession().setAttribute("verifyCode", createText);
            BufferedImage challenge = kaptchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        }catch (IllegalArgumentException e){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaChangeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChangeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
