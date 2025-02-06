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

//Re-add public before the class:
public class MainTele extends RobotCore {

    double moveX;
    double moveY;
    double turnX;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    int slideMax = 2160;

    boolean requireRetract;

    public enum axelMode {ABOVEGRAB, GRABSAMPLE, BUCKET, HANG, GRABSPEC, BAR, START, POSFIND}
    axelMode axelMoving;

    public enum hangSequence {SETUP, AXEL_BAR1, SLIDE_BAR1, SLIDE_PULL1, AXEL_ROTATE}
    hangSequence hangSeq;


    boolean hangTilt = false;

    boolean activeDrivetrain = true;

    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime runTime = new ElapsedTime();

    int axelTarget = 0;
    int slideTarget = 0;

    int xClawRotate = 0;

    double axelPower = 1;

    boolean slideReset = false;

    Gamepad.RumbleEffect endgame;
    Gamepad.RumbleEffect hangTime;
    Gamepad.RumbleEffect sampleRumble;

    Gamepad.RumbleEffect yClawTilt;

    boolean endgameRumbled = false;
    boolean hangRumbled = false;

    boolean runTimeReset = false;

    boolean aboveSample = false;


    public void init() {
        super.init();

        axelMotor.setPower(0);
        axelMotor2.setPower(0);
        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        axelTarget = 0;
        axelMoving = axelMode.START;

        slideMotor.setPower(0);
        slideMotor2.setPower(0);
        slideMotor.setTargetPosition(0);
        slideMotor2.setTargetPosition(0);

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        printDebugData();
    }

