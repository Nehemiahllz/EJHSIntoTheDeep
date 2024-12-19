package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "MainTeleOp", group = "Main")

//public class MainTele extends RobotCore {
//    final double grabHeight = 46;
//    final double ticksPerSlideMM = 4.468;
//    final double ticksPerDegree = 3.877;
//    final double axelPosLowest = 343;
//    double slideLength;
//    final double slideLengthZero = 317.5;
//    double axelAngle;
//
//    double moveX;
//    double moveY;
//    double turnX;
//
//    double frontLeftPower;
//    double frontRightPower;
//    double backLeftPower;
//    double backRightPower;
//
//    int slideMax = 4480;
//    int slideMin = 0;
//
//
//    boolean requireRetract;
//
//    public enum axelMode {GRAB, BUCKET, ABOVEGRAB, HANG, HANGING, GRABSPEC, BAR, RESET, GRABBING}
//    axelMode axelMoving;
//
//    ElapsedTime clawTimer = new ElapsedTime();
//
//
//    private PIDController controller;
//
//    public double p = 0.009, i = 0, d = 0.000125;
//    public double f = 0.005;
//
//    public static int target = 0;
//
//    private final double ticks_in_degree = 700 / 180.0;
//
//    boolean slideReset = false;
//
//    public void init() {
//        super.init();
//
//        controller = new PIDController(p, i, d);
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//        axelMotor.setPower(0);
//        axelMotor2.setPower(0);
//        axelMotor.setTargetPosition(0);
//        axelMotor2.setTargetPosition(0);
//        axelMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
//        axelMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        axelMotor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        axelMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
//        axelMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
//        target = 0;
//        axelMoving = axelMode.BUCKET;
//
//        slideMotor.setPower(0);
//        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slideMotor.setTargetPosition(0);
//        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slideMin = slideMotor.getCurrentPosition();
//
//        printDebugData();
//    }
//
//    public void loop() {
//        printDebugData();
//
//        controller.setPID(p, i, d);
//        int axelPos = axelMotor.getCurrentPosition();
//        double pid = controller.calculate(axelPos, target);
//        double ff = Math.cos(Math.toRadians(target/ticks_in_degree)) * f;
//
//        double power = pid + ff;
//
//        axelMotor.setTargetPosition(target);
//        axelMotor2.setTargetPosition(target);
//
//
//
//        if(axelMoving == axelMode.GRABBING){
//            axelMotor.setPower(0);
//            axelMotor2.setPower(0);
//        }else {
//                axelMotor.setPower(1);
//                axelMotor2.setPower(1);
//        }
//
//        slideMax = slideMin + 3030;
//        slideMotor.setPower(1);
//
//        //Drivetrain
//        moveX = gamepad1.left_stick_x;
//        moveY = -gamepad1.left_stick_y;
//        turnX = gamepad1.right_stick_x;
//
//        frontLeftPower = moveY + moveX + turnX;
//        frontRightPower = moveY - moveX - turnX;
//        backLeftPower = moveY - moveX + turnX;
//        backRightPower = moveY + moveX - turnX;
//
//        //Drivetrain Driver Controls
//        if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
//
//            if (gamepad1.right_bumper) {
//                leftFront.setPower(frontLeftPower * 0.8);
//                rightFront.setPower(frontRightPower * 0.8);
//                leftBack.setPower(backLeftPower * 0.8);
//                rightBack.setPower(backRightPower * 0.8);
//            } else if (gamepad1.left_bumper) {
//                leftFront.setPower(frontLeftPower * 0.25);
//                rightFront.setPower(frontRightPower * 0.25);
//                leftBack.setPower(backLeftPower * 0.25);
//                rightBack.setPower(backRightPower * 0.25);
//            } else {
//                leftFront.setPower(frontLeftPower * 0.55);
//                rightFront.setPower(frontRightPower * 0.55);
//                leftBack.setPower(backLeftPower * 0.55);
//                rightBack.setPower(backRightPower * 0.55);
//            }
//        } else {
//            leftFront.setPower(0);
//            rightFront.setPower(0);
//            leftBack.setPower(0);
//            rightBack.setPower(0);
//        }
//
//
//
//
//        //GRAB
//        if(gamepad2.a){
//            axelMoving = axelMode.ABOVEGRAB;
//            yClaw.setPosition(0.4);
//            xClaw.setPosition(0.05);
//            requireRetract = true;
//        }
//
//        if(gamepad2.x || gamepad1.b){
//            if(axelMoving == axelMode.ABOVEGRAB || axelMoving == axelMode.GRABBING){
//                axelMoving = axelMode.GRAB;
//                yClaw.setPosition(0.81);
//            }
//        }
//
//        if(gamepad1.a){
//            if(axelMoving == axelMode.GRAB){
//                axelMoving = axelMode.GRABBING;
//                yClaw.setPosition(0.81);
//            }
//        }
//
//
//        //GRAB/SCORE SPECIMEN
//        if(gamepad2.dpad_left){
//            xClaw.setPosition(0.05);
//            yClaw.setPosition(0.556);
//            axelMoving = axelMode.GRABSPEC;
//            requireRetract = true;
//        }
//
//        if(gamepad2.dpad_right){
//            yClaw.setPosition(0.1911);
//            xClaw.setPosition(0);
//            axelMoving = axelMode.BAR;
//            requireRetract = true;
//        }
//
//
//        //SCORE SAMPLE
//        if(gamepad2.y){
//            xClaw.setPosition(0.7);
//            claw.setPosition(0.36);
//            yClaw.setPosition(0.8);
//            axelMoving = axelMode.BUCKET;
//            requireRetract = true;
//        }
//
//
//        //HANG
//        if(gamepad1.y){
//            requireRetract = true;
//            axelMoving = axelMode.HANG;
//        }
//        if(gamepad1.x){
//            if(axelMoving == axelMode.HANG) {
//                axelMoving = axelMode.HANGING;
//            }
//        }
//
//
//        if(gamepad2.left_bumper){
//            slideReset = true;
//            slideMotor.setTargetPosition(-4000);
//        }
//        if(gamepad2.right_bumper && slideReset){
//            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            slideMotor.setTargetPosition(0);
//            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
//
//
//
//        if(requireRetract) {
//            slideMotor.setTargetPosition(0);
//            if(slideMotor.getCurrentPosition() < slideMax * 0.1){
//                requireRetract = false;
//            }
//        }else {
//                switch (axelMoving) {
//                    case GRAB:
////                        target = findAxelPos() - 5;
//                        if(slideMotor.getCurrentPosition() > 1200){
//                            target = 336;
//                        }else if(slideMotor.getCurrentPosition() > 1050){
//                            target = 330;
//                        }else if(slideMotor.getCurrentPosition() > 600){
//                            target = 324;
//                        }else{
//                            target = 314;
//                        }
//                        break;
//                    case ABOVEGRAB:
//                        if (axelMotor.getCurrentPosition() > 210) {
//                            target = 290;
//                        } else {
//                            target = 240;
//                        }
//                        break;
//                    case GRABSPEC:
//                        target = 230;
//                        break;
//                    case BUCKET:
//                        target = -40;
//                        break;
//                    case BAR:
//                        target = -40;
//                        break;
//                    case HANG:
//                        target = 125;
//                        break;
//                    case HANGING:
//                        target = -40;
//                        break;
//                    default:
//                        target = 0;
//            }
//        }
//
//
//        //Claw open
//        if(gamepad1.left_trigger > 0.1){
//            claw.setPosition(0.55);
//        }
//        if (gamepad1.right_trigger > 0.1) {
//            claw.setPosition(0.31);
//        }
//
//
//
//        //Claw rotate
//        if(clawTimer.time(TimeUnit.MILLISECONDS) > 80){
//            if(gamepad1.dpad_right){
//                xClaw.setPosition(xClaw.getPosition() + 0.055);
//                clawTimer.reset();
//            }else if(gamepad1.dpad_left){
//                xClaw.setPosition(xClaw.getPosition() - 0.055);
//                clawTimer.reset();
//            }
//        }
//
//
//        //Claw tilt out of bucket
//        if(gamepad2.left_trigger > 0.1){
//            yClaw.setPosition(0.8);
//        }
//        //Claw tilt into bucket
//        if(gamepad2.right_trigger > 0.1){
//            yClaw.setPosition(0.3);
//        }
//
//
//
//        //Manual slide extension with limits
//        if (gamepad2.dpad_up) {
//            if (axelMoving == axelMode.GRAB || axelMoving == axelMode.ABOVEGRAB) {
//                if (slideMotor.getCurrentPosition() < slideMax * 0.37) {
//                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 120);
//                }
//            } else if(axelMoving == axelMode.BUCKET || axelMoving == axelMode.BAR || axelMoving == axelMode.HANG){
//                if (slideMotor.getCurrentPosition() < slideMax - 400){
//                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 250);
//                }else if (slideMotor.getCurrentPosition() < slideMax){
//                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 80);
//                }
//            }
//        }
//        //Manual slide detraction
//        if (gamepad2.dpad_down) {
//            if (slideMotor.getCurrentPosition() > 0) {
//                    slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 200);
//            }
//        }
//
//    }
//
//    //Show data on the driver hub
//    private void printDebugData() {
//        telemetry.addLine("----Controller Inputs----");
//        telemetry.addData("slideMin", slideMin);
//        telemetry.addData("slideMax", slideMax);
//        telemetry.addData("Slide", slideMotor.getCurrentPosition());
//        telemetry.addData("Axel", axelMotor.getCurrentPosition());
//        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
//        telemetry.addData("yClaw", yClaw.getPosition());
//        telemetry.addData("xClaw", xClaw.getPosition());
//        telemetry.addData("claw", claw.getPosition());
//        telemetry.addData("axelTarget", target);
//    }
//
//    private int findAxelPos() {
//        slideLength = ((slideMotor.getCurrentPosition() + 100) * ticksPerSlideMM) + slideLengthZero;
//        axelAngle = Math.toDegrees(Math.asin(grabHeight/slideLength));
//        return (int) (axelPosLowest - (axelAngle * ticksPerDegree));
//    }
//
//}













