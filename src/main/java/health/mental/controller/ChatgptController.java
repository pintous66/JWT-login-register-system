package health.mental.controller;

import health.gpt.model.ChatgptRequest;
import health.gpt.model.ChatgptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/gpt")
@Tag(name = "ChatGPT", description = "Endpoints para interagir com a API do ChatGPT")
public class ChatgptController {

    @Value("${chatgpt.model}")
    private String model;

    @Value("${chatgpt.api.url}")
    private String apiUrl;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private static RestTemplate restTemplate = new RestTemplate();

    //endpoint in the form of /gpt/ask?query=your_query
    @Operation(summary = "Ask a question to ChatGPT", description = "Ask to ChatGpt And Get Anwers.")
    @RequestMapping(value = "/ask", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ask(@RequestBody String query) {
        ChatgptRequest chatgptRequest = new ChatgptRequest(model, query);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        ChatgptResponse chatgptResponse = restTemplate.postForObject(apiUrl,
                new HttpEntity<>(chatgptRequest, headers),
                ChatgptResponse.class);
        return chatgptResponse.getChoices().get(0).getMessage().getContent();
    }


    /*
    @RequestMapping(value = "/getmeal",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMealWithCalories(@RequestBody String query) {

        ChatgptRequest chatgptRequest = new ChatgptRequest(model, PromptMeal + query);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);


        ChatgptResponse chatgptResponse = restTemplate.postForObject(apiUrl,
                new HttpEntity<>(chatgptRequest,headers),
                ChatgptResponse.class);


        return ResponseEntity.ok(chatgptResponse.getChoices().get(0).getMessage().getContent());


    }


     */
}
