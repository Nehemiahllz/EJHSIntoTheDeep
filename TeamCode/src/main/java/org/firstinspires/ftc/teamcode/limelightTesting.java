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
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.opencv.core.Mat;

import java.util.Objects;
import java.util.Vector;

import javax.annotation.ParametersAreNonnullByDefault;

@Autonomous(name = "LimelightTesting", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class limelightTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Change StartPos
        //Left side of robot, beside the vertical bar, vertical part of the side holder
        Pose2d start = new Pose2d(0, 0, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        Sweeper sweeper = new Sweeper(hardwareMap);

        Stop stopper = new Stop(hardwareMap);

        Cam camera = new Cam(hardwareMap);

        int slideTargetDistance;


        TrajectoryActionBuilder driveUp = drive.actionBuilder(start)
                .strafeToLinearHeading(new Vector2d(-1, 0), Math.toRadians(0));

        TrajectoryActionBuilder subSample1Check = drive.actionBuilder(new Pose2d(0, 5, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-2, 5), Math.toRadians(90));

        TrajectoryActionBuilder subSample1 = drive.actionBuilder(new Pose2d(-2, 5, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-9, 5), Math.toRadians(90));


        //Closes the claw onto the specimen
        Actions.runBlocking(
                new SequentialAction(
                        claw.setClawPosition(0.807),
                        camera.activate()
                )
        );

        waitForStart();
        if (isStopRequested()) return;


        //The actual running stuff:

        Actions.runBlocking(
                new ParallelAction(
                        axel.setAxelPosition(),
                        new SequentialAction(
                                camera.rotation()
                        )
                )
        );


        TrajectoryActionBuilder afterMath = drive.actionBuilder(new Pose2d(-1, 0, Math.toRadians(targetRotation)))
                .strafeToLinearHeading(new Vector2d(-4, 0), Math.toRadians(0));


        Actions.runBlocking(
                new SequentialAction(
                        afterMath.build()
                )
        );

        for (int n = 0; n < 1000; n++) {
            telemetry.addData("finalTa", finalArea);
            telemetry.addData("finalTicks", slideDistanceTicksSample);
            telemetry.update();
        }


    }


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
                if (slideTarget == 0 && slideMotor.getCurrentPosition() < 10) {
                    return false;
                } else if (slideMotor.getCurrentPosition() > slideTarget - 6 && slideMotor.getCurrentPosition() < slideTarget + 6) {
                    return false;
                } else if (slideTarget == 2160 && slideMotor.getCurrentPosition() > 2140) {
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
    boolean axelOff = false;

    //Making the axel class to make the slide object to be moved during auto
    public class Axel {
        private PIDController controller;

        public double p = 0.005, i = 0, d = 0.00015;
        public double f = 0.0025;

        private final double ticks_in_degree = 700 / 180.0;

        DcMotorEx axelMotor;
        DcMotorEx axelMotor2;

        public Axel(HardwareMap hardwareMap) {
            controller = new PIDController(p, i, d);
            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

            axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
            axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");

            axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            axelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            axelMotor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);
            axelMotor.setDirection(DcMotorEx.Direction.FORWARD);
        }

        public class SetAxelPosition implements Action {

            public SetAxelPosition() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                controller.setPID(p, i, d);
                int axelPos = axelMotor.getCurrentPosition();
                double pid = controller.calculate(axelPos, target);
                double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

                double power = pid + ff;


                if (!axelOff) {
                    axelMotor.setPower(power);
                    axelMotor2.setPower(power);
                } else {
                    axelMotor.setPower(0);
                    axelMotor2.setPower(0);
                }

                telemetry.addData("pos", axelPos);
                telemetry.addData("target", target);
                telemetry.addData("power", power);

                telemetry.update();

                if (target == 10000000) {
                    return false;
                }
                return true;
            }
        }

        public Action setAxelPosition() {
            return new SetAxelPosition();
        }


        public class ChangeAxelPosition implements Action {
            int tar;
            double pow;

            public ChangeAxelPosition(int axelTar) {
                tar = axelTar;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                axelOff = false;
                target = tar;

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("axelTargetPos", axelMotor.getTargetPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                if (axelMotor.getCurrentPosition() > target - 3 && axelMotor.getCurrentPosition() < target + 3) {
                    return false;
                }
                return true;
            }
        }

        public Action changeAxelPosition(int axelTar) {
            return new ChangeAxelPosition(axelTar);
        }


        public class PickUp implements Action {

            public PickUp() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                telemetry.addData("axelPos", axelMotor.getCurrentPosition());
                telemetry.addData("axelTargetPos", axelMotor.getTargetPosition());
                telemetry.addData("Target", target);
                telemetry.addData("AxelPower", axelMotor.getPower());

                if (axelMotor.getCurrentPosition() > 520) {
                    axelOff = true;
                    target = 721;
                    return false;
                } else {
                    axelOff = false;
                    target = 530;
                }

                return true;
            }
        }

        public Action pickUp() {
            return new PickUp();
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


    public class Stop {
        Servo stopper;

        public Stop(HardwareMap hardwareMap) {
            stopper = hardwareMap.get(Servo.class, "stopper");
            stopper.setDirection(Servo.Direction.FORWARD);
        }

        public class SetStopperPosition implements Action {
            double stopperPos;

            public SetStopperPosition(double stopPos) {
                stopperPos = stopPos;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                stopper.setPosition(stopperPos);
                return false;
            }

        }

        public Action setStopperPosition(double stopPos) {
            return new SetStopperPosition(stopPos);
        }
    }

    int slideDistanceTicksSample;

    int i = 0;

    double robotAngle = 0;
    double targetRotation = 0;

    double finalArea = 0;
    double areaTotal = 0;

    Vector<Integer> distance = new Vector<>();
    Vector<Double> area = new Vector<>();

    public class Cam {
        private Limelight3A limelight;

        DcMotor leftBack;
        DcMotor rightBack;
        DcMotor leftFront;
        DcMotor rightFront;

        IMU imu;

        public Cam(HardwareMap hardwareMap) {
            limelight = hardwareMap.get(Limelight3A.class, "limelight");

            telemetry.setMsTransmissionInterval(11);

            limelight.pipelineSwitch(0);

            limelight.start();

            leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
            rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
            leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
            rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

            leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
            leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

            imu = hardwareMap.get(IMU.class, "imu");

            imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.FORWARD, RevHubOrientationOnRobot.UsbFacingDirection.LEFT)));

        }

        public class Activate implements Action {

            public Activate() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                limelight.start();

                return false;
            }

        }

        public Action activate() {
            return new Activate();
        }


        public class Distance implements Action {

            public Distance() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                LLResult result = limelight.getLatestResult();

                double tx = result.getTx(); // How far left or right the target is (degrees)
                double ty = result.getTy(); // How far up or down the target is (degrees)
                double ta = result.getTa();

                double a = 7301.82;
                double b = -5.8445;

                if (ta < 0.0099) {
                    return true;
                }

                if (ta <= 0.33) {
                    slideDistanceTicksSample = 800;
                } else if (ta <= 0.41) {
                    slideDistanceTicksSample = 710;
                } else if (ta <= 0.44) {
                    slideDistanceTicksSample = 620;
                } else if (ta <= 0.5) {
                    slideDistanceTicksSample = 527;
                } else if (ta <= 0.55) {
                    slideDistanceTicksSample = 435;
                } else if (ta <= 0.61) {
                    slideDistanceTicksSample = 400;
                } else if (ta <= 0.67) {
                    slideDistanceTicksSample = 365;
                } else if (ta <= 0.76) {
                    slideDistanceTicksSample = 270;
                } else if (ta <= 0.95) {
                    slideDistanceTicksSample = 175;
                } else {
                    slideDistanceTicksSample = 15;
                }

                distance.add(slideDistanceTicksSample);
                area.add(ta);

                if (i == 5) {
                    if (Objects.equals(distance.get(0), distance.get(1)) && Objects.equals(distance.get(0), distance.get(2)) && Objects.equals(distance.get(0), distance.get(3)) && Objects.equals(distance.get(0), distance.get(4)) && Objects.equals(distance.get(0), distance.get(5))) {
                        i = 0;

                        telemetry.addData("0", distance.get(0));
                        telemetry.addData("1", distance.get(1));
                        telemetry.addData("2", distance.get(2));
                        telemetry.addData("3", distance.get(3));
                        telemetry.addData("4", distance.get(4));

                        for (int h = 0; h < 6; h++) {
                            areaTotal += distance.get(h);
                        }

                        finalArea = (areaTotal / 6);

                        slideDistanceTicksSample = (int) (a * Math.exp(b * finalArea));

                        slideDistanceTicksSample += 60;

                        distance.clear();
                        area.clear();
                        return false;
                    } else {
                        i = 0;
                        distance.clear();
                        area.clear();
                        return true;
                    }
                } else if (i > 5) {
                    i = 0;
                    distance.clear();
                    area.clear();
                    return true;
                } else {
                    i++;
                    return true;
                }


            }
        }

        public Action distance() {
            return new Distance();
        }


        public class Rotation implements Action {

            double speed = 0;
            double angle = 0;

            public Rotation() {
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {

                LLResult result = limelight.getLatestResult();

                double tx = result.getTx(); // How far left or right the target is (degrees)
                double ty = result.getTy(); // How far up or down the target is (degrees)
                double ta = result.getTa();

                angle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                robotAngle = angle;

                if (robotAngle >= 0) {
                    targetRotation = 0 + robotAngle;
                } else {
                    targetRotation = 360 + robotAngle;
                }

                telemetry.addData("robotAngle", angle);
                telemetry.addData("targetRotation", targetRotation);

                leftBack.setPower(speed);
                leftFront.setPower(speed);
                rightBack.setPower(-speed);
                rightFront.setPower(-speed);

                telemetry.addData("tx", tx);

                if (tx > -4 && tx < -2) {
                    leftBack.setPower(0);
                    leftFront.setPower(0);
                    rightBack.setPower(0);
                    rightFront.setPower(0);

                    speed = 0;
                    telemetry.addLine("Targeted Successfully");
                    return false;
                } else {
                    if (tx <= -4) {
                        if (tx <= -20) {
                            speed = -0.35;
                        } else {
                            speed = -0.2;
                        }
                    } else if (tx >= -2) {
                        if (tx >= 14) {
                            speed = 0.35;
                        } else {
                            speed = 0.2;
                        }
                    }
                }


                return true;
            }

        }

        //-1.5 to -4

        public Action rotation() {
            return new Rotation();
        }


    }


}