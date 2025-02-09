package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.ejml.dense.row.decomposition.eig.watched.WatchedDoubleStepQREigenvector_FDRM;

import java.util.Vector;

@TeleOp(name = "TestTele", group = "Test")

public class Testing extends RobotCore {


    double moveX;
    double moveY;
    double turnX;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;


    Vector<Double> area = new Vector<>();
    double finalArea;

    boolean john = true;


    int axelPos;
    //Fix values, once measured
    final double grabHeight = 76.2;
    final double ticksPerSlideMM = 4.468;
    final double ticksPerDegree = 3.877;
    final double axelPosLowest = 409;
    double slideLength;
    final double slideLengthZero = 317.5;
    double axelAngle;

    private Limelight3A limelight;


    int count = 0;
    int maxCount = 0;
    int maxLocation = 0;

    double finalYAngle = 0;


    public void init() {
        super.init();
        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        axelMotor.setTargetPosition(0);
        axelMotor2.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        axelMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setTargetPosition(0);
        slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        axelMotor.setPower(1);
//        axelMotor2.setPower(1);
        slideMotor.setPower(1);
        slideMotor2.setPower(1);

        axelMotor.setPower(1);
        axelMotor2.setPower(1);

        printDebugData();


            limelight = hardwareMap.get(Limelight3A .class, "limelight");

            telemetry.setMsTransmissionInterval(11);

            limelight.pipelineSwitch(0);

            limelight.start();
    }

    public void loop() {
        LLResult result = limelight.getLatestResult();

        double tx = result.getTx(); // How far left or right the target is (degrees)
        double ty = result.getTy(); // How far up or down the target is (degrees)
        double ta = result.getTa(); //Area it takes up on the screen (percent)

        if(gamepad1.a){
            axelMotor.setTargetPosition(axelMotor.getTargetPosition() - 3);
            axelMotor2.setTargetPosition(axelMotor.getTargetPosition());
        }

        if(gamepad1.b){
            axelMotor.setTargetPosition(axelMotor.getTargetPosition() + 3);
            axelMotor2.setTargetPosition(axelMotor.getTargetPosition());
        }



        if(gamepad1.x){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() + 70);
            slideMotor2.setTargetPosition(slideMotor.getCurrentPosition() + 70);
        }

        if(gamepad1.y){
            slideMotor.setTargetPosition(slideMotor.getCurrentPosition() - 70);
            slideMotor2.setTargetPosition(slideMotor.getCurrentPosition() - 70);
        }

        if(gamepad1.left_bumper){
            slideMotor.setTargetPosition(0);
            slideMotor2.setTargetPosition(0);
        }

        if(gamepad1.right_bumper){
            slideMotor.setTargetPosition(4120);
            slideMotor2 .setTargetPosition(4120);
        }



        if(gamepad2.dpad_up){
            claw.setPosition(claw.getPosition() + 0.02);
        }
        if(gamepad2.dpad_down){
            claw.setPosition(claw.getPosition() - 0.02);
        }

        if(gamepad2.dpad_left){
            yClaw.setPosition(yClaw.getPosition() - 0.002);
        }
        if(gamepad2.dpad_right){
            yClaw.setPosition(yClaw.getPosition() + 0.002);
        }

        if(gamepad2.a){
            xClaw.setPosition(xClaw.getPosition() + 0.002);
        }
        if(gamepad2.b){
            xClaw.setPosition(xClaw.getPosition() - 0.002);
        }


        if(gamepad2.y){
            sweep.setPosition(sweep.getPosition() + 0.002);
        }
        if(gamepad2.x){
            sweep.setPosition(sweep.getPosition() - 0.002);
        }

        if(gamepad2.left_bumper){
            stopper.setPosition(stopper.getPosition() - 0.002);
        }

        if(gamepad2.right_bumper){
            stopper.setPosition(stopper.getPosition() + 0.002);
        }



        if(john) {
                area.clear();
                    for (int h = 0; h <= 999; h++) {
                        area.add(ta);
                    }

                    maxCount = 0;
                    for (int j = 0; j <= 999; j++) {
                        count = 0;
                        for (int k = 0; k <= 999; k++) {
                            if (area.get(j) == area.get(k)) {
                                count++;
                            }
                        }
                        if (count > maxCount) {
                            maxLocation = j;
                            maxCount = count;
                        }
                    }
                    telemetry.addLine("DONE!");
                    finalArea = area.get(maxLocation);

            john = false;
        }

        if(finalArea < 0.00999){
            john = true;
        }


        printDebugData();
        telemetry.addData("y", finalArea);

    }

    //Prints different info for debugging
    private void printDebugData() {
        telemetry.addLine("----Controller Inputs----");
        telemetry.addData("Axel", axelMotor.getCurrentPosition());
        telemetry.addData("Axel2", axelMotor2.getCurrentPosition());
        telemetry.addData("Slide", slideMotor.getCurrentPosition());
        telemetry.addData("claw",  claw.getPosition());
        telemetry.addData("yCLaw", yClaw.getPosition());
        telemetry.addData("xClaw", xClaw.getPosition());
        telemetry.addData("axelPos", axelPos);
        telemetry.addData("sweepPos", sweep.getPosition());
        telemetry.addData("stopperPos", stopper.getPosition());
        telemetry.addLine("_-_-_-_-_-_-_-");
    }

    private int findAxelPos() {
        slideLength = (slideMotor.getCurrentPosition() * ticksPerSlideMM) + slideLengthZero;
        axelAngle = Math.toDegrees(Math.asin(grabHeight/slideLength));
        axelPos = (int) (axelPosLowest - (axelAngle * ticksPerDegree));
        return axelPos;
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

