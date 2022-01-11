// Morgane Bentzinger - 261062953

/*
    This program allows a user to play a game of Bulls and Cows. The player needs to guess a randomly generated secret
    4-digits number. When the player takes a guess, the program reveals the number of digits that match with the secret
    number. If the matching digits are in the correct positions, they are called "bulls", if they are in different
    positions, they are called "cows". After each guess, the program reveals how many bulls and cows the player's guess
    contains.
 */

import java.util.Random;
import java.util.Scanner;

public class BullsAndCows {

    public static void main(String[] args) {
        playBullsAndCows(123);
    }

    /*
        The method contains checks if an element is contained in a given array.
        It takes two inputs:
            - an array of integers
            - an integer (the number we want to check if it exists in the array)
     */
    public static boolean contains(int[] array, int numberToCheck) {
        for(int number : array) {
            if(number == numberToCheck) {
                return true;
            }
        }
        return false;
    }

    /*
        The method generateSecretDigits generates an array of integers containing 4 unique digits between 0 and 9, made up by a randomly generated secret number.
        It takes an integer, used as the seed for the Random class, as an argument.
     */
    public static int[] generateSecretDigits(int seed) {
        Random random = new Random(seed);

        int[] secretDigits = new int[4];

        for(int i = 0; i < secretDigits.length; i++) {
            int nextRandom = random.nextInt(10);
            for(int j = 0; j < i; j++) {
                while(nextRandom == secretDigits[j]) {
                    nextRandom = random.nextInt(10);
                }
            }
            secretDigits[i] = nextRandom;
        }
        return secretDigits;

    }

    /*
        The method convertStringToArray converts the user's input (a string) into an array.
        Takes the user input as an argument.
     */
    public static int[] convertStringToArray(String userInput) {
        int[] numberArr = new int[4];

        for(int i = 0; i < userInput.length(); i++) {
            numberArr[i] = Character.getNumericValue(userInput.charAt(i));
        }
        return numberArr;
    }

    /*
        The method checkArrayValidity checks the validity of the array generated from the user's input.
        It takes two arguments:
            - the secret array created with the method generateSecretDigits
            - the user guess array derived from the user's input, created with the method convertStringToArray
     */
    public static void checkArrayValidity(int[] secret, int[] guess) {
        // check that the guessedDigit array only contains unique values
        for(int i = 0; i < guess.length; i++) {
            for(int j = 0; j < guess.length; j++) {
                if(!(i == j) && guess[i] == guess[j]) {
                    throw new IllegalArgumentException("You are not allowed to repeat the same number!");
                }
            }
        }

        // check that both arrays have the same length
        if(secret.length != guess.length) {
            throw new IllegalArgumentException("You must enter a four-digit number! You just wasted one guess!");
        }
    }

    /*
        The method checkUserInputValidity checks that the user's input can be used in the game.
        It takes the user's input as an argument.
     */
    public static void checkUserInputValidity(String userInput) {
        // check that the user input contains exactly 4 digits.
        if(userInput.length() != 4) {
            throw new IllegalArgumentException("You must enter a four-digit number! You just wasted one guess!");
        }

        // check that the user input contains only numbers between 0 and 9.
        for(int i = 0; i < userInput.length(); i++) {
            if(!(userInput.charAt(i) >= '0' && userInput.charAt(i) <= '9')) {
                throw new IllegalArgumentException("You must enter a four-digit number! You just wasted one guess!");
            }
        }

    }

    /*
        The method getNumOfBulls checks how many digits in the guess array exist in the secret array and are located at the same index in both arrays.
        It takes two arguments:
            - the secret array
            - the guess array
     */
    public static int getNumOfBulls(int[] secret, int[] guess) {
        checkArrayValidity(secret, guess);

        int numberOfBulls = 0;

        for (int i = 0; i < guess.length; i++) {
            if(contains(secret, guess[i])) {
                if(guess[i] == secret[i]) {
                    numberOfBulls++;
                }
            }
        }

        return numberOfBulls;
    }

    /*
        The method getNumOfCows checks how many digits in the guess array exist in the secret array and are located at a different index in the secret array.
        It takes two arguments:
            - the secret array
            - the guess array
     */
    public static int getNumOfCows(int[] secret, int[] guess) {
        checkArrayValidity(secret, guess);

        int numberOfCows = 0;

        for (int i = 0; i < guess.length; i++) {
            if(contains(secret, guess[i])) {
                if(guess[i] != secret[i]) {
                    numberOfCows++;
                }
            }
        }

        return numberOfCows;
    }

    /*
        The method playBullsAndCows runs a game of Bulls and Cows.
        It takes an integer as an argument, to define the seed to use for generating the secret array.
     */
    public static void playBullsAndCows(int seed) {
        int numberOfGuesses = 0;
        int[] secretDigitsArr = generateSecretDigits(seed);
        String playersGuess;
        Boolean gameOver = false;
        String endMessage = "";

        System.out.println("Welcome to Bulls and Cows, and good luck!");

        do {
            numberOfGuesses++;

            Scanner userInput = new Scanner(System.in);
            System.out.println("Guess #" + numberOfGuesses + ": Enter a four-digit number, each digit should be unique.");

            playersGuess = userInput.nextLine();

            try{
                checkUserInputValidity(playersGuess);

                int[] playersGuessArr = convertStringToArray(playersGuess);

                int numBulls = getNumOfBulls(secretDigitsArr, playersGuessArr);
                int numCows = getNumOfCows(secretDigitsArr, playersGuessArr);

                System.out.println("Bulls: " + numBulls);
                System.out.println("Cows: " + numCows);

                if (numBulls == 4) {
                    endMessage = "Congratulations! You guessed the secret number " + playersGuess + " in " + numberOfGuesses + " guesses!";
                    gameOver = true;
                }
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }

            if(numberOfGuesses >= 5 && !gameOver) {
                Scanner continuePlaying = new Scanner(System.in);
                System.out.println("Do you want to quit the game? Answers: y/n");

                String answer = continuePlaying.nextLine();

                if(answer.equals("y")) {
                    endMessage = "You've decided to quit the game after making " + numberOfGuesses + " attempts.";
                    gameOver = true;
                }
            }

        } while(!gameOver);

        System.out.println(endMessage);
    }
}
