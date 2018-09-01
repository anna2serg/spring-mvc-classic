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
import ru.homework.dto.AuthorDto;
import ru.homework.service.AuthorService;

@Controller
public class AuthorController {
	
	private AuthorService service;
	
    @Autowired
    public AuthorController(AuthorService service) {
        this.service = service;
    }	
    
    @GetMapping("/authors")
    public String listAuthors(Model model, 
    						  @ModelAttribute("filter") Optional<FilterByName> filter,
			    	          BindingResult result,
			    	          @RequestParam("page") Optional<Integer> page, 
			    	          @RequestParam("size") Optional<Integer> size) {

        return service.getAuthors(model, filter, result, page, size);
    }    
	
    @GetMapping("/authors/add")
    public String addAuthor(Model model) {
        return service.addAuthor(model);
    }    
    
    @PostMapping("/authors/add")
    public String saveNewAuthor(@ModelAttribute("authorDto") AuthorDto authorDto,
    						    Model model) {  
        return service.saveNewAuthor(authorDto, model);        
    }       
    
    @GetMapping("/authors/{id}")
    public String editAuthor(@PathVariable("id") int id, 
    						 Model model) {
        return service.editAuthor(id, model);
    }    
    
    @PostMapping("/authors/{id}")
    public String saveAuthor(@PathVariable("id") int id,
    						 @ModelAttribute("authorDto") AuthorDto authorDto,
						     Model model) {    
        return service.saveAuthor(id, authorDto, model);        
    }   
   
}
