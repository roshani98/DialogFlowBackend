package com.DB.FruitSalad.Controller;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TextController {
    @PostMapping("/textTesting")
    public ResponseEntity<Void> textTest(String text){
        try(SessionsClient sessionsClient = SessionsClient.create()){
            SessionName sessionName = SessionName.of("MarysBikeShop", "45033770-9df7-f35e-707e-f3ad5e4992b7");
            TextInput.Builder textInput =
            TextInput.newBuilder().setText(text).setLanguageCode("en-US");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse response = sessionsClient.detectIntent(sessionName, queryInput);
            QueryResult queryResult = response.getQueryResult();
            System.out.println(queryResult.getFulfillmentText());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
