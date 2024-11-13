package view.game;

import java.util.*;

public class AISolver {

        // 定义方向，分别对应上、下、左、右
        private static final int[] directionsX = {-1, 1, 0, 0};  // 上下左右
        private static final int[] directionsY = {0, 0, -1, 1};
        private static final char[] directionChars = {'U', 'D', 'L', 'R'};  // 方向字符表示，上下左右

        // 定义状态类来表示玩家和箱子的状态
        static class State {
            int playerX, playerY;  // 玩家位置
            Set<String> boxPositions;  // 箱子位置集合

            State(int playerX, int playerY, Set<String> boxPositions) {
                this.playerX = playerX;
                this.playerY = playerY;
                this.boxPositions = new HashSet<>(boxPositions);  // 复制集合
            }

            // 生成一个唯一的状态标识符
            @Override
            public int hashCode() {
                return Objects.hash(playerX, playerY, boxPositions);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                State state = (State) obj;
                return playerX == state.playerX && playerY == state.playerY && boxPositions.equals(state.boxPositions);
            }
        }

        public static AIResult aiSolve(int[][] grid) {
            int rows = grid.length;
            int cols = grid[0].length;
            Set<String> targetPositions = new HashSet<>();
            Set<String> boxPositions = new HashSet<>();
            int playerX = -1, playerY = -1;

            // 找出玩家、箱子和目标点
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == 20 || grid[i][j] == 22) {
                        playerX = i;
                        playerY = j;
                    }
                    if (grid[i][j] == 10 || grid[i][j] == 12) {
                        boxPositions.add(i + "," + j);
                    }
                    if (grid[i][j] == 2 || grid[i][j] == 12) {
                        targetPositions.add(i + "," + j);
                    }
                }
            }

            // 初始状态
            State startState = new State(playerX, playerY, boxPositions);
            Set<State> visited = new HashSet<>();
            Queue<State> queue = new LinkedList<>();
            Map<State, String> pathMap = new HashMap<>();

            // 将初始状态入队列
            visited.add(startState);
            queue.add(startState);
            pathMap.put(startState, "");

            // BFS遍历
            while (!queue.isEmpty()) {
                State currentState = queue.poll();
                int px = currentState.playerX;
                int py = currentState.playerY;
                Set<String> currentBoxPositions = currentState.boxPositions;

                // 如果当前状态满足所有箱子都在目标点的位置，则完成
                if (currentBoxPositions.equals(targetPositions)) {
                    return new AIResult(pathMap.get(currentState), pathMap.get(currentState).length());
                }

                // 尝试四个方向：上、下、左、右
                for (int i = 0; i < 4; i++) {
                    int newPx = px + directionsX[i];
                    int newPy = py + directionsY[i];

                    // 确保新位置在有效范围内
                    if (newPx < 0 || newPx >= rows || newPy < 0 || newPy >= cols || grid[newPx][newPy] == 1) {
                        continue;  // 如果是墙壁或超出边界，跳过
                    }

                    // 如果玩家移动到一个箱子的位置，需要推箱子
                    if (currentBoxPositions.contains(newPx + "," + newPy)) {
                        int pushX = newPx + directionsX[i];
                        int pushY = newPy + directionsY[i];

                        // 如果箱子推到有效的位置（不撞墙或箱子）
                        if (pushX >= 0 && pushX < rows && pushY >= 0 && pushY < cols && grid[pushX][pushY] != 1 && !currentBoxPositions.contains(pushX + "," + pushY)) {
                            Set<String> newBoxPositions = new HashSet<>(currentBoxPositions);
                            newBoxPositions.remove(newPx + "," + newPy);
                            newBoxPositions.add(pushX + "," + pushY);

                            State newState = new State(newPx, newPy, newBoxPositions);
                            if (!visited.contains(newState)) {
                                visited.add(newState);
                                queue.add(newState);
                                pathMap.put(newState, pathMap.get(currentState) + directionChars[i]);
                            }
                        }
                    } else {
                        // 玩家移动到空地或者目标点
                        State newState = new State(newPx, newPy, currentBoxPositions);
                        if (!visited.contains(newState)) {
                            visited.add(newState);
                            queue.add(newState);
                            pathMap.put(newState, pathMap.get(currentState) + directionChars[i]);
                        }
                    }
                }
            }

            return new AIResult("", -1);  // 如果没有解，返回空路径
        }

        // 返回结果的结构


        // 用于测试
        public static void main(String[] args) {
            int[][] grid = {
                    {1, 1, 1, 1, 1, 1},
                    {1, 0, 10, 2, 0, 1},
                    {1, 0, 10, 2, 0, 1},
                    {1, 0, 20, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1}
            };

            AIResult result = aiSolve(grid);
            System.out.println("Shortest Path: " + result.path);
            System.out.println("Path Length: " + result.length);
        }


}
