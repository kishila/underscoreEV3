package Sensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class LightSensor extends EV3ColorSensor implements Sensor {

	private SensorMode mode = this.getMode(1);

	/**
    *
    * @return
    */
	public LightSensor(Port port) {
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
