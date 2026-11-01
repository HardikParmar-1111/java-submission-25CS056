import java.util.Random;
import java.util.Scanner;

enum Move {
    ROCK, PAPER, SCISSORS, LIZARD, SPOCK
}

public class RPSLS {
    public static int winner(Move a, Move b) {
        return switch (a) {
            case ROCK -> b == Move.SCISSORS || b == Move.LIZARD ? 1
                    : b == Move.PAPER || b == Move.SPOCK ? -1 : 0;
            case PAPER -> b == Move.ROCK || b == Move.SPOCK ? 1
                    : b == Move.SCISSORS || b == Move.LIZARD ? -1 : 0;
            case SCISSORS -> b == Move.PAPER || b == Move.LIZARD ? 1
                    : b == Move.ROCK || b == Move.SPOCK ? -1 : 0;
            case LIZARD -> b == Move.SPOCK || b == Move.PAPER ? 1
                    : b == Move.ROCK || b == Move.SCISSORS ? -1 : 0;
            case SPOCK -> b == Move.SCISSORS || b == Move.ROCK ? 1
                    : b == Move.PAPER || b == Move.LIZARD ? -1 : 0;
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        Move[] moves = Move.values();
        int playerScore = 0;
        int computerScore = 0;
        int round = 1;

        while (round <= 5) {
            System.out.println("Enter ROCK, PAPER, SCISSORS, LIZARD or SPOCK:");
            Move player;

            try {
                player = Move.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move.");
                continue;
            }

            Move computer = moves[random.nextInt(moves.length)];
            int result = winner(player, computer);
            System.out.println("Round " + round + ": You chose " + player
                    + ", computer chose " + computer);

            if (result > 0) {
                playerScore++;
                System.out.println("You win this round");
            } else if (result < 0) {
                computerScore++;
                System.out.println("Computer wins this round");
            } else {
                System.out.println("This round is a tie");
            }
            round++;
        }

        if (playerScore > computerScore) {
            System.out.println("You win " + playerScore + "-" + computerScore);
        } else if (computerScore > playerScore) {
            System.out.println("Computer wins " + computerScore + "-" + playerScore);
        } else {
            System.out.println("Overall tie " + playerScore + "-" + computerScore);
        }
        sc.close();
    }
}
