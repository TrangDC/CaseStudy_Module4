package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Cart;
import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.Game;
import com.example.case_study_m4.service.ICategoryService;
import com.example.case_study_m4.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/home")
@SessionAttributes("cart")
public class HomeController {
    @Autowired
    private IGameService gameService;

    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> listCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute("cart")
    public Cart setUpCart() {
        return new Cart();
    }

    @GetMapping
    public ModelAndView listGames(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("website/home/main");

        PageRequest pageable = PageRequest.of(page, 8);
        Page<Game> games = gameService.findAll(pageable);

        modelAndView.addObject("games", games);
        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView searchGame(@RequestParam("keyword") String keyword,
                                   @PageableDefault(size = 8) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("website/home/main");
        Page<Game> games = gameService.searchByWord(keyword, pageable);
        modelAndView.addObject("games", games);
        modelAndView.addObject("keyword", keyword);
        return modelAndView;
    }

    @GetMapping("/filter")
    public ModelAndView filterGamesByCategory(@RequestParam("id") Long id,
                                              @PageableDefault(size = 8) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("website/home/main");

        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            Page<Game> games = gameService.findByCategory(category.get(), pageable);
            modelAndView.addObject("games", games);
            modelAndView.addObject("selectedCategory", category.get());
        } else {
            Page<Game> games = gameService.findAll(pageable);
            modelAndView.addObject("games", games);
        }
        return modelAndView;
    }


    // thêm vào giỏ hàng
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id,
                            @ModelAttribute Cart cart,
                            @RequestParam("action") String action) {
        Optional<Game> gameOptional = gameService.findById(id);

        if(!gameOptional.isPresent()) {
            return "/error_404";
        }
        if (action.equals("show")) {
            cart.addProduct(gameOptional.get());
            return "redirect:/shopping-cart";
        }
        cart.addProduct(gameOptional.get());
        return "redirect:/home";
    }

    // xóa số lượng 1 sản phẩm trong trang shopping cart
    @GetMapping("/sub/{id}")
    public String subFromCart(@PathVariable Long id,
                              @ModelAttribute Cart cart,
                              @RequestParam("action") String action) {
        Optional<Game> gameOptional = gameService.findById(id);
        if(!gameOptional.isPresent()) {
            return "/error_404";
        }
        if (action.equals("show")) {
            cart.subProduct(gameOptional.get());
            return "redirect:/shopping-cart";
        }
        cart.subProduct(gameOptional.get());
        return "redirect:/api/products";
    }
}
