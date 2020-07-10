import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LifePanel
   extends JPanel
   implements ActionListener
{
   private int xPanel = 1300;
   private int yPanel = 700;
   private int size = 10; // Size of each cell in px
   private int xWidth = xPanel / size;
   private int yHeight = yPanel / size;
   private int[][] life = new int[xWidth][yHeight];
   private int[][] oldGeneration  = new int[xWidth][yHeight];
   private boolean starts = true;

   public LifePanel()
   {
      setSize(xPanel, yPanel);
      setLayout(null);
   }

   public void paintComponent(Graphics aGraphics)
   {
      super.paintComponent(aGraphics);
      setBackground(Color.lightGray);

      new Timer(80, this).start();

      createGrid(aGraphics);
      spawnLife();
      displayLife(aGraphics);
   }

   private void createGrid(Graphics aGraphics)
   {
      aGraphics.setColor(Color.black);

      for (int i = 0; i < life.length; i++)
      {
         aGraphics.drawLine(0, i * size, xPanel, i * size); // horizontal lines
         aGraphics.drawLine(i * size, 0, i * size, yPanel); // vertical lines
      }
   }

   private void spawnLife()
   {
      if (starts)
      {
         for (int x = 0; x < life.length; x++)
         {
            for (int y = 0; y < yHeight; y++)
            {
               if ((int)(Math.random() * 5) == 0)
               {
                  oldGeneration[x][y] = 1;
               }
            }
         }

         starts = false;
      }
   }

   /*
    * Fills each cell with color depending on if it's alive or not
    */
   private void displayLife(Graphics aGraphics)
   {
      aGraphics.setColor(Color.ORANGE);

      createNewGeneration();

      for (int x = 0; x < life.length; x++)
      {
         for (int y = 0; y < yHeight; y++)
         {
            if (life[x][y] == 1)
            {
               aGraphics.fillRect(x * size, y * size, size, size);
            }
         }
      }
   }

   private void createNewGeneration()
   {
      for (int x = 0; x < life.length; x++)
      {
         for (int y = 0; y < yHeight; y++)
         {
            life[x][y] = oldGeneration[x][y];
         }
      }
   }

   private int checkIfCellIsAlive(int x, int y)
   {
      int alive = 0;

      /*
       * Counts a cells neighbor  s, deciding if the cell should be alive or not
       *
       * Note: the xWidth and yHeight thing is used to handle the edges of the grid
       */
      alive += life[(x + xWidth - 1) % xWidth][(y + yHeight - 1) % yHeight];
      alive += life[(x + xWidth) % xWidth][(y + yHeight - 1) % yHeight];

      alive += life[(x + xWidth + 1) % xWidth][(y + yHeight - 1) % yHeight];
      alive += life[(x + xWidth - 1) % xWidth][(y + yHeight) % yHeight];

      alive += life[(x + xWidth + 1) % xWidth][(y + yHeight) % yHeight];
      alive += life[(x + xWidth - 1) % xWidth][(y + yHeight + 1) % yHeight];

      alive += life[(x + xWidth) % xWidth][(y + yHeight + 1) % yHeight];
      alive += life[(x + xWidth + 1) % xWidth][(y + yHeight + 1) % yHeight];

      return alive;
   }

   public void actionPerformed(ActionEvent e)
   {
      int alive;

      for (int x = 0; x < life.length; x++)
      {
         for (int y = 0; y < yHeight; y++)
         {
            alive = checkIfCellIsAlive(x, y);

            /*
             * THE HOLY RULES OF LIFE
             */
            if (alive == 3) // cell continues to live or revives a dead cell
            {
               oldGeneration[x][y] = 1;
            } else if (alive == 2 && life[x][y] == 1) //cell continues to live
            {
               oldGeneration[x][y] = 1;
            } else // the cell dies by either under or overpopulation
            {
               oldGeneration[x][y] = 0;
            }
         }
      }

      repaint();
   }
}
