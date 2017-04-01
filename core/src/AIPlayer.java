import java.util.Random;

/**
 * Created by michael on 01/04/2017.
 */
public class AIPlayer extends Player
{
    public AIPlayer(String name)
    {
        super(name);
    }

    @Override
    public void makeMove()
    {
        if(chosenMove != null)
        {
            //Choose a random move
            Random r = new Random();
            int chosenNum = r.nextInt(3);

            switch (chosenNum)
            {
                case 0:
                    chosenMove = GameMove.Rock;
                    break;
                case 1:
                    chosenMove = GameMove.Paper;
                    break;
                case 2:
                    chosenMove = GameMove.Scissors;
                    break;
            }
        }
    }
}
