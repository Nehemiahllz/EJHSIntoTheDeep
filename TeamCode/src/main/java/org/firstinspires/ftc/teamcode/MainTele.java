package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MainTeleOp", group = "Main")

public class MainTele extends RobotCore{

    double moveX;
    double moveY;
    double turnX;

    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    /*Slide:
    0 = no power
    1 = move down
    2 = hold
    3 = move up
     */

    int slide = 0;

    public void init()
    {
        super.init();
        slideMotor.setTargetPosition(slideMotor.getCurrentPosition());
    }

    public void loop() {

       printDebugData();

        moveX = gamepad1.left_stick_x;
        moveY = -gamepad1.left_stick_y;
        turnX = gamepad1.right_stick_x;

        frontLeftPower = moveY + moveX + turnX;
        frontRightPower = moveY - moveX - turnX;
        backLeftPower = moveY - moveX + turnX;
        backRightPower = moveY + moveX - turnX;

        //Drivetrain
        if(Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1){
            frontLeft.setPower(frontLeftPower * 0.5);
            frontRight.setPower(frontRightPower * 0.5);
            backLeft.setPower(backLeftPower * 0.5);
            backRight.setPower(backRightPower * 0.5);
        } else{
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }

        if(gamepad1.dpad_up) {
            slideMotor.setTargetPosition(3160);
        }

        if(gamepad1.dpad_down) {
            slideMotor.setTargetPosition(40);
        }

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(1);

        axelMotor.setTargetPosition(-250);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setPower(1);

    }

    private void printDebugData() {
            telemetry.addLine("----Controller Inputs----");
            telemetry.addData("Axel", axelMotor.getCurrentPosition());
            telemetry.addData("Slide", slideMotor.getCurrentPosition());
            telemetry.addData("yClaw", yClaw.getPosition());
            telemetry.addData("xClaw", xClaw.getPosition());
            telemetry.addData("claw", claw.getPosition());

    }


}
