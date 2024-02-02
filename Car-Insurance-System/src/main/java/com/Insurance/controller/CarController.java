package com.Insurance.controller;

import com.Insurance.entity.Car;
import com.Insurance.entity.User;
import com.Insurance.service.CarService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@Controller
public class CarController {

    @Autowired
    private CarService service;

    @PostMapping(value = "/user/addCar")
    @ResponseBody
    public void addCar(HttpSession session, HttpServletResponse response,
                       @RequestParam("cimg") MultipartFile cimg, String cname, String cph, String ctype) throws IOException {
        StringBuilder s = null;
        try {
            User user = (User) session.getAttribute("user");
            Car car = new Car();
            car.setCname(cname);
            car.setCph(cph);
            car.setCtype(ctype);
            car.setUid(user.getId());
            String localPath = "/img/car/";
            car.setCimg(localPath + cimg.getOriginalFilename());
            service.insertCar(car);
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\car\\";
            File file = new File(path, Objects.requireNonNull(cimg.getOriginalFilename()));
            cimg.transferTo(file);
            response.setContentType("text/html; charset=UTF-8");
            s = new StringBuilder("请等待管理员审核");
            response.getWriter().write(String.valueOf(s));
        } catch (Exception e) {
            if (e.getMessage().contains("PRIMARY")) {
                s = new StringBuilder("PRIMARY");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(String.valueOf(s));
        }
    }

    @RequestMapping(value = "/user/toPersonal")
    public String toPersonal(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Car> carList = service.NotAudited(user);
        List<Car> List = service.AuditedFailed(user);
        model.addAttribute("carList", carList);
        model.addAttribute("List", List);
        return "user/personal";
    }

    @RequestMapping(value = "/user/getUserCarList")
    public String getUserCarList(Model model, HttpSession session, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        User user = (User) session.getAttribute("user");
        PageHelper.startPage(pageNum, 1);
        List<Car> CList = service.getListByUserId(user);
        model.addAttribute("CList", CList);
        if (CList != null) {
            PageInfo pageInfo = new PageInfo<>(CList);
            model.addAttribute("page", pageInfo);
        }
        return "user/CarAction";
    }

    @RequestMapping(value = "/saler/toUserList")
    public String toUserList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        List<Car> list = service.selectCarAndUser();
        model.addAttribute("CList", list);
        PageHelper.startPage(pageNum, 6);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/saler/userList";
    }

    @RequestMapping(value = "/saler/searchCph/{cph}")
    public String search(@PathVariable("cph") String cph, RedirectAttributes attr) {
        attr.addAttribute("cph", cph);
        return "redirect:/saler/searchCph";
    }

    @RequestMapping(value = "/saler/searchCph")
    public String searchCph(@ModelAttribute("cph") String cph, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (cph.equals("")) {
            return "redirect:/saler/toUserList";
        }
        List<Car> list = service.searchCph(cph);
        model.addAttribute("CList", list);
        PageHelper.startPage(pageNum, 6);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/saler/userList";
    }

    @RequestMapping(value = "/admin/getCarList")
    public String getCarList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        PageHelper.startPage(pageNum, 6);
        List<Car> list = service.selectAll();
        model.addAttribute("CList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/carManaged";
    }

    @RequestMapping(value = "/admin/statusSelect/{status}")
    public String statusSelect(@PathVariable("status") String status, HttpServletResponse res, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (status.equals("全部")) {
            return "redirect:/admin/getCarList";
        }
        PageHelper.startPage(pageNum, 6);
        List<Car> list = service.selectByStatus(status);
        model.addAttribute("CList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        model.addAttribute("status", status);
        model.addAttribute("selected", "yes");
        return "insiders/manager/carManaged";
    }

    @RequestMapping(value = "/admin/refuseCar")
    public String refuseCar(String cph,String ctype) {
        service.refuseCar(cph,ctype);
        return "redirect:/admin/getCarList";
    }

    @RequestMapping(value = "/admin/passCar")
    public String passCar(String cph,String ctype) {
        service.passCar(cph,ctype);
        return "redirect:/admin/getCarList";
    }

    @RequestMapping(value = "/admin/backStatus")
    public String backStatus(String cph, String status,String ctype) {
        service.backStatus(cph, status,ctype);
        return "redirect:/admin/getCarList";
    }

    @RequestMapping(value = "/admin/deleteCar")
    public String deleteCar(String cph,String ctype) {
        service.deleteCar(cph,ctype);
        return "redirect:/admin/getCarList";
    }

    @RequestMapping(value = "/admin/searchCar/{cph}")
    public String toSearchCar(@PathVariable("cph") String cph, RedirectAttributes attr) {
        attr.addAttribute("cph", cph);
        return "redirect:/admin/searchCar";
    }

    @RequestMapping(value = "/admin/searchCar")
    public String searchCar(@ModelAttribute("cph") String cph, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (cph.equals("")) {
            return "redirect:/admin/getCarList";
        }
        PageHelper.startPage(pageNum, 6);
        List<Car> list = service.selectByCph(cph);
        model.addAttribute("CList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/carManaged";
    }
}
