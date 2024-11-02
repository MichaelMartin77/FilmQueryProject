package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		String sql = "SELECT * FROM film WHERE title LIKE ? OR description LIKE ?";

		try {
			Connection conn = DriverManager.getConnection(URL, name, pwd);
			PreparedStatement ps = conn.prepareStatement(sql);

			String searchKeyword = "%" + keyword + "%";
			ps.setString(1, searchKeyword);
			ps.setString(2, searchKeyword);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setTitle(rs.getString("title"));
				film.setReleaseYear(rs.getInt("release_year"));
				film.setRating(rs.getString("rating"));
				film.setDescription(rs.getString("description"));
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

}
