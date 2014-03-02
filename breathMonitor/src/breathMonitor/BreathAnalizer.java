package breathMonitor;

import javax.sound.sampled.AudioFormat;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class BreathAnalizer {

	private final BasicNetwork network;

	private int trainingTime;

	public static final int TRAINING_THRESHOLD = 10;

	private static final double[][] IS_BREATHING = { { 1.0 } };

	private static double[] getData(byte[] next) {
		final double[] data = new double[next.length];
		for (int i = 0; i < next.length; i++) {
			data[i] = next[i];
		}
		return data;
	}

	public static double average(byte[] arr) {
		double avg = 0;
		for (int i = 0; i < arr.length; i++) {
			avg += arr[i];
		}
		avg /= arr.length;
		return avg;
	}

	private static double[][] getTrainingData(byte[] next) {
		final double[][] data = new double[1][4];
		data[0][0] = average(next);
		return data;
	}

	public BreathAnalizer(AudioFormat format) {
		trainingTime = 0;

		// initialize a neural network

		// la configurazione della rete e' solo provvisoria
		// va adattata in base a quell'articolo

		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 1));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 15));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 15));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
	}

	public boolean isABreaht(byte[] next) {
		// TODO Auto-generated method stub

		// System.err.println("\n" + this.getClass().getName()+
		// " boolean isABreath(byte[]) not fully implemented yet");

		if (trainingTime <= TRAINING_THRESHOLD) {
			// create training data
			final MLDataSet trainingSet = new BasicMLDataSet(
					getTrainingData(next), IS_BREATHING);

			// train the neural network
			final ResilientPropagation train = new ResilientPropagation(
					network, trainingSet);

			//System.out.println("training");
			do {
				train.iteration();
			} while (train.getError() > 0.1);
			trainingTime++;
			return true;
		}

		// the neural network is trained already
		return network.compute(new BasicMLData(getData(next))).getData()[0] >= 0.9;
	}

	public void shutdown() {
		Encog.shutdown();
	}

}
