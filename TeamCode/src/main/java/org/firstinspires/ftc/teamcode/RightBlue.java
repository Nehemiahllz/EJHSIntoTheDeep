package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "BlueRight", group = "Blue")
public class RightBlue extends LinearOpMode {
   Servo horizontal;
   @Override
    public void runOpMode() {
       horizontal = hardwareMap.get(Servo.class, "horizontal");
       horizontal.setDirection(Servo.Direction.REVERSE);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
            TrajectorySequence rightBlue = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                    .addTemporalMarker(() -> {
                        horizontal.setPosition(0);
                    })
                    .waitSeconds(1)
                    .strafeRight(30)
                    .build();

            waitForStart();
            drive.followTrajectorySequence(rightBlue);
    }
}
