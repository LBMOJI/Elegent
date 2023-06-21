package com.Insurance.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class qrCodeController {

    private final static String qrCodeUrl = "localhost:8080/doScan";

    Map<String,String> qrCodeMap = new HashMap<>();

    @GetMapping("/createQr")
    @ResponseBody
    public Result createQrCode() throws IOException {
        String uuid = UUID.randomUUID().toString();
        String qrCode = ZXingUtil.createQrCode(qrCodeUrl,200,200);
        qrCodeMap.put(qrCode,uuid);
        RedisService.set(qrCodeUrl,uuid,"NOT_SCAN");
        return ResultUtil.success(qrCode);
    }

    @GetMapping("/query")
    @ResponseBody
    public Result queryIsScannedOrVerified(@RequestParam("img")String img){
        String uuid = qrCodeMap.get(img);
        QrCodeStatus s = RedisService.get(uuid);
        return ResultUtil.success(s);
    }

    @GetMapping("/doScan")
    @ResponseBody
    public Result doAppScanQrCode(){
        String uuid = UUID.randomUUID().toString();
        QrCodeStatus status = RedisService.get(qrCodeMap.keySet().toString());
        if(status.getStatus().isEmpty()) return ResultUtil.error("二维码为空");
        if ("NOT_SCAN".equals(status.getStatus())) {//等待确认
            RedisService.set(qrCodeUrl,uuid, "SCANNED");
            return ResultUtil.success("请手机确认");

        } else if ("SCANNED".equals(status.getStatus())) {
            return ResultUtil.error("已被扫描");
        } else if ("VERIFIED".equals(status.getStatus())) {
            return ResultUtil.success("你已经确认过了");
        }
        return ResultUtil.error("服务器错误");
    }
}
