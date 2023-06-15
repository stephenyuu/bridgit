import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;

import javalib.worldimages.*;

//--------------------------- GAME CONSTANTS INTERFACE ------------------------

//represents game constants 
interface IGameConstants {
  // tile dimensions
  static int TILE_SIZE = 30; // width/height of each tile

  // game dimensions
  static int EDGE_OFFSET = TILE_SIZE * 2; // offset of board from edge

  // player colors
  static Color P1_COLOR = Color.PINK;
  static Color P2_COLOR = Color.MAGENTA;
}

//--------------------------- BRIDGIT CLASS -----------------------------------

//represents Bridgit game
class Bridgit extends World implements IGameConstants {
  int size;
  boolean isP1sTurn; // represents if it is player1's turn
  Board board;
  Utils utils;

  //constructor  
  Bridgit(int size) {
    this.utils = new Utils();
    this.size = this.utils.validSize(size);
    this.board = new Board(this.size);
    this.isP1sTurn = true;
  }

  /* TEMPLATE:
   * fields:
   *  this.size ... int
   *  this.isP1sTurn ... boolean
   *  this.board ... Board
   *  this.utils ... Utils
   * methods:
   *  this.onMouseReleased ... void
   *  this.makeScene ... WorldScene
   *  this.lastScene ... WorldScene
   * methods for fields:
   *  this.board.initBoard ... void
   *  this.board.getClicked ... Cell
   *  this.board.draw ... void
   *  this.board.leftRightPathExists ... boolean
   *  this.board.topBottomPathExists ... boolean
   *  this.utils.validSize ... int
   *  this.utils.findPathTopBottom ... boolean
   *  this.utils.findPathLeftRight ... boolean
   */

  public void onMouseReleased(Posn pos) {
    Cell temp = this.board.getClicked(pos);

    // fill clicked Cell if valid
    if (this.isP1sTurn && !(temp.filled)) {
      temp.setColor(P1_COLOR);
      this.isP1sTurn = false;
    }
    else if (!(this.isP1sTurn) && !(temp.filled)) {
      temp.setColor(P2_COLOR);
      this.isP1sTurn = true;
    }

    // check win condition
    if (this.board.leftRightPathExists(this.utils)) {
      this.endOfWorld("Player 1 Has Won");
    }
    else if (this.board.topBottomPathExists(this.utils)) {
      this.endOfWorld("Player 2 Has Won");
    }
  }

  // makeScene function
  public WorldScene makeScene() {
    //create new empty WorldScene
    WorldScene scene = this.getEmptyScene();
    //draw board onto scene
    this.board.draw(scene);
    //draw outline of the board onto the scene
    scene.placeImageXY(new RectangleImage(
        this.size * TILE_SIZE, this.size * TILE_SIZE, OutlineMode.OUTLINE, Color.BLACK),
        EDGE_OFFSET + this.size * TILE_SIZE / 2, EDGE_OFFSET + this.size * TILE_SIZE / 2);
    return scene;
  }

  // lastScene function
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.makeScene();
    scene.placeImageXY(new TextImage(msg, TILE_SIZE / 2, FontStyle.BOLD, Color.BLACK),
        EDGE_OFFSET + this.size * TILE_SIZE / 2, EDGE_OFFSET + this.size * TILE_SIZE / 2);
    return scene;
  }
}

//--------------------------- EXAMPLES CLASS ----------------------------------

//represents examples and tests 
class ExamplesBridgit implements IGameConstants {
  //tests for Bridgit Constructor
  void testBridgitConstructor(Tester t) {
    t.checkConstructorException(new IllegalArgumentException(
        "Size must be an odd number greater than or equal to 3!"),
        "Bridgit", 0);
    t.checkConstructorException(new IllegalArgumentException(
        "Size must be an odd number greater than or equal to 3!"),
        "Bridgit", 8);
    t.checkConstructorException(new IllegalArgumentException(
        "Size must be an odd number greater than or equal to 3!"),
        "Bridgit", -3);
    t.checkExpect((new Bridgit(3).size), 3);
    t.checkExpect((new Bridgit(13).size), 13);
  }

  //tests for makeScene
  void testMakeScene(Tester t) {
    Bridgit game1 = new Bridgit(3);
    Board board1 = new Board(3);

    Bridgit game2 = new Bridgit(7);
    Board board2 = new Board(7);

    WorldScene scene1 = game1.getEmptyScene();
    board1.draw(scene1);
    scene1.placeImageXY(new RectangleImage(
        3 * TILE_SIZE, 3 * TILE_SIZE, OutlineMode.OUTLINE, Color.BLACK),
        EDGE_OFFSET + 3 * TILE_SIZE / 2, EDGE_OFFSET + 3 * TILE_SIZE / 2);

    WorldScene scene2 = game2.getEmptyScene();
    board2.draw(scene2);
    scene2.placeImageXY(new RectangleImage(
        7 * TILE_SIZE, 7 * TILE_SIZE, OutlineMode.OUTLINE, Color.BLACK),
        EDGE_OFFSET + 7 * TILE_SIZE / 2, EDGE_OFFSET + 7 * TILE_SIZE / 2);

    t.checkExpect(game1.makeScene(), scene1);
    t.checkExpect(game2.makeScene(), scene2);
  }

  //tests for lastScene
  void testLastScene(Tester t) {
    Bridgit game1 = new Bridgit(3);
    Bridgit game2 = new Bridgit(7);

    WorldScene scene1 = game1.makeScene();
    scene1.placeImageXY(new TextImage("lol", TILE_SIZE / 2, FontStyle.BOLD, Color.BLACK),
        EDGE_OFFSET + 3 * TILE_SIZE / 2, EDGE_OFFSET + 3 * TILE_SIZE / 2);

    WorldScene scene2 = game1.makeScene();
    scene2.placeImageXY(new TextImage("idk", TILE_SIZE / 2, FontStyle.BOLD, Color.BLACK),
        EDGE_OFFSET + 7 * TILE_SIZE / 2, EDGE_OFFSET + 7 * TILE_SIZE / 2);    

    t.checkExpect(game1.lastScene("lol"), scene1);
    t.checkExpect(game2.lastScene("idk"), scene2);
  }

  //run the game
  void testBigBang(Tester t) {
    int size = 7;

    Bridgit game = new Bridgit(size);
    game.bigBang(size * TILE_SIZE + 2 * EDGE_OFFSET, size * TILE_SIZE + 2 * EDGE_OFFSET);
  }
}