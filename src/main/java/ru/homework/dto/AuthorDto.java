package ru.homework.dto;

import java.util.Set;

import ru.homework.domain.Author;
import ru.homework.domain.Book;

public class AuthorDto {
    private int id;
    private String surname;
    private String firstname;
    private String middlename;
    
    private Set<Book> books;
    
    public AuthorDto() {
        super();
    } 
    
    public AuthorDto(String surname, String firstname, String middlename) {
        this(0, surname, firstname, middlename);
    }     
    
    public AuthorDto(int id, String surname, String firstname, String middlename) {
        super();
        this.id = id;
        this.surname = surname;
        this.firstname = firstname;
        this.middlename = middlename;
    }        
  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }    
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }        
    
    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }       
    
    public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
    public static Author toDomainObject(AuthorDto dto) {
        return new Author(dto.getId(), dto.getSurname(), dto.getFirstname(), dto.getMiddlename());
    }

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getSurname(), author.getFirstname(), author.getMiddlename());
    }   
}
