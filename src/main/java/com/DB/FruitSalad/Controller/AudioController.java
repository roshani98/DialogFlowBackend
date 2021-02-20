package com.DB.FruitSalad.Controller;

import java.io.IOException;
import java.lang.System.Logger;

import com.google.cloud.dialogflow.v2.AudioEncoding;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.InputAudioConfig;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.protobuf.ByteString;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jdk.internal.jline.internal.Log;

@RestController
public class AudioController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Hello", HttpStatus.ACCEPTED);
    }

    @PostMapping("/audioTest")
    public ResponseEntity<byte[]> audioInputTest(@RequestPart("file") MultipartFile audioFile) {
        System.out.println("Before try");
        try {
            ByteString byteString = ByteString.copyFrom(audioFile.getBytes());
            System.out.println("Here\n"+byteString);
System.out.println(byteString.toStringUtf8());
            return new ResponseEntity<>(audioFile.getBytes(), HttpStatus.ACCEPTED);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/audioInput")
    public ResponseEntity<QueryResult> audioInput(@RequestPart("file") MultipartFile audioFile) throws IOException {
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            SessionName session = SessionName.of("MarysBikeShop", "45033770-9df7-f35e-707e-f3ad5e4992b7");
            System.out.println("Session Path: " + session.toString());

            // Note: hard coding audioEncoding and sampleRateHertz for simplicity.
            // Audio encoding of the audio content sent in the query request.
            AudioEncoding audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16;
            int sampleRateHertz = 16000;

            // Instructs the speech recognizer how to process the audio content.
            InputAudioConfig inputAudioConfig = InputAudioConfig.newBuilder().setAudioEncoding(audioEncoding) // audioEncoding
                                                                                                              // =
                                                                                                              // AudioEncoding.AUDIO_ENCODING_LINEAR_16
                    .setLanguageCode("en-US") // languageCode = "en-US"
                    .setSampleRateHertz(sampleRateHertz) // sampleRateHertz = 16000
                    .build();

            // Build the query with the InputAudioConfig
            QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(inputAudioConfig).build();

            // Read the bytes from the audio file
            byte[] inputAudio = audioFile.getBytes();

            // Build the DetectIntentRequest
            DetectIntentRequest request = DetectIntentRequest.newBuilder().setSession(session.toString())
                    .setQueryInput(queryInput).setInputAudio(ByteString.copyFrom(inputAudio)).build();

            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(request);

            // Display the query result
            QueryResult queryResult = response.getQueryResult();
            System.out.println("====================");
            System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
            System.out.format("Detected Intent: %s (confidence: %f)\n", queryResult.getIntent().getDisplayName(),
                    queryResult.getIntentDetectionConfidence());
            System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

            return new ResponseEntity<>(queryResult, HttpStatus.ACCEPTED);
        }
    }
}