package Sensor;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class TouchSensor extends EV3TouchSensor implements Sensor {

	private SensorMode mode = this.getMode(0);

	public TouchSensor(AnalogPort port) {
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
     * タッチセンサ押下のチェック
     * @return true ならタッチセンサーが押された。
     */
	public boolean isPressed() {
		float[] values = this.getValue();
		return values[0] == 1 ? true : false;
	}
}
