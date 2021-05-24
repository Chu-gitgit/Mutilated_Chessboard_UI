package boardGame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<boardGame.results.GameResult> {

    public GameResultDao() {
        super(boardGame.results.GameResult.class);
    }

    /**
     * Returns the list of {@code n} best results with respect to the steps
     * spent for solving the puzzle.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the steps(minimum)
     * spent for solving the puzzle
     */
    @Transactional
    public List<boardGame.results.GameResult> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r WHERE r.solved = true ORDER BY r.steps ASC, r.created DESC", boardGame.results.GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }

}
