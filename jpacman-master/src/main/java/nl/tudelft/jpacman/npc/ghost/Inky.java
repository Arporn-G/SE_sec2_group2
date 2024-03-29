package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Inky.
 * </p>
 * <b>AI:</b> Inky has the most complicated AI of all. Inky considers two things: Blinky's
 * location, and the location two grid spaces ahead of Pac-Man. Inky draws a
 * line from Blinky to the spot that is two squares in front of Pac-Man and
 * extends that line twice as far. Therefore, if Inky is alongside Blinky
 * when they are behind Pac-Man, Inky will usually follow Blinky the whole
 * time. But if Inky is in front of Pac-Man when Blinky is far behind him,
 * Inky tends to want to move away from Pac-Man (in reality, to a point very
 * far ahead of Pac-Man). Inky is affected by a similar targeting bug that
 * affects Speedy. When Pac-Man is moving or facing up, the spot Inky uses to
 * draw the line is two squares above and left of Pac-Man.
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Inky extends Ghost {

    private static final int SQUARES_AHEAD = 2;

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 300;

    /**
     * Creates a new "Inky".
     *
     * @param spriteMap The sprites for this ghost.
     */
    public Inky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();
        Unit blinky = Navigation.findNearest(Blinky.class, getSquare());
        Unit player = Navigation.findNearest(Player.class, getSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        if (blinky.getSquare().equals(player.getSquare())) {
            return Optional.ofNullable(blinky.getDirection());
        }

        Square destination = blinky.getSquare();
        List<Direction> path = Navigation.shortestPath(getSquare(), destination, this);

        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
}
