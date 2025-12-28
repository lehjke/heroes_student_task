package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        if (unitList == null || unitList.isEmpty() || maxPoints <= 0) {
            return army;
        }

        List<Unit> sortedUnits = new ArrayList<>();
        for (Unit unit : unitList) {
            if (unit != null && unit.getCost() > 0) {
                sortedUnits.add(unit);
            }
        }

        sortedUnits.sort(Comparator
                .comparingDouble((Unit unit) -> (double) unit.getBaseAttack() / unit.getCost()).reversed()
                .thenComparingDouble(unit -> (double) unit.getHealth() / unit.getCost()).reversed());

        List<Unit> result = new ArrayList<>();
        Map<String, Integer> typeCounts = new HashMap<>();
        int points = 0;
        int positionIndex = 0;
        int totalSlots = GRID_WIDTH * GRID_HEIGHT;

        for (Unit prototype : sortedUnits) {
            String unitType = prototype.getUnitType();
            int count = typeCounts.getOrDefault(unitType, 0);
            int cost = prototype.getCost();

            while (count < MAX_UNITS_PER_TYPE && points + cost <= maxPoints && positionIndex < totalSlots) {
                int x = positionIndex / GRID_HEIGHT;
                int y = positionIndex % GRID_HEIGHT;

                String baseName = prototype.getName();
                if (baseName == null || baseName.isEmpty()) {
                    baseName = unitType;
                }
                String name = baseName + " " + (count + 1);

                Map<String, Double> attackBonuses = prototype.getAttackBonuses();
                Map<String, Double> defenceBonuses = prototype.getDefenceBonuses();

                Unit newUnit = new Unit(
                        name,
                        prototype.getUnitType(),
                        prototype.getHealth(),
                        prototype.getBaseAttack(),
                        prototype.getCost(),
                        prototype.getAttackType(),
                        attackBonuses == null ? new HashMap<>() : new HashMap<>(attackBonuses),
                        defenceBonuses == null ? new HashMap<>() : new HashMap<>(defenceBonuses),
                        x,
                        y
                );

                result.add(newUnit);
                points += cost;
                count++;
                typeCounts.put(unitType, count);
                positionIndex++;
            }
        }

        army.setUnits(result);
        army.setPoints(points);
        return army;
    }
}
