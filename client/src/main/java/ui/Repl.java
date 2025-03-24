package ui;

import javax.management.Notification;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private final Client client;

    public Repl(String serverURL) {
        client = new Client(serverURL, this);
    }

    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        System.out.print(client.helpPreLogin());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            }
            catch(Throwable e){
                var msg = e.toString();
                System.out.print(msg);

            }
        }

        System.out.println();
    }

    public void notify(Notification notification){
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    public void printPrompt(){
        System.out.println("\n" + "\u001b" + "0m" + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
