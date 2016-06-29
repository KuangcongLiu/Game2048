/*Name:Kuangcong Liu
 *Login:cs8baoj
 *Date: 02/27/2015
 *File: Gui2048.java
 *Description: This file creates the gui for 2048, which contains two inner
 * classes, keyHandler and Tile. KeyHandler focuses on the key event from
 * the key board and Tile has basic properties such as the text and
 * rectangle's color on each tile.
 * I also did the extra credit to make it resizable.
 */

/** Gui2048.java */
/** PA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*
 * Name: Gui048 class
 * Purpose: this class makes the gui for 2048, which could show the basic
 *   model of 2048 board, move the board and update the board.
 * Return:Void.
 */
public class Gui2048 extends Application
{
    ////////////////fields////////////////////
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218,0.73);

    // For tiles >= 8
    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
    // For tiles < 8
    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);

    /** Add your own Instance Variables here */
    private GridPane gridPane;
    private Tile tile;
    private int size;
    private Font font1;
    private Font font2;
    private Font font3;

    /*
     * Name: start method
     * Purpose: we do every performation here, normally places UI controls
     *   in a scene and displays the scene in a stage.
     * Parameters: Stage primaryStage, which represents the stage we need to
     *   perform our board on.
     * Return: void.
     */
    @Override
        public void start(Stage primaryStage)
        {
            // Process Arguments and Initialize the Game Board
            processArgs(getParameters().getRaw().toArray(new String[0]));

            /** Add your Code for the GUI Here */
            //make gridPane to be an object.
            gridPane=new GridPane();

            //using this method to set all the basic information about the
            //board, such as the background and the title.
            setGridPane();

            //use this to set our exact tiles on the board.
            setTile();

            //handle the key event.
            KeyHandler handle1=new KeyHandler();

            //make a scene for the gridPane
            Scene scene=new Scene(gridPane);

            //set Title.
            primaryStage.setTitle("Gui2048");
            primaryStage.setScene(scene);

            //handle the key event.
            scene.setOnKeyPressed(handle1);

            //let the scene to be shown.
            primaryStage.show();
        }

    /** Add your own Instance Methods Here */

    /*
     * Name: setBasicFont method
     * Purpose: we use this method to set basic information about the font,
     *   such as their sizes and forms.
     * Parameters: none.
     * Return: void.
     */
    public void setBasicFont(){
        //create two magic numbers, actually I tried several times to
        //set the proper size of the font
        int sizeForOver=2;
        int fontSize;

        //we need to check whether the height is longer than the width
        //the size of the font is determined by the smaller one.
        if(gridPane.getHeight()>gridPane.getWidth())
            //when width is smaller, the size depends on width
            fontSize=(int)(gridPane.getWidth()/(sizeForFont*size));
        else
            //when height is smaller, the size depends on height
            fontSize=(int)(gridPane.getHeight()/(sizeForFont*size));

        //assign the formats and sizes to different members
        //font1 is for numbers on the tiles and score.
        font1=Font.font("Times New Roman",FontWeight.BOLD,fontSize);

        //font2 is for "2048"
        font2=Font.font("Helvetica Neue",FontWeight.BOLD,
                FontPosture.ITALIC,fontSize);
        //font 3 is for "Game Over".
        font3=Font.font("Times New Roman",FontWeight.BOLD,
                fontSize*sizeForOver);
    }

    /*
     * Name: setGridPane method
     * Purpose: this method sets basic information about the gridPane
     *   such as the background and the score
     * Parameters: none.
     * Return: void.
     */
    public void setGridPane(){
        //create a magic number, since the "2048" and score should
        //divide the whole width separately.
        int half=2;

        //set the font.
        setBasicFont();

        //since we have hgap and vgap among tiles we need to divide the whole
        //gridPane one more than we expected.
        size=board.GRID_SIZE+1;

        //set how much hgap and vgap should take.
        //and their length should depend on the width and height of the pane.
        int sizeH=size*size;
        int sizeV=(size+1)*(size+1);
        gridPane.hgapProperty().bind(gridPane.widthProperty().divide(sizeH));
        gridPane.vgapProperty().bind(gridPane.heightProperty().divide(sizeV));

        //set the padding of the gridPane.
        //they should depend on the width and height of the gridPane.
        double VP=gridPane.getHeight()/sizeV;
        double HP=gridPane.getWidth()/sizeH;
        gridPane.setPadding(new Insets(VP,HP,VP,HP));

        //set the background color.
        //using different magic numbers in a string.
        String backS=new String("-fx-background-color: rgb(187,173,160)");
        gridPane.setStyle(backS);

        //set the label "2048" and set its font
        Label lab=new Label("2048");
        lab.setFont(font2);

        //add the "2048" to the first row and first half columns of the pane.
        gridPane.add(lab,0,0,board.GRID_SIZE/half,1);

        //set it to remain in the center
        GridPane.setHalignment(lab, HPos.CENTER);
        GridPane.setValignment(lab, VPos.CENTER);

        //convert the score from int to string
        //and set its font
        String scoreStr=String.valueOf(board.getScore());
        Label score=new Label("Score: "+scoreStr);
        score.setFont(font1);

        //add the score to the first row and the last half columns of the pane.
        gridPane.add(score,board.GRID_SIZE/half,0,board.GRID_SIZE/half,1);

        //set it to remain in the center.
        GridPane.setHalignment(score, HPos.CENTER);
        GridPane.setValignment(score, VPos.CENTER);
    }

    /*
     * Name: setTile method
     * Purpose: we use this method to fill in the grid Pane with exact board.
     * Parameters: none.
     * Return: void.
     */

    public void setTile(){
        //every time we need to clear the gridPane before
        gridPane.getChildren().clear();

        //reset the basic information about the pane, such as its background.
        setGridPane();

        //use nested loop to create every tile separately.
        for(int i=0;i<board.getGrid().length;i++){
            for(int j=0;j<board.getGrid().length;j++){
                //make new Tile object
                tile=new Tile();

                //create new text and rectangle according to the value
                //we get from the board.
                Text newText=tile.setValue(board.getGrid()[i][j]);
                Rectangle newRec=tile.setColor(board.getGrid()[i][j]);

                //add it to the gridPane and remember the first row is left
                //for the "2048" and score.
                gridPane.add(newRec,j,i+1);
                gridPane.add(newText,j,i+1);

                //set the new text and rectangle to the center.
                GridPane.setHalignment(newRec, HPos.CENTER);
                GridPane.setValignment(newRec, VPos.CENTER);
                GridPane.setHalignment(newText, HPos.CENTER);
                GridPane.setValignment(newText, VPos.CENTER);
            }
        }
    }

    /*
     * Name: setGameOver method
     * Purpose: this method make a new big rectangle and new label
     *   "Game Over" to show that the game is over.
     * Parameters: none.
     * Return: void.
     */

    public void setGameOver(){
        //set magic number about the new rectangle size.
        double over=0.5;
        //another magic number about the span size of the new label and
        //rectangle
        int spanSize=board.GRID_SIZE;

        //create new rectangle and assign color for it.
        Rectangle newOne=new Rectangle();
        newOne.setFill(COLOR_GAME_OVER);

        //create new label of "Game Over" and assign basic font for it.
        Label gameOver=new Label("Game Over!");
        setBasicFont();
        gameOver.setFont(font3);

        //set the size for the rectangle according to the width and height
        //of the pane.
        newOne.widthProperty().bind(gridPane.widthProperty().divide(over));
        newOne.heightProperty().bind(gridPane.heightProperty().divide(over));

        //let the new rectangle and label span over the whole gridPane.
        gridPane.add(newOne,0,0,spanSize,spanSize);
        gridPane.add(gameOver,0,0,spanSize,spanSize);

        //set them to the center.
        GridPane.setHalignment(gameOver, HPos.CENTER);
        GridPane.setValignment(gameOver, VPos.CENTER);
        GridPane.setHalignment(newOne, HPos.CENTER);
        GridPane.setValignment(newOne, VPos.CENTER);
    }

    /*
     * Name:  Tile class
     * Purpose: we create this inner class to set basic information about every
     *   tile, such as its text and rectangle color.
     * Return: void.
     */
    private class Tile{
        ////////////////fields////////////////////
        private Rectangle rec;
        private Text text;

        //////////////constructors//////////////////
        /* Constructor: Tile
         * Purpose: Constructs a new tile, initialize its text and rectangle.
         * Parameter:none
         * Return: void
         */
        public Tile(){

            //initialize some magic number.
            size=board.GRID_SIZE+1;
            int size2=size+1;

            //initialize the rectangle and text
            rec=new Rectangle();
            text=new Text();

            //bind the property of the rectangle to the pane's width and
            //height.
            rec.widthProperty().bind(gridPane.widthProperty().divide(size));
            rec.heightProperty().bind(gridPane.heightProperty().divide(size2));
        }

        /*
         * Name: setValue method
         * Purpose: we set the exact text of the board.
         * Parameters: int valueI, represents the exact value in the board.
         * Return: text, represents the text we need to put on our gridPane.
         */
        public Text setValue(int valueIn){
            //set the basic font.
            setBasicFont();

            //create magic number, since the text should change its color
            //when it is bigger than8.
            int changeColor=8;

            //convert the value from int to string.
            String textValue=String.valueOf(valueIn);

            //if it is not 0, its value should be shown on the grid.
            if(valueIn!=0)
                this.text.setText(textValue);
            else
                //if it is 0, it should be empty.
                this.text.setText("");

            //set the color of the text
            if (valueIn< changeColor)
                text.setFill(COLOR_VALUE_DARK);
            else
                //change color, when the number is bigger than 8
                text.setFill(COLOR_VALUE_LIGHT);

            //set the font of the text and return.
            text.setFont(font1);
            return this.text;
        }

        /*
         * Name: setColor method
         * Purpose: set the color for the rectangle according to the number
         * Parameters: int valueIn, represents the number of the exact board
         * Return: Rectangle, which is set the right color.
         */
        public Rectangle setColor(int valueIn){
            //create magic numbers according to the exact number.
            int num2=2;
            int num4=4;
            int num8=8;
            int num16=16;
            int num32=32;
            int num64=64;
            int num128=128;
            int num256=256;
            int num512=512;
            int num1024=1024;
            int num2048=2048;

            //assign the color to different rectangle.
            if(valueIn==0)
                rec.setFill(COLOR_EMPTY);
            else if(valueIn==num2)
                rec.setFill(COLOR_2);
            else if(valueIn==num4)
                rec.setFill(COLOR_4);
            else if(valueIn==num8)
                rec.setFill(COLOR_8);
            else if(valueIn==num16)
                rec.setFill(COLOR_16);
            else if(valueIn==num32)
                rec.setFill(COLOR_32);
            else if(valueIn==num64)
                rec.setFill(COLOR_64);
            else if(valueIn==num128)
                rec.setFill(COLOR_128);
            else if(valueIn==num256)
                rec.setFill(COLOR_256);
            else if(valueIn==num512)
                rec.setFill(COLOR_512);
            else if(valueIn==num1024)
                rec.setFill(COLOR_1024);
            else if(valueIn==num2048)
                rec.setFill(COLOR_2048);
            else
                rec.setFill(COLOR_OTHER);

            //return the rectangle.
            return this.rec;
        }
    }

    /*
     * Name: KeyHandler class
     * Purpose: we create this class to handle the key we pressed from
     *   key board.
     * Return:Void.
     */
    private class KeyHandler implements EventHandler<KeyEvent>{
        /*
         * Name: handle method
         * Purpose: we override it to handle the key event.
         * Parameters: KeyEvent e, which represents the key we pressed
         * Return: void.
         */
        @Override
            public void handle(KeyEvent e){

                //if the key equals to "RIGHT"
                if (e.getCode() == KeyCode.RIGHT) {
                    //we first check whether we could move it.
                    if(board.canMove(Direction.RIGHT)){
                        //if we can, then move it.
                        board.move(Direction.RIGHT);

                        //after we move, we should add a random tile.
                        board.addRandomTile();

                        //set the tile in format
                        setTile();

                        //print out a line in the terminal.
                        System.out.println("Moving RIGHT");
                    }
                }
                //the following three is just like the first one.
                else if (e.getCode() == KeyCode.DOWN) {
                    if(board.canMove(Direction.DOWN)){
                        board.move(Direction.DOWN);
                        board.addRandomTile();
                        setTile();
                        System.out.println("Moving DOWN");
                    }
                }
                else if (e.getCode() == KeyCode.LEFT) {
                    if(board.canMove(Direction.LEFT)){
                        board.move(Direction.LEFT);
                        board.addRandomTile();
                        setTile();
                        System.out.println("Moving LEFT");
                    }
                }
                else if (e.getCode() == KeyCode.UP) {
                    if(board.canMove(Direction.UP)){
                        board.move(Direction.UP);
                        board.addRandomTile();
                        setTile();
                        System.out.println("Moving UP");
                    }
                }
                //if the user press wrong key
                else if(e.getCode()!=KeyCode.S){
                    //we should print out the introduction.
                    printUsage();
                }

                //if the use press the "S" key
                else if(e.getCode()==KeyCode.S){
                    try{
                        //we should save the board
                        board.saveBoard(outputBoard);

                        //and then print out a line in the terminal.
                        System.out.println("Saving Board to "+outputBoard);
                    } catch(IOException e1){
                        e1.printStackTrace();
                    }
                }

                //check whether it is game over
                if(board.isGameOver()){
                    //if it is over, call this method.
                    setGameOver();
                }
            }
    }

    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                    "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                    "Constructor is broken or the file isn't " +
                    "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }
}                                                                                                                                                       