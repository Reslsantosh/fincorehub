package com.resustainability.fincorehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/gl/records")
    public String viewGl() {
        return "gl/view-gl";
    }
    
    @GetMapping("/gl/add")
    public String showAddGlPage() {
        return "gl/add-gl";
    }
    
    @GetMapping("/pc/records")
    public String viewPc() {
        return "pc/view-pc";
    }
    
    @GetMapping("/pc/add")
    public String showAddPcPage() {
        return "pc/add-pc";
    }
    
    @GetMapping("/users/list")
    public String listUsers() {
        return "users/list-users";
    }
    
    @GetMapping("/users/add")
    public String addUser() {
        return "users/add-user";
    }
    
    @GetMapping("/revenue/budget/records")
    public String viewRevenueBudget() {
        return "budget/view-revenuebudget";
    }

    @GetMapping("/revenue/budget/add")
    public String showAddRevenueBudgetPage() {
        return "budget/add-revenuebudget";
    }

    @GetMapping("/ebitda/budget/records")
    public String viewEbitdaBudget() {
        return "budget/view-ebitdabudget";
    }

    @GetMapping("/ebitda/budget/add")
    public String showAddEbitdaBudgetPage() {
        return "budget/add-ebitdabudget";
    }
}
