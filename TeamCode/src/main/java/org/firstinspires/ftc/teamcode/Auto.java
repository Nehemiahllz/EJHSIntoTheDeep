package org.firstinspires.ftc.teamcode;

import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.media.audiofx.BassBoost;

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
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
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

import org.opencv.core.Mat;

@Autonomous(name = "Bars", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class Auto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Change StartPos
        //Left side of robot, beside the vertical bar, vertical part of the side holder
        Pose2d start = new Pose2d(3, -61, Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        Sweeper sweeper = new Sweeper(hardwareMap);

        //This top line will have the position the robot is currently in, but the bottom is where the robot will go
        //The bottom line can have as many lines as you want, but the last line will have the semi colon, not the others



        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
                .strafeTo(new Vector2d(0,-28));

        TrajectoryActionBuilder sample1Drop = drive.actionBuilder(new Pose2d(0, -28, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, -40))
                .strafeToLinearHeading(new Vector2d(15, -35), Math.toRadians(45));

        TrajectoryActionBuilder sample1 = drive.actionBuilder(new Pose2d(15, -35, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(28, -37), Math.toRadians(45));

        TrajectoryActionBuilder sample1Slide = drive.actionBuilder(new Pose2d(28, -37, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(27, -40), Math.toRadians(315));


        TrajectoryActionBuilder sample2 = drive.actionBuilder(new Pose2d(27, -40, Math.toRadians(315)))
                .strafeToLinearHeading(new Vector2d(40, -39), Math.toRadians(45));

        TrajectoryActionBuilder sample2Slide = drive.actionBuilder(new Pose2d(40, -39, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(37, -37), Math.toRadians(305));


        TrajectoryActionBuilder sample3 = drive.actionBuilder(new Pose2d(37, -37, Math.toRadians(305)))
                .strafeToLinearHeading(new Vector2d(44, -30), Math.toRadians(20))
                .strafeTo(new Vector2d(47, -30));

        TrajectoryActionBuilder sample3Slide = drive.actionBuilder(new Pose2d(47, -30, Math.toRadians(20)))
                .strafeToLinearHeading(new Vector2d(36, -43), Math.toRadians(270));

        TrajectoryActionBuilder specimen2 = drive.actionBuilder(new Pose2d(36, -43, Math.toRadians(270)))
                .splineToLinearHeading(new Pose2d(-5, -28, Math.toRadians(270)), Math.toRadians(90));



        //Closes the claw onto the specimen
        claw.setClawPosition(0);

        waitForStart();
        if (isStopRequested()) return;

        //The actual running stuff:
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                axel.setAxelPosition(),
                                new SequentialAction(
                                        claw.setClawPosition(0),
                                        new ParallelAction(
                                                claw.setClawYPosition(0.21),
                                                specimen.build(),
                                                axel.changeAxelPosition(290, 0.4),
                                                slide.setSlidePosition(380, 1)
                                        ),
                                        axel.changeAxelPosition(690, 1),
                                        claw.setClawYPosition(0),
                                        claw.setClawPosition(0.8),
                                        new SleepAction(0.1),

                                        new ParallelAction(
                                                sample1Drop.build(),
                                                slide.setSlidePosition(0, 1)
                                        ),
                                        new ParallelAction(
                                                sample1.build(),
                                                claw.setClawYPosition(0.507),
                                                claw.setClawPosition(0.8),
                                                claw.setClawXPosition(0.443),
                                                new SequentialAction(
                                                        axel.changeAxelPosition(950, 0.4),
                                                        slide.setSlidePosition(360, 1),
                                                        axel.changeAxelPosition(0,0)
                                                )
                                        ),
                                        claw.setClawPosition(0),
                                        new SleepAction(0.2),
                                        new ParallelAction(
                                                sample1Slide.build(),
                                                slide.setSlidePosition(500, 1)
                                        ),
                                        claw.setClawPosition(0.8),
                                        new SleepAction(0.3),

                                        new ParallelAction(
                                            sample2.build(),
                                                slide.setSlidePosition(200, 1)
                                                ),
                                        claw.setClawPosition(0),
                                        new SleepAction(0.2),
                                        sample2Slide.build(),
                                        slide.setSlidePosition(800, 1),
                                        claw.setClawPosition(0.8),
                                        new SleepAction(0.3),



                                        new ParallelAction(
                                                slide.setSlidePosition(100, 1),
                                                claw.setClawXPosition(0.411),
                                                sample3.build()
                                        ),
                                        claw.setClawPosition(0),
                                        new SleepAction(0.2),
                                        new ParallelAction(
                                                slide.setSlidePosition(0, 1),
                                                sample3Slide.build()
                                        ),
                                        slide.setSlidePosition(400, 1),
                                        claw.setClawPosition(0.8),
                                        new SleepAction(0.2),


                                        new ParallelAction(
                                        axel.changeAxelPosition(855, 1),
                                                claw.setClawYPosition(0.235),
                                                claw.setClawXPosition(0.611)
                                                ),
                                        slide.setSlidePosition(525, 0.6),
                                        claw.setClawPosition(0),
                                        new SleepAction(0.3),

                                        new ParallelAction(
                                                specimen2.build(),
                                                new SequentialAction(
                                                        axel.changeAxelPosition(64, 1),
                                                        slide.setSlidePosition(550, 1)
                                                ),
                                                claw.setClawYPosition(0.233)
                                        ),
                                        axel.changeAxelPosition(280, 1),
                                        claw.setClawPosition(0.8),
                                        new ParallelAction(

                                                new SequentialAction(
                                                slide.setSlidePosition(0, 1),
                                                axel.changeAxelPosition(805, 1)
                                                ),
                                                claw.setClawYPosition(0.254)
                                        )








                                ))));



    }


    //Making the slide class to make the slide object to be moved during auto
    //Making the slide class to make the slide object to be moved during auto
    public class Slide {
        DcMotorEx slideMotor;
        DcMotorEx slideMotor2;

        public Slide(HardwareMap hardwareMap) {

            slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
            slideMotor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");

            slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
            slideMotor2.setDirection(DcMotorEx.Direction.REVERSE);

            slideMotor.setTargetPosition(0);
            slideMotor2.setTargetPosition(0);
            //Reset the slide encoders to make sure it is accurate
            slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            slideMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            slideMotor.setPower(1);
            slideMotor2.setPower(1);
        }

        public class SetSlidePosition implements Action {
            int slideTarget;
            double pow;

            public SetSlidePosition(int slideTar, double power) {
                slideTarget = slideTar;
                pow = power;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                //If the slide is in roughly the correct location then stop the loop so the rest of the code can run, otherwise continue looping
                if (slideTarget == 0 && slideMotor.getCurrentPosition() < 10 && slideMotor2.getCurrentPosition() < 10) {
                    return false;
                } else if (slideMotor.getCurrentPosition() > slideTarget - 6 && slideMotor.getCurrentPosition() < slideTarget + 6) {
                    return false;
                } else {
                    slideMotor.setTargetPosition(slideTarget);
                    slideMotor2.setTargetPosition(slideTarget);

                    slideMotor.setPower(pow);
                    slideMotor2.setPower(pow);

                    telemetry.update();
                    return true;
                }

            }
        }

        public Action setSlidePosition(int slideTar, double power) {
            return new SetSlidePosition(slideTar, power);
        }

    }

    int target = 0;

    //Making the axel class to make the slide object to be moved during auto
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

            public SetAxelPosition() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("axelTargetPos", axelMotor.getTargetPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                axelMotor.setTargetPosition(target);
                axelMotor2.setTargetPosition(target);

                if (target == 10000000) {
                    return false;
                }

                telemetry.update();
                return true;
            }
        }

        public Action setAxelPosition() {
            return new SetAxelPosition();
        }


        public class ChangeAxelPosition implements Action {
            int tar;
            double pow;

            public ChangeAxelPosition(int axelTar, double power) {
                tar = axelTar;
                pow = power;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                target = tar;

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("axelTargetPos", axelMotor.getTargetPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                axelMotor.setPower(pow);
                axelMotor2.setPower(pow);

                if (axelMotor.getCurrentPosition() > target - 3 && axelMotor.getCurrentPosition() < target + 3) {
                    return false;
                } else if (axelMotor.getPower() == 0) {
                    return false;
                } else if (target == 950 && axelMotor.getCurrentPosition() > 945) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public Action changeAxelPosition(int axelTar, double power) {
            return new ChangeAxelPosition(axelTar, power);
        }


        public class ChangeAxelRotate implements Action {
            boolean lock;

            public ChangeAxelRotate(boolean locks) {
                lock = locks;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("axelTargetPos", axelMotor.getTargetPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                if (lock) {
                    axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);
                } else {
                    axelMotor2.setDirection(DcMotorEx.Direction.FORWARD);
                }

                return false;
            }

            public Action changeAxelRotate(boolean lock) {
                return new ChangeAxelRotate(lock);
            }
        }
    }


    public class Claw {
        Servo claw;
        Servo yClaw;
        Servo xClaw;

        public Claw(HardwareMap hardwareMap) {
            claw = hardwareMap.get(Servo.class, "claw");
            claw.setDirection(Servo.Direction.FORWARD);

            yClaw = hardwareMap.get(Servo.class, "yClaw");
            yClaw.setDirection(Servo.Direction.FORWARD);

            xClaw = hardwareMap.get(Servo.class, "xClaw");
            xClaw.setDirection(Servo.Direction.REVERSE);
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





    public class Sweeper {
        Servo sweep;

        public Sweeper(HardwareMap hardwareMap) {
            sweep = hardwareMap.get(Servo.class, "sweep");
            sweep.setDirection(Servo.Direction.FORWARD);
        }

        public class SetSweepPosition implements Action {
            double sweeperPos;

            public SetSweepPosition(double sweepPos) {
                sweeperPos = sweepPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                sweep.setPosition(sweeperPos);
                return false;
            }

        }

        public Action setSweepPosition(double sweepPos) {
            return new SetSweepPosition(sweepPos);
        }
    }

}