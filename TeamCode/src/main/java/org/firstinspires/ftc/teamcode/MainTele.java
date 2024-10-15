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
//        xClaw.setPosition(0);
//        yClaw.setPosition(0);
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

        if(gamepad1.left_bumper)
            yClaw.setPosition(0);
        else if(gamepad1.right_bumper)
            yClaw.setPosition(1);

        if(gamepad1.left_trigger > 0.1)
            xClaw.setPosition(0);
        else if(gamepad1.right_trigger > 0.1)
            xClaw.setPosition(1);

        //Arm
        if(gamepad1.a) {
            yClaw.setPosition(0);
            xClaw.setPosition(0);
        }
        else if(gamepad1.b){
            yClaw.setPosition(1);
            xClaw.setPosition(1);
        }

        if(gamepad1.x)
            claw.setPosition(0);
        else if(gamepad1.y){
            claw.setPosition(0.75);

    }}

    private void printDebugData() {
            telemetry.addLine("----Controller Inputs----");
            telemetry.addData("Axel", axelMotor.getCurrentPosition());
            telemetry.addData("Slide", slideMotor.getCurrentPosition());
            telemetry.addData("yClaw", yClaw.getPosition());
            telemetry.addData("xClaw", xClaw.getPosition());
            telemetry.addData("claw", claw.getPosition());



    }

}
