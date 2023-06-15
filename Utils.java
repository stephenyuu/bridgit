import java.awt.Color;
import java.util.ArrayList;

import tester.Tester;

//--------------------- UTILS CLASS ---------------------------------

//represents useful methods
class Utils implements IGameConstants {  
  //if the given size is valid, return given size
  //otherwise throw error
  public int validSize(int size) {
    if (size >= 3 && (size % 2 == 1)) {
      return size;
    }
    else {
      throw new IllegalArgumentException("Size must be an odd number greater than or equal to 3!");
    }
  }

  //return if there is a path to the bottom edge from the given cell
  public boolean findPathTopBottom(Cell c, ArrayList<Cell> checked) {
    // add this cell to the list of checked cells
    checked.add(c);
    // if downNeighbor is out of the board, this cell is on the bottom edge
    if (c.downNeighbor.color.equals(Color.BLACK)) {
      return true;
    }
    // check if neighbors are the correct color and have not been checked yet
    // then check if there is a path from top to bottom starting from each valid neighbor
    if (c.downNeighbor.color.equals(P2_COLOR) && !checked.contains(c.downNeighbor)) {
      if (this.findPathTopBottom(c.downNeighbor, checked)) {
        return true;
      }
    }
    if (c.leftNeighbor.color.equals(P2_COLOR) && !checked.contains(c.leftNeighbor)) {
      if (this.findPathTopBottom(c.leftNeighbor, checked)) {
        return true;
      }
    }
    if (c.rightNeighbor.color.equals(P2_COLOR) && !checked.contains(c.rightNeighbor)) {
      if (this.findPathTopBottom(c.rightNeighbor, checked)) {
        return true;
      }
    }
    if (c.topNeighbor.color.equals(P2_COLOR) && !checked.contains(c.topNeighbor)) {
      if (this.findPathTopBottom(c.topNeighbor, checked)) {
        return true;
      }
    }
    // return false if no path exists
    return false;
  }

  //return if there is a path to the right edge from a given cell
  public boolean findPathLeftRight(Cell c, ArrayList<Cell> checked) {
    // add this cell to the list of checked cells
    checked.add(c);
    // if rightNeighbor is out of the board, this cell is on the bottom edge
    if (c.rightNeighbor.color.equals(Color.BLACK)) {
      return true;
    }
    // check if neighbors are the correct color and have not been checked yet
    // then check if there is a path from left to right starting from each valid neighbor
    if (c.rightNeighbor.color.equals(P1_COLOR) && !checked.contains(c.rightNeighbor)) {
      if (this.findPathLeftRight(c.rightNeighbor, checked)) {
        return true;
      }
    }
    if (c.topNeighbor.color.equals(P1_COLOR) && !checked.contains(c.topNeighbor)) {
      if (this.findPathLeftRight(c.topNeighbor, checked)) {
        return true;
      }
    }
    if (c.downNeighbor.color.equals(P1_COLOR) && !checked.contains(c.downNeighbor)) {
      if (this.findPathLeftRight(c.downNeighbor, checked)) {
        return true;
      }
    }
    if (c.leftNeighbor.color.equals(P1_COLOR) && !checked.contains(c.leftNeighbor)) {
      if (this.findPathLeftRight(c.leftNeighbor, checked)) {
        return true;
      }
    }
    // return false if no path exists    
    return false;
  }
}

//-------------------- EXAMPLES CLASS -------------------------------

//represents examples and tests 
class ExamplesUtils implements IGameConstants {
  // Utils object
  Utils utils = new Utils();

  // tests for validSize
  void testValidSize(Tester t) {
    t.checkConstructorException(new IllegalArgumentException("Size must be an odd "
        + "number greater than or equal to 3!"),
        "Bridgit", 0);
    t.checkConstructorException(new IllegalArgumentException("Size must be an odd "
        + "number greater than or equal to 3!"),
        "Bridgit", 8);
    t.checkConstructorException(new IllegalArgumentException("Size must be an odd "
        + "number greater than or equal to 3!"),
        "Bridgit", -3);
    t.checkExpect(this.utils.validSize(3), 3);
    t.checkExpect(this.utils.validSize(13), 13);
  }

  // tests for findPathTopBottom
  void testFindPathTopBottom(Tester t) {
    Cell invalid = new Cell(0, 0, Color.BLACK);

    Cell cell1 = new Cell(0, 0, P2_COLOR);
    Cell cell2 = new Cell(0, 0, P1_COLOR);

    cell1.setDownNeighbor(invalid);
    cell1.setTopNeighbor(invalid);
    cell1.setLeftNeighbor(invalid);
    cell1.setRightNeighbor(invalid);

    cell2.setDownNeighbor(invalid);
    cell2.setTopNeighbor(invalid);
    cell2.setLeftNeighbor(invalid);
    cell2.setRightNeighbor(invalid);

    Cell cell3 = new Cell(0, 0, P2_COLOR);
    cell3.setDownNeighbor(cell2);
    cell3.setTopNeighbor(cell1);
    cell3.setLeftNeighbor(invalid);
    cell3.setRightNeighbor(invalid);
    
    Cell cell4 = new Cell(0, 0, P2_COLOR);
    cell4.setDownNeighbor(cell3);
    cell4.setTopNeighbor(invalid);
    cell4.setLeftNeighbor(cell2);
    cell4.setRightNeighbor(invalid);
    
    Cell cell5 = new Cell(0, 0, P2_COLOR);
    cell5.setDownNeighbor(cell2);
    cell5.setTopNeighbor(invalid);
    cell5.setLeftNeighbor(invalid);
    cell5.setRightNeighbor(invalid);

    t.checkExpect(this.utils.findPathTopBottom(cell1, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathTopBottom(cell2, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathTopBottom(cell3, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathTopBottom(cell4, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathTopBottom(cell5, new ArrayList<Cell>()), false);
  }  

  // tests for findPathLeftRight
  void testFindPathLeftRight(Tester t) {
    Cell invalid = new Cell(0, 0, Color.BLACK);

    Cell cell1 = new Cell(0, 0, P1_COLOR);
    Cell cell2 = new Cell(0, 0, P2_COLOR);

    cell1.setDownNeighbor(invalid);
    cell1.setTopNeighbor(invalid);
    cell1.setLeftNeighbor(invalid);
    cell1.setRightNeighbor(invalid);

    cell2.setDownNeighbor(invalid);
    cell2.setTopNeighbor(invalid);
    cell2.setLeftNeighbor(invalid);
    cell2.setRightNeighbor(invalid);

    Cell cell3 = new Cell(0, 0, P1_COLOR);
    cell3.setDownNeighbor(cell2);
    cell3.setTopNeighbor(cell1);
    cell3.setLeftNeighbor(invalid);
    cell3.setRightNeighbor(invalid);
    
    Cell cell4 = new Cell(0, 0, P1_COLOR);
    cell4.setDownNeighbor(cell3);
    cell4.setTopNeighbor(invalid);
    cell4.setLeftNeighbor(invalid);
    cell4.setRightNeighbor(cell2);
    
    Cell cell5 = new Cell(0, 0, P1_COLOR);
    cell5.setDownNeighbor(invalid);
    cell5.setTopNeighbor(invalid);
    cell5.setLeftNeighbor(invalid);
    cell5.setRightNeighbor(cell2);

    t.checkExpect(this.utils.findPathLeftRight(cell1, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathLeftRight(cell2, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathLeftRight(cell3, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathLeftRight(cell4, new ArrayList<Cell>()), true);
    t.checkExpect(this.utils.findPathLeftRight(cell5, new ArrayList<Cell>()), false);
  }


}