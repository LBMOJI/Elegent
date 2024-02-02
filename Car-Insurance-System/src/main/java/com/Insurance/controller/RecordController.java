package com.Insurance.controller;

import com.Insurance.entity.Record;
import com.Insurance.entity.User;
import com.Insurance.service.RecordService;
import com.Insurance.tools.Month;
import com.Insurance.tools.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Controller
public class RecordController {
    @Autowired
    private RecordService service;

    @RequestMapping(value = "/user/getRecord")
    public String getRecord(Model model, HttpSession session, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        User user = (User) session.getAttribute("user");
        PageHelper.startPage(pageNum, 2);
        List<Record> RList = service.getRecordByUserId(user);
        model.addAttribute("RList", RList);
        if (RList != null) {
            PageInfo pageInfo = new PageInfo<>(RList);
            model.addAttribute("page", pageInfo);
        }
        return "user/getRecord";
    }

    @RequestMapping(value = "/user/addRecord")
    @ResponseBody
    public void addRecord(Record record, HttpSession session, HttpServletResponse response) throws ParseException, IOException {
        User user = (User) session.getAttribute("user");
        record.setUid(user.getId());
        record.setPerson(user.getName());
        String s = service.addRecord(record, user);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(String.valueOf(s));
    }

    @RequestMapping(value = "/ByCph")
    public String selectByCph(@ModelAttribute("id") String id
            , @ModelAttribute("cph") String cph
            , @ModelAttribute("phone") String phone
            , Model model) {
        Record rcd = service.checkByCph(id, cph, phone);
        if (rcd == null) {
            model.addAttribute("applyError", "请检查您输入的保单号、车牌号与手机号");
            return "user/apply";
        }
        model.addAttribute("record", rcd);
        return "user/addAcception";
    }

    @RequestMapping(value = "/ByCno")
    public String selectByCno(@ModelAttribute("id") String id
            , @ModelAttribute("cno") String cno
            , @ModelAttribute("person") String person
            , Model model) {
        Record rcd = service.checkByCno(id, cno, person);
        if (rcd == null) {
            model.addAttribute("applyError", "请检查您输入的保单号、投保人姓名与投保人证件号");
            return "user/apply";
        }
        model.addAttribute("record", rcd);
        return "user/addAcception";
    }

    @RequestMapping(value = "/admin/getRecordList")
    public String getRecordList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        PageHelper.startPage(pageNum, 6);
        List<Record> list = service.selectAll();
        model.addAttribute("RList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/recordManaged";
    }

    @RequestMapping(value = "/admin/searchRecord/{rid}")
    public String toSearch(@PathVariable("rid") String rid, RedirectAttributes attr) {
        attr.addAttribute("rid", rid);
        return "redirect:/admin/searchRecord";
    }

    @RequestMapping(value = "/admin/searchRecord")
    public String searchRecord(@ModelAttribute("rid") String rid, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (rid.equals("")) {
            return "redirect:/admin/getRecordList";
        }
        PageHelper.startPage(pageNum, 6);
        List<Record> list = service.selectListById(rid);
        model.addAttribute("RList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/recordManaged";
    }

    @RequestMapping(value = "/admin/addRecord")
    @ResponseBody
    public void addRecordByAdmin(String time,Record record, HttpServletResponse response) throws IOException {
        try {
            service.addByAdmin(record,time);
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            if (e.toString().contains("NullPointerException")) {
                response.getWriter().write("用户不存在");
            } else {
                if (e.getMessage().contains("Index: 0, Size: 0")) {
                    response.getWriter().write("输入的保险或车辆不存在");
                }
            }
        }
    }

    @RequestMapping(value = "/admin/deleteRecord")
    public String deleteRecord(String id) {
        service.deleteByAdmin(id);
        return "redirect:/admin/getRecordList";
    }

    @RequestMapping(value = "/admin/reviseRecord")
    @ResponseBody
    public void reviseRecord(String start,String end,Record record, HttpServletResponse response) throws IOException {
        try {
            service.updateRecord(record,start,end);
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            if (e.toString().contains("NullPointerException")) {
                response.getWriter().write("用户不存在");
            } else {
                if (e.getMessage().contains("Index: 0, Size: 0")) {
                    response.getWriter().write("输入的保险或车辆不存在");
                }
            }
        }
    }

    @RequestMapping(value = "/admin/getDataPicture")
    public String getData(Model model) {
        try {
            Result<Integer> result = service.getData();
            Month month = service.getMonth();
            model.addAttribute("result", result);
            model.addAttribute("month", month);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "insiders/manager/pie-simple";
    }

    @GetMapping(value = "/user/recordCompute")
    @ResponseBody
    public void compute(String cph, String type, HttpSession session, HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("user");
        BigDecimal price = service.compute(cph, type, user);
        if (price != null) {
            response.getWriter().print(price);
        }else{
            response.getWriter().print("");
        }
    }
}
