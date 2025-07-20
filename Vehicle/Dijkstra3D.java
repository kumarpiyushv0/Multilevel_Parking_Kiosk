package Vehicle;

import java.util.PriorityQueue;
import java.util.HashSet;

public class Dijkstra3D {

    static class Node implements Comparable<Node> {
        int floor, row, col, dist;

        Node(int f, int r, int c, int d) {
            floor = f;
            row = r;
            col = c;
            dist = d;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }

        @Override
        public int hashCode() {
            return (floor * 10000 + row * 100 + col);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Node) {
                Node o = (Node) obj;
                return o.floor == floor && o.row == row && o.col == col;
            }
            return false;
        }
    }

    public static int[] findNearestEmpty(String[][][] carMatrix, int[] start, int[][] upRamps, int[][] downRamps) {
        int F = carMatrix.length;
        int R = carMatrix[0].length;
        int C = carMatrix[0][0].length;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();

        pq.offer(new Node(start[0], start[1], start[2], 0));

        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } }; // row, col directions

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (visited.contains(curr))
                continue;
            visited.add(curr);

            if (carMatrix[curr.floor][curr.row][curr.col] == null) {
                return new int[] { curr.floor, curr.row, curr.col };
            }

            // Same floor movement
            for (int[] dir : directions) {
                int newRow = curr.row + dir[0];
                int newCol = curr.col + dir[1];
                if (isValid(curr.floor, newRow, newCol, F, R, C)) {
                    pq.offer(new Node(curr.floor, newRow, newCol, curr.dist + 1));
                }
            }

            // Upward ramp (floor + 1)
            for (int[] ramp : upRamps) {
                if (ramp[0] == curr.floor && ramp[1] == curr.row && ramp[2] == curr.col) {
                    if (curr.floor + 1 < F)
                        pq.offer(new Node(curr.floor + 1, curr.row, curr.col, curr.dist + 1));
                }
            }

            // Downward ramp (floor - 1)
            for (int[] ramp : downRamps) {
                if (ramp[0] == curr.floor && ramp[1] == curr.row && ramp[2] == curr.col) {
                    if (curr.floor - 1 >= 0)
                        pq.offer(new Node(curr.floor - 1, curr.row, curr.col, curr.dist + 1));
                }
            }
        }

        return new int[] { -1, -1, -1 }; // No empty spot found
    }

    private static boolean isValid(int f, int r, int c, int F, int R, int C) {
        return (f >= 0 && f < F && r >= 0 && r < R && c >= 0 && c < C);
    }
}
