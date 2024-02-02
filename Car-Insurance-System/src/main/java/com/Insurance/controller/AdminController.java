package com.Insurance.controller;

import com.Insurance.entity.Admin;
import com.Insurance.service.AdminService;
import com.Insurance.tools.Month;
import com.Insurance.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService service;

    @PostMapping(value = "/adminDoLogin")
    public String doLogin(Admin admin, String verifyCode, Model model, HttpSession session) {
        String code = session.getAttribute("verifyCode").toString();
        if (!code.equals(verifyCode)) {
            model.addAttribute("errorMsg", "验证码错误");
            return "insiders/insidersLogin";
        }
        Admin LoginAdmin = service.doLogin(admin);
        if (LoginAdmin == null) {
            model.addAttribute("errorMsg", "请检查输入的账号密码");
            return "insiders/insidersLogin";
        }
        session.setAttribute("admin", LoginAdmin);
        return "redirect:/admin/getDataPicture";
    }

    @RequestMapping(value = "/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/toLogin";
    }

    @RequestMapping(value = "/admin/myself")
    public String toMyself() {
        return "insiders/manager/myselfManaged";
    }

    @PostMapping(value = "/admin/updateAdmin")
    public String updateAdmin(Admin admin, HttpSession session) {
        service.updateAdmin(admin);
        Admin logAdmin = service.selectById(admin.getId());
        session.removeAttribute("admin");
        session.setAttribute("admin", logAdmin);
        return "redirect:/admin/myself";
    }

    @PostMapping(value = "/admin/deleteAdmin")
    public String deleteAdmin(Integer id, HttpSession session) {
        service.deleteAdmin(id);
        session.removeAttribute("admin");
        return "redirect:/admin/toLogin";
    }
}
