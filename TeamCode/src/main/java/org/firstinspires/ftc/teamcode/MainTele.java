package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MainTeleOp", group = "Main")

public class MainTele extends RobotCore{

    double moveX;
    double moveY;
    double turnX;

    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;


    public void init()
    {
        super.init();
    }

    public void loop() {

        printDebugData();

        moveX = gamepad1.left_stick_x;
        moveY = -gamepad1.left_stick_y;
        turnX = gamepad1.right_stick_x;

        frontLeftPower = moveY + moveX + turnX;
        frontRightPower = moveY - moveX - turnX;
        backLeftPower = moveY - moveX + turnX;
        backRightPower = moveY + moveX - turnX;

        //Drivetrain
        if(Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1){
            frontLeft.setPower(frontLeftPower * 0.5);
            frontRight.setPower(frontRightPower * 0.5);
            backLeft.setPower(backLeftPower * 0.5);
            backRight.setPower(backRightPower * 0.5);
        } else{
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);



        }

        //Arm

        if(gamepad1.a){
            claw.getPosition(.7);
        }
        if(gamepad1.b){
            claw.getPosition(0);
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
