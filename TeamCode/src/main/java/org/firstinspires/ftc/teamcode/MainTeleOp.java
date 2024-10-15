package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MainTele", group = "Main")
public class MainTeleOp extends RobotCore {

    double y = 0;
    double x = 0;
    double rx = 0;


    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = (y + x + rx) / denominator;
    double backLeftPower = (y - x + rx) / denominator;
    double frontRightPower = (y - x - rx) / denominator;
    double backRightPower = (y + x - rx) / denominator;

    //This is a public subclass of RobotCore, so the robot's wheel motors are initialized in RobotCore
    public void init() {
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horizontal.setPosition(0);
        pivot.setPosition(0);
    }

    public void loop() {

        y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        x = -gamepad1.left_stick_x; // Counteract imperfect strafing
        rx = gamepad1.right_stick_x;

        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeftPower = (y - x + rx);
        backLeftPower = (y - x + rx);
        frontRightPower = (y - x - rx);
        backRightPower = (y - x - rx);

        if (gamepad1.b && (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1)) {
            frontLeft.setPower(frontLeftPower * 0.5);
            frontRight.setPower(frontRightPower * 0.5);
            backLeft.setPower(backLeftPower * 0.5);
            backRight.setPower(backRightPower * 0.5);
        } else if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        } else {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

        }


        //Pivot and Claw Controls
        if(gamepad1.x)
            pivot.setPosition(0);
        else if(gamepad1.y)
            pivot.setPosition(0.25);

        if(gamepad1.left_bumper) {
            leftClaw.setPower(1);
            rightClaw.setPower(-1);
        }
        else if(gamepad1.right_bumper)
        {
            leftClaw.setPower(-1);
            rightClaw.setPower(1);
        }

        //All Slide Controls

        if (gamepad1.dpad_up && leftSlide.getCurrentPosition() < 1000 && rightSlide.getCurrentPosition() > -1000)
        {
            leftSlide.setPower(1);
            rightSlide.setPower(-1);
        } else if (gamepad1.dpad_down && leftSlide.getCurrentPosition() > 0 && rightSlide.getCurrentPosition() < 0)
        {
            leftSlide.setPower(-1);
            rightSlide.setPower(1);
        }
        else
        {
            leftSlide.setPower(0.5);
            rightSlide.setPower(-0.5);
        }

        if(gamepad1.right_stick_y < 0.5)
            horizontal.setPosition(0.5);
        else if(gamepad1.right_stick_y > 0.5)
            horizontal.setPosition(0);





    }
}
