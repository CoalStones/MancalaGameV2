## Note

This was my third assignment for my Object Oriented Programming class <br>
This assignment took me about 40 hours to complete, about half of that was spent on creating the GUI and connecting it to the game rules. I recieved a 93% off the functionality of the code (Not including any penalties or bonus marks) <br>
I do not give permission to reuse any part of this code in any academic project.

# Project Title

Mancala

## Description

Users may play a visial version of the boardgame Mancala with another player <br><br>

* MancalaGame
    * Holds all the game logic
    * Dependencies
        * PCountable
            * Pit
            * Store
        * Player
        * MancalaDataStructure
        * GameRules
            * Kalah
            * Ayo
* GUI
    * Does all the visuals
    * Dependencies
        * Player
        * Saver
        * MancalaGame

## Getting Started

Run the jar file!

### Dependencies

* Default Java Libraries
* Array Lists
* Scanner
* Exceptions
* JFrame
* Files

### Executing program

* How to build and run the program
* Step-by-step bullets
```
run the gradle file with gradle build
use gradle echo to find the command
your choice of .jar file or class files

To run the program from jar:
java -jar build/libs/Mancala.jar
```
* include the expected output
A JFrame GUI

## Development History

Keep a log of what things you accomplish when.  You can use git's tagging feature to tag the versions or you can reference commits.

* 0.6 
    * Fixed more PMD errors
* 0.5
    * Created GUI
* 0.4
    * Serialization
* 0.3
    * Different Game Rules
* 0.2
    * Fixed PMD Errors
* 0.1
    * Refactored the code

## Acknowledgments

swingtutorial - Created by Judi McCuaig

Inspiration, code snippets, etc.
* [awesome-readme](https://github.com/matiassingers/awesome-readme)
* [simple-readme] (https://gist.githubusercontent.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc/raw/d59043abbb123089ad6602aba571121b71d91d7f/README-Template.md)



