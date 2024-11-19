package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Vector;

@Autonomous(name = "Buckets", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class Buckets extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Change StartPos
        //Left side of robot, beside the vertical bar, vertical part of the side holder, -18 or jacob way of starting, -42 for two tiles
        Pose2d start = new Pose2d(-6, -50, Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Grab claw = new Grab(hardwareMap);
        GrabY yClaw = new GrabY(hardwareMap);
        GrabX xClaw = new GrabX(hardwareMap);

        //This top line will have the position the robot is currently in, but the bottom is where the robot will go
        //The bottom line can have as many lines as you want, but the last line will have the semi colon, not the others
        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
                .splineToConstantHeading(new Vector2d(-4, -21), Math.toRadians(90));

        //This above is the first one, but most will look like the one bellow
        //The first line previous position must be Pose2d even though you prolly made it with a vector
        //The vector2d is the pose2d without any turning and you will just put the degrees your at
        //For the pose2d you will do the same as vector but the third parameter is the the position you want to end up in
        //For the pose2d fourth parameter, KEEP THE NUMBER AT 90, THIS IS THE TANGENT AND WE DONT KNOW WHAT IT DOES

        //EX based off right after the specimen sequence

        //TrajectoryActionBuilder specimen2PickUp = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
        //        .splineToLinearHeading(new Pose2d(-40, -20, Math.toRadians(270)), Math.toRadians(90))
        //        .splineToConstantHeading(new Vector2d(-40, 40), Math.toRadians(270));

        //First it is taking the position of the specimen movement
        //The next line moves the robot to -40, -20 and rotates it 180 degrees to be 270 degrees
        //The last line keeps the rotation the same but moves the robot forward to -40,40


        //VERY EXPIREMENTAL GUESTIMATION AUTO PATHS:
        //FIXING THE POSITIONS IN HERE SHOULD MAKE EVERYTHING WORK UNLESS IT DECIDES TO STOP IN THE MIDDLE AS IT DOES

        TrajectoryActionBuilder sample1PickUp = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
                //moves the robot away from the sub
                .splineToConstantHeading(new Vector2d(-4, -35), Math.toRadians(90))
                //puts the robot in picking position
                .splineToConstantHeading(new Vector2d(-44, -30), Math.toRadians(90));

        TrajectoryActionBuilder sampleScoreClose = drive.actionBuilder(new Pose2d(-44, -29, Math.toRadians(90)))
                //twists the robot and moves it to be just before the bucket
                .splineToLinearHeading(new Pose2d(-44, -36, Math.toRadians(45)), Math.toRadians(90));

                //moves it closer to the bucket to simulate driving behavior of leaning the side against the bucket for scoring
        TrajectoryActionBuilder sampleScore = drive.actionBuilder(new Pose2d(-47, -39, Math.toRadians(45)))
                .splineToConstantHeading(new Vector2d(-49, -42), Math.toRadians(45));

        TrajectoryActionBuilder sample2PickUp = drive.actionBuilder(new Pose2d(-33, -36, Math.toRadians(45)))
                .splineToConstantHeading(new Vector2d(-40, -42), Math.toRadians(45))
                //twists the robot straight and puts it into position to pick sample 2
                .splineToLinearHeading(new Pose2d(-52, -30, Math.toRadians(90)), Math.toRadians(90));


        TrajectoryActionBuilder sample3PickUp = drive.actionBuilder(new Pose2d(-33, -36, Math.toRadians(45)))
                //twists the robot straight and puts it into position to pick sample 3
                .splineToLinearHeading(new Pose2d(-41, -14, Math.toRadians(180)), Math.toRadians(90));

        TrajectoryActionBuilder sample3Post = drive.actionBuilder(new Pose2d(-41, -13, Math.toRadians(180)))
                .splineToConstantHeading(new Vector2d(-40, -30), Math.toRadians(45));

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(315)))
                //parks
                .splineToLinearHeading(new Pose2d(40, -36, Math.toRadians(360)), Math.toRadians(90));


        //Just closes the claw onto the specimen
        claw.setClawPosition(0.32);

        waitForStart();
        if (isStopRequested()) return;

        //The actual running stuff:
        Actions.runBlocking(
                new SequentialAction(
                        //Also closing the claw
                        new ParallelAction(
                        claw.setClawPosition(0.33),
                        yClaw.setClawYPosition(0.88),
                        xClaw.setClawXPosition(0.3683)
                        ),

                        //scores the first specimen
                        new ParallelAction(
                                specimen.build(),
                                slide.setSlidePosition(1580)
                        ),
                        axel.setAxelPosition(80),
                        slide.setSlidePosition(600),

                        new ParallelAction(
                                axel.setAxelPosition(0),
                        claw.setClawPosition(0.6),
                        yClaw.setClawYPosition(0.8644),
                        xClaw.setClawXPosition(0.3683),
                                sample1PickUp.build()
                        ),

                        //Sequence to pick up the first sample
                            axel.setAxelPosition(330),
                            new SleepAction(0.1),
                            claw.setClawPosition(0.33),
                            new SleepAction(0.1),
                            axel.setAxelPosition(0),


                        //sequence to score the first sample
                        new ParallelAction(
                            sampleScoreClose.build(),
                                slide.setSlidePosition(3020),
                                xClaw.setClawXPosition(1)
                        ),
                        sampleScore.build(),
                        new ParallelAction(
                            slide.setSlidePosition(3020),
                            new SequentialAction(
                                 yClaw.setClawYPosition(0.5),
                                    new SleepAction(0.3),
                                    claw.setClawPosition(0.6),
                                    yClaw.setClawYPosition(0.3),
                                    new SleepAction(0.2),
                                    yClaw.setClawYPosition(0.8644)
                            )
                        ),

                        //sequence to pick up the second sample and finish reset from scoring
                        new ParallelAction(
                            xClaw.setClawXPosition(0.3683),
                            slide.setSlidePosition(600),
                            sample2PickUp.build()
                        ),
                        axel.setAxelPosition(330),
                            new SleepAction(0.1),
                            claw.setClawPosition(0.33),
                            new SleepAction(0.1),
                                axel.setAxelPosition(0),

                        //sequence to score second sample
                        new ParallelAction(
                                sampleScoreClose.build(),
                                slide.setSlidePosition(3020),
                                xClaw.setClawXPosition(1)
                        ),
                        sampleScore.build(),
                        new ParallelAction(
                                slide.setSlidePosition(3020),
                                new SequentialAction(
                                        yClaw.setClawYPosition(0.5),
                                        new SleepAction(0.3),
                                        claw.setClawPosition(0.6),
                                        yClaw.setClawYPosition(0.3),
                                        new SleepAction(0.2),
                                        yClaw.setClawYPosition(0.8644)
                                )
                        ),

                        //grab third sample
                        new ParallelAction(
                                xClaw.setClawXPosition(0),
                                slide.setSlidePosition(600),
                                sample3PickUp.build()
                        ),
                        axel.setAxelPosition(330),
                        new SleepAction(0.1),
                        claw.setClawPosition(0.33),
                        new SleepAction(0.1),
                        axel.setAxelPosition(0),
                        sample3Post.build(),

                        //sequence to score third sample
                        new ParallelAction(
                                sampleScoreClose.build(),
                                slide.setSlidePosition(3020),
                                xClaw.setClawXPosition(1)
                        ),
                        sampleScore.build(),
                        new ParallelAction(
                                slide.setSlidePosition(3020),
                                new SequentialAction(
                                        yClaw.setClawYPosition(0.5),
                                        new SleepAction(0.3),
                                        claw.setClawPosition(0.6),
                                        yClaw.setClawYPosition(0.3),
                                        new SleepAction(0.2),
                                        yClaw.setClawYPosition(0.8644)
                                )
                        ),

                        slide.setSlidePosition(0),
                        axel.setAxelPosition(0)
                )
        );

    }


    public class Slide {
        DcMotorEx slideMotor;

        public Slide(HardwareMap hardwareMap) {

            slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");

            slideMotor.setTargetPosition(0);

            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            slideMotor.setPower(1);
        }

        public class SetSlidePosition implements Action {
            int slideTarget;

            public SetSlidePosition(int slideTar) {
                slideTarget = slideTar;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                slideMotor.setTargetPosition(slideTarget);
                if(slideMotor.getCurrentPosition() > slideTarget -5 && slideMotor.getCurrentPosition() < slideTarget + 5){
                    return false;
                }else{
                    return true;
                }
            }
        }

        public Action setSlidePosition(int slideTar) {
            return new SetSlidePosition(slideTar);
        }

    }


    public class Axel {
        DcMotorEx axelMotor;
        DcMotorEx axelMotor2;

        public Axel(HardwareMap hardwareMap) {
            axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
            axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");
            axelMotor2.setDirection(DcMotor.Direction.REVERSE);

            axelMotor.setTargetPosition(0);
            axelMotor2.setTargetPosition(0);

            axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            axelMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            axelMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            axelMotor.setPower(0.15);
            axelMotor2.setPower(0.15);
        }

        public class SetAxelPosition implements Action {
            int axelTarget;

            public SetAxelPosition(int axelTar) {
                axelTarget = axelTar;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                axelMotor.setTargetPosition(axelTarget);
                axelMotor2.setTargetPosition(axelTarget);
                if(axelMotor.getCurrentPosition() > axelTarget -5 && axelMotor.getCurrentPosition() < axelTarget + 5){
                    return false;
                }else{
                    return true;
                }

            }
        }

        public Action setAxelPosition(int axelTar) {
            return new SetAxelPosition(axelTar);
        }

    }


    public class Grab {
        Servo claw;

        public Grab(HardwareMap hardwareMap) {
            claw = hardwareMap.get(Servo.class, "claw");
            claw.setDirection(Servo.Direction.FORWARD);
        }

        public class SetClawPosition implements Action {
            double clawPosition;

            public SetClawPosition(double clawPos) {
                clawPosition = clawPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                claw.setPosition(clawPosition);
                return false;
            }

        }

        public Action setClawPosition(double clawPos) {
            return new SetClawPosition(clawPos);
        }
    }

    public class GrabY {
        Servo yClaw;

        public GrabY(HardwareMap hardwareMap) {
            yClaw = hardwareMap.get(Servo.class, "yClaw");
            yClaw.setDirection(Servo.Direction.FORWARD);
        }

        public class SetClawYPosition implements Action {
            double clawYPosition;

            public SetClawYPosition(double clawYPos) {
                clawYPosition = clawYPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                yClaw.setPosition(clawYPosition);
                return false;
            }

        }

        public Action setClawYPosition(double clawYPos) {
            return new SetClawYPosition(clawYPos);
        }
    }

    public class GrabX {
        Servo xClaw;

        public GrabX(HardwareMap hardwareMap) {
            xClaw = hardwareMap.get(Servo.class, "xClaw");
            xClaw.setDirection(Servo.Direction.REVERSE);
        }

        public class SetClawXPosition implements Action {
            double clawXPosition;

            public SetClawXPosition(double clawXPos) {
                clawXPosition = clawXPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                xClaw.setPosition(clawXPosition);
                return false;
            }

        }

        public Action setClawXPosition(double clawXPos) {
            return new SetClawXPosition(clawXPos);
        }

    }

}