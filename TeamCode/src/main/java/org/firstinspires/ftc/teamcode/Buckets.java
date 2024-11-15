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
@Autonomous(name = "Buckets", group = "Autonomous")
public class Buckets extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(-12, -61, Math.toRadians(90));

        //Change the X and Y values for picking up samples 2 and 3

       // Pose2d sampleTwoPose = new Pose2d(sampleTwoX,sampleTwoY,Math.toRadians(120));


        //Pose2d sampleThreePose = new Pose2d(sampleThreeX,sampleThreeY,Math.toRadians(120));



        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        Slides slides = new Slides(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        TrajectoryActionBuilder toSpecimenBar = drive.actionBuilder(startPose)
                .waitSeconds(0.3)
                .lineToY(-34)
                .waitSeconds(0.5);

        Pose2d sampleOnePose = new Pose2d(-44,-30,Math.toRadians(120));
        TrajectoryActionBuilder toSample1 = drive.actionBuilder(new Pose2d(-12, -32, Math.toRadians(90)))
                .lineToY(-40)
                .splineToLinearHeading(sampleOnePose, Math.toRadians(90));

        /** **************** **/
        Pose2d bucketPose = new Pose2d(-52, -52, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket1 = drive.actionBuilder(sampleOnePose)
                .waitSeconds(0.3)
                .lineToX(-40)
                .splineToLinearHeading(bucketPose, Math.toRadians(180));

        double sampleTwoX = -50.5, sampleTwoY = -24.5;
        TrajectoryActionBuilder toSample2 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-47, -45))
                .splineToLinearHeading(new Pose2d(-40,-25, Math.toRadians(180)), -90)


                .strafeTo(new Vector2d(sampleTwoX,sampleTwoY));

        TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(90)))
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));

        double sampleThreeX = -60.5, sampleThreeY = -23;
        TrajectoryActionBuilder toSample3 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-47, -45))
                .splineToLinearHeading(new Pose2d(-48,-30, Math.toRadians(180)), 0)
                .strafeTo(new Vector2d(sampleThreeX,sampleThreeY));

        TrajectoryActionBuilder toBucket3 = drive.actionBuilder(new Pose2d(sampleThreeX, sampleThreeY, Math.toRadians(160)))
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));

        TrajectoryActionBuilder practicePark = drive.actionBuilder(bucketPose)
                .splineToSplineHeading(startPose, Math.toRadians(-90));

        TrajectoryActionBuilder park = drive.actionBuilder(bucketPose)
                .splineToLinearHeading(new Pose2d(-20,-12, Math.toRadians(0)), Math.toRadians(0));





        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        //Specimen
                        new ParallelAction(
                                slides.setHorizontal(0),
                                slides.setSlidePositions(2300),
                                claw.setPivot(0.6),
                                toSpecimenBar.build()
                        ),
                        new ParallelAction(
                                toSample1.build(),
                                slides.setSlidePositions(700),
                                claw.setPivot(0),
                                //slides.setHorizontal(0.6),
                                claw.intake()
                        //to Sample 1
                        ),
                        slides.setSlidePositions(0),
                        new SleepAction(0.7),
                        //slides.setHorizontal(0.07),
                        new ParallelAction(
                                claw.off(),
                                slides.setSlidePositions(4360),
                                claw.setPivot(0.74),
                                toBucket1.build()
                        ),
                        //new SleepAction(0.2),
                        //slides.setHorizontal(0.50),
                        new SleepAction(0.3),
                        claw.eject(0.5),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),
                        new SleepAction(0.2),
                        new ParallelAction(
                                toSample2.build(),
                                slides.setSlidePositions(700),
                                claw.intake(),
                                claw.setPivot(0)
                        ),

                        //To sample 2
                        //slides.setHorizontal(0.64),
                        //claw.setPivot(0),
                        new SleepAction(0.1),
                        //new SleepAction(0.4),
                        slides.setSlidePositions(0),
                        new SleepAction(0.5),
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0),
                                toBucket2.build(),
                                claw.setPivot(0.785),
                                slides.setSlidePositions(4390)
                                ),

                        //Drop sample 2
                        //slides.setHorizontal(0.7),
                        new SleepAction(0.5),
                        claw.eject(0.5),
                        new ParallelAction(
                        claw.setPivot(1),
                        slides.setHorizontal(0)
                        ),
                        new ParallelAction(
                                toSample3.build(),
                                slides.setSlidePositions(700),
                                claw.setPivot(0)
                        ),
                        //to Sample 3
                        new ParallelAction(

                                claw.intake()),
                                new SleepAction(0.1),
                                slides.setSlidePositions(0),
                                new SleepAction(0.5),
                                new ParallelAction(
                                        claw.off(),
                                        slides.setHorizontal(0),
                                        toBucket3.build(),
                        claw.setPivot(0.785),
                        slides.setSlidePositions(4390),
                        slides.setHorizontal(0)
                                ),
                        claw.eject(0.5),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),

                        //Parking
                        new ParallelAction(
                                claw.setPivot(0),
                                slides.setHorizontal(0),
                                slides.setSlidePositions(0)
                        )

                        //Practice Park will hopefully put this close to the start pose to help reset
                        //practicePark.build()
                        //Replace with park.build for meets if needed
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


                if(leftSlide.getCurrentPosition() < target && rightSlide.getCurrentPosition() < target) {
                    leftSlide.setPower(1);
                    rightSlide.setPower(1);
                    return true;
                } else if (leftSlide.getCurrentPosition() > target && rightSlide.getCurrentPosition() > target) {
                    leftSlide.setPower(-1);
                    rightSlide.setPower(-1);
                    return true;
                }
                else
                {
                    leftSlide.setPower(0.002);
                    rightSlide.setPower(0.002);
                    return false;
                }
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
            ElapsedTime runtime = new ElapsedTime();
            double targetTime = 0;
            double targetPos;

            public SetPivot(double target)
            {
                targetPos = target;
            }

            public SetPivot(double target, double time)
            {
                targetPos = target;
                targetTime = time;

            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket)
            {
                if(runtime.seconds() < targetTime)
                    return true;
                else
                {
                    pivot.setPosition(targetPos);
                    return false;
                }
            }
        }
        public Action setPivot(double target){ return new SetPivot(target);}
        public Action setPivot(double target, double time){return new SetPivot(target, time);}




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

