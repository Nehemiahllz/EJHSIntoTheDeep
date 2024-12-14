package org.firstinspires.ftc.teamcode;

import android.media.audiofx.BassBoost;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;


import java.util.concurrent.TimeUnit;

@Autonomous(name = "Bars", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class Auto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {

        //Setting the starting position of the robot
        Pose2d start = new Pose2d(6,-42,Math.toRadians(270));

        //Making objects out of our classes for our different mechanical components
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Grab claw = new Grab(hardwareMap);
        GrabY yClaw = new GrabY(hardwareMap);
        GrabX xClaw = new GrabX(hardwareMap);

        //Setting all of the trajectories that our robot follows so that it is quicker when running
        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
                .strafeTo(new Vector2d(7, -11));
//                .splineToLinearHeading(new Pose2d(7, -11, Math.toRadians(270)), Math.toRadians(90));

        TrajectoryActionBuilder sample1 = drive.actionBuilder(new Pose2d(7,-11, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(7, -20), Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(40.5, -20, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder dropSample1 = drive.actionBuilder(new Pose2d(40.5,-20, Math.toRadians(90)))
                .splineToConstantHeading(new Vector2d(43, -24), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(35, -29, Math.toRadians(290)), Math.toRadians(90));

        TrajectoryActionBuilder sample2 = drive.actionBuilder(new Pose2d(35,-29, Math.toRadians(290)))
                .splineToConstantHeading(new Vector2d(35, -24), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(50, -21, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder dropSample2 = drive.actionBuilder(new Pose2d(50,-21, Math.toRadians(90)))
                .splineToLinearHeading(new Pose2d(35, -31, Math.toRadians(290)), Math.toRadians(90));

        TrajectoryActionBuilder grabSpecimen1Close = drive.actionBuilder(new Pose2d(35,-31, Math.toRadians(290)))
                .splineToLinearHeading(new Pose2d(34, -30, Math.toRadians(270)), Math.toRadians(90));

        TrajectoryActionBuilder grabSpecimen1 = drive.actionBuilder(new Pose2d(34,-30, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(34, -35.1), Math.toRadians(270));

        TrajectoryActionBuilder scoreSpecimen1 = drive.actionBuilder(new Pose2d(34, -35.1, Math.toRadians(270)))
                .strafeTo(new Vector2d(-4, -11.25));

        TrajectoryActionBuilder grabSpecimen2Close = drive.actionBuilder(new Pose2d(-4,-11.25, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(34, -30), Math.toRadians(270));

        TrajectoryActionBuilder grabSpecimen2 = drive.actionBuilder(new Pose2d(34,-30, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(34, -35.1), Math.toRadians(270));

        TrajectoryActionBuilder scoreSpecimen2 = drive.actionBuilder(new Pose2d(34, -35.1, Math.toRadians(270)))
                .strafeTo(new Vector2d(-7, -11.25));

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(-7,-11.25, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(30, -37), Math.toRadians(270));

        //Setting the claw position to hold the sample as necessary during init
        claw.setClawPosition(0.32);
        yClaw.setClawYPosition(0.8644);
        xClaw.setClawXPosition(0.3683);

        //If we press stop, the auto will actually stop running
        waitForStart();
        if(isStopRequested()) return;

        //All of the action that happens during the auto, in a sequence
        Actions.runBlocking(new SequentialAction(
                        //Running the robot to the bar, setting the claw orientation, and moving the slide to clip the specimen
                                new ParallelAction(
                                    specimen.build(),
                                    claw.setClawPosition(0.32),
                                    yClaw.setClawYPosition(0.1911),
                                        slide.setSlidePosition(950)
                                ),
                                new ParallelAction(
                                axel.setAxelPosition(0, 1),
                                slide.setSlidePosition(1850)
                                ),

                                new ParallelAction(
                                        xClaw.setClawXPosition(0.05),
                                        claw.setClawPosition(0.73),
                                        sample1.build(),
                                        yClaw.setClawYPosition(0.81),
                                        new SequentialAction(
                                                slide.setSlidePosition(0),
                                                axel.setAxelPosition(260, 1),
                                                axel.setAxelPosition(290, 1),
                                                axel.setAxelPosition(324, 1)
                                        )
                                ),
                                //Move the axel down onto the sample and close the claw
                                new ParallelAction(
                                    claw.setClawPosition(0.31),
                                        axel.setAxelPosition(326, 0)
                                        ),
                                new SleepAction(0.2),
                                //Move the axel up for driving and move the robot to deposit the sample
                                new ParallelAction(
                                axel.setAxelPosition(324, 1),
                                dropSample1.build()
                                ),
                                //Release the sample when in deposit location
                                claw.setClawPosition(0.73),

                                new ParallelAction(
                                    sample2.build(),
                                    axel.setAxelPosition(324, 1)
                                ),
                                new ParallelAction(
                                    claw.setClawPosition(0.31),
                                        axel.setAxelPosition(326, 0)
                                        ),

                                new SleepAction(0.2),

                                new ParallelAction(
                                    dropSample2.build(),
                                        axel.setAxelPosition(324, 1)
                                        ),
                                claw.setClawPosition(0.73),

                                new ParallelAction(
                                        axel.setAxelPosition(233, 1),
                                        xClaw.setClawXPosition(0.05),
                                        yClaw.setClawYPosition(0.556),
                                        grabSpecimen1Close.build()
                                ),
                                grabSpecimen1.build(),
                                claw.setClawPosition(0.31),

                                new SleepAction(0.2),

                                new ParallelAction(
                                        scoreSpecimen1.build(),
                                        yClaw.setClawYPosition(0.1911),
                                        new SequentialAction(
                                            axel.setAxelPosition(0, 1),
                                                slide.setSlidePosition(950)
                                                )
                                ),
                                slide.setSlidePosition(1850),
                                claw.setClawPosition(0.73),

                                new ParallelAction(
                                        grabSpecimen2Close.build(),
                                        xClaw.setClawXPosition(0.05),
                                        yClaw.setClawYPosition(0.556),
                                        new SequentialAction(
                                                slide.setSlidePosition(0),
                                                axel.setAxelPosition(233, 1)
                                        )
                                ),
                                grabSpecimen2.build(),
                                claw.setClawPosition(0.31),

                                new SleepAction(0.2),

                                new ParallelAction(
                                        scoreSpecimen2.build(),
                                        yClaw.setClawYPosition(0.1911),
                                        new SequentialAction(
                                                axel.setAxelPosition(0, 1),
                                                slide.setSlidePosition(950)
                                        )
                                ),
                                slide.setSlidePosition(1850),
                                claw.setClawPosition(0.73),

                                new ParallelAction(
                                        slide.setSlidePosition(0),
                                        park.build()
                                )
                ));
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
                if(slideTarget == 0 && slideMotor.getCurrentPosition() < 10){
                    return false;
                } else if(slideMotor.getCurrentPosition() > slideTarget - 25 && slideMotor.getCurrentPosition() < slideTarget + 25){
                    return false;
                }else{
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

                if(target < 0){
                    if(axelMotor.getCurrentPosition() < 2){
                        return false;
                    }
                }
                //If the axel is in roughly the correct location then stop the loop so the rest of the code can run, otherwise continue looping
                if(axelMotor.getCurrentPosition() > target -4 && axelMotor.getCurrentPosition() < target + 4 && target > -1){
                    return false;
                }else{
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
            return new Auto.Grab.SetClawPosition(clawPos);
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
            return new Auto.GrabY.SetClawYPosition(clawYPos);
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
                return new Auto.GrabX.SetClawXPosition(clawXPos);
            }

    }

}