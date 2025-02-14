package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp
public class PIDAxelTest extends OpMode {
    private PIDController controller;

    public static double p = 0.005, i = 0, d = 0.00015;
    public static double f = 0.0025;

    public static int target = 0;

    private final double ticks_in_degree = 700/180.0;

    private DcMotorEx axelMotor;
    private DcMotorEx axelMotor2;


    private PIDController controllerS;

    public static double pS = 0.0049, iS = 0, dS = 0.00015;
    public static double fS = 0.0005;

    public static int targetS = 0;

    private final double ticks_in_degreeS = 700/180.0;

    private DcMotorEx slideMotor;
    private DcMotorEx slideMotor2;


    @Override
    public void init(){
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        axelMotor = hardwareMap.get(DcMotorEx.class, "axelMotor");
       axelMotor2 = hardwareMap.get(DcMotorEx.class, "axelMotor2");

        axelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        axelMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        axelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
       axelMotor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        axelMotor2.setDirection(DcMotorEx.Direction.REVERSE);
        axelMotor.setDirection(DcMotorEx.Direction.FORWARD);



        controllerS = new PIDController(pS, iS, dS);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
        slideMotor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        slideMotor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        slideMotor2.setDirection(DcMotorEx.Direction.REVERSE);
        slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
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
        telemetry.addData("power", power);



        controllerS.setPID(pS, iS, dS);
        int slidePos = slideMotor.getCurrentPosition();
        double pidS = controllerS.calculate(slidePos, targetS);
        double ffS = Math.cos(Math.toRadians(targetS / ticks_in_degreeS)) * fS;

        double powerS = pidS + ffS;

        slideMotor.setPower(powerS);
        slideMotor2.setPower(powerS);

        telemetry.addData("posS", slidePos);
        telemetry.addData("targetS", targetS);
        telemetry.addData("powerS", powerS);

        telemetry.update();
    }
}
