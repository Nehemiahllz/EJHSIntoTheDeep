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
@Autonomous(name = "Specimens", group = "Autonomous")
public class Specimens extends LinearOpMode
{




    @Override
    public void runOpMode() throws InterruptedException
    {
        Slides slides = new Slides(hardwareMap);
        Claw claw = new Claw(hardwareMap);

        Pose2d startPose = new Pose2d( -12, 61, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        TrajectoryActionBuilder toSpecimenBar = drive.actionBuilder(startPose)
                .lineToY(32)
                .waitSeconds(0.2);

        TrajectoryActionBuilder toSample1 = drive.actionBuilder(new Pose2d(-12, 32, Math.toRadians(270)))
                .lineToY(40)
                .strafeTo(new Vector2d(-50, 41));

        TrajectoryActionBuilder dropOff1 = drive.actionBuilder( new Pose2d(-50, 40, Math.toRadians(270)))
                //.splineToLinearHeading(new Pose2d(-50, 45, Math.toRadians(90)), Math.toRadians(90));
                .turn(Math.toRadians(185));

        TrajectoryActionBuilder toSample2 = drive.actionBuilder( new Pose2d(-50, 45, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(-60, 40, Math.toRadians(180)), Math.toRadians(90));

        //TrajectoryActionBuilder dropOff2 = drive.actionBuilder(new Pose2d(-60, 40, Math.toRadians(180)))
                       // .splineToLinearheading(new Pose2d())

        Actions.runBlocking(claw.setPivot(0.7));
        Actions.runBlocking(slides.setHorizontal(0));
        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                slides.setSlidePositions(2170),
                                claw.setPivot(0.30),
                                toSpecimenBar.build()
                        ),
                        new ParallelAction(

                                //to Sample 1
                                toSample1.build(),
                                slides.setSlidePositions(1300),
                                slides.setHorizontal(0.73),
                                claw.setPivot(0)
                        ),
                        new ParallelAction(
                                claw.intake(),
                                slides.setSlidePositions(0)
                        ),
                        new SleepAction(0.5),
                        claw.off(),
                        new ParallelAction(
                                dropOff1.build(),
                                slides.setSlidePositions(400)
                                ),
                        claw.eject(),
                        new SleepAction(0.5)
//                        slides.setSlidePositions(600),
//                        toSample2.build(),
//                        new ParallelAction(
//                                claw.intake(),
//                                slides.setSlidePositions(0)
//                        ),
//                        new SleepAction(1),
//                        claw.off()





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


        public class SetSlidePositions implements Action {

            int target;
            boolean under;

            public SetSlidePositions(int tar) {
                target = tar;
            }


            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {


                if (leftSlide.getCurrentPosition() < target && rightSlide.getCurrentPosition() < target) {
                    under = true;
                    while (under) {
                        if (leftSlide.getCurrentPosition() >= target && rightSlide.getCurrentPosition() >= target)
                            under = false;
                        leftSlide.setPower(1);
                        rightSlide.setPower(1);
                    }

                    leftSlide.setPower(0.002);
                    rightSlide.setPower(0.002);

                    return false;
                } else if (leftSlide.getCurrentPosition() > target && rightSlide.getCurrentPosition() > target) {
                    under = false;
                    while (!under) {
                        if (leftSlide.getCurrentPosition() <= target && rightSlide.getCurrentPosition() <= target)
                            under = true;
                        leftSlide.setPower(-1);
                        rightSlide.setPower(-1);
                    }
                    leftSlide.setPower(0.002);
                    rightSlide.setPower(0.002);

                    return false;
                } else return false;
            }
        }

        public Action setSlidePositions(int tar) {
            return new SetSlidePositions(tar);
        }

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
        public Action resetEncoders(){ return new ResetEncoders();}



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
        public Action setHorizontal(double target) {return new SetHorizontal(target);}
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
        public Action setPivot(double target){ return new SetPivot(target);}




        public class Eject implements Action {
            ElapsedTime timer = new ElapsedTime();
            double runTime = 0.0;
            public Eject(){
                runTime = 0.0;
            };
            public Eject(double time)
            {
                runTime = time;
            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if(runTime > 0.0) {
                    timer.reset();
                    while(timer.seconds() < runTime) {
                        leftClaw.setPower(-0.5);
                        rightClaw.setPower(0.5);
                    }
                    leftClaw.setPower(0);
                    rightClaw.setPower(0);
                }
                else
                {
                    leftClaw.setPower(-0.5);
                    rightClaw.setPower(0.5);
                }
                return false;
            }
        }

        public Action eject() {
            return new Eject();
        }
        public Action eject(double time){ return new Eject(time);}


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
                        leftClaw.setPower(1);
                        rightClaw.setPower(-1);
                    }
                    leftClaw.setPower(0);
                    rightClaw.setPower(0);
                }
                else
                {
                    leftClaw.setPower(1);
                    rightClaw.setPower(-1);
                }
                return false;
            }
        }

        public Action intake() {
            return new Intake();
        }
        public Action intake(double time){
            return new Intake(time);
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
