package ru.homework.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import ru.homework.domain.Book;
import ru.homework.dto.BookDto;
import ru.homework.dto.GenreDto;
import ru.homework.exception.NotFoundException;
import ru.homework.repository.BookRepository;

@Service
public class BookService {
	
	private final BookRepository repository;
	private final AppSettings settings;
	private int fetchSize;	
	
	public BookService(BookRepository repository, AppSettings settings) {
		this.repository = repository;
		this.settings = settings;
		this.fetchSize = this.settings.getFetchsize();		
	}
    public String getBooks(Model model, 
						   @ModelAttribute("filter") Optional<FilterByName> filter,
					       BindingResult result,
					       @RequestParam("page") Optional<Integer> page, 
					       @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(fetchSize);
		
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
			if (currentPage > totalPages) { 
				currentPage = totalPages;
				getBooks(model, filter, result, Optional.ofNullable(currentPage), Optional.ofNullable(pageSize));
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
			.boxed()
			.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		
		model.addAttribute("currentPage", currentPage);
		return "book_all";
	} 
    
    public String addNewBook(Model model) {
    	BookDto bookDto = new BookDto();
    	GenreDto genreDto = new GenreDto();
    	Set<Author> authorsDto = new HashSet<Author>();
    	
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("genreDto", genreDto);
        model.addAttribute("authorsDto", authorsDto);
        
        return "book_add";
    }          
    
    public String saveNewBook(@ModelAttribute("bookDto") BookDto bookDto,
			   				  Model model) {
    	Book book = BookDto.toDomainObject(bookDto);   	
		repository.save(book);     
		
		return "redirect:/books";        
	}
    
    public String editBook(@PathVariable("id") int id,
    					   Model model) {
    	Book book = repository.findById(id).orElseThrow(NotFoundException::new);
    	BookDto bookDto = BookDto.toDto(book);
        model.addAttribute("bookDto", bookDto);
        return "book_edit";
    }  
    
    public String saveBook(@PathVariable("id") int id,
						   @ModelAttribute("bookDto") BookDto bookDto,
						   Model model) {
		Book updBook = BookDto.toDomainObject(bookDto);
		Book book = repository.findById(updBook.getId()).orElseThrow(NotFoundException::new);
		book.setName(updBook.getName()); 
		book.setAuthors(updBook.getAuthors());
		book.setGenre(updBook.getGenre());
		repository.save(book);
		
		return "redirect:/books";        
	}    
}
