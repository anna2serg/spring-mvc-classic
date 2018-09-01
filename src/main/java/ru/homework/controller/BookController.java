package ru.homework.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.homework.common.FilterByName;
import ru.homework.dto.BookDto;
import ru.homework.service.BookService;

@Controller
public class BookController {
	private final BookService service;
	
    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }	

    @GetMapping("/books")
    public String getBooks(Model model, 
    					   @ModelAttribute("filter") Optional<FilterByName> filter,
			    	       BindingResult result,
			    	       @RequestParam("page") Optional<Integer> page, 
			    	       @RequestParam("size") Optional<Integer> size) {
        return service.getBooks(model, filter, result, page, size);
    } 
    
    @GetMapping("/books/{id}")
    public String editBook(@PathVariable("id") int id, 
    					   Model model) {
        return service.editBook(id, model);
    }   
    
    @PostMapping("/books/{id}")
    public String saveBook(@PathVariable("id") int id,
    					   @ModelAttribute("bookDto") BookDto bookDto,
						   Model model) {
        return service.saveBook(id, bookDto, model);        
    }     
    
}
