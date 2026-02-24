\# Blackjack Training App

\![Java\](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
\![Swing\](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)

An advanced Blackjack training application written in Java (Swing). This
program is designed not only for gameplay but primarily for learning the
Hi-Lo card counting strategy, running simulations, and practicing with
time constraints.

\-\--

\## Gallery

\### Interactive Mode The main gameplay view with real-time statistics
and a Hi-Lo counter.

\![Gameplay\](/gameplay.png)

\### Exercise Mode A fast-paced training module to practice card
counting without playing full hands.

\![Exercise\](/excericse.png)

\-\--

\## Features

The application offers three primary modes:

\* \*\*Interactive Mode:\*\* Full game simulation. \* Supported actions:
Hit, Stand, Double, Split. \* Visual representation of player and dealer
cards. \* Live statistics: Wins, Losses, and Profit. \* \*\*Exercise
Mode:\*\* Dedicated module for learning card counting. \* Cards are
dealt at an adjustable speed. \* The player must keep track of the
Running Count in their head. \* \*\*Simulation Mode:\*\* Automated mode
for testing your card counting skills in simulated game.

\### Key Configuration Options: \* \*\*Hi-Lo System:\*\* Built-in
counter displaying Running Count and True Count in real-time. \*
\*\*Adjustable Difficulty:\*\* \* Shoe Size: 1 to 8 decks. \* Speed
Slider: Controls the animation and dealing pace.

\-\--

\## Technologies

The project is built using pure Java without heavy external frameworks:
\* \*\*Language:\*\* Java SE \* \*\*GUI:\*\* Swing (JFrame, JPanel,
Graphics2D) \* \*\*Core Logic:\*\* Object-oriented game engine
(GameEngine, Shoe, Hand).

\-\--

\## How to Run

\### Prerequisites \* Java Development Kit (JDK) 8 or newer.

\### Installation & Execution 1. Clone the repository: 2. javac \*.java
3. java BlackjackGUI
