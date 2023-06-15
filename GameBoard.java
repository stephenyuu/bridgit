import tester.Tester;
import java.util.ArrayList;
import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldimages.*;

//--------------------------- BOARD CLASS -------------------------------------

// represents the game board
class Board implements IGameConstants {
  ArrayList<Cell> board;
  int size;

  // constructor
  Board(int size) {
    this.initBoard(size);
  }

  /* TEMPLATE:
   * fields:
   *  this.board ... ArrayList<Cell>
   *  this.size ... int
   * methods:
   *  this.initBoard ... void
   *  this.getClicked ... Cell
   *  this.draw ... void
   *  this.leftRightPathExists ... boolean
   *  this.topBottomPathExists ... boolean
   * methods for fields:
   * 
   */

  void initBoard(int size) {
    this.size = size;
    this.board = new ArrayList<Cell>();
    Color cellColor;
    int remainder;

    for (int r = 0; r < this.size; r++) {
      remainder = r % 2;
      if (remainder == 0) {
        cellColor = P2_COLOR;
      }
      else {
        cellColor = P1_COLOR;
      }
      for (int c = 0; c < this.size; c++) {
        if (c % 2 == remainder) {
          this.board.add(new Cell(
              c * TILE_SIZE + TILE_SIZE / 2, r * TILE_SIZE + TILE_SIZE / 2, Color.WHITE));
        }
        else {
          this.board.add(new Cell(
              c * TILE_SIZE + TILE_SIZE / 2, r * TILE_SIZE + TILE_SIZE / 2, cellColor));
        }

        // index of current cell
        int i = r * this.size + c;
        // if row is the first, last, or in the middle...
        if (r == 0) {
          this.board.get(i).fill(); // on edge -> set to filled
          this.board.get(i).setTopNeighbor(new Cell(0, 0, Color.BLACK));
        }
        else if (r == this.size - 1) {
          this.board.get(i).fill(); // on edge -> set to filled
          this.board.get(i).setDownNeighbor(new Cell(0, 0, Color.BLACK));

          this.board.get(i).setTopNeighbor(this.board.get(i - this.size));
          this.board.get(i - this.size).setDownNeighbor(this.board.get(i));
        }
        else {
          this.board.get(i).setTopNeighbor(this.board.get(i - this.size));
          this.board.get(i - this.size).setDownNeighbor(this.board.get(i));
        }

        // if column is the first, last, or in the middle...
        if (c == 0) {
          this.board.get(i).fill(); // on edge -> set to filled
          this.board.get(i).setLeftNeighbor(new Cell(0, 0, Color.BLACK));
        }
        else if (c == this.size - 1) {
          this.board.get(i).fill(); // on edge -> set to filled
          this.board.get(i).setRightNeighbor(new Cell(0, 0, Color.BLACK));

          this.board.get(i).setLeftNeighbor(this.board.get(i - 1));
          this.board.get(i - 1).setRightNeighbor(this.board.get(i));
        }
        else {
          this.board.get(i).setLeftNeighbor(this.board.get(i - 1));
          this.board.get(i - 1).setRightNeighbor(this.board.get(i));
        }
      }
    }
  }

  // get the Cell that's been clicked
  Cell getClicked(Posn p) {
    for (Cell c : this.board) {
      // check if current cell was clicked and return it if true
      if (c.wasClicked(p)) {
        return c;
      }
    }

    // return invalid cell if no cell has been clicked
    return new Cell(0, 0, Color.BLACK);
  }

  // draw this board onto the given scene
  void draw(WorldScene acc) {
    for (Cell c : this.board) {
      acc.placeImageXY(c.getImg(), c.pos.x + EDGE_OFFSET, c.pos.y + EDGE_OFFSET);
    }
  }

  // return if a path from the left edge to the right edge exists
  boolean leftRightPathExists(Utils utils) {
    for (int i = 1; i < this.size; i += 2) {
      if (utils.findPathLeftRight(this.board.get(i * this.size), new ArrayList<Cell>())) {
        return true;
      }
    }
    return false;
  }

