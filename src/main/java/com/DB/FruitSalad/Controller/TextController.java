package com.DB.FruitSalad.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.DB.FruitSalad.AppConstants;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.cx.v3.DetectIntentRequest;
import com.google.cloud.dialogflow.cx.v3.DetectIntentResponse;
import com.google.cloud.dialogflow.cx.v3.Match;
import com.google.cloud.dialogflow.cx.v3.Match.MatchType;
import com.google.cloud.dialogflow.cx.v3.MatchIntentRequest;
import com.google.cloud.dialogflow.cx.v3.MatchIntentResponse;
import com.google.cloud.dialogflow.cx.v3.QueryInput;
import com.google.cloud.dialogflow.cx.v3.QueryResult;
import com.google.cloud.dialogflow.cx.v3.SessionName;
import com.google.cloud.dialogflow.cx.v3.SessionsClient;
import com.google.cloud.dialogflow.cx.v3.SessionsSettings;
import com.google.cloud.dialogflow.cx.v3.TextInput;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TextController {
	private static Logger log = Logger.getAnonymousLogger();

	@PostMapping("/textTesting")
	public ResponseEntity<Map<String, String>> textTest(@RequestParam("navigationPageName") String navigationPageName,
			@RequestParam("sessionId") String sessionId) {
		Map<String, String> responseMap = new HashMap<>();
		try {
			SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
			sessionsSettingsBuilder.setEndpoint("dialogflow.googleapis.com:443");
			SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
					.setCredentialsProvider(
							FixedCredentialsProvider.create(GoogleCredentials.fromStream(new FileInputStream(
									new File("E:\\DBTechHackathon Files\\hack-fruitsalad-2805a0bee0a7.json")))))
					.build();
			SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
			SessionName sessionName = SessionName.of(AppConstants.PROJECT_NAME, AppConstants.LOCATION,
					AppConstants.NAVIGATION_AGENT, sessionId);

			TextInput.Builder textInput = TextInput.newBuilder().setText(navigationPageName);
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).setLanguageCode("en-US").build();

			MatchIntentRequest matchIntent = MatchIntentRequest.newBuilder().setSession(sessionName.toString())
					.setQueryInput(queryInput).build();

			MatchIntentResponse matchIntentResponse = sessionsClient.matchIntent(matchIntent);

			if (matchIntentResponse.getMatchesCount() == 1
					&& !matchIntentResponse.getMatches(0).getMatchType().equals(MatchType.NO_MATCH)) {
				DetectIntentRequest detectIntentRequest = DetectIntentRequest.newBuilder()
						.setSession(sessionName.toString()).setQueryInput(queryInput).build();
				DetectIntentResponse detectIntentResponse = sessionsClient.detectIntent(detectIntentRequest);
				QueryResult queryResult = detectIntentResponse.getQueryResult();
				System.out.println(queryResult.getResponseMessages(0).getText().getText(0));
				responseMap.put(AppConstants.PAGE_TYPE, AppConstants.NAVIGATION_KEY);
				responseMap.put(AppConstants.RESPONSE_TEXT_KEY,
						queryResult.getResponseMessages(0).getText().getText(0));
//				System.out.println(queryResult.getResponseMessagesCount());
//				System.out.println(queryResult.getResponseMessages(queryResult.getResponseMessagesCount()-1));
			}
			return new ResponseEntity<>(responseMap, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
