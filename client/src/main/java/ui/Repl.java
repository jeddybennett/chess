package ui;

import exception.ResponseException;
import model.GameData;
import websocket.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl implements ServerMessageObserver {
    private final Client client;

    public Repl(String serverURL) throws ResponseException {
        client = new Client(serverURL, this, this);
    }

    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.\n" +
                "Type \"help\" to get started!");

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (!Objects.equals(result, "quit")) {
                    System.out.print(SET_TEXT_COLOR_BLUE + result);
                }
            }
            catch(Throwable e){
                var msg = e.toString();
                System.out.print(msg);

            }
        }

        System.out.println("You won't get any better if you leave");
    }

    public void printPrompt(){
        String promptState = client.isLogin() ? "LOGGED_OUT" : "LOGGED_IN";
        if(!client.isLogin()){
            promptState = client.isInGame() ? "IN_GAME" : "LOGGED_IN";
        }
        System.out.print("\n" + "\u001b" + promptState + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(ServerMessage message){
        switch(message.getServerMessageType()){
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getError());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());

        }
    }
    public void displayError(String error){
        System.out.println(SET_TEXT_COLOR_RED + error);
    }

    public void displayNotification(String notification){
        System.out.println(SET_TEXT_COLOR_MAGENTA + notification);
    }

    public void loadGame(Object game){
        Client.updateGame((GameData) game);
        Client.redrawGame();
    }
}
