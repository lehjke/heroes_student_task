package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    private static final int FIELD_WIDTH = 27;
    private static final int FIELD_HEIGHT = 21;

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();
        if (unitsByRow == null || unitsByRow.isEmpty()) {
            return result;
        }

        boolean[][] occupied = new boolean[FIELD_WIDTH][FIELD_HEIGHT];
        for (List<Unit> rowUnits : unitsByRow) {
            if (rowUnits == null) {
                continue;
            }
            for (Unit unit : rowUnits) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                int x = unit.getxCoordinate();
                int y = unit.getyCoordinate();
                if (x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT) {
                    occupied[x][y] = true;
                }
            }
        }

        for (List<Unit> rowUnits : unitsByRow) {
            if (rowUnits == null) {
                continue;
            }
            for (Unit unit : rowUnits) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                int x = unit.getxCoordinate();
                int y = unit.getyCoordinate();
                if (x < 0 || x >= FIELD_WIDTH || y < 0 || y >= FIELD_HEIGHT) {
                    continue;
                }
                int neighborX = isLeftArmyTarget ? x + 1 : x - 1;
                if (neighborX < 0 || neighborX >= FIELD_WIDTH || !occupied[neighborX][y]) {
                    result.add(unit);
                }
            }
        }

        return result;
    }
}