  //return if a path from the top edge to the bottom edge exists
  boolean topBottomPathExists(Utils utils) {
    for (int i = 1; i < this.size; i += 2) {
      if (utils.findPathTopBottom(this.board.get(i), new ArrayList<Cell>())) {
        return true;
      }
    }
    return false;
  }
}

//--------------------------- CELL CLASS --------------------------------------

// represents a cell
class Cell implements IGameConstants {
  Color color;
  boolean filled;
  Posn pos;

  // neighbors
  Cell topNeighbor;
  Cell downNeighbor;
  Cell leftNeighbor;
  Cell rightNeighbor;

  // constructor
  Cell(int x, int y, Color color) {
    this.pos = new Posn(x, y);
    this.color = color;
    this.filled = !(this.color.equals(Color.WHITE));
  }

  /* TEMPLATE:
   * fields:
   *  this.color ... Color
   *  this.filled ... boolean
   *  this.pos ... Posn
   *  this.topNeighbor ... Cell
   *  this.downNeighbor ... Cell
   *  this.leftNeighbor ... Cell
   *  this.rightNeighbor ... Cell
   * methods:
   *  this.setTopNeighbor ... void
   *  this.setDownNeighbor ... void
   *  this.setLeftNeighbor ... void
   *  this.setRightNeighbor ... void
   *  this.setColor ... void
   *  this.wasClicked ... boolean
   *  this.fill ... void
   *  this.getImg ... WorldImage
   * methods for fields:
   * 
   */

  // set topNeighbor to given Cell
  void setTopNeighbor(Cell adjacent) {
    this.topNeighbor = adjacent;
  }

  // set downNeighbor to given Cell
  void setDownNeighbor(Cell adjacent) {
    this.downNeighbor = adjacent;
  }

  // set leftNeighbor to given Cell
  void setLeftNeighbor(Cell adjacent) {
    this.leftNeighbor = adjacent;
  }

  // set rightNeighbor to given Cell
  void setRightNeighbor(Cell adjacent) {
    this.rightNeighbor = adjacent;
  }

  // set color to given Color
  void setColor(Color color) {
    this.color = color;
    this.filled = !this.color.equals(Color.WHITE);
  }

  // return if this cell was clicked
  public boolean wasClicked(Posn p) {
    int x = p.x + TILE_SIZE / 2 - EDGE_OFFSET;
    int y = p.y + TILE_SIZE / 2 - EDGE_OFFSET;
    return (x >= this.pos.x) && (x <= (this.pos.x + TILE_SIZE))
        && (y >= this.pos.y) && (y <= (this.pos.y + TILE_SIZE)); 
  }

  // set filled to true
  public void fill() {
    this.filled = true;
  }

  // return the image of this tile
  WorldImage getImg() {
    return new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, this.color);
  }
}

//--------------------------- EXAMPLES CLASS ----------------------------------

// represents examples and tests
class ExamplesGameBoard implements IGameConstants {

  // examples of Cell
  Cell whiteCell;
  Cell magCell;
  Cell pinkCell;

  // examples of Board
  Board board1;
  Board board2;

  // initializes examples
  public void initData() {

    // examples of Cell
    this.whiteCell = new Cell(15, 15, Color.WHITE);
    this.magCell = new Cell(45, 15, P2_COLOR);
    this.pinkCell = new Cell(15, 45, P1_COLOR);

    // examples of Board
    this.board1 = new Board(5);
    this.board2 = new Board(7);
  }

  //------------------------- BOARD METHOD TESTS ------------------------------

