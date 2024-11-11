package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.MecanumDrive;


import java.util.concurrent.TimeUnit;

@Autonomous(name = "Buckets", group = "Auto")

public class Auto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        Pose2d start = new Pose2d(0,0,Math.toRadians(90));

        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        Slide slide = new Slide(hardwareMap);

        TrajectoryActionBuilder starter = drive.actionBuilder(start)
                .splineToLinearHeading(new Pose2d(0,0, Math.toRadians(180)), Math.toRadians(90));




        waitForStart();
        if(isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        starter.build(),
                        new SleepAction(2),
                        slide.setSlidePosition(500)

                )
        );
    }




    public class Slide
    {
        DcMotorEx axel1;
        DcMotorEx axel2;
        DcMotorEx slide;
        public Slide(HardwareMap hardwareMap)
        {
            axel1 = hardwareMap.get(DcMotorEx.class, "axel1");
            axel2 = hardwareMap.get(DcMotorEx.class, "axel2");
            slide = hardwareMap.get(DcMotorEx.class, "slide");

            axel1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            axel2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            slide.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        public class SetSlidePosition implements Action{
            int target;
            boolean under;
            public SetSlidePosition(int tar)
            {
                target = tar;
            }

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if(slide.getCurrentPosition() < target)
                {
                    under = true;
                    while(under)
                    {
                        if(slide.getCurrentPosition() >= target)
                            under = false;

                        slide.setPower(1);
                    }

                    slide.setPower(0);
                    return false;
                }
                else
                {
                    under = false;
                    while(!under)
                    {
                        if(slide.getCurrentPosition() >= target)
                            under = true;
                        slide.setPower(-1);
                    }
                    slide.setPower(0);
                    return false;
                }
            }
        }
        public Action setSlidePosition(int tar){ return new SetSlidePosition(tar);}

    }



}