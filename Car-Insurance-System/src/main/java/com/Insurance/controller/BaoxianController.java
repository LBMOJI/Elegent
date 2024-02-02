package com.Insurance.controller;

import com.Insurance.entity.Baoxian;
import com.Insurance.service.BaoxianService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BaoxianController {

    @Autowired
    private BaoxianService baoxianService;

    @RequestMapping(value = "/getBaoXian")
    public String getBaoXian(Model model, @ModelAttribute("cphError") String cphError) {
        List<Baoxian> list = baoxianService.getAllBaoXian();
        model.addAttribute("BList", list);
        if (!cphError.equals("")) {
            model.addAttribute("cphError", cphError);
        }
        return "user/baoxian";
    }

    @RequestMapping(value = "/user/toAddRecord")
    public String toAddRecord(Integer id,Model model){
        Baoxian baoxian = baoxianService.selectById(id);
        model.addAttribute("baoxian",baoxian);
        return "user/addRecord";
    }

    @RequestMapping(value = "/admin/getBaoxianList")
    public String getBaoxianList(Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        PageHelper.startPage(pageNum, 4);
        List<Baoxian> list = baoxianService.getAllBaoXian();
        model.addAttribute("BList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/baoxianManaged";
    }

    @RequestMapping(value = "/admin/searchBaoxian/{name}")
    public String toSearch(@PathVariable("name") String name, RedirectAttributes attr) {
        attr.addAttribute("name", name);
        return "redirect:/admin/searchBaoxian";
    }

    @RequestMapping(value = "/admin/searchBaoxian")
    public String searchBaoxian(@ModelAttribute("name") String name, Model model, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        if (name.equals("")) {
            return "redirect:/admin/getBaoxianList";
        }
        PageHelper.startPage(pageNum,4);
        List<Baoxian> list = baoxianService.searchBaoxian(name);
        model.addAttribute("BList", list);
        PageInfo pageInfo = new PageInfo<>(list);
        model.addAttribute("page", pageInfo);
        return "insiders/manager/baoxianManaged";
    }

    @RequestMapping(value = "/admin/deleteBaoxian")
    public String BaoxianDelete(String id) {
        baoxianService.deleteBaoxian(id);
        return "redirect:/admin/getBaoxianList";
    }

    @RequestMapping(value = "/admin/reviseBaoxian")
    public String reviseBaoxian(Baoxian baoxian) {
        baoxianService.updateBaoxian(baoxian);
        return "redirect:/admin/getBaoxianList";
    }

    @RequestMapping(value = "/admin/addBaoxian")
    public String addBaoxian(Baoxian baoxian){
        baoxianService.insertBaoxian(baoxian);
        return "redirect:/admin/getBaoxianList";
    }
}
