package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
//    app.launch();
	}

//	private void test() {
//		try {
//			Film film = db.findFilmById(6);
//			System.out.println(film.getTitle() + " \n" + film.getDescription() + " \n" + film.getActors());
//			System.out.println(film);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void launch() {
		Scanner input = new Scanner(System.in);
		startUserInterface(input);
		input.close();
	}

	private void startUserInterface(Scanner input) {
		boolean running = true;

		while (running) {
			System.out.println("Welcome! Please select the following items from the list below:");
			System.out.println("1. Look film up by it's id");
			System.out.println("2. Look up a film by a search keyword");
			System.out.println("3. Exit");

			int menuSelection = input.nextInt();

			switch (menuSelection) {
			case 1:
				System.out.println("Please enter a film id: \n");
				int filmId = input.nextInt();

				try {
					Film film = db.findFilmById(filmId);
					if (film == null) {
						System.out.println("This film does not exist");
						System.out.println();
					} else {
						System.out.println("Title: " + film.getTitle());
						System.out.println("Year: " + film.getReleaseYear());
						System.out.println("Rating: " + film.getRating());
						System.out.println("Description: " + film.getDescription());
					}
					// Call getLanguage to retrieve the language details
					Film languageFilm = db.getLanguage(film.getLanguageId()); // Assuming you have a getLanguage method
					if (languageFilm != null) {
						System.out.println("Language: " + languageFilm.getLanguageName());
					} else {
						System.out.println("Language details not found.");
					}

					System.out.println();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Search Film By Keyword: ");
				String keyword = input.next();
				input.nextLine();

				List<Film> films = db.searchFilmByKeyword(keyword);

				if (films == null || films.isEmpty()) {
					System.out.println("No matching films were found containing the word " + keyword);
					System.out.println();
				} else {
					for (Film film : films) {
						System.out.println("Title: " + ((Film) film).getTitle());
						System.out.println("Year: " + ((Film) film).getReleaseYear());
						System.out.println("Rating: " + ((Film) film).getRating());
						System.out.println("Description: " + ((Film) film).getDescription());
						System.out.println();
					}

				}

			}
		}

	}

}
