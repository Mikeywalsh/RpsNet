import com.esotericsoftware.kryo.Kryo;

/**
 * A Rock-Paper-Scissors game instance
 */
public class Game
{
    public static final int SCORE_LIMIT = 3;

    Player player1;
    Player player2;

    public Game(Player p1, Player p2)
    {
        player1 = p1;
        player2 = p2;
    }

    //If both players have made their move, then get the result
    public void Update()
    {
        if(player1.getChosenMove() != null && player2.getChosenMove() != null)
        {

        }
    }

}
