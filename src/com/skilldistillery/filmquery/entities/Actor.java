package com.skilldistillery.filmquery.entities;

import java.util.List;
import java.util.Objects;

public class Actor {
	private int id; 
	private String firstName; 
	private String lastName; 
	private List<Actor> actors;
	
	// methods 
	public Actor() { }

	public Actor(String fn, String ln) {
		firstName = fn; 
		lastName = ln; 
	}
	
	public Actor(int sagMemberNumber, String fn, String ln) {
		this.id = sagMemberNumber; 
		firstName = fn; 
		lastName = ln; 
	}

	
	public Actor(int id, String firstName, String lastName, List<Actor> actors) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.actors = actors;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, id, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		return Objects.equals(firstName, other.firstName) && id == other.id && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		
		return "Actor First Name = " + firstName + ", Last Name= " + lastName;
	}
	
	
}
