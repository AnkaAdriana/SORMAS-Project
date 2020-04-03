import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

WebUI.callTestCase(findTestCase('Login/partials/LoginAsSurveillanceSupervisor'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('SurveillanceSupervisor/partials/SwitchToCases'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/a_Search_Entry_link'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/input_Case_CaseIdUuid_inputBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_CasePerson_tab'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/input_Person_PassportNumber_inputBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Hospitalization_tab'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/div_Hospitation_Accommodation_selectBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Symptoms_tab'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/div_Symptoms_Comments_inputBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_EpidemiologicalData_tab'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/i_EpidemiologicalData_description_text'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Contacts_tab'))

WebUI.verifyElementPresent(findTestObject('Object Repository/Surveillance/CaseView/div_Contacts_NewContact_btn'), 3)

if (isStandalone) {
	WebUI.closeBrowser()
}
