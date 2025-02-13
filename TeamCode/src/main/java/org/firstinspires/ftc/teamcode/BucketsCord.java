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
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Objects;
import java.util.Vector;

@Autonomous(name = "BucketsCord", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class BucketsCord extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Change StartPos
        //Left side of robot, beside the vertical bar, vertical part of the side holder
        Pose2d start = new Pose2d(-33, -63, Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        Sweeper sweeper = new Sweeper(hardwareMap);

        Stop stopper = new Stop(hardwareMap);

        Cam camera = new Cam(hardwareMap);

        //This top line will have the position the robot is currently in, but the bottom is where the robot will go
        //The bottom line can have as many lines as you want, but the last line will have the semi colon, not the others

        TrajectoryActionBuilder sample = drive.actionBuilder(start)
                .strafeTo(new Vector2d(-34, -47))
                .strafeToLinearHeading(new Vector2d(-54.4, -53.8), Math.toRadians(45));

        TrajectoryActionBuilder sample1 = drive.actionBuilder(new Pose2d(-54.4, -53.8, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(-46.5, -41), Math.toRadians(90));

        TrajectoryActionBuilder sample1Score = drive.actionBuilder(new Pose2d(-46.5, -41, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-50, -50), Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(-53, -53.4), Math.toRadians(45));

        TrajectoryActionBuilder sample2 = drive.actionBuilder(new Pose2d(-53, -53.4, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(-56.5, -41), Math.toRadians(90));

        TrajectoryActionBuilder sample2Score = drive.actionBuilder(new Pose2d(-56.5, -41, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-50, -50), Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(-53, -53.4), Math.toRadians(45));

        TrajectoryActionBuilder sample3 = drive.actionBuilder(new Pose2d(-53, -53.4, Math.toRadians(45)))
                .strafeToLinearHeading(new Vector2d(-44, -23), Math.toRadians(180))
                .strafeTo(new Vector2d(-51.25, -23));

        TrajectoryActionBuilder sample3Score = drive.actionBuilder(new Pose2d(-51.25, -23, Math.toRadians(180)))
                .strafeToLinearHeading(new Vector2d(-50, -50), Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(-52.5, -54.5), Math.toRadians(45));



        TrajectoryActionBuilder submersible1 = drive.actionBuilder(new Pose2d(-52.5, -54.5, Math.toRadians(45)))
                .splineToLinearHeading(new Pose2d(-35, -10, Math.toRadians(0)), Math.toRadians(90))
                .strafeTo(new Vector2d(-23.5, -10));

        TrajectoryActionBuilder submersible1Grab = drive.actionBuilder(new Pose2d(-23.5, -10, Math.toRadians(0)))
                .strafeTo(new Vector2d(-23.5, -4.5));

        TrajectoryActionBuilder sub1SPullOut = drive.actionBuilder(new Pose2d(-23.5, -4.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-35, -4.5), Math.toRadians(0));

        TrajectoryActionBuilder sub1Score = drive.actionBuilder(new Pose2d(-35, -4.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-50, -52), Math.toRadians(45))
                .strafeTo(new Vector2d(-52.5, -54.5));


        TrajectoryActionBuilder submersible2 = drive.actionBuilder(new Pose2d(-23.5, -10, Math.toRadians(0)))
                .strafeTo(new Vector2d(-23.5, -8));

        TrajectoryActionBuilder submersible2Grab = drive.actionBuilder(new Pose2d(-23.5, -8, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-23.5, -2.5), Math.toRadians(0));

        TrajectoryActionBuilder sub2SPullOut = drive.actionBuilder(new Pose2d(-23.5, -2.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-35, -2.5), Math.toRadians(0));

        TrajectoryActionBuilder sub2Score = drive.actionBuilder(new Pose2d(-35, -2.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-50, -52), Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(-52.5, -54.5), Math.toRadians(45));


        TrajectoryActionBuilder submersible3 = drive.actionBuilder(new Pose2d(-23.5, -8, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-23.5, -6), Math.toRadians(45));

        TrajectoryActionBuilder submersible3Grab = drive.actionBuilder(new Pose2d(-23.5, -6, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-23.5, -0.5), Math.toRadians(0));

        TrajectoryActionBuilder sub3SPullOut = drive.actionBuilder(new Pose2d(-23.5, -0.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-35, -0.5), Math.toRadians(0));

        TrajectoryActionBuilder sub3Score = drive.actionBuilder(new Pose2d(-35, -0.5, Math.toRadians(0)))
                .strafeToLinearHeading(new Vector2d(-50, -52), Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(-52.5, -54.5), Math.toRadians(45));


        //Closes the claw onto the sample
        Actions.runBlocking(
                new ParallelAction(
                    claw.setClawPosition(0.4),
                        camera.activate()
                )
        );

        boolean stopInput = false;
        int xPos = 0;
        int yPos = 0;


        while(!stopInput){
            if(gamepad2.dpad_up){
                yPos ++;
            }

            if(gamepad2.dpad_down){
                yPos--;
            }


            if(gamepad2.dpad_right){
                xPos++;
            }

            if(gamepad2.dpad_left){
                xPos--;
            }

            try {
                Thread.sleep(50); // Adjust the delay as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        waitForStart();
        if (isStopRequested()) return;





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
                } else if (slideTarget == 15 && slideMotor.getCurrentPosition() < 25) {
                    return false;
                }else if (slideMotor.getCurrentPosition() > slideTarget - 6 && slideMotor.getCurrentPosition() < slideTarget + 6) {
                    return false;
                } else if (slideTarget == 2240 && slideMotor.getCurrentPosition() > 2210) {
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

                axelMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                axelMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

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


                if(tx > -5.5 && tx < 5.5) {

                    telemetry.addData("tx", tx);

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
                            telemetry.addData("5", distance.get(5));


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
                } else{
                    limeCheckLoc++;
                    telemetry.addData("tx", tx);
                    return false;
                }

            }

        }

        public Action subSample(){
            return new SubSample();
        }


    }



}