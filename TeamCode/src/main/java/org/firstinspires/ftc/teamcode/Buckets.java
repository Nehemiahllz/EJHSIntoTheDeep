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
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        Slides slides = new Slides(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Servo pivot = hardwareMap.get(Servo.class, "pivot");

        TrajectoryActionBuilder toSpecimenBar = drive.actionBuilder(startPose)
                .waitSeconds(0.5)
                .lineToY(-34)
                .waitSeconds(0.3);

        Pose2d sampleOnePose = new Pose2d(-45,-30,Math.toRadians(120));
        TrajectoryActionBuilder toSample1 = drive.actionBuilder(new Pose2d(-12, -34, Math.toRadians(90)))
                .waitSeconds(0.3)
                .lineToY(-40)
                .splineToLinearHeading(sampleOnePose, Math.toRadians(90));

        /** **************** **/
        Pose2d bucketPose = new Pose2d(-54, -48.5, Math.toRadians(-120));
        TrajectoryActionBuilder toBucket1 = drive.actionBuilder(sampleOnePose)
                .waitSeconds(0.3)
                .lineToX(-40)
                .splineToLinearHeading(bucketPose, Math.toRadians(180));

        double sampleTwoX = -58, sampleTwoY = -39;
        TrajectoryActionBuilder toSample2 = drive.actionBuilder(bucketPose)
                .splineToLinearHeading(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(82)), Math.toRadians(90));


        bucketPose = new Pose2d(-54, -50, Math.toRadians(-118));
        TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(90)))
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));

        //-61.7

//        TrajectoryActionBuilder toSample3 = drive.actionBuilder(bucketPose)
//                .strafeTo(new Vector2d(-47, -48))
//                .splineToLinearHeading(new Pose2d(-48,-30, Math.toRadians(180)), 0)
//                .strafeTo(new Vector2d(sampleThreeX,sampleThreeY));
        double sampleThreeX = -56, sampleThreeY = -25; // -64, -22.5
        TrajectoryActionBuilder toSample3 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-47, -48))
                .splineToLinearHeading(new Pose2d(sampleThreeX,sampleThreeY, Math.toRadians(180)), 0)
                //.splineToLinearHeading(new Pose2d(-48,-22.5, Math.toRadians(180)), 0)
                //.strafeTo(new Vector2d(sampleThreeX,sampleThreeY))
                ;

        bucketPose = new Pose2d(-54, -45, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket3 = drive.actionBuilder(new Pose2d(sampleThreeX, sampleThreeY, Math.toRadians(180)))
                .lineToX(-55)
                .splineToLinearHeading(bucketPose,  Math.toRadians(90));

        TrajectoryActionBuilder park = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-50, -40))
                .splineToLinearHeading(new Pose2d(-25,-5, Math.toRadians(0)), Math.toRadians(270));





        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        //to specimen
                        new ParallelAction(
                                slides.setHorizontal(0),
                                slides.setSlidePositions(2290),
                                claw.setPivot(0.3),
                                toSpecimenBar.build()
                        ),
                        //to sample 1
                        new ParallelAction(
                                toSample1.build(),
                                slides.setSlidePositions(500),
                                claw.setPivot(0),
                                claw.intake()
                        ),
                        slides.setSlidePositions(0),
                        new SleepAction(0.6),
                        //to bucket 1
                        new ParallelAction(
                                claw.off(),
                                slides.setSlidePositions(4400),
                                slides.setHorizontal(0.03),
                                claw.setPivot(0.3),
                                toBucket1.build()
                        ),
                        new SleepAction(0.3),
                        claw.eject(0.5, 0.5),
                        new ParallelAction(
                                claw.setPivot(0.75),
                                slides.setHorizontal(0)
                        ),
                        new SleepAction(0.2),

                        //to sample 2
                        new ParallelAction(
                                toSample2.build(),
                                claw.setPivot(0.03),
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        slides.setSlidePositions(500)
                                ),
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        slides.setHorizontal(0.075)
                                ),
                                claw.intake()

                        ),
                        new SleepAction(0.2),
                        slides.setSlidePositions(0),
                        new SleepAction(1.3),

                        //to bucket 2
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0),
                                toBucket2.build(),
                                claw.setPivot(0.3),
                                slides.setSlidePositions(4400)
                                ),
                        new SleepAction(1),
                        claw.eject(0.5,0.3),
                        new ParallelAction(
                        claw.setPivot(0.7),
                        slides.setHorizontal(0)
                        ),

                        //to sample 3
                        new ParallelAction(
                                slides.setSlidePositions(300),
                                toSample3.build(),
                                claw.intake(),
                                slides.setHorizontal(0.09),
                                claw.setPivot(0)
                        ),
                                //to bucket 3
                        slides.setSlidePositions(0),
                        new SleepAction(1),
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0),
                                toBucket3.build(),
                                claw.setPivot(0.3),
                                slides.setSlidePositions(4400),
                                slides.setHorizontal(0)
                                ),
                        claw.eject(0.5,0.3),
                        new ParallelAction(
                                claw.setPivot(0.75),
                                slides.setHorizontal(0)
                        ),
                        //Parking
                        new ParallelAction(
                                park.build(),
                                slides.setHorizontal(0),
                                new SequentialAction(
                                        new SleepAction(0.3),
                                        slides.setSlidePositions(1780)
                                ),
                                claw.setPivot(0.75),
                                new SequentialAction(
                                        new SleepAction(3),
                                        claw.setPivot(0)
                                )
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
                        leftClaw.setPower(-ejectPower);
                        rightClaw.setPower(ejectPower);
                    }
                    leftClaw.setPower(0);
                    rightClaw.setPower(0);
                }
                else
                {
                    leftClaw.setPower(-ejectPower);
                    rightClaw.setPower(ejectPower);
                }
                return false;
            }
        }

        public Action eject() {
            return new Eject();
        }
        public Action eject(double time){ return new Eject(time);}
        public Action eject(double time, double power){return new Eject(time, power);}


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

