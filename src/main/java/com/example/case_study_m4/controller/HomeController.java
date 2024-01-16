package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.*;
import com.example.case_study_m4.repository.IUserRepository;
import com.example.case_study_m4.service.ICategoryService;
import com.example.case_study_m4.service.IGameService;
import com.example.case_study_m4.service.IOrderService;
import com.example.case_study_m4.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
@SessionAttributes("cart")

public class HomeController {
    @Autowired
    private IGameService gameService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IOrderService iOrderService;
    @ModelAttribute("categories")
    public Iterable<Category> listCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute("cart")
    public Cart setUpCart() {
        return new Cart();
    }

    @GetMapping
    public ModelAndView listGames(@RequestParam(defaultValue = "0") int page,
                                  Principal principal) {
        ModelAndView modelAndView = new ModelAndView("website/home/main");

        if (principal != null) {
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            modelAndView.addObject("user", user);
        }

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
    public ModelAndView filterGamesByCategory(@PathVariable Long id,
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


    @GetMapping("/gameDetail/{id}")
    public ModelAndView showGameDetail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("website/home/gameDetail");

        Optional<Game> gameOptional = gameService.findById(id);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            modelAndView.addObject("game", game);
        } else {
            return new ModelAndView("/error_404");
        }

        return modelAndView;
    }

    // Hiển thị 1 thằng users
    @GetMapping("/users/{id}")
    public String showUser(@PathVariable long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        List<Order> orders = iOrderService.findByUserId(id);
        model.addAttribute("orders", orders);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("message", "Users not found");
        }

        return "/website/home/user_infor";

    }
}
