package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TestTele", group = "Test")

public class Testing extends RobotCore {

    double moveX;
    double moveY;
    double turnX;

    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    int slideMax = 4270;
    int slideMin = 0;

    boolean axelMovingA = false;
    boolean axelMovingB = false;
    boolean axelMovingX = false;

    boolean hanging = false;


    public void init() {
        super.init();

        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);

        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setPower(0.8);
        axelMotor2.setPower(0.8);

        slideMotor.setTargetPosition(slideMotor.getCurrentPosition());
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(0.7);
        slideMin = slideMotor.getCurrentPosition();

        printDebugData();
    }

    public void loop() {
        printDebugData();
        if (gamepad2.dpad_up) {
                slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 50);
        }
        if (gamepad2.dpad_down){
                slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 50);
        }

        if(gamepad1.dpad_up){
            claw.setPosition(claw.getPosition() - 0.01);
        }
        if(gamepad1.dpad_down){
            claw.setPosition(claw.getPosition() + 0.01);
        }

        if(gamepad1.dpad_left){
            yClaw.setPosition(yClaw.getPosition() - 0.01);
        }

        if(gamepad1.dpad_right){
            yClaw.setPosition(yClaw.getPosition() + 0.01);
        }

        if(gamepad1.a){
            xClaw.setPosition(xClaw.getPosition() - 0.01);
        }

        if(gamepad1.b){
            xClaw.setPosition(xClaw.getPosition() + 0.01);
        }

        axelMotor.setTargetPosition(axelMotor.getCurrentPosition());
        axelMotor2.setTargetPosition(axelMotor.getCurrentPosition());
    }

    //Prints different info for debugging
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("claw",  claw.getPosition());
        telemetry.addData("yCLaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());

    }
}

