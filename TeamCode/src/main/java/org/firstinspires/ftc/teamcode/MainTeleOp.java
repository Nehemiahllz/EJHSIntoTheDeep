package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "MainTele", group = "Main")
public class MainTeleOp extends RobotCore
{

    double y = 0;
    double x = 0;
    double rx = 0;


    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = (y + x + rx) / denominator;
    double backLeftPower = (y - x + rx) / denominator;
    double frontRightPower = (y - x - rx) / denominator;
    double backRightPower = (y + x - rx) / denominator;

    double max;

    //This is a public subclass of RobotCore, so the robot's wheel motors are initialized in RobotCore
    public void init()
    {
        super.init();
    }

    public void loop() {


        //MOVING CONTROLS-----------------------------------------------------------------
        y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        x = -gamepad1.left_stick_x; // Counteract imperfect strafing
        rx = gamepad1.right_stick_x;

//        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeftPower = (y + x - rx);
        backLeftPower = (y - x - rx);
        frontRightPower = (y - x + rx);
        backRightPower = (y + x + rx);

        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max / 2;
            backRightPower  /= max / 2;
        }

        if (gamepad1.b && (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1)) {
            frontLeft.setPower(frontLeftPower * 0.5);
            frontRight.setPower(frontRightPower * 0.5);
            backLeft.setPower(backLeftPower * 0.5);
            backRight.setPower(backRightPower * 0.5);
        } else if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        } else {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }


        //INTAKE  CONTROLS------------------------------------------------------
        if (gamepad1.right_stick_y > 0.5)
            horizontal.setPosition(0);
        if (gamepad1.right_stick_y < -0.5)
            horizontal.setPosition(0.42);

        if (gamepad1.a)
            pivot.setPosition(0);
        if (gamepad1.y)
            pivot.setPosition(1);
        if(gamepad1.x)
            pivot.setPosition(0.65);

        //Taking In Sample
        if (gamepad2.left_bumper) {
            leftClaw.setPower(1);
            rightClaw.setPower(-1);
        //Pushing Out Sample
        } else if (gamepad2.right_bumper) {
            leftClaw.setPower(-1);
            rightClaw.setPower(1);
        } else {
            leftClaw.setPower(0);
            rightClaw.setPower(0);
        }


        //SLIDE CONTROLS-----------------------------------------------------------
        if (gamepad2.dpad_up && limitHeight("<", 4000)) {
            leftSlide.setPower(1);
            rightSlide.setPower(1);
        } else if (gamepad2.dpad_down && limitHeight(">=", 0)) {
            leftSlide.setPower(-0.75);
            rightSlide.setPower(-0.75);
        } else {
            leftSlide.setPower(0.002);
            rightSlide.setPower(0.002);
        }

        //Slide L 4088 R 4060

        //TELEMETRY----------------------------------------------------------------------
        telemetry.addData("Left Slide pos: ", leftSlide.getCurrentPosition());
        telemetry.addData("Right Slide Pos: ", rightSlide.getCurrentPosition());
        telemetry.update();

    }

    public boolean limitHeight(String modifier, int targetPosition)
    {
        if(modifier == ">")
        {
            if(leftSlide.getCurrentPosition() > targetPosition  && rightSlide.getCurrentPosition() > targetPosition)
                return true;
            else return false;
        }
        else if (modifier == "<")
        {
            if(leftSlide.getCurrentPosition() < targetPosition  && rightSlide.getCurrentPosition() < targetPosition)
                return true;
            else return false;
        }
        else if (modifier == "<=")
        {
            if(leftSlide.getCurrentPosition() <= targetPosition && rightSlide.getCurrentPosition() <= targetPosition)
                return true;
            else return false;
        }
        else if(modifier == ">=")
        {
            if(leftSlide.getCurrentPosition() >= targetPosition && rightSlide.getCurrentPosition() >= targetPosition)
                return true;
            else return false;
        }
        else return false;
    }

    public void setBothSlideModes(String mode)
    {
        if(mode.equals("RUN_USING_ENCODER"))
        {
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else if(mode.equals("RUN_WITHOUT_ENCODER"))
        {
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        else if(mode.equals("RUN_TO_POSITION"))
        {
            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else if(mode.equals("STOP_AND_RESET_ENCODER"))
        {
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

}
