package Sensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;

public class UltrasonicSensor extends EV3UltrasonicSensor implements Sensor {

	private SensorMode mode = this.getMode(0);

	/**
    *
    * @return
    */
	public UltrasonicSensor(Port port) {
		super(port);
	}

	/**
    *
    * @return
    */
	public float[] getValue() {
		float[] result = new float[mode.sampleSize()];
		mode.fetchSample(result, 0);
		return result;
	}

	/**
    *
    * @return
    */
	public float getLight() {
		return this.getValue()[0];
	}

}
