package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Other_RoadRunner_Classes.MecanumDrive;


@Config 
@Autonomous(group = "A", name = "Specimens Reimagined")
public class SpecimensReimagined extends LinearOpMode {
    
    @Override
    public void runOpMode() throws InterruptedException
    {
        Slides slides = new Slides(hardwareMap);
        Claw claw = new Claw(hardwareMap);

        Pose2d startPose = new Pose2d( 12, 61, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        TrajectoryActionBuilder specimen1 = drive.actionBuilder(startPose)
                .waitSeconds(0.5)
                .lineToY(36);


        Pose2d sampleOnePos = new Pose2d(-23, 39, Math.toRadians(270));
        TrajectoryActionBuilder sample1 = drive.actionBuilder(new Pose2d(12, 36, Math.toRadians(270)))
                .waitSeconds(1)
                .lineToY(50)
                .splineToLinearHeading(sampleOnePos, Math.toRadians(0));

        TrajectoryActionBuilder dropOff = drive.actionBuilder( sampleOnePos)
                .turn(Math.toRadians(210))
                .lineToY(43);

        Pose2d sampleTwoPos = new Pose2d(-32, 43, Math.toRadians(270));
        TrajectoryActionBuilder sample2 = drive.actionBuilder( new Pose2d(-20, 42, Math.toRadians(210)))
                .waitSeconds(1)
                .splineToLinearHeading(sampleTwoPos, Math.toRadians(0));

        TrajectoryActionBuilder dropOff2 = drive.actionBuilder(sampleTwoPos)
                .turn(Math.toRadians(180))
                .strafeTo(new Vector2d(-28, 42));

        TrajectoryActionBuilder specimen2 = drive.actionBuilder(new Pose2d(-28, 42, Math.toRadians(90)))
                .waitSeconds(1)
                .lineToY(52);

        TrajectoryActionBuilder toBar = drive.actionBuilder(new Pose2d(-20, 52, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(20, 39, Math.toRadians(270)), Math.toRadians(0))
                .strafeTo(new Vector2d(20,31));

        TrajectoryActionBuilder specimen3 = drive.actionBuilder(new Pose2d(20, 31, Math.toRadians(270)))
                .lineToY(50)
                .splineToLinearHeading(new Pose2d(-23, 40, Math.toRadians(90)), Math.toRadians(270));

        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                specimen1.build(),
                                slides.setSlidePositions(2100),
                                claw.setPivot(0.38),
                                slides.setHorizontal(0)
                        )
                )
        );

    }

    public class Slides{
        DcMotorEx leftSlide;
        DcMotorEx rightSlide;
        Servo horizontal;

        public Slides(HardwareMap hardwareMap) {
            leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
            rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");
            horizontal = hardwareMap.get(Servo.class, "horizontal");

            leftSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            leftSlide.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            rightSlide.setDirection(DcMotor.Direction.REVERSE);
            horizontal.setDirection(Servo.Direction.REVERSE);

        }


        public class SetSlidePositions implements Action
        {
            int target;
            public SetSlidePositions(int tar)
            {
                target = tar;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                leftSlide.setTargetPosition(target);
                rightSlide.setTargetPosition(target);
                leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftSlide.setPower(1);
                rightSlide.setPower(1);
                return false;
            }
        }

        public Action setSlidePositions(int tar) {return new Slides.SetSlidePositions(tar);}



        public class ResetEncoders implements Action
        {

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                leftSlide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                rightSlide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                return false;
            }
        }
        public Action resetEncoders(){ return new Slides.ResetEncoders();}



        public class SetHorizontal implements Action {
            double target;
            public SetHorizontal(double pos)
            {
                target = pos;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                horizontal.setPosition(target);
                return false;
            }
        }
        public Action setHorizontal(double target) {return new Slides.SetHorizontal(target);}
    }

    public class Claw {
        Servo pivot;
        CRServo leftClaw;
        CRServo rightClaw;

        public Claw(HardwareMap hardwareMap) {
            pivot = hardwareMap.get(Servo.class, "pivot");
            leftClaw = hardwareMap.get(CRServo.class, "leftClaw");
            rightClaw = hardwareMap.get(CRServo.class, "rightClaw");

            pivot.setDirection(Servo.Direction.REVERSE);
        }



        public class SetPivot implements Action {
            double targetPos;

            public SetPivot(double target)
            {
                targetPos = target;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket)
            {
                pivot.setPosition(targetPos);
                return false;
            }
        }
        public Action setPivot(double target){ return new Claw.SetPivot(target);}




        public class Eject implements Action {
            ElapsedTime timer = new ElapsedTime();
            double runTime = 0.0;
            double ejectPower = 0.5;
            public Eject(){
                runTime = 0.0;
            };
            public Eject(double time)
            {
                runTime = time;
            }

            public Eject(double time, double power)
            {
                runTime = time;
                ejectPower = power;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if(runTime > 0.0) {
                    timer.reset();
                    while(timer.seconds() < runTime) {
                        leftClaw.setPower(ejectPower);
                        rightClaw.setPower(-ejectPower);
                    }
                    leftClaw.setPower(0);
                    rightClaw.setPower(0);
                }
                else
                {
                    leftClaw.setPower(ejectPower);
                    rightClaw.setPower(-ejectPower);
                }
                return false;
            }
        }

        public Action eject() {
            return new Claw.Eject();
        }
        public Action eject(double time){ return new Claw.Eject(time);}
        public Action eject(double time, double power){return new Claw.Eject(time, power);}


        public class Intake implements Action {
            ElapsedTime timer = new ElapsedTime();
            double runTime = 0.0;

            public Intake(){
                runTime = 0.0;
            }
            public Intake(double time)
            {
                runTime = time;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if(runTime > 0.0 )
                {
                    timer.reset();
                    while(timer.seconds() < runTime)
                    {
                        leftClaw.setPower(-1);
                        rightClaw.setPower(1);
                    }
                    leftClaw.setPower(0);
                    rightClaw.setPower(0);
                }
                else
                {
                    leftClaw.setPower(-1);
                    rightClaw.setPower(1);
                }
                return false;
            }
        }

        public Action intake() {
            return new Claw.Intake();
        }


        public class Off implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                leftClaw.setPower(0);
                rightClaw.setPower(0);
                return false;
            }
        }

        public Action off() {
            return new Claw.Off();
        }
    }

}

