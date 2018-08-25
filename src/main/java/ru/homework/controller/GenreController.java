package ru.homework.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.homework.domain.Genre;
import ru.homework.dto.GenreDto;
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
 	
    	int currentPage = page.orElse(1);
    	int pageSize = size.orElse(2);

    	Filter nameFilter = filter.orElse(null); 
        Page<Genre> genrePage = null;
        
    	HashMap<String, String> filters = new HashMap<>();
    	if (nameFilter != null && !nameFilter.getValue().equals("")) {
    		filters.put("name", nameFilter.getValue());
    	}    	
    	genrePage = repository.findAllByFilters(filters, PageRequest.of(currentPage - 1, pageSize, Sort.by("id").ascending()));

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
	
    @GetMapping("/genres/add")
    public String addGenre(Model model) {
    	GenreDto genreDto = new GenreDto();
        model.addAttribute("genreDto", genreDto);
        return "genre_add";
    }    
    
    @PostMapping("/genres/add")
    public String saveNewGenre(@ModelAttribute("genreDto") GenreDto genreDto,
    						   Model model) {
    	Genre genre = GenreDto.toDomainObject(genreDto);   	
        repository.save(genre);     
        return "redirect:/genres";        
    }       
    
    @GetMapping("/genres/edit/{id}")
    public String editGenre(@PathVariable("id") int id, Model model) {
    	Genre genre = repository.findById(id).orElseThrow(NotFoundException::new);
    	GenreDto genreDto = GenreDto.toDto(genre);
        model.addAttribute("genreDto", genreDto);
        return "genre_edit";
    }    
    
    @PostMapping("/genres/edit/{id}")
    public String saveGenre(@PathVariable("id") int id,
    						@ModelAttribute("genreDto") GenreDto genreDto,
						    Model model) {
    	Genre updGenre = GenreDto.toDomainObject(genreDto);
    	Genre genre = repository.findById(updGenre.getId()).orElseThrow(NotFoundException::new);
    	genre.setName(updGenre.getName());
        repository.save(genre);
        
        return "redirect:/genres";        
    }   
    
}
