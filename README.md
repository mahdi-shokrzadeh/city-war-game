# City War - OOP Course Project

## Introduction

This project is a part of our Object-Oriented Programming (OOP) course.

## Team Information

- Team Members: MohammadMahdi Shokrzadeh - Mahdi Ghalami - Parsa Joinedi

- Project Start Date: Sunday, April 21 ,2024

## About the Game

City War is a strategic card game where two players compete against each other. 


### Game Demo
<div style="display: flex; flex-wrap: wrap;"> 
  <img src="https://raw.githubusercontent.com/mahdi-shokrzadeh/city-war-game/main-graphic/gameplay/1.jpg" alt="Gameplay Image 5" style="width: 98%; margin-bottom: 10px;"> 
</div>
<div style="display: flex; flex-wrap: wrap;"> 
  <img src="https://raw.githubusercontent.com/mahdi-shokrzadeh/city-war-game/main-graphic/gameplay/2.jpg" alt="Gameplay Image 1" style="width: 48%; margin-right: 2%;"> 
  <img src="https://raw.githubusercontent.com/mahdi-shokrzadeh/city-war-game/main-graphic/gameplay/3.jpg" alt="Gameplay Image 2" style="width: 48%;"> 
</div> 
<div style="display: flex; flex-wrap: wrap;"> 
  <img src="https://raw.githubusercontent.com/mahdi-shokrzadeh/city-war-game/main-graphic/gameplay/4.jpg" alt="Gameplay Image 3" style="width: 48%; margin-right: 2%;"> 
  <img src="https://raw.githubusercontent.com/mahdi-shokrzadeh/city-war-game/main-graphic/gameplay/5.jpg" alt="Gameplay Image 4" style="width: 48%;"> 
</div>


### Watch the Gameplay Video


[Gameplay Video](https://github.com/mahdi-shokrzadeh/city-war-game/raw/main-graphic/gameplay/Finalgameplay-1.m4v)


### Gameplay

At the start of the game, each player selects 20 cards from the card bank to form their deck. The cards in the game are of two types: healing/damage and spell. Each card has three attributes: 

1. Card attack/defense point
2. Duration
3. Player Damage

The game board is divided into two sections, and it is possible that one of the cells of each ground is randomly destroyed. At the start of each round, 5 random cards from the 20 cards come to the players' hands, and in each move, they pick another random card. 

Each player plays a card on the game ground and, depending on the duration number, occupies some of their ground. Immediately after placing the card, the damage player is added to the total damage of the player. 

The second player, to reduce the total damage of the opponent, must either use spell cards like a shield or place a card in the opposite houses that have more defense/attack. In both cases, the weaker card houses are destroyed, and the damage they had previously added to the player is also reduced. 

Each round will have 4 hands, and in each hand, players must fill their ground or damage the opponent's ground with spell cards. At the end of each round, the timeline indicator starts moving from the left of the ground and executes the moves of the two players in parallel. 

If during the move, a player's HP is depleted due to the incoming damages, the game ends, and the winner is determined. Otherwise, the game ground is cleared, and the hands are changed. Some of the winner's cards are upgraded after the win.


### Used Technologies

City War is developed using the following technologies:

- **Java**: Core programming language for the game logic.
- **JavaFX**: Used for building the graphical user interface.
- **Maven**: Dependency management and build automation tool.
- **Jackson**: Library for database handling and JSON processing.

### How to Use

To get started with City War, follow these steps:

1. **Clone the Project**:
   ```bash
   git clone https://github.com/mahdi-shokrzadeh/city-war-game.git
   ```

2. **Install Dependencies**:
   ```bash
   mvn install
   ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```
   
4. **Run the Application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.city-war-game.HelloApplication"
   ```
