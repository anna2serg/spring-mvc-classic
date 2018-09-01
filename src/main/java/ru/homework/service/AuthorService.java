package ru.homework.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.homework.common.FilterByName;
import ru.homework.configuration.AppSettings;
import ru.homework.domain.Author;
import ru.homework.dto.AuthorDto;
import ru.homework.exception.NotFoundException;
import ru.homework.repository.AuthorRepository;

@Service
public class AuthorService {
	
	private final AuthorRepository repository;
	private final AppSettings settings;
	private int fetchSize;	
	
	public AuthorService(AuthorRepository repository, AppSettings settings) {
		this.repository = repository;
		this.settings = settings;
		this.fetchSize = this.settings.getFetchsize();
	}
	
    public String getAuthors(Model model, 
			  	  @ModelAttribute("filter") Optional<FilterByName> filter,
		          BindingResult result,
		          @RequestParam("page") Optional<Integer> page, 
		          @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(fetchSize);
		
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
    
    public String addAuthor(Model model) {
    	AuthorDto authorDto = new AuthorDto();
        model.addAttribute("authorDto", authorDto);
        return "author_add";
    }   
    
    public String saveNewAuthor(@ModelAttribute("authorDto") AuthorDto authorDto,
			   					Model model) {
		Author author = AuthorDto.toDomainObject(authorDto);   	
		repository.save(author);     
		return "redirect:/authors";        
	}           

    public String editAuthor(@PathVariable("id") int id, Model model) {
    	Author author = repository.findById(id).orElseThrow(NotFoundException::new);
    	AuthorDto authorDto = AuthorDto.toDto(author);
        model.addAttribute("authorDto", authorDto);
        return "author_edit";
    }       
 
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
