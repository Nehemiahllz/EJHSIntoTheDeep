package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "MainTele", group = "A")
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

        //denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
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
            backLeftPower   /= max / 2.5;
            backRightPower  /= max / 2.5;
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



        //INTAKE  CONTROLS----------------------------------------------------------------------------------------------
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


        //HORIZONTAL SLIDE CONTROLS--------------------------------------------------------------------------
        
        if (gamepad1.right_stick_y > 0.5)
            horizontal.setPosition(0);
        if (gamepad1.right_stick_y < -0.5)
            horizontal.setPosition(0.23);

        //PIVOT CONTROLS -------------------------------------------------------------------------------------
            if (gamepad1.a)
                pivot.setPosition(0);
            if (gamepad1.y)
                pivot.setPosition(0.6);
            if (gamepad1.x)
                pivot.setPosition(0.2);

        //HANG CONTROLS----------------------------------------------------------------------------
        if(gamepad1.left_stick_button)
        {
            leftHang.setPosition(0);
            rightHang.setPosition(0);
        }
        if(gamepad1.right_stick_button)
        {
            leftHang.setPosition(1);
            rightHang.setPosition(1);
        }



        //VERTICAL SLIDES CONTROLS-----------------------------------------------------------
        if (gamepad2.dpad_up) {
            setSlidePositions(4400);
        }
        else if(gamepad2.dpad_down) {
            setSlidePositions(0);
        }
        else if(gamepad2.dpad_right  || gamepad1.dpad_right)
            setSlidePositions(290);
        else if(gamepad2.dpad_left || gamepad1.dpad_left)
            setSlidePositions(4480);
        else if(gamepad2.left_trigger > 0.3 || gamepad1.left_trigger > 0.3)
            setSlidePositions(325);
        else if(gamepad2.right_trigger > 0.3  || gamepad1.right_trigger > 0.3)
            setSlidePositions(2200);
        else
        {
            setBothSlideModes("RUN_USING_ENCODER");
            leftSlide.setPower(0.002);
            rightSlide.setPower(0.002);
        }

        if(gamepad1.dpad_up)
        {
            leftGrevious.setPower(1);
            rightGrevious.setPower(1);
        }
        else if(gamepad1.dpad_down)
        {
            leftGrevious.setPower(-1);
            rightGrevious.setPower(-1);
        }
        else
        {
            leftGrevious.setPower(0);
            rightGrevious.setPower(0);
        }


        //TELEMETRY----------------------------------------------------------------------
        telemetry.addData("Left Slide pos: ", leftSlide.getCurrentPosition());
        telemetry.addData("Right Slide Pos: ", rightSlide.getCurrentPosition());
        telemetry.update();

    }


    //METHODS-----------------------------------------------------------------------------
    public boolean limitHeight(String modifier, int targetPosition)
    {
        if(modifier == ">")
        {
            if(findLowestSlide().getCurrentPosition() > targetPosition)
                return true;
            else
                return false;
        }
        else if (modifier == "<")
        {
            if(findHighestSlide().getCurrentPosition() < targetPosition)
                return true;
            else
                return false;
        }
        else if (modifier == "<=")
        {
            if(findHighestSlide().getCurrentPosition() <= targetPosition)
                return true;
            else
                return false;
        }
        else if(modifier == ">=")
        {
            if(findLowestSlide().getCurrentPosition() >= targetPosition)
                return true;
            else
                return false;
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

    public void setSlidePositions(int tar)
    {

        leftSlide.setTargetPosition(tar);
        rightSlide.setTargetPosition(tar);
        setBothSlideModes("RUN_TO_POSITION");
        leftSlide.setPower(1);
        rightSlide.setPower(1);
    }

    public DcMotor findHighestSlide()
    {
        if(leftSlide.getCurrentPosition() > rightSlide.getCurrentPosition())
            return leftSlide;
        else
            return rightSlide;
    }

    public DcMotor findLowestSlide()
    {
        if(leftSlide.getCurrentPosition() < rightSlide.getCurrentPosition())
            return leftSlide;
        else
            return rightSlide;
    }

}
