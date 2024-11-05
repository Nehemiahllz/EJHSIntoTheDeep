package org.firstinspires.ftc.teamcode;

//import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.OdometryPodComputer.GoBildaPinpointDriver;

@TeleOp(name = "Debug", group = "Debug")
public class DebugTele extends RobotCore
{
    Pose2D myPose;

    double y = 0;
    double x = 0;
    double rx = 0;


    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = (y + x + rx) / denominator;
    double backLeftPower = (y - x + rx) / denominator;
    double frontRightPower = (y - x - rx) / denominator;
    double backRightPower = (y + x - rx) / denominator;

    double max;

    double zeroPower = 0.003;
    public void init()
    {
        super.init();
        computer.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        computer.setOffsets(-158.75, -190.5);
        computer.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

    }

    @Override
    public void loop()
    {

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


        computer.update();
        myPose = computer.getPosition();
        if(gamepad1.right_stick_y > 0.5)
            horizontal.setPosition(0);
        if(gamepad1.right_stick_y < -0.5)
            horizontal.setPosition(0.35);

        if(gamepad1.dpad_left)
            pivot.setPosition(0);
        if(gamepad1.dpad_right)
            pivot.setPosition(0.57);

        if(gamepad1.left_bumper)
        {
            leftClaw.setPower(1);
            rightClaw.setPower(-1);
            //telemetry.speak("Hello, my name is Betty", "deu", "de");

        }else if(gamepad1.right_bumper)
        {
            leftClaw.setPower(-1);
            rightClaw.setPower(1);
            //telemetry.speak(computer.toString(), "eng", "us");
        } else
        {
            leftClaw.setPower(0);
            rightClaw.setPower(0);
        }

        if(gamepad1.dpad_up && limitHeight("<", 4000))
        {
            leftSlide.setPower(1);
            rightSlide.setPower(1);
        }
        else if(gamepad1.dpad_down && limitHeight(">", 0))
        {
            leftSlide.setPower(-1);
            rightSlide.setPower(-1);
        }
//        else if(limitHeight("<", 300)) {
//            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        }
        else
        {
            leftSlide.setPower(zeroPower);
            rightSlide.setPower(zeroPower);
        }

        if(gamepad1.right_trigger > 0.5)
            zeroPower += 0.001;
        else if(gamepad1.left_trigger > 0.5)
            zeroPower -= 0.001;

        if(gamepad1.b) {
            try {
                testWheels();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(gamepad1.y)
            computer.resetPosAndIMU();
        telemetry.addLine("Current Positions:");
        telemetry.addData("Left Slide: ", leftSlide.getCurrentPosition());
        telemetry.addData("Right Slide: ", rightSlide.getCurrentPosition());
        telemetry.addData("Vertical Slides zero power:", zeroPower);
        telemetry.addData("Horizontal: ", horizontal.getPosition());
        telemetry.addData("Pivot: ", pivot.getPosition());
        if(leftClaw.getPower() != 0)
            telemetry.addData("Left Claw Active, Power of ", leftClaw.getPower());
        else telemetry.addLine("Left Claw Inactive");
        if(rightClaw.getPower() != 0)
            telemetry.addData("Right Claw Active, Power of ", rightClaw.getPower());
        else telemetry.addLine("Right Claw Inactive");
        telemetry.addLine();
        telemetry.addLine("Odometry Values:");
        telemetry.addData("X: ", computer.getPosX());
        telemetry.addData("Y: ", computer.getPosY());
        telemetry.addData("Heading: ", computer.getHeading());
        telemetry.addData("Position: ", computer.getPosition().toString());
        telemetry.update();



    }

    public void testWheels() throws InterruptedException {
        frontLeft.setPower(1);
        Thread.sleep(3000);
        frontLeft.setPower(0);
        frontRight.setPower(1);
        Thread.sleep(3000);
        frontRight.setPower(0);
        backLeft.setPower(1);
        Thread.sleep(3000);
        backLeft.setPower(0);
        backRight.setPower(1);
        Thread.sleep(3000);
        backRight.setPower(0);
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
        } else return false;
    }


}
