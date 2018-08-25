package ru.homework.dto;

import ru.homework.domain.Genre;

public class GenreDto {
    private int id;
    private String name;
    
    public GenreDto() {
        super();
    } 

    public GenreDto(String name) {
        this(0, name);
    }       
    
    public GenreDto(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }        
  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
    public static Genre toDomainObject(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }    
}
