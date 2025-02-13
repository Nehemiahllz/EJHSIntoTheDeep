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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
        Pose2d start = new Pose2d(0, 0, Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        Sweeper sweeper = new Sweeper(hardwareMap);

        Stop stopper = new Stop(hardwareMap);

        Cam camera = new Cam(hardwareMap);

        int slideTargetDistance;


        TrajectoryActionBuilder driveUp = drive.actionBuilder(start)
                .strafeToLinearHeading(new Vector2d(0, 5), Math.toRadians(90));

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
                        camera.subSample(),
                        axel.changeAxelPosition(955, 0.7),
                        new SleepAction(0.6),
                        axel.changeAxelPosition(0,0),
                        axel.changeAxelPosition(10000000, 0)
                )
                )
        );

        telemetry.addData("slideTarget", slideDistanceTicksSample);

        Actions.runBlocking(
                new SequentialAction(
                        slide.setSlidePosition(slideDistanceTicksSample, 1)
                )
        );

//        if(targetSample == 1){
//            Actions.runBlocking(
//                    new SequentialAction(
//                            new ParallelAction(
//                                    slide.setSlidePosition(slideDistanceTicksSample, 1),
//                                    subSample1.build()
//                            ),
//                            claw.setClawPosition(0.2)
//
//                    )
//            );
//        }

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

                axelMotor.setPower(pow);
                axelMotor2.setPower(pow);

                return false;
            }
        }

        public Action changeAxelPosition(int axelTar, double power) {
            return new ChangeAxelPosition(axelTar, power);
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
    int limeCheckLoc = 0;

    int i = 0;

    Vector<Double> area = new Vector<>();
    Vector<Integer> distance = new Vector<>();

    public class Cam {
        private Limelight3A limelight;

        public Cam(HardwareMap hardwareMap){
            limelight = hardwareMap.get(Limelight3A .class, "limelight");

            telemetry.setMsTransmissionInterval(11);

            limelight.pipelineSwitch(0);

            limelight.start();
        }

        public class Activate implements Action{

            public Activate(){
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                limelight.start();

                return false;
            }

        }

        public Action activate(){
            return new Activate();
        }



        public class SubSample implements Action{

            public SubSample(){
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){

                LLResult result = limelight.getLatestResult();

                double tx = result.getTx(); // How far left or right the target is (degrees)
                double ty = result.getTy(); // How far up or down the target is (degrees)
                double ta = result.getTa();


                if(tx > -7 && tx < 7) {

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
                    }else if (ta <= 0.67) {
                        slideDistanceTicksSample = 365;
                    } else if (ta <= 0.76) {
                        slideDistanceTicksSample = 270;
                    }else if (ta <= 0.85) {
                        slideDistanceTicksSample = 175;
                    } else {
                        slideDistanceTicksSample = 50;
                    }

                    distance.add(slideDistanceTicksSample);

                    if (i == 5) {
                        if (Objects.equals(distance.get(0), distance.get(1)) && Objects.equals(distance.get(0), distance.get(2)) && Objects.equals(distance.get(0), distance.get(3)) && Objects.equals(distance.get(0), distance.get(4)) && Objects.equals(distance.get(0), distance.get(5))) {
                            i = 0;

                            telemetry.addData("0", distance.get(0));
                            telemetry.addData("1", distance.get(1));
                            telemetry.addData("2", distance.get(2));
                            telemetry.addData("3", distance.get(3));
                            telemetry.addData("4", distance.get(4));

                            distance.clear();
                            return false;
                        } else {
                            i = 0;
                            distance.clear();
                            return true;
                        }
                    } else if (i > 5) {
                        i = 0;
                        distance.clear();
                        return true;
                    } else {
                        i++;
                        return true;
                    }
                }else{
                    limeCheckLoc++;
                    return false;
                }




//                    for (int s = 0; s <= 1; s++) {
//                        area.clear();
//                        for (int h = 0; h <= 999; h++) {
//                            area.add(ta);
//                        }
//
//                        maxCount = 0;
//                        for (int j = 0; j <= 999; j++) {
//                            count = 0;
//                            for (int k = 0; k <= 999; k++) {
//                                if (area.get(j) == area.get(k)) {
//                                    count++;
//                                }
//                            }
//                            if (count > maxCount) {
//                                maxLocation = j;
//                                maxCount = count;
//                            }
//                        }
//                        telemetry.addLine("DONE!");
//                        finalArea = area.get(maxLocation);
//
//                        if(finalArea < 0.0099){
//                            return true;
//                        }
//
//                        if (finalArea <= 0.46) {
//                            slideDistanceTicksSample = 800;
//                        } else if (finalArea <= 0.62) {
//                            slideDistanceTicksSample = 620;
//                        } else if (finalArea <= 0.8) {
//                            slideDistanceTicksSample = 435;
//                        } else if (finalArea <= 0.92) {
//                            slideDistanceTicksSample = 365;
//                        } else if (finalArea <= 1.3) {
//                            slideDistanceTicksSample = 175;
//                        } else {
//                            slideDistanceTicksSample = 50;
//                        }
//
//                        distance.add(slideDistanceTicksSample);
//
//
//                        telemetry.addData("Target X", tx);
//                        telemetry.addData("Target Y", ty);
//                        telemetry.addData("Target Area", ta);
//                        telemetry.addData("Final Area", finalArea);
//                        telemetry.addData("slideTicks", slideDistanceTicksSample);
//
//                        telemetry.update();
//                    }
//
//                    telemetry.addData("distance1", distance.get(0));
//                    telemetry.addData("distance2", distance.get(1));
//
//                        if(distance.get(0) == distance.get(1)){
//                            distanceEqual = true;
//                            telemetry.addLine("Ronaldo");
//                        }else{
//                            distanceEqual = false;
//                            telemetry.addLine("WRONG");
//                        }
//
//                        if (distanceEqual) {
//                            return false;
//                        }else{
//                            return true;
//                        }
//
//                    } else{
//                        telemetry.addData("Limelight", "No Targets");
//                        telemetry.update();
//                        return true;
//                    }

            }

        }

        public Action subSample(){
            return new SubSample();
        }


    }



}