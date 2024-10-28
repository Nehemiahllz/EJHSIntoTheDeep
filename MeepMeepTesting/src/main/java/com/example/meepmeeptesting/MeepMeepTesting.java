package com.example.meepmeeptesting;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        System.setProperty("sun.java2d.opengl", "true");
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, 60, Math.toRadians(-90)))
                //Specimen that thang!
                .lineToYSplineHeading(36, Math.toRadians(-90))
                .waitSeconds(1)
                //We Are Number 1 (grab that thang!)
                .strafeTo(new Vector2d(50, 40))
                //.splineToSplineHeading(new Pose2d(35, 25, Math.toRadians(0)), -90)
                .waitSeconds(0.5)
                //Place that thang!
                .splineToSplineHeading(new Pose2d(52, 52, Math.toRadians(45)), 90)
                .waitSeconds(2)
                //Lowkey Gotta Take a Numba 2 (grab that thang!)
                .splineTo(new Vector2d(58, 35), Math.toRadians(-90))
                .waitSeconds(0.5)
                //Place that thang!
                .splineToSplineHeading(new Pose2d(52, 52, Math.toRadians(45)), 90)
                .waitSeconds(2)
                //Is that Number 3 I see? (grab that thang!)
                .splineToSplineHeading(new Pose2d(55, 27, Math.toRadians(0)), -90)
                .waitSeconds(0.5)
                //Place that thang!
                .splineToSplineHeading(new Pose2d(52, 52, Math.toRadians(45)), 90)
                .waitSeconds(2)
                //Park the bus...
                .splineToSplineHeading(new Pose2d(25, 10, Math.toRadians(180)), 90)
                .waitSeconds(2)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

