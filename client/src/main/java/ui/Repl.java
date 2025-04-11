package ui;

import com.google.gson.Gson;
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
        client = new Client(serverURL, this);
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
        System.out.print("\u001B[49m");
        System.out.print("\u001B[39m");
    }

    @Override
    public void notify(ServerMessage serverMessage, String message){
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();
        switch(type){
            case NOTIFICATION -> displayNotification(new Gson().fromJson(message, NotificationMessage.class));
            case ERROR -> displayError(new Gson().fromJson(message, ErrorMessage.class));
            case LOAD_GAME -> loadGame(new Gson().fromJson(message, LoadGameMessage.class));
            default -> System.out.println("Unrecognized Message Type");

        }
    }
    public void displayError(ErrorMessage message){
        String errorMessage = message.getError();
        System.out.println(SET_TEXT_COLOR_RED + errorMessage);
        System.out.print("\u001B[49m");
        System.out.print("\u001B[39m");
        printPrompt();
    }

    public void displayNotification(NotificationMessage notification){
        String notificationMessage = notification.getMessage();
        System.out.println(SET_TEXT_COLOR_MAGENTA + notificationMessage);
        System.out.print("\u001B[49m");
        System.out.print("\u001B[39m");
        printPrompt();
    }

    public void loadGame(LoadGameMessage message){
        GameData game = message.getGame();
        Client.updateGame(game);
        System.out.println();
        Client.redrawGame();
        printPrompt();
    }
}
