import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Scanner;

public class App {

    private final Scanner keyboard = new Scanner(System.in);

    private List<String> dataset = new ArrayList<>();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {


        while (true) {
            printTitleCard();
            printMainMenu();
            int mainMenuSelection = promptForMenuSelection("Please choose an option: ");
            if (mainMenuSelection == 1) {
                playCodeBreaker();
            }
            if (mainMenuSelection == 2) {
//                playTwoPlayer();
                break;
            }
            if (mainMenuSelection == 3) {
                displayPastResults();
            }
            if (mainMenuSelection == 0) {
                break;
            }
            break;
        }

    }

    private void printTitleCard() {
        System.out.println("*****************");
        System.out.println("WELCOME TO CODE BREAKER");
        System.out.println("*****************");
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println("1: Play Code Breaker");
//        System.out.println("2: Play Two Player Game");
        System.out.println("3: Display Past Results");
        System.out.println("0: Exit");
        System.out.println();
    }

    private void playCodeBreaker() {
        String resultString = "";
        System.out.println("One of you is the Code Maker.");
        System.out.println("One of you is the Code Breaker.");
        System.out.println("In a moment, the Code Maker will create the code.");
        System.out.println("It will be a combination of letters A, B, C & D.");
        System.out.println("So the code could be AADC or DCCA or BBBB or any other combo.");
        String codeToBreak = promptForString("Player One - Input Code: ").toUpperCase();
        for (int i = 0; i < 12; i++) {
            System.out.println("**********");
        }

        System.out.println("CODE BREAKER DON'T LOOK ABOVE THIS LINE.");


//                                                                                                                      System.out.println(playWord + " test");
        String playerGuess = "";
        String playerGuessLog = "";
        int guessCount = 0;
        char[] playerGuessSplit = playerGuess.toCharArray();
        while (true) {
            playerGuess = promptForString("Player 2 - Input guess (4 characters - combo of A,B,C,D): ").toUpperCase();
            if (playerGuess.length() != 4) {
                System.out.println("Guess must be 4 characters.");
                System.out.println("You've made " + guessCount + " guess(es) so far.");
//                                                                                                                      break;
//                                                                                                                      playSinglePlayer(playWordSplit, playWord);
                continue;
            }
            resultString = checkGuessVsPlayWord(playerGuess, codeToBreak);
            playerGuessLog += resultString + "\n";
            System.out.println(playerGuessLog);
            System.out.println(letterCountString(playerGuess, codeToBreak));
            guessCount++;
//                                                                                                                      System.out.println(guessCount);
            if (guessCount > 15) {
                System.out.println("*insert sad tuba sound here*");
                System.out.println("TOO BAD!");
                System.out.println("The code you were looking for was: " + codeToBreak + ".");
                logFailResults(guessCount);
                break;
            }
            if (playerGuess.equalsIgnoreCase(codeToBreak)) {
                displayResults(guessCount);
                logSuccessResults(guessCount);
                break;
            }
        }
        promptForReturn();
    }

    private void playTwoPlayer() {

    }

    private String checkGuessVsPlayWord(String playerGuess, String playWord) {
        String guessResultString = "";
        char[] playerGuessArray = playerGuess.toCharArray();
        char[] playWordArray = playWord.toCharArray();
        int rightCount = 0;
        int wrongCount = 0;
        int nearCount = 0;
        String nearString = "";

        // so the issue here is that if there's a word with more than one of a single letter
        // it will 'print' problematically
        // feel like i need to map word first, and get values of each letter.
        Map<Character, Integer> playWordMap = WordMap(playWord);
        Map<Character, Integer> guessWordMap = WordMap(playerGuess);
        // so now with the near stuff it's a matter of testing maps against each other.
        // i think i need to build a map as i go
        Map<Character, Integer> buildingMap = new HashMap<>();
        for (int i = 0; i < playerGuessArray.length; i++) {
            char guessLetter = playerGuessArray[i];
            if (!buildingMap.containsKey(guessLetter)) {
                buildingMap.put(guessLetter, 1);
            } else {
                buildingMap.put(guessLetter, buildingMap.get(guessLetter) + 1);
            }

            if (playerGuessArray[i] == playWordArray[i]) {
                guessResultString += "|+" + playerGuessArray[i] + "+|";
                rightCount++;
            }
            // so it's really here that i have an issue.
            // nullpointerexception by way of mapping. fixed.
            // STILL NOT WORKING
            // ok so the playWordMap will in a word like apple be : a 1 p 2 l 1 e 3
            // so for a guess against apple like plant it would say yes there's a p
            // had the map building in the wrong spot
            if (playerGuessArray[i] != playWordArray[i] &&
                    (new String(playWordArray).indexOf(playerGuessArray[i]) > -1)) { // if guessLetter isn't in the right place, but it is in the word...

                if (buildingMap.get(guessLetter) <= playWordMap.get(guessLetter)) {
                    guessResultString += "!?" + playerGuessArray[i] + "?|";
                    nearCount++;
                } else {
                    guessResultString += "|-" + playerGuessArray[i] + "-|";
                    wrongCount++;
                }
            }
            if (playerGuessArray[i] != playWordArray[i] &&
                    (new String(playWordArray).indexOf(playerGuessArray[i]) < 0)) {
                guessResultString += "|-" + playerGuessArray[i] + "-|";
                wrongCount++;
            }
        }
        return guessResultString;
    }

