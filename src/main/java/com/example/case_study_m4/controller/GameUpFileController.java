package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.GameUpFile;
import com.example.case_study_m4.service.ICategoryService;
import com.example.case_study_m4.service.IGameUpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("admin")
public class GameUpFileController {
    @Autowired
    private IGameUpFileService gameUpFileService;

    @Autowired
    private ICategoryService categoryService;

    @Value("$={file-upload}")
    private String upload;

    @ModelAttribute("categories")
    public Iterable<Category> listCategories() {
        return categoryService.findAll();
    }


    @GetMapping
    public ModelAndView findAll(@PageableDefault(size = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");
        modelAndView.addObject("products", gameUpFileService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showForm() {
        ModelAndView modelAndView = new ModelAndView("/admin/games/form");
        modelAndView.addObject("products", new GameUpFileController());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/form");
        modelAndView.addObject("products", gameUpFileService.findById(id));
        return modelAndView;
    }

    @PostMapping("/home")
    public String save(@ModelAttribute GameUpFile product) {
        MultipartFile file = product.getFile();
        if (file.getSize() != 0) {
            String fileName = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
                product.setImage(fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (product.getId() == null) {
            product.setImage("No image.jpg");
        } else {
            product.setImage(gameUpFileService.findById(product.getId()).getImage());
        }
        System.out.println(file.getOriginalFilename());
        gameUpFileService.save(product);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        gameUpFileService.remove(id);
        return "redirect:/admin";
    }
}
