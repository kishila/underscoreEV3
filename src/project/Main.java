package project;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import project.task.BluetoothTask;
import project.task.DriveTask;

public class Main {

	// スケジューラ
	private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> futureDrive;
    private ScheduledFuture<?> futureBluetooth;

    // タスク
    private DriveTask  driveTask;
    private BluetoothTask bluetoothTask;

    // フラグ
    private boolean buttonPressed;    // タッチセンサーが押されたかの状態

    /**
     * コンストラクタ。
     * スケジューラとタスクオブジェクトを作成。
     */
    public Main() {
    	scheduler  = Executors.newScheduledThreadPool(2);
    	driveTask  = new DriveTask();
    	bluetoothTask = new BluetoothTask();
    	futureBluetooth = scheduler.scheduleAtFixedRate(bluetoothTask, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * スタート前の作業。
     * 尻尾を完全停止位置に固定し、スタート指示があるかをチェックする。
     * @return true=wait / false=start
     */
    public boolean waitForStart() {
        boolean res = true;
        if (Button.ENTER.isDown()) {
        	buttonPressed = true;          // ボタンが押された
        } else {
            if (buttonPressed) {
                res = false;
                buttonPressed = false;     // ボタンが押された後に放した
            }
        }

        if (bluetoothTask.checkRemoteCommand(BluetoothTask.REMOTE_COMMAND_START)) {  // PC で 'g' キーが押された
            res = false;
        }

        return res;
    }

    /**
     * 終了指示のチェック。
     */
    public boolean waitForStop() {
    	boolean res = true;
        if (Button.ENTER.isDown()) {
        	buttonPressed = true;          // ボタンが押された
        } else {
            if (buttonPressed) {
                res = false;
                buttonPressed = false;     // ボタンが押された後に放した
            }
        }

        if (bluetoothTask.checkRemoteCommand(BluetoothTask.REMOTE_COMMAND_STOP)) { // PC で 's' キー押されたら走行終了
            res = false;
        }

        return res;
    }

    /**
     * 走行開始時の作業スケジューリング。
     */
    public void start() {
        futureDrive = scheduler.scheduleAtFixedRate(driveTask, 0, 5, TimeUnit.MILLISECONDS);
    }

    /**
     * 走行終了時のタスク終了後処理。
     */
    public void stop () {
        if (futureDrive != null) {
            futureDrive.cancel(true);
        }
        if (futureBluetooth != null) {
            futureBluetooth.cancel(true);
            bluetoothTask.close();
}
    }

    /**
     * スケジューラのシャットダウン。
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }

    /**
     * メイン
     */
	public static void main(String[] args) {
		LCD.drawString("Please Wait...  ", 0, 4);

		Main main = new Main();

        // スタート待ち
        LCD.drawString("Push to START", 0, 4);
        while (main.waitForStart()) {
            Delay.msDelay(100);
        }

        // 走行開始
		LCD.drawString("Running       ", 0, 4);
		main.start();
        while (main.waitForStop()) {
            Delay.msDelay(100);
        }

        // 終了
        main.stop();
        main.shutdown();
	}
}
