abstract public class Player
{
    protected String name;
    protected int score = 0;
    protected GameMove chosenMove;

    public Player(String n)
    {
        name = n;
    }

    abstract public void makeMove();

    public void resetTurn()
    {
        chosenMove = null;
    }

    public GameMove getChosenMove()
    {
        return chosenMove;
    }
}