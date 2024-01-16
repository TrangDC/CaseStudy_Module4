package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.Category;
import com.example.case_study_m4.model.Game;
import com.example.case_study_m4.service.ICategoryService;
import com.example.case_study_m4.service.IGameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/games")
public class GameController {
    @Autowired
    private IGameService gameService;

    @Autowired
    private ICategoryService categoryService;

    @Value("$={file-upload}")
    private String upload;

    @ModelAttribute("categories")
    public Iterable<Category> listCategories() {
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView listGames(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");

        PageRequest pageable = PageRequest.of(page, 8);
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
    public ModelAndView saveGame(@Valid @ModelAttribute("game") Game game,
                                 BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/form");
        MultipartFile file = game.getFile();

        // Kiểm tra xem người dùng có nhập dữ liệu không
        if (game.getName() == null || game.getName().isEmpty()) {
            modelAndView.addObject("message", "Vui lòng nhập tên game");
            return modelAndView;
        }
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi validation, trả về thông báo và không lưu game
            modelAndView.addObject("message", "Có lỗi xảy ra, vui lòng kiểm tra lại thông tin");
            return modelAndView;
        }

        if (file != null && file.getSize() != 0) {
            String fileName = file.getOriginalFilename();
            try {
                FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
                game.setImage(fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (game.getId() == null) {
            game.setImage("No image.jpg");
        } else {
            game.setImage(gameService.findById(game.getId()).get().getImage());
        }


        // Xử lý logic khi không có lỗi
        gameService.save(game);

        // Thiết lập lại trang thêm mới với thông báo thành công và đối tượng game mới
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
                             @Valid @ModelAttribute("game") Game updatedGame,
                             BindingResult bindingResult) {
        Optional<Game> existingGame = gameService.findById(id);
        MultipartFile file = updatedGame.getFile();

        if (existingGame.isPresent()) {
            // Kiểm tra lỗi validation
            if (bindingResult.hasErrors()) {
                // Nếu có lỗi validation, trả về trang form với thông báo lỗi và giữ lại dữ liệu đã nhập
                return "/admin/games/form";
            }

            // Kiểm tra và xử lý ảnh mới
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                try {
                    // Lưu ảnh mới vào thư mục upload
                    FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
                    updatedGame.setImage(fileName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            // Cập nhật thông tin game
            updatedGame.setId(id);
            gameService.save(updatedGame);
        }

        return "redirect:/admin/games";
    }


    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
           gameService.remove(id);
           return "redirect:/admin/games";
    }
    @GetMapping("/search")
    public ModelAndView searchGame(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                   @PageableDefault(size = 8) Pageable pageable,
                                   Model model) {
        ModelAndView modelAndView = new ModelAndView("/admin/games/list");

        Page<Game> games;

        if (keyword != null && !keyword.isEmpty()) {
            games = gameService.searchByWord(keyword, pageable);
        } else {
            games = gameService.findAll(pageable);
        }

        modelAndView.addObject("games", games);
        modelAndView.addObject("keyword", keyword);

        // Truyền thông tin tìm kiếm vào Model để giữ giá trị trên URL
        model.addAttribute("keyword", keyword);

        return modelAndView;
    }


    @GetMapping("/filter")
    public ModelAndView filterGamesByCategory(@RequestParam("id") Long id,
                                              @PageableDefault(size = 8) Pageable pageable) {
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
