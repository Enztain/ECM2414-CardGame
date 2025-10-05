# ECM2414 Card Game – Multithreaded Coursework

## Overview
This project implements a multithreaded card game in Java for the ECM2414 module (2025–26).  
Each player and deck is handled by its own thread, with synchronization ensuring atomic draw–discard operations.

## Project Structure
src/
└── main/
└── java/
└── com/ecm2414/cardgame/app/CardGame.java

- app/ → main entry point and game controller
- domain/ → Card, Hand, Deck, Player
- io/ → logging, file input/output
- util/ → thread utilities
- test/ → JUnit test classes

## How to Run
1. Compile and build using IntelliJ or the command line:
   javac -d out $(find src/main/java -name "*.java")
   java -cp out com.ecm2414.cardgame.app.CardGame

2. Or build a runnable JAR in IntelliJ:
- Go to Build → Build Artifacts → Build
- Run using:
  ```
  java -jar out/artifacts/ECM2414-CardGame.jar
  ```

## Contributors
- Artem – Core setup, Card and Hand implementation
- Peer’s Name – Deck, Player, and concurrency control

## Tools
- IntelliJ IDEA Community Edition
- OpenJDK 23
- JUnit 5 for testing

## License
This project is for educational purposes as part of the ECM2414 coursework.