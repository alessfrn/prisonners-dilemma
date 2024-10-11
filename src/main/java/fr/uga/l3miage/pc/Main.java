package fr.uga.l3miage.pc;

import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to start a new game ? (y/n)");
        String userInput = scanner.next();
        while (!userInput.equals("n")) {
             if (!userInput.equals("y")) {
                System.out.println("y or n only !");
            }
            System.out.println("How many turns should the game last ? (integer) :");
            GameManager gameManager = GameManager.getInstance();
            gameManager.startNewGameRandomStrategy(scanner.nextInt());
            while (gameManager.getActiveGame().getCurrentTurn() < gameManager.getActiveGame().getMaxTurns()) {
                System.out.println("Would you like to betray or cooperate with the neighbouring Tribe ? (b/c)");
                userInput = scanner.next();
                if (!userInput.equals("b") && !userInput.equals("c")) {
                    System.out.println("b or c only !");
                    continue;
                }
                gameManager.getActiveGame().playTurn(userInput.equals("b") ? TribeAction.BETRAY : TribeAction.COOPERATE);
            }
            gameManager.endGame();
            System.out.println("Would you like to start a new game ? (y/n)");
            userInput = scanner.next();
        }
        System.out.println("Goodbye !");
    }
}