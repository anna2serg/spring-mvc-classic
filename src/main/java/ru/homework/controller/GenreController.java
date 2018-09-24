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
    
    /* Запрос списка жанров. Режимы:
     * 1. просто список
     * 2. для книги (задан book)
     * */
    @GetMapping("/genres")
    public String getGenres(Model model, 
    						@ModelAttribute("filter") Optional<FilterByName> filter,
			    	        BindingResult result,
			    	        @RequestParam("book") Optional<Integer> book,
			    	        @RequestParam("page") Optional<Integer> page, 
			    	        @RequestParam("size") Optional<Integer> size) {
    	return service.getGenres(model, filter, result, book, page, size);  	
    }    
	
    
    //Запрос на добавление жанра 
    @GetMapping("/genres/add")
    public String addNewGenre(Model model) {
        return service.addNewGenre(model);
    }    
    
    //Сохранение добавленного жанра
    @PostMapping("/genres/add")
    public String saveNewGenre(@ModelAttribute("genreDto") GenreDto genreDto,
    						   Model model) {
        return service.saveNewGenre(genreDto, model);        
    }       
    
    //Запрос на редактирование жанра 
    @GetMapping("/genres/{id}")
    public String editGenre(@PathVariable("id") int id, 
    						Model model) {
        return service.editGenre(id, model);
    }    
    
    //Сохранение отредактированного жанра
    @PostMapping("/genres/{id}")
    public String saveGenre(@PathVariable("id") int id,
    						@ModelAttribute("genreDto") GenreDto genreDto,
						    Model model) {
        return service.saveGenre(id, genreDto, model);        
    }   
    
}
