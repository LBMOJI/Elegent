package com.Insurance.controller;

import com.Insurance.entity.Saler;
import com.Insurance.service.SalerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SalerController {
    @Autowired
    private SalerService service;

    @RequestMapping(value = "/salerDoLogin")
    public String doLogin(Saler saler, String verifyCode, Model model, HttpSession session){
        String code = session.getAttribute("verifyCode").toString();
        if (!code.equals(verifyCode)){
            model.addAttribute("errorMsg","验证码错误");
            return "insiders/insidersLogin";
        }
        Saler LoginSaler = service.doLogin(saler);
        if (LoginSaler == null){
            model.addAttribute("errorMsg","请检查输入的账号密码");
            return "insiders/insidersLogin";
        }
        session.setAttribute("saler",LoginSaler);
        return "redirect:/saler/getAcception";
    }

    @RequestMapping(value = "/insidersToLogin")
    public String toLogin(){
        return "insiders/insidersLogin";
    }

    @RequestMapping(value = "/saler/logout")
    public String logout(HttpSession session){
        session.removeAttribute("saler");
        return "insiders/insidersLogin";
    }

    @RequestMapping(value = "/saler/toInformation")
    public String toInformation(){
        return "insiders/saler/information";
    }

    @PostMapping(value = "/saler/pwdRevise")
    public String pwdRevise(Saler saler,HttpSession session){
        Saler saler2 = (Saler) session.getAttribute("saler");
        saler.setId(saler2.getId());
        service.pwdRevise(saler);
        Saler saler1 = service.selectById(saler.getId());
        session.removeAttribute("saler");
        session.setAttribute("saler",saler1);
        return "insiders/saler/information";
    }

    @PostMapping(value = "/saler/revise")
    public String revise(Saler saler,HttpSession session){
        Saler saler1 = (Saler) session.getAttribute("saler");
        saler.setId(saler1.getId());
        service.revise(saler);
        Saler saler2 = service.selectById(saler.getId());
        session.removeAttribute("saler");
        session.setAttribute("saler",saler2);
        return "insiders/saler/information";
    }

    @RequestMapping(value = "/admin/getSalerList")
    public String getSalerList(Model model,@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum){
        PageHelper.startPage(pageNum,10);
        List<Saler> list = service.selectAll();
        model.addAttribute("SList",list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page",pageInfo);
        return "insiders/manager/salerManaged";
    }

    @RequestMapping(value = "/admin/searchSaler/{name}")
    public String toSearch(@PathVariable("name") String name, RedirectAttributes attr){
        attr.addAttribute("name",name);
        return "redirect:/admin/searchSaler";
    }

    @RequestMapping(value = "/admin/searchSaler")
    public String doSearchSaler(@ModelAttribute("name")String name, Model model,@RequestParam(name = "pageNum",required = false,defaultValue = "1")int pageNum){
        if (name.equals("")){
            return "redirect:/admin/getSalerList";
        }
        PageHelper.startPage(pageNum,10);
        List<Saler> list = service.selectByName(name);
        model.addAttribute("SList",list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page",pageInfo);
        return "insiders/manager/salerManaged";
    }

    @RequestMapping(value = "/admin/deleteSaler")
    public String deleteSaler(Integer id){
        service.deleteSaler(id);
        return "redirect:/admin/getSalerList";
    }

    @RequestMapping(value = "/admin/updateSaler")
    public String updateSaler(Saler saler){
        service.revise(saler);
        return "redirect:/admin/getSalerList";
    }

    @RequestMapping(value = "/admin/addSaler")
    public String addSaler(Saler saler){
        service.insertSaler(saler);
        return "redirect:/admin/getSalerList";
    }
}
