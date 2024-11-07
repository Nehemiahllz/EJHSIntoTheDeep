package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto1", group = "Auto")

public class Auto extends RobotCore {

    ElapsedTime timer = new ElapsedTime();
    boolean timeTrue = true;

    public void init() {
        super.init();
        slideMotor.setPower(0);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        printDebugData();
    }

    public void loop() {
        printDebugData();
        if(timeTrue) {
            timer.reset();
        }
        timeTrue = false;
        if(timer.seconds() >= 5) {
            claw.setPosition(0.39);
            axelMotor.setTargetPosition(0);
            axelMotor2.setTargetPosition(0);


            leftBack.setPower(-0.3);
            leftFront.setPower(0.3);
            rightBack.setPower(0.3);
            rightFront.setPower(-0.3);
        }

    }

    //Show data on the driver hub
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("yClaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());
        telemetry.addData("claw", claw.getPosition());
    }

}