    public void loop() {
        printDebugData();
        rumbles();
        drivetrain();


        axelMotor.setTargetPosition(axelTarget);
        axelMotor2.setTargetPosition(axelTarget);
        slideMotor.setTargetPosition(slideTarget);
        slideMotor2.setTargetPosition(slideTarget);

        slideMotor.setPower(1);
        slideMotor2.setPower(1);

        axelMotor.setPower(axelPower);
        axelMotor2.setPower(axelPower);

        //GRAB
        if (gamepad2.a) {
            if(axelMoving == axelMode.GRABSAMPLE){
                requireRetract = false;
            }else{
                requireRetract = true;
            }
            axelMoving = axelMode.ABOVEGRAB;
            stopper.setPosition(0.53);
            yClaw.setPosition(0.807);
            xClaw.setPosition(0.745);
            xClawRotate = 0;
        }

        if ((gamepad2.x || gamepad1.b) && axelMoving == axelMode.ABOVEGRAB) {
            axelMoving = axelMode.GRABSAMPLE;
            yClaw.setPosition(0.95);
        }

        if (axelMoving == axelMode.HANG && gamepad2.b) {
            hangTilt = true;
        }


        //GRAB/SCORE SPECIMEN
        if (gamepad2.dpad_left) {
            xClawRotate = 0;
            xClaw.setPosition(0.745);
            yClaw.setPosition(0.7);
            axelMoving = axelMode.GRABSPEC;
            requireRetract = true;
        }

        if (gamepad2.dpad_right && axelMoving == axelMode.GRABSPEC) {
            yClaw.setPosition(0.1);
            xClaw.setPosition(0.745);
            axelMoving = axelMode.BAR;
        }


        //SCORE SAMPLE
        if (gamepad2.y) {
            requireRetract = true;
            xClawRotate = 0;
            xClaw.setPosition(0.745);
            yClaw.setPosition(0.95);
            axelMoving = axelMode.BUCKET;
        }


        //HANG
        if (gamepad1.y) {
            requireRetract = true;
            hangSeq = hangSequence.SETUP;
            axelMoving = axelMode.HANG;
        }


        if (gamepad2.left_bumper) {
            slideReset = true;
            slideTarget = -5000;
            axelTarget = -2000;
        }
        if (gamepad2.right_bumper && slideReset) {
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            slideTarget = 0;

            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            axelTarget = 0;

            axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            slideReset = false;
            axelMoving = axelMode.BUCKET;
        }


        if (requireRetract) {
            if(axelMoving == axelMode.GRABSPEC){
                slideTarget = 100;
            }else {
                slideTarget = 0;
            }
            if (slideMotor.getCurrentPosition() < slideMax * 0.1 && slideMotor2.getCurrentPosition() < slideMax * 0.1) {
                requireRetract = false;
            }
        } else if (!slideReset) {
            switch (axelMoving) {
                case GRABSAMPLE:
                    axelPower = 0;
                    break;
                case ABOVEGRAB:
                    axelPower = 1;
                    axelTarget = 630;
                    break;
                case GRABSPEC:
                    axelTarget = 630;
                    if(axelMotor.getCurrentPosition() > 610){
                        axelPower = 0;
                    }else{
                        axelPower = 0.7;
                    }
                    break;
                case BUCKET:
                    axelTarget = -10;
                    if(axelMotor.getCurrentPosition() < 100) {
                        axelPower = 0.3;
                    }else{
                        axelPower = 0.85;
                    }
                    break;
                case BAR:
                    axelTarget = -10;
                    if(axelMotor.getCurrentPosition() < 100) {
                        axelPower = 0.3;
                    }else{
                        axelPower = 0.85;
                    }
                    break;
                case HANG:
                    switch(hangSeq){
                        case SETUP:
                            axelPower = 0.8;
                            axelTarget = 0;
                            stopper.setPosition(0.08);

                            if(axelMotor.getCurrentPosition() < 10){
                                hangSeq = hangSequence.SLIDE_BAR1;
                            }
                            break;

                        case SLIDE_BAR1:
                            slideTarget = 1055;
                            if(slideMotor.getCurrentPosition() > 1050){
                                hangSeq = hangSequence.AXEL_BAR1;
                            }
                            break;

                        case AXEL_BAR1:
                            if(axelMotor.getCurrentPosition() > 200) {
                                axelPower = 0.025;
                            }else{
                                axelPower = 0.1;
                            }

                            axelTarget = 247;
                            if(axelMotor.getCurrentPosition() > 245){
                                hangSeq = hangSequence.SLIDE_PULL1;
                            }
                            break;

                        case SLIDE_PULL1:

                            slideTarget = 0;

                            activeDrivetrain = false;
                            leftBack.setPower(1);
                            rightBack.setPower(1);
                            leftFront.setPower(1);
                            rightFront.setPower(1);

                            stopper.getController().pwmDisable();
                            claw.getController().pwmDisable();

                            if(slideMotor.getCurrentPosition() < 600){
                                axelTarget = 150;
                            }


                            if(slideMotor.getCurrentPosition() < 250){
                                hangSeq = hangSequence.AXEL_ROTATE;
                            }
                            break;

                        case AXEL_ROTATE:
                            axelPower = 1;
                            axelTarget = 0;

                            activeDrivetrain = true;
                            break;

                        default:
                            axelTarget = 0;
                            slideTarget = 0;
                            axelPower = 0.7;
                    }
                    break;
                default:
                    axelPower = 1;
                    axelTarget = 0;
            }
        }


        if(gamepad1.left_trigger > 0.1) claw.setPosition(0.807);

        //Claw close
        if (gamepad1.right_trigger > 0.1) claw.setPosition(0.15);


        //Claw rotate
        if(clawTimer.time(TimeUnit.MILLISECONDS) > 80){
            if(gamepad1.dpad_right && xClawRotate < 2){
                xClaw.setPosition(xClaw.getPosition() + 0.02835);
                xClawRotate += 1;
                clawTimer.reset();
            }else if(gamepad1.dpad_left && xClawRotate > -2){
                xClaw.setPosition(xClaw.getPosition() - 0.02835);
                xClawRotate -= 1;
                clawTimer.reset();
            }
        }

        //Claw tilt out of bucket
        if(gamepad2.left_trigger > 0.1) yClaw.setPosition(0.95);

        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1) yClaw.setPosition(0.35);


        if(gamepad2.left_stick_y > 0.2){
            sweep.setPosition(0.23);
        }else{
            sweep.setPosition(0.84);
        }




        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.GRABSAMPLE || axelMoving == axelMode.ABOVEGRAB) {
                if (slideTarget < 800) {
                   slideTarget += 25;
                }
            } else if(axelMoving == axelMode.BUCKET){
                slideTarget = slideMax;
            } else if(axelMoving == axelMode.BAR){
                slideTarget = 1380;
            } else if(axelMoving == axelMode.HANG){
                slideTarget = 1400;
            }
        }
        //Manual slide detraction
        if (gamepad2.dpad_down) {
            if (slideMotor.getCurrentPosition() > 0) {
                if(axelMoving == axelMode.HANG){
                    slideTarget = 0;
                }else {
                    slideTarget -= 50;
                }
            }
        }

    }

    //Show data on the driver hub
    private void printDebugData() {
        telemetry.addLine("----Behind the Scenes----");
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("Slide2", slideMotor2.getCurrentPosition());
        telemetry.addData("slideTarget", slideTarget);
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

        yClawTilt = new Gamepad.RumbleEffect.Builder()
                .addStep(0, 1, 100)
                .addStep(1, 0, 100)
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

    private void drivetrain() {
        if (activeDrivetrain) {
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

}