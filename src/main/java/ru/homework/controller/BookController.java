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
import org.springframework.web.bind.annotation.RequestParam;

import ru.homework.common.FilterByName;
import ru.homework.domain.Book;
import ru.homework.repository.BookRepository;

@Controller
public class BookController {
	private final BookRepository repository;
	
    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }	

    @GetMapping("/books")
    public String listBooks(Model model, 
    						@ModelAttribute("filter") Optional<FilterByName> filter,
			    	        BindingResult result,
			    	        @RequestParam("page") Optional<Integer> page, 
			    	        @RequestParam("size") Optional<Integer> size) {
 	
    	int currentPage = page.orElse(1);
    	int pageSize = size.orElse(2);

    	FilterByName nameFilter = filter.orElse(null); 
        Page<Book> bookPage = null;
        
    	HashMap<String, String> filters = new HashMap<>();
    	if (nameFilter != null && !nameFilter.getName().equals("")) {
    		filters.put("name", nameFilter.getName());
    	}    	
    	bookPage = repository.findAllByFilters(filters, PageRequest.of(currentPage - 1, pageSize, Sort.by("id").ascending()));

        model.addAttribute("bookPage", bookPage);
        
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        model.addAttribute("currentPage", currentPage);
        
        return "book_all";
    }  
    
}
