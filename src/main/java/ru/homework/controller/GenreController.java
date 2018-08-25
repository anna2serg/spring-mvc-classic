package ru.homework.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.homework.controller.GenreController.Filter;
import ru.homework.domain.Genre;
import ru.homework.exception.NotFoundException;
import ru.homework.repository.GenreRepository;

@Controller
public class GenreController {
	
	public class Filter{
	    private String value = "";
	    public String getValue(){
	        return value;
	    }
	    public void setValue(String value){
	        this.value = value;
	    }
	}	
	
    private static int currentPage = 1;
    private static int pageSize = 2;	
    private static Filter nameFilter;
	private final GenreRepository repository;
	
    @Autowired
    public GenreController(GenreRepository repository) {
        this.repository = repository;
    }	
    
    @GetMapping("/genres")
    public String listGenre(Model model, 
    						@ModelAttribute("filter") Optional<Filter> filter,
			    	        BindingResult result,
			    	        @RequestParam("page") Optional<Integer> page, 
			    	        @RequestParam("size") Optional<Integer> size) {
 	
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);   	
        
        Page<Genre> genrePage = null;
        filter.ifPresent(f -> nameFilter = f);
        
    	HashMap<String, String> filters = new HashMap<>();
    	if (nameFilter == null || nameFilter.getValue().equals("")) {
    		filters.put("name", nameFilter.getValue());
    	}    	
    	genrePage = repository.findAllByFilters(filters, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("genrePage", genrePage);
        
        int totalPages = genrePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        model.addAttribute("currentPage", currentPage);
        
        return "genre_all";
    }    
	
    @GetMapping("/genres/edit/{id}")
    public String editGenre(@PathVariable("id") int id, Model model) {
    	Genre genre = repository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("genre", genre);
        return "genre_edit";
    }    
    
    @PostMapping("/genres/edit/{id}")
    public String saveGenre(@PathVariable("id") int id,
					        @RequestParam("name") String name,
						    Model model) {
    	Genre genre = repository.findById(id).orElseThrow(NotFoundException::new);
    	genre.setName(name);
        repository.save(genre);
        
        return "redirect:/genres";        
    }   
    
}
