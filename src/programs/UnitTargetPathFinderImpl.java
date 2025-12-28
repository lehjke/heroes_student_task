package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        List<Edge> emptyPath = new ArrayList<>();
        if (attackUnit == null || targetUnit == null) {
            return emptyPath;
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        if (!isInside(startX, startY) || !isInside(targetX, targetY)) {
            return emptyPath;
        }

        if (startX == targetX && startY == targetY) {
            emptyPath.add(new Edge(startX, startY));
            return emptyPath;
        }

        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        if (existingUnitList != null) {
            for (Unit unit : existingUnitList) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }
                int x = unit.getxCoordinate();
                int y = unit.getyCoordinate();
                if (isInside(x, y)) {
                    blocked[x][y] = true;
                }
            }
        }

        blocked[startX][startY] = false;
        blocked[targetX][targetY] = false;

        int[][] prevX = new int[WIDTH][HEIGHT];
        int[][] prevY = new int[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            Arrays.fill(prevX[x], -1);
            Arrays.fill(prevY[x], -1);
        }

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];
            if (cx == targetX && cy == targetY) {
                break;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];
                if (!isInside(nx, ny) || visited[nx][ny] || blocked[nx][ny]) {
                    continue;
                }
                visited[nx][ny] = true;
                prevX[nx][ny] = cx;
                prevY[nx][ny] = cy;
                queue.add(new int[]{nx, ny});
            }
        }

        if (!visited[targetX][targetY]) {
            return emptyPath;
        }

        List<Edge> path = new ArrayList<>();
        int cx = targetX;
        int cy = targetY;
        path.add(new Edge(cx, cy));
        while (!(cx == startX && cy == startY)) {
            int px = prevX[cx][cy];
            int py = prevY[cx][cy];
            if (px < 0 || py < 0) {
                return emptyPath;
            }
            cx = px;
            cy = py;
            path.add(new Edge(cx, cy));
        }

        Collections.reverse(path);
        return path;
    }

    private static boolean isInside(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
}
