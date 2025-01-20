package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp
public class PIDAxelTest extends RobotCore{
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    private final double ticks_in_degree = 700/90.0;

    private DcMotorEx axelMotor;
    private DcMotorEx axelMotor2;

    @Override
    public void init(){
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
        axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");

        axelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        axelMotor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);
    }

    @Override
    public void loop(){
        controller.setPID(p, i, d);
        int axelPos = axelMotor.getCurrentPosition();
        double pid = controller.calculate(axelPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        axelMotor.setPower(power);
        axelMotor2.setPower(power);

        telemetry.addData("pos", axelPos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}
