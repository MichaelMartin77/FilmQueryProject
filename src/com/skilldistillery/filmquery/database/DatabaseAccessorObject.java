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
	        // start a transaction
	        conn.setAutoCommit(false);

	        String sql = "INSERT INTO film (title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) "
	                + "VALUES (?, ?, ?, 1, ?, ?, ?, ?, ?, ?)";

	        // compile / optimize the sql into the db, and request the generated keys be accessible
	        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

	        // bind (assign) the film properties into our sql statements bind vars
	        stmt.setString(1, aFilm.getTitle());
	        stmt.setString(2, aFilm.getDescription());
	        stmt.setInt(3, aFilm.getReleaseYear());
	        stmt.setInt(4, aFilm.getRentalDuration());
	        stmt.setDouble(5, aFilm.getRentalRate());
	        stmt.setInt(6, aFilm.getLength());
	        stmt.setDouble(7, aFilm.getReplacementCost());
	        stmt.setString(8, aFilm.getRating());
	        stmt.setString(9, aFilm.getSpecialFeatures());

	        // run the query in the database
	        int updateCount = stmt.executeUpdate();

	        // check if the INSERT was successful in creating 1 new Film
	        if (updateCount == 1) {
	            // good news: we can grab this new Film's id
	            ResultSet keys = stmt.getGeneratedKeys();

	            // we're expecting just 1 generated key
	            if (keys.next()) {
	                // grab the generated key (id)
	                int newFilmId = keys.getInt(1);

	                // change the initial id in our Java entity to film's 'real' id
	                aFilm.setId(newFilmId);
	            }

	            // an explicit commit of the transaction is required to prevent a rollback
	            conn.commit();

	        } else {
	            // something went wrong with the INSERT
	            aFilm = null;
	        }

	        conn.close();

	    } catch (SQLException sqle) {
	        sqle.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException sqle2) {
	                System.err.println("Error trying to rollback");
	            }
	        }
	        throw new RuntimeException("Error inserting film " + aFilm);
	    }

	    return aFilm;
	}

}
