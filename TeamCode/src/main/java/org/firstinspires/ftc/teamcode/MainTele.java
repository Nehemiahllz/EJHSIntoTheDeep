package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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

    int axelPos;
    //Fix values, once measured
    final int grabHeight = 100;
    final double ticksPerSlideMM = 4.3908;
    final double ticksPerDegree = 4.5;
    final double axelPosLowest = 3;
    double slideLength;
    final double slideLengthZero = 234.95;
    double axelAngle;

    public enum axelMode {GRAB, SUB, BUCKET, RUNG, REST, HANG, HANGING, CLIP}
    axelMode axelMoving;

    public void init() {
        super.init();
        axelMotor.setPower(0);
        axelMotor2.setPower(0);
        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMoving = axelMode.REST;

        slideMotor.setPower(0);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMin = slideMotor.getCurrentPosition();

        printDebugData();
    }

    public void loop() {
        printDebugData();

        slideMax = slideMin + 3030;
        xClaw.setPosition(0.32);
        axelMotor.setTargetPosition(axelPos);
        axelMotor2.setTargetPosition(axelPos);
        slideMotor.setPower(1);

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

            if (gamepad1.right_bumper) {
                frontLeft.setPower(frontLeftPower * 0.8);
                frontRight.setPower(frontRightPower * 0.8);
                backLeft.setPower(backLeftPower * 0.8);
                backRight.setPower(backRightPower * 0.8);
            } else if (gamepad1.left_bumper) {
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


        if(gamepad1.x || gamepad2.a || gamepad2.x || gamepad2.y){
            axelMotor.setPower(0.6);
            axelMotor2.setPower(0.6);
            slideMotor.setTargetPosition(0);

            if(axelMoving == axelMode.SUB) {
                if(gamepad2.x){
                    yClaw.setPosition(0.65);
                    axelMoving = axelMode.GRAB;
                }
            }

            if(gamepad1.x) axelMoving = axelMode.HANG;

            if(gamepad2.a){
                yClaw.setPosition(0.5);
                axelMoving = axelMode.SUB;
            }

            if(gamepad2.y){
                yClaw.setPosition(0.6);
                axelMoving = axelMode.BUCKET;
            }

        }else if(gamepad1.y) axelMoving = axelMode.HANGING;

        if(slideMotor.getCurrentPosition() < slideMin + 150) {
            switch (axelMoving) {
                case GRAB:
                    if (axelMotor.getCurrentPosition() > 320) {
                        axelMotor.setPower(1);
                        axelMotor2.setPower(1);
                    } else if(axelMotor.getCurrentPosition() > 295){
                        axelPos = 323;
                    } else {
                        axelPos = 300;
                    }
                    break;
                case SUB:
                    if (axelMotor.getCurrentPosition() > 267) {
                        axelMotor.setPower(1);
                        axelMotor2.setPower(1);
                        axelPos = 270;
                    }  else if(axelMotor.getCurrentPosition() > 235){
                        axelPos = 270;
                    } else if(axelMotor.getCurrentPosition() > 180){
                        axelPos = 240;
                    } else if (axelMotor.getCurrentPosition() > 90) {
                        axelPos = 190;
                    } else{
                        axelPos = 100;
                    }
                    break;
                case BUCKET:
                    if (axelMotor.getCurrentPosition() < 10) {
                        axelPos = 0;
                    } else if (axelMotor.getCurrentPosition() < 40) {
                        axelPos = 8;
                    } else if (axelMotor.getCurrentPosition() < 90) {
                        axelPos = 30;
                    } else if (axelMotor.getCurrentPosition() < 180) {
                        axelPos = 80;
                    } else if (axelMotor.getCurrentPosition() < 270) {
                        axelPos = 170;
                    } else {
                        axelPos = 260;
                    }
                    break;
                case HANG:
                    axelMotor.setPower(1);
                    axelMotor2.setPower(1);
                    axelPos = 125;
                    break;
                default:
                    axelPos = 0;
            }
        }

        if(axelMoving == axelMode.HANGING){
            axelMotor.setPower(1);
            axelMotor2.setPower(1);
            axelMotor.setTargetPosition(80);
            axelMotor2.setTargetPosition(80);
        }

        //Claw open
        if(gamepad1.left_trigger > 0.1){
            claw.setPosition(0.12);
        }

        //Claw close
        if(gamepad1.right_trigger > 0.1){
            claw.setPosition(0.39);
        }

        //Tilt claw down and reset
        if(gamepad1.b){
            yClaw.setPosition(yClaw.getPosition() + 0.004);
        }else if(gamepad1.dpad_right){
            yClaw.setPosition(0.65);
        }

        //Claw tilt out of bucket
        if(gamepad2.left_trigger > 0.1){
            yClaw.setPosition(0.6);
        }
        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1){
            yClaw.setPosition(0.1);
        }

        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.SUB || axelMoving == axelMode.GRAB) {
                if (slideMotor.getCurrentPosition() < slideMax * 0.45) {
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 80);
                }
            } else if(axelMoving == axelMode.BUCKET){
                if (slideMotor.getCurrentPosition() < slideMax - 400){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 200);
                }else if (slideMotor.getCurrentPosition() < slideMax){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 40);
                }
            } else{
                slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 115);
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

    private void findAxelPos() {
        slideLength = (slideMotor.getCurrentPosition() * ticksPerSlideMM) + slideLengthZero;
        axelAngle = Math.toDegrees(Math.asin(grabHeight/slideLength));
        axelPos = (int) (axelPosLowest - (axelAngle * ticksPerDegree));
    }

}