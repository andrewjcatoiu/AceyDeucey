import java.net.Socket; 
import java.io.IOException; 
import java.io.DataInputStream; 
import java.io.DataOutputStream;
import java.util.*;

public class Acey {
    public static void main(String[] args) {
        // Validates for IpAddress and IpPort
        if  (args.length != 2) {
            System.out.println("Enter 2 args: <<IpAddress>> <<IpPort>>");
        }

        // Stores args in variables
        String IpAddress = args[0];
        int IpPort = Integer.parseInt(args[1]);

        try {
            Socket socket = new Socket(IpAddress, IpPort); // Variables become paramters for socket
            DataInputStream dis = new DataInputStream(socket.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String message = read(dis); // Obtains commands from dealer
            System.out.println(message);

            ArrayList<String> cards = new ArrayList<>(); // Initializes an ArrayList of all card values
            String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
            for (int i = 0; i < 7; i++) { // Takes into account there are 7 decks
                for (int x = 0; x < 4; x++) { // Takes into account there are 4 suits
                    for (String rank : ranks) {
                        cards.add(rank);
                    }
                }
            }
            
            while (!message.contains("done")) { // Loops until dealer gives 'done' command  
                
                if (message.equals("login")) { // Writes necessary info to dealer after reading 'login' command
                    write(dos, "ajcatoiu:Andrew");
                } 
                else if (message.contains("play")) { // Reads 'play' command and writes the method of betting (mid, high, low) and how much to bet

                    String[] pParts = message.split(":"); // Parses play command
                    int bet = 1; // Constant bet for now
                    int pot = Integer.parseInt(pParts[1]);
                    int stack = Integer.parseInt(pParts[2]);
                    String firstCard = pParts[3];
                    String secCard = pParts[4];
                    System.out.println("Pot: " + pot + "; Stack: " + stack + "; Card #1: " + firstCard + "; Card #2: " + secCard); // Updates every game
                    
                    String valFirstCard = firstCard.substring(0, 2); // Value '10' in the string of the card is the only value to have a length of 2
                    if (!firstCard.contains("10")) { // If value of the card is not of length 2 or is written as a letter in the string of the card
                        valFirstCard = firstCard.substring(0, 1);
                        if (valFirstCard.equals("A")){
                            valFirstCard = "1";
                        } else if (valFirstCard.equals("J")) {
                            valFirstCard = "12";
                        } else if (valFirstCard.equals("Q")) {
                            valFirstCard = "13";
                        } else if (valFirstCard.equals("K")) {
                            valFirstCard = "14";
                        }
                    }
                    int valInt1 = Integer.parseInt(valFirstCard);

                    String valSecCard = secCard.substring(0, 2); // Same logic as code for valFirstCard
                    if (!secCard.contains("10")) {
                        valSecCard = secCard.substring(0, 1);
                        if (valSecCard.equals("A")){
                            valSecCard = "1";
                        } else if (valSecCard.equals("J")) {
                            valSecCard = "12";
                        } else if (valSecCard.equals("Q")) {
                            valSecCard = "13";
                        } else if (valSecCard.equals("K")) {
                            valSecCard = "14";
                        }
                    }
                    int valInt2 = Integer.parseInt(valSecCard);


                    ArrayList<String> values = new ArrayList<>(); // ArrayList for all cards dealt
                    for (int i = 6; i < pParts.length; i++) { // Loops through all cards dealt in the play command
                        String part = pParts[i];
                        String dealtCard = part.substring(0, 2); // If the value is '10'
                        if (!part.contains("10")) { // Every other value has string length of 1
                            dealtCard = part.substring(0, 1);
                        }
                        values.add(dealtCard); // Adds each dealt card to ArrayList
                    }
                    for (String s : values) { // Removes value from 'cards' ArrayList if value is in 'values' ArrayList
                        cards.remove(s);
                    }


                    if (valInt1 != valInt2) { // Different card values means betting 'mid'
                        
                        int a = 0;
                        int b = 0;
                        if (valInt1 > valInt2) { // Checking which value is lower for looping purposes
                            b = valInt1;
                            a = valInt2;
                        } else if (valInt1 < valInt2) {
                            b = valInt2;
                            a = valInt1;
                        }

                        double rangeCount = 0;
                        for (int i = a + 1; i < b; i++) { // Limits exclude the card values dealt
                            String card = "";
                            switch (i) { // Ace and King cards are impossible for betting
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
                            rangeCount += Collections.frequency(cards, card); // Checks how many available cards there are in the 'cards' ArrayList
                        }

                        double size = cards.size();
                        double prob = (double) (rangeCount / size); // Probability of betting a winning card is the count of possible winning cards divided by remaining possible cards to be dealt
                        if (prob > 0.9) { // Assigning bet amounts for probability ranges
                            bet = stack / 4;
                        } else if (prob > 0.84) {
                            bet = 4;
                        } else if (prob > 0.7) {
                            bet = 3;
                        } else if (prob > 0.5) {
                            bet = 2;
                        } else {
                            bet = 1;
                        }

                        if (pot < bet) { // Resolves illegal bet of betting more than your stack
                            bet = pot;
                        }
                        if (stack < bet) {
                            bet = stack;
                        }

                        write(dos, "mid:" + Integer.toString(bet)); // Method of betting is mid if the card values are different

                    }


                    if (valInt1 == valInt2) { // Method of betting is either high or low
                        
                        double lowerCount = 0;
                        for (int i = 1; i < valInt1; i++) {
                            String card = "";
                            switch (i) { // King is highest possible card for finding count of all cards lower, if betting low
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
                            lowerCount += Collections.frequency(cards, card); // Total amount of available cards that are less than a certain value
                        }
                        double lowSize = cards.size();
                        double lowProb = (double) (lowerCount / lowSize); // Probability of betting a winning card is the count of possible winning lower cards divided by remaining possible cards to be dealt
                        
                        double higherCount = 0;
                        for (int i = valInt1 + 1; i < 15; i++) {
                            String card = "";
                            switch (i) { // Ace is lowest possible card for finding count of all cards higher, if betting high
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
                            higherCount += Collections.frequency(cards, card); // Total amount of available cards that are greater than a certain value
                        }
                        double highSize = cards.size();
                        double highProb = (double) (higherCount / highSize); // Probability of betting a winning card is the count of possible winning higher cards divided by remaining possible cards to be dealt

                        if (lowProb >= highProb) { // If greater probability of winning when betting low, then bet low
                            if (lowProb > 0.9) { // Assign bet amounts for probability ranges
                                bet = pot / 4;
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
                            write(dos, "low:" + Integer.toString(bet)); // Write low bet to server
                        } else { // If greater probability of winning when betting high, then bet high
                            if (highProb > 0.9) { // Assign bet amounts for probability ranges
                                bet = pot / 4;
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
                            write(dos, "high:" + Integer.toString(bet)); // Write high bet to server
                        } 
                    }

                    if (cards.size() < 5) { // Refreshes the deck if nearly all the cards have been dealt
                        cards.clear(); // Empties previous ArrayList
                        String[] cardVals = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
                        for (int i = 0; i < 7; i++) { // Takes into account there are 7 decks
                            for (int x = 0; x < 4; x++) { // Takes into account there are 4 suits
                                for (String val : cardVals) {
                                    cards.add(val);
                                }
                            }
                        }
                    }

                }

                message = read(dis); // Next command is read; put at the bottom because there is another command that happens before the loop
                System.out.println(message); // Command needs to be parsed before next command can be read

            }

            dis.close();
            dos.close();
            socket.close(); // Closing socket at end of loop prevents further exceptions

        } catch (IOException e) { // Catches IOException
            e.printStackTrace();
        }
    }
    

    private static void write(DataOutputStream dos, String s) throws IOException { // Replying to dealer
        dos.writeUTF(s); 
        dos.flush(); 
    } 
    

    private static String read(DataInputStream dis) throws IOException { // Reading info from dealer
        return dis.readUTF(); 
    } 
    
}