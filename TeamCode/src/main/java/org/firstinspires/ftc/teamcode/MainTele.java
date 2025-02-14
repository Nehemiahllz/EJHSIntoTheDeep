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

    int slideMax = 2290;

    boolean requireRetract;

    public enum axelMode {ABOVEGRAB, GRABSAMPLE, BUCKET, HANG, GRABSPEC, BAR, START,RESET}
    axelMode axelMoving;

    public enum hangSequence {SETUP, AXEL_BAR1, SLIDE_BAR1, SLIDE_PULL1, AXEL_ROTATE}
    hangSequence hangSeq;


    boolean activeDrivetrain = true;

    ElapsedTime clawTimer = new ElapsedTime();
    ElapsedTime runTime = new ElapsedTime();


    int xClawRotate = 0;


    private PIDController controller;

    private final double ticks_in_degree = 700/180.0;

    private static double p = 0.005, i = 0, d = 0.00015;
    private static double f = 0.0025;

    private static int target = 0;

    private PIDController controllerS;

    public static double pS = 0.0049, iS = 0, dS = 0.00015;
    public static double fS = 0.0005;

    public static int targetS = 0;

    private final double ticks_in_degreeS = 700/180.0;


    Gamepad.RumbleEffect endgame;
    Gamepad.RumbleEffect hangTime;
    Gamepad.RumbleEffect sampleRumble;

    Gamepad.RumbleEffect yClawTilt;

    boolean endgameRumbled = false;
    boolean hangRumbled = false;

    boolean runTimeReset = false;

    boolean aboveSample = false;

    boolean axelReset = false;
    boolean axelWrong = false;


    public void init() {
        super.init();

        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        controllerS = new PIDController(pS, iS, dS);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        axelMotor.setPower(0);
        axelMotor2.setPower(0);

        axelMoving = axelMode.START;

        slideMotor.setPower(0);
        slideMotor2.setPower(0);

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        axelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        printDebugData();
    }

    public void loop() {
        printDebugData();
        rumbles();
        drivetrain();


if(axelMoving != axelMode.GRABSAMPLE && axelMoving != axelMode.GRABSPEC) {
    controller.setPID(p, i, d);
    int axelPos = axelMotor.getCurrentPosition();
    double pid = controller.calculate(axelPos, target);
    double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

    double power = pid + ff;

    axelMotor.setPower(power);
    axelMotor2.setPower(power);

    telemetry.addData("pos", axelPos);
    telemetry.addData("target", target);
    telemetry.addData("power", power);
}


        controllerS.setPID(pS, iS, dS);
        int slidePos = slideMotor.getCurrentPosition();
        double pidS = controllerS.calculate(slidePos, targetS);
        double ffS = Math.cos(Math.toRadians(targetS / ticks_in_degreeS)) * fS;

        double powerS = pidS + ffS;

        slideMotor.setPower(powerS);
        slideMotor2.setPower(powerS);


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
            yClaw.setPosition(0.807);
        }


        //GRAB/SCORE SPECIMEN
        if (gamepad2.dpad_left) {
            xClawRotate = 0;
            xClaw.setPosition(0.745);
            yClaw.setPosition(0.68);
            axelMoving = axelMode.GRABSPEC;
            requireRetract = true;

        }

        if (gamepad2.dpad_right && axelMoving == axelMode.GRABSPEC) {
            yClaw.setPosition(0);
            xClaw.setPosition(0.745);
            axelMoving = axelMode.BAR;
        }


        //SCORE SAMPLE
        if (gamepad2.y) {
            requireRetract = true;
            xClawRotate = 0;
            xClaw.setPosition(0.745);
            yClaw.setPosition(0.267);
            axelReset = true;
            axelMoving = axelMode.BUCKET;
        }


        //HANG
        if (gamepad1.y) {
            requireRetract = true;
            hangSeq = hangSequence.SETUP;
            axelMoving = axelMode.HANG;
        }


        if (gamepad2.left_bumper) {
            axelWrong = true;
        }


        if (requireRetract) {
            if(axelMoving == axelMode.GRABSPEC){
                targetS = 100;
            }else {
                targetS = 0;
            }
            if (slideMotor.getCurrentPosition() < slideMax * 0.1 && slideMotor2.getCurrentPosition() < slideMax * 0.1) {
                requireRetract = false;
            }
        } else {
            switch (axelMoving) {
                case GRABSAMPLE:
                    target = 721;
                    axelMotor.setPower(0);
                    axelMotor2.setPower(0);
                    break;
                case ABOVEGRAB:
                    target = 520;
                    break;
                case GRABSPEC:
                    target = 721;
                    axelMotor.setPower(0);
                    axelMotor2.setPower(0);
                    break;
                case BUCKET:

                    if (axelReset && axelMotor.getCurrentPosition() < 10 && slideMotor.getCurrentPosition() > 400) {
                        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                        axelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        axelMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                        axelReset = false;
                        axelWrong = false;
                    }

                    if (axelWrong) {
                        target = -200;
                    } else {
                        target = -5;
                    }
                    break;
                case BAR:

                    target = -5;
                    break;
                case HANG:
                    switch (hangSeq) {
                        case SETUP:
                            target = 133;
                            stopper.setPosition(0.08);

                            if (axelMotor.getCurrentPosition() < 140 && axelMotor.getCurrentPosition() > 127) {
                                hangSeq = hangSequence.SLIDE_BAR1;
                            }
                            break;

                        case SLIDE_BAR1:
                            targetS = 1055;
                            if (slideMotor.getCurrentPosition() > 1020) {
                                hangSeq = hangSequence.AXEL_BAR1;
                            }
                            break;

                        case AXEL_BAR1:
                            target = 241;

                            if (axelMotor.getCurrentPosition() > 232) {
                                hangSeq = hangSequence.SLIDE_PULL1;
                            }
                            break;

                        case SLIDE_PULL1:

                            targetS = 0;

                            activeDrivetrain = false;
                            leftBack.setPower(1);
                            rightBack.setPower(1);
                            leftFront.setPower(1);
                            rightFront.setPower(1);

                            stopper.getController().pwmDisable();
                            claw.getController().pwmDisable();

                            if (slideMotor.getCurrentPosition() < 750) {
                                target = 120;
                            }


                            if (slideMotor.getCurrentPosition() < 310 && slideMotor2.getCurrentPosition() < 310) {
                                hangSeq = hangSequence.AXEL_ROTATE;
                            }
                            break;

                        case AXEL_ROTATE:
                            target = 0;

                            activeDrivetrain = true;
                            break;

                        default:
                            target = 0;
                            targetS = 0;
                    }
                    break;
                default:
                    target = 0;
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
        if(gamepad2.left_trigger > 0.1) yClaw.setPosition(0.807);

        //Claw tilt into bucket
        if(gamepad2.right_trigger > 0.1) yClaw.setPosition(0.2678);


        if(gamepad1.left_stick_button){
            sweep.setPosition(0.23);
        }else{
            sweep.setPosition(0.84);
        }




        //Manual slide extension with limits
        if (gamepad2.dpad_up) {
            if (axelMoving == axelMode.GRABSAMPLE || axelMoving == axelMode.ABOVEGRAB) {
                if (targetS < 800) {
                    targetS += 25;
                }
            } else if(axelMoving == axelMode.BUCKET){
                targetS = slideMax;
            } else if(axelMoving == axelMode.BAR){
                targetS = 1380;
            } else if(axelMoving == axelMode.HANG){
                targetS = 1400;
            }
        }
        //Manual slide detraction
        if (gamepad2.dpad_down) {
            if (slideMotor.getCurrentPosition() > 0) {
                if(axelMoving == axelMode.HANG){
                    targetS = 0;
                }else {
                    targetS -= 50;
                }
            }
        }

    }

    //Show data on the driver hub
    private void printDebugData() {
        telemetry.addLine("----Behind the Scenes----");
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("Slide2", slideMotor2.getCurrentPosition());
        telemetry.addData("slideTarget", targetS);
        telemetry.addLine(" ");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("axelTarget", target);
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