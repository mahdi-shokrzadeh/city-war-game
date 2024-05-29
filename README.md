# City War - OOP Course Project

## Introduction

This project is a part of our Object-Oriented Programming (OOP) course.

## Team Information

- Team Members: Mahdi Ghalami - MohammadMahdi Shokrzadeh - Parsa Joinedi
- Student Numbers: 402102284 - 402101985 - 402101509
- Project Start Date: Sunday, April 21 ,2024

## About the Game

City War is a strategic card game where two players compete against each other. 

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

