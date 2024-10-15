package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestTele", group = "Test")

public class Testing extends RobotCore {


    public void init() {
        super.init();
    }

    public void loop() {
        printDebugData();
        if (gamepad1.a) {
            slideMotor.setPower(0.4);
        } else {
            slideMotor.setPower(0);
        }

    }

    //Prints different info for debugging
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
    }
}

