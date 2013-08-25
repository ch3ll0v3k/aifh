package com.heatonresearch.aifh.learning;

import com.heatonresearch.aifh.learning.score.ScoreFunction;
import com.heatonresearch.aifh.randomize.GenerateRandom;
import com.heatonresearch.aifh.randomize.MersenneTwisterGenerateRandom;

/**
 * http://en.wikipedia.org/wiki/Simulated_annealing
 */
public class TrainAnneal implements LearningAlgorithm {
    private RegressionAlgorithm algorithm;
    private GenerateRandom rnd = new MersenneTwisterGenerateRandom();
    private double globalBestError = Double.POSITIVE_INFINITY;
    private double[] globalBest[];
    private double currentError;
    private ScoreFunction score;
    private int kMax;
    private int k;
    private double startingTemperature;
    private double endingTemperature;
    private double currentTemperature;
    private int cycles = 1000;
    private double lastProbability;

    public TrainAnneal(RegressionAlgorithm theAlgorithm, ScoreFunction theScore) {
        this(theAlgorithm, theScore, 1000, 400, 0.0001);
    }

    public TrainAnneal(RegressionAlgorithm theAlgorithm, ScoreFunction theScore, int theKMax, double theStartingTemperature, double theEndingTemperature) {
        this.algorithm = theAlgorithm;
        this.score = theScore;
        this.kMax = theKMax;
        this.currentError = score.calculateScore(this.algorithm);
        this.startingTemperature = theStartingTemperature;
        this.endingTemperature = theEndingTemperature;
    }

    public double coolingSchedule() {
        double ex = (double) k / (double) kMax;
        return this.startingTemperature * Math.pow(this.endingTemperature / this.startingTemperature, ex);
    }

    @Override
    public void iteration() {
        int len = this.algorithm.getLongTermMemory().length;
        k++;

        this.currentTemperature = coolingSchedule();

        for (int cycle = 0; cycle < this.cycles; cycle++) {
            // backup current state
            double[] oldState = new double[len];
            System.arraycopy(this.algorithm.getLongTermMemory(), 0, oldState, 0, len);

            // randomize the method
            performRandomize(this.algorithm.getLongTermMemory());

            // did we improve it?  Only keep the new method if it improved (greedy).
            double trialError = score.calculateScore(this.algorithm);

            // was this iteration an improvement?  If so, always keep.
            boolean keep = false;

            if (trialError < this.currentError) {
                keep = true;
            } else {

                this.lastProbability = calcProbability(currentError, trialError, this.currentTemperature);
                if (this.lastProbability > this.rnd.nextDouble()) {
                    keep = true;
                }
            }

            if (keep) {
                this.currentError = currentError;
                // better than global error
                if (trialError < this.globalBestError) {
                    this.globalBestError = trialError;
                    System.arraycopy(this.algorithm.getLongTermMemory(), 0, oldState, 0, len);
                }
            } else {
                System.arraycopy(oldState, 0, this.algorithm.getLongTermMemory(), 0, len);
            }
        }
    }

    public void performRandomize(double[] memory) {
        for (int i = 0; i < memory.length; i++) {
            double d = this.rnd.nextGaussian() * 3;
            memory[i] += d;
        }
    }

    public boolean done() {
        return k >= kMax;
    }

    @Override
    public double getLastError() {
        return this.globalBestError;
    }

    public double calcProbability(double ecurrent, double enew, double t) {
        return Math.exp(-(Math.abs(enew - ecurrent) / t));
    }

    public String getStatus() {
        StringBuilder result = new StringBuilder();
        result.append("k=");
        result.append(this.k);
        result.append(",kMax=");
        result.append(this.kMax);
        result.append(",t=");
        result.append(this.currentTemperature);
        result.append(",prob=");
        result.append(this.lastProbability);
        return result.toString();
    }
}