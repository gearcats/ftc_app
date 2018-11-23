package org.firstinspires.ftc.teamcode.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class AarreTestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(JunitTestSuite.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure);
		}

		System.out.println(result.wasSuccessful());
	}
}