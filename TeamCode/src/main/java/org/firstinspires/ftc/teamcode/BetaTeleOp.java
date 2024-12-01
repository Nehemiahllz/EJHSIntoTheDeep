package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OdometryPodComputer.GoBildaPinpointDriver;

@TeleOp(name = "Beta TeleOp", group = "Z")
public class BetaTeleOp extends OpMode {

    DcMotor frontLeft;
    DcMotor backLeft;

    DcMotor frontRight;
    DcMotor backRight;

    DcMotorEx leftSlide;
    DcMotorEx rightSlide;

    Servo leftClaw;
    Servo rightClaw;

    GoBildaPinpointDriver computer;
    double y = 0;
    double x = 0;
    double rx = 0;


    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = (y + x + rx) / denominator;
    double backLeftPower = (y - x + rx) / denominator;
    double frontRightPower = (y - x - rx) / denominator;
    double backRightPower = (y + x - rx) / denominator;

    double max;

    public void init()
    {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");

        computer = hardwareMap.get(GoBildaPinpointDriver.class, "computer");

        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rightSlide.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        rightClaw.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        computer.setOffsets(127, -317.5);
        computer.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        computer.setEncoderResolution(136.53);
        computer.resetPosAndIMU();
    }

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
            backLeftPower   /= max;
            backRightPower  /= max;
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

        if(gamepad1.dpad_up && limitHeight("<=", 500))
        {
            leftSlide.setPower(0.2);
            rightSlide.setPower(0.2);
        }
        else if(gamepad1.dpad_down && limitHeight(">=", 0))
        {
            leftSlide.setPower(-0.5);
            rightSlide.setPower(-0.5);
        }
        else
        {
            leftSlide.setPower(0.001);
            rightSlide.setPower(0.001);
        }

        if(gamepad1.a)
        {
            leftClaw.setPosition(0.4);
            rightClaw.setPosition(0.4);

        }
        else if (gamepad1.y)
        {
            leftClaw.setPosition(0);
            rightClaw.setPosition(0);
        }

        computer.update();

        telemetry.addData("LeftSlide:" , leftSlide.getCurrentPosition());
        telemetry.addData("rightSlide:", rightSlide.getCurrentPosition());

        telemetry.addData("X Pos:", computer.getPosX());
        telemetry.addData("Y Pos:", computer.getPosY());
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
}
