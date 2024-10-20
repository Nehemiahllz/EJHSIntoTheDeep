package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OdometryPodComputer.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

import java.security.interfaces.RSAMultiPrimePrivateCrtKey;

@Autonomous(name = "PinPointTest", group = "Testing")
public class PinPoint_Test_Auto extends LinearOpMode {

    public void runOpMode()
    {
        GoBildaPinpointDriver computer = hardwareMap.get(GoBildaPinpointDriver.class, "Computer");
        computer.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        computer.setOffsets(-158.75, -190.5);
        computer.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        computer.resetPosAndIMU();
        computer.update();

        Servo horizontal = hardwareMap.get(Servo.class, "horizontal");
        horizontal.setDirection(Servo.Direction.REVERSE);




        Pose2d startPose = new Pose2d(0,0,Math.toRadians(0));
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startPose);
        TrajectorySequence testTraj = drive.trajectorySequenceBuilder(startPose)
                .addTemporalMarker( () -> {
                    horizontal.setPosition(0);
    })
                .waitSeconds(2)
                .forward(20)
               // .lineToLinearHeading(new Pose2d(30,10,Math.toRadians(180)))
                .build();

        waitForStart();
        drive.followTrajectorySequence(testTraj);


    }

}
