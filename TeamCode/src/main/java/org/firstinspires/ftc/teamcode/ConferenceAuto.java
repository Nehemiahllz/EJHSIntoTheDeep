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


    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d startPose = new Pose2d(-24, -61, Math.toRadians(90));


        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);


        Slides slides = new Slides(hardwareMap);

        Claw claw = new Claw(hardwareMap);


        Pose2d sampleOnePose = new Pose2d(-35,-29,Math.toRadians(120));
        TrajectoryActionBuilder toSample1 = drive.actionBuilder(startPose)
                .waitSeconds(0.3)
                .splineToLinearHeading(sampleOnePose, Math.toRadians(90));

        /** ************************************************************************* **/
        Pose2d bucketPose = new Pose2d(-42, -52, Math.toRadians(-127));
        TrajectoryActionBuilder toBucket1 = drive.actionBuilder(sampleOnePose)
                .waitSeconds(0.3)
                .strafeTo(new Vector2d(-30,-30))
                .splineToLinearHeading(bucketPose, Math.toRadians(180));0

        double sampleTwoX = -47, sampleTwoY = -39;
        TrajectoryActionBuilder toSample2 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-38, -54))
                .splineToLinearHeading(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(82)), Math.toRadians(90));


        bucketPose = new Pose2d(-40, -52, Math.toRadians(-108));
        TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(sampleTwoX, sampleTwoY, Math.toRadians(90)))
                .waitSeconds(1)
                .splineToLinearHeading(bucketPose, Math.toRadians(-90));

        double sampleThreeX = -40, sampleThreeY = -25.8; // -64, -22.5
        TrajectoryActionBuilder toSample3 = drive.actionBuilder(bucketPose)
                .strafeTo(new Vector2d(-36, -30))
                .splineToLinearHeading(new Pose2d(sampleThreeX,sampleThreeY, Math.toRadians(180)), 0)
                ;

        bucketPose = new Pose2d(-40, -50, Math.toRadians(-135));
        TrajectoryActionBuilder toBucket3 = drive.actionBuilder(new Pose2d(sampleThreeX, sampleThreeY, Math.toRadians(180)))
                .lineToX(-40)
                .splineToLinearHeading(bucketPose,  Math.toRadians(90));


        Actions.runBlocking(slides.setHorizontal(0));
        Actions.runBlocking(claw.setPivot(0.2));
        Actions.runBlocking(slides.resetEncoders());
        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                toSample1.build(),
                                slides.setSlidePositions(300),
                                claw.setPivot(0.2),
                                claw.intake()
                        ),
                        slides.setSlidePositions(17, 0.5),
                        new SleepAction(1),
                        new ParallelAction(
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        claw.off()
                                ),
                                slides.setHorizontal(0.14),
                                claw.setPivot(0.04)
                        ),
                        new SleepAction(2),

                        slides.setSlidePositionsWithCamera(),


                        //bucket 1
                        new ParallelAction(
                        toBucket1.build(),
                        slides.setHorizontal(0),
                        claw.setPivot(0.6)
                        ),
                        new SleepAction(0.3),
                        claw.eject(1),
                        new ParallelAction(
                                claw.setPivot(0.8),
                                slides.setHorizontal(0)
                        ),



                        //to sample 2
                        new SleepAction(0.2),
                        new ParallelAction(
                                toSample2.build(),
                                new SequentialAction(
                                        new SleepAction(0.6),
                                        slides.setSlidePositions(700)
                                ),
                                claw.intake(),
                                new SequentialAction(
                                        new SleepAction(0.9),
                                        slides.setHorizontal(0.12)
                                ),
                                claw.setPivot(0.2)
                        ),


                        new SleepAction(0.1),
                        slides.setSlidePositions(17, 0.5),
                        new SleepAction(1),
                        new ParallelAction(
                                claw.off(),
                                slides.setHorizontal(0.2),
                                claw.setPivot(0.05)
                        ),



                        //bucket 2
                        new SleepAction(2),
                        new ParallelAction(
                                slides.setHorizontal(0),
                                slides.setSlidePositionsWithCamera(),
                                claw.setPivot(0.6),
                                toBucket2.build()
                        ),
                        new SleepAction(0.5),
                        claw.eject(0.5),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),




                        //to Sample 3
                        new ParallelAction(
                                toSample3.build(),
                                new SequentialAction(
                                        new SleepAction(0.6),
                                        claw.setPivot(0.2),
                                        slides.setSlidePositions(900)

                                ),
                                new SequentialAction(
                                        new SleepAction(2),
                                        slides.setHorizontal(0.15)
                                )

                        ),
                        new ParallelAction(

                                claw.intake()),
                        new SleepAction(0.1),
                        slides.setSlidePositions(17, 0.2),
                        new SleepAction(1.2),
                        new ParallelAction(
                                new SequentialAction(
                                        new SleepAction(0.5),
                                        claw.off(),
                                        slides.setHorizontal(0.15),
                                        claw.setPivot(0)
                                )

                        ),
                        new SleepAction(2),
                        slides.setSlidePositionsWithCamera(),




                        //bucket 3
                        new SleepAction(1),
                        new ParallelAction(
                                toBucket3.build(),
                                slides.setHorizontal(0),
                                claw.setPivot(0.6)
                        ),
                        claw.eject(1),
                        new ParallelAction(
                                claw.setPivot(1),
                                slides.setHorizontal(0)
                        ),
                        //Parking
                        new ParallelAction(
                                claw.setPivot(0.2),
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

            int[] highest = new int[] {0,0,0};
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                if(camera.blocks().length > 0) {
//                    if (camera.blocks(1).length > 0)
//                        target = 4430;
//
//                    else if (camera.blocks(3).length > 0)
//                        target = 300;
//
//                     else //if (camera.blocks(3).length > 0)
//                        target = 2600;

                     //Red = 1, Blue = 2, yellow = 3




                HuskyLens.Block[] currentRedArray = camera.blocks(1);
                HuskyLens.Block[] currentBlueArray = camera.blocks(2);
                HuskyLens.Block[] currentYellowArray = camera.blocks(3);

                HuskyLens.Block[][] colorArrays = new HuskyLens.Block[][] {currentRedArray, currentBlueArray, currentYellowArray};

                for(int i = 0; i < colorArrays.length; i++) {
                    if(colorArrays[i].length > 0) {
                        for (int j = 0; j < colorArrays[i].length; j++) {
                            if (colorArrays[i][j].height > highest[i])
                                highest[i] = colorArrays[i][j].height;
                        }
                    }
                }

                if(highest[0] > highest[1] && highest[0] >highest[2])
                    target = 4430;
                else if(highest[1] > highest[0] && highest[1] > highest[2])
                    target = 2600;
                else if(highest[2] > highest[1] && highest[2] > highest[0])
                    target = 300;


                if(target == 2600)
                    telemetry.addLine("Color seen blue");
                else if(target == 300)
                    telemetry.addLine("Color seen Yellow");
                else
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
                    leftClaw.setPower(-1);
                    rightClaw.setPower(1);
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