//Re-add public before the class:
public class MainTele extends RobotCore {

    double moveX;
    double moveY;
    double turnX;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    int slideMax = 4250;

    boolean requireRetract;

    public enum axelMode {ABOVEGRAB, GRABSAMPLE, BUCKET, HANG, GRABSPEC, BAR}
    axelMode axelMoving;

    public enum hangSequence {RESET, AXEL_BAR1, SLIDE_BAR1, SLIDE_PULL1, AXEL_CLIP1, AXEL_BAR2, SLIDE_BAR2, SLIDE_PULL2, AXEL_CLIP2}
    hangSequence hangSeq;


    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime runTime = new ElapsedTime();

    int axelTarget;
    int slideTarget;

    boolean slideReset = false;

    Gamepad.RumbleEffect endgame;
    Gamepad.RumbleEffect hangTime;
    Gamepad.RumbleEffect sampleRumble;

    boolean endgameRumbled = false;
    boolean hangRumbled = false;

    boolean runTimeReset = false;

    boolean aboveSample = true;


    public void init() {
        super.init();

        axelMotor.setPower(0);
        axelMotor2.setPower(0);
        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        axelMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        axelMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        axelTarget = 0;
        axelMoving = axelMode.BUCKET;

        slideMotor.setPower(0);
//        slideMotor2.setPower(0);
        slideMotor.setTargetPosition(0);
//        slideMotor2.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        printDebugData();
    }

