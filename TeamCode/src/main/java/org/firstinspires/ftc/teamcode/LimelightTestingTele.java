package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class LimelightTestingTele extends RobotCore {

    private Limelight3A limelight;

    double speed = 0;

    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A .class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);

        limelight.start();

        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop(){
        LLResult result = limelight.getLatestResult();

        double tx = result.getTx(); // How far left or right the target is (degrees)
        double ty = result.getTy(); // How far up or down the target is (degrees)
        double ta = result.getTa();

        leftBack.setPower(-speed);
        rightBack.setPower(speed);
        leftFront.setPower(speed);
        rightFront.setPower(-speed);

        if(tx > -4.5 && tx < -1.5){
            speed = 0;
        }else{
            if(tx <= -4.5){
                speed = 0.2;
            }else if(tx >= -1.5){
                speed = -0.2;
            }
        }


    }
}