  // tests for initBoard
  void testInitBoard(Tester t) {
    this.initData();

    // checks to see if cells in initialized board1 are correct
    t.checkExpect(this.board1.board.get(0).color, Color.WHITE);
    t.checkExpect(this.board1.board.get(0).pos, new Posn(15, 15));
    t.checkExpect(this.board1.board.get(0).rightNeighbor, this.board1.board.get(1));
    t.checkExpect(this.board1.board.get(1).color, P2_COLOR);
    t.checkExpect(this.board1.board.get(1).pos, new Posn(45, 15));
    t.checkExpect(this.board1.board.get(1).rightNeighbor, this.board1.board.get(2));
    t.checkExpect(this.board1.board.get(5).color, P1_COLOR);
    t.checkExpect(this.board1.board.get(5).pos, new Posn(15, 45));
    t.checkExpect(this.board1.board.get(5).leftNeighbor, new Cell(0, 0, Color.BLACK));
    t.checkExpect(this.board1.board.get(5).rightNeighbor, this.board1.board.get(6));
    t.checkExpect(this.board1.board.get(5).downNeighbor, this.board1.board.get(10));
    t.checkExpect(this.board1.board.get(5).topNeighbor, this.board1.board.get(0));

    //checks to see if cells in initialized board2 are correct
    t.checkExpect(this.board2.board.get(2).color, Color.WHITE);
    t.checkExpect(this.board2.board.get(2).pos, new Posn(75, 15));
    t.checkExpect(this.board2.board.get(13).color, P1_COLOR);
    t.checkExpect(this.board2.board.get(13).pos, new Posn(195, 45));
    t.checkExpect(this.board2.board.get(15).color, P2_COLOR);
    t.checkExpect(this.board2.board.get(15).pos, new Posn(45, 75));
    t.checkExpect(this.board2.board.get(15).leftNeighbor, this.board2.board.get(14));
    t.checkExpect(this.board2.board.get(15).rightNeighbor, this.board2.board.get(16));
    t.checkExpect(this.board2.board.get(15).downNeighbor, this.board2.board.get(22));
    t.checkExpect(this.board2.board.get(15).topNeighbor, this.board2.board.get(8));
  }

  // tests for getClicked
  void testGetClicked(Tester t) {
    this.initData();

    t.checkExpect(this.board1.getClicked(new Posn(0, 0)), new Cell(0, 0, Color.BLACK));
    t.checkExpect(this.board1.getClicked(
        new Posn(5 + EDGE_OFFSET, 5 + EDGE_OFFSET)), this.board1.board.get(0));
    t.checkExpect(this.board1.getClicked(
        new Posn(15 + EDGE_OFFSET, 15 + EDGE_OFFSET)), this.board1.board.get(0));
    t.checkExpect(this.board1.getClicked(
        new Posn(15 + EDGE_OFFSET, 45 + EDGE_OFFSET)), this.board1.board.get(5));
    t.checkExpect(this.board1.getClicked(
        new Posn(105 + EDGE_OFFSET, 75 + EDGE_OFFSET)), this.board1.board.get(13));
  }

  // tests for draw
  void testDraw(Tester t) {
    this.initData();

    WorldScene boardScene = new WorldScene(5 * TILE_SIZE + 2 * EDGE_OFFSET, 
        5 * TILE_SIZE + 2 * EDGE_OFFSET);
    for (int r = 0; r < 5; r++) {
      int remainder = r % 2;
      Color cellColor;
      if (remainder == 0) {
        cellColor = P2_COLOR;
      }
      else {
        cellColor = P1_COLOR;
      }
      for (int c = 0; c < 5; c++) {
        if (c % 2 == remainder) {
          boardScene.placeImageXY(new RectangleImage(TILE_SIZE, TILE_SIZE, 
              OutlineMode.SOLID, Color.WHITE),
              c * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET, 
              r * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET);
        }
        else {
          boardScene.placeImageXY(new RectangleImage(TILE_SIZE, TILE_SIZE, 
              OutlineMode.SOLID, cellColor),
              c * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET,
              r * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET);
        }
      }
    }

    WorldScene testScene = new WorldScene(5 * TILE_SIZE + 2 * EDGE_OFFSET, 
        5 * TILE_SIZE + 2 * EDGE_OFFSET);
    t.checkExpect(testScene, testScene);
    this.board1.draw(testScene);
    t.checkExpect(testScene, boardScene);

    this.initData();

    WorldScene boardScene2 = new WorldScene(5 * TILE_SIZE + 2 * EDGE_OFFSET, 
        5 * TILE_SIZE + 2 * EDGE_OFFSET);
    for (int r = 0; r < 7; r++) {
      int remainder = r % 2;
      Color cellColor;
      if (remainder == 0) {
        cellColor = P2_COLOR;
      }
      else {
        cellColor = P1_COLOR;
      }
      for (int c = 0; c < 7; c++) {
        if (c % 2 == remainder) {
          boardScene2.placeImageXY(new RectangleImage(TILE_SIZE, TILE_SIZE, 
              OutlineMode.SOLID, Color.WHITE),
              c * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET, 
              r * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET);
        }
        else {
          boardScene2.placeImageXY(new RectangleImage(TILE_SIZE, TILE_SIZE, 
              OutlineMode.SOLID, cellColor),
              c * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET, 
              r * TILE_SIZE + TILE_SIZE / 2 + EDGE_OFFSET);
        }
      }
    }

    WorldScene testScene2 = new WorldScene(5 * TILE_SIZE + 2 * EDGE_OFFSET, 
        5 * TILE_SIZE + 2 * EDGE_OFFSET);
    t.checkExpect(testScene2, testScene2);
    this.board1.draw(testScene2);
    t.checkExpect(testScene2, boardScene);

  }

