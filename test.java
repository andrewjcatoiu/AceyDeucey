import java.util.*;

public class test {
    public static void main(String[] args) {
        // ArrayList<String> cards = new ArrayList<>();
        // String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        // for (int i = 0; i < 7; i++) {
        //     for (int a = 0; a < 4; a++) {
        //         for (String rank : ranks) {
        //             cards.add(rank);
        //         }
        //     }
        // }
        // System.out.println(cards);




        // int a = 2;
        // int b = 9;
        // for (int i = a + 1; i < b; i++) {
        //     System.out.println(i);
        // }

        int bet = 0;
        int stack = 500;
        int pot = 500;
        ArrayList<String> cards = new ArrayList<>();
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (int i = 0; i < 7; i++) {
            for (int x = 0; x < 4; x++) {
                for (String rank : ranks) {
                    cards.add(rank);
                }
            }
        }

        


        // int a = 1;
        // int b = 6;
        // double rangeCount = 0;
        // for (int i = a + 1; i < b; i++) {
        //     String card = "";
        //     switch (i) {
        //         case 11:
        //             break;
        //         case 12:
        //             card = "J";
        //             break;
        //         case 13:
        //             card = "Q";
        //             break;
        //         case 14:
        //             card = "K";
        //             break;
        //         default:
        //             card = Integer.toString(i);
        //     }
        //     rangeCount += Collections.frequency(cards, card);
            
        // }
        // System.out.print(rangeCount);

        // double size = cards.size();
        // double prob = (double) (rangeCount / size);
        // System.out.println(prob);

        String message = "play:144:354:8H:QH:dealt:AH:QS:AD:4C:3D:2H:9S:4C:KD:JD:AC:QC:4S:QD:QD:9H:QC:9D:KS:QH:3H:6C:3D:2H:KD:JH:AH:8S:QS:QC:JC:9H:8C:7D:10S:AS:7S:9D:10H:5H:AD:2C:AD:8H:6H:KC:7H:2S:4D:7D:JH:2D:JS:AC:4S:7D:10C:4H:5H:6C:10C:6C:10D:6C:7C:QS:5C:AS:9S:3D:6S:4D:JS:7S:6H:JH:3C:10H:QC:7C:8C:3D:AD:KC:8H:4C:AH:4H:10S:10D:8D:10D:7H:2H:8D:2S:8S:JC:JC:8S:8D:5D:3H:4H:AS:KC:4D:9D:8H:JH:10H:7H:2C:7S:4C:9D:5D:8H:7H:6S:6D:9D:AH:4H:3S:5C:5D:9S:4H:KH:9C:QS:3C:3D:6C:7D:JD:4D:6S:3D:7C:6S:AH:KC:5S:3H:KC:QD:2H:10S:5C:7D:5S:JS:QH:6S:4H:JH:5S:6D:2S:AD:9S:2C:AC:3S:5C:7S:2S:KS:5H:9H:6H:7S:4C:8D:10S:5C:AS:JC:KD:9H:3S:JS:2C:3C:7C:AS:JD:4D:7C:9H:KD:4C:JC:JS:AC:JD:KH:KS:8D:10D:10S:QS:6H:4S:2H:QD:QS:4C:6H:2D:6D:KS:AC:QC:5C:8C:JD:9C:5D:7D:10D:9C:4S:3S:9S:7D:9S:8H:QH";
        String[] parts = message.split(":");
        ArrayList<String> values = new ArrayList<>();
        for (int i = 6; i < parts.length; i++) {
            String part = parts[i];
            String dealtCard = part.substring(0, 2);
            if (!part.contains("10")) {
                dealtCard = part.substring(0, 1);
            }
            values.add(dealtCard);
        }
        System.out.println("before removal size " + cards.size() + ": " + cards);
        for (String s : values) {
            cards.remove(s);
        }
        System.out.println("\nafter removal size " + cards.size() + ": " + cards);
        // System.out.println(cards.size());


        int a = 3;
        double lowerCount = 0;
        for (int i = 1; i < a; i++) {
            String card = "";
            switch (i) {
                case 1:
                    card = "A";
                    break;
                case 11:
                    break;
                case 12:
                    card = "J";
                    break;
                case 13:
                    card = "Q";
                    break;
                default:
                    card = Integer.toString(i);
            }
            lowerCount += Collections.frequency(cards, card);
        }
        System.out.println(lowerCount);
        double lowSize = cards.size();
        double lowProb = (double) (lowerCount / lowSize);
        System.out.println(lowProb);


        int b = 3;
        double higherCount = 0;
        for (int i = b + 1; i < 15; i++) {
            String card = "";
            switch (i) {
                case 11: 
                    break;
                case 12:
                    card = "J";
                    break;
                case 13:
                    card = "Q";
                    break;
                case 14:
                    card = "K";
                    break;
                default:
                    card = Integer.toString(i);
            }
            higherCount += Collections.frequency(cards, card);
        }
        System.out.println(higherCount);
        double highSize = cards.size();
        double highProb = (double) (higherCount / highSize);
        System.out.println(highProb);

        if (lowProb >= highProb) {
            if (lowProb > 0.9) {
                bet = 5;
            } else if (lowProb > 0.84) {
                bet = 4;
            } else if (lowProb > 0.7) {
                bet = 3;
            } else if (lowProb > 0.5) {
                bet = 2;
            } else {
                bet = 1;
            }
            if (pot < bet) {
                bet = pot;
            }
            if (stack < bet) {
                bet = stack;
            }
            System.out.println(lowProb + "    " + bet);
            System.out.println("Lower count: " + lowerCount);
        } else {
            if (highProb > 0.9) {
                bet = 5;
            } else if (highProb > 0.84) {
                bet = 4;
            } else if (highProb > 0.7) {
                bet = 3;
            } else if (highProb > 0.5) {
                bet = 2;
            } else {
                bet = 1;
            }

            if (pot < bet) {
                bet = pot;
            }
            if (stack < bet) {
                bet = stack;
            }
            System.out.println("Higher count: " + higherCount);
            System.out.println(highProb + "     " + bet);
        }
    }
}
