import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {

    static char[][] mainArr = new char[3][3];
    private Scanner userIn = new Scanner(System.in);
    private char player = 'o';
    private char bot = 'x';
    private boolean botTurn = false;
    private ArrayList<TheResponse> responses = new ArrayList<>();
    

    public boolean isBotTurn() {
        return botTurn;
    }


    public TicTacToe() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                mainArr[i][j] = ' ';

            }                    
        }

    }
    

    private void displayMessage() {

        int c = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if( j != 2) 
                System.out.print(" "+c+" |");
               else
                System.out.print(" "+c+" ");
                c++;

            }
            System.out.println();
            if( i != 2) System.out.println("-----------");
            
        }


    }

    private void input(char input) {

        int inp = userIn.nextInt();

        int c = 1;

        outerLoop:
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (c == inp && mainArr[i][j] == ' ') {
                    mainArr[i][j] = input;
                    break outerLoop;
                }             
                c++;
                              
            }
                   
        }

        displayGame();
        
    }


    public void GamePLay() {

        displayMessage();

        int turn = (int)(Math.random()*10);

        while (!arrayFullCheck()) {

            if( turn % 2 == 0) {
                input(player);
                botTurn = false;
            } 
            if( turn % 2 != 0) {
                theBotPlays();
                botTurn = true;
            } 
            turn++;

            if(GameWinCheck()) break;
        }


    }

    //checks "future" winning condition based on the argument(bot or player)
    private boolean botDecides(char decider) {

        if(mainArr[1][1] == ' ') {
            mainArr[1][1] = bot;
            displayGame();
            return true;
        }

        //horzental check
        for (int i = 0; i < 3; i++) {

            int count = 0;

            for (int j = 0; j < 3; j++) {

                if(mainArr[i][j] == decider) count++;

            }

            if(count == 2) {
                for (int j = 0; j < 3; j++) {
                    if(mainArr[i][j] == ' ') {
                        mainArr[i][j] = bot;
                        displayGame();
                        return true;
                    }
                    
                }
            }
        }

        //verticle check
        for (int i = 0; i < 3; i++) {

            int count = 0;

            for (int j = 0; j < 3; j++) {

                if(mainArr[j][i] == decider) count++;

            }

            if(count == 2) {
                for (int j = 0; j < 3; j++) {
                    if(mainArr[j][i] == ' ') {
                        mainArr[j][i] = bot;
                        displayGame();
                        return true;
                    } 
                }
            }
        }

        //top left diagnal
        int count = 0;

        for (int i = 0; i < 3; i++) {        

            if (mainArr[i][i] == decider) count++;


            if(count == 2) {
                for (int j = 0; j < 3; j++) {
                    if(mainArr[j][j] == ' ') {
                        mainArr[j][j] = bot;
                        displayGame();
                        return true;
                    } 
                }
            }

        }

        //top right diagnal
        int count2 = 0;

        for (int i = 0; i < 3; i++) {        

            if (mainArr[i][2-i] == decider) count2++;


            if(count2 == 2) {
                for (int j = 0; j < 3; j++) {
                    if(mainArr[j][2-j] == ' ') {
                        mainArr[j][2-j] = bot;
                        displayGame();
                        return true;
                    } 
                }
            }

        }

        return false;

    }


    private void theBotPlays() {

       //bot check if winning move exists
        if(!botDecides(bot)) {
            //checks if the counter moves exits
            if(!botDecides(player)) {

                //else it then "thinks of the next move"
                for (int i = 0; i < 3; i++) {

                    for (int j = 0; j < 3; j++) {

                        if(mainArr[i][j] == ' ') {                            
                            emptyCellCalculates(i, j);                           
                        }
                        
                    }
                }

                reaction();
                displayGame();
            }
        }

    }

    private void reaction() {

        int priority = 5;

        for(TheResponse r : responses) {
            if(r.response < priority) priority = r.response;
        }

        ArrayList<TheResponse> selectedOnes = new ArrayList<>();

        for(TheResponse r : responses) {
            if(r.response == priority) {
                selectedOnes.add(r);
            }
        }

        int max = selectedOnes.size()-1;

        int rand = (int) (Math.random() * (max + 1));

        int R = selectedOnes.get(rand).row;
        int C = selectedOnes.get(rand).col;

        mainArr[R][C] = bot;


        responses.clear();



    }

    private void emptyCellCalculates(int y, int x) {

        boolean cornerCheck = false;

        int cell = getCellNumber(y, x);

        if(cell == 1 || cell == 3 || cell == 7 || cell == 9)
            cornerCheck = true;

        boolean oneWayAttack = false;
        boolean twoWayAttack = false;
        boolean twoWayDefence = false;

        
        TheRow colStuff = new TheRow();

        for (int i = 0; i < 3; i++) {
            if(i != x) {
                if(mainArr[y][i] == ' ') colStuff.Bs++;
                if(mainArr[y][i] == bot) colStuff.Xs++;
                if(mainArr[y][i] == player) colStuff.Os++;
            }
        }

        TheRow rowStuff = new TheRow();

        for (int i = 0; i < 3; i++) {
            if(i != y) {
                if(mainArr[i][x] == ' ') rowStuff.Bs++;
                if(mainArr[i][x] == bot) rowStuff.Xs++;
                if(mainArr[i][x] == player) rowStuff.Os++;
            }
        }

        if((colStuff.Xs == 1 && colStuff.Bs == 1) || (rowStuff.Xs == 1 && rowStuff.Bs == 1)) oneWayAttack = true;
        if((colStuff.Xs == 1 && colStuff.Bs == 1) && (rowStuff.Xs == 1 && rowStuff.Bs == 1)) twoWayAttack = true;
        if(cornerCheck && colStuff.Bs == 2 && rowStuff.Bs == 2) twoWayAttack = true;
        if((colStuff.Os == 1 && colStuff.Bs == 1) && (rowStuff.Os == 1 && rowStuff.Bs == 1)) twoWayDefence = true;


        TheResponse response = new TheResponse();

        if(cornerCheck && mainArr[1][1] != bot) {
            response.row = y;
            response.col = x;
            response.response = 1;
            responses.add(response);
            return;
        }

        if(twoWayAttack) {
            response.row = y;
            response.col = x;
            response.response = 1;
            responses.add(response);
            return;
        }

        if(oneWayAttack) {
            response.row = y;
            response.col = x;
            response.response = 2;
            responses.add(response);
            return;
        }

        if(twoWayDefence) {
            response.row = y;
            response.col = x;
            response.response = 3;
            responses.add(response);
            return;
        }

        if((colStuff.Xs == 1 && colStuff.Os == 1) || (rowStuff.Xs == 1 && rowStuff.Os == 1)) {
            response.row = y;
            response.col = x;
            response.response = 4;
            responses.add(response);
            return;
        }

        if(!cornerCheck && (colStuff.Bs == 2 || rowStuff.Bs == 2)) {
            response.row = y;
            response.col = x;
            response.response = 4;
            responses.add(response);
            return;
        }

        if((colStuff.Xs == 1 && colStuff.Os == 1) && (rowStuff.Xs == 1 && rowStuff.Os == 1)) {
            response.row = y;
            response.col = x;
            response.response = 5;
            responses.add(response);
            return;
        }

        if(!cornerCheck && colStuff.Bs == 2 && rowStuff.Bs == 2) {
            response.row = y;
            response.col = x;
            response.response = 5;
            responses.add(response);
            return;
        }

    }

    private int getCellNumber(int row , int col) {
        
        int c = 1;

        outerLoop:
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (i == row && j == col) {                  
                    break outerLoop;
                }             
                c++;
                              
            }
                   
        }

        return c;


    }


    private void displayGame() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

               if( j != 2) 
                System.out.print(" "+mainArr[i][j]+" |");
               else
                System.out.print(" "+mainArr[i][j]+" ");


            }
            
            System.out.println();
            if( i != 2) System.out.println("-----------");
            
        }
    }


    private boolean GameWinCheck() {


        boolean check = false;

        for (int i = 0; i < 3; i++) {
            if(mainArr[i][0] != ' ')
                if(mainArr[i][0] == mainArr[i][1] && mainArr[i][0] == mainArr[i][2]) check = true;
        }

        for (int i = 0; i < 3; i++) {

            if(mainArr[0][i] != ' ')
                if(mainArr[0][i] == mainArr[1][i] && mainArr[0][i] == mainArr[2][i]) check = true;
        }

        if(mainArr[0][0] != ' ')
            if(mainArr[0][0] == mainArr[1][1] &&  mainArr[0][0] == mainArr[2][2]) check = true;

        if(mainArr[0][2] != ' ')
            if(mainArr[0][2] == mainArr[1][1] && mainArr[0][2] == mainArr[2][0]) check = true;


        return check;


    }


    private boolean arrayFullCheck() {

        boolean check = true;

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

               if(mainArr[i][j] == ' ') check = false;

            }        
            
        }

        return check;


    }

   
}
