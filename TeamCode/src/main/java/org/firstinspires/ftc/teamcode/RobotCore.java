package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//RobotCore
@Disabled
public class RobotCore extends OpMode {

    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor rightFront;

    DcMotor slideMotor;
    DcMotor slideMotor2;

    DcMotorEx axelMotor;
    DcMotorEx axelMotor2;

    Servo yClaw;
    Servo xClaw;
    Servo claw;

    Servo sweep;


    @Override
    public void init(){
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        slideMotor2 = hardwareMap.get(DcMotor.class, "slideMotor2");

        axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
        axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");

        yClaw = hardwareMap.get(Servo.class, "yClaw");
        yClaw.setDirection(Servo.Direction.FORWARD);

        xClaw = hardwareMap.get(Servo.class, "xClaw");
        xClaw.setDirection(Servo.Direction.REVERSE);

        claw = hardwareMap.get(Servo.class, "claw");
        claw.setDirection(Servo.Direction.FORWARD);

        sweep = hardwareMap.get(Servo.class, "sweep");
        sweep.setDirection(Servo.Direction.FORWARD);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection((DcMotor.Direction.REVERSE));

        axelMotor.setDirection(DcMotorEx.Direction.FORWARD);
        axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);

        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    //These methods are to be overridden in the classes
    @Override
    public void start(){

    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){

    }

    }