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

	public static void main(String[] args) throws SQLException {
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
		try {
			startUserInterface(input);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		boolean running = true;

		while (running) {
			System.out.println("Welcome! Please select the following items from the list below:");
			System.out.println("1. Look film up by it's id");
			System.out.println("2. Look up a film by a search keyword");
			System.out.println("3. Add New Film");
			System.out.println("4. Delete a Film");
			System.out.println("5. Update a Film");
			System.out.println("6. Exit");

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
						System.out.println("Language: " + film.getLanguage());
						System.out.println("Actors: " + film.getActors());
						System.out.println();
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Search Film By Keyword: ");
				String keyword = input.next();
//				input.nextLine();

				List<Film> films = db.searchFilmByKeyword(keyword);

				if (films == null || films.isEmpty()) {
					System.out.println("No matching films were found containing the word " + keyword);
					System.out.println();
				} else {
					for (Film film : films) {
						System.out.println("Title: " + film.getTitle());
						System.out.println("Year: " + film.getReleaseYear());
						System.out.println("Rating: " + film.getRating());
						System.out.println("Description: " + film.getDescription());
						System.out.println("Language: " + film.getLanguage());
						System.out.println("Actors: " + film.getActors());
						System.out.println();
					}

				}
				break;
			case 3:
				System.out.println("Please enter the film title: ");
				String title = input.nextLine();
				input.nextLine();

				System.out.println("Please enter the film rental rate: ");
				double rentalRate = input.nextDouble();
				input.nextLine();

				System.out.println("Please enter the replacement cost: ");
				double replacementCost = input.nextDouble();
				input.nextLine();

				Film newFilm = new Film(title, rentalRate, replacementCost);
				Film createdFilm = db.createFilm(newFilm);

				if (createdFilm != null) {
					System.out.println("Film added successfully! Generated ID: " + createdFilm.getId());
					System.out.println();
				} else {
					System.out.println("Failed to add film.");
					System.out.println();
				}
				break;
			case 4:
				System.out.println("Please enter the flm ID to delete: ");
				int filmIdToDelete = input.nextInt();
				Film filmToDelete = db.findFilmById(filmIdToDelete);

				if (filmToDelete == null) {
					System.out.println("Film with the ID: " + filmToDelete + " does not exist");
				} else {
					boolean success = db.deleteFilm(filmToDelete);
					if (success) {
						System.out.println("Film deletion test completed successfully (rollback performed).");
					} else {
						System.out.println("Film deletion test failed");
					}

				}

				break;
			case 5:
				 System.out.println("Please enter the film id to update: ");
	                int filmIdToUpdate = input.nextInt();
	                Film filmToUpdate = db.findFilmById(filmIdToUpdate);

	                if (filmToUpdate == null) {
	                    System.out.println("Film with ID " + filmIdToUpdate + " does not exist.");
	                } else {
	                    System.out.println("Current Title: " + filmToUpdate.getTitle());
	                    System.out.print("Enter new title (or press Enter to keep the current title): ");
	                    input.nextLine();  // Consume the remaining newline
	                    String newTitle = input.nextLine();
	                    if (!newTitle.trim().isEmpty()) {
	                        filmToUpdate.setTitle(newTitle);
	                    }

	                    System.out.println("Current Description: " + filmToUpdate.getDescription());
	                    System.out.print("Enter new description (or press Enter to keep the current description): ");
	                    String newDescription = input.nextLine();
	                    if (!newDescription.trim().isEmpty()) {
	                        filmToUpdate.setDescription(newDescription);
	                    }

	                    boolean updateSuccess = db.updateFilm(filmToUpdate);

	                    if (updateSuccess) {
	                        System.out.println("Film updated successfully!");
	                    } else {
	                        System.out.println("Failed to update film.");
	                    }
	                }
	                break;
			case 6:
				System.out.println();
				System.out.println("Exiting.... \nGoodBye");
				running = false;
				break;
			}

		}

	}
}
