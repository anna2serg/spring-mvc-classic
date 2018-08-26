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

import ru.homework.common.FilterByName;
import ru.homework.domain.Author;
import ru.homework.dto.AuthorDto;
import ru.homework.exception.NotFoundException;
import ru.homework.repository.AuthorRepository;

@Controller
public class AuthorController {
	private final AuthorRepository repository;
	
    @Autowired
    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }	
    
    @GetMapping("/authors")
    public String listAuthors(Model model, 
    						@ModelAttribute("filter") Optional<FilterByName> filter,
			    	        BindingResult result,
			    	        @RequestParam("page") Optional<Integer> page, 
			    	        @RequestParam("size") Optional<Integer> size) {
 	
    	int currentPage = page.orElse(1);
    	int pageSize = size.orElse(2);

    	FilterByName nameFilter = filter.orElse(null); 
        Page<Author> authorPage = null;
        
    	HashMap<String, String> filters = new HashMap<>();
    	if (nameFilter != null && !nameFilter.getName().equals("")) {
    		filters.put("name", nameFilter.getName());
    	}    	
    	authorPage = repository.findAllByFilters(filters, PageRequest.of(currentPage - 1, pageSize, Sort.by("id").ascending()));

        model.addAttribute("authorPage", authorPage);
        
        int totalPages = authorPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        model.addAttribute("currentPage", currentPage);
        
        return "author_all";
    }    
	
    @GetMapping("/authors/add")
    public String addAuthor(Model model) {
    	AuthorDto authorDto = new AuthorDto();
        model.addAttribute("authorDto", authorDto);
        return "author_add";
    }    
    
    @PostMapping("/authors/add")
    public String saveNewAuthor(@ModelAttribute("authorDto") AuthorDto authorDto,
    						   Model model) {
    	Author author = AuthorDto.toDomainObject(authorDto);   	
        repository.save(author);     
        return "redirect:/authors";        
    }       
    
    @GetMapping("/authors/edit/{id}")
    public String editAuthor(@PathVariable("id") int id, Model model) {
    	Author author = repository.findById(id).orElseThrow(NotFoundException::new);
    	AuthorDto authorDto = AuthorDto.toDto(author);
        model.addAttribute("authorDto", authorDto);
        return "author_edit";
    }    
    
    @PostMapping("/authors/edit/{id}")
    public String saveAuthor(@PathVariable("id") int id,
    						@ModelAttribute("authorDto") AuthorDto authorDto,
						    Model model) {
    	Author updAuthor = AuthorDto.toDomainObject(authorDto);
    	Author author = repository.findById(updAuthor.getId()).orElseThrow(NotFoundException::new);
    	author.setSurname(updAuthor.getSurname());
    	author.setFirstname(updAuthor.getFirstname());
    	author.setMiddlename(updAuthor.getMiddlename());
        repository.save(author);
        
        return "redirect:/authors";        
    }   
   
}
