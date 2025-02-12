# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Sequence Diagram
[Sequence Diagram]https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco67F8H5LCBALnAWspqig5QIAePK0uWo7vvorxLNiCaGC6YZumSFIGshI6jCaRLhhaHIwAY3DgDAr6GAAZt4wz0R0WjyKG5Fup22HlEa2iOs6BJ4Sy5Set6QbEe82hkUyPGRlRwA2mxAraLM06QHOMA8pAMDTkJibJlBJb7oe2a5pg-4gjBJRXAMKGjAuXzTrOzbjlMfTfleNmFNkPYwP2g69A5JFjlWLlBm584eX0XlmJwq7eH4gReCg6B7gevjMMe6SZJg-kXkU1DXtIACiu5lfUZXNC0D6qE+3SuY26Dtr+ZxAiWzVzlZJntXxMAIfYNS+gGUUtWgWFyjhIncWJRgoNwmSxv63XoHJZoRoUloLUthhrWghk4cZnVpj0kGnbxPmlOdbWdoVYB9gOQ5LolnjJRukK2ru0IwAA4qOrK5aeBXnswtnXn9VW1fYo5NeNPU-my1nlAdvWXbZ4KDdCAOjKoY11hNU2wRqomkjA5JgCtBMzkTXHydKinlDRKB0QdMDMRArEGa6jOY-K+lBvG02k3N5PILEuNqLCG0UUz1GLazzDAMqFOAxzLFq3j9ObVdsFkoDP2xEdnYo1rahG3mfV63ZpZ9LDePjJULgu40ewwHdSZ+WDT3Bf0DtqE7FQuy4bse8uSXroE2A+FA2DcPAuqZP9o4pHlZ45ODl4lWmN4NDDcPBAj6BDgHAByo7eTnyN9VcB2zOXlfo6mbIDRJmRSzT0UN6OFekSbuFi+6FMUtTB2ywp21USzdEB4LfpaRlnPc0GHHADrcv83B5vz7Tc7CyTg8M+T7coJ3Achrz5ry9G5sOlfEZbwbowAJLSAPJ2ps-KBv830EQ2de2o435OwDm-KuxwvZdkzr7IcQDX7SFAcA6Qr0VzvSjgESwi0ELJBgAAKQgDyFOoxAg6AQKABsoNM6t2upUKolI7wtADvDQmc4hzx2AFgqAcAIAISgD3BBECOyf2BHXYuaB3zkK4TwvhAif4oL-sI7OToBYACtCFoE7vXGAHDpG8OgHIt+xMVEzRkGTYelMx7iInozKezNFaz1TppCaGsua7zXhvBST8d4HQPiY0Wx8LEUnPsgmx187EK1oswMB0hXGsQDhoB+NssYX0EthAJm1+LYA4mfUcsJdGUBkQYu+slPG2PZKjFSBTuH6P4SU+QOkMAQHNok8xySBa3XSVAs2t1rI2wAkI3y0CApBReqYCO6CUoBC8JwrsXpYDAGwPHQg8REhpxBg9GhOdygVHKpVaqtVjCez-LXGAIBuB4EZAAIX0CgWExhsR9O8ec+Z1zbn3J0MY4SZih7lCOUk+WHBFbJxuBpcRciynX28Z0kW3TTm9OtlAn8VxBn3R9oFZ6vQDgJRXEAA 

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
