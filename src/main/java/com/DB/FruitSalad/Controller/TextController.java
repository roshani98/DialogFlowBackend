package com.DB.FruitSalad.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TextController {
    @PostMapping("/textTesting")
    public ResponseEntity<String> textTest(@RequestParam("navigationPageName") String navigationPageName) {

        try {
            SessionsSettings.Builder sessionsSettingsBuilder = SessionsSettings.newBuilder();
            sessionsSettingsBuilder.setEndpoint("dialogflow.googleapis.com:443");
            SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                    .setCredentialsProvider(
                            FixedCredentialsProvider.create(GoogleCredentials.fromStream(new FileInputStream(
                                    new File("E:\\DBTechHackathon Files\\hack-fruitsalad-2805a0bee0a7.json")))))
                    .build();
            SessionsClient sessionsClient = SessionsClient.create(sessionsSettings);
            SessionName sessionName = SessionName.of("hack-fruitsalad", UUID.randomUUID().toString());
            // TextInput.Builder textInput = TextInput.newBuilder().setText(navigationPageName).setLanguageCode("en-US");
            // QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            // DetectIntentResponse response = sessionsClient.detectIntent(sessionName, queryInput);
            // QueryResult queryResult = response.getQueryResult();
            // System.out.println(queryResult.getFulfillmentText());
            return new ResponseEntity<>(sessionName.getSession(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
