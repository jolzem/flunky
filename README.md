# Flunkyball

Welcome to Flunkyball, a Java-based online game inspired by the popular Flunkyball drinking game. This project provides a server and client implementation of the game, allowing users to play with friends over the internet or on a local connection.

## Requirements

To play Flunkyball online, you will need the following:

- BlueJ
- Java 8 or higher (Included with BlueJ)
- An internet connection or connection to a local network

## Installation

To install Flunkyball, simply clone this repository to your local machine:

```
git clone https://github.com/jolzem/flunky.git
```

Feel free to distribute the flunky-client folder to the people you want to play with.

### Please add words and graphics by yourself!

In the flunky-client/ folder create an "img" directory and add the following images:
- connect.png (only player 1)
- start.png (both players in idle position)
- p1drink.png (p1 drinking, p2 idle, bottle fallen)
- p1throw.png (p1 throwing, p2 idle, bottle standing)
- p2drink.png (p1 idle, p2 drinking, bottle fallen)
- p2throw.png (p1 idle, p2 throwing, bottle standing)

To add words, edit the FlunkyServer.java file on line 58:

Add your words by adding `, "<your word>"` at the end of the line **before** the `};`

## Usage

To run Flunkyball, open the package.bluej files in the flunky and flunk-client folders.

To start the server, in the flunky project, create an object of the `FlunkyServer` class. This will start the server and listen for incoming connections.

To start the client, in the flunky-client project, create an object of the `FlunkyGUI` class. This will launch the client GUI, allowing you to connect to the server and start a new game.

Make sure to start the server before starting the client, otherwise the client will not be able to connect to the server.

## How to Play

After two users have joined and one of them has pressed the "Start" button, the game will begin.

The game starts with a simulated bottle in the middle of the screen. A user will be asked to submit a letter. If they manage to do so in a set period of time, the simulated bottle in the middle will be hit, and the user will start drinking, gaining points.

The other user then has to submit a sequence of characters to "pick the bottle back up". The longer they take to submit these, the more points the first player will gain.

This is then repeated, alternating between players until one player has emptied their bottle.

The game is played entirely through the FlunkyGUI client. The user interface will display the current score, as well as which player is currently up to submit a letter or sequence of characters.

Have fun playing Flunkyball Online Game!

## License

This implementation of Flunkyball is open source software licensed under the GNU General Public License, version 3.0. You can find a copy of the license in the `LICENSE` file.

### This license does not apply to the files Server.java and flunky-client/Client.java, as they were neither written nor owned by me. Do not distribute these files without prior consent by the North-Rine-Westphalia State Institute for Schools (QUA-Lis NRW).

## Credits

This project is based on the Server and Client classes from the German NRW Abitur in Networking. The client GUI is built using the Java JFrame API.

Thanks to the original authors of the NRW Abitur in Networking classes for providing the foundation for this project as well as my teacher for providing me with the resources to build this project.

## Future Goals

- ~~Add graphics/images to visualize gameplay and to _fill the void_.~~
