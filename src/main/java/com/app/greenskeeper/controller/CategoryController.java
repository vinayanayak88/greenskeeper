//package com.app.greenskeeper.controller;
//
//import java.util.List;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.app.greenskeeper.domain.Category;
//import com.app.greenskeeper.service.CategoryService;
//
//import lombok.AllArgsConstructor;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping(value = "/plants/categories")
//@AllArgsConstructor
//@Slf4j
//public class CategoryController {
//
//    @NonNull
//    private CategoryService categoryService;
//
//    @GetMapping
//    public List<Category> getAllCategory(){
//        return categoryService.getAllCategory();
//    }
//
//    @PostMapping
//    public Category addCategory(@RequestBody Category category){
//        return categoryService.addCategory(category);
//    }
//}
