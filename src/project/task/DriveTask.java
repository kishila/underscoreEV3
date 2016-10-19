package project.task;

import lejos.hardware.lcd.LCD;
import project.device.LeftMotor;
import project.device.RightMotor;
import project.device.SonarSensor;

public class DriveTask implements Runnable {

	RightMotor motorR = RightMotor.getInstance();
	LeftMotor motorL = LeftMotor.getInstance();
	SonarSensor sonar = SonarSensor.getInstance();

	private int lcdCount;

	public DriveTask(){
		lcdCount = 0;
	}

    /**
     * EV3本体の制御。
     */
    @Override
    public void run() {
    	// デバイスの値取得
    	int tachoR = motorR.getTachoCount();
    	int tachoL = motorL.getTachoCount();
    	float sonarValue = sonar.getDistance();
    	int sonarCMValue = (int)(sonarValue * 100); // センチ単位に変換

    	// LCD表示
    	if (lcdCount == 5) {
    		LCD.clear();
    		LCD.drawString("Right:" + tachoR, 0, 0);
    		LCD.drawString("Left:" + tachoL, 0, 1);
    		LCD.drawString("Sonar:" + sonarCMValue, 0, 2);
    		lcdCount = 0;
    	} else {
    		lcdCount++;
    	}
    }

    /*
     *  ステアリング
     */
    private void motorRotate(int value) {
    	motorR.controlMotor(value);
    	motorL.controlMotor(-value);
    }

	/*
	 * 停止
	 */
    private void motorStop() {
    	motorR.controlMotor(0);
    	motorL.controlMotor(0);
    }
}
