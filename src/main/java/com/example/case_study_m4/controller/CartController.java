package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Cart;
import com.example.case_study_m4.model.Game;
import com.example.case_study_m4.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/shopping-cart")
@SessionAttributes("cart")
public class CartController {

    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }

    @Autowired
    private IGameService gameService;

    @GetMapping("")
    public ModelAndView showCart(@SessionAttribute("cart") Cart cart) {
        ModelAndView modelAndView = new ModelAndView("website/shopping_cart/list");
        modelAndView.addObject("cart", cart);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteFromCart(@PathVariable Long id,
                                 @ModelAttribute Cart cart) {
        Optional<Game> gameOptional = gameService.findById(id);
        if (!gameOptional.isPresent()) {
            return "/error_404";
        } else {
            cart.deleteProductFromCart(id);
            return "redirect:/shopping-cart";
        }
    }

    @GetMapping("/deleteAll")
    public String deleteFromCart(@ModelAttribute Cart cart) {
        cart.deleteAllFromCart();
        return "redirect:/shopping-cart";
    }
}
