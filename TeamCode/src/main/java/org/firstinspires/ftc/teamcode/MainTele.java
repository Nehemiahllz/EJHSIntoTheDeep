package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    final double grabHeight = 15.2;
    final double ticksPerSlideMM = 4.468;
    final double ticksPerDegree = 3.877;
    final double axelPosLowest = 409;
    double slideLength;
    final double slideLengthZero = 317.5;
    double axelAngle;

    public enum axelMode {GRAB, SUB, BUCKET, RUNG, REST, HANG, HANGING, CLIP, LOW}
    axelMode axelMoving;

    ElapsedTime timer = new ElapsedTime();
    boolean timeTrue = true;

    public void init() {
        super.init();
        axelMotor.setPower(0);
        axelMotor2.setPower(0);
        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
                leftFront.setPower(frontLeftPower * 0.8);
                rightFront.setPower(frontRightPower * 0.8);
                leftBack.setPower(backLeftPower * 0.8);
                rightBack.setPower(backRightPower * 0.8);
            } else if (gamepad1.left_bumper) {
                leftFront.setPower(frontLeftPower * 0.25);
                rightFront.setPower(frontRightPower * 0.25);
                leftBack.setPower(backLeftPower * 0.25);
                rightBack.setPower(backRightPower * 0.25);
            } else {
                leftFront.setPower(frontLeftPower * 0.55);
                rightFront.setPower(frontRightPower * 0.55);
                leftBack.setPower(backLeftPower * 0.55);
                rightBack.setPower(backRightPower * 0.55);
            }
        } else {
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
        }


        if(gamepad1.x || gamepad2.a || gamepad2.y){
            axelMotor.setPower(1);
            axelMotor2.setPower(1);
            slideMotor.setTargetPosition(0);


            if(gamepad1.x) axelMoving = axelMode.HANG;

            if(gamepad2.a){
                yClaw.setPosition(0.8644);
                axelMoving = axelMode.SUB;
            }

            if(gamepad2.y){
                xClaw.setPosition(1);
                axelMoving = axelMode.BUCKET;
            }

        }else if(gamepad1.right_trigger > 0.1){
            if(axelMoving == axelMode.SUB || axelMoving == axelMode.LOW) {
                axelMotor.setPower(0);
                axelMotor2.setPower(0);
                yClaw.setPosition(0.8644);
                axelMoving = axelMode.GRAB;
            }
        }else if(gamepad2.x){
            if(axelMoving == axelMode.SUB) {
                axelMotor.setPower(1);
                axelMotor2.setPower(1);
            yClaw.setPosition(0.8644);
            axelMoving = axelMode.LOW;
            }
        }else if(gamepad1.y) {
            if(axelMoving == axelMode.HANG) {
                axelMoving = axelMode.HANGING;
            }
        }

        if(slideMotor.getCurrentPosition() < slideMin + 150) {
            switch (axelMoving) {
                case SUB:
                    if(slideMotor.getCurrentPosition() > 300) {
                        axelPos = 330;
                    }else if (axelMotor.getCurrentPosition() > 267) {
                        axelPos = 285;
                    }else if(axelMotor.getCurrentPosition() > 235){
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
                    axelPos = 0;
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

        if(axelMoving == axelMode.LOW){
            axelPos = 330;
        }

        if(axelMoving == axelMode.HANGING){
            axelMotor.setPower(1);
            axelMotor2.setPower(1);
            axelMotor.setTargetPosition(0);
            axelMotor2.setTargetPosition(0);
        }

        //Claw open
        if(gamepad1.left_trigger > 0.1){
            claw.setPosition(0.57);
        }
        if (gamepad1.right_trigger > 0.7) {
            claw.setPosition(0.36);
        }

        //Tilt claw down and reset
        if(gamepad1.dpad_left){
            xClaw.setPosition(xClaw.getPosition() + 0.025);
        }else if(gamepad1.dpad_right){
            xClaw.setPosition(xClaw.getPosition() - 0.025);
        }

        //Claw tilt out of bucket
        if(gamepad2.left_trigger > 0.1){
            yClaw.setPosition(0.8644);
        }
        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1){
            yClaw.setPosition(0.4406);
        }

        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.SUB || axelMoving == axelMode.LOW) {
                if (slideMotor.getCurrentPosition() < slideMax * 0.45) {
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 120);
                }
            } else if(axelMoving == axelMode.BUCKET){
                if (slideMotor.getCurrentPosition() < slideMax - 400){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 500);
                }else if (slideMotor.getCurrentPosition() < slideMax){
                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 80);
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
        telemetry.addData("axelPos", axelPos);
    }

    private void findAxelPos() {
        slideLength = (slideMotor.getCurrentPosition() * ticksPerSlideMM) + slideLengthZero;
        axelAngle = Math.toDegrees(Math.asin(grabHeight/slideLength));
        axelPos = (int) (axelPosLowest - (axelAngle * ticksPerDegree));
    }

}