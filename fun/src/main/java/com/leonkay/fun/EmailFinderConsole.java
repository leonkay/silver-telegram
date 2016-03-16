package com.leonkay.fun;

/**
 * Created by leonkay on 3/14/16.
 */
public class EmailFinderConsole {

    /**
     * Main Executable that takes in a URL as its first argument. The program will navigate
     * to the url and any urls linked to by the application, searching for email addresses.
     * @param args - the arguments to this command line application. Should only be a URL string.
     */
    public static void main(String ... args) {

        if (args != null && args.length > 0) {

            for (String url : args) {
                EmailFinder finder = new EmailFinder();
                finder.visit(url);
            }
        }
        else {
            System.out.println("ERROR: Please provide a URL as an argument to this application");
        }
    }
}
