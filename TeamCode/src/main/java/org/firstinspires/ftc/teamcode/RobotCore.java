package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
public class RobotCore extends OpMode {

    DcMotor backLeft;
    DcMotor backRight;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor slideMotor;
    DcMotor axelMotor;

    Servo yClaw;
    Servo xClaw;
    Servo claw;

    @Override
    public void init(){

        ///commit testtt
    }

    @Override
    public void loop()
    {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        axelMotor = hardwareMap.get(DcMotor.class, "axelMotor");

        yClaw = hardwareMap.get(Servo.class, "yClaw");
        xClaw = hardwareMap.get(Servo.class, "xClaw");
        claw = hardwareMap.get(Servo.class, "claw");

    }

}
