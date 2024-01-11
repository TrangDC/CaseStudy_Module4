package com.example.case_study_m4.controller;

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
@RequestMapping("/games")
public class GameController {
    @Autowired
    private IGameService gameService;

    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> listCategories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView listGames(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");

        PageRequest pageable = PageRequest.of(page, 5);
        Page<Game> games = gameService.findAll(pageable);

        modelAndView.addObject("games", games);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/admin/games/form");
        modelAndView.addObject("game", new Game());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveGame(@ModelAttribute("products") Game game) {
        gameService.save(game);
        ModelAndView modelAndView = new ModelAndView("/admin/games/form");
        modelAndView.addObject("game", new Game());
        modelAndView.addObject("message", "New game created successfully");
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Game> game = gameService.findById(id);
        if (game.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/admin/games/form");
            modelAndView.addObject("game", game.get());
            return modelAndView;
        } else {
            return new ModelAndView("/error_404");
        }
    }

    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id,
                                       @ModelAttribute("product") Game game) {
        Optional<Game> detail = gameService.findById(id);
        if(detail != null){
            game.setId(id);
            gameService.save(game);
        }
        return "redirect:/games";
    }
    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
           gameService.remove(id);
           return "redirect:/games";
    }
    @GetMapping("/search")
    public ModelAndView searchGame(@RequestParam("keyword") String keyword,
                               @PageableDefault(size = 4) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");
        Page<Game> games = gameService.searchByWord(keyword, pageable);
        modelAndView.addObject("games", games);
        modelAndView.addObject("keyword", keyword);
        return modelAndView;
    }

    @GetMapping("/filter")
    public ModelAndView filterGamesByCategory(@RequestParam("id") Long id,
                                              @PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");

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

}
