package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
        Pose2d start = new Pose2d(6,-50,Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Grabber grabber = new Grabber(hardwareMap);

        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
                .splineToConstantHeading(new Vector2d(-6,-21), Math.toRadians(90));

        TrajectoryActionBuilder sample2 = drive.actionBuilder(new Pose2d(-6,-21, Math.toRadians(90)))
                .splineToConstantHeading(new Vector2d(29, -34), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(29,-34), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(29,-4), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(40,-4), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(40,-32), Math.toRadians(90));

        TrajectoryActionBuilder sample3 = drive.actionBuilder(new Pose2d(40,-35, Math.toRadians(90)))
                .splineToConstantHeading(new Vector2d(40, -4), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(48,-4), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(48,-33), Math.toRadians(90));

        TrajectoryActionBuilder specimen2PickUp = drive.actionBuilder(new Pose2d(48, -35, Math.toRadians(90)))
                        .splineToLinearHeading(new Pose2d(48, -15, Math.toRadians(270)), Math.toRadians(90))
                                .splineToConstantHeading(new Vector2d(46, -28), Math.toRadians(270));

        TrajectoryActionBuilder postSpecimen2PickUp = drive.actionBuilder(new Pose2d(46, -28, Math.toRadians(270)))
                .splineToLinearHeading(new Pose2d(5, -27, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
                .splineToConstantHeading(new Vector2d(40, -32), Math.toRadians(90));






        grabber.setClawPosition(0.32, 0.8644, 0.3683);

        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        grabber.setClawPosition(0.32, 0.8644, 0.3683),

                        specimen.build(),
                        slide.setSlidePosition(1800),

                        new ParallelAction(
                                slide.setSlidePosition(1800),
                                axel.setAxelPosition(50, 0.2)
                        ),
                        slide.setSlidePosition(600),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),
                        axel.setAxelPosition(0, -0.5),
                        slide.setSlidePosition(0),
                        sample2.build(),
                        sample3.build(),
                        specimen2PickUp.build(),
                        grabber.setClawPosition(0.6, 0.7, 0.3683),
                        axel.setAxelPosition(300, 0.1),
                        new SleepAction(0.2),
                        grabber.setClawPosition(0.32, 0.7, 0.3683),
                        new SleepAction(0.2),
                        axel.setAxelPosition(0, -0.5),

                        //Nothing bellow here actually occurs;
                        postSpecimen2PickUp.build(),
                        specimen.build(),
                        slide.setSlidePosition(1800),

                        new ParallelAction(
                                slide.setSlidePosition(1800),
                                axel.setAxelPosition(50, 0.2)
                        ),
                        slide.setSlidePosition(600),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),
                        axel.setAxelPosition(0, -0.5),
                        slide.setSlidePosition(0),
                        park.build()



                )

        );
    }




    public class Slide
    {
        DcMotorEx slideMotor;
        public Slide(HardwareMap hardwareMap)
        {

            slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");

            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        public class SetSlidePosition implements Action {
            int slideTarget;
            ElapsedTime runTime = new ElapsedTime();
            public SetSlidePosition(int slideTar) { slideTarget = slideTar;}

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                runTime.reset();
                if (runTime.seconds() < 10) {
                    if (slideMotor.getCurrentPosition() < slideTarget) {
                        if (slideMotor.getCurrentPosition() >= slideTarget - 100) {
                            slideMotor.setPower(0.5);
                        } else slideMotor.setPower(1);

                        telemetry.addData("slide pos:", slideMotor.getCurrentPosition());
                        telemetry.update();

                        return true;
                    } else if (slideMotor.getCurrentPosition() > slideTarget) {
                        if (slideMotor.getCurrentPosition() > slideTarget + 100) {
                            slideMotor.setPower(-1);
                            return true;
                        } else {
                            slideMotor.setPower(-0.1);
                            return true;
                        }
                    } else {
                        slideMotor.setPower(0.002);
                        telemetry.clearAll();
                        return false;
                    }
                }
                else return false;
            }
        }
        public Action setSlidePosition(int slideTar){ return new SetSlidePosition(slideTar);}

    }


    public class Axel
    {
        DcMotorEx axelMotor;
        DcMotorEx axelMotor2;
        public Axel(HardwareMap hardwareMap)
        {
            axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
            axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");
            axelMotor2.setDirection(DcMotor.Direction.REVERSE);

            axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            axelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            axelMotor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        public class SetAxelPosition implements Action {
            int axelTarget;
            double axelPower;

            public SetAxelPosition(int axelTar, double power) { axelTarget = axelTar; axelPower = power;}

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                if (axelMotor.getCurrentPosition() < axelTarget + 5 && axelMotor.getCurrentPosition() > axelTarget - 5) {
                    axelMotor.setPower(0.005);
                    axelMotor2.setPower(0.005);
                    return false;
                }else{
                    axelMotor.setPower(axelPower);
                    axelMotor2.setPower(axelPower);
                    return true;
                }
            }
        }
        public Action setAxelPosition(int axelTar, double power){ return new SetAxelPosition(axelTar, power);}

    }


    public class Grabber {
        Servo yClaw;
        Servo xClaw;
        Servo claw;

        public Grabber(HardwareMap hardwareMap) {
            yClaw = hardwareMap.get(Servo.class, "yClaw");
            yClaw.setDirection(Servo.Direction.FORWARD);

            xClaw = hardwareMap.get(Servo.class, "xClaw");
            xClaw.setDirection(Servo.Direction.REVERSE);

            claw = hardwareMap.get(Servo.class, "claw");
            claw.setDirection(Servo.Direction.FORWARD);
        }

        public class SetClawPosition implements Action {
            double yClawPosition;
            double clawPosition;
            double xClawPosition;

            public SetClawPosition(double clawPos, double yClawPos, double xClawPos) { clawPosition = clawPos; yClawPosition = yClawPos; xClawPosition = xClawPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                claw.setPosition(clawPosition);
                yClaw.setPosition(yClawPosition);
                xClaw.setPosition(xClawPosition);

                return false;
            }

        }

        public Action setClawPosition(double clawPos, double yClawPos, double xClawPos) {
            return new SetClawPosition(clawPos, yClawPos, xClawPos);
        }

    }


}