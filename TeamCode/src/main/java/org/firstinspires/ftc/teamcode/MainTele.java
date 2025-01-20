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

    public enum hangSequence {RESET, AXEL_BAR1, SLIDE_BAR1, SLIDE_PULL1, AXEL_CLIP1, SLIDE_PULL2}
    hangSequence hangSeq;

    public enum axelPosFind {RESET, DROP, ACTIVATE}
    axelPosFind axelPosFinder;


    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime runTime = new ElapsedTime();
    ElapsedTime dropTime = new ElapsedTime();

    int axelTarget = 0;
    int slideTarget = 0;

    int axelPower = 1;

    boolean slideReset = false;

    Gamepad.RumbleEffect endgame;
    Gamepad.RumbleEffect hangTime;
    Gamepad.RumbleEffect sampleRumble;

    boolean endgameRumbled = false;
    boolean hangRumbled = false;

    boolean runTimeReset = false;

    boolean aboveSample = false;

    int offset = 0;

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


        if(axelMoving != axelMode.BUCKET){
            axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);
        }


        //GRAB
        if(gamepad2.a){
            axelMoving = axelMode.ABOVEGRAB;
            yClaw.setPosition(0.507);
            xClaw.setPosition(0.6);
            requireRetract = true;
        }

        if((gamepad2.x || gamepad1.b) && axelMoving == axelMode.ABOVEGRAB){
                axelMoving = axelMode.GRABSAMPLE;
                yClaw.setPosition(0.507);
        }


        //GRAB/SCORE SPECIMEN
        if(gamepad2.dpad_left){
            xClaw.setPosition(0.6);
            yClaw.setPosition(0.587);
            claw.setPosition(0.75);
            axelMoving = axelMode.GRABSPEC;
            requireRetract = true;
        }

        if(gamepad2.dpad_right && axelMoving == axelMode.GRABSPEC){
            yClaw.setPosition(0.22);
            xClaw.setPosition(0.6);
            axelMoving = axelMode.BAR;
        }


        //SCORE SAMPLE
        if(gamepad2.y){
            xClaw.setPosition(0.6);
            yClaw.setPosition(0.507);
            axelMoving = axelMode.BUCKET;
            requireRetract = true;
        }


        //HANG
        if(gamepad1.y && gamepad2.b){
            requireRetract = true;
            hangSeq = hangSequence.RESET;
            axelMoving = axelMode.HANG;
        }


        if(gamepad2.left_bumper){
            slideReset = true;
            slideTarget = -5000;
        }
        if(gamepad2.right_bumper && slideReset){
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            slideTarget = 0;

            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }


        if(requireRetract) {
            slideTarget = 0;
            if(slideMotor.getCurrentPosition() < slideMax * 0.1 && slideMotor2.getCurrentPosition() < slideMax * 0.1){
                requireRetract = false;
            }
        }else {
            switch (axelMoving) {
                case GRABSAMPLE:
                        axelPower = 0;
                    break;
                case ABOVEGRAB:
                    axelPower = 1;
                    axelTarget = 917;
                    break;
                case GRABSPEC:
                    axelPower = 1;
                    axelTarget = 813;
                    break;
                case BUCKET:
                    axelPower = 1;
                    axelTarget = 360;
                    break;
                case BAR:
                    axelPower = 1;
                    axelTarget = 292;
                    break;
                case HANG:
                    switch (hangSeq) {
                        case RESET:
                            axelTarget = 0;
                            slideTarget = 480;
                            if (axelMotor.getCurrentPosition() < 20 && slideMotor.getCurrentPosition() > 460 && slideMotor.getCurrentPosition() < 500) {
                                hangSeq = hangSequence.SLIDE_BAR1;
                            }
                            break;
                        case SLIDE_BAR1:
                            axelMotor.setPower(0.8);
                            axelMotor2.setPower(0.8);
                            axelTarget = 450;
                            if (axelMotor.getCurrentPosition() > 445) {
                                hangSeq = hangSequence.AXEL_BAR1;
                            }
                            break;
                        case AXEL_BAR1:
                            slideTarget = 0;
                            if (slideMotor.getCurrentPosition() < 20) {
                                hangSeq = hangSequence.SLIDE_PULL1;
                            }
                        case SLIDE_PULL1:
                            axelMotor.setPower(1);
                            axelMotor2.setPower(1);
                            axelTarget = 260;
                            if (axelMotor.getCurrentPosition() < 258) {
                                hangSeq = hangSequence.SLIDE_PULL2;
                            }
                            break;
                        case SLIDE_PULL2:
                            slideTarget = 340;
                            if (slideMotor.getCurrentPosition() > 230) {
                                hangSeq = hangSequence.AXEL_CLIP1;
                            }
                            break;
                        case AXEL_CLIP1:
                            slideTarget = 0;
                            axelTarget = 0;
                            break;
                    }
                    break;
                default:
                    axelPower = 1;
                    axelTarget = 0;
            }
        }


        if(gamepad1.left_trigger > 0.1) claw.setPosition(0.75);

        //Claw close
        if (gamepad1.right_trigger > 0.1) claw.setPosition(0);


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
        if(gamepad2.left_trigger > 0.1) yClaw.setPosition(0.507);

        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1) yClaw.setPosition(0.05);


        if(gamepad2.left_stick_y > 0.2){
            sweep.setPosition(0.23);
        }else{
            sweep.setPosition(0.84);
        }




        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.GRABSAMPLE || axelMoving == axelMode.ABOVEGRAB) {
                if (slideTarget < 1060) {
                   slideTarget += 25;
                }
            } else if(axelMoving == axelMode.BUCKET){
                axelMotor2.setDirection(DcMotorEx.Direction.FORWARD);
                slideTarget = slideMax;
            } else if(axelMoving == axelMode.BAR){
                slideTarget = 1250;
            }
        }
        //Manual slide detraction
        if (gamepad2.dpad_down) {
            if (slideMotor.getCurrentPosition() > 0) {
                slideTarget -= 50;
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