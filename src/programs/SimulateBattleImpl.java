package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy == null || computerArmy == null) {
            return;
        }

        while (true) {
            List<Unit> turnOrder = new ArrayList<>();
            collectAliveUnits(playerArmy, turnOrder);
            collectAliveUnits(computerArmy, turnOrder);

            if (turnOrder.isEmpty()) {
                return;
            }

            turnOrder.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            boolean anyAction = false;
            for (Unit unit : turnOrder) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                Unit target = unit.getProgram().attack();
                if (printBattleLog != null) {
                    printBattleLog.printBattleLog(unit, target);
                }
                if (target != null && target != unit) {
                    anyAction = true;
                }
            }

            if (!hasAliveUnits(playerArmy) || !hasAliveUnits(computerArmy) || !anyAction) {
                return;
            }
        }
    }

    private static void collectAliveUnits(Army army, List<Unit> target) {
        if (army == null || army.getUnits() == null) {
            return;
        }
        for (Unit unit : army.getUnits()) {
            if (unit != null && unit.isAlive()) {
                target.add(unit);
            }
        }
    }

    private static boolean hasAliveUnits(Army army) {
        if (army == null || army.getUnits() == null) {
            return false;
        }
        for (Unit unit : army.getUnits()) {
            if (unit != null && unit.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
