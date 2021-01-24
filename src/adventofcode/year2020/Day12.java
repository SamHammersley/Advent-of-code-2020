package adventofcode.year2020;

import adventofcode.Puzzle;

public class Day12 extends Puzzle<Integer> {

    public Day12() {
        super("\n", "");
    }

    @Override
    public Integer part1() {
        Ship ship = new Ship();

        input.forEach(s -> {
            char symbol = s.charAt(0);
            int value = Integer.parseInt(s.substring(1));

            switch(symbol) {
                case 'N':
                    ship.move(0, -value);
                    break;
                case 'S':
                    ship.move(0, value);
                    break;
                case 'E':
                    ship.move(value, 0);
                    break;
                case 'W':
                    ship.move(-value, 0);
                    break;
                case 'L':
                    ship.rotate(360 - value);
                    break;
                case 'R':
                    ship.rotate(value);
                    break;
                case 'F':
                    ship.forwards(value);
                    break;
            }
        });

        return ship.distanceFromOrigin();
    }

    @Override
    public Integer part2() {
        Ship ship = new Ship();

        input.forEach(s -> {
            char symbol = s.charAt(0);
            int value = Integer.parseInt(s.substring(1));

            switch(symbol) {
                case 'N':
                    ship.moveWaypoint(0, -value);
                    break;
                case 'S':
                    ship.moveWaypoint(0, value);
                    break;
                case 'E':
                    ship.moveWaypoint(value, 0);
                    break;
                case 'W':
                    ship.moveWaypoint(-value, 0);
                    break;
                case 'L':
                    ship.rotateWaypointAboutShip(-value);
                    break;
                case 'R':
                    ship.rotateWaypointAboutShip(value);
                    break;
                case 'F':
                    int deltaX = ship.waypointX * value;
                    int deltaY = ship.waypointY * value;

                    ship.move(deltaX, deltaY);
                    break;
            }
        });

        return ship.distanceFromOrigin();
    }

    private class Ship {

        private int x = 0, y = 0;

        private int waypointX = 10, waypointY = -1;

        private int orientation = 90;

        void move(int deltaX, int deltaY) {
            this.x += deltaX;
            this.y += deltaY;
        }

        void rotate(int angle) {
            orientation += angle;
            orientation %= 360;
        }

        void moveWaypoint(int deltaX, int deltaY) {
            setWaypoint(waypointX + deltaX, waypointY + deltaY);
        }

        void setWaypoint(int x, int y) {
            this.waypointX = x;
            this.waypointY = y;
        }

        void forwards(int amount) {
            switch(orientation) {
                case 0:
                    move(0, -amount);
                    break;
                case 90:
                    move(amount, 0);
                    break;
                case 180:
                    move(0, amount);
                    break;
                case 270:
                    move(-amount, 0);
                    break;
            }
        }

        void rotateWaypointAboutShip(int angle) {
            double radians = Math.toRadians(angle);
            int newWaypointX = (int) (waypointX * Math.cos(radians)) - (int) (waypointY * Math.sin(radians));
            int newWaypointY = (int) (waypointX * Math.sin(radians)) + (int) (waypointY * Math.cos(radians));

            setWaypoint(newWaypointX, newWaypointY);
        }

        private int distanceFromOrigin() {
            return Math.abs(x) + Math.abs(y);
        }

    }

}