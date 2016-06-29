/*Name: Kuangcong Liu
 *Login:cs8baoj
 *Date: 01/23/2015
 *File: GameManager.java
 *Description:
 *   In this program, we simply read the command the user provides and
 *   construct different boards according to the user's commands.
 *   If the user gives wrong command, we need to prompt him by printing
 *   instructions.
 */
//------------------------------------------------------------------//
// GameManager.java                                                 //
//                                                                  //
// Game Manager for 2048                                            //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//

import java.util.*;
import java.io.*;

/*
 * Name: GameManager class
 * Purpose: we make a class including the board we need to display
 *         and the filename we need to save our file as.
 * Parameters: None.
 * Return: Void.
 */

public class GameManager
{
    private Board board;
    private String outputFileName;

    /* Constructor: GameManager
     * Purpose: Generate a new game
     * Parameter: int boardSize, represents the size of our board
     *            String outputBoard, represents the file name of the output
     *            Random random, reoresents the random variable
     * Return: void
     */
    public GameManager(int boardSize, String outputBoard, Random random)
    {
        this.outputFileName=outputBoard;

        //as same as the first board constructor.
        this.board=new Board(boardSize, random);
    }

    /* Constructor: GameManager
     * Purpose: load a board using the filename passed in parameter.
     * Parameter: int boardSize, represents the size of our board
     *            String outputBoard, represents the file name of the input
     *            Random random, reoresents the random variable
     * Return: void
     */
    public GameManager(String inputBoard, String outputBoard, Random random)
        throws IOException
        {
            this.outputFileName=outputBoard;

            //as same as the second board constructor.
            this.board=new Board(inputBoard,random);
        }

    /*
     * Name: play method
     * Purpose: in this method, we read from the user command and check it
     *     whether it is valid. If it is valid, we need to check again
     *     whether the move is valid. If both of them are valid, we could
     *     move according to the giving direction.
     * Parameters:None.
     * Return:void.
     */

    public void play() throws IOException
    {
        //print out the controls used to operate the game.
        this.printControls();

        //print out the current state of the 2048 board
        System.out.println(this.board);

        //prompt the user for a command
        Scanner input=new Scanner(System.in);
        System.out.print(">");

        //use hasNext() to decide whether there is a command input
        while(input.hasNext()){
            String command=input.next();
            //check whether the user enters a valid move "w"
            if(command.equals("w")){
                //check if that move is valid
                if(this.board.canMove(Direction.UP)){
                    //if it is valid we will need to move it
                    this.board.move(Direction.UP);

                    //after every move, we need to add a random tile.
                    this.board.addRandomTile();
                }
            }

            //the followings are similiar with the first one, depending on
            //different command.
            else if(command.equals("s")){
                if(this.board.canMove(Direction.DOWN)){
                    this.board.move(Direction.DOWN);
                    this.board.addRandomTile();
                }
            }
            else if(command.equals("a")){
                if(this.board.canMove(Direction.LEFT)){
                    this.board.move(Direction.LEFT);
                    this.board.addRandomTile();
                }
            }
            else if(command.equals("d")){
                if(this.board.canMove(Direction.RIGHT)){
                    this.board.move(Direction.RIGHT);
                    this.board.addRandomTile();
                }
            }
            //If any invalid commands are received
            else if(!command.equals("q")){
                //print the controls again
                printControls();
            }

            //If the user decides to quit with command q or the game is over
            if(command.equals("q")||this.board.isGameOver()){
                //we will save the board to the output file specified by
                //outputFileName
                this.board.saveBoard(this.outputFileName);

                //then exit the method
                System.exit(-1);
            }

            //the updated board will be printed to the screen again
            System.out.println(this.board);

            //prompt the user for another command.
            System.out.print(">");
        }
    }
    // Print the Controls for the Game
    private void printControls()
    {
        System.out.println("  Controls:");
        System.out.println("    w - Move Up");
        System.out.println("    s - Move Down");
        System.out.println("    a - Move Left");
        System.out.println("    d - Move Right");
        System.out.println("    q - Quit and Save Board");
        System.out.println();
    }
}