    private String letterCountString(String playerGuess, String playWord) {
        char[] playerGuessArray = playerGuess.toCharArray();
        char[] playWordArray = playWord.toCharArray();
        int rightCount = 0;
        int wrongCount = 0;
        int nearCount = 0;
        for (int i = 0; i < playerGuessArray.length; i++) {
            if (playerGuessArray[i] == playWordArray[i]) {
                rightCount++;
            }
            if (playerGuessArray[i] != playWordArray[i] &&
                    (new String(playWordArray).indexOf(playerGuessArray[i]) > -1)) {
                nearCount++;
                // ok but then do i need to make another map and compare the two?
            }
            if (playerGuessArray[i] != playWordArray[i] &&
                    (new String(playWordArray).indexOf(playerGuessArray[i]) < 0)) {
                wrongCount++;
            }
        }
        String letterCountString = "Your last guess has: ";
        if (rightCount > 0) {
            letterCountString += "\n" + rightCount + " letters in the right place (+).";
        }
        if (nearCount > 0) {
            letterCountString += "\n" + nearCount + " letters in the code but in the wrong place (?).";
        }
        if (wrongCount > 0) {
            letterCountString += "\n" + wrongCount + " letters not in the code at all (-).";
        }
        return letterCountString;
    }

    private void displayResults(int guessCount) {
        System.out.println("******************");
        if (guessCount == 1) {
            System.out.println("YOU GOT IT IN " + guessCount + " GUESS!!");
            System.out.println("THAT'S FREAKING INCREDIBLE!");
        }
        if (guessCount > 1 && guessCount < 6) {
            System.out.println("YOU GOT IT IN " + guessCount + " GUESSES!!");
            System.out.println("VERY NICE!");
        }
        if (guessCount >= 6) {
            System.out.println("YOU GOT IT IN " + guessCount + " GUESSES!!");
            System.out.println("THAT WAS CLOSE!");
        }
        System.out.println("******************");
    }

    private int promptForMenuSelection(String prompt) {
        System.out.print(prompt);
        int menuSelection;
        try {
            menuSelection = Integer.parseInt(keyboard.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    private String promptForString(String prompt) {
        System.out.print(prompt);
        return keyboard.nextLine();
    }

    public Map<Character, Integer> WordMap(String word) {
        //idea here is to generate a map that can be used to test existence of letters in guess.
        List<Character> wordSplit = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            wordSplit.add(word.charAt(i));
        }
        Map<Character, Integer> output = new HashMap<>();

        for (Character letter : wordSplit) {
            if (!output.containsKey(letter)) {
                output.put(letter, 1);
            } else {
                output.put(letter, output.get(letter) + 1);
            }
        }
        return output;
    }

    private void promptForReturn() {
        System.out.println("Press RETURN to continue.");
        keyboard.nextLine();
        run();
    }

    private void logSuccessResults(int guessCount ) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String auditPath = "resultsLog.txt";
        File logFile = new File(auditPath);
        try (PrintWriter log = new PrintWriter(new FileOutputStream(logFile, true))) {
            log.println("Successful in " + guessCount + " guesses on " + strDate);
        } catch (
                FileNotFoundException fnfe) {
            System.out.println("*** Unable to open log file: " + logFile.getAbsolutePath());
        }
    }

    private void logFailResults(int guessCount ) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String auditPath = "resultsLog.txt";
        File logFile = new File(auditPath);
        try (PrintWriter log = new PrintWriter(new FileOutputStream(logFile, true))) {
            log.println("Unsuccessful on " + strDate);
        } catch (FileNotFoundException e) {
            System.out.println("*** Unable to open log file: " + logFile.getAbsolutePath());
        }
    }

    private void displayPastResults(){
        String filePath = "resultsLog.txt";
        File logFile = new File(filePath);
        try (Scanner fileInput = new Scanner(logFile)) {
            while (fileInput.hasNextLine()) {
                System.out.println(fileInput.nextLine());
            }
        }catch (FileNotFoundException fnfe) {
            System.out.println("The file was not found: " + logFile.getAbsolutePath());
        }
    }

}
