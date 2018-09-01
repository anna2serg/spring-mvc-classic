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
import ru.homework.dto.GenreDto;
import ru.homework.service.GenreService;

@Controller
public class GenreController {
	
	private GenreService service;
	
    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }	
    
    @GetMapping("/genres")
    public String getGenres(Model model, 
    						@ModelAttribute("filter") Optional<FilterByName> filter,
			    	        BindingResult result,
			    	        @RequestParam("page") Optional<Integer> page, 
			    	        @RequestParam("size") Optional<Integer> size) {
    	return service.getGenres(model, filter, result, page, size);  	
    }    
	
    @GetMapping("/genres/add")
    public String addGenre(Model model) {
        return service.addGenre(model);
    }    
    
    @PostMapping("/genres/add")
    public String saveNewGenre(@ModelAttribute("genreDto") GenreDto genreDto,
    						   Model model) {
        return service.saveNewGenre(genreDto, model);        
    }       
    
    @GetMapping("/genres/{id}")
    public String editGenre(@PathVariable("id") int id, 
    						Model model) {
        return service.editGenre(id, model);
    }    
    
    @PostMapping("/genres/{id}")
    public String saveGenre(@PathVariable("id") int id,
    						@ModelAttribute("genreDto") GenreDto genreDto,
						    Model model) {
        return service.saveGenre(id, genreDto, model);        
    }   
    
}
