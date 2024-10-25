package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "MainTeleOp", group = "Main")

public class MainTele extends RobotCore {

    double moveX;
    double moveY;
    double turnX;

    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    int slideMax = 4480;
    int slideMin = 0;

    boolean axelMovingA = false;
    boolean axelMovingB = false;
    boolean axelMovingX = false;

    boolean slideReset = false;


    public void init() {
        super.init();

        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setPower(0.8);
        axelMotor2.setPower(0.8);

        slideMotor.setPower(0.6);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor.setTargetPosition(slideMotor.getCurrentPosition());
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMin = slideMotor.getCurrentPosition();

        printDebugData();
    }

    public void loop() {
        printDebugData();

        slideMax = slideMin + 3030;
        xClaw.setPosition(0.35);

        //Drivetrain
        moveX = gamepad1.left_stick_x;
        moveY = -gamepad1.left_stick_y;
        turnX = gamepad1.right_stick_x;

        frontLeftPower = moveY + moveX + turnX;
        frontRightPower = moveY - moveX - turnX;
        backLeftPower = moveY - moveX + turnX;
        backRightPower = moveY + moveX - turnX;

//        axelMotor.setPositionPIDFCoefficients(0.5);
//        axelMotor2.setPositionPIDFCoefficients(0.5);

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

        //Claw open
        if(gamepad1.left_trigger > 0.1){
            claw.setPosition(0.25);
        }

        //Claw close
        if(gamepad1.right_trigger > 0.1){
            claw.setPosition(0.41);
        }

        //Driver #2 controls

        //Claw tilt out of bucket
        if(gamepad2.left_trigger > 0.1){
            yClaw.setPosition(0.6);
        }
        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1){
            yClaw.setPosition(0.1);
        }

        //Going in/out of the aquarium
        if(gamepad2.a) {
            slideReset = false;
            axelMovingA = true;
            axelMovingB = false;
            axelMovingX = false;
            axelMotor.setPower(0.6);
            axelMotor2.setPower(0.6);
            yClaw.setPosition(0.65);
            slideMotor.setTargetPosition(0);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(axelMovingA){
            if(slideMotor.getCurrentPosition() < slideMin + 100) {
                if (axelMotor.getCurrentPosition() == 255) {
                    axelMotor.setPower(0.8);
                    axelMotor2.setPower(0.8);
                }  else if(axelMotor.getCurrentPosition() > 235){
                    axelMotor.setTargetPosition(255);
                    axelMotor2.setTargetPosition(255);
                } else if(axelMotor.getCurrentPosition() > 180){
                    axelMotor.setTargetPosition(240);
                    axelMotor2.setTargetPosition(240);
                } else if (axelMotor.getCurrentPosition() > 90) {
                    axelMotor.setTargetPosition(190);
                    axelMotor2.setTargetPosition(190);
                } else{
                    axelMotor.setTargetPosition(100);
                    axelMotor2.setTargetPosition(100);
                }
            }
        }

        //Picking up samples
        if(gamepad2.b) {
            slideReset = false;
            axelMovingA = false;
            axelMovingB = true;
            axelMovingX = false;
            axelMotor.setPower(0.4);
            axelMotor2.setPower(0.4);
            yClaw.setPosition(0.65);
            slideMotor.setTargetPosition(0);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(axelMovingB){
            if(slideMotor.getCurrentPosition() < slideMin + 100) {
                if (axelMotor.getCurrentPosition() == 323) {
                    axelMotor.setPower(0.8);
                    axelMotor2.setPower(0.8);
                } else if(axelMotor.getCurrentPosition() > 295){
                    axelMotor.setTargetPosition(323);
                    axelMotor2.setTargetPosition(323);
                } else if(axelMotor.getCurrentPosition() > 265){
                    axelMotor.setTargetPosition(300);
                    axelMotor2.setTargetPosition(300);
                } else if(axelMotor.getCurrentPosition() > 180){
                    axelMotor.setTargetPosition(275);
                    axelMotor2.setTargetPosition(275);
                } else if (axelMotor.getCurrentPosition() > 90) {
                    axelMotor.setTargetPosition(190);
                    axelMotor2.setTargetPosition(190);
                } else{
                    axelMotor.setTargetPosition(100);
                    axelMotor2.setTargetPosition(100);
                }
            }
        }

        //Going for bucket
        if(gamepad2.x) {
            slideReset = false;
            axelMovingA = false;
            axelMovingB = false;
            axelMovingX = true;
            axelMotor.setPower(0.6);
            axelMotor2.setPower(0.6);
            slideMotor.setTargetPosition(0);
            yClaw.setPosition(0.6);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(axelMovingX){
            if(slideMotor.getCurrentPosition() < slideMin + 50) {
                if (axelMotor.getCurrentPosition() < 10) {
                    axelMotor.setTargetPosition(0);
                    axelMotor2.setTargetPosition(0);
                } else if (axelMotor.getCurrentPosition() < 40) {
                    axelMotor.setTargetPosition(8);
                    axelMotor2.setTargetPosition(8);
                } else if (axelMotor.getCurrentPosition() < 90) {
                    axelMotor.setTargetPosition(30);
                    axelMotor2.setTargetPosition(30);
                } else if (axelMotor.getCurrentPosition() < 180) {
                    axelMotor.setTargetPosition(80);
                    axelMotor2.setTargetPosition(80);
                } else if (axelMotor.getCurrentPosition() < 270) {
                    axelMotor.setTargetPosition(170);
                    axelMotor2.setTargetPosition(170);
                } else {
                    axelMotor.setTargetPosition(260);
                    axelMotor2.setTargetPosition(260);
                }
            }
        }

        //Fix the slide positions

            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setPower(0.6);

        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMovingA || axelMovingB) {
                if (slideMotor.getCurrentPosition() < slideMax * 0.45) {
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 80);
                }
            } else if(axelMovingX){
                if (slideMotor.getCurrentPosition() < slideMax - 400){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 200);
                }else if (slideMotor.getCurrentPosition() < slideMax){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 40);
                }
            }
        }
        //Manual slide detraction
        if (gamepad2.dpad_down) {
            if (slideMotor.getCurrentPosition() > 0) {
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 120);
            }
        }

    }

    //Show data on the driver hub
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("slideMin", slideMin);
        telemetry.addData("slideMax", slideMax);
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("yClaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());
        telemetry.addData("claw", claw.getPosition());
    }

}