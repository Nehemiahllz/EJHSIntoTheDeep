package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Buckets", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class Buckets extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Change StartPos
        //Left side of robot, beside the vertical bar, vertical part of the side holder, -18 or jacob way of starting, -42 for two tiles
        Pose2d start = new Pose2d(-6, -50, Math.toRadians(270));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Grab claw = new Grab(hardwareMap);
        GrabY yClaw = new GrabY(hardwareMap);
        GrabX xClaw = new GrabX(hardwareMap);

        //This top line will have the position the robot is currently in, but the bottom is where the robot will go
        //The bottom line can have as many lines as you want, but the last line will have the semi colon, not the others
        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
//                .splineToConstantHeading(new Vector2d(0, -19.5), Math.toRadians(270))
                .strafeTo(new Vector2d(-4, -19));

        TrajectoryActionBuilder sample1 = drive.actionBuilder(new Pose2d(-4, -19, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(-4, -26), Math.toRadians(270))

                .splineToLinearHeading(new Pose2d(-42.8, -29, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder sampleScoreClose = drive.actionBuilder(new Pose2d(-42.8, -29, Math.toRadians(90)))
                //twists the robot and moves it to be just before the bucket
                .splineToLinearHeading(new Pose2d(-45, -38.5, Math.toRadians(45)), Math.toRadians(90))
                .strafeTo(new Vector2d(-51, -45));

        TrajectoryActionBuilder sample2PickUp = drive.actionBuilder(new Pose2d(-51, -45, Math.toRadians(45)))
//                .splineToConstantHeading(new Vector2d(-40, -42), Math.toRadians(45))

                .splineToLinearHeading(new Pose2d(-40, -42, Math.toRadians(90)), Math.toRadians(90))

                .strafeTo(new Vector2d(-53, -28.25));

        TrajectoryActionBuilder sample2ScoreClose = drive.actionBuilder(new Pose2d(-53, -28.25, Math.toRadians(90)))
                //twists the robot and moves it to be just before the bucket
                .strafeTo(new Vector2d(-53, -33))
                .splineToLinearHeading(new Pose2d(-45, -38.5, Math.toRadians(45)), Math.toRadians(90))
                .strafeTo(new Vector2d(-51, -45));

        TrajectoryActionBuilder sample3PickUp = drive.actionBuilder(new Pose2d(-51, -45, Math.toRadians(90)))
                //twists the robot straight and puts it into position to pick sample 3
                .splineToLinearHeading(new Pose2d(-45, -17.5, Math.toRadians(180)), Math.toRadians(90))

                .splineToConstantHeading(new Vector2d(-45, -17.65), Math.toRadians(180));

        TrajectoryActionBuilder sample3ScoreClose = drive.actionBuilder(new Pose2d(-45, -17.65, Math.toRadians(180)))
                //twists the robot and moves it to be just before the bucket
                .strafeTo(new Vector2d(-42, -16))
                .splineToLinearHeading(new Pose2d(-45, -41, Math.toRadians(45)), Math.toRadians(90))
                .strafeTo(new Vector2d(-51, -45));

        //Closes the claw onto the specimen
        claw.setClawPosition(0.32);

        waitForStart();
        if (isStopRequested()) return;

        //The actual running stuff:
        Actions.runBlocking(
                new SequentialAction(

                        new ParallelAction(
                                specimen.build(),
                                claw.setClawPosition(0.32),
                                yClaw.setClawYPosition(0.1911),
                                slide.setSlidePosition(1100)
                        ),
                        slide.setSlidePosition(1850),

                        new ParallelAction(
                                xClaw.setClawXPosition(0.05),
                                claw.setClawPosition(0.73),
                                sample1.build(),
                                yClaw.setClawYPosition(0.81),
                                new SequentialAction(
                                        slide.setSlidePosition(0),
                                        axel.setAxelPosition(260, 1),
                                        axel.setAxelPosition(290, 1),
                                        axel.setAxelPosition(322, 1)
                                )
                        ),
                        //Move the axel down onto the sample and close the claw
                        new ParallelAction(
                            claw.setClawPosition(0.31),
                            axel.setAxelPosition(326, 0)
                                ),
                        new SleepAction(0.25),

                        new ParallelAction(
                                claw.setClawPosition(0.31),
                                xClaw.setClawXPosition(0.7),
                                sampleScoreClose.build(),
                                axel.setAxelPosition(0, 1),
                                slide.setSlidePosition(3035)
                        ),

                        yClaw.setClawYPosition(0.3),
                        new SleepAction(0.5),
                        claw.setClawPosition(0.73),
                        new SleepAction(0.5),
                        yClaw.setClawYPosition(0.81),
                        new SleepAction(0.1),
                        slide.setSlidePosition(0),

                        new ParallelAction(
                                xClaw.setClawXPosition(0.05),
                                claw.setClawPosition(0.73),
                                sample2PickUp.build(),
                                yClaw.setClawYPosition(0.81),
                                new SequentialAction(
                                        slide.setSlidePosition(0),
                                        axel.setAxelPosition(260, 1),
                                        axel.setAxelPosition(290, 1),
                                        axel.setAxelPosition(320, 1)
                                )
                        ),
                        new ParallelAction(
                                claw.setClawPosition(0.31),
                                axel.setAxelPosition(326, 0)
                        ),
                        new SleepAction(0.25),

                        new ParallelAction(
                                claw.setClawPosition(0.31),
                                xClaw.setClawXPosition(0.7),
                                sample2ScoreClose.build(),
                                        axel.setAxelPosition(0, 1),
                                        slide.setSlidePosition(3035)
                        ),
                        yClaw.setClawYPosition(0.3),
                        new SleepAction(0.5),
                        claw.setClawPosition(0.73),
                        new SleepAction(0.5),
                        yClaw.setClawYPosition(0.81),
                        new SleepAction(0.1),
                        slide.setSlidePosition(0),

                        new ParallelAction(
                                xClaw.setClawXPosition(0.40),
                                claw.setClawPosition(0.73),
                                sample3PickUp.build(),
                                yClaw.setClawYPosition(0.81),
                                new SequentialAction(
                                        slide.setSlidePosition(0),
                                        axel.setAxelPosition(260, 1),
                                        axel.setAxelPosition(290, 1),
                                        axel.setAxelPosition(320, 1)
                                )
                        ),
                        new ParallelAction(
                                claw.setClawPosition(0.31),
                                axel.setAxelPosition(326, 0)
                        ),
                        new SleepAction(0.25),

                        new ParallelAction(
                                claw.setClawPosition(0.36),
                                xClaw.setClawXPosition(0.7),
                                sample3ScoreClose.build(),
                                        axel.setAxelPosition(0, 1),
                                        slide.setSlidePosition(3035)
                        ),

                        yClaw.setClawYPosition(0.3),
                        new SleepAction(0.5),
                        claw.setClawPosition(0.73),
                        new SleepAction(0.5),
                        yClaw.setClawYPosition(0.81),
                        new SleepAction(0.1),
                        slide.setSlidePosition(0)

                )
        );

    }


    //Making the slide class to make the slide object to be moved during auto
    public class Slide {
        DcMotorEx slideMotor;

        public Slide(HardwareMap hardwareMap) {

            slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");

            slideMotor.setTargetPosition(0);
            //Reset the slide encoders to make sure it is accurate
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
                telemetry.addData("axelPos", slideMotor.getCurrentPosition());
                telemetry.addData("Target", slideTarget);
                //If the slide is in roughly the correct location then stop the loop so the rest of the code can run, otherwise continue looping
                if (slideTarget == 0 && slideMotor.getCurrentPosition() < 10) {
                    return false;
                } else if (slideMotor.getCurrentPosition() > slideTarget - 25 && slideMotor.getCurrentPosition() < slideTarget + 25) {
                    return false;
                } else {
                    slideMotor.setTargetPosition(slideTarget);
                    telemetry.update();
                    return true;
                }
            }
        }

        public Action setSlidePosition(int slideTar) {
            return new Slide.SetSlidePosition(slideTar);
        }

    }

    //Making the slide class to make the slide object to be moved during auto
    public class Axel {
        DcMotorEx axelMotor;
        DcMotorEx axelMotor2;

        public Axel(HardwareMap hardwareMap) {
            axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
            axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");

            //Reverse the second axelMotor so they work together
            axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);

            axelMotor.setTargetPosition(0);
            axelMotor2.setTargetPosition(0);

            //Reset the slide encoders to make sure it is accurate
            axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            axelMotor.setPower(1);
            axelMotor2.setPower(1);
        }

        public class SetAxelPosition implements Action {
            int target;
            double power;

            public SetAxelPosition(int axelTar, double tarPower) {
                target = axelTar;
                power = tarPower;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                axelMotor.setPower(power);
                axelMotor2.setPower(power);

                if(target == 326){
                    return false;
                }
                //If the axel is in roughly the correct location then stop the loop so the rest of the code can run, otherwise continue looping
                if (axelMotor.getCurrentPosition() > target - 4 && axelMotor.getCurrentPosition() < target + 4) {
                    return false;
                } else {
                    axelMotor.setTargetPosition(target);
                    axelMotor2.setTargetPosition(target);

                    telemetry.update();
                    return true;
                }
            }
        }

        public Action setAxelPosition(int axelTar, double tarPower) {
            return new Axel.SetAxelPosition(axelTar, tarPower);
        }

    }


    //Making the classes for all of the various servos for the claw, just sets the targetPosition to the parameter value inserted and never loops
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
            return new Grab.SetClawPosition(clawPos);
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
            return new GrabY.SetClawYPosition(clawYPos);
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
            return new GrabX.SetClawXPosition(clawXPos);
        }

    }

}