package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.Game;
import com.example.case_study_m4.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ModelAndView listGames(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/category/list");

        PageRequest pageable = PageRequest.of(page, 5);
        Page<Category> categories = categoryService.findAll(pageable);

        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

}
