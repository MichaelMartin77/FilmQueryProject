package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId)throws SQLException;
  public Actor findActorById(int actorId) throws SQLException;
  public List<Actor> findActorsByFilmId(int filmId) throws SQLException;
  public List<Film> searchFilmByKeyword(String keyword);
  public String getLanguage(int languageId); 
  public Film createFilm(Film aFilm); 
  public boolean deleteFilm(Film aFilm); 
}	
