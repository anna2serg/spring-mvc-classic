package ru.homework.dto;

import java.util.HashSet;
import java.util.Set;

import ru.homework.domain.Author;
import ru.homework.domain.Book;
import ru.homework.domain.Comment;
import ru.homework.domain.Genre;

public class BookDto {
    private int id;
    private String name;
    private Set<Author> authors = new HashSet<Author>();
    private Genre genre;
    private Set<Comment> comments = new HashSet<Comment>();
  
    public BookDto() {
    	super();
    }       
    
    public BookDto(String name, Set<Author> authors, Genre genre) {
        this(0, name, authors, genre);
    }            
 
    public BookDto(int id, String name, Set<Author> authors, Genre genre) {
        super();
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.genre = genre;
    }        
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
    	this.authors = authors;
    } 
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }      
    
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }    
    
    public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
    public static Book toDomainObject(BookDto dto) {
        return new Book(dto.getId(), dto.getName(), dto.getAuthors(), dto.getGenre());
    }
    
    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getName(), book.getAuthors(), book.getGenre());
    }   
}
