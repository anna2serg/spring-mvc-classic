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
import ru.homework.domain.Book;
import ru.homework.domain.Genre;
import ru.homework.dto.BookDto;
import ru.homework.dto.GenreDto;
import ru.homework.exception.NotFoundException;
import ru.homework.repository.BookRepository;
import ru.homework.repository.GenreRepository;

@Service
public class GenreService {
	
	private final GenreRepository genreRepository;
	private final BookRepository bookRepository;
	private final AppSettings settings;
	private int fetchSize;
	
	public GenreService(GenreRepository genreRepository, BookRepository bookRepository, AppSettings settings) {
		this.genreRepository = genreRepository;
		this.bookRepository = bookRepository;
		this.settings = settings;
		this.fetchSize = this.settings.getFetchsize();
	}
	
    public String getGenres(Model model, 
							@ModelAttribute("filter") Optional<FilterByName> filter,
					        BindingResult result,
					        @RequestParam("book") Optional<Integer> book,
					        @RequestParam("page") Optional<Integer> page, 
					        @RequestParam("size") Optional<Integer> size) {
    	
    	int currentPage = page.orElse(1);
    	int pageSize = size.orElse(fetchSize);

    	FilterByName nameFilter = filter.orElse(null); 
        Page<Genre> genrePage = null;
        
    	HashMap<String, String> filters = new HashMap<>();
    	if (nameFilter != null && !nameFilter.getName().equals("")) {
    		filters.put("name", nameFilter.getName());
    	}    	
    	genrePage = genreRepository.findAllByFilters(filters, PageRequest.of(currentPage - 1, pageSize, Sort.by("id").ascending()));

        model.addAttribute("genrePage", genrePage);
        
        int totalPages = genrePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
        model.addAttribute("currentPage", currentPage);
        
        int bookId = book.orElse(-1);
        if (bookId == -1) {
            return "genre_all";       	
        } else {
        	Book bookById = null;
        	BookDto bookDto = null;
        	if (bookId != 0) {
            	bookById = bookRepository.findById(bookId).orElseThrow(NotFoundException::new); 
            	bookDto = BookDto.toDto(bookById);
        	} else {
        		bookDto = new BookDto();
        	}
        	
            model.addAttribute("bookDto", bookDto);              	
            return "genre_book";
        } 	
    	
    }
    
    public String addNewGenre(Model model) {
    	GenreDto genreDto = new GenreDto();
        model.addAttribute("genreDto", genreDto);
        
        return "genre_add";
    }      
    
    public String saveNewGenre(@ModelAttribute("genreDto") GenreDto genreDto,
			   				   Model model) {
		Genre genre = GenreDto.toDomainObject(genreDto);   	
		genreRepository.save(genre);     
		
		return "redirect:/genres";        
	}    
    
    public String editGenre(@PathVariable("id") int id, 
    						Model model) {
    	Genre genre = genreRepository.findById(id).orElseThrow(NotFoundException::new);
    	GenreDto genreDto = GenreDto.toDto(genre);
        model.addAttribute("genreDto", genreDto);
        
        return "genre_edit";
    }    
    
    public String saveGenre(@PathVariable("id") int id,
							@ModelAttribute("genreDto") GenreDto genreDto,
						    Model model) {
		Genre updGenre = GenreDto.toDomainObject(genreDto);
		Genre genre = genreRepository.findById(updGenre.getId()).orElseThrow(NotFoundException::new);
		genre.setName(updGenre.getName());
		genreRepository.save(genre);
		
		return "redirect:/genres";        
	}       
    
}