    public void loop() {
        printDebugData();
        rumbles();
        drivetrain();


        axelMotor.setTargetPosition(axelTarget);
        axelMotor2.setTargetPosition(axelTarget);
        slideMotor.setTargetPosition(slideTarget);
//        slideMotor2.setTargetPosition(slideTarget);

        slideMotor.setPower(1);
        axelMotor.setPower(1);
        axelMotor2.setPower(1);
//        slideMotor2.setPower(1);


        //GRAB
        if(gamepad2.a){
            axelMoving = axelMode.ABOVEGRAB;
            yClaw.setPosition(0.4);
            xClaw.setPosition(0.05);
            requireRetract = true;
        }

        if((gamepad2.x || gamepad1.b) && axelMoving == axelMode.ABOVEGRAB){
                axelMoving = axelMode.GRABSAMPLE;
                yClaw.setPosition(0.81);
        }


        //GRAB/SCORE SPECIMEN
        if(gamepad2.dpad_left){
            xClaw.setPosition(0.05);
            yClaw.setPosition(0.556);
            axelMoving = axelMode.GRABSPEC;
            requireRetract = true;
        }

        if(gamepad2.dpad_right){
            yClaw.setPosition(0.1911);
            xClaw.setPosition(0);
            axelMoving = axelMode.BAR;
            requireRetract = true;

            //EXPERIMENTAL AUTO RELEASE CLAW:
            claw.setPosition(0.39);
        }


        //SCORE SAMPLE
        if(gamepad2.y){
            xClaw.setPosition(0.7);
            claw.setPosition(0.36);
            yClaw.setPosition(0.8);
            axelMoving = axelMode.BUCKET;
            requireRetract = true;
        }


        //HANG
        if(gamepad1.y && gamepad2.b){
            requireRetract = true;
            axelMoving = axelMode.HANG;
        }


        if(gamepad2.left_bumper){
            slideReset = true;
            slideMotor.setTargetPosition(-4000);
        }
        if(gamepad2.right_bumper && slideReset){
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setTargetPosition(0);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }



        if(requireRetract) {
            slideMotor.setTargetPosition(0);
            if(slideMotor.getCurrentPosition() < slideMax * 0.1){
                requireRetract = false;
            }
        }else {
            switch (axelMoving) {
                case GRABSAMPLE:
                    axelTarget = 336;
                    break;
                case ABOVEGRAB:
                    axelTarget = 330;
                    break;
                case GRABSPEC:
                    axelTarget = 230;
                    break;
                case BUCKET:
                    axelTarget = 0;
                    break;
                case BAR:
                    axelTarget = 120;
                    slideTarget = 2000;
                    break;
                case HANG:
                    switch (hangSeq){
                        case RESET:
                            axelTarget = 0;
                            slideTarget = 0;
                            if(axelMotor.getCurrentPosition() < 20 && slideMotor.getCurrentPosition() < 100){
                                hangSeq = hangSequence.SLIDE_BAR1;
                            }
                            break;
                        case SLIDE_BAR1:
                            slideTarget = 2000;
                            if(slideMotor.getCurrentPosition() > 1995 && slideMotor.getCurrentPosition() < 2005){
                                hangSeq = hangSequence.AXEL_BAR1;
                            }
                            break;
                        case AXEL_BAR1:
                            axelTarget = 120;
                            if(axelMotor.getCurrentPosition() > 118 && axelMotor.getCurrentPosition() < 122){
                                hangSeq = hangSequence.SLIDE_PULL1;
                            }
                            break;
                        case SLIDE_PULL1:
                            slideTarget = 0;
                            if(slideMotor.getCurrentPosition() < 5){
                                hangSeq = hangSequence.AXEL_CLIP1;
                            }
                        case AXEL_CLIP1:
                            axelTarget = 100;
                            if(axelMotor.getCurrentPosition() > 98 && axelMotor.getCurrentPosition() < 102){
                                hangSeq = hangSequence.SLIDE_BAR2;
                            }
                            break;
                        case SLIDE_BAR2:
                            slideTarget = 2000;
                            if(slideMotor.getCurrentPosition() > 1995 && slideMotor.getCurrentPosition() < 2005){
                                hangSeq = hangSequence.AXEL_BAR2;
                            }
                            break;
                        case AXEL_BAR2:
                            axelTarget = 120;
                            if(axelMotor.getCurrentPosition() > 118 && axelMotor.getCurrentPosition() < 202){
                                hangSeq = hangSequence.SLIDE_PULL2;
                            }
                            break;
                        case SLIDE_PULL2:
                            slideTarget = 0;
                            if(slideMotor.getCurrentPosition() < 5){
                                hangSeq = hangSequence.AXEL_CLIP2;
                            }
                            break;
                        case AXEL_CLIP2:
                            axelTarget = 100;
                            break;
                    }
                    break;
                default:
                    axelTarget = 0;
            }
        }


        //Claw open
        if(gamepad1.left_trigger > 0.1){
            claw.setPosition(0.55);
        }
        if (gamepad1.right_trigger > 0.1) {
            claw.setPosition(0.31);
        }

        //Claw rotate
        if(clawTimer.time(TimeUnit.MILLISECONDS) > 80){
            if(gamepad1.dpad_right){
                xClaw.setPosition(xClaw.getPosition() + 0.055);
                clawTimer.reset();
            }else if(gamepad1.dpad_left){
                xClaw.setPosition(xClaw.getPosition() - 0.055);
                clawTimer.reset();
            }
        }

        //Claw tilt out of bucket
        if(gamepad2.left_trigger > 0.1){
            yClaw.setPosition(0.8);
        }
        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1){
            yClaw.setPosition(0.3);
        }



        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.GRABSAMPLE || axelMoving == axelMode.ABOVEGRAB) {
                if (slideTarget < slideMax * 0.37) {
                   slideTarget += 50;
                }
            } else if(axelMoving == axelMode.BUCKET){
                slideTarget = slideMax;
            }
        }
        //Manual slide detraction
        if (gamepad2.dpad_down) {
            if (slideMotor.getCurrentPosition() > 0) {
                slideTarget -= -100;
            }
        }

    }

    //Show data on the driver hub
    private void printDebugData() {
        telemetry.addLine("----Behind the Scenes----");
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
//        telemetry.addData("Slide2", slideMotor2.getCurrentPosition());
        telemetry.addData("slideTarget", slideTarget);
        telemetry.addData("slideMax", slideMax);
        telemetry.addLine(" ");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("axelTarget", axelTarget);
        telemetry.addLine(" ");
        telemetry.addData("yClaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());
        telemetry.addData("claw", claw.getPosition());
    }

    private void rumbles(){

        if(!runTimeReset){
            runTime.reset();
            runTimeReset = true;
        }

        telemetry.addData("runTime: ", runTime.time(TimeUnit.SECONDS));
        telemetry.addData("endgameRumbled", endgameRumbled);
        telemetry.addData("hangRumbled", hangRumbled);

        endgame = new Gamepad.RumbleEffect.Builder()
                .addStep(0.7, 0, 200)
                .addStep(0, 0.7, 200)
                .addStep(0, 0, 100)
                .addStep(1, 1, 100)
                .addStep(0.9, 0.9, 35)
                .addStep(0.8, 0.8, 35)
                .addStep(0.7, 0.7, 35)
                .addStep(0.6, 0.6, 35)
                .addStep(0.5, 0.5, 35)
                .addStep(0.4, 0.4, 35)
                .addStep(0.3, 0.3, 35)
                .addStep(0.2, 0.2, 35)
                .addStep(0.1, 0.1, 35)
                .build();

        hangTime = new Gamepad.RumbleEffect.Builder()
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .addStep(0, 0, 100)
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .addStep(0, 0, 100)
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .addStep(0, 0, 100)
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .addStep(0, 0, 100)
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .addStep(0, 0, 100)
                .addStep(0.5, 0, 100)
                .addStep(0, 0, 100)
                .addStep(0, 0.5, 100)
                .build();

        sampleRumble = new Gamepad.RumbleEffect.Builder()
                .addStep(1, 1, 50)
                .build();


        if (runTime.time(TimeUnit.SECONDS) > 89 && !endgameRumbled)  {
            gamepad1.runRumbleEffect(endgame);
            endgameRumbled = true;
        }

        if (runTime.time(TimeUnit.SECONDS) > 104 && !hangRumbled)  {
            gamepad1.runRumbleEffect(hangTime);
            hangRumbled = true;
        }

        if(aboveSample){
            gamepad1.runRumbleEffect(sampleRumble);
        }
    }

    private void drivetrain(){
        //Drivetrain
        moveX = gamepad1.left_stick_x;
        moveY = -gamepad1.left_stick_y;
        turnX = gamepad1.right_stick_x;

        frontLeftPower = moveY + moveX + turnX;
        frontRightPower = moveY - moveX - turnX;
        backLeftPower = moveY - moveX + turnX;
        backRightPower = moveY + moveX - turnX;

        //Drivetrain Driver Controls
        if (Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {

            if (gamepad1.right_bumper) {
                leftFront.setPower(frontLeftPower * 0.8);
                rightFront.setPower(frontRightPower * 0.8);
                leftBack.setPower(backLeftPower * 0.8);
                rightBack.setPower(backRightPower * 0.8);
            } else if (gamepad1.left_bumper) {
                leftFront.setPower(frontLeftPower * 0.25);
                rightFront.setPower(frontRightPower * 0.25);
                leftBack.setPower(backLeftPower * 0.25);
                rightBack.setPower(backRightPower * 0.25);
            } else {
                leftFront.setPower(frontLeftPower * 0.55);
                rightFront.setPower(frontRightPower * 0.55);
                leftBack.setPower(backLeftPower * 0.55);
                rightBack.setPower(backRightPower * 0.55);
            }
        } else {
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
        }
    }


}