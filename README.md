# Flappy Bird JavaFX FXML Project

This project is a recreation of the HTML/JavaScript-based Flappy Bird game using JavaFX and FXML.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── flappybird/
│   │           ├── Main.java                    # Main class
│   │           ├── controller/
│   │           │   ├── LoginController.java     # Login screen controller
│   │           │   ├── RegisterController.java  # Registration screen controller
│   │           │   └── GameController.java      # Game screen controller
│   │           ├── model/
│   │           │   ├── User.java               # User model
│   │           │   └── UserManager.java        # User management
│   │           └── game/
│   │               └── GameEngine.java         # Game engine
│   └── resources/
│       ├── fxml/
│       │   ├── login.fxml                      # Login screen FXML
│       │   ├── register.fxml                   # Registration screen FXML
│       │   └── game.fxml                       # Game screen FXML
│       └── css/
│           └── style.css                       # Style file
```

## Class Descriptions

### 1. Main.java
- Main class of the JavaFX application
- Starts the application and loads the initial scene
- Configures Stage and Scene settings

### 2. LoginController.java
- Controller class for the login screen
- Manages user login operations
- Provides navigation to registration screen
- Features Enter key login capability

### 3. RegisterController.java
- Controller class for the registration screen
- Manages new user registration operations
- Performs form validation
- Redirects to login screen after successful registration

### 4. GameController.java
- Controller class for the game screen
- Manages game loop (using AnimationTimer)
- Handles user input (keyboard and mouse)
- Manages game over screen
- Updates leaderboard

### 5. User.java
- Model class that holds user information
- Contains username, password, and bestScore fields
- Suitable for JSON serialization

### 6. UserManager.java
- Class that performs user management operations
- Data storage with JSON files
- Login, registration, and score update operations
- Leaderboard creation

### 7. GameEngine.java
- Class containing the game logic
- Bird physics, pipe creation and movement
- Collision detection
- Canvas drawing operations
- Easy mode settings (wide gaps, smooth controls)

## Features

### User Management
- ✅ User registration and login system
- ✅ Password-protected accounts
- ✅ Demo accounts (demo/1234, test/test)
- ✅ Data storage with JSON files

### Game Features
- ✅ Easy mode (like the HTML version)
- ✅ Smooth controls and wide gaps
- ✅ Score system and record tracking
- ✅ Leaderboard (top 5)
- ✅ New record notifications

### Technical Features
- ✅ Game rendering with JavaFX Canvas
- ✅ Smooth game loop with AnimationTimer
- ✅ UI design with FXML
- ✅ Style management with CSS
- ✅ MVC architecture

## Requirements

- Java 17 or higher
- JavaFX 21.0.2
- Maven 3.6+
- Windows/Mac/Linux compatible

## How to Run

### Method 1: Using Maven
```bash
mvn clean javafx:run
```

### Method 2: Using JAR file
```bash
mvn clean package
java -jar target/fbg-game-1.0.0.jar
```

### Method 3: Using run.bat (Windows)
```batch
run.bat
```

## Demo Accounts

- **Username:** demo | **Password:** 1234
- **Username:** test | **Password:** test
- **Username:** player | **Password:** pass

## Game Controls

- **SPACE key:** Jump
- **Mouse click:** Jump
- **ENTER:** Confirm in forms

## Installation

1. Clone the repository
2. Make sure Java 17+ is installed
3. Run `mvn clean package` to build
4. Execute `java -jar target/fbg-game-1.0.0.jar`

## Development

### Building the Project
```bash
# Clean and compile
mvn clean compile

# Run tests (if any)
mvn test

# Package as JAR
mvn package

# Run the application
mvn javafx:run
```

### Project Architecture
- **MVC Pattern:** Model-View-Controller architecture
- **JavaFX:** Modern Java desktop application framework
- **FXML:** XML-based UI layout
- **Maven:** Dependency management and build tool

## Contributing

1. Fork the project
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## License

This project is open source and available under the MIT License.

## Version History

- **v1.0.0** - Initial release with complete game functionality
- Easy mode Flappy Bird gameplay
- User management system
- Leaderboard and score tracking

## Support

For issues and questions, please create an issue in the repository.

---

**Enjoy playing FBG Game! 🎮**