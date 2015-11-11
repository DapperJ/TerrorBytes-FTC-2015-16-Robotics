

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class K9TeleOp extends OpMode {
	
	DcMotor motorRight;
	DcMotor motorLeft;
	DcMotor motorLift;

	/**
	 * Constructor
	 */
	public K9TeleOp() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "Right" and "Left"
		 *   "Right" is on the right side of the bot.
		 *   "Left" is on the left side of the bot and reversed.
		 */
		motorLift = hardwareMap.dcMotor.get("Lift");
		motorRight = hardwareMap.dcMotor.get("Left");
		motorLeft = hardwareMap.dcMotor.get("Right");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
		
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right
		float throttle = -gamepad1.left_stick_y;
		float direction = gamepad1.left_stick_x;
		float right = (throttle - direction)*speed;
		float left = (throttle + direction)*speed;
		int lift = Liftyloo;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);
		lift = Range.clip(Lift, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);
		lift = (float)scaleInput(lift);
		
		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);
		motorLift.setPower(lift);

		// update the position of the arm.
		if (gamepad1.dpad_up){
			// if down on the d-pad is pressed it lifts up the Lift
			
			Liftyloo = 0.75
		}
		if (gamepad1.dpad_down){
			// if up on the d-pad is pressed it pulls down the Lift
			
			Liftyloo = -0.75
		}
		else if (gampad1.x){
			// If x is pressed it puts full power to the wheels
			
			speed = 1.0
		}
		else if (gampad1.b){
			// If b is pressed it puts half power to the wheels
			
			speed = 0.5
		}
		else {
			// If nothing is pressed
			
			speed = 0.75
		}



		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("Lift tgt pwr", "Lift pwr: " + String.format("%.2f", lift);

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

    	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		
		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;
	}

}
