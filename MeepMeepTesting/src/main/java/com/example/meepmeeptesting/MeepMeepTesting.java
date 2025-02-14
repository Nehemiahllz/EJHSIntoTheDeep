package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();


        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-26, -7, Math.toRadians(0)))

//                .strafeToLinearHeading(new Vector2d(-54, -53.4), Math.toRadians(45))
                                .splineToLinearHeading(new Pose2d(-54, -53.4, Math.toRadians(45)), Math.toRadians(90))




//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(24, -63, Math.toRadians(90)))
                //Push + close grab = 27 sec - TOO SLOW
//                .strafeTo(new Vector2d(40,-7))
//                .strafeTo(new Vector2d(47.5, -7))
//                .strafeTo(new Vector2d(47.5, -27))
//
//                .strafeTo(new Vector2d(47.5,-7))
//                .strafeTo(new Vector2d(57.5, -7))
//                .strafeTo(new Vector2d(57.5, -27))
//
//                .strafeToLinearHeading(new Vector2d(58, -5), Math.toRadians(0))
//                .strafeTo(new Vector2d(58, -57))
//
//                .strafeToLinearHeading(new Vector2d(35, -59), Math.toRadians(270))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))



                //Push + far grab = 23.5 - MAYBE, SCARILY SLOW
//                .strafeTo(new Vector2d(40,-7))
//                .strafeTo(new Vector2d(47.5, -7))
//                .strafeTo(new Vector2d(47.5, -27))
//
//                .strafeTo(new Vector2d(47.5,-7))
//                .strafeTo(new Vector2d(57.5, -7))
//                .strafeTo(new Vector2d(57.5, -27))
//
//                .strafeToLinearHeading(new Vector2d(58, -5), Math.toRadians(0))
//                .strafeTo(new Vector2d(58, -57))
//
//                .strafeToLinearHeading(new Vector2d(35, -59), Math.toRadians(270))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))



                //Rotate w/ push 3 + far grab = 20.5, VERY FAST, but sketchy
//                .strafeToLinearHeading(new Vector2d(37, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(46.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(47, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(47.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(58, -5), Math.toRadians(0))
//                .strafeTo(new Vector2d(58, -57))
//
//                .strafeToLinearHeading(new Vector2d(35, -59), Math.toRadians(270))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))



                //Rotate + far grab = 18.4, LIGHTNING FAST, extremely rotate based _
                //WINNER WINNER, CHICKEN DINNER

//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(24, -63, Math.toRadians(90)))
//                .strafeToLinearHeading(new Vector2d(37, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(46.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(47, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(56.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(57, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(37, -59), Math.toRadians(270))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))



//                //Start score + rotate + far grab = 18.02, LIGHTNING FAST, extremely rotate based
//                        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -63, Math.toRadians(270)))
//                .strafeTo(new Vector2d(9, -33))
//                .strafeTo(new Vector2d(9, -36))
//
//                .strafeToLinearHeading(new Vector2d(37, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(46.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(47, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(56.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(57, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(37, -59), Math.toRadians(270))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))



                //Long Rotate + far grab = 19.3, VERY FAST, SKETCHIEST
//                .strafeToLinearHeading(new Vector2d(47, -41), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(56.5, -41), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(57, -41), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(56.5, -39), Math.toRadians(300))
//
//                .strafeToLinearHeading(new Vector2d(57, -39), Math.toRadians(60))
//                .strafeToLinearHeading(new Vector2d(37, -59), Math.toRadians(270))
//
//                .strafeTo(new Vector2d(35, -59))
//                .strafeTo(new Vector2d(2, -32))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))
//
//                .strafeToLinearHeading(new Vector2d(20, -47), Math.toRadians(315))
//                .strafeToLinearHeading(new Vector2d(2, -32), Math.toRadians(270))


//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-10, -63, Math.toRadians(270)))
//                .strafeTo(new Vector2d(-10, -33))
//                .strafeTo(new Vector2d(-10, -37))
//
//                .strafeToLinearHeading(new Vector2d(-48, -39), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
//
//                .strafeToLinearHeading(new Vector2d(-58, -39), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
//
//                .strafeToLinearHeading(new Vector2d(-53, -25), Math.toRadians(180))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))

//
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-38, -63, Math.toRadians(90)))
//
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
//
//                .strafeToLinearHeading(new Vector2d(-48, -39), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
//
//                .strafeToLinearHeading(new Vector2d(-58, -39), Math.toRadians(90))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
//
//                .strafeToLinearHeading(new Vector2d(-53, -25), Math.toRadians(180))
//                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))


                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}