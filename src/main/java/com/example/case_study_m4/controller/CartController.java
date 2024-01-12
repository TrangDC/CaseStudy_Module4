package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shopping-cart")
@SessionAttributes("cart")
public class CartController {

    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }

    @GetMapping("")
    public ModelAndView showCart(@SessionAttribute("cart") Cart cart) {
        ModelAndView modelAndView = new ModelAndView("website/shopping_cart/list");
        modelAndView.addObject("cart", cart);
        return modelAndView;
    }


}
