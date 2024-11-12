package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";

	public DatabaseAccessorObject() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		String name = "student";
		String pwd = "student";

		String sql = "SELECT * FROM film WHERE id = ?";
		Connection conn = DriverManager.getConnection(URL, name, pwd);
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, filmId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String description = rs.getString("description");
			Integer releaseYear = rs.getInt("release_year");
			int languageId = rs.getInt("language_id");
			int rentalDuration = rs.getInt("rental_duration");
			double rentalRate = rs.getDouble("rental_rate");
			int length = rs.getInt("length");
			double replacementCost = rs.getDouble("replacement_cost");
			String rating = rs.getString("rating");
			String specialFeatures = rs.getString("special_features");

			List<Actor> actors = findActorsByFilmId(id);

			film = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
					replacementCost, rating, specialFeatures, actors);

			String languageName = getLanguage(languageId);
			film.setLanguage(languageName);

		}

		rs.close();
		ps.close();
		conn.close();
		return film;

	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		String name = "student";
		String pwd = "student";

		String sql = "SELECT * FROM actor WHERE id = ?";

		Connection conn = DriverManager.getConnection(URL, name, pwd);
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, actorId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");

			actor = new Actor(id, firstName, lastName);
		}

		ps.close();
		conn.close();

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		ArrayList<Actor> actors = new ArrayList<Actor>();
		String name = "student";
		String pwd = "student";

		String sql = "SELECT a.id, a.first_name, a.last_name FROM actor a JOIN film_actor fa ON a.id = fa.actor_id WHERE fa.film_id = ?";

		Connection conn = DriverManager.getConnection(URL, name, pwd);
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, filmId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");

			Actor actor = new Actor(id, firstName, lastName); // Create an Actor object
			actors.add(actor);
		}

		rs.close();
		conn.close();
		ps.close();
		return actors;
	}

	@Override
	public List<Film> searchFilmByKeyword(String keyword) {
		List<Film> foundFilms = new ArrayList<>();
		String name = "student";
		String pwd = "student";
		String sql = "SELECT film.id, film.title, film.release_year, film.rating, film.description, film.language_id, language.name FROM film JOIN language ON film.language_id = language.id WHERE film.title LIKE ? OR film.description LIKE ?";

		try {
			Connection conn = DriverManager.getConnection(URL, name, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);

			String searchKeyword = "%" + keyword + "%";
			ps.setString(1, searchKeyword);
			ps.setString(2, searchKeyword);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				int filmId = rs.getInt("id");
				film.setTitle(rs.getString("title"));
				film.setReleaseYear(rs.getInt("release_year"));
				film.setRating(rs.getString("rating"));
				film.setDescription(rs.getString("description"));
				int languageId = rs.getInt("language_id");
				film.setLanguage(rs.getString("name"));
				String languageName = getLanguage(languageId);
				film.setLanguage(languageName);

				List<Actor> actors = findActorsByFilmId(filmId);
				film.setActors(actors);
				foundFilms.add(film);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foundFilms;
	}

	public String getLanguage(int languageId) {
		String languageName = null;
		String name = "student";
		String pwd = "student";
		String sql = "SELECT language.name FROM language WHERE language.id = ?";

		try {
			Connection conn = DriverManager.getConnection(URL, name, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, languageId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				languageName = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return languageName;

	}

	public Film createFilm(Film aFilm) {
		String name = "student";
		String pwd = "student";
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, name, pwd);

			conn.setAutoCommit(false);

			String sql = "INSERT INTO film (title, language_id, rental_rate, replacement_cost) VALUES (?, 1, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, aFilm.getTitle());
			stmt.setDouble(2, aFilm.getRentalRate());
			stmt.setDouble(3, aFilm.getReplacementCost());

			int updateCount = stmt.executeUpdate();

			if (updateCount == 1) {

				ResultSet keys = stmt.getGeneratedKeys();
				if (keys.next()) {
					int newFilmId = keys.getInt(1);
					aFilm.setId(newFilmId);
				}
				conn.commit();
			} else {
				aFilm = null;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException sqle2) {
				System.err.println("Error trying to rollback");
			}
			throw new RuntimeException("Error inserting film " + aFilm);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return aFilm;
	}


	@Override
	public boolean deleteFilm(Film afilm) {

		String name = "student";
		String pwd = "student";

		try (Connection conn = DriverManager.getConnection(URL, name, pwd)) {
			conn.setAutoCommit(false);

			String deleteFilmSql = "DELETE FROM film WHERE id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(deleteFilmSql)) {
				stmt.setInt(1, afilm.getId());

				int rowsAffected = stmt.executeUpdate();

				if (rowsAffected == 0) {
					conn.rollback();
					return false; // No film found with this ID
				}

				conn.commit();
				return true;

			} catch (SQLException e) {
				conn.rollback();
				e.printStackTrace();
				return false;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean updateFilm(Film aFilm) {
		String name = "student";
	    String pwd = "student";

	    String updateFilmSql = "UPDATE film SET title = ?, description = ? WHERE id = ?";
	    
	    try (Connection conn = DriverManager.getConnection(URL, name, pwd)) {
	        conn.setAutoCommit(false);

	        try (PreparedStatement stmt = conn.prepareStatement(updateFilmSql)) {
	            // Set the title and description parameters based on the provided Film object
	            stmt.setString(1, aFilm.getTitle());
	            stmt.setString(2, aFilm.getDescription());
	            stmt.setInt(3, aFilm.getId());

	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected == 0) {
	                conn.rollback();
	                return false; // No film found with this ID
	            }

	            conn.commit();
	            return true;

	        } catch (SQLException e) {
	            conn.rollback();
	            e.printStackTrace();
	            return false;
	        }
	    } catch (SQLException sqle) {
	        sqle.printStackTrace();
	        return false;
	    }
	}
	
	
	

	@Override
	public Actor createActor(Actor actor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveActor(Actor actor) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
