package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Other_RoadRunner_Classes.MecanumDrive;


@Config
@Autonomous(name = "Conference Auto", group = "Autonomous")
public class ConferenceAuto extends LinearOpMode {

    public int readCamera()
    {
        HuskyLens camera = hardwareMap.get(HuskyLens.class, "camera");
        if (camera.blocks(1).length > 0)
            return 2200;
        else if (camera.blocks(2).length > 0)
            return 300;
        else if (camera.blocks(3).length > 0)
            return 3900;
        else return 100;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(-12, -61, Math.toRadians(90));


        //MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0, Math.toRadians(0)));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);


        Slides slides = new Slides(hardwareMap);

        Claw claw = new Claw(hardwareMap);


        TrajectoryActionBuilder firstPickup = drive.actionBuilder(new Pose2d(0,0, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(0, 15, Math.toRadians(90)), Math.toRadians(90));


        TrajectoryActionBuilder toSpecimenBar = drive.actionBuilder(startPose)
                .waitSeconds(0.3)
                .lineToY(-32)
                .waitSeconds(0.5);

        Pose2d sampleOnePose = new Pose2d(-43,-30,Math.toRadians(120));
        TrajectoryActionBuilder toSample1 = drive.actionBuilder(startPose)
                .lineToY(-50)
                .splineToLinearHeading(sampleOnePose, Math.toRadians(90));

        /** **************** **/
        Pose2d bucketPose = new Pose2d(-51.5, -51.5, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket1 = drive.actionBuilder(sampleOnePose)
                .waitSeconds(0.3)
                .lineToX(-40)
                .splineToLinearHeading(bucketPose, Math.toRadians(180));

        double sampleTwoX = -57, sampleTwoY = -36;
        TrajectoryActionBuilder toSample2 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-47, -45))
                .splineToLinearHeading(new Pose2d(-50,-40, Math.toRadians(90)), -90)
                .strafeTo(new Vector2d(sampleTwoX,sampleTwoY));

        bucketPose = new Pose2d(-49, -44, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(90)))
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));

        double sampleThreeX = -56, sampleThreeY = -23;
        TrajectoryActionBuilder toSample3 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-47, -45))
                .splineToLinearHeading(new Pose2d(-48,-30, Math.toRadians(180)), 0)
                .strafeTo(new Vector2d(sampleThreeX,sampleThreeY));
        bucketPose = new Pose2d(-51.5, -51.5, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket3 = drive.actionBuilder(new Pose2d(sampleThreeX, sampleThreeY, Math.toRadians(180)))
                .lineToX(-55)
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));


        Actions.runBlocking(slides.setHorizontal(0));
        Actions.runBlocking(claw.setPivot(0.3));
        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        //.3 is down
        //.65 is out

        Actions.runBlocking(
                new SequentialAction(
//                        new ParallelAction(
//                        firstPickup.build(),
//                        slides.setSlidePositions(300)
//                                ),
//                        new SleepAction(1),
//                        new ParallelAction(
//                        claw.intake(),
//                        slides.setSlidePositions(0)
//                        ),
//                        new SleepAction(2),
//                        claw.off(),
//                        slides.setSlidePositions(100)

                        //Specimen
//                        new ParallelAction(
//                                slides.setHorizontal(0),
//                                slides.setSlidePositions(2300),
//                                claw.setPivot(0.6),
//                                toSpecimenBar.build()
//                        ),
                        new ParallelAction(
                                toSample1.build(),
                                slides.setSlidePositions(300),
                                claw.setPivot(0.3),
                                claw.intake()
                        ),
                        slides.setSlidePositions(17, 0.5),
                        new SleepAction(1),
                        new ParallelAction(
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        claw.off()
                                ),
                                slides.setHorizontal(0.2),
                                claw.setPivot(0.05)
                        ),
                        new SleepAction(2),

                        slides.setSlidePositionsWithCamera(),
                        new ParallelAction(
                        toBucket1.build(),
                        slides.setHorizontal(0),
                        claw.setPivot(0.65)
                        ),
                        new SleepAction(0.3),
                        claw.eject(1),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),
                        new SleepAction(0.2),
                        new ParallelAction(
                                toSample2.build(),
                                slides.setSlidePositions(700),
                                claw.intake(),
                                new SequentialAction(
                                        new SleepAction(0.3),
                                        slides.setHorizontal(0.2)
                                ),
                                claw.setPivot(0.3)
                        ),

                        //To sample 2
                        new SleepAction(0.1),
                        slides.setSlidePositions(17, 0.5),
                        new SleepAction(1),
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0.2),
                                claw.setPivot(0.05)
                        ),
                        new SleepAction(2),
                        new ParallelAction(
                                slides.setSlidePositionsWithCamera(),
                                claw.setPivot(0.65),
                                toBucket2.build()
                        ),
                        new SleepAction(0.5),
                        claw.eject(0.5),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),
                        new ParallelAction(
                                toSample3.build(),
                                slides.setSlidePositions(700),
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        slides.setHorizontal(0.2)
                                ),
                                claw.setPivot(0.3)
                        ),
                        //to Sample 3
                        new ParallelAction(

                                claw.intake()),
                        new SleepAction(0.1),
                        slides.setSlidePositions(17, 0.2),
                        new SleepAction(1.2),
                        new ParallelAction(
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        claw.off()
                                ),
                                slides.setHorizontal(0.2),
                                claw.setPivot(0.05)
                        ),
                        new SleepAction(2),
                        slides.setSlidePositionsWithCamera(),
                        new SleepAction(1),
                        new ParallelAction(
                                toBucket3.build(),
                                slides.setHorizontal(0),
                                claw.setPivot(0.65)
                        ),
                        claw.eject(1),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),
                        //Parking
                        new ParallelAction(
                                claw.setPivot(0.3),
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
        HuskyLens camera;

        public Slides(HardwareMap hardwareMap) {
            leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
            rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");
            horizontal = hardwareMap.get(Servo.class, "horizontal");

            camera = hardwareMap.get(HuskyLens.class, "camera");
            camera.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

            leftSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            leftSlide.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            rightSlide.setDirection(DcMotor.Direction.REVERSE);
            horizontal.setDirection(Servo.Direction.REVERSE);

        }


        public class SetSlidePositionsWithCamera implements Action
        {
            double pow;
            public SetSlidePositionsWithCamera(double power)
            {
                pow = power;
            }

            public SetSlidePositionsWithCamera(){
                pow = 1;
            };

            int target = leftSlide.getCurrentPosition();


            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                if(camera.blocks().length > 0) {
                    if (camera.blocks(1).length > 0)
                        target = 4350;

                    else if (camera.blocks(3).length > 0)
                        target = 300;

                     else //if (camera.blocks(3).length > 0)
                        target = 2500;
            //}
//                if(camera.blocks().length > 0) {
//                    if (camera.blocks(1).length > camera.blocks(2).length && camera.blocks(1).length > camera.blocks(3).length)
//                        target = 2200;
//                    if (camera.blocks(2).length > camera.blocks(1).length && camera.blocks(1).length > camera.blocks(3).length)
//                        target = 300;
//                    if (camera.blocks(3).length > camera.blocks(2).length && camera.blocks(1).length > camera.blocks(1).length)
//                        target = 3900;
//                }
//                else return true;


                if(target == 2500)
                    telemetry.addLine("Color seen blue");
                else if(target == 300)
                    telemetry.addLine("Color seen Yellow");
                else if(target == 4200)
                    telemetry.addLine("Color seen red");

                telemetry.update();



                leftSlide.setTargetPosition(target);
                rightSlide.setTargetPosition(target);

                leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftSlide.setPower(pow);
                rightSlide.setPower(pow);
                return false;

            }
        }

        public Action setSlidePositionsWithCamera()
        {
            return new SetSlidePositionsWithCamera();
        }
        public Action setSlidePositionsWithCamera(double power){return new SetSlidePositionsWithCamera(power);}


        public class SetSlidePositions implements Action {

            int target;
            double pow;

            public SetSlidePositions(int tar) {
                target = tar;
            }

            public SetSlidePositions(int tar, double power){ target = tar; pow = power;}


            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        public Action setSlidePositions(int tar, double power) { return new SetSlidePositions(tar, power);}
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

