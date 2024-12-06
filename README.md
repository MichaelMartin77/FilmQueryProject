# FilmQueryProject

## Description  
This command-line application allows users to retrieve and display film data from a database through a menu-driven interface. It utilizes JDBC for database interactions and follows a modular design pattern to separate concerns. The project demonstrates fundamental Java programming skills, including JDBC integration, object-oriented programming, and user input handling.

### Key Features  
- **Menu Navigation**:  
  Users can select from options to look up films by ID, search films by keyword, or exit the application.  

- **Film Lookup**:  
  - Retrieve detailed information about a film, including title, year, rating, description, and language.  
  - View the cast of actors associated with the film.  

- **Keyword Search**:  
  - Search for films by a keyword in the title or description.  
  - Display matching films with their basic details.  

- **Extensible Database Access**:  
  - Encapsulation of all database queries in the `DatabaseAccessorObject` class, ensuring clean separation of logic.  
  - Support for returning domain objects such as `Film` and `Actor` rather than raw data.  


## Technologies Used  
- **Java**: Core programming language for the application.  
- **JDBC**: For database connectivity and SQL execution.  
- **JUnit**: (Stretch Goal) For writing unit tests to ensure the reliability of database methods.  
- **Git/GitHub**: For version control and project management.  


## Lessons Learned  
This project reinforced several essential programming concepts and practices:  

1. **Modular Design**:  
   - Separating application logic into distinct classes and interfaces (`DatabaseAccessor`, `DatabaseAccessorObject`, `FilmQueryApp`) for better maintainability and scalability.  

2. **Object-Oriented Programming**:  
   - Encapsulating film and actor details in objects like `Film` and `Actor`.  
   - Returning rich objects instead of raw data structures for more intuitive data handling.  

3. **User Input Handling**:  
   - Building a dynamic and interactive menu for a better user experience.  
   - Validating user input to handle cases like invalid film IDs or empty search results gracefully.  

4. **Database Management**:  
   - Writing efficient SQL queries to fetch and filter data based on user input.  
   - Understanding JDBC workflows such as connection establishment, prepared statements, and result set processing.  

5. **Testing and Debugging**:  
   - (Stretch Goal) Using JUnit to test database access methods to ensure they return accurate and expected results.  


## Future Improvements  
- Implement stretch goals to expand functionality:  
  - Add submenus for viewing detailed film information, including all column values and inventory details.  
  - Display film categories as part of the detailed view.  
- Improve the database layer with more efficient queries and error handling mechanisms.  
- Add input validation to ensure robust and error-free user interaction.  
- Enhance testing coverage with comprehensive unit and integration tests.  