package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OdometryPodComputer.GoBildaPinpointDriver;

import java.util.Arrays;
import java.util.List;
@Disabled
@TeleOp(name = "RobotCore", group = "Core")
public class RobotCore extends OpMode
{
    DcMotor frontLeft, frontRight, backRight, backLeft, leftSlide, rightSlide;

    Servo horizontal, pivot;

    CRServo leftClaw, rightClaw;

    GoBildaPinpointDriver computer;

    List<DcMotor> motors;


    public void init()
    {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");

        horizontal = hardwareMap.get(Servo.class, "horizontal");
        pivot = hardwareMap.get(Servo.class, "pivot");

        leftClaw = hardwareMap.get(CRServo.class, "leftClaw");
        rightClaw = hardwareMap.get(CRServo.class, "rightClaw");

        computer = hardwareMap.get(GoBildaPinpointDriver.class, "Computer");

        computer.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        motors = Arrays.asList(frontLeft, backLeft, frontRight, backRight, leftSlide, rightSlide);


        //Reversing
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        horizontal.setDirection(Servo.Direction.REVERSE);
        rightSlide.setDirection(DcMotor.Direction.REVERSE);

        //Slide Settings

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        for(DcMotor motor: motors)
        {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

    }

    @Override
    public void loop() {

    }


}
