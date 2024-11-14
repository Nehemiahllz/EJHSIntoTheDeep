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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Vector;

@Autonomous(name = "Buckets", group = "Auto")

//Red Bucket Corner is -60,-60
//Blue Bucket Corner is 60,60

public class Buckets extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        //Change StartPos
        Pose2d start = new Pose2d(6,-50,Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        Axel axel = new Axel(hardwareMap);

        Grabber grabber = new Grabber(hardwareMap);


        //This top line will have the position the robot is currently in, but the bottom is where the robot will go
        //The bottom line can have as many lines as you want, but the last line will have the semi colon, not the others
        TrajectoryActionBuilder specimen = drive.actionBuilder(start)
                .splineToConstantHeading(new Vector2d(-6,-21), Math.toRadians(90));

        //This above is the first one, but most will look like the one bellow
        //The first line previous position must be Pose2d even though you prolly made it with a vector
        //The vector2d is the pose2d without any turning and you will just put the degrees your at
        //For the pose2d you will do the same as vector but the third parameter is the the position you want to end up in
        //For the pose2d fourth parameter, KEEP THE NUMBER AT 90, THIS IS THE TANGENT AND WE DONT KNOW WHAT IT DOES

        //EX based off right after the specimen sequence

        //TrajectoryActionBuilder specimen2PickUp = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
        //        .splineToLinearHeading(new Pose2d(-40, -20, Math.toRadians(270)), Math.toRadians(90))
        //        .splineToConstantHeading(new Vector2d(-40, 40), Math.toRadians(270));

        //First it is taking the position of the specimen movement
        //The next line moves the robot to -40, -20 and rotates it 180 degrees to be 270 degrees
        //The last line keeps the rotation the same but moves the robot forward to -40,40


        //VERY EXPIREMENTAL GUESTIMATION AUTO PATHS:
        //FIXING THE POSITIONS IN HERE SHOULD MAKE EVERYTHING WORK UNLESS IT DECIDES TO STOP IN THE MIDDLE AS IT DOES

        TrajectoryActionBuilder sample1PickUp = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
                //moves the robot away from the sub
                .splineToConstantHeading(new Vector2d(-6, -34), Math.toRadians(90))
                //slides the robot left to get even further
                .splineToConstantHeading(new Vector2d(-28, -34), Math.toRadians(90))
                //puts the robot in picking position
                .splineToConstantHeading(new Vector2d(-28, -26), Math.toRadians(90));

        TrajectoryActionBuilder sampleScore = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(90)))
                //twists the robot and moves it to be just before the bucket
                .splineToLinearHeading(new Pose2d(-31, -34, Math.toRadians(315)), Math.toRadians(90))
                //moves it closer to the bucket to simulate driving behavior of leaning the side against the bucket for scoring
                .splineToConstantHeading(new Vector2d(-33, -36), Math.toRadians(315));

        TrajectoryActionBuilder sample2PickUp = drive.actionBuilder(new Pose2d(-33, -36, Math.toRadians(315)))
                //twists the robot straight and puts it into position to pick sample 2
                .splineToLinearHeading(new Pose2d(-43, -26, Math.toRadians(90)), Math.toRadians(90));

        TrajectoryActionBuilder sample3PickUp = drive.actionBuilder(new Pose2d(-33, -36, Math.toRadians(315)))
                //twists the robot be parallel to the sample in order to grab it from the side with a rotated claw, and puts it into pciking position
                .splineToLinearHeading(new Pose2d(-49, -13, Math.toRadians(360)), Math.toRadians(90));


        TrajectoryActionBuilder park = drive.actionBuilder(new Pose2d(-6, -21, Math.toRadians(315)))
                //parks
                .splineToLinearHeading(new Pose2d(40, -32, Math.toRadians(360)), Math.toRadians(90));


        //Just closes the claw onto the specimen
        grabber.setClawPosition(0.32, 0.8644, 0.3683);

        //Where it starts when you press go, before this is basically init
        waitForStart();
        if(isStopRequested()) return;

        //The actual running stuff:
        Actions.runBlocking(
                new SequentialAction(
                        //Also closing the claw because i have anxiety
                        grabber.setClawPosition(0.32, 0.8644, 0.3683),

                        //Put everything in here
                        //Put commas between each action
                        //grabber.setClawPosition(clawPos, yClawPos, xClawPos) CHANGES THE CLAW
                        //slide.setSlidePosition(TargetPos) CHANGES THE SLIDE
                        //axel.setAxelPosition(TargetPos, Power) CHANGES THE AXEL - negative power is back and vise versa
                        //The final action doesnt have a comma, and watch out for parenthasese cause there are about 50 of em

                        //If you want to run things at the same time use:
                        //new ParallelAction() AND THE STUFF THAT YOU WANT TO RUN SIMULTANEOUSLY WILL GO INSIDE THE ()

                        //To call you movement stuff just go:
                        //name.build() AND NAME WILL BE WHATEVER THE TRAJECTORY SEQUENCE IS NAMED WHERE YOU MADE IT


                        //VERY EXPIREMENTAL GUESTIMATION AUTO RUN:

                        //scores the first specimen - THIS IS THE SAME FROM BARS AUTO
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

                        //Sequence to pick up the first sample
                        sample1PickUp.build(),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),
                        axel.setAxelPosition(300, 0.1),
                        new SleepAction(0.2),
                        grabber.setClawPosition(0.32, 0.8644, 0.3683),
                        new SleepAction(0.2),
                        axel.setAxelPosition(0, -0.5),

                        //sequence to score the first sample
                        new ParallelAction(
                            sampleScore.build(),
                                axel.setAxelPosition(0,-0.7),
                                slide.setSlidePosition(3020),
                                grabber.setClawPosition(0.32, 0.8644, 1)
                        ),
                        grabber.setClawPosition(0.32, 0.4406, 1),
                        new SleepAction(0.4),
                        grabber.setClawPosition(0.6, 0.4406, 1),
                        new SleepAction(0.3),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),

                        //sequence to pick up the second sample and finish reset from scoring
                        new ParallelAction(
                                axel.setAxelPosition(0, -0.7),
                                slide.setSlidePosition(0),
                                grabber.setClawPosition(0.6, 0.8644, 0.3683),
                                sample2PickUp.build()
                        ),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),
                        axel.setAxelPosition(300, 0.1),
                        new SleepAction(0.2),
                        grabber.setClawPosition(0.32, 0.8644, 0.3683),
                        new SleepAction(0.2),
                        axel.setAxelPosition(0, -0.5),

                        //sequence to score the second sample
                        new ParallelAction(
                                sampleScore.build(),
                                axel.setAxelPosition(0,-0.7),
                                slide.setSlidePosition(3020),
                                grabber.setClawPosition(0.32, 0.8644, 1)
                        ),
                        grabber.setClawPosition(0.32, 0.4406, 1),
                        new SleepAction(0.4),
                        grabber.setClawPosition(0.6, 0.4406, 1),
                        new SleepAction(0.3),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),

                        //sequence to pick up the third sample and finish reset from scoring
                        new ParallelAction(
                                axel.setAxelPosition(0, -0.7),
                                slide.setSlidePosition(0),
                                grabber.setClawPosition(0.6, 0.8644, 0.68415),
                                sample3PickUp.build()
                        ),
                        grabber.setClawPosition(0.6, 0.8644, 0.68415),
                        axel.setAxelPosition(300, 0.1),
                        new SleepAction(0.2),
                        grabber.setClawPosition(0.32, 0.8644, 0.68415),
                        new SleepAction(0.2),
                        axel.setAxelPosition(0, -0.5),

                        //sequence to score the third sample

                        new ParallelAction(
                                sampleScore.build(),
                                axel.setAxelPosition(0,-0.7),
                                slide.setSlidePosition(3020),
                                grabber.setClawPosition(0.32, 0.8644, 1)
                        ),
                        grabber.setClawPosition(0.32, 0.4406, 1),
                        new SleepAction(0.4),
                        grabber.setClawPosition(0.6, 0.4406, 1),
                        new SleepAction(0.3),
                        grabber.setClawPosition(0.6, 0.8644, 0.3683),

                        //sequence to park and finish reset from scoring
                        new ParallelAction(
                                axel.setAxelPosition(0, -0.7),
                                slide.setSlidePosition(0),
                                grabber.setClawPosition(0.6, 0.8644, 0.68415),
                                park.build()
                        )
                        )
        );

    }



    //DONT MESS WITH ANYTHING BELLOW HERE - Unless your Jacob and tryna fix something :)

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