package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        printDebugData();
    }

    public void loop() {
        findAxelPos();
        printDebugData();
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(gamepad1.dpad_up){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 50);
        }
        if(gamepad1.dpad_down){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 50);
        }

        if(gamepad1.a){
            axelMotor.setPower(1);
            axelMotor2.setPower(1);
            axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            yClaw.setPosition(0.65);
            if(slideMotor.getCurrentPosition() > 1000) {
                axelMotor.setTargetPosition(axelPos - 10);
                axelMotor2.setTargetPosition(axelPos - 10);
            }else{
                axelMotor.setTargetPosition(axelPos - 5);
                axelMotor2.setTargetPosition(axelPos - 5);
            }
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

