public class CardDriver {
    public static void main(String[] args) {
        Card[] cards = new Card[5];
        int count = 0;
        Card[] newCards = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Clubs"),
            new Card("Ace", "Spades"),
            new Card("Ten", "Diamonds")
        };

        for (Card card : newCards) {
            boolean duplicate = false;

            for (int i = 0; i < count; i++) {
                if (card.equals(cards[i])) {
                    System.out.println("Duplicate found: " + card);
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                cards[count] = card;
                count++;
            }
        }
    }
}
