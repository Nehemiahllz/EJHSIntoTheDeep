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

    public void init() {
        super.init();
        slideMotor.setTargetPosition(slideMotor.getCurrentPosition());
    }

    public void loop() {

        printDebugData();
        //Drivetrain
        moveX = gamepad1.left_stick_x;
        moveY = -gamepad1.left_stick_y;
        turnX = gamepad1.right_stick_x;

        frontLeftPower = moveY + moveX + turnX;
        frontRightPower = moveY - moveX - turnX;
        backLeftPower = moveY - moveX + turnX;
        backRightPower = moveY + moveX - turnX;

        //Drivetrain Driver Controls
        if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {

            if (gamepad1.right_trigger > 0.1) {
                frontLeft.setPower(frontLeftPower * 0.8);
                frontRight.setPower(frontRightPower * 0.8);
                backLeft.setPower(backLeftPower * 0.8);
                backRight.setPower(backRightPower * 0.8);
            } else if (gamepad1.left_trigger > 0.1) {
                frontLeft.setPower(frontLeftPower * 0.25);
                frontRight.setPower(frontRightPower * 0.25);
                backLeft.setPower(backLeftPower * 0.25);
                backRight.setPower(backRightPower * 0.25);
            } else {
                frontLeft.setPower(frontLeftPower * 0.55);
                frontRight.setPower(frontRightPower * 0.55);
                backLeft.setPower(backLeftPower * 0.55);
                backRight.setPower(backRightPower * 0.55);
            }
        } else {
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


        axelMotor.setTargetPosition(-250);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setPower(1);

        //Driver #1 controls
        if(gamepad1.left_bumper){
            claw.setPosition();
        }

        if(gamepad1.right_bumper){
            claw.setPosition();
        }

        //Driver #2 controls

        if(gamepad2.a){
            slideMotor.setTargetPosition();
            axelMotor.setTargetPosition();
            yClaw.setPosition();
        }

        if(gamepad2.b){
            slideMotor.setTargetPosition(40);
            axelMotor.setTargetPosition();
            yClaw.setPosition();
        }

        if(gamepad2.x){
            slideMotor.setTargetPosition(3160);
            axelMotor.setTargetPosition();
            yClaw.setPosition();
        }

        if(gamepad2.dpad_up){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 50);
        }

        if(gamepad2.dpad_down){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 50);
        }

            if (gamepad2.left_bumper) {
                slideMotor.setTargetPosition(3160);
                axelMotor.setTargetPosition();
            }
            if (gamepad2.right_bumper) {
                slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                slideMotor.setPower(-1);
                axelMotor.setTargetPosition();
            } else{
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setPower(1);
        }

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
