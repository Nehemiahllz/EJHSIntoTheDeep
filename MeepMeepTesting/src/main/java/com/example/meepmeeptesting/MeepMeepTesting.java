package com.example.meepmeeptesting;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        System.setProperty("sun.java2d.opengl", "true");
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(80, 80, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

//        //Samples Without Turn
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -61, Math.toRadians(90)))
//                //Place Specimen
//                .lineToY(-36)
//                .waitSeconds(.5)
//                .strafeTo(new Vector2d(35,-42))
//                .strafeTo(new Vector2d(40, -5))
//                //Grab and Deposit Sample 1
//                .strafeTo(new Vector2d(48,-5))
//                .strafeTo(new Vector2d(48,-55))
//                //Grab and Deposit Sample 2
//                .strafeTo(new Vector2d(48,-5))
//                .strafeTo(new Vector2d(60,-5))
//                //.strafeTo(new Vector2d(55,-40))
//
//                //Grab Specimen 1
//                .strafeTo(new Vector2d(48, -57))
//                .waitSeconds(.5)
//                .strafeTo(new Vector2d(12,-45))
//                .strafeTo(new Vector2d(12, -36))
//                .waitSeconds(.5)
//
//
//                //Grab Specimen 2
//                .strafeTo(new Vector2d(48,-40))
//                .strafeTo(new Vector2d(48, -57))
//                .waitSeconds(.5)
//                .strafeTo(new Vector2d(12,-45))
//                .strafeTo(new Vector2d(12, -36))
//                .waitSeconds(.5)
//
//                //Sample 3
//                                .strafeTo(new Vector2d(25, -45))
//                                .strafeToLinearHeading(new Vector2d(60, -25), Math.toRadians(180))
//                                .strafeTo(new Vector2d(60, -60))
//
//                //Specimen 3
//                                .strafeToLinearHeading(new Vector2d(48, -40), Math.toRadians(90))
//                                .strafeTo(new Vector2d(48,-57))
//                .waitSeconds(.5)
//                .strafeTo(new Vector2d(12,-45))
//                .strafeTo(new Vector2d(12, -36))
//                .waitSeconds(2)
//                                .strafeTo(new Vector2d(12,-37))
////
//                .build());
//

                //Samples With Turn
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -61, Math.toRadians(90)))
                //Place Specimen
                .lineToY(-36)
                        .waitSeconds(.5)
                                .strafeTo(new Vector2d(35,-42))
                                .strafeTo(new Vector2d(40, -5))
                //Grab and Deposit Sample 1
                        .strafeTo(new Vector2d(48,-5))
                                .strafeTo(new Vector2d(48,-55))
                //Grab and Deposit Sample 2
                .strafeTo(new Vector2d(43,-5))
                        .strafeTo(new Vector2d(43,-5))
                .strafeTo(new Vector2d(63,-5))
                .strafeTo(new Vector2d(48,-40))

                //Grab Specimen 1
                                .strafeToLinearHeading(new Vector2d(48, -57),Math.toRadians(-90))
                        .waitSeconds(1)
                                .strafeToLinearHeading(new Vector2d(12,-45), Math.toRadians(90))
                                .strafeTo(new Vector2d(12, -36))
                                .waitSeconds(.5)


                //Grab Specimen 2
                                .strafeToLinearHeading(new Vector2d(48,-40),Math.toRadians(-90))
                .strafeTo(new Vector2d(48, -57))
                        .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(12,-45), Math.toRadians(90))
                .strafeTo(new Vector2d(12, -36))
                                .waitSeconds(.5)
                //Sample 3
//                        .splineToLinearHeading(new Pose2d(60,-25,Math.toRadians(180)), Math.toRadians(90))
                                .strafeTo(new Vector2d(25, -45))
                                .strafeToLinearHeading(new Vector2d(60, -25), Math.toRadians(180))
                                .strafeTo(new Vector2d(60, -60))

                //Specimen 3
                                .strafeToLinearHeading(new Vector2d(48, -40), Math.toRadians(-90))
                                .strafeTo(new Vector2d(48,-57))
                .waitSeconds(.5)
                .strafeToLinearHeading(new Vector2d(12,-45),Math.toRadians(90))
                .strafeTo(new Vector2d(12, -36))
                .waitSeconds(2)
                                .strafeTo(new Vector2d(12,-37))

                //Park
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