  // tests for leftRightPathExists
  void testLeftRightPathExists(Tester t) {
    this.initData();
    Utils utils = new Utils();

    t.checkExpect(this.board1.leftRightPathExists(utils), false);
    this.board1.board.get(6).setColor(P1_COLOR);
    t.checkExpect(this.board1.leftRightPathExists(utils), false);
    this.board1.board.get(8).setColor(P1_COLOR);
    t.checkExpect(this.board1.leftRightPathExists(utils), true);

    this.initData();
    t.checkExpect(this.board1.leftRightPathExists(utils), false);
    this.board1.board.get(6).setColor(P1_COLOR);
    t.checkExpect(this.board1.leftRightPathExists(utils), false);
    this.board1.board.get(12).setColor(P1_COLOR);
    t.checkExpect(this.board1.leftRightPathExists(utils), false);
    this.board1.board.get(18).setColor(P1_COLOR);
    t.checkExpect(this.board1.leftRightPathExists(utils), true);
  }

  //tests for topBottomPathExists
  void testTopBottomPathExists(Tester t) {
    this.initData();
    Utils utils = new Utils();

    t.checkExpect(this.board1.topBottomPathExists(utils), false);
    this.board1.board.get(8).setColor(P2_COLOR);
    t.checkExpect(this.board1.topBottomPathExists(utils), false);
    this.board1.board.get(18).setColor(P2_COLOR);
    t.checkExpect(this.board1.topBottomPathExists(utils), true);

    this.initData();
    t.checkExpect(this.board1.topBottomPathExists(utils), false);
    this.board1.board.get(6).setColor(P2_COLOR);
    t.checkExpect(this.board1.topBottomPathExists(utils), false);
    this.board1.board.get(12).setColor(P2_COLOR);
    t.checkExpect(this.board1.topBottomPathExists(utils), false);
    this.board1.board.get(18).setColor(P2_COLOR);
    t.checkExpect(this.board1.topBottomPathExists(utils), true);
  }

  //------------------------- CELL METHOD TESTS -------------------------------

