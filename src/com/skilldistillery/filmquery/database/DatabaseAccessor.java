package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId)throws SQLException;
   public Actor findActorById(int actorId);
  public List<Actor> findActorsByFilmId(int filmId);
}
