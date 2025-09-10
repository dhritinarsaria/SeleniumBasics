package testlistener;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class MyTestListener implements ITestListener {
	public void onTestStart(ITestResult result) {
		System.out.println(result.getName());
	}

	public void onTestSuccess(ITestResult result) {
		System.out.println("Test case passed: "+result.getStatus());
	}

	public void onTestFailure(ITestResult result) {
		System.out.println("Test case failed: "+ result.getStatus());
	}

	public void onTestSkipped(ITestResult result) {
		System.out.println("Test case skipped: "+result.getStatus());
	}
}