  // tests for getImg
  void testGetImg(Tester t) {
    this.initData();

    t.checkExpect(this.whiteCell.getImg(), 
        new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, Color.WHITE));
    t.checkExpect(this.magCell.getImg(),
        new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, P2_COLOR));
    t.checkExpect(this.pinkCell.getImg(), 
        new RectangleImage(TILE_SIZE, TILE_SIZE, OutlineMode.SOLID, P1_COLOR));
  }

  // tests for setTopNeighbor
  void testSetTopNeighbor(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.topNeighbor, null);
    t.checkExpect(this.pinkCell.topNeighbor, null);
    t.checkExpect(this.whiteCell.topNeighbor, null);

    this.magCell.setTopNeighbor(magCell);
    this.pinkCell.setTopNeighbor(magCell);
    this.whiteCell.setTopNeighbor(pinkCell);

    t.checkExpect(this.magCell.topNeighbor, magCell);
    t.checkExpect(this.pinkCell.topNeighbor, magCell);
    t.checkExpect(this.whiteCell.topNeighbor, pinkCell);
  }

  // tests for setDownNeighbor
  void testSetDownNeighbor(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.downNeighbor, null);
    t.checkExpect(this.pinkCell.downNeighbor, null);
    t.checkExpect(this.whiteCell.downNeighbor, null);

    this.magCell.setDownNeighbor(magCell);
    this.pinkCell.setDownNeighbor(magCell);
    this.whiteCell.setDownNeighbor(pinkCell);

    t.checkExpect(this.magCell.downNeighbor, magCell);
    t.checkExpect(this.pinkCell.downNeighbor, magCell);
    t.checkExpect(this.whiteCell.downNeighbor, pinkCell); 
  }

  // tests for setLeftNeighbor
  void testSetLeftNeighbor(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.leftNeighbor, null);
    t.checkExpect(this.pinkCell.leftNeighbor, null);
    t.checkExpect(this.whiteCell.leftNeighbor, null);

    this.magCell.setLeftNeighbor(magCell);
    this.pinkCell.setLeftNeighbor(magCell);
    this.whiteCell.setLeftNeighbor(pinkCell);

    t.checkExpect(this.magCell.leftNeighbor, magCell);
    t.checkExpect(this.pinkCell.leftNeighbor, magCell);
    t.checkExpect(this.whiteCell.leftNeighbor, pinkCell); 
  }

  // tests for setRightNeighbor
  void testSetRightNeighbor(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.rightNeighbor, null);
    t.checkExpect(this.pinkCell.rightNeighbor, null);
    t.checkExpect(this.whiteCell.rightNeighbor, null);

    this.magCell.setRightNeighbor(magCell);
    this.pinkCell.setRightNeighbor(magCell);
    this.whiteCell.setRightNeighbor(pinkCell);

    t.checkExpect(this.magCell.rightNeighbor, magCell);
    t.checkExpect(this.pinkCell.rightNeighbor, magCell);
    t.checkExpect(this.whiteCell.rightNeighbor, pinkCell);
  }

  // tests for setColor
  void testSetColor(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.color, P2_COLOR);
    t.checkExpect(this.pinkCell.color, P1_COLOR);
    t.checkExpect(this.whiteCell.color, Color.WHITE);

    t.checkExpect(this.magCell.filled, true);
    t.checkExpect(this.pinkCell.filled, true);
    t.checkExpect(this.whiteCell.filled, false);

    this.magCell.setColor(Color.WHITE);
    this.pinkCell.setColor(Color.BLACK);
    this.whiteCell.setColor(Color.MAGENTA);

    t.checkExpect(this.magCell.color, Color.WHITE);
    t.checkExpect(this.pinkCell.color, Color.BLACK);
    t.checkExpect(this.whiteCell.color, Color.MAGENTA);

    t.checkExpect(this.magCell.filled, false);
    t.checkExpect(this.pinkCell.filled, true);
    t.checkExpect(this.whiteCell.filled, true);
  }

  // tests for wasCLicked
  void testWasClicked(Tester t) {
    this.initData();

    Posn pos1 = new Posn(45 + EDGE_OFFSET, 15 + EDGE_OFFSET);
    Posn pos2 = new Posn(0, 1000);
    Posn pos3 = new Posn(15 + EDGE_OFFSET, 45 + EDGE_OFFSET);
    Posn pos4 = new Posn(15 + EDGE_OFFSET, 15 + EDGE_OFFSET);
    Posn pos5 = new Posn(25 + EDGE_OFFSET, 5 + EDGE_OFFSET);

    t.checkExpect(this.magCell.wasClicked(pos1), true);
    t.checkExpect(this.magCell.wasClicked(pos2), false);
    t.checkExpect(this.pinkCell.wasClicked(pos1), false);
    t.checkExpect(this.pinkCell.wasClicked(pos3), true);
    t.checkExpect(this.whiteCell.wasClicked(pos4), true);
    t.checkExpect(this.whiteCell.wasClicked(pos5), true);
  }

  // tests for fill
  void testFill(Tester t) {
    this.initData();

    t.checkExpect(this.magCell.filled, true);
    t.checkExpect(this.pinkCell.filled, true);
    t.checkExpect(this.whiteCell.filled, false);

    this.magCell.fill();
    this.pinkCell.fill();
    this.whiteCell.fill();

    t.checkExpect(this.magCell.filled, true);
    t.checkExpect(this.pinkCell.filled, true);
    t.checkExpect(this.whiteCell.filled, true);

  }
}