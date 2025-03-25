package ui;

import javax.management.Notification;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private final Client client;

    public Repl(String serverURL) {
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

    public void notify(Notification notification){
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    public void printPrompt(){
        String promptState = client.isLogin() ? "LOGGED_OUT" : "LOGGED_IN";
        System.out.println("\n" + "\u001b" + promptState + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
