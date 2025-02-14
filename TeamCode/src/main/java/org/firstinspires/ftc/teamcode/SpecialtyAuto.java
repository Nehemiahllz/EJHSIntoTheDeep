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
@Autonomous(name = "SpecialtyAuto", group = "Z")
public class SpecialtyAuto extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        Slides slides = new Slides(hardwareMap);
        Claw claw = new Claw(hardwareMap);

        Pose2d startPose = new Pose2d( 39, 63, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        Pose2d bucketPose = new Pose2d(54, 55, Math.toRadians(45));
        TrajectoryActionBuilder dropOffOne = drive.actionBuilder(startPose)
                .splineToLinearHeading(bucketPose, Math.toRadians(90))
                ;

        Pose2d sampleOnePose = new Pose2d(50 ,43, Math.toRadians(270));
        TrajectoryActionBuilder sampleOne = drive.actionBuilder(bucketPose)
                        .splineToLinearHeading(sampleOnePose, Math.toRadians(0))
                ;

        bucketPose = new Pose2d(54, 53, Math.toRadians(45));
        TrajectoryActionBuilder dropOffTwo = drive.actionBuilder(sampleOnePose)
                        .splineToLinearHeading(bucketPose, Math.toRadians(0))
                ;

        Pose2d sampleTwoPose = new Pose2d(58.5, 41.5, Math.toRadians(270));
        TrajectoryActionBuilder sampleTwo = drive.actionBuilder(bucketPose)
                        .splineToLinearHeading(sampleTwoPose, Math.toRadians(0))
                ;

        bucketPose = new Pose2d(54, 50, Math.toRadians(45));
        TrajectoryActionBuilder dropOffThree = drive.actionBuilder(sampleTwoPose)
                .splineToLinearHeading(bucketPose, Math.toRadians(0))
                ;

        Pose2d sampleThreePose = new Pose2d(55, 26, Math.toRadians(0));
        TrajectoryActionBuilder sampleThree = drive.actionBuilder(bucketPose)
                .splineToLinearHeading(sampleThreePose, Math.toRadians(0))
                .lineToX(65)
                ;

        bucketPose = new Pose2d(53, 51, Math.toRadians(45));
        TrajectoryActionBuilder dropOffFour = drive.actionBuilder(sampleThreePose)
                .splineToLinearHeading(bucketPose, Math.toRadians(0))
                ;

        TrajectoryActionBuilder park = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(45, 10))
                .splineToLinearHeading(new Pose2d(22,5, Math.toRadians(180)), Math.toRadians(-90))
                ;

        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
               new SequentialAction(
                       //Bucket 1--------------------------------Bucket 1
                       new ParallelAction(
                               slides.setHorizontal(0),
                               slides.setSlidePositions(4400),
                               claw.setPivot(0.4),
                               dropOffOne.build()
                       ),
                       new SleepAction(1),
                       claw.eject(1,1),
                       claw.setPivot(0.7),

                       //Sample 1--------------------------------Sample 1
                       new ParallelAction(
                               sampleOne.build(),
                               new SequentialAction(
                                       new SleepAction(0.6),
                                       claw.setPivot(0.03),
                                       slides.setHorizontal(0.3)
                               ),
                               claw.intake(),
                               slides.setSlidePositions(500)
                       ),
                       slides.setSlidePositions(0),
                       new SleepAction(1),

                       //Bucket 2--------------------------------Bucket 2
                       new ParallelAction(
                               claw.off(),
                               slides.setSlidePositions(4400),
                               slides.setHorizontal(0),
                               claw.setPivot(0.4),
                               dropOffTwo.build()
                       ),
                       new SleepAction(1),
                       claw.eject(1,0.5),
                       claw.setPivot(0.7),

                       //Sample 2--------------------------------Sample 2
                       new ParallelAction(
                               sampleTwo.build(),
                               new SequentialAction(
                                       new SleepAction(0.6),
                                       claw.setPivot(0.03),
                                       slides.setHorizontal(0.26)
                               ),
                               claw.intake(),
                               slides.setSlidePositions(500)
                       ),
                       slides.setSlidePositions(0),
                       new SleepAction(1),

                       //Bucket 3--------------------------------Bucket 3
                       new ParallelAction(
                               claw.off(),
                               slides.setSlidePositions(4400),
                               slides.setHorizontal(0),
                               claw.setPivot(0.4),
                               dropOffThree.build()
                       ),
                       new SleepAction(1),
                       claw.eject(1,0.5),
                       claw.setPivot(0.7),

                       //Sample 3--------------------------------Sample 3
                       new ParallelAction(
                               new SequentialAction(
                                       new SleepAction(0.7),
                                       claw.setPivot(0.03)
                                       ),
                               slides.setSlidePositions(500),
                               claw.intake(),
                               sampleThree.build()
                       ),
                       slides.setSlidePositions(0),
                       new SleepAction(1.5),

                       //Bucket 4--------------------------------Bucket 4
                       new ParallelAction(
                               new SequentialAction(
                                       new SleepAction(1),
                                       claw.off()
                               ),
                               slides.setSlidePositions(4400),
                               slides.setHorizontal(0),
                               claw.setPivot(0.4),
                               dropOffFour.build()
                       ),
                       new SleepAction(0.5),
                       claw.eject(1.2,0.3),
                       claw.setPivot(0.7),

                       //Parking--------------------------------Parking
                       new ParallelAction(
                               slides.setSlidePositions(1820),
                               claw.setPivot(0.4),
                               park.build()
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
            public SetSlidePositions(int tar) {target = tar;}

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
            //ElapsedTime timer = new ElapsedTime();
            //double runTime = 0.0;

            public Intake(){
                // runTime = 0.0;
            }
            //public Intake(double time)
//            {
//                runTime = time;
//            }
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                if(runTime > 0.0 )
//                {
//                    timer.reset();
//                    while(timer.seconds() < runTime)
//                    {
//                        leftClaw.setPower(1);
//                        rightClaw.setPower(-1);
//                    }
//                    leftClaw.setPower(0);
//                    rightClaw.setPower(0);
//                }
//                else
//                {
//                    leftClaw.setPower(1);
//                    rightClaw.setPower(-1);
//                }
//                return false;

                leftClaw.setPower(-1);
                rightClaw.setPower(1);
                return false;
            }
        }

        public Action intake() {
            return new Intake();
        }
        // public Action intake(double time){
//            return new Intake(time);
//        }



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
