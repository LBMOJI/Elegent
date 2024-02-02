package com.Insurance.controller;

import com.Insurance.entity.User;
import com.Insurance.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String StartUp() {
        return "index";
    }

    @RequestMapping(value = "/userToLogin")
    public String toLogin() {
        return "user/login";
    }

    @PostMapping(value = "/userDoLogin")
    public String login(HttpSession session, String phone, String password, String verifyCode, Model model) {
        User user = userService.VerifyUserInLogin(phone, password);
        String code = session.getAttribute("verifyCode").toString();
        if (user == null) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "user/login";
        }
        if (!code.equals(verifyCode)) {
            model.addAttribute("errorMsg", "验证码错误");
            return "user/login";
        }
        session.setAttribute("user", user);
        return "redirect:/index";
    }

    @RequestMapping(value = "/userToRegister")
    public String toRegister() {
        return "user/register";
    }

    @PostMapping(value = "/userRegister")
    public String register(User user, Model model) {
        try {
            userService.addUser(user);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                String[] split = e.getCause().getMessage().split("_");
                String errorMsg = split[1];
                switch (errorMsg) {
                    case "phone":
                        model.addAttribute("errorMsg", "该手机号已被注册");
                        break;
                    case "cno":
                        model.addAttribute("errorMsg", "该身份证号已被注册");
                        break;
                }
            }
            return "user/register";
        }
        return "user/login";
    }

    @RequestMapping(value = "/forget")
    public String toForget() {
        return "user/forgetPassword";
    }

    @PostMapping(value = "/toFindPswd")
    public String toFindPswd(String phone, Model model, HttpSession session) {
        User user = userService.selectByPhone(phone);
        if (user != null) {
            session.setAttribute("resetUser", user);
            return "user/resetPassword";
        }
        model.addAttribute("errorMsg", "未找到该手机号，请检查后重新输入");
        return "user/forgetPassword";
    }

    @PostMapping(value = "/resetPswd")
    public String resetPswd(String pwd, HttpSession session) {
        User user = (User) session.getAttribute("resetUser");
        user.setPwd(pwd);
        userService.update(user);
        return "user/login";
    }

    @RequestMapping(value = "/user/LogOut")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }

    @RequestMapping(value = "/user/getInformation")
    public String getInformation() {
        return "user/getInformation";
    }

    @RequestMapping(value = "/user/revise")
    public void revise(User user, HttpSession session,HttpServletResponse res) throws IOException {
        StringBuilder s = null;
        try {
            User logUser = (User) session.getAttribute("user");
            user.setId(logUser.getId());
            userService.update(user);
            User NewUser = userService.selectById(logUser.getId());
            session.removeAttribute("user");
            session.setAttribute("user", NewUser);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                s = getStringBuilder(s, e);
            }
            res.setContentType("text/html; charset=UTF-8");
            res.getWriter().write(String.valueOf(s));
        }
    }

    @RequestMapping(value = "/toApply")
    public String toApply() {
        return "user/apply";
    }

    @RequestMapping(value = "/user/cancellate")
    public String cancellate(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.userDelete(user);
        session.removeAttribute("user");
        return "index";
    }

    @RequestMapping(value = "/admin/getUserList")
    public String getUserList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<User> users = userService.selectAll();
        model.addAttribute("userList", users);
        PageInfo pageInfo = new PageInfo<>(users);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/adminIndex";
    }

    @RequestMapping(value = "/admin/searchUser/{uid}")
    public String searchUserByUid(@PathVariable("uid") String uid, RedirectAttributes attr) {
        attr.addAttribute("uid", uid);
        return "redirect:/admin/searchUser";
    }

    @RequestMapping(value = "/admin/searchUser")
    public String search(@ModelAttribute("uid") String uid, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (uid.equals("")) {
            return "redirect:/admin/getUserList";
        }
        Integer integer = Integer.valueOf(uid);
        PageHelper.startPage(pageNum, 10);
        List<User> list = userService.selectListById(integer);
        model.addAttribute("userList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/adminIndex";
    }

    @PostMapping(value = "/admin/addUser")
    @ResponseBody
    public void addUser(User user, HttpServletResponse res) throws IOException {
        StringBuilder s = null;
        try {
            userService.addUser(user);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                if (e.getMessage().contains("PRIMARY")) {
                    s = new StringBuilder("id重复");
                }else {
                    s = getStringBuilder(s, e);
                }
            }
            res.setContentType("text/html; charset=UTF-8");
            res.getWriter().write(String.valueOf(s));
        }
    }

    @PostMapping(value = "/admin/deleteUser")
    public String deleteUser(String userid) {
        Integer id = Integer.parseInt(userid);
        userService.deleteById(id);
        return "redirect:/admin/getUserList";
    }

    @RequestMapping(value = "/admin/reviseUser")
    public void reviseUser(User user,HttpServletResponse res) throws IOException {
        StringBuilder s = null;
        res.setContentType("text/html; charset=UTF-8");
        try {
            userService.update(user);
            s = new StringBuilder("");
            res.getWriter().write(String.valueOf(s));
        }catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                s = getStringBuilder(s, e);
            }
            res.getWriter().write(String.valueOf(s));
        }
    }

    private StringBuilder getStringBuilder(StringBuilder s, Exception e) {
        String[] split = e.getCause().getMessage().split("_");
        String errorMsg = split[1];
        switch (errorMsg) {
            case "phone":
                s = new StringBuilder("该手机号已被注册");
                break;
            case "cno":
                s = new StringBuilder("该身份证号已被注册");
                break;
        }
        return s;
    }
}
