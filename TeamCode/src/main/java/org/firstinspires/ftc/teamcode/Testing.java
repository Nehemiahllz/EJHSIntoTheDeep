package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "TestTele", group = "Test")

public class Testing extends RobotCore {

    int axelPos;
    //Fix values, once measured
    final double grabHeight = 76.2;
    final double ticksPerSlideMM = 4.468;
    final double ticksPerDegree = 3.877;
    final double axelPosLowest = 409;
    double slideLength;
    final double slideLengthZero = 317.5;
    double axelAngle;


    public void init() {
        super.init();
        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        axelMotor.setPower(1);
        axelMotor2.setPower(1);
        slideMotor.setPower(1);
        printDebugData();
    }

    public void loop() {
        printDebugData();
        findAxelPos();

        if(gamepad1.a){
            axelMotor.setTargetPosition(axelMotor.getCurrentPosition() + 12);
            axelMotor2.setTargetPosition(axelMotor.getCurrentPosition() + 12);
        }

        if(gamepad1.b){
            axelMotor.setTargetPosition(axelMotor.getCurrentPosition() - 12);
            axelMotor2.setTargetPosition(axelMotor.getCurrentPosition() - 12);
        }

        if(gamepad1.left_trigger > 0.1){
            axelMotor.setTargetPosition(0);
            axelMotor2.setTargetPosition(0);
        }

        if(gamepad1.right_trigger > 0.1){
            axelMotor.setTargetPosition(679);
            axelMotor2.setTargetPosition(679);
        }



        if(gamepad1.x){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 70);
            slideMotor2.setTargetPosition(slideMotor.getCurrentPosition() + 70);
        }

        if(gamepad1.y){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 70);
            slideMotor2.setTargetPosition(slideMotor.getCurrentPosition() - 70);
        }

        if(gamepad1.left_bumper){
            slideMotor.setTargetPosition(0);
            slideMotor2.setTargetPosition(0);
        }

        if(gamepad1.right_bumper){
            slideMotor.setTargetPosition(4250);
            slideMotor2.setTargetPosition(4250);
        }



        if(gamepad2.dpad_up){
            claw.setPosition(claw.getPosition() + 0.002);
        }
        if(gamepad2.dpad_down){
            claw.setPosition(claw.getPosition() - 0.002);
        }

        if(gamepad2.dpad_left){
            yClaw.setPosition(yClaw.getPosition() - 0.002);
        }
        if(gamepad2.dpad_right){
            yClaw.setPosition(yClaw.getPosition() + 0.002);
        }

        if(gamepad2.a){
            xClaw.setPosition(xClaw.getPosition() + 0.002);
        }
        if(gamepad2.b){
            xClaw.setPosition(xClaw.getPosition() - 0.002);
        }
    }

    //Prints different info for debugging
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("claw",  claw.getPosition());
        telemetry.addData("yCLaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());
        telemetry.addData("axelPos", axelPos);
    }

    private void findAxelPos() {
        slideLength = (slideMotor.getCurrentPosition() * ticksPerSlideMM) + slideLengthZero;
        axelAngle = Math.toDegrees(Math.asin(grabHeight/slideLength));
        axelPos = (int) (axelPosLowest - (axelAngle * ticksPerDegree));
    }
}

