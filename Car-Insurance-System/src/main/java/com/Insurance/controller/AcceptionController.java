package com.Insurance.controller;

import com.Insurance.entity.Acception;
import com.Insurance.entity.Saler;
import com.Insurance.entity.User;
import com.Insurance.service.AcceptionService;
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
import java.util.List;

@Controller
public class AcceptionController {

    @Autowired
    private AcceptionService service;

    @RequestMapping(value = "/user/getAcception")
    public String getAcception(HttpSession session, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        User user = (User) session.getAttribute("user");
        PageHelper.startPage(pageNum, 2);
        List<Acception> list = service.getAll(user.getId());
        model.addAttribute("AList", list);
        if (list != null) {
            PageInfo pageInfo = new PageInfo<>(list);
            model.addAttribute("page", pageInfo);
        }
        return "user/compensate";
    }

    @RequestMapping(value = "/byCph")
    public String selectByCph(Acception acception, String verifyCode, HttpSession session, Model model, RedirectAttributes attr) {
        String code = session.getAttribute("verifyCode").toString();
        if (!code.equals(verifyCode)) {
            model.addAttribute("applyError", "验证码错误");
            return "user/apply";
        }
        Acception a = service.checkByCph(acception);
        if (a == null) {
            attr.addAttribute("id", acception.getId());
            attr.addAttribute("cph", acception.getCph());
            attr.addAttribute("phone", acception.getPhone());
            return "redirect:/ByCph";
        }
        model.addAttribute("acception", a);
        return "user/addAcception";
    }

    @RequestMapping(value = "/byCno")
    public String selectByCno(Acception acception, String cno, String verifyCode, HttpSession session, Model model, RedirectAttributes attr) {
        String code = session.getAttribute("verifyCode").toString();
        if (!code.equals(verifyCode)) {
            model.addAttribute("applyError", "验证码错误");
            return "user/apply";
        }
        Acception a = service.checkByCno(acception.getId(),cno,acception.getPerson());
        if (a == null) {
            attr.addAttribute("id", acception.getId());
            attr.addAttribute("cno", cno);
            attr.addAttribute("person", acception.getPerson());
            return "redirect:/ByCno";
        }
        model.addAttribute("acception", a);
        return "user/addAcception";
    }

    @RequestMapping(value = "/addByRecord/{id}")
    public String addAcception(@PathVariable("id") String id, HttpServletResponse res) {
        service.addAcception(id);
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = res.getWriter();
            out.print("<script>alert('已提交理赔申请');window.location.href='/toApply';</script>");
            out.flush();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return "redirect:/toApply";
    }

    @RequestMapping(value = "/saler/getAcception")
    public String getAcceptionBySaler(HttpSession session,
                                      Model model,
                                      @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        Saler saler = (Saler) session.getAttribute("saler");
        PageHelper.startPage(pageNum, 6);
        List<Acception> list = service.getBySalerId(saler.getId());
        model.addAttribute("AList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/saler/salerIndex";
    }

    @RequestMapping(value = "/saler/startAcception")
    public String startAcception(String id) {
        service.startAcception(id);
        return "redirect:/saler/getAcception";
    }

    @RequestMapping(value = "/saler/finishAcception")
    public String finishAcception(String id, BigDecimal payout) {
        service.finishAcception(id,payout);
        return "redirect:/saler/getAcception";
    }

    @RequestMapping(value = "/saler/refuseAcception")
    public String refuseAcception(String id) {
        service.refuseAcception(id);
        return "redirect:/saler/getAcception";
    }

    @RequestMapping(value = "/saler/search/{bid}")
    public String search(@PathVariable("bid") String bid, RedirectAttributes attr) {
        attr.addAttribute("bid", bid);
        return "redirect:/saler/search";
    }

    @RequestMapping(value = "/saler/search")
    public String doSearch(@ModelAttribute("bid") String bid, Model model,
                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (bid.equals("")) {
            return "redirect:/saler/getAcception";
        }
        PageHelper.startPage(pageNum, 6);
        List<Acception> list = service.search(bid);
        model.addAttribute("AList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/saler/salerIndex";
    }

    @RequestMapping(value = "/admin/getAcceptionList")
    public String getAcceptionList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        PageHelper.startPage(pageNum, 6);
        List<Acception> list = service.selectAllByAdmin();
        model.addAttribute("AList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/acceptionManaged";
    }

    @RequestMapping(value = "/admin/searchAcception/{aid}")
    public String toSearch(@PathVariable("aid") String aid, RedirectAttributes attr) {
        attr.addAttribute("aid", aid);
        return "redirect:/admin/searchAcception";
    }

    @RequestMapping(value = "/admin/searchAcception")
    public String searchAcception(@ModelAttribute("aid") String aid, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (aid.equals("")) {
            return "redirect:/admin/getAcceptionList";
        }
        PageHelper.startPage(pageNum, 6);
        List<Acception> list = service.search(aid);
        model.addAttribute("AList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/acceptionManaged";
    }

    @RequestMapping(value = "/admin/addAcception")
    @ResponseBody
    public void addAcceptionByAdmin(Acception acception,HttpServletResponse response) throws IOException {
        try {
            service.addByAdmin(acception);
        }catch (Exception e){
            response.setContentType("text/html; charset=UTF-8");
            if (e.toString().contains("NullPointerException")) {
                response.getWriter().write("用户或车辆信息不存在");
            }else {
                if (e.getMessage().contains("Index: 0, Size: 0")) {
                    response.getWriter().write("输入的保险名称不存在");
                }
                response.getWriter().write(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/admin/deleteAcception")
    public String deleteAcception(String id) {
        service.deleteAcception(id);
        return "redirect:/admin/getAcceptionList";
    }

    @RequestMapping(value = "/admin/updateAcception")
    @ResponseBody
    public void updateAcception(Acception acception,HttpServletResponse response) throws IOException {
        try {
            service.updateAcception(acception);
        }catch (Exception e){
            response.setContentType("text/html; charset=UTF-8");
            if (e.toString().contains("NullPointerException")) {
                response.getWriter().write("用户或车辆信息不存在");
            }else {
                if (e.getMessage().contains("Index: 0, Size: 0")) {
                    response.getWriter().write("输入的保险名称不存在");
                }
                response.getWriter().write(e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/admin/rollBackStatus")
    public String backStatus(String id,HttpServletResponse response) {
        if (!service.backStatus(id)){
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print("<script>alert('" + "该状态不需要回退" + "');window.location.href='/admin/getAcceptionList';</script>");
                out.flush();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
        return "redirect:/admin/getAcceptionList";
    }

}
