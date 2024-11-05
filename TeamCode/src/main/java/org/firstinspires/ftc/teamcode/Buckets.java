package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
@Autonomous(name = "Buckets", group = "Autonomous")
public class Buckets extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(-12, -61, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        Slides slides = new Slides(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        TrajectoryActionBuilder toSpecimenBar = drive.actionBuilder(startPose)
                .lineToY(-32)
                .waitSeconds(0.2);

        TrajectoryActionBuilder toSample1 = drive.actionBuilder(new Pose2d(-12, -32, Math.toRadians(90)))
                .lineToY(-40)
                .splineToLinearHeading(new Pose2d(-40,-37,Math.toRadians(120)), Math.toRadians(90));

        TrajectoryActionBuilder toBucket1 = drive.actionBuilder(new Pose2d(-40, -37, Math.toRadians(120)))
                .splineToSplineHeading(new Pose2d(-52, -50, Math.toRadians(-140)), -90);

        TrajectoryActionBuilder toSample2 = drive.actionBuilder(new Pose2d(-52, -50, Math.toRadians(-140)))
                .splineToLinearHeading(new Pose2d(-56,-39, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(-56, -39, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(-49, -50, Math.toRadians(-140)), -90);

        TrajectoryActionBuilder toSample3 = drive.actionBuilder(new Pose2d(-49, -50, Math.toRadians(-140)))
                .splineToLinearHeading(new Pose2d(-52,-30, Math.toRadians(160)), Math.toRadians(90));

        TrajectoryActionBuilder toBucket3 = drive.actionBuilder(new Pose2d(-52, -30, Math.toRadians(160)))
                .splineToLinearHeading(new Pose2d(-49, -50, Math.toRadians(-140)), -90);

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(-49, -50, Math.toRadians(-140)))
                .splineToLinearHeading(new Pose2d(-20,-8, Math.toRadians(0)), Math.toRadians(0));





        Actions.runBlocking(claw.setPivot(0.7));
        Actions.runBlocking(slides.setHorizontal(0));
        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(


                        //Specimen
                        new ParallelAction(
                                slides.setSlidePositions(2170),
                                claw.setPivot(0.30),
                                toSpecimenBar.build()
                        ),
                        new ParallelAction(
                                toSample1.build(),
                                slides.setSlidePositions(1300)
                        //to Sample 1
                        ),
                        new ParallelAction(
                                claw.setPivot(0),
                                slides.setHorizontal(0.73),
                                slides.setSlidePositions(700)
                        ),
                        new ParallelAction(
                                claw.intake(),
                                slides.setSlidePositions(0)
                        ),
                        new SleepAction(1),
                        claw.off(),
                        new ParallelAction(
                                slides.setSlidePositions(300),
                                claw.setPivot(0.7),
                                slides.setHorizontal(0)
                        ),


                        //Drop sample 1
                        new ParallelAction(
                                claw.setPivot(0.36),
                                toBucket1.build()
                        ),
                        slides.setSlidePositions(4300),
                        slides.setHorizontal(0.65),
                        new SleepAction(0.5),
                        claw.eject(1),
                        new ParallelAction(
                                claw.setPivot(0.7),
                                slides.setHorizontal(0)
                        ),
                        new ParallelAction(
                                slides.setSlidePositions(700),
                                claw.setPivot(0),
                                toSample2.build()
                        ),


                        //To sample 2
                        slides.setHorizontal(0.73),
                        claw.intake(),
                        new SleepAction(0.2),
                        slides.setSlidePositions(0),
                        new SleepAction(0.3),
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0)
                        ),


                        //Drop sample 2
                        toBucket2.build(),
                        new ParallelAction(
                                claw.setPivot(0.37),
                                slides.setSlidePositions(4300),
                                slides.setHorizontal(0.73)
                        ),
                        new SleepAction(0.5),
                        claw.eject(1),
                        claw.setPivot(0.7),
                        slides.setHorizontal(0),
                        new SleepAction(0.2),
                        new ParallelAction(
                                slides.setSlidePositions(0),
                                claw.setPivot(0)
                        ),


//                        //to Sample 3
//                        toSample3.build(),
//                        slides.setHorizontal(0.73),
//                        claw.intake(),
//                        slides.setSlidePositions(0),
//                        new SleepAction(0.2),
//                        claw.off(),
//                        slides.setHorizontal(0),
//
//
//                        //Drop sample 3
//                        toBucket3.build(),
//                        claw.setPivot(0.37),
//                        slides.setSlidePositions(4200),
//                        slides.setHorizontal(0.73),
//                        //new SleepAction(0.5),
//                        claw.eject(1),
//                        //new SleepAction(0.2),
//                        claw.setPivot(0.7),
//                        slides.setHorizontal(0),
//                        //new SleepAction(1),
//                        new ParallelAction(
//                                slides.setSlidePositions(0),
//                                claw.setPivot(0)
//                        ),

                        //Parking
                        park.build(),
                        new ParallelAction(
                                claw.setPivot(0),
                                slides.setHorizontal(0),
                                slides.setSlidePositions(0)
                        )
                )
        );

    }
    //Accessory moving classes

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
            return new Off();
        }
    }
}

