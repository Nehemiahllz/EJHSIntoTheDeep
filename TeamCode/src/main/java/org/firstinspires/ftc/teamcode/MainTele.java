package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    int axelTarPos;

    public enum axelMode {GRAB, SUB, BUCKET, RUNG, REST, HANG, HANGING, CLIP, LOW, RESET, grab2nd, pull2nd}
    axelMode axelMoving;

    ElapsedTime timer = new ElapsedTime();
    boolean timeTrue = true;

    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int axelTarget = 0;

    private final double ticks_in_degree = 700 / 180.0;

    public void init() {
        super.init();

        controller  = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

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

        controller.setPID(p, i , d);
        int axelPos = axelMotor.getCurrentPosition();
        double pid = controller.calculate(axelPos, axelTarget);
        double ff = Math.cos(Math.toRadians(axelTarget / ticks_in_degree)) * f;

        double power = pid + ff;

        axelMotor.setPower(power);
        axelMotor2.setPower(power);

        if(gamepad2.dpad_right){
            axelTarget += 10;
        }
        if(gamepad2.dpad_left){
            axelTarget -= 10;
        }


        if(gamepad2.right_bumper){
            axelMoving = axelMode.RESET;
            axelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            axelMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slideMotor.setTargetPosition(-3000);
        }

        if(axelMoving == axelMode.RESET) {
            axelMotor.setPower(-0.7);
            axelMotor2.setPower(-0.7);

            if(gamepad2.left_bumper){
                axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        }else{
            axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        slideMax = slideMin + 3030;
        axelMotor.setTargetPosition(axelTarPos);
        axelMotor2.setTargetPosition(axelTarPos);
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
                xClaw.setPosition(0.3);
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
            if (axelMoving == axelMode.HANG) {
                axelMotor.setPower(1);
                axelMotor2.setPower(1);
                axelMoving = axelMode.HANGING;
            }
        }

        if(slideMotor.getCurrentPosition() < slideMin + 150) {
            switch (axelMoving) {
                case SUB:
                    if(slideMotor.getCurrentPosition() > 300) {
                        axelTarPos = 330;
                    }else if (axelMotor.getCurrentPosition() > 267) {
                        axelTarPos = 285;
                    }else if(axelMotor.getCurrentPosition() > 235){
                        axelTarPos = 270;
                    } else if(axelMotor.getCurrentPosition() > 180){
                        axelTarPos = 240;
                    } else if (axelMotor.getCurrentPosition() > 90) {
                        axelTarPos = 190;
                    } else{
                        axelTarPos = 100;
                    }
                    break;
                case BUCKET:
                    axelTarPos = 0;
                    break;
                case HANG:
                    axelMotor.setPower(1);
                    axelMotor2.setPower(1);
                    axelTarPos = 125;
                    break;
                default:
                    axelTarPos = 0;
            }
        }

        if(axelMoving == axelMode.LOW){
            axelTarPos = 330;
        }

        if(axelMoving == axelMode.HANGING){
            axelTarPos = 0;
        }

        //Claw open
        if(gamepad1.left_trigger > 0.1){
            claw.setPosition(0.57);
        }
        if (gamepad1.right_trigger > 0.7) {
            claw.setPosition(0.36);
        }

        if(timer.time(TimeUnit.MILLISECONDS) > 200){
            if(gamepad1.dpad_left){
                xClaw.setPosition(xClaw.getPosition() + 0.11);
                timer.reset();
            }else if(gamepad1.dpad_right){
                xClaw.setPosition(xClaw.getPosition() - 0.11);
                timer.reset();
            }
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
        telemetry.addData("axelPos", axelTarPos);
    }

}