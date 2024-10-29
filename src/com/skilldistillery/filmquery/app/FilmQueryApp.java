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
		System.out.println("Welcome! Please select the following items from the list below:");
		System.out.println("1. Look film up by it's id");
		System.out.println("2. Look up a film by search by a search keyword");
		System.out.println("3. Exit");
		
		int menuSelection = input.nextInt(); 
		
		if (menuSelection == 1) { 
			System.out.println("Please enter a film id");
			int filmId = input.nextInt();
			try {
				Film film = db.findFilmById(filmId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
	
	

}
