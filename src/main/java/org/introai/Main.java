package org.introai;

import org.introai.bots.*;

public class Main {
    /**
     * Caries out multiple simulations at multiple q-values for each bot.
     */
    public static void main(String[] args) {
        final int SHIP_SIZE = 50;
        final int RUNS = 200;
        final double Q_START = 0.1;
        final double Q_INTERVAL = 0.1;
        double q;

        q = Q_START;
        System.out.println("Bot 1 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot1(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS + " = " + (double) wins / RUNS);
            q += Q_INTERVAL;
        }

        q = Q_START;
        System.out.println("Bot 2 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot2(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS + " = " + (double) wins / RUNS);
            q += Q_INTERVAL;
        }

        q = Q_START;
        System.out.println("Bot 3 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot3(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS + " = " + (double) wins / RUNS);
            q += Q_INTERVAL;
        }

        q = Q_START;
        System.out.println("Bot 4 @ size " + SHIP_SIZE);
        while (q <= 1.005) {
            int wins = 0;

            for (int i = 0; i < RUNS; i++) {
                Simulation sim = new Simulation(SHIP_SIZE, q);
                Bot bot = new Bot4(sim.getShipMap());
                boolean result = sim.run(bot);
                if (result) wins += 1;
            }
            System.out.println("At q = " + q + ", wins = " + wins + "/" + RUNS + " = " + (double) wins / RUNS);
            q += Q_INTERVAL;
        }
    }
}