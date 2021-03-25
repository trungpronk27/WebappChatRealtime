package com.messages.controller;

import com.messages.entity.Employee;
import com.messages.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/search")
    public String search(@RequestParam("term") String term, Model model){
        if (StringUtils.isEmpty(term)) {
            return "redirect:/home";
        }
        model.addAttribute("listEmp", employeeService.searchEmployee(term));
        return "employee/home";
    }

    @GetMapping("/employee")
    public String view(Model model){
        model.addAttribute("listEmp", employeeService.getAllEmployee());
        return "employee/home";
    }

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model){
        Employee employee  = new Employee();
        model.addAttribute("employee", employee);
        return "employee/add";
    }

    @PostMapping("/employee")
    public String save(@ModelAttribute("employee") Employee employee, RedirectAttributes redirect){
        employeeService.saveEmp(employee);
        redirect.addFlashAttribute("successMessage", "Saved contact successfully!");
        return "redirect:/employee";
    }

    @GetMapping("/showFormUpdate/{id}")
    public String showFormUpdate(@PathVariable(value = "id") long id, Model model){
        Employee employee  = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee/update";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id, RedirectAttributes redirect){
        this.employeeService.deleteEmpById(id);
        redirect.addFlashAttribute("successMessage", "Deleted contact successfully!");
        return "redirect:/employee";
    }

}
